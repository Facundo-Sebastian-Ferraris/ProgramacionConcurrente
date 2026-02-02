package ElementosCentro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Confiteria {
    private final Random rng = new Random();

    private final Semaphore 
        GENERIC_Sillas = new Semaphore(100),    
        RENDEVOUZ_Cajero = new Semaphore(0),    
        RENDEVOUZ_TomarPedido = new Semaphore(0),    
        RENDEVOUZ_RecibirPedido = new Semaphore(0),   
        RENDEVOUZ_RecibirNumeroMostrador = new Semaphore(0), 
        RENDEVOUZ_Cocina = new Semaphore(0),
        RENDEVOUZ_Caja = new Semaphore(0);
        

    private final List<BlockingQueue<String>> mostradores = new ArrayList<>();
    private final String nGUI = "Confiteria";
    private final String[] menu = {"Tostadas", "Calzone", "Huevo y Palta", "Fugazza"};

    
    private int numeroMostrador = 0;
    private AtomicInteger numeroVentas = new AtomicInteger(0);
    private boolean conPostre = false;
    private String pedido = "";
    

    public Confiteria(){
        mostradores.add(new ArrayBlockingQueue<>(3)); // Mostrador 0 - salado
        mostradores.add(new ArrayBlockingQueue<>(3)); // Mostrador 1 - salado
        mostradores.add(new ArrayBlockingQueue<>(3)); // Mostrador 2 - postre
    }

    public void cliente_Ingresar(String nombreHilo,boolean conPostre) throws InterruptedException{
        //  Parte 1: Ingreso
        GENERIC_Sillas.acquire();       //  toma silla
        ImpresionGUI.print(nGUI, nombreHilo + " entra a confiteria");
        RENDEVOUZ_Cajero.release();     //  notifica cajero
        ImpresionGUI.print(nGUI, nombreHilo + " esta en la caja esperando");
        
        
        //  Parte 2: Realizar pedido
        RENDEVOUZ_TomarPedido.acquire();        //  espero a dar su pedido
        pedido = elegirMenu();
        String comida = pedido;
        this.conPostre = conPostre;
        ImpresionGUI.print(nGUI,nombreHilo + " desea " + comida);
        if (conPostre) ImpresionGUI.print(nGUI, "\t y la pidio con postre");       
        RENDEVOUZ_RecibirPedido.release();      //notifica a cajero que termino su pedido
        ImpresionGUI.print(nGUI, "\n" + nombreHilo + " espera a que le digan en cual mostrador esperar la comida");
        
                   
        //  Parte 3: Recibir numero del mostrador
        RENDEVOUZ_RecibirNumeroMostrador.acquire();     //  Espera a recibir en cual mesa esperar
        int nMostrador = numeroMostrador;               //  Adquiere el numero de mostrador al cual acudir
        ImpresionGUI.print(nGUI, nombreHilo + " recibe numero y espera en el mostrador " + (nMostrador+1));
        RENDEVOUZ_Caja.release();                       // Avisa a cajero que recibio el numero a ir
        

        //  Parte 4: Retirar Comida
        comida = mostradores.get(nMostrador).take();        //  Retira comida del mostrador correspondiente
        ImpresionGUI.print(nGUI, nombreHilo + " retira " + comida);
        if (conPostre) {
            mostradores.get(2).take();             //  Retira postre del unico mostrador de postres
            ImpresionGUI.print(nGUI, nombreHilo + " retira postre");
        }
        

        //  Parte 5: Comer
        ImpresionGUI.print(nGUI, nombreHilo + " empieza a comer");
        Thread.sleep(3000);


        //  Parte 6 (Final): irse
        ImpresionGUI.print(nGUI, nombreHilo + " termina de comer y se va");
        GENERIC_Sillas.release(); //libera silla
    }
    
    
    public void cajero_Atender(String nombreHilo) throws InterruptedException{
        //  Parte 1: Esperando clientes
        ImpresionGUI.print(nGUI, nombreHilo + " espera clientes");
        RENDEVOUZ_Cajero.acquire();


        //  Parte 2: Atiende a Cliente
        ImpresionGUI.print(nGUI, nombreHilo + " le pregunta el pedido al cliente");
        RENDEVOUZ_TomarPedido.release();
        RENDEVOUZ_RecibirPedido.acquire();
        numeroVentas.incrementAndGet();

        //  Parte 3: Asignar numero de mostrador a Cliente
        numeroMostrador = (numeroMostrador + 1) % 2;
        ImpresionGUI.print(nGUI, nombreHilo + " le asigna el numero de mostrador al cual debe ir");
        RENDEVOUZ_RecibirNumeroMostrador.release();


        //  Parte 4: Notifica a Cocinero para que realice pedido
        ImpresionGUI.print(nGUI, nombreHilo + " le avisa al cocinero el pedido a realizar");
        RENDEVOUZ_Cocina.release();


        //  Parte 5: Espera a que el cliente y cocinero tomen numero de mostrador, y si el cocinero debe hacer postre 
        ImpresionGUI.print(nGUI, nombreHilo + " termina de atender al cliente y al cocinero");
        RENDEVOUZ_Caja.acquire(2);
    }
     
    public void cocinero_Preparar(String nombreHilo) throws InterruptedException{
        //  Parte 1: Esperar pedido 
        ImpresionGUI.print(nGUI, nombreHilo + " espera que el cajero le entregue el pedido");
        RENDEVOUZ_Cocina.acquire();
        ImpresionGUI.print(nGUI, nombreHilo + " recibe pedido a preparar y en cual mostrador dejarlo");


        //  Parte 2: Obtener numero de mostrador a servir y si debe servir postre o no
        int nMostrador = numeroMostrador;
        String pedido = this.pedido;
        boolean postre = conPostre;
        RENDEVOUZ_Caja.release();       //  Avisa a cajero que recibio la orden


        //  Parte 3: Realizar pedido 
        ImpresionGUI.print(nGUI, nombreHilo + " empieza a cocinar");
        Thread.sleep(3000);
        ImpresionGUI.print(nGUI, nombreHilo + " sirve el plato en el mostrador");


        //  Parte 4: Servir en mostrador 
        mostradores.get(nMostrador).put(pedido);
        if (postre) {
        ImpresionGUI.print(nGUI, nombreHilo + " sirve postre en el mostrador");
            mostradores.get(2).put("Postre");
        }
    }

    private String elegirMenu(){
        return menu[rng.nextInt(0,menu.length)];
    }

    public int get_numeroVentas(){
        return numeroVentas.get();
    }
}
