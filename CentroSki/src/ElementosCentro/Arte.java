package ElementosCentro;

import centroski.ANSI_Colors;

public class Arte {
  
   private String s = "  ";
   private String r = ANSI_Colors.RED_BG + s + ANSI_Colors.RESET;  // Rojo
   private String g = ANSI_Colors.GREEN_BG + s + ANSI_Colors.RESET;  // Verde
   private String b = ANSI_Colors.BLUE_BG + s + ANSI_Colors.RESET;  // Azul
   private String m = ANSI_Colors.MAGENTA_BG + s + ANSI_Colors.RESET;  // Magenta
   private String c = ANSI_Colors.CYAN_BG + s + ANSI_Colors.RESET;  // Cyan
   private String w = ANSI_Colors.WHITE_BG + s + ANSI_Colors.RESET;  // Blanco
   private String x = ANSI_Colors.BLACK_BG + s + ANSI_Colors.RESET;  // Negro
   private String y = ANSI_Colors.GRAY_BG + s + ANSI_Colors.RESET;  // Gris Claro
   private String z = ANSI_Colors.DARKGRAY_BG + s + ANSI_Colors.RESET;  // Gris Oscuro
   private String p = ANSI_Colors.PINKSKIN_BG + s + ANSI_Colors.RESET;  // Gris Oscuro
   private String n = "\n";
   
   public Arte(){}
   // CENTRO SKI
   public String draw(){
      String q = "";
      q += ANSI_Colors.BOLD+ANSI_Colors.rainbow("BIENVENIDOS AL CENTRO DE SKI!!!\n\n");
      q += s + s + s + w + w + w + b + b + s + s + s + s + s + s + s + s + w + w + w + m + m + s + s + s + s + s + n;
      q += s + s + w + w + w + w + w + b + b + s + s + s + s + s + s + w + w + w + w + w + m + m + s + s + s + s + n;
      q += s + s + w + x + x + w + w + w + b + b + s + s + s + s + s + w + x + x + w + w + w + m + m + s + s + s + n;
      q += s + s + w + x + x + x + w + w + b + b + b + s + s + s + s + w + x + x + x + w + w + m + m + m + s + s + n;
      q += s + s + s + p + p + x + w + w + b + b + b + s + s + s + s + s + p + p + x + w + w + m + m + m + s + s + n;
      q += s + s + s + p + x + p + w + w + b + b + b + s + s + s + s + s + p + x + p + w + w + m + m + m + s + s + n;
      q += s + s + p + p + x + p + w + w + b + b + b + s + s + s + s + p + p + x + p + w + w + m + m + m + s + s + n;
      q += s + s + p + p + p + b + w + w + b + b + b + s + s + s + s + p + p + p + m + w + w + m + m + m + s + s + n;
      q += s + w + w + w + w + x + w + w + b + b + s + s + s + s + w + w + w + w + x + w + w + m + m + s + s + s + n;
      q += s + s + w + w + w + x + x + b + b + s + s + s + s + s + s + w + w + w + x + x + m + m + s + s + s + s + n;
      q += s + s + b + b + b + b + x + x + b + b + x + s + s + s + s + m + m + m + m + x + x + m + m + x + s + s + n;
      q += s + s + w + b + p + p + p + x + b + b + b + s + s + s + s + w + m + p + p + p + x + m + m + m + s + s + n; 
      q += s + w + b + p + x + x + p + p + x + b + b + s + s + s + w + m + p + x + x + p + p + x + m + m + s + s + n;
      q += s + w + b + p + x + p + p + p + x + p + p + s + s + s + w + m + p + x + p + p + p + x + p + p + s + s + n;
      q += b + b + b + p + x + p + p + p + p + p + p + p + s + m + m + m + p + x + p + p + p + p + p + p + p + s + n;
      q += b + b + b + p + p + p + p + p + b + b + b + s + s + m + m + m + p + p + p + p + p + m + m + m + s + s + n;
      q += s + b + b + p + p + p + p + p + b + b + b + b + s + s + m + m + p + p + p + p + p + m + m + m + m + s + n;
      q += s + s + b + b + p + p + p + b + b + b + x + s + s + s + s + m + m + p + p + p + m + m + m + x + s + s + n;
      q += s + s + w + w + s + s + w + w + w + w + x + s + s + s + s + w + w + s + s + w + w + w + w + x + s + s + n;
      q += s + w + w + w + s + w + w + w + w + w + x + s + s + s + w + w + w + s + w + w + w + w + w + x + s + s + n;
      q += ANSI_Colors.BOLD+ANSI_Colors.rainbow("\n\t\t\tPor Facundo Ferraris FAI[3820]\n\n");


      return q;
   }

}
