package Runnables;

import ElementosCentro.Complejo;
import centroski.ANSI_Colors;

//  Entidad que interactua en todas las atracciones
public class Entrenador implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido


    //  Constructor
    public Entrenador(Complejo complejo){
        this.complejo = complejo;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        try {
            complejo.claseSki_Instructor_instruir(getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //  Modularizacion del nombre   (colorea el nombre a ORANGE por ser Entrenador)
    private String getName(){
        return ANSI_Colors.ORANGE+Thread.currentThread().getName()+ANSI_Colors.RESET;
    }
}
