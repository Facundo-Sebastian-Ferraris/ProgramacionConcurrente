/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ElementosCentro;
import static centroski.ANSI_Colors.*;
/**
 *
 * @author facundo
 */
public class MedioElevacion {
    private String nombre;
    private Molinete molinetes[];

    public MedioElevacion(String nombre, int cantidadMolinetes){
        this.nombre = nombre;
        this.molinetes = new Molinete[cantidadMolinetes];
    }
    
    @Override
    public String toString(){
        String r = GREEN + "Nombre:\t"+ rainbow(this.nombre) +
                CYAN + "\nNumero de Molinetes:\t" + YELLOW + this.molinetes.length;
        return r;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getUsosTotal() {
        int r = 0;
        for (int i = 0; i < molinetes.length; i++) {
            r += this.molinetes[i].getUsosTotal();
        }
        return r;
    }

}
