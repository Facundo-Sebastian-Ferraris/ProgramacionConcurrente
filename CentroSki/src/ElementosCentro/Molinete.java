/*
📍 Molinetes: Cada molinete tiene un lector de tarjetas que verifica los pases de los esquiadores y registra su uso.
Por ejemplo: el medio de elevación Silla Triple posee 3 molinetes que permiten
el acceso a 3 esquiadores por vez.
 */
package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author facundo
 */
public class Molinete {
    private final AtomicInteger usosTotal;
    private final AtomicInteger esperando;
    private final Semaphore mutex_Acceso;

    public Molinete(){
        mutex_Acceso = new Semaphore(1);
        usosTotal = new AtomicInteger(0);
        esperando = new AtomicInteger(0);
    }
    
    public void accederMolinete() throws InterruptedException {
        esperando.incrementAndGet();
        boolean r = mutex_Acceso.tryAcquire(5, TimeUnit.SECONDS);
        if (r) {
            usosTotal.incrementAndGet();
            esperando.decrementAndGet();
        }
        return r;
    }
    
    public void habilitarMolinete(){
        mutex_Acceso.release();
    }

    
    public int getEsperando() {
        return esperando.get();
    }

    public int getUsosTotal() {
        return usosTotal.get();
    }
}
