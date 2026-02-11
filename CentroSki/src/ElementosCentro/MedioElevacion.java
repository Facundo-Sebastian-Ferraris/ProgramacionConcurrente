package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MedioElevacion {
  private final Molinete[]
    molinetes;              //  ⛷️ Atributo del Medio


  private final Semaphore
    MUTEX_Skiers,                  //  🚦 Mecanismo para exclusion mutua
    MUTEX_Aerosilla[];

  private int
    sillasDisponibles[],    //  🪑 Sillas disponibles
    indice;                 //  🔄 Alternar Molinetes

  private final AtomicInteger
    skiersEsperando;        //  ⛷️ Recurso de Control


  private boolean
    finalViaje[],             //  ✅ Indica si la silla está llena
    inicioViaje[];            //  🚂 Indica si la silla está en movimiento


  // 🏗️ Constructor
  public MedioElevacion(int cantidadMolinetes){
    MUTEX_Skiers = new Semaphore(1);
    MUTEX_Aerosilla = new Semaphore[2];
    MUTEX_Aerosilla[0] = new Semaphore(1);
    MUTEX_Aerosilla[1] = new Semaphore(1);
    skiersEsperando = new AtomicInteger();


    molinetes = new Molinete[cantidadMolinetes];
    iniciarMolinetes(cantidadMolinetes);            //⚙️ Modularizacion para iniciar cada molinete


    sillasDisponibles = new int[2];
    sillasDisponibles[0] = molinetes.length;
    sillasDisponibles[1] = molinetes.length;
    indice = 0;


    finalViaje = new boolean[2];
    finalViaje[0] = false;
    finalViaje[1] = false;
    

    inicioViaje = new boolean[2];
    inicioViaje[0] = false;
    inicioViaje[1] = false;
  }


  private void iniciarMolinetes(int cantidadMolinetes){
    for (int i = 0; i < molinetes.length; i++) {
        molinetes[i] = new Molinete();
    }
  }




  public boolean esquiador_ingresar(boolean telepase, String nombreHilo, boolean subida) throws InterruptedException{
    //  🎫 Asignacion de molinete
    MUTEX_Skiers.acquire();
      int i = indice++;                       //  🎯 Obtencion de molinete
      indice %= this.molinetes.length;        //  🔄 Ciclar indice [0, N], N = cantidad molinetes
    MUTEX_Skiers.release();


    //  ✅ Validar pase por molinete
    if(!molinetes[i].ingresar(telepase)) {    //  🚫 Terminar algoritmo si ya no es posible ingresar
      printGUI(nombreHilo + "no cuenta con telepase, entonces se va");
      return false;
    }
 
    printGUI(nombreHilo + " pasa y espera abordaje");

    skiersEsperando.incrementAndGet();
    esquiador_esperar(nombreHilo, subida);    //  🎯 Modularizacion  donde entra monitores
    skiersEsperando.decrementAndGet();
    return true;
  }

  private synchronized void esquiador_esperar(String nombreHilo, boolean subida)  throws InterruptedException{
    int i = (subida)?1:0;


    // 🖥️ Monitor While:
    // 😴 Hilos esperan si ocurre cualquiera de los casos:
    //    1. ❌ Si no hay sillas para subirse
    //    2. 🚂 Si la aerosilla se va
    while(sillasDisponibles[i] == 0 || inicioViaje[i]){
      if (sillasDisponibles[i] == 0) printGUI("No hay sillas disponibles!");
      if (inicioViaje[i]) printGUI("la aerosilla se va");


        printGUI( nombreHilo + " esperando silla...");
        wait();
        printGUI( nombreHilo + " chequea si hay silla...");
    }


    printGUI(nombreHilo + " sube silla");


    sillasDisponibles[i]--;          // 📈 Hilo sube a silla
    while(!finalViaje[i]) wait();    // ⏳ Hilo espera que termine el viaje
    sillasDisponibles[i]++;          // 📉 Hilo termina de viajar y se baja

    printGUI(nombreHilo + " baja silla");
  }




  public synchronized void aerosilla_Viaje(String nombreHilo, boolean subida) throws InterruptedException{
    int i = (subida)?1:0;

    MUTEX_Aerosilla[i].acquire();
    inicioViaje[i] = false;   // 🟢 habilita silla para abordar hilos
    printGUI("\t" + nombreHilo + " <<  setea silla en movimiento a "+ inicioViaje[i]);

    finalViaje[i] = false;    // 🔄 indica trayecto de hilos a terminar
    printGUI("\t" + nombreHilo + " << despierta hilos");

    notifyAll();              // 📢 despierta todos los hilos a subirse

    printGUI("\t" + nombreHilo + " << espera 1000");
    wait(1000);               // ⏱️ 1s tolerancia
    printGUI("\t" + nombreHilo + " >> pasaron 1000 y ahora empieza viaje con " + (molinetes.length-sillasDisponibles[i]));
    inicioViaje[i] = true;    // 🚫 silla se mueve, ningun hilo puede subirse ahora


    wait(1000);               // 🕐 1s de viaje
    printGUI("\t" + nombreHilo + " >> termina viaje y avisa que se vayan");
    finalViaje[i] = true;     // ✅ termina viaje


    notifyAll();              // 📢 habilita silla para que se bajen
    wait(1000);               // ⏱️ 1s de tolerancia
    MUTEX_Aerosilla[i].release();
  }



  // 📊 GETTERS
  public synchronized int getUsosTotal(){
    int r = 0;
    for (int i = 0; i < molinetes.length; i++) {
        r += this.molinetes[i].getUsosTotal();
    }
    return r;
  }


  public int getskiersEsperando(){
    return skiersEsperando.get();
  }

  public int getNumMolinetes(){
    return molinetes.length;
  }

   private void printGUI(String r){
        ImpresionGUI.print( "Medio Elevacion", r);
    }

    public int getPersonasEsperando(){
      return skiersEsperando.get();
    }
}