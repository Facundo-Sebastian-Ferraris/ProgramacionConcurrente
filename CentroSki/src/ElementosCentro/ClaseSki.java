package ElementosCentro;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClaseSki {

    // Semaforos
    private final Semaphore
        claseFinalizada = new Semaphore(0); 

    // Flags para locks
    private boolean 
        alumnosListos = false;
    
    // Lock
    private final ReentrantLock mutex = new ReentrantLock(true);
    
    // Condiciones para Lock
    private final Condition
        barreraReparada = mutex.newCondition(),
        alumnosSuficientes = mutex.newCondition();

    
    // La barrera para permitir que 4 hilos esten en X tiempo
    private final CyclicBarrier 
        barrera = new CyclicBarrier(4,()->{notificarEntrenadores();});
    
    public ClaseSki() {
    }

    public void ingresarClase() throws InterruptedException {
            
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
            
        } catch (TimeoutException e) {
            repararBarrera();
        } catch (BrokenBarrierException e) {
            System.out.println("💥 Se rompió la barrera :(");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e; 
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
    
    private void notificarEntrenadores() {
        alumnosListos = true; 
        mutex.lock();
        try {
            alumnosSuficientes.signal();
        } finally {
            mutex.unlock();
        }
    }

    public void entrenador_Ensenar() throws InterruptedException{
        mutex.lock();
        try {
            while (!alumnosListos) {
                alumnosSuficientes.await();
            }
            alumnosListos = false;
            System.out.println("Iniciando Clase");
            Thread.sleep(1000);
            System.out.println("Finalizando Clase");
            claseFinalizada.release(4);
            barreraReparada.signalAll();
        } finally {
            mutex.unlock();
        }
    }
}