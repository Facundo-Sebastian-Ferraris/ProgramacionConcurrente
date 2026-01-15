package ElementosCentro;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Complejo {
    private final Random random = new Random();
    //  ELEMENTOS CLAVE:
    //      Medio Elevacion:
    //              Mecanismo: Monitores + Mutex
    private final MedioElevacion[] 
        mediosDeElevacion = new MedioElevacion[4];
    private AtomicInteger
        personasEnMedio = new AtomicInteger(0);  
    

    //      Clases de Ski:
    //              Mecanismo: Lockers
    private final ClaseSki 
        clases;


    //      Confiteria
    //              Mecanismo: Semaforos Genericos + Rendevouz
    private final Confiteria 
        confiteria;
    
    
    private final Reloj
        reloj;
    

    //CONSTRUCTOR
    public Complejo(Reloj reloj, MedioElevacion[] mediosDeElevacion, ClaseSki clases, Confiteria confiteria){
        this.reloj = reloj;
        for (int i = 0; i < this.mediosDeElevacion.length; i++) {
            this.mediosDeElevacion[i] = mediosDeElevacion[i];
        }
        this.clases = clases;
        this.confiteria = confiteria;
    }

    // DECISION ALEATORIA
    //      1.  ir confiteria
    //      2.  clase ski
    //      3.  subir medio elevacion
    //      4.  irse  
    public void complejo_cliente_ingreso(String nombreHilo) throws InterruptedException{
        int accion = random.nextInt(1, 3);              //  Cliente decide que hacer
        boolean telepase = random.nextBoolean();        //  Cliente ingresa al parque con o sin telepase


        //  Cliente se queda en parque hasta que lo decida, o hasta que el parque cierre
        while (parque_abierto()|| accion != 4) {
            switch (accion) {
                case 1:
                    confiteria_cliente_ingresar(nombreHilo);
                    break;
                case 2:
                    claseSki_cliente_asistir(nombreHilo);
                    break;
                default:
                    medio_cliente_irMedioElevacion(telepase, nombreHilo, accion);
                    break;
            }
            accion = random.nextInt(1, 4);
        }
        System.out.println(nombreHilo + " se retira del parque");
    }


    private boolean parque_abierto(){
        int hora = reloj.getHoras();
        return (hora>=8 && hora<=22);
    }

    //  RELACIONADO A MEDIO ELEVACION
    public void medio_cliente_irMedioElevacion(boolean telepase, String nombreHilo, int indice) throws InterruptedException{
        if (!mediosAbiertos()) return;


        personasEnMedio.incrementAndGet();
        mediosDeElevacion[indice].esquiador_ingresar(telepase, nombreHilo);
        personasEnMedio.decrementAndGet();
    }


    public void medio_embarcador_darSilla(int indice)throws InterruptedException{
        if (!mediosAbiertos() && personasEnMedio.get()==0) return;


        mediosDeElevacion[indice].embarcador_DarSilla();
    }

    //      Modularizacion del horario de apertura de los medios
    private boolean mediosAbiertos(){
        int hora = reloj.getHoras();
        return (hora>=10 && hora<17);
    }


    //  RELACIONADO A CLASES DE SKI
    //      Clientes
    public void claseSki_cliente_asistir(String nombreHilo) throws InterruptedException{
        clases.esquiador_irClase(nombreHilo);
    }


    //      Instructores
    public void claseSki_Instructor_instruir(String nombreHilo) throws InterruptedException{
        clases.instructor_darClase(nombreHilo);
    }


    //  RELACIONADO A CONFITERIA
    //      Clientes
    public void confiteria_cliente_ingresar(String nombreHilo) throws InterruptedException{
        confiteria.cliente_Ingresar(nombreHilo, random.nextBoolean());
    }


    //      Cajeros
    public void confiteria_cajero_atender(String nombreHilo) throws InterruptedException{
        confiteria.cajero_Atender(nombreHilo);
    }


    //      Cocineros
    public void confiteria_cocinero_cocinar(String nombreHilo) throws InterruptedException{
        confiteria.cocinero_Preparar(nombreHilo);
    }

    // RELACIONADO A METRICAS

    //      Medios de Elevacion

    public int get_usos_MedioElevacionX(int x){
        //  Si X esta fuera de rango, retornar 1
        return (x<0 || x>3)?-1:mediosDeElevacion[x].getUsosTotal();
    }

    //      Todo
    @Override
    public String toString(){
        String r = "---Parque Ski---\nData:\n";
        

        //  Medios de Elevacion
        r+= "\tMedios de Elevacion:\n";
        r += "\t\tAccesos:\n";

        
        int totalAccesos = 0;
        for (int i = 0; i < mediosDeElevacion.length; i++) {
            int usos = mediosDeElevacion[i].getUsosTotal();
            r += "\t\t\tMedio" + (i+1) + ":(" + usos + ")\n";
            totalAccesos += usos;
        }
        r += "\t\t\t------------------------------------\n";
        r += "\t\t\tTotal:(" + totalAccesos + ")\n\n";



        //  Clases Ski
        r+= "\tClases de Ski:\n";
        r+= "\t\tClases logradas: " + clases.get_ClasesExitosas() + "\n\n";


        //  Confitera
        r+= "\tConfiteria:\n";
        r+= "\t\tCantidad Ventas: " + confiteria.get_numeroVentas() + "\n";


        return r;
    }
}
