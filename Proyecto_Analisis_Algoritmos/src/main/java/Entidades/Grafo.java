package Entidades;

import java.awt.Color;
import java.util.*;
/**
 * clase que representa el grafo
 * @author equipo
 */
public class Grafo {
    private Map<Vertice, List<Arista>> adyacencias;
    
    /**
     * contructor que inicializa el grafo
     */
    public Grafo() {
        adyacencias = new HashMap<>();
    }
    
    /**
     * metodo que agrega un vertice al grafo
     * @param vertice grafo insertar
     */
    public void agregarVertice(Vertice vertice) {
        adyacencias.putIfAbsent(vertice, new ArrayList<>());
    }
    
    /**
     * metodo que agrega una arista dentro de 2 vertices
     * @param origen vertice de origen
     * @param destino vertice de destino
     * @param peso peso de la arista
     */
    public void agregarArista(Vertice origen, Vertice destino, double peso) {
        adyacencias.get(origen).add(new Arista(origen, destino, peso));
        adyacencias.get(destino).add(new Arista(destino, origen, peso));
    }
    
    /**
     * metodo que agrega una arista dentro de 2 vertices de forma dirigida
     * @param origen vertice de origen
     * @param destino vertice de destino
     * @param peso peso de la arista
     */
    public void agregarAristaDirigida(Vertice origen, Vertice destino, double peso) {
        adyacencias.get(origen).add(new Arista(origen, destino, peso));
    }
    
    /**
     * metodo que regresa los vecinos del vertice solicitado
     * @param vertice vertice 
     * @return lista de vecinos
     */
    public List<Vertice> getVecinos(Vertice vertice) {
        List<Vertice> vecinos = new ArrayList<>();
        List<Arista> aristas = adyacencias.get(vertice);
        if (aristas != null) {
            for (Arista arista : aristas) {
                vecinos.add(arista.getDestino());
            }
        }
        return vecinos;
    }
    
    /**
     * metodo que regresa todas las aristas actuales
     * @return lista de aristas
     */
    public List<Arista> getAristas() {
        List<Arista> todas = new ArrayList<>();
        for (List<Arista> lista : adyacencias.values()) {
            todas.addAll(lista);
        }
        return todas;
    }
    
    /**
     * metodo que regresa la arista utilizada
     * @param vertice
     * @return 
     */
    public List<Arista> getAristasUnicas(Vertice vertice) {
        return adyacencias.getOrDefault(vertice, new ArrayList<>());
    }
    
    /**
     * metodo que extrae todos los vertices del grafo
     * @return lista de vertices
     */
    public List<Vertice> getVertices() {
        return new ArrayList<>(adyacencias.keySet());
    }
    
    /**
     * metodo que reinicia el estado de todos los vertices
     * a rojo representando como no visitados
     */
    public void formatearColores() {
        for (Vertice vertice : adyacencias.keySet()) {
            vertice.setEstado(Color.RED);
        }
        
        for (Arista arista : getAristas()) {
            arista.setEstado(Color.LIGHT_GRAY);
        }
    }
}