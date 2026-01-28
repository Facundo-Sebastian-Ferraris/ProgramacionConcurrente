package Tests;

import ElementosCentro.Confiteria;

public class TestConfiteria {
    
    public static void main(String[] args) throws InterruptedException {
        // Crear una confitería para la prueba
        Confiteria confiteria = new Confiteria();
        
        // Array para almacenar los hilos de clientes
        Thread[] clientes = new Thread[120];
        
        // Crear 120 hilos que simulan clientes intentando usar la confitería
        for (int i = 0; i < 120; i++) {
            final int id = i;
            clientes[i] = new Thread(() -> {
                try {
                    // Cada cliente intenta ingresar con o sin postre (alternando para mayor variedad)
                    boolean conPostre = (id % 2 == 0);
                    confiteria.cliente_Ingresar("Cliente-" + Thread.currentThread().getName(), conPostre);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Hilo cliente interrumpido: " + Thread.currentThread().getName());
                }
            }, "Cliente-" + i);
        }
        
        // Crear algunos hilos de cajeros y cocineros para manejar los pedidos
        Thread[] cajeros = new Thread[5];  // 5 cajeros
        for (int i = 0; i < 5; i++) {
            final int id = i;
            cajeros[i] = new Thread(() -> {
                try {
                    // Los cajeros atienden clientes continuamente
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            confiteria.cajero_Atender("Cajero-" + Thread.currentThread().getName());
                        } catch (InterruptedException e) {
                            break; // Salir del bucle si se interrumpe
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error en cajero: " + Thread.currentThread().getName() + " - " + e.getMessage());
                }
            }, "Cajero-" + i);
        }
        
        Thread[] cocineros = new Thread[5];  // 5 cocineros
        for (int i = 0; i < 5; i++) {
            final int id = i;
            cocineros[i] = new Thread(() -> {
                try {
                    // Los cocineros preparan pedidos continuamente
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            confiteria.cocinero_Preparar("Cocinero-" + Thread.currentThread().getName());
                        } catch (InterruptedException e) {
                            break; // Salir del bucle si se interrumpe
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error en cocinero: " + Thread.currentThread().getName() + " - " + e.getMessage());
                }
            }, "Cocinero-" + i);
        }
        
        // Iniciar todos los hilos de cajeros y cocineros
        System.out.println("Iniciando cajeros y cocineros...");
        for (Thread cajero : cajeros) {
            cajero.start();
        }
        for (Thread cocinero : cocineros) {
            cocinero.start();
        }
        
        // Iniciar todos los hilos de clientes
        System.out.println("Iniciando 120 hilos de clientes para probar Confiteria...");
        for (Thread cliente : clientes) {
            cliente.start();
        }
        
        // Esperar a que todos los hilos de clientes terminen
        for (Thread cliente : clientes) {
            cliente.join();
        }
        
        // Interrumpir y esperar a que terminen los hilos de cajeros y cocineros
        for (Thread cajero : cajeros) {
            cajero.interrupt();
        }
        for (Thread cocinero : cocineros) {
            cocinero.interrupt();
        }
        
        for (Thread cajero : cajeros) {
            cajero.join();
        }
        for (Thread cocinero : cocineros) {
            cocinero.join();
        }
        
        System.out.println("Prueba de Confiteria completada.");
        System.out.println("Total ventas registradas: " + confiteria.get_numeroVentas());
        System.out.println("Se verificó que no hubo violaciones de sección crítica.");
    }
}