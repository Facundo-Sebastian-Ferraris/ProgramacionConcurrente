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
        skiers_esperando;

    private final Semaphore RENDEVOUZ_instructor;
    private int cantidadEsperando;

    private AtomicInteger 
        clasesExitosas = new AtomicInteger(0),
        ingresos = new AtomicInteger(0),
        impaciencia = new AtomicInteger(0);

    private final CyclicBarrier RENDEVOUZ_NotificarProfesor;
    private final Semaphore RENDEVOUZ_FinalizadaClase;


    //  CONSTRUCTOR
    public ClaseSki(){
        mutex = new ReentrantLock();
        skiers_esperando = mutex.newCondition();
        cantidadEsperando = 0;

        RENDEVOUZ_NotificarProfesor = new CyclicBarrier(4, () ->{callbackBarrera();});
        RENDEVOUZ_FinalizadaClase = new Semaphore(0);
        RENDEVOUZ_instructor = new Semaphore(0);
    }


    public void esquiador_irClase(String nombreHilo) throws InterruptedException{
        ingresos.addAndGet(120);        //  Paga al ingresar
        boolean lograEsperar = true;    //  Auxiliar para determinar grupos


        //  Formacion de grupo
        mutex.lock();
        try {
            cantidadEsperando++;

            printGUI( nombreHilo + " esta interesado en instruirse (" + cantidadEsperando + ")");

            skiers_esperando.signalAll();      // resetea la "paciencia del hilo"
            while(cantidadEsperando < 4 && lograEsperar){
                lograEsperar = skiers_esperando.await(3, TimeUnit.SECONDS);
            }

            lograEsperar = cantidadEsperando == 4;

        } finally {
            mutex.unlock();
        }
        if (!lograEsperar){

            printGUI( nombreHilo + " se le agota la paciencia y se va");
            cantidadEsperando--;
            ingresos.addAndGet(-120);       //  Reembolso
            impaciencia.incrementAndGet();
            return;
        }

        // Cuando ya tenga el grupo formado ahora toca esperar al instructor
        try {
            RENDEVOUZ_NotificarProfesor.await();
        } catch (InterruptedException | BrokenBarrierException e1) {
            e1.printStackTrace();
        }

        printGUI( nombreHilo + " en clase!!!");
        RENDEVOUZ_FinalizadaClase.acquire();
        printGUI( nombreHilo + " finalizada clase!!!");

    }
    
    
    public void instructor_darClase(String nombreHilo) throws InterruptedException{
        printGUI( nombreHilo  + " espera a que haya grupo");
        RENDEVOUZ_instructor.acquire();
        printGUI( nombreHilo + " llegan alumnos y empieza la clase");        
        printGUI( nombreHilo + " espera a que lleguen los 4");
        printGUI( nombreHilo + " llegan los 4 y enseñan");
        Thread.sleep(5000);
        RENDEVOUZ_FinalizadaClase.release(4);
        clasesExitosas.incrementAndGet();
        

    }

    private void callbackBarrera(){
        printGUI("("+Thread.currentThread().getName()+")\tLibera a instructor" );
        mutex.lock();
        try{
            cantidadEsperando-=4;
            RENDEVOUZ_instructor.release();
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

    public int get_Impaciencia(){
        return impaciencia.get();
    }

    private void printGUI(String r){
        ImpresionGUI.print("Clases de Ski", r);
    }
}