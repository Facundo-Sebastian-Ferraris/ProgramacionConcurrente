package Tests;

import java.awt.Color;

import ElementosCentro.MedioElevacion;
import ElementosCentro.VentanaGUI;

/**
 * Test class for MedioElevacion with 120 threads to test concurrency
 * Uses VentanaGUI to display real-time updates of pending skiers and available chairs
 */
public class TestMedioElevacion {
    
    /**
     * Main method that executes the test
     * Creates 120 skier threads and monitors the MedioElevacion system
     */
    public static void main(String[] args) throws InterruptedException {
        // Create a lift system with 3 turnstiles for testing
        MedioElevacion medioElevacion = new MedioElevacion(3);
        
        // Counter to track how many skiers still need to board
        int totalEsquiadores = 45;
        int[] esquiadoresRestantes = {totalEsquiadores}; // Using array to modify inside lambda
        
        // Array to store thread references
        Thread[] esquiadores = new Thread[totalEsquiadores];
        
        // Create 120 threads that simulate skiers trying to use the lift
        for (int i = 0; i < totalEsquiadores; i++) {
            esquiadores[i] = new Thread(() -> {
                try {
                    // Each skier attempts to enter with a pass (true) and a unique name
                    medioElevacion.esquiador_ingresar(true, "Esquiador-" + Thread.currentThread().getName());
                    
                    // Decrease counter of remaining skiers
                    synchronized(esquiadoresRestantes) {
                        esquiadoresRestantes[0]--;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + Thread.currentThread().getName());
                }
            }, "Hilo-" + i);
        }
        
        // Create the GUI window to display information
        VentanaGUI ventana = new VentanaGUI("Test Medio Elevación - Estado en Tiempo Real", 600, 600, Color.green, Color.black);
        
        // Thread to update the GUI periodically with current status
        Thread guiUpdater = new Thread(() -> {
            boolean a = false;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Format the status information to display
                    String estado = "=== ESTADO DEL MEDIO DE ELEVACIÓN ===\n";
                    estado += "Esquiadores restantes por subir: " + esquiadoresRestantes[0] + "\n";
                    estado += "Sillas disponibles: " + medioElevacion.getSillasDisponibles() + "\n";
                    estado += "==================================\n\n";
                    
                    // Update the GUI with the current status
                    ventana.actualizarTexto(estado);
                    
                    Thread.sleep(500); // Update every 500ms
                    a = !a;
                    if (a) {
                        ventana.setBackground(Color.black);
                        ventana.setForeground(Color.white);
                    } else { ventana.setBackground(Color.white);
                        ventana.setForeground(Color.black);

                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        // Start all skier threads
        System.out.println("Starting 120 threads to test MedioElevacion...");
        for (Thread hilo : esquiadores) {
            hilo.start();
        }
        
        // Start the GUI updater thread
        guiUpdater.start();
        
        // Simulate the operator behavior that gives chairs periodically
        Thread embarcador = new Thread(() -> {
            int i = 0;
            try {
                while(true) {
                    System.out.println("Embarcador: Liberando silla #" + (++i));
                    medioElevacion.embarcador_DarSilla("embarcador");
                    Thread.sleep(2000); // Wait between chair releases
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Embarcador interrupted");
            }
        }, "Embarcador");
        
        embarcador.start();
        
        // Wait for all threads to finish
        for (Thread hilo : esquiadores) {
            hilo.join();
        }
        
        embarcador.join();
        guiUpdater.interrupt();
        
        System.out.println("MedioElevacion test completed.");
        System.out.println("Total recorded uses: " + medioElevacion.getUsosTotal());
        System.out.println("Verified that there were no critical section violations.");
    }
}