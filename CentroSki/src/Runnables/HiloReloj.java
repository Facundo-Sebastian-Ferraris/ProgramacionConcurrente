package Runnables;

import ElementosCentro.Reloj;

//  Clase que simula el timepo del Complejo
public class HiloReloj implements Runnable {

    private final Reloj r;              //  Recurso compartido
    private final int intervalos;       //  Definir en cuantos ms ocurre un minuto


    //  CONSTRUCTOR
    public HiloReloj(Reloj r, int intervalos){
        this.r = r;
        this.intervalos = intervalos;
    }


    //  RUNNER  
    @Override
    public void run(){
        while (true) {
            try {
                Thread.sleep(intervalos);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r.incrementar_Minuto();
        }
    }
}
