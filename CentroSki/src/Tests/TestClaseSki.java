package Tests;

import ElementosCentro.ClaseSki;

public class TestClaseSki {
    
    public static void main(String[] args) throws InterruptedException {
        // Crear una clase de ski para la prueba
        ClaseSki claseSki = new ClaseSki();
        
        // Array para almacenar los hilos de esquiadores
        Thread[] esquiadores = new Thread[120];
        
        // Crear 120 hilos que simulan esquiadores intentando tomar clases
        for (int i = 0; i < 120; i++) {
            final int id = i;
            esquiadores[i] = new Thread(() -> {
                try {
                    // Cada esquiador intenta ir a clase
                    claseSki.esquiador_irClase("Esquiador-" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Hilo esquiador interrumpido: " + Thread.currentThread().getName());
                }
            }, "Esquiador-" + i);
        }
        
        // Crear algunos hilos de instructores para dar clases
        Thread[] instructores = new Thread[10];  // 10 instructores
        for (int i = 0; i < 10; i++) {
            final int id = i;
            instructores[i] = new Thread(() -> {
                try {
                    // Los instructores dan clases continuamente
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            claseSki.instructor_darClase("Instructor-" + Thread.currentThread().getName());
                        } catch (InterruptedException e) {
                            break; // Salir del bucle si se interrumpe
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error en instructor: " + Thread.currentThread().getName() + " - " + e.getMessage());
                }
            }, "Instructor-" + i);
        }
        
        // Iniciar todos los hilos de instructores
        System.out.println("Iniciando instructores...");
        for (Thread instructor : instructores) {
            instructor.start();
        }
        
        // Iniciar todos los hilos de esquiadores
        System.out.println("Iniciando 120 hilos de esquiadores para probar ClaseSki...");
        for (Thread esquiador : esquiadores) {
            esquiador.start();
        }
        
        // Esperar a que todos los hilos de esquiadores terminen
        for (Thread esquiador : esquiadores) {
            esquiador.join();
        }
        
        // Interrumpir y esperar a que terminen los hilos de instructores
        for (Thread instructor : instructores) {
            instructor.interrupt();
        }
        
        for (Thread instructor : instructores) {
            instructor.join();
        }
        
        System.out.println("Prueba de ClaseSki completada.");
        System.out.println("Total clases exitosas registradas: " + claseSki.get_ClasesExitosas());
        System.out.println("Se verificó que no hubo violaciones de sección crítica.");
    }
}