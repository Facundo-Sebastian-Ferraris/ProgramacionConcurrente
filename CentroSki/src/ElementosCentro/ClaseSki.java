package ElementosCentro;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ClaseSki {
    private final Lock mutex[];
    private final Condition
        clientes_esperando[],
        nuevos_esperando[];

    private int
        clientesEsperando[],
        instructoresEsperando[];

    private boolean
        hayGrupo[];

    private AtomicInteger
        clasesExitosas = new AtomicInteger(0),
        ingresos = new AtomicInteger(0),
        impaciencia = new AtomicInteger(0);

    private final CyclicBarrier GENERICO_IniciarClase[];
    private final CyclicBarrier     RENDEVOUZ_FinalizadaClase[];


    // CONSTRUCTOR 🏗️
    public ClaseSki() {
        // Inicialización de arrays
        mutex = new ReentrantLock[2];
        clientes_esperando = new Condition[2];
        nuevos_esperando = new Condition[2];
        clientesEsperando = new int[2];
        instructoresEsperando = new int[2];
        hayGrupo = new boolean[2];
        GENERICO_IniciarClase = new CyclicBarrier[2];
        RENDEVOUZ_FinalizadaClase = new CyclicBarrier[2];

        for (int i = 0; i < 2; i++) {
            mutex[i] = new ReentrantLock();
            clientes_esperando[i] = mutex[i].newCondition();
            nuevos_esperando[i] = mutex[i].newCondition();
            clientesEsperando[i] = 0;
            instructoresEsperando[i] = 0;
            hayGrupo[i] = false;

            final boolean esSki = (i == 0);
            GENERICO_IniciarClase[i] = new CyclicBarrier(5, () -> callbackBarrera(esSki));
            RENDEVOUZ_FinalizadaClase[i] = new CyclicBarrier(5);
        }
    }




    public void esquiador_irClase(String nombreHilo, boolean ski) throws InterruptedException{
        int i = ski?0:1;
        ingresos.addAndGet(120);        //	 Paga al ingresar 💰

        if (!esquiadorFormaGrupo(nombreHilo,i)) return;


        // Cuando ya tenga el grupo formado ahora toca esperar al instructor ⏳
        try {
            GENERICO_IniciarClase[i].await();
            printGUI( nombreHilo + " en clase!!!");
            RENDEVOUZ_FinalizadaClase[i].await();
            printGUI( nombreHilo + " finalizada clase!!!");
        } catch (InterruptedException | BrokenBarrierException e1) {
            e1.printStackTrace();
        }
    }


    private boolean esquiadorFormaGrupo(String nombreHilo, int i) throws InterruptedException{
        boolean lograEsperar = true;    //	 Auxiliar para determinar grupos 🆗


        //	 Formacion de grupo 👥
        mutex[i].lock();
        try  {
            while (clientesEsperando[i] == 4) {
                nuevos_esperando[i].await();
            }

            clientesEsperando[i]++;
            printGUI( nombreHilo + " esta interesado en instruirse en "+((i==0)?"ski":"snow")+" (" + clientesEsperando[i] + ")");
            clientes_esperando[i].signalAll();      // resetea la "paciencia del hilo" 🔄
            while(clientesEsperando[i] < 4 && lograEsperar){
                lograEsperar = clientes_esperando[i].await(3, TimeUnit.SECONDS);
            }

            lograEsperar = clientesEsperando[i] == 4;
            if (!lograEsperar) {
                printGUI( nombreHilo + " se le agota la paciencia y se va");
                ingresos.addAndGet(-120);       //	 Reembolso 💸
                clientesEsperando[i]--;
                impaciencia.incrementAndGet();
                return false;
            }
        } finally {
            mutex[i].unlock();
        }
        return true;
    }




    public void instructor_darClase(String nombreHilo, boolean ski) throws InterruptedException{
        int i = ski?0:1;
        try {
            GENERICO_IniciarClase[i].await();
            printGUI( nombreHilo + " lleva a cabo la clase!!!");
            Thread.sleep(3000);
            RENDEVOUZ_FinalizadaClase[i].await();
            printGUI( nombreHilo + " concluye la clase!!!");
        } catch (InterruptedException | BrokenBarrierException e1) {
            e1.printStackTrace();
        }
        clasesExitosas.incrementAndGet();
    }




    private void callbackBarrera(boolean ski){
        int i = ski?0:1;
        printGUI("("+Thread.currentThread().getName()+")\tLibera a instructor" );
        mutex[i].lock();
        try{
            clientesEsperando[i]-=4;
            nuevos_esperando[i].signalAll();
        }finally{
            mutex[i].unlock();
        }

    }




    public int get_ClasesExitosas(){
        return  clasesExitosas.get();
    }

    public int get_Ingresos(){
        return ingresos.get();
    }

    public int get_Impaciencia(){
        return impaciencia.get();
    }

    private void printGUI(String r){
        ImpresionGUI.print("Clases de Ski", r);
    }
}