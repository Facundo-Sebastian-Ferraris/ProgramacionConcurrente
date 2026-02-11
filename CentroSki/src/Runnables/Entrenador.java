package Runnables;

import ElementosCentro.Complejo;

//  Entidad que interactua en todas las atracciones
public class Entrenador implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido
    private boolean ski;        //  Recurso Compartido


    //  Constructor
    public Entrenador(Complejo complejo, boolean ski){
        this.complejo = complejo;
        this.ski = ski;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        try {
            while (true) {
                complejo.claseSki_Instructor_instruir(getName(), ski);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //  Modularizacion del nombre   (colorea el nombre a ORANGE por ser Entrenador)
    private String getName(){
        return Thread.currentThread().getName();
    }
}
