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
        notifyAll();
    }

    public synchronized void esperarHasta(int hora, int minuto) throws InterruptedException{
        while (this.hora<hora || this.minuto<minuto) {
            wait();
        }
    }


    public synchronized void ventana(int a_hora, int b_hora, int a_minuto, int b_minuto) throws InterruptedException {
        int
            inicio = a_hora * 60 + a_minuto,
            fin = b_hora * 60 + b_minuto,
            ahora = hora * 60 + minuto;

         while (ahora < inicio || ahora >= fin) {
             wait();
             ahora = hora * 60 + minuto;
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
