package Runnables;

import ElementosCentro.Complejo;
import centroski.ANSI_Colors;

//  Entidad que interactua en todas las atracciones
public class Cajero implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido


    //  Constructor
    public Cajero(Complejo complejo){
        this.complejo = complejo;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        try {
            while (true) {
                complejo.confiteria_cajero_atender(getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //  Modularizacion del nombre   (colorea el nombre a Verde por ser Cajero)
    private String getName(){
        return ANSI_Colors.GREEN+Thread.currentThread().getName()+ANSI_Colors.RESET;
    }
}
