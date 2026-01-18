package centroski;

public class TestColores {
    public static void main(String[] args) {
        System.out.println("=== Colores de Texto Básicos ===");
        System.out.println(ANSI_Colors.BLACK + "Hola Mundo Negro" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.RED + "Hola Mundo Rojo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.GREEN + "Hola Mundo Verde" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.YELLOW + "Hola Mundo Amarillo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.BLUE + "Hola Mundo Azul" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.PURPLE + "Hola Mundo Morado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.CYAN + "Hola Mundo Cian" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.WHITE + "Hola Mundo Blanco" + ANSI_Colors.RESET);

        System.out.println("\n=== Colores de Texto Extendidos ===");
        System.out.println(ANSI_Colors.ORANGE + "Hola Mundo Naranja" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.PINK + "Hola Mundo Rosa" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.LIME + "Hola Mundo Lima" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.TEAL + "Hola Mundo Verde Azulado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.MAGENTA + "Hola Mundo Magenta" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.INDIGO + "Hola Mundo Índigo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.VIOLET + "Hola Mundo Violeta" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.GOLD + "Hola Mundo Dorado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.SILVER + "Hola Mundo Plateado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.GRAY + "Hola Mundo Gris" + ANSI_Colors.RESET);

        System.out.println("\n=== Colores de Fondo Básicos ===");
        System.out.println(ANSI_Colors.BLACK_BG + "Hola Mundo con Fondo Negro" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.RED_BG + "Hola Mundo con Fondo Rojo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.GREEN_BG + "Hola Mundo con Fondo Verde" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.YELLOW_BG + "Hola Mundo con Fondo Amarillo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.BLUE_BG + "Hola Mundo con Fondo Azul" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.PURPLE_BG + "Hola Mundo con Fondo Morado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.CYAN_BG + "Hola Mundo con Fondo Cian" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.WHITE_BG + "Hola Mundo con Fondo Blanco" + ANSI_Colors.RESET);

        System.out.println("\n=== Colores de Fondo Extendidos ===");
        System.out.println(ANSI_Colors.ORANGE_BG + "Hola Mundo con Fondo Naranja" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.PINK_BG + "Hola Mundo con Fondo Rosa" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.LIME_BG + "Hola Mundo con Fondo Lima" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.TEAL_BG + "Hola Mundo con Fondo Verde Azulado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.MAGENTA_BG + "Hola Mundo con Fondo Magenta" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.INDIGO_BG + "Hola Mundo con Fondo Índigo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.VIOLET_BG + "Hola Mundo con Fondo Violeta" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.GOLD_BG + "Hola Mundo con Fondo Dorado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.SILVER_BG + "Hola Mundo con Fondo Plateado" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.GRAY_BG + "Hola Mundo con Fondo Gris" + ANSI_Colors.RESET);

        System.out.println("\n=== Combinaciones de Color y Estilo ===");
        System.out.println(ANSI_Colors.BOLD + ANSI_Colors.RED + "Hola Mundo en Negrita y Rojo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.UNDERLINE + ANSI_Colors.BLUE + "Hola Mundo Subrayado y Azul" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.ITALIC + ANSI_Colors.GREEN + "Hola Mundo en Cursiva y Verde" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.BOLD + ANSI_Colors.ORANGE + "Hola Mundo en Negrita y Naranja" + ANSI_Colors.RESET);

        System.out.println("\n=== Efecto Arcoíris ===");
        System.out.println(ANSI_Colors.rainbow("Hola Mundo con Efecto Arcoíris"));
        
        System.out.println("\n=== Combinación de Color de Texto y Fondo ===");
        System.out.println(ANSI_Colors.ORANGE + ANSI_Colors.BLACK_BG + "Texto Naranja con Fondo Negro" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.WHITE + ANSI_Colors.ORANGE_BG + "Texto Blanco con Fondo Naranja" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.BLUE + ANSI_Colors.YELLOW_BG + "Texto Azul con Fondo Amarillo" + ANSI_Colors.RESET);
        System.out.println(ANSI_Colors.BLACK + ANSI_Colors.GOLD_BG + "Texto Negro con Fondo Dorado" + ANSI_Colors.RESET);
    }
}