package ElementosCentro;

public class Complejo {
    private final MedioElevacion[] mediosDeElevacion = new MedioElevacion[4];
    private final Reloj reloj;
    

    public Complejo(Reloj reloj, MedioElevacion[] mediosDeElevacion){
        this.reloj = reloj;
        for (int i = 0; i < this.mediosDeElevacion.length; i++) {
            this.mediosDeElevacion[i] = mediosDeElevacion[i];
        }
    }

    public void ir_MedioElevacion(boolean telepase, String nombreHilo, int indice) throws InterruptedException{
        int hora = reloj.getHoras();
        if (hora>=10 && hora<17) {
            this.mediosDeElevacion[indice].esquiador_ingresar(telepase, nombreHilo);
        }
    }

}
