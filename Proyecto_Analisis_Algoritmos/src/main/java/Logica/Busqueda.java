package Logica;

import Entidades.Arista;
import Entidades.Grafo;
import Entidades.Vertice;
import java.awt.Color;
import java.util.*;
import javax.swing.SwingUtilities;

public class Busqueda {
    private Grafo grafo;
    private PanelGrafo panel; 
    
    public Busqueda(Grafo grafo, PanelGrafo panel) {
        this.grafo = grafo;
        this.panel = panel;
    }

    
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
    
    public Map<Vertice,Double> dijkstra(Vertice origen){
     Map<Vertice, Double> distancias = new HashMap<>();
     Map<Vertice, Vertice> previo = new HashMap<>();
     PriorityQueue<Vertice> cola = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
     
     // Inicializar distancias 
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
        // Pintar el resultado final 
        for (Vertice v : grafo.getVertices()) {
            v.setEstado(Color.CYAN);
        }
        origen.setEstado(Color.GREEN);
        repintar();
        return distancias;
    }
    
    
    private void repintar() {
        SwingUtilities.invokeLater(panel::repaint);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    
    private Vertice find(Map<Vertice, Vertice> padre, Vertice v) {
    if (padre.get(v) != v)
        padre.put(v, find(padre, padre.get(v)));
    return padre.get(v);
}

private void union(Map<Vertice, Vertice> padre, Vertice a, Vertice b) {
    Vertice ra = find(padre, a);
    Vertice rb = find(padre, b);
    if (ra != rb) padre.put(ra, rb);
}

}