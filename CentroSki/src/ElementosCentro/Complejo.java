package ElementosCentro;


public class Complejo {
    private final MedioElevacion[] mediosDeElevacion = new MedioElevacion[4];//Semaforos!!!
    private final ClaseSki clases;// LOCKS!!!

    private final Reloj reloj;
    

    public Complejo(Reloj reloj, MedioElevacion[] mediosDeElevacion, ClaseSki clases){
        this.reloj = reloj;
        for (int i = 0; i < this.mediosDeElevacion.length; i++) {
            this.mediosDeElevacion[i] = mediosDeElevacion[i];
        }
        this.clases = clases;
    }

    public void ir_MedioElevacion(boolean telepase, String nombreHilo, int indice) throws InterruptedException{
        int hora = reloj.getHoras();
        if (hora>=10 && hora<17) {
            this.mediosDeElevacion[indice].esquiador_ingresar(telepase, nombreHilo);
        }
    }

    public void asistir_clase(String nombreHilo) throws InterruptedException{
        int hora = reloj.getHoras();
        if (hora >= 8 && hora < 22) { 
            this.clases.esquiador_irClase(nombreHilo);
        } 
    }

}
