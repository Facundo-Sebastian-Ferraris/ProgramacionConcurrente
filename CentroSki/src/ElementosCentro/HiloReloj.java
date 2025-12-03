package ElementosCentro;

public class HiloReloj implements Runnable {
    private final Reloj r;
    private final int intervalos;

    public HiloReloj(Reloj r, int intervalos){
        this.r = r;
        this.intervalos = intervalos;
    }

    @Override
    public void run(){
        while (true) {
            try {
                Thread.sleep(intervalos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r.incrementarMinuto();
        }
    }
}
