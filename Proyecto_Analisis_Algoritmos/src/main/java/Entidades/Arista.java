package Entidades;

/**
 * Clase que representa la arista entre dos vertices
 * @author equipo 
 */
public class Arista {
    private Vertice origen;
    private Vertice destino;
    private double peso;
    
    /**
     * constructor que inicializa la la clase arista
     * @param origen vertice de origen
     * @param destino vertice de destino
     * @param peso valor de la arista
     */
    public Arista(Vertice origen, Vertice destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }
    
    /**
     * metodo para regresar el vertice origen de la arista
     * @return vertice de origen
     */
    public Vertice getOrigen() {
        return origen;
    }
    
    /**
     * metodo para regresar el vertice destino de la arista
     * @return vertice de destino
     */
    public Vertice getDestino() {
        return destino;
    }

    /**
     * metodo que regresa el peso de la arista
     * @return peso de la arista
     */
    public double getPeso() {
        return peso;
    }
    
    /**
     * metodo para setear el peso de la arista
     * @param peso peso de la arista
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }
}
