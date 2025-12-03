/*
Medios de Elevación 🚡

El complejo cuenta con 4 medios de elevación, y cada uno tiene un grupo de n molinetes (donde n puede variar entre 1 y 4)
que dan acceso al medio a los esquiadores.

📊 Contador: La suma del uso de todos los molinetes de un medio determina la cantidad de veces que fue utilizado.
🕒 Horario: Los medios de elevación están habilitados desde las 10:00 hasta las 17:00, durante las cuales los esquiadores tienen acceso ilimitado.
🎿 Esquiadores: Cada esquiador experimentado esquía por un tiempo, utiliza los medios de elevación, descansa, visita la confitería y continúa esquiando.

 */
package ElementosCentro;
import static centroski.ANSI_Colors.*;
/**
 *
 * @author facundo
 */
public class MedioElevacion {
    private String nombre;
    private Molinete molinetes[];

    public MedioElevacion(String nombre, int cantidadMolinetes){
        
        if (cantidadMolinetes < 1 || cantidadMolinetes > 4) {
            throw new IllegalArgumentException("La cantidad de molinetes debe ser entre 1 y 4!");
        }
        
        this.nombre = nombre;
        this.molinetes = new Molinete[cantidadMolinetes];
        for (int i = 0; i < molinetes.length; i++) {
            molinetes[i] = new Molinete();
        }
    }

    @Override
    public String toString(){
        String r = GREEN + "Nombre:\t"+ rainbow(this.nombre) +
                CYAN + "\nNumero de Molinetes:\t" + YELLOW + this.molinetes.length;
        return r;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getUsosTotal() {
        int r = 0;
        for (int i = 0; i < molinetes.length; i++) {
            r += this.molinetes[i].getUsosTotal();
        }
        return r;
    }

}
