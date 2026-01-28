package ElementosCentro;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImpresionGUI {

    // Almacena las ventanas por nombre
    private static final Map<String, VentanaGUI> ventanas = new ConcurrentHashMap<>();

    // Configuración por defecto
    private static final int ANCHO = 400;
    private static final int ALTO = 300;
    private static final Color BG = Color.BLACK;
    private static final Color FG = Color.GREEN;

    /**
     * Imprime un mensaje en una ventana con el nombre dado.
     * Si no existe, la crea.
     *
     * @param nombre   Nombre único de la ventana (ej: "Parque")
     * @param mensaje  Mensaje a agregar (ej: "ingreso cliente")
     */
    public static void print(String nombre, String mensaje) {
        VentanaGUI ventana = ventanas.computeIfAbsent(nombre, k -> new VentanaGUI(nombre, ANCHO, ALTO, BG, FG));

        ventana.agregarTexto(mensaje + "\n");
    }

    /**
     * Cierra una ventana por nombre
     */
    public static void cerrar(String nombre) {
        VentanaGUI ventana = ventanas.remove(nombre);
        if (ventana != null) {
            ventana.cerrarVentana();
        }
    }

    /**
     * Cierra todas las ventanas
     */
    public static void cerrarTodo() {
        ventanas.values().forEach(VentanaGUI::cerrarVentana);
        ventanas.clear();
    }

    public static VentanaGUI getGUI(String nombre){
        return ventanas.get(nombre);
    }
}   