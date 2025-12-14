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

    public void cliente_Ingresar(String nombreHilo,boolean conPostre) throws InterruptedException{
        GENERIC_Sillas.acquire(); //toma silla
        System.out.println(nombreHilo + " entra a confiteria");
        RENDEVOUZ_Cajero.release();//notifica cajero
        System.out.println(nombreHilo + " esta en la caja esperando");
        RENDEVOUZ_TomarPedido.acquire();//espero a dar su pedido
        
        //pedido
        this.conPostre = conPostre;
        System.out.print(nombreHilo + " toma pedido");
        if (conPostre) {
            System.out.println( " que incluira postre");
        }else{
            System.out.println();
        }
        
        System.out.println(nombreHilo + " espera a que le digan en cual mostrador esperar la comida");
        RENDEVOUZ_RecibirPedido.release();//notifica a cajero que termino su pedido
        RENDEVOUZ_RecibirNumeroMesa.acquire();//espera a recibir en cual mesa esperar
        int nMesa = numeroMesa;
        System.out.println(nombreHilo + " recibe numero y espera en el mostrador " + (nMesa+1));
        RENDEVOUZ_Caja.release();
        
        Mesas[nMesa].acquire();
        System.out.println(nombreHilo + " retira comida");
        if (conPostre) {
            Mesas[2].acquire();
            System.out.println(nombreHilo + " retira postre");
        }
        
        System.out.println(nombreHilo + " empieza a comer");
        Thread.sleep(3000);

        System.out.println(nombreHilo + " termina de comer y se va");
        
        GENERIC_Sillas.release(); //libera silla
    }

     public void cajero_Atender(String nombreHilo) throws InterruptedException{
        System.out.println(nombreHilo + " espera clientes");
        RENDEVOUZ_Cajero.acquire();

        System.out.println(nombreHilo + " le pregunta el pedido al cliente");
        RENDEVOUZ_TomarPedido.release();
        RENDEVOUZ_RecibirPedido.acquire();
        
        numeroMesa = (numeroMesa + 1) % 2;
        System.out.println(nombreHilo + " le asigna el numero de mostrador al cual debe ir");
        System.out.println(nombreHilo + " le avisa al cocinero el pedido a realizar");
        RENDEVOUZ_Cocina.release();
        RENDEVOUZ_RecibirNumeroMesa.release();
        System.out.println(nombreHilo + " termina de atender al cliente y al cocinero");
        RENDEVOUZ_Caja.acquire(2);
        
    }
     
     public void cocinero_Preparar(String nombreHilo) throws InterruptedException{
        System.out.println(nombreHilo + " espera que el cajero le entregue el pedido");
         RENDEVOUZ_Cocina.acquire();
         System.out.println(nombreHilo + " recibe pedido a preparar y en cual mostrador dejarlo");
         int nMesa = numeroMesa;
         boolean postre = conPostre;
         
         RENDEVOUZ_Caja.release();
         System.out.println(nombreHilo + " empieza a cocinar");
         Thread.sleep(3000);
         System.out.println(nombreHilo + " sirve el plato en el mostrador");
         Mesas[nMesa].release();
         if (postre) {
            System.out.println(nombreHilo + " sirve postre en el mostrador");
             Mesas[2].release();
         }
     }
}
