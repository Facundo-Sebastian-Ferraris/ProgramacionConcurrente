/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Personas;

/**
 *
 * @author facundo
 */
public class Reloj implements Runnable {
    private int hora;
    private int minuto;
    private int intervalo;
    private boolean activo;

    public Reloj(int hora, int minuto, int intervalo){
        this.hora = hora;
        this.minuto = minuto;
        this.intervalo = intervalo;
        this.activo = true;
    }

    @Override
    public void run(){
        System.out.println("\tIniciando HILO");
        while(activo){
            
            try {
                
                Thread.sleep(this.intervalo);
                
                if (this.minuto + 1 >= 60) {
                    
                    this.minuto = 0;
                    
                    if (this.hora+1>=24) {
                        this.hora = 0;
                    } else {
                        this.hora++;
                    }
                    
                } else {               
                    this.minuto++;
                }
            } catch (InterruptedException e) {}
        }
    }
    
     public String getHoraActual() {
         return String.format("%02d:%02d", hora, minuto);
    }

    public void detener() {
        this.activo = false;
    }
}
