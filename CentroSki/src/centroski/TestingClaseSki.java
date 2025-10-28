/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package centroski;

import ElementosCentro.ClaseSki;
import clientes.Esquiador;

/**
 *
 * @author facundo
 */
public class TestingClaseSki {

    public static void main(String[] args) {

        // Crear una clase de ski con tiempo máximo de espera (en segundos)
        ClaseSki clase = new ClaseSki("Clase A", 1000); // 5 segundos
        
        // Crear los esquiadores
        Esquiador e1 = new Esquiador(clase, ANSI_Colors.GREEN);
        Esquiador e2 = new Esquiador(clase, ANSI_Colors.RED);
        Esquiador e3 = new Esquiador(clase, ANSI_Colors.CYAN);
        Esquiador e4 = new Esquiador(clase, ANSI_Colors.PURPLE);
        Esquiador e5 = new Esquiador(clase, ANSI_Colors.WHITE);

        // Crear los hilos
        Thread t1 = new Thread(e1, "Esquiador-1");
        Thread t2 = new Thread(e2, "Esquiador-2");
        Thread t3 = new Thread(e3, "Esquiador-3");
        Thread t4 = new Thread(e4, "Esquiador-4");
        Thread t5 = new Thread(e5, "Esquiador-5");

        // Simulamos distintas llegadas (algunos tarde)
        try {
            t1.start();
            Thread.sleep(500);
            t2.start();
            Thread.sleep(500);
            t3.start();
            Thread.sleep(500);
            t4.start();
            Thread.sleep(7000); // llega muy tarde
            t5.start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Esperamos un poco y mostramos estadísticas
        try {
            Thread.sleep(10000);
            System.out.println("\n--- ESTADÍSTICAS ---");
            System.out.println("Clases exitosas: " + clase.getClasesExitosas());
            System.out.println("Clases canceladas: " + clase.getClasesCanceladas());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
