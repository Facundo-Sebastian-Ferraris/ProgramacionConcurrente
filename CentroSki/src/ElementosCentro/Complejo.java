package ElementosCentro;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
        printGUI(nombreHilo + "entra al centro de ski!");


        //  Datos iniciales de acceso del cliente 🧍
        boolean arriba = false;
        boolean salir = false;
        personasEnParque.incrementAndGet();			    //	 +1 Cliente que accedio 👤
        int visitas = 0;
        int accion = 0;								    //	 Cliente decide que hacer 🤔
        boolean telepase = random.nextBoolean();        //	 Cliente ingresa al parque con o sin telepase 🎫


        //	 Cliente se queda en parque hasta que lo decida, o hasta que el parque cierre ⏳
        while (parque_abierto() && !salir) {
            if (arriba) {
                accion = random.nextInt(1, 4);
                switch (accion) {
                    case 1: confiteria_cliente_ingresar(nombreHilo); break;
                    case 2: arriba = medio_cliente_irMedioElevacion(telepase, nombreHilo, random.nextInt(0,4),false) ? false : !esquiar(nombreHilo); break;
                    case 3: arriba = !esquiar(nombreHilo); break;
                }
            } else {
                accion = random.nextInt(1, 3);
                switch (accion) {
                    case 1: claseSki_cliente_asistir(nombreHilo); break;
                    case 2: arriba = medio_cliente_irMedioElevacion(telepase, nombreHilo, random.nextInt(0,4),true); break;
                }
                if (visitas>5) {
                    salir = random.nextBoolean();
                }
            }
            visitas ++;
        }


        printGUI(nombreHilo + " se retira del parque");
        personasEnParque.decrementAndGet();     //	 -1 Cliente que accedio 👋
    }


    private boolean parque_abierto(){
        int hora = reloj.getHoras();
        return (8<=hora && hora<22);
    }

    private boolean esquiar(String nombreHilo){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printGUI(nombreHilo + " baja esquiando!!!");
        return true;
    }




    //	 RELACIONADO A MEDIO ELEVACION 🚡
    public boolean medio_cliente_irMedioElevacion(boolean telepase, String nombreHilo, int indice, boolean subida) throws InterruptedException{
        personasEnMedio.incrementAndGet();
        boolean r = mediosDeElevacion[indice].esquiador_ingresar(telepase, nombreHilo, subida);
        personasEnMedio.decrementAndGet();
        return r;
    }




    public void medio_gestor_darSilla(int indice, String nombreHilo)throws InterruptedException{
        System.out.println("esperando para abrir");
        reloj.ventana(10, 17,0,0);
        System.out.println("abierto!");


        printGUI(nombreHilo + " iniciando gestor");
        MedioElevacion m = mediosDeElevacion[indice];
        while (m.permaneceGestor()) {
            mediosDeElevacion[indice].gestor_Aerosillas(nombreHilo);
        }
         printGUI(nombreHilo + " se retira");
    }

    //		 Modularizacion del horario de apertura de los medios ⏰
    private boolean mediosAbiertos(){
        int hora[] = reloj.getTiempo();
        int horarioInicio = 10*60-1; // 9:59
        int horarioCierre = 17; // 17:00
        return (((hora[1]*60+hora[2])>= horarioInicio) && hora[1]<horarioCierre);
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
        VentanaGUI  v= ImpresionGUI.getGUI("Complejo Datos");
        while (reloj.getHoras()<20 || personasEnParque.get() > 0) {
            reloj.incrementar_Minuto();
            Thread.sleep(intervalos);
            if (!mediosAbiertos()) {
                for (MedioElevacion medioElevacion : mediosDeElevacion) {
                    medioElevacion.cerrar();
                }
            }else{
                for (MedioElevacion medioElevacion : mediosDeElevacion) {
                    medioElevacion.abrir();
                }
            }
            if (reloj.getMinutos()%20 == 0) {
                v.actualizarTexto(this.toString());
            }
        }
        v.actualizarTexto(this.toString());
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
            "Impacientes: " + impaciencia + "(total perdido:$"+(impaciencia*120)+"\n" +
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


    private void printGUI(String r){
        ImpresionGUI.print("Complejo", r);
    }
}
