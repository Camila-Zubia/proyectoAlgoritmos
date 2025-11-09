/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.awt.Color;
import java.util.*;
/**
 *
 * @author lagar
 */
public class Grafo {
    
    private Map<Vertice, List<Arista>> adyacencias;

    public Grafo() {
        adyacencias = new HashMap<>();
    }

    public void agregarVertice(Vertice v) {
        adyacencias.putIfAbsent(v, new ArrayList<>());
    }

    public void agregarArista(Vertice origen, Vertice destino, double peso) {
        adyacencias.get(origen).add(new Arista(origen, destino, peso));
    }

    public void agregarArista(Vertice origen, Vertice destino) {
        agregarArista(origen, destino, 1.0); // peso por defecto
    }

    public List<Vertice> getVecinos(Vertice v) {
        List<Vertice> vecinos = new ArrayList<>();
        List<Arista> aristas = adyacencias.get(v);
        if (aristas != null) {
            for (Arista a : aristas) {
                vecinos.add(a.getDestino());
            }
        }
        return vecinos;
    }

    public List<Arista> getAristas() {
        List<Arista> todas = new ArrayList<>();
        for (List<Arista> lista : adyacencias.values()) {
            todas.addAll(lista);
        }
        return todas;
    }

    public List<Vertice> getVertices() {
        return new ArrayList<>(adyacencias.keySet());
    }

    public void formatearColores() {
        for (Vertice vertice : adyacencias.keySet()) {
            vertice.setEstado(Color.RED);
        }
    }
}