package centroski;

//IMPORTACIONES
import static centroski.ANSI_Colors.*;

import ElementosCentro.ClaseSki;
import ElementosCentro.MedioElevacion;

import clientes.Reloj;
import clientes.Esquiador;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author facundo
 */

public class CentroSki {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //CONSTRUCCION PARQUE
            // MOLINETES
            // CLASES
            // CAFETERIA
        //  CONSTRUCCION DE INSTANCIAS
            //  CLASE SKI
        
        ClaseSki clase = new ClaseSki("Clase A", 5000);
            
        //  CONSTRUCCION DE RUNNABLES
        Esquiador esquiador1 = new Esquiador(clase);
        Esquiador esquiador2 = new Esquiador(clase);
        Esquiador esquiador3 = new Esquiador(clase);
        Esquiador esquiador4 = new Esquiador(clase);
        Esquiador esquiador5 = new Esquiador(clase);
        
        
        //  CONSUTRCCION DE HILOS
        Thread hilo_Esquiador1 = new Thread(esquiador1, "hilo_Esquiador1");
        Thread hilo_Esquiador2 = new Thread(esquiador2, "hilo_Esquiador2");
        Thread hilo_Esquiador3 = new Thread(esquiador3, "hilo_Esquiador3");
        Thread hilo_Esquiador4 = new Thread(esquiador4, "hilo_Esquiador4");
        Thread hilo_Esquiador5 = new Thread(esquiador5, "hilo_Esquiador5");
        
        
        hilo_Esquiador1.start();
        hilo_Esquiador2.start();
        hilo_Esquiador3.start();
        hilo_Esquiador4.start();
        hilo_Esquiador5.start();
        
        
        Reloj timer = new Reloj(0,0,500);
        Thread th_timer = new Thread(timer);
        MedioElevacion medios[] = new MedioElevacion[4];
        String nombres[] = {"Medio A", "Medio B", "Medio C", "Medio D"};
        PrintMain();
        for (int i = 0; i < 4; i++) {
            medios[i] = new MedioElevacion(nombres[i], chooseRandomNumber(1, 4));
            System.out.println(medios[i].toString() + "\n-------------");
        }
        
        
        th_timer.start();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(200);
                System.out.println(timer.getHoraActual());
            } catch (InterruptedException ex) {
                Logger.getLogger(CentroSki.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        timer.detener();
        
        
        
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
