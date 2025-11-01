/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ElementosCentro;

import java.util.concurrent.Semaphore;

/**
 *
 * @author facundo
 */

// Es una confiteria con:
// Capacidad de hasta 100 personas
// 1 Caja
// 2 mostradores para servir comida
// 1 mostrador para servir postre

/**
 * Premisa:
 * Persona ingresa a la confiteria y paga en la caja
 * Retira comida de los mostradores de acuerdo a lo que pidio
 * Si la persona llega a caja, es porque hay asiento disponible
*/

/**
 * Que datos podria obtener?
 * Cantidad de clientes que hubieron
 * Cantidad de comida rapida pedida
 * Cantidad de postres pedidos
 * 
 * 
 * Que mecanismos podria utilizar?
 * Semaforos generales? Para los asientos
 * Rendevouz para el proceso de pedir
* */

public class Confiteria {
    //Atributos
    private int cantidad_Clientes;
    
    //Mecanismos
    private final Semaphore gen_Clientes;
    private final Semaphore gen_Cajas;

    private final Semaphore mutex_Clientes;
    private final Semaphore rendevouz_Clientes;
    private final Semaphore mutex_Cajas;
    
    //constructor
    public Confiteria(){
        this.cantidad_Clientes = 0;
        
        this.gen_Clientes = new Semaphore(100);
        this.gen_Cajas = new Semaphore(2);
        this.mutex_Clientes = new Semaphore(1);
        this.mutex_Cajas = new Semaphore(1);
        this.rendevouz_Clientes = new Semaphore(1);
        
    }
    
    //paso 1
    public void ingresar(int[] pedidos) throws InterruptedException{
        this.gen_Clientes.acquire();
        
        inc_Cantidad_Clientes();
        
        
    }
    
    private synchronized void inc_Cantidad_Clientes(){
        this.cantidad_Clientes++;
    }
    
    private synchronized void dec_Cantidad_Clientes(){
        this.cantidad_Clientes--;
    }
    
    public synchronized int get_Cantidad_Clientes(){
        return this.cantidad_Clientes;
    }
}
