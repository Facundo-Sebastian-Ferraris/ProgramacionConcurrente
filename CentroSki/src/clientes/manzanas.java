/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbproject/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clientes;
import java.util.Scanner;
/**
 *
 * @author facundo
 */
public class manzanas {

    /**
     * @param args the command line arguments
     */
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
        
        input.nextLine(); // Consume leftover newline
        System.out.println("Presione Enter para la siguiente generacion o ingrese 0 para terminar");
        printArreglo(personas);
        while (true) {
            String userInput = input.nextLine();
            if (userInput.equals("0")) {
                break; // Terminate if user enters 0
            }
            if (userInput.isEmpty()) { // Proceed on Enter (empty input)
                repartir(personas);
                System.out.println("Generacion " + gen++);
                printArreglo(personas);
                System.out.println("");
            }
        }
    }
    
    public static void repartir(int m[]) {
        int aux[] = new int[m.length];
        int direccion1 = 0;
        int direccion2 = 0;

        int valor1 = 0;
        int valor2 = 0;

        for (int i = 0; i < m.length; i++) {
            valor1 = m[i] / 2;
            valor2 = m[i] / 2;
            if (m[i] % 2 != 0) {
                valor1++;
            }
            direccion1 = i - 1;
            direccion2 = i + 1;
            
            if (direccion1 < 0) {
                direccion1 = m.length - 1;
            }
            
            if (direccion2 > m.length - 1) {
                direccion2 = 0;
            }
            
            aux[direccion1] += valor1;
            aux[direccion2] += valor2;
        }
        
        // Copy aux back to m
        for (int i = 0; i < m.length; i++) {
            m[i] = aux[i];
        }
    }
    
    public static void printArreglo(int m[]) {
        System.out.print("[ ");
        for (int i = 0; i < m.length; i++) {
            System.out.print(m[i] + " ");
        }
        System.out.println("]");
    }
}