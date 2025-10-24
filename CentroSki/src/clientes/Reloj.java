/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientes;

/**
 *
 * @author facundo
 */
public class Reloj extends Thread {
    private int hora;
    private int minuto;
    private boolean activo;

    public Reloj(int hora, int minuto){
        this.hora = hora;
        this.minuto = minuto;
    }

    @Override
    public void run(){
        
        while(activo){
            
            try {
                
                Thread.sleep(100);
                
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
        return this.hora+":"+this.minuto;
    }

    public void detener() {
        this.activo = false;
    }
}
