package centroski;

//IMPORTACIONES
import static centroski.ANSI_Colors.*;

import java.util.Random;
/**
 *
 * @author facundo
 */

public class CentroSki {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        System.out.println(RED+"HOLAAAAA" + RESET + "asdasds");
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
