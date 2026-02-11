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
        GENERIC_Sillas,                         // 🪑 100 sillas disponibles
        RENDEVOUZ_Cajero,                       // 🤝 Espera cliente en caja
        RENDEVOUZ_TomarPedido,                  // 📝 Cliente listo para dar pedido
        RENDEVOUZ_RecibirPedido,                // ✅ Cliente terminó de pedir
        RENDEVOUZ_RecibirNumeroMostrador,       // 📍 Espera número de mostrador
        RENDEVOUZ_Cocina,                       // 🔔 Notificar a cocina
        RENDEVOUZ_Caja;                         // 🧾 Confirmación de mostrador asignado
        
    private final List<BlockingQueue<String>> mostradores = new ArrayList<>();
    private final String[] menu = {"Tostadas", "Calzone", "Huevo y Palta", "Fugazza"};

    
    private int numeroMostrador = 0;
    private AtomicInteger numeroVentas = new AtomicInteger(0);
    private boolean conPostre = false;
    private ArrayBlockingQueue<String> pedido = new ArrayBlockingQueue<String>(100);
    

    //  CONSTRUCTOR
    public Confiteria(){
        GENERIC_Sillas = new Semaphore(100);
        RENDEVOUZ_Cajero = new Semaphore(0);
        RENDEVOUZ_TomarPedido = new Semaphore(0);
        RENDEVOUZ_RecibirPedido = new Semaphore(0);
        RENDEVOUZ_RecibirNumeroMostrador = new Semaphore(0);
        RENDEVOUZ_Cocina = new Semaphore(0);
        RENDEVOUZ_Caja = new Semaphore(0);


        mostradores.add(new ArrayBlockingQueue<>(3));       // 🥘 Mostrador 0 - salado
        mostradores.add(new ArrayBlockingQueue<>(3));       // 🥘 Mostrador 1 - salado
        mostradores.add(new ArrayBlockingQueue<>(3));       // 🍰 Mostrador 2 - postre
    }




    public void cliente_Ingresar(String nombreHilo, boolean conPostre) throws InterruptedException{
        //  👤 Parte 1: Ingreso
        GENERIC_Sillas.acquire();       //  🪑 Toma silla
        printGUI( nombreHilo + " entra a confiteria");
        RENDEVOUZ_Cajero.release();     //  💰 Notifica al cajero
        printGUI( nombreHilo + " esta en la caja esperando");
        
        
        //  📝 Parte 2: Realizar pedido
        RENDEVOUZ_TomarPedido.acquire();        //  ⏳ Espera a dar su pedido
        String comidaDeseada= elegirMenu();
        this.conPostre = conPostre;
        pedido.add(comidaDeseada);
        printGUI(nombreHilo + " desea " + comidaDeseada);
        if (conPostre) printGUI( "\t y la pidio con postre");       
        RENDEVOUZ_RecibirPedido.release();      //  ✅ Notifica a cajero que terminó su pedido
        printGUI( "\n" + nombreHilo + " espera a que le digan en cual mostrador esperar la comida");
        
                   
        //  📍 Parte 3: Recibir numero del mostrador
        RENDEVOUZ_RecibirNumeroMostrador.acquire();     //  🔢 Espera número de mostrador
        int nMostrador = numeroMostrador;               //  📦 Obtiene mostrador asignado
        printGUI( nombreHilo + " recibe numero y espera en el mostrador " + (nMostrador+1));
        RENDEVOUZ_Caja.release();                       //  ✅ Confirma recepción del número
        

        //  🍽️ Parte 4: Retirar Comida
        String comidaObtenida = mostradores.get(nMostrador).take();     //   📦 Retira del mostrador
        if (!comidaDeseada.equals(comidaObtenida)) {
            printGUI( nombreHilo + "AAAAAAAAAAAAAAAAAAAAAAAAA retiro algo que no correspondia!!!");
        } else {
            printGUI( nombreHilo + "BIEEEEENNNNN, LLEGO LO QUE QUERIA!!!");
        }
        printGUI( nombreHilo + " retira " + comidaObtenida);
        if (conPostre) {
            mostradores.get(2).take();                                  //   🍰 Retira postre
            printGUI( nombreHilo + " retira postre");
        }
        

        //  😋 Parte 5: Comer
        printGUI( nombreHilo + " empieza a comer");
        Thread.sleep(3000);


        //  👋 Parte 6 (Final): irse
        printGUI( nombreHilo + " termina de comer y se va");
        GENERIC_Sillas.release(); // 🪑 Libera silla
    }
    
    
    private String elegirMenu(){
        return menu[rng.nextInt(0,menu.length)];
    }

    


    public void cajero_Atender(String nombreHilo) throws InterruptedException{ 
        //  💰 Parte 1: Esperando clientes
        printGUI( nombreHilo + " espera clientes");
        RENDEVOUZ_Cajero.acquire();


        //  📝 Parte 2: Atiende a Cliente
        printGUI( nombreHilo + " le pregunta el pedido al cliente");
        RENDEVOUZ_TomarPedido.release();
        RENDEVOUZ_RecibirPedido.acquire();
        numeroVentas.incrementAndGet();

        //  🔄 Parte 3: Asignar numero de mostrador a Cliente
        numeroMostrador = (numeroMostrador + 1) % 2;        //  📦 Rotación entre mostradores salados
        printGUI( nombreHilo + " le asigna el numero de mostrador al cual debe ir");
        RENDEVOUZ_RecibirNumeroMostrador.release();


        //  👨‍🍳 Parte 4: Notifica a Cocinero para que realice pedido
        printGUI( nombreHilo + " le avisa al cocinero el pedido a realizar");
        RENDEVOUZ_Cocina.release();


        //  ✅ Parte 5: Espera confirmaciones
        printGUI( nombreHilo + " termina de atender al cliente y al cocinero");
        RENDEVOUZ_Caja.acquire(2);      // 👤 Cliente + 👨‍🍳 Cocinero confirmaron
    }
     




    public void cocinero_Preparar(String nombreHilo) throws InterruptedException{
        //  👨‍🍳 Parte 1: Esperar pedido 
        printGUI( nombreHilo + " chequea pedidos");
        String pedido = this.pedido.take();
        boolean postre = conPostre;
        printGUI( nombreHilo + " recibe pedido a preparar y en cual mostrador dejarlo");
        
        
        //  📦 Parte 2: Obtener detalles del pedido
        RENDEVOUZ_Cocina.acquire();     //  🔔 Recibe notificación del cajero
        int nMostrador = numeroMostrador;
        RENDEVOUZ_Caja.release();       //  ✅ Confirma recepción de orden


        //  🔥 Parte 3: Realizar pedido 
        printGUI( nombreHilo + " empieza a cocinar");
        Thread.sleep(3000);
        printGUI( nombreHilo + " sirve el plato en el mostrador");


        //  📤 Parte 4: Servir en mostrador 
        mostradores.get(nMostrador).put(pedido);
        if (postre) {
            printGUI( nombreHilo + " sirve postre en el mostrador");
            mostradores.get(2).put("Postre");       // 🍰 Mostrador exclusivo de postres
        }
    }




   



    public int get_numeroVentas(){
        return numeroVentas.get();
    }

    private void printGUI(String r){
        ImpresionGUI.print( "Confiteria", r);
    }
}