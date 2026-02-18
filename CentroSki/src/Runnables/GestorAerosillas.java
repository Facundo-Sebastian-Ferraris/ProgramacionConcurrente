package Runnables;

import ElementosCentro.Complejo;

//  Entidad que interactua en todas las atracciones
public class GestorAerosillas implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido
    private final int indice;

    //  Constructor
    public GestorAerosillas(Complejo complejo, int indice){
        this.complejo = complejo;
        this.indice = indice;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();

        try {
            //while (true){
                complejo.medio_gestor_darSilla(indice, nombreHilo);
            //}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
