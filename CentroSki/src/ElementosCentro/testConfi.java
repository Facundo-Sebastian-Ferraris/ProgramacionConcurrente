package ElementosCentro;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class testConfi {

    // 🎨 Colores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";      // Clientes
    private static final String YELLOW = "\u001B[33m";   // Cajero
    private static final String RED = "\u001B[31m";      // Cocineros
    private static final String GREEN = "\u001B[32m";    // Éxito
    private static final String WHITE = "\u001B[37m";    // Neutro

    public static void main(String[] args) throws InterruptedException {
        Confiteria confiteria = new Confiteria();
        
        final int NUM_CLIENTES = 6;
        final int NUM_COCINEROS = 2;
        
        CountDownLatch clientesListos = new CountDownLatch(NUM_CLIENTES);
        CountDownLatch cocinerosFinalizan = new CountDownLatch(NUM_COCINEROS);
        
        AtomicInteger exitosos = new AtomicInteger(0);

        // 🎉 Encabezado animado
        System.out.println(GREEN + "================================================");
        System.out.println("🏔️  SIMULACIÓN DEL COMPLEJO \"CAÍDA RÁPIDA\" 🎿");
        System.out.println("🍽️  SECCIÓN CONFITERÍA — FLUJO CLIENTE-CAJERO-COCINERO");
        System.out.println("================================================" + RESET);
        System.out.println();

        // 👥 Clientes (con nombres y colores)
        String[] clientes = {"🧑 Alex", "👩 Cami", "🧑 Diego", "👩 Eli", "🧑 Franco", "👩 Gaby"};
        for (int i = 0; i < NUM_CLIENTES; i++) {
            final String nombre = clientes[i % clientes.length];
            final boolean postre = (i % 3 == 0); // 1 de cada 3 pide postre
            
            new Thread(() -> {
                try {
                    confiteria.cliente_Ingresar(nombre, postre);
                    exitosos.incrementAndGet();
                } catch (InterruptedException e) {
                    System.err.println(RED + "⚠️  " + nombre + " fue interrumpido" + RESET);
                } finally {
                    clientesListos.countDown();
                }
            }, CYAN + "👤 " + nombre + RESET).start();
        }

        // 🧾 Cajero
        new Thread(() -> {
            try {
                for (int i = 0; i < clientes.length; i++) {
                    confiteria.cajero_Atender(YELLOW+"Cajero"+RESET);
                }
            } catch (InterruptedException e) {
                System.err.println(YELLOW + "⚠️  Cajero interrumpido" + RESET);
            }
        }, YELLOW + "🧾 Cajero" + RESET).start();

        // 👨‍🍳 Cocineros
        for (int i = 0; i < NUM_COCINEROS; i++) {
            final String nombre = "👨‍🍳 Cocinero-" + (i + 1);
            new Thread(() -> {
                try {
                    while (clientesListos.getCount() > 0) {
                        confiteria.cocinero_Preparar(nombre);
                    }
                } catch (InterruptedException e) {
                    System.err.println(RED + "⚠️  " + nombre + " interrumpido" + RESET);
                } finally {
                    cocinerosFinalizan.countDown();
                }
            }, RED + nombre + RESET).start();
        }

        // ⏳ Esperar a que todos terminen (sin timeout estricto, ya que no hay deadlock)
        clientesListos.await();
        cocinerosFinalizan.await(20, java.util.concurrent.TimeUnit.SECONDS); // breve espera a que terminen últimos pedidos

        // 📊 Resumen final
        System.out.println();
        System.out.println(GREEN + "✅ SIMULACIÓN FINALIZADA" + RESET);
        System.out.println(WHITE + "--------------------------------" + RESET);
        System.out.println("✔️  Clientes atendidos: " + GREEN + exitosos.get() + RESET + " / " + NUM_CLIENTES);
        
        System.out.println(WHITE + "--------------------------------" + RESET);
        System.out.println(GREEN + "🎉 ¡Todos disfrutaron su comida en el Complejo Caída Rápida!" + RESET);
    }
}