package ElementosCentro;

import Runnables.Aerosilla;
import Runnables.Cajero;
import Runnables.Cliente;
import Runnables.Cocinero;
import Runnables.Entrenador;
import Runnables.HiloReloj;

public class testComplejo {
    public static void main(String[] args) {
        //  RECURSOS PRIMORDIALES     
        Reloj reloj = new Reloj(8, 10);


        MedioElevacion[] mediosElevacion = new MedioElevacion[4];
        for (int i = 0; i < mediosElevacion.length; i++) {
            mediosElevacion[i] = new MedioElevacion(2);
        }


        ClaseSki clases = new ClaseSki();


        Confiteria confiteria = new Confiteria();

        //  ENSAMBLE DE COMPLEJO
        Complejo prueba = new Complejo(reloj, mediosElevacion, clases, confiteria);


        //  ARMADO DE HILOS
        //      CLIENTES
        Cliente r1= new Cliente(prueba);
        Thread tr1= new Thread(r1,"pablito");


        //      CONFITERIA
        Cajero cajero = new Cajero(prueba);
        Thread t_cajero = new Thread(cajero, "cajero");
        
        Cocinero cocinero = new Cocinero(prueba);
        Thread  t_cocinero = new Thread(cocinero, "cocinero");

        //      CLASE SKI
        Entrenador entrenador = new Entrenador(prueba);
        Thread t_entrenador = new Thread(entrenador, "entrenador");
        

        //      MEDIO ELEVACION
        Aerosilla a1 = new Aerosilla(prueba, 0);
        Aerosilla a2 = new Aerosilla(prueba, 1);
        Aerosilla a3 = new Aerosilla(prueba, 2);
        Aerosilla a4 = new Aerosilla(prueba, 3);

        Thread ta1 = new Thread(a1);
        Thread ta2 = new Thread(a2);
        Thread ta3 = new Thread(a3);
        Thread ta4 = new Thread(a4);
        

        //      TIEMPO
        HiloReloj chronos = new HiloReloj(reloj, 500);
        Thread  t_chronos = new Thread(chronos, "chronos");
        

        //  ARRANQUE HILOS
        //      TIEMPO
        t_chronos.start();
        

        //      CONFITERIA
        t_cajero.start();
        t_cocinero.start();

        //      CLASE SKI
        t_entrenador.start();


        //      MEDIO ELEVACION
        ta1.start();
        ta2.start();
        ta3.start();
        ta4.start();


        //      CLIENTES
        tr1.start();


        //  IMPRESION DATOS
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(prueba.toString());
        }
        
    }
}
