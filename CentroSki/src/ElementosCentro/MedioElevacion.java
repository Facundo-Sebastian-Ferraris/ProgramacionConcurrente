package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MedioElevacion {
  private final Molinete[][]
    molinetes;              //  ⛷️ Atributo del Medio

  private final Aerosilla aerosillas[] = new Aerosilla[2];


  private final Semaphore
    MUTEX_Skiers,                  //  🚦 Mecanismo para exclusion mutua
    MUTEX_Aerosilla[];

  private int
    indice[];                 //  🔄 Alternar Molinetes

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

    aerosillas[0] = new Aerosilla(cantidadMolinetes, true);
    aerosillas[1] = new Aerosilla(cantidadMolinetes, false);

    molinetes = new Molinete[2][cantidadMolinetes];  // se usa el doble por el uso de los extremos
    iniciarMolinetes(cantidadMolinetes);            //⚙️ Modularizacion para iniciar cada molinete


    indice = new int[2];
    indice[0] = 0;
    indice[1] = 0;

    finalViaje = new boolean[2];
    finalViaje[0] = false;
    finalViaje[1] = false;


    inicioViaje = new boolean[2];
    inicioViaje[0] = false;
    inicioViaje[1] = false;
  }


  private void iniciarMolinetes(int cantidadMolinetes){
    for (int i = 0; i < molinetes.length; i++) {
        for (int j = 0; j < molinetes[0].length; j++) {
          molinetes[i][j] = new Molinete();
        }
    }
  }




  public boolean esquiador_ingresar(boolean telepase, String nombreHilo, boolean arriba) throws InterruptedException{
    int s = arriba ? 1 : 0;
    //  🎫 Asignacion de molinete

    // (0, 1, 2 ||  3, 4, 5 6)  s*indice[s]+
    MUTEX_Skiers.acquire();
    int i = indice[s]++;
    indice[s] %= aerosillas.length;
    MUTEX_Skiers.release();


    //  ✅ Validar pase por molinete
    if(!molinetes[s][i].ingresar(telepase)) {    //  🚫 Terminar algoritmo si ya no es posible ingresar
      printGUI(nombreHilo + "no cuenta con telepase, entonces se va");
      return false;
    }

    printGUI(nombreHilo + " pasa y espera abordaje");

    skiersEsperando.incrementAndGet();
    esquiador_esperar(nombreHilo, arriba);    //  🎯 Modularizacion  donde entra monitores
    skiersEsperando.decrementAndGet();
    return true;
  }

  private synchronized void esquiador_esperar(String nombreHilo, boolean arriba)  throws InterruptedException{
    Aerosilla aero = buscarAerosilla(arriba);
    while(!aero.subirse()){
        printGUI( nombreHilo + " esperando silla...");
        wait();
        printGUI( nombreHilo + " chequea si hay silla...");
        aero = buscarAerosilla(!arriba);
    }

    while(!aero.bajarse()) wait();   // ⏳ Hilo espera que termine el viaje

    printGUI(nombreHilo + " baja silla");
  }


  private Aerosilla buscarAerosilla(boolean arriba){
    int i = 0;
    while (aerosillas[i].getArriba() != arriba) {
      i = (i + 1) % aerosillas.length;
    }

    return aerosillas[i];
  }


  public synchronized void gestor_Aerosillas() throws InterruptedException{
    wait(3000);     //  3s de espera antes de iniciar viaje
    //  Iniciar viajes para las 2 aerosillas
    for (int i = 0; i < aerosillas.length; i++) {
     aerosillas[i].iniciarViaje();
    }
    wait(3000);     //  3s de viaje
    for (int i = 0; i < aerosillas.length; i++) {
      aerosillas[i].terminarViaje();
    }
    notifyAll();    //  Notificar para que se bajen de la aerosilla
    wait(3000);     //  3s de para que se bajen
    notifyAll();    //  Notificar para que se suban nuevos
  }


  // 📊 GETTERS
  public synchronized int getUsosTotal(){
    int r = 0;
    for (int i = 0; i < molinetes.length; i++) {
      for (int j = 0; j < molinetes[0].length; j++) {
        r += this.molinetes[i][j].getUsosTotal();
      }
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