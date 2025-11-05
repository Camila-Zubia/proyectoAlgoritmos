/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public Vertice getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return origen.getNombre() + " -> " + destino.getNombre() + " (" + peso + ")";
    }
}
