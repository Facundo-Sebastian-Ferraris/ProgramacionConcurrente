/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mediosElevacion;

/**
 *
 * @author facundo
 */
public class Molinete {
    private final int lectoraDeTarjeta = 1;
    private int capacidad;
    private int usosTotal;

    
    public Molinete(){
        this.capacidad = 3;
        this.usosTotal = 0;
    }
    
    public void ocuparMolinete(){
        this.capacidad--;
        this.usosTotal++;
    }
    
    public void desocuparMolinete(){
        this.capacidad++;
    }

    public int getUsosTotal() {
        return this.usosTotal;
    }
}
