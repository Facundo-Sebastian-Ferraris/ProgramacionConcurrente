/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Personas;

import ElementosCentro.ClaseSki;
import centroski.ANSI_Colors;

/**
 *
 * @author facundo
 */
public class Entrenador implements Runnable {
    private final String color = ANSI_Colors.GREEN;
    private String nombre;
    private final ClaseSki clase;

    public Entrenador(ClaseSki clase, String nombre) {
        this.clase = clase;
        this.nombre = color+nombre+ANSI_Colors.RESET;
    }

    public String getColor() {
        return color;
    }

    public ClaseSki getClase() {
        return clase;
    }

    @Override
    public void run() {
        System.out.println(nombre + " ejecutando..." + ANSI_Colors.RESET);
        // while (true) {
            try {
                clase.instructor_Ensenar(nombre);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        // }
    }

    public String getNombre(){
        return nombre;
    }
}
