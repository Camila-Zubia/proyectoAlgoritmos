package Logica;

import Entidades.Arista;
import Entidades.Grafo;
import Entidades.Vertice;
import java.awt.Color;
import java.util.*;
import javax.swing.SwingUtilities;
/**
 * clase que representa los algoritmos para grafos
 * @author equipo
 */
public class Algoritmos {
    private Grafo grafo;
    private PanelGrafo panel; 
    
    /**
     * Contructor que inicializa la clase
     * @param grafo grafo
     * @param panel panel del grafo
     */
    public Algoritmos(Grafo grafo, PanelGrafo panel) {
        this.grafo = grafo;
        this.panel = panel;
    }

    /**
     * metodo que representa el DFS
     * @param verticeInicio vertice donde comienza
     * @return el orden en como se recorrio
     */
    public List<Vertice> DFS(Vertice verticeInicio) {
        List<Vertice> orden = new ArrayList<>();
        Set<Vertice> visitado = new HashSet<>();
        apoyoDFS(verticeInicio, visitado, orden);
        for (Vertice v : grafo.getVertices()) {
            if (!visitado.contains(v)) {
                apoyoDFS(v, visitado, orden);
            }
        }
        return orden;
    }
    
    /**
     * metodo que apoya el metodo DFS
     * @param actual vertice actual
     * @param visitado lista de visitados
     * @param orden lista del orden de recorrido
     */
    private void apoyoDFS(Vertice actual, Set<Vertice> visitado, List<Vertice> orden) {
        visitado.add(actual);
        actual.setEstado(Color.YELLOW); 
        repintar();
        orden.add(actual);
        for (Vertice vecino : grafo.getVecinos(actual)) {
            if (!visitado.contains(vecino)) {
                apoyoDFS(vecino, visitado, orden);
            }
        }
        actual.setEstado(Color.GREEN); 
        repintar();
    }
    
    /**
     * metodo que representa el BFS
     * @param verticeInicio vertice donde comienza
     * @return el orden en como se recorrio
     */
    public List<Vertice> BFS(Vertice verticeInicio) {
        List<Vertice> orden = new ArrayList<>();
        Set<Vertice> visitado = new HashSet<>();
        Queue<Vertice> cola = new LinkedList<>();
        visitado.add(verticeInicio);
        cola.add(verticeInicio);
        verticeInicio.setEstado(Color.YELLOW);
        repintar();
        while (true) {
            while (!cola.isEmpty()) {
                Vertice actual = cola.poll();
                orden.add(actual);
                for (Vertice vecino : grafo.getVecinos(actual)) {
                    if (!visitado.contains(vecino)) {
                        visitado.add(vecino);
                        cola.add(vecino);
                        vecino.setEstado(Color.YELLOW);
                        repintar();
                    }
                }

                actual.setEstado(Color.GREEN);
                repintar();
            }
            Vertice noVisitado = null;
            for (Vertice vertice : grafo.getVertices()) {
                if (!visitado.contains(vertice)) {
                    noVisitado = vertice;
                    break;
                }
            }
            if (noVisitado == null){
                break;
            }
            visitado.add(noVisitado);
            cola.add(noVisitado);
            noVisitado.setEstado(Color.YELLOW);
            repintar();
        }
        return orden;
    }

    /**
     * metodo que representa el algoritmo de Kruskal
     * @return arbol de esparcimiento
     */
    public List<Arista> kruskal(){
     List<Arista> aristas = new ArrayList<>(grafo.getAristas());
     List<Arista> mst = new ArrayList<>();
     Map<Vertice,Vertice> padre = new HashMap<>();
     
     
        for (Vertice v: grafo.getVertices()) {
            padre.put(v, v);
        }
    
        aristas.sort(Comparator.comparingDouble(Arista::getPeso));
        for (Arista a : aristas) {
            Vertice u = a.getOrigen();
            Vertice v = a.getDestino();
            if(find(padre,u)!= find(padre,v)) {
                mst.add(a);
                union(padre,u,v);
                u.setEstado(Color.YELLOW);
                v.setEstado(Color.YELLOW);
                repintar();
            }
        }
        for(Arista a : mst){
            a.getOrigen().setEstado(Color.GREEN);
            a.getDestino().setEstado(Color.GREEN);
            repintar();
        }
        
        return mst;
    }
    
    /**
     * metodo que representa el algoritmo de dijkstra
     * @param origen vertice origen
     * @return distancias
     */
    public Map<Vertice,Double> dijkstra(Vertice origen){
        Map<Vertice, Double> distancias = new HashMap<>();
        Map<Vertice, Vertice> previo = new HashMap<>();
        PriorityQueue<Vertice> cola = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        for (Vertice v : grafo.getVertices()) {
            distancias.put(v, Double.POSITIVE_INFINITY);
            previo.put(v, null);
            v.setEstado(Color.RED);
            
        }
        distancias.put(origen, 0.0);
        cola.add(origen);
        origen.setEstado(Color.YELLOW);
        repintar();
        while(!cola.isEmpty()){
            Vertice actual = cola.poll();
            actual.setEstado(Color.GREEN);
            repintar();   
            for (Arista a : grafo.getAristas()) {
                if (a.getOrigen().equals(actual)) {
                    Vertice vecino = a.getDestino();
                    double nuevaDistancia = distancias.get(actual) + a.getPeso();
                    if (nuevaDistancia < distancias.get(vecino)) {
                        distancias.put(vecino, nuevaDistancia);
                        previo.put(vecino, actual);
                        cola.add(vecino);
                        vecino.setEstado(Color.YELLOW);
                        repintar();
                    }
                }
            }
        }
        for (Vertice v : grafo.getVertices()) {
            v.setEstado(Color.CYAN);
        }
        origen.setEstado(Color.GREEN);
        repintar();
        return distancias;
    }
    
    /**
     * metodo que repinta el frame para mostrar el proceso del grafo
     */
    private void repintar() {
        SwingUtilities.invokeLater(panel::repaint);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    
    /**
     * metodo para encontrar vertices
     * @param padre predecesor del vertice
     * @param v vertice
     * @return vertice hijo
     */
    private Vertice find(Map<Vertice, Vertice> padre, Vertice v) {
        if (padre.get(v) != v)
            padre.put(v, find(padre, padre.get(v)));
        return padre.get(v);
    }
    
    /**
     * metodo que une un padre y un hijo
     * @param padre predecesor del vertice
     * @param a vertice a
     * @param b vertice b
     */
    private void union(Map<Vertice, Vertice> padre, Vertice a, Vertice b) {
        Vertice ra = find(padre, a);
        Vertice rb = find(padre, b);
        if (ra != rb) padre.put(ra, rb);
    }

}