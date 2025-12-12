package ElementosCentro;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class Confiteria {
    private final Semaphore GENERIC_Sillas;
    private final Exchanger<Boolean> pedido;    
    


    public Confiteria(){
        GENERIC_Sillas = new Semaphore(100);
        pedido = new Exchanger();
    }

    public void cliente_Ingresar(boolean conPostre) throws InterruptedException{
        GENERIC_Sillas.acquire(); //toma silla

        pedido.exchange(conPostre);
        GENERIC_Sillas.release(); //libera silla

    }

     public void cajero_Atender(boolean conPostre) throws InterruptedException{
        pedido.exchange(null);
        
    }
}
