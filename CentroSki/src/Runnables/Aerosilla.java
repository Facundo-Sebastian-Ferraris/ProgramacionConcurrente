package Runnables;

import ElementosCentro.Complejo;

//  Entidad que interactua en todas las atracciones
public class Aerosilla implements Runnable{
    private final Complejo complejo;        //  Recurso Compartido
    private final int indice;

    //  Constructor
    public Aerosilla(Complejo complejo, int indice){
        this.complejo = complejo;
        this.indice = indice;
    }


    //  Ejecucion Principal
    @Override
    public void run() {
        try {
            complejo.medio_embarcador_darSilla(indice);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
