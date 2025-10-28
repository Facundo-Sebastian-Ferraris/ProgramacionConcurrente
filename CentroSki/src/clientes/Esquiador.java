/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package clientes;

import ElementosCentro.ClaseSki;
import centroski.ANSI_Colors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

/**
 *
 * @author facundo
 */
public class Esquiador implements Runnable {
    private final String color;
    private final ClaseSki clase;

    public Esquiador(ClaseSki clase, String color) {
        this.clase = clase;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public ClaseSki getClase() {
        return clase;
    }

    @Override
    public void run() {
        System.out.println(color + Thread.currentThread().getName() + " ejecutando..." + ANSI_Colors.RESET);
        clase.entrarClase();
    }
}
