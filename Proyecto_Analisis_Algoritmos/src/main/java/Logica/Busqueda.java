package Logica;

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
        return orden;
    }

    private void apoyoDFS(Vertice actual, Set<Vertice> visitado, List<Vertice> orden) {
        visitado.add(actual);
        actual.setEstado(Color.YELLOW);
        orden.add(actual);
        repintarConRetraso();
        for (Vertice vecino : grafo.getVecinos(actual)) {
            if (!visitado.contains(vecino)) {
                apoyoDFS(vecino, visitado, orden);
            }
        }
        actual.setEstado(Color.GREEN);
        repintarConRetraso();
    }
    
    public List<Vertice> BFS(Vertice verticeInicio) {
        List<Vertice> orden = new ArrayList<>();
        Set<Vertice> visitado = new HashSet<>();
        Queue<Vertice> cola = new LinkedList<>();
        visitado.add(verticeInicio);
        cola.add(verticeInicio);
        verticeInicio.setEstado(Color.YELLOW);
        repintarConRetraso();
        while (!cola.isEmpty()) {
            Vertice actual = cola.poll();
            orden.add(actual);
            for (Vertice vecino : grafo.getVecinos(actual)) {
                if (!visitado.contains(vecino)) {
                    visitado.add(vecino);
                    cola.add(vecino);
                    vecino.setEstado(Color.YELLOW);
                    repintarConRetraso();
                }
            }

            actual.setEstado(Color.GREEN);
            repintarConRetraso();
        }

        return orden;
    }
    
    public List<Vertice> DFSCompleto() {
        List<Vertice> orden = new ArrayList<>();
        Set<Vertice> visitado = new HashSet<>();
        for (Vertice vertice : grafo.getVertices()) {
            if (!visitado.contains(vertice)) {
                apoyoDFS(vertice, visitado, orden);
            }
        }
        return orden;
    }

    public List<Vertice> BFSCompleto() {
        List<Vertice> orden = new ArrayList<>();
        Set<Vertice> visitado = new HashSet<>();

        for (Vertice inicio : grafo.getVertices()) {
            if (!visitado.contains(inicio)) {
                Queue<Vertice> cola = new LinkedList<>();
                cola.add(inicio);
                visitado.add(inicio);
                inicio.setEstado(Color.YELLOW);
                repintarConRetraso();

                while (!cola.isEmpty()) {
                    Vertice actual = cola.poll();
                    orden.add(actual);

                    for (Vertice vecino : grafo.getVecinos(actual)) {
                        if (!visitado.contains(vecino)) {
                            visitado.add(vecino);
                            cola.add(vecino);
                            vecino.setEstado(Color.YELLOW);
                            repintarConRetraso();
                        }
                    }

                    actual.setEstado(Color.GREEN);
                    repintarConRetraso();
                }
            }
        }

        return orden;
    }
    
    private void repintarConRetraso() {
        SwingUtilities.invokeLater(panel::repaint);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}