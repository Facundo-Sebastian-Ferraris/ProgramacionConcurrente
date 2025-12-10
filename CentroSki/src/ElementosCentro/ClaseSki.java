package ElementosCentro;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClaseSki {

    private final Lock mutex;
    private final Condition sonCuatro;
    private final Condition esperando;

    private int cantidadEsperando;
    private boolean hayGrupo;
    private CyclicBarrier barrera = new CyclicBarrier(4,()->{callBack();});
    private final Semaphore GENERIC_transcursoClase;

    public ClaseSki(){
        mutex = new ReentrantLock();
        sonCuatro = mutex.newCondition();
        esperando = mutex.newCondition();
        GENERIC_transcursoClase = new Semaphore(0);
        cantidadEsperando = 0;
        hayGrupo = false;
    }

    public void esquiador_irClase(String nombreHilo) throws InterruptedException, BrokenBarrierException{

        boolean espera = true;
        mutex.lock();
        try{
            if (++cantidadEsperando == 4) {
                sonCuatro.signalAll();
                cantidadEsperando -=4;
            } else {
                espera = sonCuatro.await(4, TimeUnit.SECONDS);
                if (!espera) {
                    cantidadEsperando--;
                }
            }
        } finally {
            mutex.unlock();
        }

        if (espera) {
            barrera.await();
            GENERIC_transcursoClase.acquire();
        }
    }

    public void callBack(){
        mutex.lock();
        try{
            hayGrupo = true;
            esperando.signal();
        } finally {
            mutex.unlock();
        }
    }

    public void instructor_darClase(String nombreHilo) throws InterruptedException{
        mutex.lock();
        try {
            while (!hayGrupo) {
                esperando.await();
            }
            hayGrupo = false;
        } finally {
            mutex.unlock();
        }

        Thread.sleep(5000);
        GENERIC_transcursoClase.release(4);
    }


}