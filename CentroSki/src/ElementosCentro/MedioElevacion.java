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

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 *
 * @author facundo
 */
public class MedioElevacion {
    private String nombre;
    private Molinete molinetes[];

    private boolean abierto = false;
    private final Lock entrada = new ReentrantLock();
    private final Condition espera = entrada.newCondition();
    private Semaphore hacerFila;
    private int baseEspera;


    public MedioElevacion(String nombre, int cantidadMolinetes){
        
        if (cantidadMolinetes < 1 || cantidadMolinetes > 4) {
            throw new IllegalArgumentException("La cantidad de molinetes debe ser entre 1 y 4!");
        }
        this.nombre = nombre;
        this.molinetes = new Molinete[cantidadMolinetes];
        for (int i = 0; i < molinetes.length; i++) {
            molinetes[i] = new Molinete();
        }
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
    

    public void habilitar(){
        entrada.lock();
        try{
            abierto = true;
            espera.signalAll();
        } finally {
            entrada.unlock();
        }
    }

    public void deshabilitar(){
        entrada.lock();
        try{
            abierto = false;
        } finally {
            entrada.unlock();
        }
    }

    public void ingresar() throws InterruptedException{
        //ver si esta habilitado
        if (abierto) {
            //hacer cola pero sin esperar eternamente (tryacquire)
            hacerFila.acquire();
            Molinete molineteSeleccionado = elegirMolinete();
            hacerFila.release();
            
            molineteSeleccionado.accederMolinete();
            baseEspera++;

            
        }
        //si cierran los que estan en cola se deben retirar
        //al entrar, gestionar a quien habilitar el paso 
    }

    public Molinete elegirMolinete(){//Elige al que tiene menos ocupados
        int indice = 0;
        int pocos = molinetes[indice].getEsperando();
        for (int i = 1; i < molinetes.length; i++) {
            int numeroEsperando = molinetes[i].getEsperando();
            if(pocos > numeroEsperando){
                pocos = numeroEsperando;
                indice = i;
            }
        }
        return this.molinetes[indice];
    }

    public int getUsosTotal() {
        int r = 0;
        for (int i = 0; i < molinetes.length; i++) {
            r += this.molinetes[i].getUsosTotal();
        }
        return r;
    }

}
