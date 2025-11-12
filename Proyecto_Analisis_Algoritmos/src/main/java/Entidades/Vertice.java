package Entidades;

import java.awt.Color;

/**
 * clase que representa el Vertice
 * @author equipo
 */
public class Vertice {
    private String nombre;
    private int x, y;
    private Color estado;
    private double d;
    private Vertice π;
    
    /**
     * constructor que inicializa la clase
     * @param nombre nombre del vetize
     * @param x coordenada x
     * @param y coordenada y
     */
    public Vertice(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        this.d = 0;
        this.π = null;
    }
    
    /**
     * metodo que obtiene el nombre del vertice
     * @return nombre del vertice
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * metodo que setea un nombre al vertice
     * @param nombre nombre del vertice
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * metodo que regresa el estado en formato de color del vertice
     * @return color del vertice
     */
    public Color getEstado() {
        return estado;
    }
    
    /**
     * metodo que setea el estado en formato de color al vertice
     * @param estado color para el estado,
     */
    public void setEstado(Color estado) {
        this.estado = estado;
    }
    
    /**
     * metodo que regresa la coordenada x del vertice
     * @return cordenada x
     */
    public int getX() {
        return x;
    }
    
    /**
     * metodo que setea una coordenada x para el vertice
     * @param x coordenada x
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * metodo que regresa la coordenada y del vertice
     * @return cordenada y
     */
    public int getY() {
        return y;
    }
    
    /**
     * metodo que setea una coordenada y para el vertice
     * @param y coordenada y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * metodo que obtiene la distancia del vertice
     * @return distancia
     */
    public double getD() {
        return d;
    }
    
    /**
     * metodo que setea la distancia del vertice
     * @param d distancia
     */
    public void setD(double d) {
        this.d = d;
    }
    
    /**
     * metodo que obtiene el vértice predecesor en el camino más corto.
     * @return predecesor
     */
    public Vertice getΠ() {
        return π;
    }
    
    /**
     * metodoo qie setea el vertice predecesor
     * @param π predecesor
     */
    public void setΠ(Vertice π) {
        this.π = π;
    }
}
