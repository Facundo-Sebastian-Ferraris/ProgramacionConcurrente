package ElementosCentro;

import java.util.concurrent.Semaphore;

public class MedioElevacion {
  
  private final Molinete[] 
    molinetes;                  //  Atributo del Medio
  
  
  private final Semaphore 
    MUTEX = new Semaphore(1);   //  Mecanismo para exclusion mutua
  
  private int 
    esquiadoresEnEspera,        // Contador de esquiadores esperando
    sillasDisponibles,          // Sillas disponibles
    indice;                     //  Para alternar los molinetes en los que entrara cada persona
  
  private boolean
    viajeTerminado,             // Indica si la silla está llena
    sillaEnMovimiento;          // Indica si la silla está en movimiento (no disponible temporalmente)


  // Constructor
  public MedioElevacion(int cantidadMolinetes){
    //INICIO MOLINETES
    molinetes = new Molinete[cantidadMolinetes];
    iniciarMolinetes(cantidadMolinetes);            //Modularizacion para iniciar cada molinete
    
    sillasDisponibles = molinetes.length;
    indice = 0;
    esquiadoresEnEspera = 0;
    esquiadoresASubir = 0;
    viajeTerminado = false;
    sillaEnMovimiento = false;
  }
  
  private void iniciarMolinetes(int cantidadMolinetes){
    for (int i = 0; i < molinetes.length; i++) {
        molinetes[i] = new Molinete();
    }
  }


  public void esquiador_ingresar(boolean telepase, String nombreHilo) throws InterruptedException{
    //  Asignacion de molinete
    MUTEX.acquire();
    int i = indice++;                   //  Obtencion de molinete
    indice %= this.molinetes.length;    //  Ciclar indice [0, N], N = cantidad molinetes
    MUTEX.release();


    //  Validar pase por molinete
    if(!molinetes[i].ingresar(telepase)) return;  //  Terminar algoritmo si ya no es posible ingresar
    System.out.println(nombreHilo + " pasa y espera abordaje");  //  Mensaje de exito


    esquiador_esperar(nombreHilo);    //  Modularizacion  donde entra monitores 
  }
  
  private synchronized void esquiador_esperar(String nombreHilo)  throws InterruptedException{
    //  Monitor While 🖥️:
    //  Hilos esperan si ocurre cualquiera de los casos:
    //    1.  Si no hay sillas para subirse
    //    2.  Si la aerosilla se va
    while(sillasDisponibles == 0 || sillaEnMovimiento){
        System.out.println(nombreHilo + " esperando silla...");
        wait();
    }
    
    sillasDisponibles--;              //  Hilo sube a silla
    while(!viajeTerminado) wait();    //  Hilo espera que termine el viaje
    sillasDisponibles++;              //  Hilo termina de viajar y se baja
    
  }

  public synchronized void embarcador_DarSilla() throws InterruptedException{
    sillaEnMovimiento = false;    //  habilita silla para abordar hilos
    viajeTerminado = false;       //  indica trayecto de hilos a terminar
    notifyAll();                  //  despierta todos los hilos a subirse

    Thread.sleep(1000);           //  1s tolerancia
    sillaEnMovimiento = true;     //  silla se mueve, ningun hilo puede subirse ahora

    Thread.sleep(1000);           //  1s de viaje
    viajeTerminado = true;        //  termina viaje

    notifyAll();                  //  habilita silla para que se bajen
    Thread.sleep(1000);           //  1s de tolerancia
  }



  public synchronized int getUsosTotal(){
    int r = 0;
    for (int i = 0; i < molinetes.length; i++) {
        r += this.molinetes[i].getUsosTotal();
    }
    return r;
  }
  
  // Métodos para verificar estado (útiles para debugging)
  public synchronized int getEsquiadoresEnEspera() {
      return esquiadoresEnEspera;
  }
}