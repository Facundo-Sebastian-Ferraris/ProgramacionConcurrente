package ElementosCentro;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Complejo {
    private final Random random = new Random();
    //  ELEMENTOS CLAVE: 🏗️

    //  GLOBAL 🌍
    private final AtomicInteger
        personasEnParque = new AtomicInteger();

    //  Medio Elevacion: 🚡
    //      Mecanismo: Monitores + Mutex 🔒
    private final MedioElevacion[]
        mediosDeElevacion = new MedioElevacion[4];


    private AtomicInteger
        personasEnMedio = new AtomicInteger(0);


    //  Clases de Ski: ⛷️
    //      Mecanismo: Lockers 🔐
    private final ClaseSki
        clases;


    private AtomicInteger
        personasEnClase = new AtomicInteger(0);

    //  Confiteria 🍰
    //      Mecanismo: Semaforos Genericos + Rendevouz 🚦
    private final Confiteria
        confiteria;

    private AtomicInteger
        personasEnConfiteria = new AtomicInteger(0);

    //  Utilitario ⏰
    private final Reloj
        reloj;



    //	 CONSTRUCTOR 🏗️
    public Complejo(Reloj reloj, MedioElevacion[] mediosDeElevacion, ClaseSki clases, Confiteria confiteria){
        this.reloj = reloj;
        for (int i = 0; i < this.mediosDeElevacion.length; i++) {
            this.mediosDeElevacion[i] = mediosDeElevacion[i];
        }
        this.clases = clases;
        this.confiteria = confiteria;
    }

    //  RELACIONADO A COMPLEJO 🏔️


    //  DECISION ALEATORIA 🎲
    //      1.	 ir confiteria 🍰
    //      2.	 clase ski ⛷️
    //      3.	 subir medio elevacion 🚡
    //      4.	 irse 🚪
    public void complejo_cliente_ingreso(String nombreHilo) throws InterruptedException{
        reloj.ventana(8, 22, 0, 0);


        //  Datos iniciales de acceso del cliente 🧍
        boolean arriba = false;
        boolean salir = false;
        personasEnParque.incrementAndGet();			    //	 +1 Cliente que accedio 👤
        int accion = 0;								    //	 Cliente decide que hacer 🤔
        boolean telepase = random.nextBoolean();        //	 Cliente ingresa al parque con o sin telepase 🎫


        //	 Cliente se queda en parque hasta que lo decida, o hasta que el parque cierre ⏳
        while (parque_abierto() && !salir) {
            if (arriba) {
                accion = random.nextInt(1, 4);
                switch (accion) {
                    case 1: confiteria_cliente_ingresar(nombreHilo); break;
                    case 2: medio_cliente_irMedioElevacion(telepase, nombreHilo, random.nextInt(0,4),false); break;
                    case 3: esquiar(nombreHilo); arriba = false;
                }
            } else {
                accion = random.nextInt(1, 3);
                switch (accion) {
                    case 1: claseSki_cliente_asistir(nombreHilo); break;
                    case 2: medio_cliente_irMedioElevacion(telepase, nombreHilo, random.nextInt(0,4),true); break;
                }
            }
            salir = random.nextBoolean();
        }


        ImpresionGUI.print("Complejo", nombreHilo + " se retira del parque");
        personasEnParque.decrementAndGet();     //	 -1 Cliente que accedio 👋
    }


    private boolean parque_abierto(){
        int hora = reloj.getHoras();
        return (8<=hora && hora<22);
    }

    private void esquiar(String nombreHilo){
        ImpresionGUI.print("Complejo", nombreHilo + " baja esquiando!!!");
    }




    //	 RELACIONADO A MEDIO ELEVACION 🚡
    public void medio_cliente_irMedioElevacion(boolean telepase, String nombreHilo, int indice, boolean subida) throws InterruptedException{
        if (!mediosAbiertos()) return;      //	 Si los medios no estan abiertos entonces no es posible entrar ⛔


        ImpresionGUI.print("Medio Elevacion", nombreHilo + " va al medio n°" + (indice + 1));
        personasEnMedio.incrementAndGet();
        mediosDeElevacion[indice].esquiador_ingresar(telepase, nombreHilo, subida);
        personasEnMedio.decrementAndGet();
    }




    public void medio_embarcador_darSilla(int indice, String nombreHilo, boolean subida)throws InterruptedException{
        reloj.ventana(10, 17, 0, 0);


        personasEnMedio.incrementAndGet();
        mediosDeElevacion[indice].aerosilla_Viaje(nombreHilo, subida);
        personasEnMedio.decrementAndGet();
    }

    //		 Modularizacion del horario de apertura de los medios ⏰
    private boolean mediosAbiertos(){
        int hora = reloj.getHoras();
        return (hora>=10 && hora<17);
    }




    //	 RELACIONADO A CLASES DE SKI ⛷️
    //		 Clientes 👥
    public void claseSki_cliente_asistir(String nombreHilo) throws InterruptedException{
        personasEnClase.incrementAndGet();
        clases.esquiador_irClase(nombreHilo, random.nextBoolean());
        personasEnClase.decrementAndGet();
    }




    //		 Instructores 👨‍🏫
    public void claseSki_Instructor_instruir(String nombreHilo, boolean ski) throws InterruptedException{
        clases.instructor_darClase(nombreHilo, ski);
    }




    //	 RELACIONADO A CONFITERIA 🍰
    //		 Clientes 👥
    public void confiteria_cliente_ingresar(String nombreHilo) throws InterruptedException{
        personasEnConfiteria.incrementAndGet();
        confiteria.cliente_Ingresar(nombreHilo, random.nextBoolean());
        personasEnConfiteria.decrementAndGet();
    }




    //		 Cajeros 💵
    public void confiteria_cajero_atender(String nombreHilo) throws InterruptedException{
        confiteria.cajero_Atender(nombreHilo);
    }




    //		 Cocineros 👨‍🍳
    public void confiteria_cocinero_cocinar(String nombreHilo) throws InterruptedException{
        confiteria.cocinero_Preparar(nombreHilo);
    }




    // RELACIONADO A METRICAS 📊
    //		 RELOJ ⏰
    public void complejo_reloj_simular(int intervalos) throws InterruptedException{
        while (reloj.getHoras()<20 || personasEnParque.get() > 0) {
            reloj.incrementar_Minuto();
            Thread.sleep(intervalos);
        }
    }

    @Override
    public String toString(){
        String
            linea="\n---------------------------\n",
            r ="";
        r+=getTiempo() + "\n";

        String
            estadoParque = parque_abierto()?"Abierto":"Cerrado",
            estadoMedios = mediosAbiertos()?"Abierto":"Cerrado";
        int
            clasesExitosas = clases.get_ClasesExitosas(),
            ingresosClase = clases.get_Ingresos(),
            impaciencia = clases.get_Impaciencia();
        r+=
            "CENTRO SKI\t<"+estadoParque+">\n" +
            "Personas: " + personasEnParque.get() +
            linea +
            "Clase de Ski: \n" +
            "Personas: " + personasEnClase.get() + "\n" +
            "Impacientes: " + impaciencia + "\n" +
            "Clases exitosas: " + clasesExitosas + " (total teorico: "+ (clasesExitosas*4*120) +")\n" +
            "Ingresos: $" + ingresosClase + " ("+ (((clasesExitosas*4*120)- ingresosClase)/120) + " no pagaron)\n" +
            linea +
            "Confiteria:\n" +
            "Personas: " + personasEnConfiteria.get() + "\n" +
            "Ventas: " + confiteria.get_numeroVentas() +
            linea +
            "Medios de Elevacion\t<"+estadoMedios+">\n" +
            "Personas: " + personasEnMedio.get() + "\n";

        for (int i = 0; i < mediosDeElevacion.length; i++) {
            r +=
                "Medio " + (i+1) +
                ":\n\tMolinetes: " + mediosDeElevacion[i].getNumMolinetes()+
                ":\n\tUsos: " + mediosDeElevacion[i].getUsosTotal() +
                "\n\tPersonas: " + mediosDeElevacion[i].getPersonasEsperando() + "\n";
        }


        return r;
    }

    private String getTiempo(){
        int[] time = reloj.getTiempo();
        return String.format("%02d:%02d", time[1], time[2]);
    }
}
