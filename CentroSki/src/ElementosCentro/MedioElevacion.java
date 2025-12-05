/*
Medios de Elevación 🚡

El complejo cuenta con 4 medios de elevación, y cada uno tiene un grupo de n molinetes (donde n puede variar entre 1 y 4)
que dan acceso al medio a los esquiadores.

📊 Contador: La suma del uso de todos los molinetes de un medio determina la cantidad de veces que fue utilizado.
🕒 Horario: Los medios de elevación están habilitados desde las 10:00 hasta las 17:00, durante las cuales los esquiadores tienen acceso ilimitado.
🎿 Esquiadores: Cada esquiador experimentado esquía por un tiempo, utiliza los medios de elevación, descansa, visita la confitería y continúa esquiando.

 */
package ElementosCentro;
import static centroski.ANSI_Colors.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author facundo
 */
public class MedioElevacion {

    //atributos
    private String nombre;
    private Molinete molinetes[];
    private int indiceMolinetes;
    private int cantidadSubidos;
    private AtomicBoolean[] espacios;
    
    //mecanismos
    private final Semaphore MUTEX_ingreso;
    private Lock espacioAerosilla = new ReentrantLock();
    private Condition esperarAerosilla = espacioAerosilla.newCondition();
    private Semaphore RENDEVOUZ_liberarEsquiadores = new Semaphore(0);
    private CyclicBarrier barrera_espera = new CyclicBarrier(this.molinetes.length);


    public MedioElevacion(String nombre, int cantidadMolinetes){
        //Excepcion
        if (cantidadMolinetes < 1 || cantidadMolinetes > 4) {
            throw new IllegalArgumentException("La cantidad de molinetes debe ser entre 1 y 4!");
        }

        //Atributos
        this.nombre = nombre;
        this.molinetes = new Molinete[cantidadMolinetes];
        for (int i = 0; i < molinetes.length; i++) {
            molinetes[i] = new Molinete();
        }

        this.espacios = new AtomicBoolean[cantidadMolinetes];
        for (int i = 0; i < espacios.length; i++) {
            espacios[i] = new AtomicBoolean(false);
        }
        //Mecanismos
        indiceMolinetes = 0;
        cantidadSubidos = 0;
        MUTEX_ingreso = new Semaphore(1);

    }

    @Override
    public String toString(){
        String r = GREEN + "Nombre:\t"+ rainbow(this.nombre) +
                CYAN + "\nNumero de Molinetes:\t" + YELLOW + this.molinetes.length;
        return r;
    }

    public String getNombre() {
        return nombre;
    }
    



    public void esquiador_ingresar() throws InterruptedException{
        //ver si esta habilitado
        MUTEX_ingreso.acquire();
        int i = (indiceMolinetes++)%molinetes.length;
        MUTEX_ingreso.release();


        this.molinetes[i].accederMolinete();
        while (true) {
            
            try{
                barrera_espera.await(2, TimeUnit.SECONDS);
            } catch(TimeoutException e){
                molinetes[i].habilitarMolinete();
                barrera_espera.reset();
                break;
            }catch(BrokenBarrierException e){
                
            }
        }

        //si cierran los que estan en cola se deben retirar
        //al entrar, gestionar a quien habilitar el paso 
    }

    public int getUsosTotal() {
        int r = 0;
        for (int i = 0; i < molinetes.length; i++) {
            r += this.molinetes[i].getUsosTotal();
        }
        return r;
    }

}
