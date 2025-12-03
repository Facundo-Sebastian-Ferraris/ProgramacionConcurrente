package ElementosCentro;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClaseSki {

    // Atributos
    private String nombreClase;
    private final AtomicInteger clasesExitosas = new AtomicInteger(0);

    // Semaforos
    private final Semaphore
        claseFinalizada = new Semaphore(0); 

    // Flags para locks
    private boolean 
        alumnosListos = false,
        entrenadorRetirandose = false;
    
    private AtomicBoolean
        flag_RepararBarrera = new AtomicBoolean(false);
    
    // Lock
    private final ReentrantLock mutex = new ReentrantLock(true);
    
    // Condiciones para Lock
    private final Condition
        barreraReparada = mutex.newCondition(),
        alumnosSuficientes = mutex.newCondition();

    
    // La barrera para permitir que 4 hilos esten en X tiempo
    private final CyclicBarrier 
        barrera = new CyclicBarrier(4,()->{esquiador_CallBack();});
    
    public ClaseSki(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    // ======   DINAMICA   ======
    /*
        Instructor
        1- Un instructor espera en cabina a que hayan un grupo de 4 alumnos
        2- Un instructor se le avisa que ya hay un grupo completo
        3- El instructor realiza la clase
        4- El instructor concluye la clase y libera a los esquiadores
        5- El instructor vuelve a la cabina

        Esquiador
        1- Un esquiador esta en espera para formar un grupo de 4 incluyendolo
            Escenarios:
                a. Se le agota la paciencia y se retira
                    a.1.    La barrera se rompe y debe repararse
                        Peligros:
                            i.  Multiples hilos entrantes recaigan en Broken hasta que se repare
                    a.2.    Si estan interesados

                b. Logra formar el grupo
                    b.1.    Uno del grupo avisa al instructor que el grupo ya se formo
                    b.2.    El grupo recibe la clase hasta ser liberado por el instructor
                    b.3.    El grupo es liberado por el instructor y se retiran
     */

    //  ======  ESQUIADORES ======

    public void esquiador_ingresarClase(String nombreHilo) throws InterruptedException {
        int intentos = 0;
        boolean exito = false;
        while (intentos <3 && !exito ) {
            
            mutex.lock();
            try {
                while (barrera.isBroken()) {
                    barreraReparada.await();
                }
            } finally {
                mutex.unlock();
            }
            try {
                barrera.await(2, TimeUnit.SECONDS);
                claseFinalizada.acquire();
                exito = true;
                
            } catch (TimeoutException e) {
                System.out.println("\t\tTIME OUT!!!");
                System.out.println( nombreHilo +" se canso de esperar la clase " + nombreClase + " y se retira" );
                repararBarrera();
            } catch (BrokenBarrierException e) {
                System.out.println( nombreHilo +" no pude realizar la clase " + nombreClase + " por cancelacion" );
            } 
            intentos++;
        }
    }

    private void repararBarrera() {
        mutex.lock();
        try {
            if (barrera.isBroken()) {
                barrera.reset(); 
            }
            barreraReparada.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    private void esquiador_CallBack(){
        System.out.println("====== Clase " + nombreClase + " inicia! ======");
        notificarInstructores();
    }

    private void notificarInstructores() {
        alumnosListos = true; 
        mutex.lock();
        try {
            clasesExitosas.incrementAndGet();
            alumnosSuficientes.signal();
        } finally {
            mutex.unlock();
        }
    }

    
    //  ======  INSTRUCTORES    ======

    

    public void instructor_Ensenar(String nombreHilo) throws InterruptedException{
        mutex.lock();
        try {
            while (!alumnosListos) {
                alumnosSuficientes.await();
            }            
            alumnosListos = false;
            claseFinalizada.release(4);
        } finally {
            mutex.unlock();
        }
    }

    public void retirarEntrenadores(){
        mutex.lock();
        try{
            entrenadorRetirandose = true;

        } finally {
            mutex.unlock();
        }
    }

    public void habilitarEntrenadores(){
        mutex.lock();
        try{
            entrenadorRetirandose = false;
        } finally {
            mutex.unlock();
        }
    }

    public int getClasesExitosas(){
        return clasesExitosas.get();
    }
}