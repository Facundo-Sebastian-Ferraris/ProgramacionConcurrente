package ElementosCentro;


public class Reloj {
    private int
        dia,
        hora,
        minuto;

    public Reloj(int hora, int minuto){
        this.dia = 0;
        this.hora = hora;
        this.minuto = minuto;
    }

    public synchronized int getDias(){
        return dia;
    }

    public synchronized int getHoras(){
        return hora;
    }

    public synchronized int getMinutos(){
        return minuto;
    }

    public synchronized int[] getTiempo(){
        return new int[]{dia,hora,minuto};
    }

    public synchronized void incrementar_Minuto(){
        minuto++;
        if (minuto == 60) {
            minuto = 0;
            hora++;
            if (hora == 24) {
                hora = 0;
                dia++;
            }
        }
    }

    public synchronized void incrementar_Hora(){
        hora++;
        if (hora == 24) {
            hora = 0;
            dia++;
        }
    }
}
