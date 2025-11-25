package ElementosCentro;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author facundo
 */

/**
 * Dinamica
 *  1) Entra alumno y espera a que esten todos para la clase
 *      1.a) Si estan todos, inicia la clase
 *      1.b) Si no estan todos y pasa X tiempo, se cancela la clase y se 
 *      liberan
 * 
 *  Cual es la informacion que puedo obtener?
 *      cantidad de espacios totales
 *      cantidad de alumnos esperando
 *      cantidad de clases exitosas
 *      cantidad de clases canceladas
 *
 *  Como simulo una clase iniciada?
 *      una vez que empieza la clase, debo contener los hilos dentro del 
 *      metodo durante un momento y luego liberarlos.
 * 
 *  Con que mecanismo de sinc voy a implementar?
 *      Cyclic Barrier donde se inicia algo tras tantos acquires o
 *      se cancela pasado un tiempo
**/
public class ClaseSki {
    // Atributos
    private final String nombreClase;
    private final int tiempo_milis;
    private int clasesExitosas;
    private int clasesCanceladas;
    private final AtomicInteger alumnosEnEspera = new AtomicInteger(0);
    
    
    //  Mecanismos de Sincronizacion
    
    ////    Mecanismo nuevo! : Atomic Boolean, es un mecanismo que me permite manejar un valor 
    ////    booleano (true/false) sin necesidad de usar sincronización explícita (synchronized)
    private final AtomicBoolean cancelada = new AtomicBoolean(false);
    
    ////    Mecanismo nuevo!: Barrera Ciclica, me permite determinar cuantos hilos quiero acumular,
    ////    para luego liberarlos, ademas de tener una excepcion de tiempo limite
    private final CyclicBarrier alumnos;

    //  Semaforos
    private final Semaphore rendevouz_alumno;
    

    //  Constructor
    public ClaseSki(String nombre, int tiempo_milis) {
        this.nombreClase = nombre;
        this.tiempo_milis = tiempo_milis;
        this.clasesExitosas = 0;
        this.clasesCanceladas = 0;
        
        this.rendevouz_alumno = new Semaphore(0);

        // Barrera Ciclica: indico que quiero que hayan 5 hilos esperando para liberarlos,
        // si se logra, entonces ejecutar el callback que indica que se logro dar la clase
        this.alumnos = new CyclicBarrier(5, () -> {
            System.out.println(nombreClase + " comienza!");
            clasesExitosas++;
        });
    }

    //  Metodos
    public void entrarClase() throws InterruptedException {
        // es necesario utilziar tryNcatch debido a que la barrera ciclica al terminar el tiempo
        // redirigira los hilos que estuvieron esperando a la excepcion TimeOut
        // y a los hilos que estaban por esperar, seran dirigidos a la exepcion de la barrera rota.
        
        //  Nota: para reparar la barrera se debe resetear la barrera (si se resetea con hilos 
        //  esperando, estos son llevados a la excepcion de barrera rota, asi que en lo posible
        //  queremos evitarlo)
        try {
            System.out.println(Thread.currentThread().getName() + " llegó a " + nombreClase);
            
            alumnosEnEspera.incrementAndGet();
            //  awaits()
            alumnos.await(tiempo_milis, TimeUnit.MILLISECONDS);
            System.out.println(Thread.currentThread().getName() + " participa en " + nombreClase);
        } catch (TimeoutException e) {
            if (alumnosEnEspera.decrementAndGet() == 0){
                clasesCanceladas++;
                alumnos.reset();
            }
        } catch (BrokenBarrierException | InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " se entera de la cancelación de " + nombreClase);
        }
    }

    public int getAlumnosEsperando(){
        return (int)alumnosEnEspera.get();
    }
    
    public int getClasesExitosas() {
        return clasesExitosas;
    }

    public int getClasesCanceladas() {
        return clasesCanceladas;
    }
}