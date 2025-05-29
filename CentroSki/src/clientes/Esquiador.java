/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package clientes;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author facundo
 */
class Esquiador implements Runnable{

    public Esquiador(mediosElevacion.MedioElevacion a) {
        
    }
    
    @Override
    public void run(){
        while (true) {
            System.out.println("Hola");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Esquiador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
