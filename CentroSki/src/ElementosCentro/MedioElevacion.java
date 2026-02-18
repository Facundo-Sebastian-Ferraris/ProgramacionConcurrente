package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MedioElevacion {
  private final Molinete[][]
    molinetes;              //  ⛷️ Atributo del Medio

  private final Aerosilla aerosillas[] = new Aerosilla[2];


  private final Semaphore
    MUTEX_Aerosilla[];

  private int
    indice[];                 //  🔄 Alternar Molinetes

  private final AtomicInteger
    skiersEsperando;        //  ⛷️ Recurso de Control


  private boolean
    finalViaje[],             //  ✅ Indica si la silla está llena
    inicioViaje[];            //  🚂 Indica si la silla está en movimiento

  private AtomicBoolean abierto = new AtomicBoolean(false);

  // 🏗️ Constructor
  public MedioElevacion(int cantidadMolinetes, int id){
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




  public synchronized boolean esquiador_ingresar(boolean telepase, String nombreHilo, boolean arriba) throws InterruptedException{
    if (!abierto.get()) return false;


    int s = arriba ? 1 : 0;
    //  🎫 Asignacion de molinete
    int i = indice[s]++;
    indice[s] %= molinetes[s].length;


    //  ✅ Validar pase por molinete
    if(!molinetes[s][i].ingresar(telepase)) {    //  🚫 Terminar algoritmo si ya no es posible ingresar
      printGUI(nombreHilo + "no cuenta con telepase, entonces se va");
      return false;
    }
    printGUI(nombreHilo + " pasa y espera abordaje");

    //  Espera aerosilla
    skiersEsperando.incrementAndGet();
    esquiador_esperar(nombreHilo, arriba);    //  🎯 Modularizacion  donde entra monitores
    skiersEsperando.decrementAndGet();
    return true;
  }

  private void esquiador_esperar(String nombreHilo, boolean arriba)  throws InterruptedException{
    Aerosilla aero = buscarAerosilla(arriba);
    while( aero == null || !aero.subirse()){
        printGUI( nombreHilo + " esperando silla...");
        wait();
        printGUI( nombreHilo + " chequea si hay silla...");
        aero = buscarAerosilla(arriba);
    }
    System.out.println(nombreHilo +" se va de viaje!!!");
    while(aero.getArriba() == arriba){
      printGUI( nombreHilo + " no puede bajarse...");
      wait();
      printGUI( nombreHilo + " ve si se puede bajar...");
    }

    aero.bajarse();
    printGUI(nombreHilo + " baja silla");
  }


  //  BUSCA AEROSILLA DISPONIBLE PARA SUBIRSE
  private Aerosilla buscarAerosilla(boolean arriba){
    Aerosilla r = null;
    int i = 0;
    Aerosilla candidata;

    while (r == null && i < aerosillas.length) {
      candidata = aerosillas[i];
      if (candidata.esDisponible(arriba)) {
        r = candidata;
      }
      i++;
    }
    return r;
  }


  public synchronized void gestor_Aerosillas(String nombreHilo) throws InterruptedException{
    //  HABILITAR ABORDAJE EN SILLAS
    for (Aerosilla aerosilla : aerosillas) {
      aerosilla.habilitarAbordaje();
    }
    notifyAll();
    printGUI("\t" + nombreHilo + " habilita abordaje de las aerosillas");
    wait(3000);


    //  INICIAR VIAJE
    printGUI("\t" + nombreHilo + " inicia viaje");
    for (Aerosilla aerosilla : aerosillas) {
      aerosilla.iniciarViaje();
    }
    wait(3000);     //  3s de viaje

    // FINALIZAR VIAJE
    printGUI("\t" + nombreHilo + " finaliza viaje");
    for (Aerosilla aerosilla : aerosillas) {
      aerosilla.terminarViaje();
    }
    notifyAll();    //  Notificar para que se bajen de la aerosilla
    printGUI("\t" + nombreHilo + " hacen bajar a los skiers");
    wait(3000);     //  3s de para que se bajen
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

    public synchronized boolean hayPendientes(){
      boolean r = true;

      r = skiersEsperando.get()> 0;
      return r;
    }

    public synchronized boolean estaAbierto(){
      return abierto.get();
    }

    public synchronized boolean permaneceGestor(){
      // el gestor solo se puede retirar si se cierra y no haya gente adentro
      return abierto.get() || skiersEsperando.get() > 0;
    }

    public void cerrar(){
      abierto.set(false);
    }

    public void abrir(){
      abierto.set(true);
    }
}