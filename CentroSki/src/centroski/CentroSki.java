package centroski;

//IMPORTACIONES
import static centroski.ANSI_Colors.*;
import mediosElevacion.MedioElevacion;

import java.util.Random;
/**
 *
 * @author facundo
 */

public class CentroSki {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MedioElevacion medios[] = new MedioElevacion[4];
        String nombres[] = {"Medio A", "Medio B", "Medio C", "Medio D"};
        PrintMain();
        for (int i = 0; i < 4; i++) {
            medios[i] = new MedioElevacion(nombres[i], chooseRandomNumber(1, 4));
            System.out.println(medios[i].toString() + "\n-------------");
        }
        
        
        
        
    }
    
    private static void PrintMain() {
        System.out.println(BOLD + rainbow("BIENVENIDOS A CAIDA RAPIDA!!!"));
        System.out.println(CYAN + "Planificacion de cada parte:\n" + RESET);
    }
    
    private static int chooseRandomNumber(int from, int to){
        Random r = new Random();
        return r.nextInt(from, to+1);
    }
    
    
}
