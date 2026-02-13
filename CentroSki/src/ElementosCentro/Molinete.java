
package ElementosCentro;

/**
 *
 * @author facundo
 */
public class Molinete {
  private int usosTotal;
  public Molinete(){
    this.usosTotal = 0;
  }

  public synchronized boolean ingresar(boolean telepase) {
    if (telepase) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      usosTotal++;
    }
    return telepase;
  }

  public synchronized int getUsosTotal(){
    return usosTotal;
  }
}
