package Tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import ElementosCentro.Arte;
import ElementosCentro.ClaseSki;
import ElementosCentro.Complejo;
import ElementosCentro.Confiteria;
import ElementosCentro.ImpresionGUI;
import ElementosCentro.MedioElevacion;
import ElementosCentro.Reloj;
import ElementosCentro.VentanaGUI;
import Runnables.Aerosilla;
import Runnables.Cajero;
import Runnables.Cliente;
import Runnables.Cocinero;
import Runnables.Entrenador;
import Runnables.RunReloj;

public class testComplejo {
    public static void main(String[] args) {
        Random rng = new Random();

        //  RECURSOS PRIMORDIALES     
        Reloj reloj = new Reloj(8, 10);


        MedioElevacion[] mediosElevacion = new MedioElevacion[4];
        for (int i = 0; i < mediosElevacion.length; i++) {
            mediosElevacion[i] = new MedioElevacion(rng.nextInt(1,4));
        }


        ClaseSki clases = new ClaseSki();


        Confiteria confiteria = new Confiteria();

        //  ENSAMBLE DE COMPLEJO
        Complejo prueba = new Complejo(reloj, mediosElevacion, clases, confiteria);


        //  ARMADO DE HILOS
        //      CLIENTES
        Thread[] a = new Thread[10];
        armarHilos(a, prueba);

        //      CONFITERIA
        Cajero cajero = new Cajero(prueba);
        Thread t_cajero = new Thread(cajero, "cajero");
        

        Cocinero cocinero = new Cocinero(prueba);
        Cocinero cocinero2 = new Cocinero(prueba);
        Cocinero cocinero3 = new Cocinero(prueba);
        Thread  t_cocinero = new Thread(cocinero, "cocinero");
        Thread  t_cocinero2 = new Thread(cocinero2, "cocinero");
        Thread  t_cocinero3 = new Thread(cocinero3, "cocinero");


        //      CLASE SKI
        Entrenador entrenador = new Entrenador(prueba);
        Entrenador entrenador2 = new Entrenador(prueba);
        Entrenador entrenador3 = new Entrenador(prueba);
        Thread t_entrenador = new Thread(entrenador, "entrenador1");
        Thread t_entrenador2 = new Thread(entrenador2, "entrenador2");
        Thread t_entrenador3 = new Thread(entrenador3, "entrenador3");
        

        //      MEDIO ELEVACION
        Aerosilla a1 = new Aerosilla(prueba, 0);
        Aerosilla a2 = new Aerosilla(prueba, 1);
        Aerosilla a3 = new Aerosilla(prueba, 2);
        Aerosilla a4 = new Aerosilla(prueba, 3);

        Thread ta1 = new Thread(a1, "Aerosilla 1");
        Thread ta2 = new Thread(a2, "Aerosilla 2");
        Thread ta3 = new Thread(a3, "Aerosilla 3");
        Thread ta4 = new Thread(a4, "Aerosilla 4");
        

        //      TIEMPO
        RunReloj chronos = new RunReloj(prueba, 100);
        Thread  t_chronos = new Thread(chronos, "chronos");
        

        Arte aa = new Arte();
        System.out.println("\u001B[2J\u001B[H"+aa.draw());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            //  GUI
            
            
        ImpresionGUI.print("Complejo Datos", "");
        ImpresionGUI.print("Confiteria", "");
        ImpresionGUI.print("Medio Elevacion", "");
        ImpresionGUI.print("Complejo", "");
        ImpresionGUI.print("Clases de Ski", "");
            
        VentanaGUI GUI_ventana = ImpresionGUI.getGUI("Complejo Datos");
        VentanaGUI GUI_Confi = ImpresionGUI.getGUI("Confiteria");
        VentanaGUI GUI_Comple = ImpresionGUI.getGUI("Complejo");
        VentanaGUI GUI_Clases = ImpresionGUI.getGUI("Clases de Ski");
        VentanaGUI GUI_Medio = ImpresionGUI.getGUI("Medio Elevacion");

        Dimension resolucion = Toolkit.getDefaultToolkit().getScreenSize();

        int grosorLabel = 30;
        int pantalla_ancho = resolucion.width;
        int pantalla_alto = resolucion.height - grosorLabel;

        int gui_complejo_x = 0;
        int gui_complejo_y = 0;
        int gui_complejo_ancho = pantalla_ancho/2;
        int gui_complejo_alto = pantalla_alto/3;
        GUI_Comple.setResolution(gui_complejo_ancho, gui_complejo_alto);
        GUI_Comple.setPosition(gui_complejo_x,gui_complejo_y);


        int gui_confiteria_x = 0;
        int gui_confiteria_y = gui_complejo_alto + grosorLabel;
        int gui_confiteria_ancho = pantalla_ancho/2;
        int gui_confiteria_alto = pantalla_alto/3;
        GUI_Confi.setResolution(gui_confiteria_ancho, gui_confiteria_alto);
        GUI_Confi.setPosition(gui_confiteria_x, gui_confiteria_y);

        int gui_medio_x = 0;
        int gui_medio_y = gui_confiteria_alto + gui_confiteria_alto + grosorLabel;
        int gui_medio_ancho = pantalla_ancho/2;
        int gui_medio_alto = pantalla_alto/3;
        GUI_Medio.setResolution(gui_medio_ancho, gui_medio_alto);
        GUI_Medio.setPosition(gui_medio_x, gui_medio_y);

        int gui_ventana_x = gui_complejo_ancho;
        int gui_ventana_y = 0;
        int gui_ventana_ancho = pantalla_ancho/2;
        int gui_ventana_alto = (pantalla_alto * 2)/3 ;
        GUI_ventana.setResolution(gui_ventana_ancho, gui_ventana_alto);
        GUI_ventana.setPosition(gui_ventana_x, gui_ventana_y);


        int gui_Clases_x = gui_complejo_ancho;
        int gui_Clases_y = gui_ventana_alto + grosorLabel;
        int gui_Clases_ancho = pantalla_ancho/2;
        int gui_Clases_alto = pantalla_alto/3 ;
        GUI_Clases.setResolution(gui_Clases_ancho, gui_Clases_alto);
        GUI_Clases.setPosition(gui_Clases_x, gui_Clases_y);



        //  ARRANQUE HILOS

        //      TIEMPO
        t_chronos.start();
        

        //      CONFITERIA
        t_cajero.start();
        t_cocinero.start();
        t_cocinero2.start();
        t_cocinero3.start();

        //      CLASE SKI
        t_entrenador.start();
        t_entrenador2.start();
        t_entrenador3.start();


        //      MEDIO ELEVACION
        ta1.start();
        ta2.start();
        ta3.start();
        ta4.start();


        //      CLIENTES
        arrancarHilos(a);
        
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GUI_ventana.actualizarTexto(prueba.toString());
        }

    }

    public static void armarHilos(Thread[] a,Complejo c){
        for (int i = 0; i < a.length; i++) {
            Cliente cliente = new Cliente(c);
            a[i] = new Thread(cliente,"<"+(i+1)+">");
        }
    }

    public static void arrancarHilos(Thread[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i].start();
        }
    }

              
}
