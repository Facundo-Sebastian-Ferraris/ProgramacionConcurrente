package ElementosCentro;

import java.util.concurrent.Semaphore;

public class Confiteria {
    private final Semaphore GENERIC_Sillas = new Semaphore(100);    
    private final Semaphore RENDEVOUZ_Cajero = new Semaphore(0);    
    private final Semaphore RENDEVOUZ_TomarPedido = new Semaphore(0);    
    private final Semaphore RENDEVOUZ_RecibirPedido = new Semaphore(0);    
    private final Semaphore RENDEVOUZ_RecibirNumeroMesa = new Semaphore(0);    
    private final Semaphore RENDEVOUZ_Cocina = new Semaphore(0);    
    private final Semaphore RENDEVOUZ_Caja = new Semaphore(0);    
    private final Semaphore[] Mesas = new Semaphore[3];    
    
   
    
    private int numeroMesa = 0;
    private boolean conPostre = false;
    


    public Confiteria(){
        Mesas[0] = new Semaphore(0);
        Mesas[1] = new Semaphore(0);
        Mesas[2] = new Semaphore(0);

    }

    public void cliente_Ingresar(boolean conPostre) throws InterruptedException{
        GENERIC_Sillas.acquire(); //toma silla

        RENDEVOUZ_Cajero.release();//notifica cajero
        RENDEVOUZ_TomarPedido.acquire();//espero a dar su pedido
        
        //pedido
        this.conPostre = conPostre;
        
        RENDEVOUZ_RecibirPedido.release();//notifica a cajero que termino su pedido
        RENDEVOUZ_RecibirNumeroMesa.acquire();
        int nMesa = numeroMesa;
        
        RENDEVOUZ_Caja.release();
        
        Mesas[nMesa].acquire();
        if (conPostre) {
            Mesas[2].acquire();
        }
        
        Thread.sleep(3000);
        
        GENERIC_Sillas.release(); //libera silla
    }

     public void cajero_Atender() throws InterruptedException{
        RENDEVOUZ_Cajero.acquire();
        RENDEVOUZ_TomarPedido.release();
        RENDEVOUZ_RecibirPedido.acquire();
        
        numeroMesa = (numeroMesa + 1)%2;
        
        RENDEVOUZ_Cocina.release();
        RENDEVOUZ_RecibirNumeroMesa.release();
        
        RENDEVOUZ_Caja.acquire(2);
        
    }
     
     public void cocinero_Preparar() throws InterruptedException{
         RENDEVOUZ_Cocina.acquire();
         
         int nMesa = numeroMesa;
         boolean postre = conPostre;
         
         RENDEVOUZ_Caja.release();
         
         Thread.sleep(3000);
         Mesas[nMesa].release();
         if (postre) {
             Mesas[2].release();
         }
     }
}
