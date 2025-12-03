/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package centroski;

import ElementosCentro.ClaseSki;
import Personas.Esquiador;
import Personas.Entrenador;

/**
 *
 * @author facundo
 */
public class TestingClaseSki {

    public static void main(String[] args) throws InterruptedException{

        // Crear una clase de ski con tiempo máximo de espera (en segundos)
        ClaseSki clase = new ClaseSki("A"); // 5 segundos
        
        // Crear los esquiadores
        Esquiador e1 = new Esquiador(clase, "Esquiador 1");
        Esquiador e2 = new Esquiador(clase, "Esquiador 2");
        Esquiador e3 = new Esquiador(clase, "Esquiador 3");
        Esquiador e4 = new Esquiador(clase, "Esquiador 4");
        

        Entrenador et1 = new Entrenador(clase, "Entrenador 1");
        // Crear los hilos
        Thread t1 = new Thread(e1, e1.getNombre());
        Thread t2 = new Thread(e2, e2.getNombre());
        Thread t3 = new Thread(e3, e3.getNombre());
        Thread t4 = new Thread(e4, e4.getNombre());
        Thread t5 = new Thread(et1, et1.getNombre());

        // Simulamos distintas llegadas (algunos tarde)
        t5.start(); 
            t1.start();
            Thread.sleep(50);
            t2.start();
            Thread.sleep(50);
            t3.start();
            Thread.sleep(5000);
            t4.start();
    }
}
