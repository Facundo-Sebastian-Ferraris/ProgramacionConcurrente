package ElementosCentro;


import javax.swing.*;
import java.awt.*;


//  Clase para crear una ventana basica para imprimir texto
public class VentanaGUI {
    private JFrame frame;
    private JTextArea textArea;
    

    //  CONSTRUCTOR
    public VentanaGUI(String titulo, int x, int y, Color bg, Color fg) {
        inicializarGUI(titulo, x, y, bg, fg);
    }
    
    
    private void inicializarGUI(String titulo, int x, int y, Color bg, Color fg) {
        //  Crear la ventana principal
        frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(x, y);
        frame.setLocationRelativeTo(null);      //  Centrar ventana
        

        //  Crear area de texto
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(15, 15, 15, 15));
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        textArea.setBackground(bg);
        textArea.setForeground(fg);
        
        //  Crear Scroll
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        //  Agregar Scroll
        frame.add(scrollPane, BorderLayout.CENTER);
        
        //  Abrir Ventana
        frame.setVisible(true);
    }
    
    
    //  Refrescar ventana con nuevo texto
    public void actualizarTexto(String newText) {
        SwingUtilities.invokeLater(() -> {
            textArea.setText(newText);
        });
    }
    
   
    //  Agregar mas texto
    public void agregarTexto(String textToAdd) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(textToAdd);
            // Scroll to the bottom
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
    
    
    //  Cerrar ventana
    public void cerrarVentana() {
        SwingUtilities.invokeLater(() -> {
            frame.dispose();
        });
    }
    

    //  Mostrar ventana
    public void mostrarVentana() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
    
    
    //  Ocultar ventana
    public void ocultarVentana() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
        });
    }

    
    //  Cambiar fondo
    public void setBackground(Color c){
        textArea.setBackground(c);
    }


    //  Cambiar texto
    public void setForeground(Color c){
        textArea.setForeground(c);
    }

    public void setResolution(int x, int y){
        frame.setSize(x, y);
    }

     public void setPosition(int x, int y){
        frame.setLocation(x, y);
    }
}