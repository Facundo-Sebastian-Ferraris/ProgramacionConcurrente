package centroski;

public class ANSI_Colors {
    // Colores de texto
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Colores extendidos (usando la escala de 256 colores)
    public static final String ORANGE = "\u001B[38;5;208m";  // Naranja
    public static final String PINK = "\u001B[38;5;205m";    // Rosa
    public static final String LIME = "\u001B[38;5;190m";    // Lima
    public static final String TEAL = "\u001B[38;5;44m";     // Verde azulado
    public static final String MAGENTA = "\u001B[35m";       // Magenta (también disponible como PURPLE)
    public static final String INDIGO = "\u001B[38;5;54m";   // Índigo
    public static final String VIOLET = "\u001B[38;5;128m";  // Violeta
    public static final String GOLD = "\u001B[38;5;220m";    // Dorado
    public static final String SILVER = "\u001B[38;5;246m";  // Plateado
    public static final String GRAY = "\u001B[38;5;245m";    // Gris

    // Colores de fondo
    public static final String BLACK_BG = "\u001B[40m";
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String PURPLE_BG = "\u001B[45m";
    public static final String CYAN_BG = "\u001B[46m";
    public static final String WHITE_BG = "\u001B[47m";

    // Colores de fondo extendidos (usando la escala de 256 colores)
    public static final String ORANGE_BG = "\u001B[48;5;208m";  // Fondo naranja
    public static final String PINK_BG = "\u001B[48;5;205m";    // Fondo rosa
    public static final String LIME_BG = "\u001B[48;5;190m";    // Fondo lima
    public static final String TEAL_BG = "\u001B[48;5;44m";     // Fondo verde azulado
    public static final String MAGENTA_BG = "\u001B[48;5;128m"; // Fondo magenta
    public static final String INDIGO_BG = "\u001B[48;5;54m";   // Fondo índigo
    public static final String VIOLET_BG = "\u001B[48;5;128m";  // Fondo violeta
    public static final String GOLD_BG = "\u001B[48;5;220m";    // Fondo dorado
    public static final String SILVER_BG = "\u001B[48;5;246m";  // Fondo plateado
    public static final String GRAY_BG = "\u001B[48;5;245m";    // Fondo gris
    public static final String DARKGRAY_BG = "\u001B[48;5;240m";    // Fondo gris"\u001B[48;5;223m"
    public static final String PINKSKIN_BG = "\u001B[48;2;255;145;147m";    // Fondo Piel

    // Estilos adicionales
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String ITALIC = "\u001B[3m"; // Cursiva

        // Función para crear efecto arcoíris
    public static String rainbow(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // Colores del arcoíris (en orden: rojo, naranja, amarillo, verde, azul, índigo, violeta)
        String[] rainbowColors = { RED, YELLOW, GREEN, CYAN, BLUE, PURPLE, RED };
        StringBuilder result = new StringBuilder();

        // Aplica un color diferente a cada carácter
        for (int i = 0; i < text.length(); i++) {
            String color = rainbowColors[i % rainbowColors.length];
            result.append(color).append(text.charAt(i));
        }
        // Resetea el color al final
        result.append(RESET);
        return result.toString();
    }
}