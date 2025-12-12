package ElementosCentro;

public class Pedido {
    private final String comida;
    private final boolean conPostre;

    public Pedido(String comida, boolean conPostre){
        this.comida = comida;
        this.conPostre = conPostre;
    }

    public String getComida(){
        return comida;
    }

    public boolean getConPostre(){
        return conPostre;
    }
}
