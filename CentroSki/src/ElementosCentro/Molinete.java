/*
📍 Molinetes: Cada molinete tiene un lector de tarjetas que verifica los pases de los esquiadores y registra su uso.
Por ejemplo: el medio de elevación Silla Triple posee 3 molinetes que permiten
el acceso a 3 esquiadores por vez.
 */
package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author facundo
 */
public class Molinete {
    private final AtomicInteger usosTotal;
    private final Semaphore mutex_Acceso;
    private boolean habilitado = true;

    public Molinete(){
        mutex_Acceso = new Semaphore(1, true);
        usosTotal = new AtomicInteger(0);
    }
    
    public void accederMolinete() throws InterruptedException {
        mutex_Acceso.acquire();
        habilitado = false;
        usosTotal.incrementAndGet();
    }
    
    public void habilitarMolinete(){
            mutex_Acceso.release();
    }

    public int getUsosTotal() {
        return usosTotal.get();
    }
}
