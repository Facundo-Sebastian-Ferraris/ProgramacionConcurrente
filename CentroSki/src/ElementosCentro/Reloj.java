package ElementosCentro;

import java.util.concurrent.atomic.AtomicInteger;

public class Reloj {
    private AtomicInteger hora;
    private AtomicInteger minuto;

    public Reloj(int hora, int minuto){
        this.hora = new AtomicInteger((hora+minuto/60)%24);
        this.minuto = new AtomicInteger(minuto%60);
    }

    public Reloj(){
        this.hora = new AtomicInteger(0);
        this.minuto = new AtomicInteger(0);
    }

    public synchronized void incrementarMinuto(){
        hora.set((hora.get() + minuto.incrementAndGet()/60)%24);
        minuto.set(minuto.get()%60);
    }

    public void incrementarHora(){
        hora.set(hora.get() % 24);
    }

    public int getHoras(){
        return hora.get();
    }

    public int getMinutos(){
        return minuto.get();
    }

    public synchronized int[] getTiempo(){
        return new int[]{hora.get(),minuto.get()};
    }
}
