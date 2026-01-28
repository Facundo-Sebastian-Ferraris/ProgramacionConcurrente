package Runnables;

import java.awt.Color;

import ElementosCentro.Complejo;
import centroski.ANSI_Colors;

//  Entidad que interactua en todas las atracciones
public class Cliente implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido


    //  Constructor
    public Cliente(Complejo complejo){
        this.complejo = complejo;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        try {
            complejo.complejo_cliente_ingreso(getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //  Modularizacion del nombre   (colorea el nombre a Cyan por ser Cliente)
    private String getName(){
        return Thread.currentThread().getName();
    }
}
