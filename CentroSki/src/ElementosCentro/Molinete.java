
package ElementosCentro;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author facundo
 */
public class Molinete {
    private final AtomicInteger usosTotal;
  public Molinete(){
    this.usosTotal = new AtomicInteger(0);
  }

  public boolean ingresar(boolean telepase) {
    if (telepase) {
      usosTotal.incrementAndGet();        
    }
    return telepase;
  }

  public int getUsosTotal(){
    return usosTotal.get();
  }
}
