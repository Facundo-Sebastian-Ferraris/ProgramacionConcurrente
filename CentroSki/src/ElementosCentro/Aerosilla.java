package ElementosCentro;

public class Aerosilla {

    private final int capacidad;
    private int sillasDisponibles;
    private boolean viajando;
    private boolean arriba;
    private boolean abordaje;




    public Aerosilla(int capacidad, boolean arriba){
        this.capacidad = capacidad;
        this.arriba = arriba;
        viajando = false;
        sillasDisponibles = capacidad;
        abordaje = true;
    }




    public synchronized boolean subirse(){
        boolean r = !viajando && abordaje && sillasDisponibles > 0;
        if (r){
            sillasDisponibles--;
        }

        return r;
    }




    public synchronized boolean bajarse(){
        boolean r = !viajando && !abordaje && sillasDisponibles < capacidad;
        if (r){
            sillasDisponibles++;
        } else {
        }


        return r;
    }




    public synchronized void iniciarViaje(){
        viajando = true;
        abordaje = false;
    }




    public synchronized void terminarViaje(){
        viajando = false;
        arriba = !arriba;
    }




    public synchronized void habilitarAbordaje() {
        abordaje = true;
    }




    public synchronized boolean getArriba(){
        return arriba;
    }




    public synchronized boolean esDisponible(boolean arriba){
        return (this.arriba==arriba && !viajando);
    }




    public synchronized int getSubidos(){
        return capacidad-sillasDisponibles;
    }




    public synchronized boolean getViajando(){
        return viajando;
    }




    public synchronized boolean esVacio(){
        return sillasDisponibles == 0;
    }
}
