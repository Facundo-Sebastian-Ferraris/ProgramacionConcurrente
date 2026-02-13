package ElementosCentro;

public class Aerosilla {

    private final int capacidad;
    private int sillasDisponibles;
    private boolean viajando;
    private boolean arriba;




    public Aerosilla(int capacidad, boolean arriba){
        this.capacidad = capacidad;
        this.arriba = arriba;
        viajando = false;
        sillasDisponibles = capacidad;
    }




    public boolean subirse(){
        boolean r = sillasDisponibles > 0 && !viajando;
        if (r){
            sillasDisponibles--;
        }

        return r;
    }




    public boolean bajarse(){
        boolean r = sillasDisponibles < capacidad && !viajando;
        if (r){
            sillasDisponibles++;
        }

        return r;
    }




    public void iniciarViaje(){
        viajando = true;
    }




    public void terminarViaje(){
        viajando = false;
        arriba = !arriba;
    }




    public boolean getArriba(){
        return arriba;
    }




    public boolean getViajando(){
        return viajando;
    }
}
