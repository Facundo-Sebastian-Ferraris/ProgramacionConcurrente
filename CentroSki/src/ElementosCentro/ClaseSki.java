package ElementosCentro;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ClaseSki {
    private final Lock mutex;
    private final Condition 
        intructores_esperando,
        skiers_esperando;

    private final Semaphore GENERIC_Esperando;
    private int cantidadEsperando;

    private AtomicInteger 
        clasesExitosas = new AtomicInteger(0),
        ingresos = new AtomicInteger(0);

    private final CyclicBarrier RENDEVOUZ_NotificarProfesor;
    private final Semaphore RENDEVOUZ_FinalizadaClase;


    //  CONSTRUCTOR
    public ClaseSki(){
        mutex = new ReentrantLock();
        intructores_esperando = mutex.newCondition();
        skiers_esperando = mutex.newCondition();
        cantidadEsperando = 0;

        GENERIC_Esperando = new Semaphore(4);
        RENDEVOUZ_NotificarProfesor = new CyclicBarrier(4, () ->{callbackBarrera();});
        RENDEVOUZ_FinalizadaClase = new Semaphore(0);
    }


    public void esquiador_irClase(String nombreHilo) throws InterruptedException{
        ingresos.addAndGet(120);        //  Paga al ingresar
        boolean lograEsperar = true;    //  Auxiliar para determinar grupos
        
        GENERIC_Esperando.acquire();
        
        
        //  Formacion de grupo
        mutex.lock();
        try {
            cantidadEsperando++;
            ImpresionGUI.print("Clases de Ski", nombreHilo + " esta interesado en instruirse (" + cantidadEsperando + ")");
            
            skiers_esperando.signalAll();      // resetea la "paciencia del hilo"
            while(cantidadEsperando < 4 && lograEsperar){
                lograEsperar = skiers_esperando.await(2, TimeUnit.SECONDS);
            }


            if (cantidadEsperando == 4) {
                lograEsperar = true;
            }

        } finally {
            mutex.unlock();
        }
        if (!lograEsperar){
            cantidadEsperando--;
            GENERIC_Esperando.release();
            ingresos.addAndGet(-120);       //  Reembolso
            return;
        }      //  Despues de liberar el lock se retira


        //  
        // Cuando ya tenga el grupo formado ahora toca esperar al instructor
        try {
            RENDEVOUZ_NotificarProfesor.await();
        } catch (InterruptedException | BrokenBarrierException e1) {
            e1.printStackTrace();
        }

        ImpresionGUI.print("Clases de Ski", nombreHilo + " en clase!!!");
        RENDEVOUZ_FinalizadaClase.acquire();
        ImpresionGUI.print("Clases de Ski", nombreHilo + " finalizada clase!!!");

    }
    
    
    public void instructor_darClase(String nombreHilo) throws InterruptedException{
        mutex.lock();
        try {
            ImpresionGUI.print("Clases de Ski", nombreHilo  + " espera a que haya grupo");


            intructores_esperando.await();
            ImpresionGUI.print("Clases de Ski", nombreHilo + " llegan alumnos y empieza la clase");
            cantidadEsperando-=4;
           
        } finally {
            mutex.unlock();
        }
        
        ImpresionGUI.print("Clases de Ski", nombreHilo + " espera a que lleguen los 4");
        ImpresionGUI.print("Clases de Ski", nombreHilo + " llegan los 4 y enseñan");
        Thread.sleep(5000);
        RENDEVOUZ_FinalizadaClase.release(4);
        clasesExitosas.incrementAndGet();
        

    }

    private void callbackBarrera(){
        ImpresionGUI.print("Clases de Ski","("+Thread.currentThread().getName()+")\tLibera a instructor" );
        GENERIC_Esperando.release(4);
        mutex.lock();
        try{
            intructores_esperando.signal();
        }finally{
            mutex.unlock();
        }

    }

    public int get_ClasesExitosas(){
        return  clasesExitosas.get();
    }

    public int get_Ingresos(){
        return ingresos.get();
    }
}