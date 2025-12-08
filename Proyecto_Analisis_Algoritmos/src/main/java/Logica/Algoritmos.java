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
        List<Vertice> orden = new ArrayList<>();//1
        Set<Vertice> visitado = new HashSet<>();//1
        apoyoDFS(verticeInicio, visitado, orden);//
        for (Vertice v : grafo.getVertices()) {
            if (!visitado.contains(v)) {
                v.setΠ(verticeInicio);
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
        for (Arista a : grafo.getAristasSalientes(actual)) {
            Vertice v = a.getDestino();
            if (!visitado.contains(v)) {
                v.setΠ(actual);
                a.setEstado(Color.YELLOW);
                for (Arista aInversa : grafo.getAristasSalientes(v)) {
                    if (aInversa.getDestino().equals(actual)) {
                        aInversa.setEstado(Color.YELLOW);
                        break;
                    }
                }
                repintar();
                apoyoDFS(v, visitado, orden);
                a.setEstado(Color.GREEN);
                for (Arista aInversa : grafo.getAristasSalientes(v)) {
                    if (aInversa.getDestino().equals(actual)) {
                        aInversa.setEstado(Color.GREEN);
                        break;
                    }
                }  
            }else{
                if (a.getEstado() == Color.LIGHT_GRAY) {
                    a.setEstado(Color.CYAN);
                    for (Arista aInversa : grafo.getAristasSalientes(v)) {
                        if (aInversa.getDestino().equals(actual)) {
                            if (aInversa.getEstado() != Color.GREEN) {
                                aInversa.setEstado(Color.CYAN);
                            }
                            break;
                        }
                    }
                }
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
                List<Arista> aristasUsadas = new ArrayList<>();
                    for (Arista a : grafo.getAristasSalientes(actual)) {
                        Vertice vecino = a.getDestino();
                        if (!visitado.contains(vecino)) {
                            a.setEstado(Color.YELLOW);
                            vecino.setΠ(actual);
                            aristasUsadas.add(a);
                            for (Arista aInversa : grafo.getAristasSalientes(vecino)) {
                                if (aInversa.getDestino().equals(actual)) {
                                    aInversa.setEstado(Color.YELLOW);
                                    aristasUsadas.add(aInversa);
                                    break;
                                }
                            }
                            visitado.add(vecino);
                            cola.add(vecino);
                            vecino.setEstado(Color.YELLOW);
                            repintar();
                        }else{
                            if (a.getEstado() == Color.LIGHT_GRAY) {
                                a.setEstado(Color.CYAN);
                                for (Arista aInversa : grafo.getAristasSalientes(actual)) {
                                    if (aInversa.getDestino().equals(actual)) {
                                        if (aInversa.getEstado() != Color.GREEN) {
                                            aInversa.setEstado(Color.CYAN);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    actual.setEstado(Color.GREEN);
                    for (Arista a : aristasUsadas) {
                        a.setEstado(Color.GREEN);
                    }
                    repintar();
                }
                repintar();
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
        aristasUnicas.sort(Comparator.comparingDouble(Arista::getPeso));

        // Recorrer las aristas en orden creciente
        for (Arista a : aristasUnicas) {
            Vertice u = a.getOrigen();
            Vertice v = a.getDestino();
            Color colorVertice;
            //Mostrar que la arista esta siendo evaluada
            u.setEstado(Color.YELLOW);
            v.setEstado(Color.YELLOW);
            for (Arista aInversa : grafo.getAristasSalientes(v)) {
                if (aInversa.getDestino().equals(u)) {
                    aInversa.setEstado(Color.YELLOW);
                    break;
                }
            }
            repintar();

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
            for (Arista aInversa : grafo.getAristasSalientes(v)) {
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
        Map<Vertice, Double> distancia = new HashMap<>();
        Set<Vertice> visitados = new HashSet<>();
        for (Vertice v : grafo.getVertices()) {
            distancia.put(v, Double.POSITIVE_INFINITY);
            v.setEstado(Color.RED);
            v.setΠ(null);
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
            
            Vertice padre = actual.getΠ();
            if (padre != null) {
                for (Arista a : grafo.getAristasSalientes(padre)) {
                    if (a.getDestino().equals(actual)) {
                        a.setEstado(Color.GREEN);
                        break;
                    }
                }
                for (Arista aInversa : grafo.getAristasSalientes(actual)) {
                    if (aInversa.getDestino().equals(padre)) {
                        aInversa.setEstado(Color.GREEN);
                        break;
                    }
                }
            }
            repintar();
            for (Arista arista : grafo.getAristasSalientes(actual)) {
                Vertice vecino = arista.getDestino();
                double nuevaDistancia = distancia.get(actual) + arista.getPeso();

                if (nuevaDistancia < distancia.get(vecino)) {
                    Vertice padreAntiguo = vecino.getΠ(); //
                    if (padreAntiguo != null) {
                        for (Arista aristaAntigua : grafo.getAristasSalientes(padreAntiguo)) {
                            if (aristaAntigua.getDestino().equals(vecino)) {
                                aristaAntigua.setEstado(Color.LIGHT_GRAY);
                                break;
                            }
                        }
                        for (Arista aInversa : grafo.getAristasSalientes(vecino)) {
                            if (aInversa.getDestino().equals(padreAntiguo)) {
                                aInversa.setEstado(Color.LIGHT_GRAY);
                                break;
                            }
                        }
                    }
                    distancia.put(vecino, nuevaDistancia);
                    vecino.setΠ(actual);
                    cola.add(vecino);

                    arista.setEstado(Color.YELLOW);
                    for (Arista aInversa : grafo.getAristasSalientes(vecino)) {
                        if (aInversa.getDestino().equals(actual)) {
                            aInversa.setEstado(Color.YELLOW);
                            break;
                        }
                    }
                    vecino.setEstado(Color.YELLOW);
                    repintar();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        repintar();
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
     * metodo que simula un dijkstra de manera interna
     * @param origen vertice origen
     * @return dintancia
     */
    public Map<Vertice, Double> dijkstraInterno(Vertice origen) {
        Map<Vertice, Double> distancia = new HashMap<>();
        Set<Vertice> visitados = new HashSet<>();
        for (Vertice v : grafo.getVertices()) {
            distancia.put(v, Double.POSITIVE_INFINITY);
            v.setΠ(null);
        }
        distancia.put(origen, 0.0);
        PriorityQueue<Vertice> cola = new PriorityQueue<>(Comparator.comparingDouble(distancia::get));
        cola.add(origen);

        while (!cola.isEmpty()) {
            Vertice actual = cola.poll();

            if (visitados.contains(actual)) {
                continue;
            }

            visitados.add(actual);
            
            //Recoremos los vecinos del vertice actual
            for (Arista arista : grafo.getAristasSalientes(actual)) {
                Vertice vecino = arista.getDestino();
                double nuevaDistancia = distancia.get(actual) + arista.getPeso();

                if (nuevaDistancia < distancia.get(vecino)) {
                    distancia.put(vecino, nuevaDistancia);
                    vecino.setΠ(actual);
                    cola.add(vecino);
                }
            }
        }
        return distancia;
    }
    
    /**
     * metodo para hacer el camino
     * @param destino vertice destino
     * @return camino
     */
    public List<Vertice> caminoMasCorto(Vertice destino){
        List<Vertice> camino = new ArrayList<>();
        Vertice actual = destino;
        while(actual != null){
            camino.add(actual);
            actual = actual.getΠ();
        }
        Collections.reverse(camino);
        return camino;
    }
    
    /**
     * metodo que colorea el camino 
     * @param camino lista de vertices
     */
    public void colorearCamino(List<Vertice> camino){
        for (int i = 0; i < camino.size() - 1; i++) {
            Vertice u = camino.get(i);
            Vertice v = camino.get(i+1);
            u.setEstado(Color.GREEN);
            for (Arista a : grafo.getAristasSalientes(u)) {
                if (a.getDestino().equals(v)) {
                    a.setEstado(Color.GREEN);
                    break;
                }
            }
            for (Arista aInversa : grafo.getAristasSalientes(v)) {
                if (aInversa.getDestino().equals(u)) {
                    aInversa.setEstado(Color.GREEN);
                    break;
                }
            }
        }
        camino.get(camino.size() - 1).setEstado(Color.GREEN);
        repintar();
    }

    /**
     * T(n) = 5
     * metodo que repinta el frame para mostrar el proceso del grafo
     */
    private void repintar() {
        SwingUtilities.invokeLater(panel::repaint);//2
        try {
            Thread.sleep(200);//1
        } catch (InterruptedException e) {//1
            System.out.println(e);//1
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
    
    public List<Arista> prim(Vertice inicio) {
        List<Arista> mst = new ArrayList<>();
        Set<Vertice> visitados = new HashSet<>();
        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingDouble(Arista::getPeso));
        visitados.add(inicio);
        inicio.setEstado(Color.YELLOW);
        repintar();
        for (Arista a : grafo.getAristasSalientes(inicio)) {
            pq.add(a);
            a.setEstado(Color.YELLOW);
        }
        repintar();

        while (visitados.size() < grafo.getVertices().size() && !pq.isEmpty()) {
            Arista menor = pq.poll();
            Vertice u = menor.getOrigen();
            Vertice v = menor.getDestino();
            if (visitados.contains(u) && visitados.contains(v)) {
                menor.setEstado(Color.CYAN);
                continue;
            }
            mst.add(menor);
            menor.setEstado(Color.GREEN);
            Vertice nuevo = visitados.contains(u) ? v : u;
            nuevo.setEstado(Color.GREEN);
            visitados.add(nuevo);
            repintar();
            for (Arista a : grafo.getAristasSalientes(nuevo)) {
                if (!visitados.contains(a.getDestino())) {
                    a.setEstado(Color.YELLOW);
                    pq.add(a);
                }
            }
            repintar();
        }
        return mst;
    }
    
    public List<Arista> boruvka() {
        List<Arista> mst = new ArrayList<>();
        Map<Vertice, Vertice> padre = new HashMap<>();
        for (Vertice v : grafo.getVertices()) {
            padre.put(v, v);
            v.setEstado(Color.RED);
        }
        repintar();
        int numComponentes = grafo.getVertices().size();
        while (numComponentes > 1) {
            Map<Vertice, Arista> mejor = new HashMap<>();
            for (Arista a : grafo.getAristas()) {
                Vertice u = find(padre, a.getOrigen());
                Vertice v = find(padre, a.getDestino());

                if (u != v) {
                    if (!mejor.containsKey(u) || a.getPeso() < mejor.get(u).getPeso()) mejor.put(u, a);
                    if (!mejor.containsKey(v) || a.getPeso() < mejor.get(v).getPeso()) mejor.put(v, a);
                }
            }
            for (Arista a : mejor.values()) {
                Vertice u = find(padre, a.getOrigen());
                Vertice v = find(padre, a.getDestino());

                if (u != v) {
                    mst.add(a);
                    union(padre, u, v);
                    numComponentes--;

                    a.getOrigen().setEstado(Color.GREEN);
                    a.getDestino().setEstado(Color.GREEN);
                    a.setEstado(Color.GREEN);
                    repintar();
                }
            }
        }
        return mst;
    }
    
    public Map<Vertice, Double> bellmanFord(Vertice origen) {
        Map<Vertice, Double> distancia = new HashMap<>();

        for (Vertice v : grafo.getVertices()) {
            distancia.put(v, Double.POSITIVE_INFINITY);
            v.setΠ(null);
            v.setEstado(Color.RED);
        }
        repintar();

        distancia.put(origen, 0.0);
        origen.setEstado(Color.YELLOW);
        repintar();

        int V = grafo.getVertices().size();
        List<Arista> aristas = grafo.getAristas();

        for (int i = 1; i < V; i++) {
            for (Arista a : aristas) {
                Vertice u = a.getOrigen();
                Vertice v = a.getDestino();
                double nueva = distancia.get(u) + a.getPeso();

                if (nueva < distancia.get(v)) {
                    distancia.put(v, nueva);
                    v.setΠ(u);

                    u.setEstado(Color.YELLOW);
                    v.setEstado(Color.YELLOW);
                    a.setEstado(Color.YELLOW);
                    repintar();
                }
            }
        }
        for (Arista a : aristas) {
            Vertice u = a.getOrigen();
            Vertice v = a.getDestino();
            if (distancia.get(u) + a.getPeso() < distancia.get(v)) {
                JOptionPane.showMessageDialog(null, 
                   "Existe ciclo negativo en el grafo");
                return distancia;
            }
        }

        for (Vertice v : distancia.keySet()) {
            v.setEstado(Color.GREEN);
        }
        repintar();

        return distancia;
    }

}
