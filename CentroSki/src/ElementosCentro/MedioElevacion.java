/*
Sistema de Pases para el ingreso de esquiadores
*/
package ElementosCentro;

import java.util.concurrent.Semaphore;

/**
 *
 * @author facundo
 */
public class MedioElevacion {

  private final Molinete[] molinetes;

  private int indice;
  private final Semaphore MUTEX_indice;
  private final Semaphore GENERIC_esperaSilla;
  private final Semaphore GENERIC_bajarSilla;

  public MedioElevacion(int cantidadMolinetes){
    molinetes = new Molinete[cantidadMolinetes];
    for (int i = 0; i < molinetes.length; i++) {
        molinetes[i] = new Molinete();
    }
    MUTEX_indice = new Semaphore(1, true);
    GENERIC_esperaSilla = new Semaphore(0, true);
    GENERIC_bajarSilla = new Semaphore(0, true);
    indice = 0;
  }

  public void esquiador_ingresar(boolean telepase, String nombreHilo) throws InterruptedException{
    int i;
    MUTEX_indice.acquire();
    i = indice++;
    indice %= this.molinetes.length;
    MUTEX_indice.release();
    if(pasarMolinete(i, telepase)){
        System.out.println(nombreHilo + " logra pasar y espera para subirse");
        GENERIC_esperaSilla.acquire();
        System.out.println(nombreHilo + " en aerosilla");
        GENERIC_bajarSilla.acquire();
        System.out.println(nombreHilo + " se baja de la aerosilla");

    } else {
        System.out.println(nombreHilo + " no logro pasar por no tener pase");
    }
  }

  public void embarcador_DarSilla() throws InterruptedException{
    Thread.sleep(300);
    GENERIC_esperaSilla.release(this.molinetes.length);
    Thread.sleep(300);//Timeout a que se suban los esquiadores

    int
        noSubidos = GENERIC_esperaSilla.drainPermits(), //impide que se suban mas
        subidos = this.molinetes.length - noSubidos;

    GENERIC_bajarSilla.release
    
  }

  private boolean pasarMolinete(int i, boolean telepase){
    return this.molinetes[i].ingresar(telepase);
  }

  public int getUsosTotal(){
    int r = 0;
    for (int i = 0; i < molinetes.length; i++) {
        r += this.molinetes[i].getUsosTotal();
    }
    return r;
  }
}
