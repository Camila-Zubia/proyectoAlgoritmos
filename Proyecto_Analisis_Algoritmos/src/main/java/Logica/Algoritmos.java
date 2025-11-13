package Logica;

import Entidades.Arista;
import Entidades.Grafo;
import Entidades.Vertice;
import java.awt.Color;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * clase que representa los algoritmos para grafos
 *
 * @author equipo
 */
public class Algoritmos {

    private Grafo grafo;
    private PanelGrafo panel;

    /**
     * Contructor que inicializa la clase
     *
     * @param grafo grafo
     * @param panel panel del grafo
     */
    public Algoritmos(Grafo grafo, PanelGrafo panel) {
        this.grafo = grafo;
        this.panel = panel;
    }

    /**
     * metodo que representa el DFS
     *
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
     *
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
     *
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
            if (noVisitado == null) {
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
     *
     * @return arbol de esparcimiento
     */
    public List<Arista> kruskal() {
        Set<String> ejesUnicos = new HashSet<>();
        List<Arista> aristasUnicas = new ArrayList<>();
        List<Arista> aristas = new ArrayList<>(grafo.getAristas());
        List<Arista> mst = new ArrayList<>();
        Map<Vertice, Vertice> padre = new HashMap<>();
        double pesoTotal = 0.0;
        
        for (Arista a : aristas) {
            Vertice u = a.getOrigen();
            Vertice v = a.getDestino();
            String clave1 = u.getNombre() + "-" + v.getNombre();
            String clave2 = v.getNombre() + "-" + u.getNombre();

            if (!ejesUnicos.contains(clave1) && !ejesUnicos.contains(clave2)) {
                aristasUnicas.add(a);
                ejesUnicos.add(clave1);
            }
        }

        // Inicializar los conjuntos disjuntos (cada vertice es su propio padre)
        for (Vertice v : grafo.getVertices()) {
            padre.put(v, v);
            v.setEstado(Color.RED); // Estado inicial
        }
        repintar();

        // ordenar aristas por peso
        aristas.sort(Comparator.comparingDouble(Arista::getPeso));

        // Recorrer las aristas en orden creciente
        for (Arista a : aristasUnicas) {
            Vertice u = a.getOrigen();
            Vertice v = a.getDestino();
            Color colorVertice;
            //Mostrar que la arista esta siendo evaluada
            u.setEstado(Color.YELLOW);
            v.setEstado(Color.YELLOW);
            colorVertice = Color.YELLOW;
            repintar();
            System.out.println("Evaluando arista: " + u.getNombre() + "-" + v.getNombre()
                    + "(Peso: " + a.getPeso() + ")");

            // Verificar si pertenece a diferentes conjuntos
            if (find(padre, u) != find(padre, v)) {
                mst.add(a);
                union(padre, u, v);
                u.setEstado(Color.GREEN);
                v.setEstado(Color.GREEN);
                colorVertice = Color.GREEN;
                repintar();

                pesoTotal += a.getPeso();
                System.out.println("Arista agregada al MST (Peso acumulado:" + pesoTotal + ")");
            } else {
                colorVertice = Color.RED;
                System.out.println("Arista destacada (formaria ciclo)");
            }
            a.setEstado(colorVertice);
            for (Arista aInversa : grafo.getAristasUnicas(v)) {
                if (aInversa.getDestino().equals(u)) {
                    aInversa.setEstado(colorVertice);
                    break;
                }
            }
            repintar();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Finalizar: mostrar todos los vertices del MST en color verde brillante
        
        repintar();

        return mst;
    }

    /**
     * metodo que representa el algoritmo de dijkstra
     *
     * @param origen vertice origen
     * @return distancias
     */
    public Map<Vertice, Double> dijkstra(Vertice origen) {
        // Inicializacion de Estructuras 
        Map<Vertice, Double> distancia = new HashMap<>();
        Map<Vertice, Vertice> previo = new HashMap<>();
        Set<Vertice> visitados = new HashSet<>();

        // Inicializar todas las distancias a infinito
        for (Vertice v : grafo.getVertices()) {
            distancia.put(v, Double.POSITIVE_INFINITY);
            v.setEstado(Color.RED);

        }
        repintar();

        distancia.put(origen, 0.0);
        origen.setEstado(Color.YELLOW);
        repintar();

        PriorityQueue<Vertice> cola = new PriorityQueue<>(Comparator.comparingDouble(distancia::get));
        cola.add(origen);

        while (!cola.isEmpty()) {
            Vertice actual = cola.poll();

            if (visitados.contains(actual)) {
                continue;
            }

            visitados.add(actual);
            actual.setEstado(Color.GREEN);

            repintar();
            System.out.println("Visitado: " + actual.getNombre() + "(Distancia minima: " + distancia.get(actual) + ")");

            //Recoremos los vecinos del vertice actual
            for (Arista arista : grafo.getAristas()) {
                if (arista.getOrigen().equals(actual)) {
                    Vertice vecino = arista.getDestino();
                    double nuevaDistancia = distancia.get(actual) + arista.getPeso();

                    if (nuevaDistancia < distancia.get(vecino)) {
                        distancia.put(vecino, nuevaDistancia);
                        previo.put(vecino, actual);
                        cola.add(vecino);

                        vecino.setEstado(Color.ORANGE);
                        repintar();
                        System.out.println("Actualizado " + vecino.getNombre() + "-> nueva distancia: " + nuevaDistancia);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repintar();

        System.out.println("\n Diskstra completado.\nDistancias minimas desde" + origen.getNombre() + ":");
        for (Vertice v : grafo.getVertices()) {
            System.out.println(v.getNombre() + "->" + distancia.get(v));
        }
        // Crear copia final para mostrar en JOptionPane
        final Vertice origenFinal = origen;
        final Map<Vertice, Double> distanciasFinal = new HashMap<>(distancia);

        SwingUtilities.invokeLater(() -> {
            StringBuilder resulatado = new StringBuilder("Distancias minimas desde" + origenFinal.getNombre() + ":\n\n");
            for (Map.Entry<Vertice, Double> entry : distanciasFinal.entrySet()) {
                resulatado.append(String.format("%-30s : %.2f\n", entry.getKey().getNombre(), entry.getValue()));
            }
            JOptionPane.showMessageDialog(panel,
                    resulatado.toString(),
                    "Resultado de Dijkstra",
                    JOptionPane.INFORMATION_MESSAGE);

        });

        return distancia;
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
     *
     * @param padre predecesor del vertice
     * @param v vertice
     * @return vertice hijo
     */
    private Vertice find(Map<Vertice, Vertice> padre, Vertice v) {
        if (padre.get(v) != v) {
            padre.put(v, find(padre, padre.get(v)));
        }
        return padre.get(v);
    }

    /**
     * metodo que une un padre y un hijo
     *
     * @param padre predecesor del vertice
     * @param a vertice a
     * @param b vertice b
     */
    private void union(Map<Vertice, Vertice> padre, Vertice a, Vertice b) {
        Vertice ra = find(padre, a);
        Vertice rb = find(padre, b);
        if (ra != rb) {
            padre.put(ra, rb);
        }
    }

}
