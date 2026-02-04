package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MedioElevacion {
  private final Molinete[] 
    molinetes;                  //  Atributo del Medio
  
  
  private final Semaphore 
    MUTEX = new Semaphore(1);   //  Mecanismo para exclusion mutua
  

  private int 
    sillasDisponibles,          // Sillas disponibles
    indice;                     //  Para alternar los molinetes en los que entrara cada persona

  private final AtomicInteger
    personasEsperando = new AtomicInteger();
  

  private boolean
    finalViaje,             // Indica si la silla está llena
    inicioViaje;          // Indica si la silla está en movimiento (no disponible temporalmente)


  // Constructor
  public MedioElevacion(int cantidadMolinetes){
    molinetes = new Molinete[cantidadMolinetes];
    iniciarMolinetes(cantidadMolinetes);            //Modularizacion para iniciar cada molinete
    

    sillasDisponibles = molinetes.length;
    indice = 0;
    finalViaje = false;
    inicioViaje = false;
  }
  

  private void iniciarMolinetes(int cantidadMolinetes){
    for (int i = 0; i < molinetes.length; i++) {
        molinetes[i] = new Molinete();
    }
  }


  public void esquiador_ingresar(boolean telepase, String nombreHilo) throws InterruptedException{
    //  Asignacion de molinete
    MUTEX.acquire();
      int i = indice++;                           //  Obtencion de molinete
      indice %= this.molinetes.length;            //  Ciclar indice [0, N], N = cantidad molinetes
    MUTEX.release();


    //  Validar pase por molinete
    if(!molinetes[i].ingresar(telepase)) {        //  Terminar algoritmo si ya no es posible ingresar
      printGUI(nombreHilo + "no cuenta con telepase, entonces se va");  //  Mensaje de exito
      return;
    }  

    printGUI(nombreHilo + " pasa y espera abordaje");  //  Mensaje de exito

    personasEsperando.incrementAndGet();
    esquiador_esperar(nombreHilo);                //  Modularizacion  donde entra monitores 
    personasEsperando.decrementAndGet();
  }
  

  private synchronized void esquiador_esperar(String nombreHilo)  throws InterruptedException{


    //  Monitor While 🖥️:
    //  Hilos esperan si ocurre cualquiera de los casos:
    //    1.  Si no hay sillas para subirse
    //    2.  Si la aerosilla se va
    while(sillasDisponibles == 0 || inicioViaje){
      if (sillasDisponibles == 0) printGUI("No hay sillas disponibles!");
      if (inicioViaje) printGUI("la aerosilla se va");
      

        printGUI( nombreHilo + " esperando silla...");
        wait();
        printGUI( nombreHilo + " chequea si hay silla...");
    }
    

    printGUI(nombreHilo + " sube silla");
    
    
    sillasDisponibles--;              //  Hilo sube a silla
    while(!finalViaje) wait();    //  Hilo espera que termine el viaje
    sillasDisponibles++;              //  Hilo termina de viajar y se baja
    
    printGUI(nombreHilo + " baja silla");
  }


  public synchronized void embarcador_DarSilla(String nombreHilo) throws InterruptedException{
    inicioViaje = false;    //  habilita silla para abordar hilos
    printGUI("\t" + nombreHilo + " <<  setea silla en movimiento a "+ inicioViaje);

    finalViaje = false;     //  indica trayecto de hilos a terminar
    printGUI("\t" + nombreHilo + " << despierta hilos");

    notifyAll();            //  despierta todos los hilos a subirse

    printGUI("\t" + nombreHilo + " << espera 1000");
    wait(1000);             //  1s tolerancia
    printGUI("\t" + nombreHilo + " >> pasaron 1000 y ahora empieza viaje con "+ (molinetes.length-sillasDisponibles));
    inicioViaje = true;     //  silla se mueve, ningun hilo puede subirse ahora


    wait(1000);             //  1s de viaje
    printGUI("\t" + nombreHilo + " >> termina viaje y avisa que se vayan");
    finalViaje = true;      //  termina viaje


    notifyAll();            //  habilita silla para que se bajen
    wait(1000);             //  1s de tolerancia
  }


  public synchronized int getUsosTotal(){
    int r = 0;
    for (int i = 0; i < molinetes.length; i++) {
        r += this.molinetes[i].getUsosTotal();
    }
    return r;
  }

  public synchronized int getSillasDisponibles(){
    return sillasDisponibles;
  }

  public int getPersonasEsperando(){
    return personasEsperando.get();
  }

  public int getNumMolinetes(){
    return molinetes.length;
  }

   private void printGUI(String r){
        ImpresionGUI.print( "Medio Elevacion", r);
    }
}