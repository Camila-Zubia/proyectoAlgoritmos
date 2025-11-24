package Entidades;

import java.awt.Color;

/**
 * Clase que representa la arista entre dos vertices
 * @author equipo 
 */
public class Arista {
    private Vertice origen;
    private Vertice destino;
    private double peso;
    private Color estado;
    
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
    
    /**
     * metodo para obtener el estado de la arista
     * @return estado de la arista
     */
    public Color getEstado() {
        return estado;
    }
    
    /**
     * metodo para setear el estado de la arista
     * @param estado nuevo estado de la aristas
     */
    public void setEstado(Color estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Arista{" +
                "origen=" + origen +
                ", destino=" + destino +
                '}';
    }
}
