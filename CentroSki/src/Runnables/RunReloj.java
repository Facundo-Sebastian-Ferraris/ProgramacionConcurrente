package Runnables;

import ElementosCentro.Complejo;

//  Clase que simula el timepo del Complejo
public class RunReloj implements Runnable {

    private final Complejo c;              //  Recurso compartido
    private final int intervalos;       //  Definir en cuantos ms ocurre un minuto


    //  CONSTRUCTOR
    public RunReloj(Complejo c, int intervalos){
        this.c = c;
        this.intervalos = intervalos;
    }


    //  RUNNER 
    @Override
    public void run(){
        try {
            c.complejo_reloj_simular(intervalos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
