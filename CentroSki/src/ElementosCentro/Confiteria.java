package ElementosCentro;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

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
    private AtomicInteger numeroVentas = new AtomicInteger(0);
    private boolean conPostre = false;
    


    public Confiteria(){
        Mesas[0] = new Semaphore(0);
        Mesas[1] = new Semaphore(0);
        Mesas[2] = new Semaphore(0);

    }

    public void cliente_Ingresar(String nombreHilo,boolean conPostre) throws InterruptedException{
        GENERIC_Sillas.acquire();       //  toma silla
        ImpresionGUI.print("Confiteria", nombreHilo + " entra a confiteria");
        RENDEVOUZ_Cajero.release();     //  notifica cajero
        ImpresionGUI.print("Confiteria", nombreHilo + " esta en la caja esperando");
        RENDEVOUZ_TomarPedido.acquire();        //  espero a dar su pedido
        

        //  Pedido
        this.conPostre = conPostre;
        ImpresionGUI.print("Confiteria",nombreHilo + " toma pedido");
        if (conPostre) ImpresionGUI.print("Confiteria", "\tha pedido con postre");
        

        ImpresionGUI.print("Confiteria", "\n" + nombreHilo + " espera a que le digan en cual mostrador esperar la comida");
        RENDEVOUZ_RecibirPedido.release();//notifica a cajero que termino su pedido
        RENDEVOUZ_RecibirNumeroMesa.acquire();//espera a recibir en cual mesa esperar
        int nMesa = numeroMesa;
        ImpresionGUI.print("Confiteria", nombreHilo + " recibe numero y espera en el mostrador " + (nMesa+1));
        RENDEVOUZ_Caja.release();
        

        Mesas[nMesa].acquire();
        ImpresionGUI.print("Confiteria", nombreHilo + " retira comida");
        if (conPostre) {
            Mesas[2].acquire();
            ImpresionGUI.print("Confiteria", nombreHilo + " retira postre");
        }
        

        ImpresionGUI.print("Confiteria", nombreHilo + " empieza a comer");
        Thread.sleep(3000);


        ImpresionGUI.print("Confiteria", nombreHilo + " termina de comer y se va");
        

        GENERIC_Sillas.release(); //libera silla
    }

     public void cajero_Atender(String nombreHilo) throws InterruptedException{
        ImpresionGUI.print("Confiteria", nombreHilo + " espera clientes");
        RENDEVOUZ_Cajero.acquire();

        ImpresionGUI.print("Confiteria", nombreHilo + " le pregunta el pedido al cliente");
        RENDEVOUZ_TomarPedido.release();
        RENDEVOUZ_RecibirPedido.acquire();
        numeroVentas.incrementAndGet();
        numeroMesa = (numeroMesa + 1) % 2;
        ImpresionGUI.print("Confiteria", nombreHilo + " le asigna el numero de mostrador al cual debe ir");
        ImpresionGUI.print("Confiteria", nombreHilo + " le avisa al cocinero el pedido a realizar");
        RENDEVOUZ_Cocina.release();
        RENDEVOUZ_RecibirNumeroMesa.release();
        ImpresionGUI.print("Confiteria", nombreHilo + " termina de atender al cliente y al cocinero");
        RENDEVOUZ_Caja.acquire(2);
        
    }
     
     public void cocinero_Preparar(String nombreHilo) throws InterruptedException{
        ImpresionGUI.print("Confiteria", nombreHilo + " espera que el cajero le entregue el pedido");
         RENDEVOUZ_Cocina.acquire();
         ImpresionGUI.print("Confiteria", nombreHilo + " recibe pedido a preparar y en cual mostrador dejarlo");
         int nMesa = numeroMesa;
         boolean postre = conPostre;
         
         RENDEVOUZ_Caja.release();
         ImpresionGUI.print("Confiteria", nombreHilo + " empieza a cocinar");
         Thread.sleep(3000);
         ImpresionGUI.print("Confiteria", nombreHilo + " sirve el plato en el mostrador");
         Mesas[nMesa].release();
         if (postre) {
            ImpresionGUI.print("Confiteria", nombreHilo + " sirve postre en el mostrador");
             Mesas[2].release();
         }
     }

     public int get_numeroVentas(){
        return numeroVentas.get();
     }
}
