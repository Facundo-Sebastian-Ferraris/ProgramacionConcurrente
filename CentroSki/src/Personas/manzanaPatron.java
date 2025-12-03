package Personas;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class manzanaPatron {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int gen = 1;

        // Prompt for number of people
        System.out.println("Ingrese la cantidad de personas:");
        int numPersonas = input.nextInt();

        // Prompt for number of apples
        System.out.println("Ingrese la cantidad de manzanas:");
        int numManzanas = input.nextInt();

        // Initialize array with dynamic size and set first element
        int personas[] = new int[numPersonas];
        personas[0] = numManzanas; // Set first element to number of apples
        for (int i = 1; i < numPersonas; i++) {
            personas[i] = 0; // Initialize rest of the array with zeros
        }

        // Map to store states and detect cycles
        HashMap<String, Integer> estados = new HashMap<>();
        String estadoActual = Arrays.toString(personas);
        estados.put(estadoActual, 0);

        System.out.println("Generacion 0");
        printArreglo(personas);

        while (true) {
            repartir(personas);
            estadoActual = Arrays.toString(personas);
           // System.out.println("Generacion " + gen);
            //printArreglo(personas);

            // Check if current state was seen before
            if (estados.containsKey(estadoActual)) {
                int genAnterior = estados.get(estadoActual);
                int periodo = gen - genAnterior;
                System.out.println("¡Patrón estable detectado!");
                System.out.println("Iteraciones hasta el patrón: " + genAnterior);
                System.out.println("Período del ciclo: " + periodo);
                break;
            } else {
                estados.put(estadoActual, gen);
            }
            gen++;
        }

        input.close();
    }

    public static void repartir(int m[]) {
        int aux[] = new int[m.length];
        int direccion1, direccion2, valor1, valor2;

        for (int i = 0; i < m.length; i++) {
            valor1 = m[i] / 2;
            valor2 = m[i] / 2;
            if (m[i] % 2 != 0) {
                valor1++;
            }
            direccion1 = (i - 1 + m.length) % m.length; // Circular left
            direccion2 = (i + 1) % m.length; // Circular right

            aux[direccion1] += valor1;
            aux[direccion2] += valor2;
        }

        // Copy aux back to m
        System.arraycopy(aux, 0, m, 0, m.length);
    }

    public static void printArreglo(int m[]) {
        System.out.print("[ ");
        for (int i = 0; i < m.length; i++) {
            System.out.print(m[i] + " ");
        }
        System.out.println("]");
    }
}