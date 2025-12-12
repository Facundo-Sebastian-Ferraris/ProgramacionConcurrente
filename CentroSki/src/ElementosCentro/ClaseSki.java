package ElementosCentro;

import java.util.concurrent.BrokenBarrierException;
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
    private int instructoresLibres;
    private boolean hayGrupo;


    private final Semaphore RENDEVOUZ_LlegadaProfe;
    private final Semaphore RENDEVOUZ_FinalizadaClase;


    public ClaseSki(){
        mutex = new ReentrantLock();
        sonCuatro = mutex.newCondition();
        esperando = mutex.newCondition();
        RENDEVOUZ_LlegadaProfe = new Semaphore(0);
        RENDEVOUZ_FinalizadaClase = new Semaphore(0);

        cantidadEsperando = 0;
        instructoresLibres = 0;
        hayGrupo = false;
    }

    public void esquiador_irClase(String nombreHilo) throws InterruptedException{
        boolean lograEsperar = true;
        mutex.lock();
        try {
            System.out.println(nombreHilo +" esta interesado en instruirse");
            hayGrupo = ++cantidadEsperando >=4;
            esperando.signalAll(); // resetea la "paciencia del hilo"
            while(!hayGrupo && lograEsperar){
                lograEsperar = sonCuatro.await(2, TimeUnit.SECONDS);
            }
            
            if (!lograEsperar) {
                System.out.println(nombreHilo + " se le agota la paciencia y se va");
                hayGrupo = --cantidadEsperando >=4;
            }
            
        } finally {
            mutex.unlock();
        }
        
        // Cuando ya tenga el grupo formado ahora toca esperar al instructor
        RENDEVOUZ_LlegadaProfe.acquire();
        System.out.println(nombreHilo + " en clase!!!");
        RENDEVOUZ_FinalizadaClase.acquire();
         System.out.println(nombreHilo + " finalizada clase!!!");

    }
    
    
    public void callBackGrupo(){
        mutex.lock();
        try{
            esperando.signal();
        } finally {
            mutex.unlock();
        }
    }
    
        public void callBackClase(){
        mutex.lock();
        try{
            esperando.signal();
        } finally {
            mutex.unlock();
        }
    }

    public void instructor_darClase(String nombreHilo) throws InterruptedException{
        mutex.lock();
        try {
            instructoresLibres++;
            while (!hayGrupo) {
                esperando.await();
            }
            cantidadEsperando -= 4;
            hayGrupo = cantidadEsperando >=4;
            instructoresLibres--;
        } finally {
            mutex.unlock();
        }

        RENDEVOUZ_LlegadaProfe.release(4);
        Thread.sleep(5000);
        RENDEVOUZ_FinalizadaClase.release(4);


    }


}