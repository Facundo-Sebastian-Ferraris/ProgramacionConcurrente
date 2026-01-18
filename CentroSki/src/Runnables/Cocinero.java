package Runnables;

import ElementosCentro.Complejo;
import centroski.ANSI_Colors;

//  Entidad que interactua en todas las atracciones
public class Cocinero implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido


    //  Constructor
    public Cocinero(Complejo complejo){
        this.complejo = complejo;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        try {
            while (true) {
                complejo.confiteria_cocinero_cocinar(getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //  Modularizacion del nombre   (colorea el nombre a YELLOW por ser Cocinero)
    private String getName(){
        return ANSI_Colors.YELLOW+Thread.currentThread().getName()+ANSI_Colors.RESET;
    }
}
