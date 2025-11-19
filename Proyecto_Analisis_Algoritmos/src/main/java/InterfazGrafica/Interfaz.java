/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InterfazGrafica;

import Entidades.Arista;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import Entidades.Grafo;
import Entidades.Vertice;
import Logica.Algoritmos;
import Logica.PanelGrafo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.table.DefaultTableModel;

/**
 * clase que representa el frame del proyecto
 *
 * @author equipo
 */
public class Interfaz extends JFrame {

    private final Grafo grafo;// grafo
    private final PanelGrafo panelGrafo;// panel del grafo
    private final Algoritmos buscar; // algoritmos de busqueda

    /**
     * contructor que inicializa la clase
     *
     * @param grafo grafo
     */
    public Interfaz(Grafo grafo) {
        this.grafo = grafo;
        setTitle("Grafo");
        setSize(1150, 790);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        panelGrafo = new PanelGrafo(grafo);
        buscar = new Algoritmos(grafo, panelGrafo);
        add(panelGrafo, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton dfsButton = new JButton("DFS");
        JButton bfsButton = new JButton("BFS");
        JButton kruskalButton = new JButton("Kruskal (MST)");
        JButton tablaButton = new JButton("Mostrar tabla");
        JButton dijkstraButton = new JButton("Diskstra");
        JButton dijkstraButton2 = new JButton("Camino más corto entre 2 vertices");
        JButton limpiar = new JButton("Limpiar mapa");
        JButton complejidadButton = new JButton("complejidad temporal de los algoritmos");
        dfsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bfsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        kruskalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dijkstraButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dijkstraButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        limpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        complejidadButton.setAlignmentX(CENTER_ALIGNMENT);
        dfsButton.addActionListener(e -> ejecutarDFS());
        bfsButton.addActionListener(e -> ejecutarBFS());
        kruskalButton.addActionListener(e -> ejecutarKruskal());
        tablaButton.addActionListener(e -> mostrarTabla());
        dijkstraButton.addActionListener(e -> ejecutarDijkstra());
        dijkstraButton2.addActionListener(e -> caminoMasCorto());
        limpiar.addActionListener(e -> limpiar());
        complejidadButton.addActionListener(e -> Tn());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(bfsButton);
        buttonPanel.add(dfsButton);
        buttonPanel.add(kruskalButton);
        buttonPanel.add(dijkstraButton);
        buttonPanel.add(dijkstraButton2);
        buttonPanel.add(tablaButton);
        buttonPanel.add(limpiar);
        buttonPanel.add(complejidadButton);
        add(buttonPanel, BorderLayout.EAST);
    }

    /**
     * metodo que ejecuta y prepara al algoritmo de DFS
     */
    private void ejecutarDFS() {
        Vertice seleccionado = panelGrafo.getVerticeSeleccionadoOrigen();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un vértice para iniciar DFS.");
            return;
        }
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            List<Vertice> recorrido = buscar.DFS(seleccionado);
            SwingUtilities.invokeLater(panelGrafo::repaint);
            JOptionPane.showMessageDialog(this,
                    "Recorrido DFS desde " + seleccionado.getNombre() + ": " + recorrido.toString());
        }).start();
    }

    /**
     * metodo que ejecuta y prepara al algoritmo de bFS
     */
    private void ejecutarBFS() {
        Vertice seleccionado = panelGrafo.getVerticeSeleccionadoOrigen();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un vértice para iniciar BFS.");
            return;
        }
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            List<Vertice> recorrido = buscar.BFS(seleccionado);
            SwingUtilities.invokeLater(panelGrafo::repaint);
            JOptionPane.showMessageDialog(this,
                    "Recorrido BFS desde " + seleccionado.getNombre() + ": " + recorrido.toString());
        }).start();
    }

    /**
     * metodo que ejecuta y prepara al algoritmo de Kruskal
     */
    private void ejecutarKruskal() {
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            List<Arista> mst = buscar.kruskal();
        }).start();
    }

    /**
     * metodo que ejecuta y prepara al algoritmo de Disktra
     */
    private void ejecutarDijkstra() {
        Vertice seleccionado = panelGrafo.getVerticeSeleccionadoOrigen();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un vértice para iniciar Dijkstra.");
            return;
        }
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            buscar.dijkstra(seleccionado);
        }).start();
    }
    
    private void caminoMasCorto() {
        Vertice inicio = panelGrafo.getVerticeSeleccionadoOrigen();
        Vertice fin = panelGrafo.getVerticeSeleccionadoDestino();
        if (inicio == null || fin == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un vértice de inicio y uno de fin.");
            return;
        }
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            buscar.dijkstraInterno(inicio);
            List<Vertice> camino = buscar.caminoMasCorto(fin);
            if (camino.isEmpty() || !camino.get(0).equals(inicio)) {
                SwingUtilities.invokeLater(()->{
                JOptionPane.showMessageDialog(this, "No hay camino");
                });
            }else{
                buscar.colorearCamino(camino);
            }
            SwingUtilities.invokeLater(panelGrafo::repaint);
        }).start();
    }

    /**
     *
     * metodo que ejecuta y prepara una tabla de ayacencia
     */
    private void mostrarTabla() {
        List<Vertice> vertices = grafo.getVertices();
        int numVertices = vertices.size();
        String[] columnasMatriz = new String[numVertices + 1];
        columnasMatriz[0] = " ";
        for (int i = 0; i < numVertices; i++) {
            columnasMatriz[i + 1] = vertices.get(i).getNombre();
        }
        Object[][] datosMatriz = new Object[numVertices][numVertices + 1];
        Map<Vertice, Map<Vertice, Double>> adyacencia = new HashMap<>();
        for (Arista a : grafo.getAristas()) {
            adyacencia.putIfAbsent(a.getOrigen(), new HashMap<>());
            adyacencia.get(a.getOrigen()).put(a.getDestino(), a.getPeso());
        }
        for (int i = 0; i < numVertices; i++) {
            Vertice origen = vertices.get(i);
            datosMatriz[i][0] = origen.getNombre();
            for (int j = 0; j < numVertices; j++) {
                Vertice destino = vertices.get(j);
                Double peso = adyacencia.getOrDefault(origen, Collections.emptyMap()).get(destino);
                if (peso != null) {
                    datosMatriz[i][j + 1] = peso;
                } else {
                    datosMatriz[i][j + 1] = 0.0;
                }
            }
        }
        DefaultTableModel modeloMatriz = new DefaultTableModel(datosMatriz, columnasMatriz) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaMatriz = new JTable(modeloMatriz);
        tablaMatriz.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaMatriz.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollMatriz = new JScrollPane(tablaMatriz);
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.add(scrollMatriz);
        panelPrincipal.setPreferredSize(new Dimension(600, 500));
        JOptionPane.showMessageDialog(this, panelPrincipal, "Matriz de Adyacencia",
                JOptionPane.INFORMATION_MESSAGE);
    }
   
    /**
     * metodo que formatea el grafo al estado original
     */
    public void limpiar(){
        grafo.formatearColores();
        panelGrafo.repaint();
    }
    
    /**
     * metodo que invoca el mensaje de los T(n)
     */
    public void Tn(){
        JOptionPane.showMessageDialog(null, """
                                            DFS T(n) = 8n^2 + 18n + 8s
                                            BFS T(n) = 5n^4 + 14n^3 + 12n^2 + 10n + 11
                                            Kruscal T(n) = 17n^2 + 28n + 13
                                            Dijkstra T(n) = 24n^2 + 25n + 19 
                                            Ruta mas corta T(n) = 24n^2 + 27n + 23""", "Complejidad TemporaL",INFORMATION_MESSAGE);
    }
    /**
     * metodo main para ejecutar el proyecto
     *
     * @param args parametro de ejecucion
     */
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        Vertice a = new Vertice("Tuxtla Gutiérrez", 270, 320);
        Vertice b = new Vertice("Tapachula de Córdova y Ordóñez", 400, 640);
        Vertice c = new Vertice("San Cristóbal de Las Casas", 400, 330);
        Vertice d = new Vertice("Comitán de Domínguez", 510, 420);
        Vertice e = new Vertice("Heroica Chiapa de Corzo", 310, 340);
        Vertice f = new Vertice("Palenque", 530, 120);
        Vertice g = new Vertice("Cintalapa de Figueroa", 80, 270);
        Vertice h = new Vertice("Ocosingo", 480, 260);
        Vertice i = new Vertice("Ocozocoautla de Espinosa", 180, 300);
        Vertice j = new Vertice("Tonalá", 90, 390);
        Vertice k = new Vertice("Villaflores", 190, 380);
        Vertice l = new Vertice("Berriozábal", 220, 270);
        Vertice m = new Vertice("Huixtla", 360, 590);
        Vertice n = new Vertice("Reforma", 250, 50);
        Vertice o = new Vertice("Motozintla de Mendoza", 430, 560);
        Vertice p = new Vertice("Arriaga", 70, 340);
        Vertice q = new Vertice("Las Margaritas", 550, 370);
        Vertice r = new Vertice("Frontera Comalapa", 460, 510);
        Vertice s = new Vertice("Las Rosas", 470, 390);
        Vertice t = new Vertice("Teopisca", 470, 340);
        Vertice u = new Vertice("Suchiapa", 270, 370);
        Vertice v = new Vertice("Mapastepec", 270, 520);
        Vertice w = new Vertice("Cacahoatán", 450, 620);
        Vertice x = new Vertice("Yajalón", 450, 150);
        Vertice y = new Vertice("Pijijiapan", 210, 460);
        Vertice z = new Vertice("Ciudad Hidalgo", 440, 690);
        Vertice aa = new Vertice("Venustiano Carranza", 430, 430);
        Vertice ab = new Vertice("Pichucalco", 260, 130);
        Vertice ac = new Vertice("Acala", 400, 370);
        Vertice ad = new Vertice("Simojovel de Allende", 370, 230);

        grafo.agregarVertice(a);
        grafo.agregarVertice(b);
        grafo.agregarVertice(c);
        grafo.agregarVertice(d);
        grafo.agregarVertice(e);
        grafo.agregarVertice(f);
        grafo.agregarVertice(g);
        grafo.agregarVertice(h);
        grafo.agregarVertice(i);
        grafo.agregarVertice(j);
        grafo.agregarVertice(k);
        grafo.agregarVertice(l);
        grafo.agregarVertice(m);
        grafo.agregarVertice(n);
        grafo.agregarVertice(o);
        grafo.agregarVertice(p);
        grafo.agregarVertice(q);
        grafo.agregarVertice(r);
        grafo.agregarVertice(s);
        grafo.agregarVertice(t);
        grafo.agregarVertice(u);
        grafo.agregarVertice(v);
        grafo.agregarVertice(w);
        grafo.agregarVertice(x);
        grafo.agregarVertice(y);
        grafo.agregarVertice(z);
        grafo.agregarVertice(aa);
        grafo.agregarVertice(ab);
        grafo.agregarVertice(ac);
        grafo.agregarVertice(ad);

        grafo.agregarArista(a, e, 15.1);
        grafo.agregarArista(e, c, 53.9);
        grafo.agregarArista(a, u, 21.1);
        grafo.agregarArista(a, l, 23.7);
        grafo.agregarArista(l, i, 14.6);
        grafo.agregarArista(i, k, 74.4);
        grafo.agregarArista(i, g, 47.6);
        grafo.agregarArista(g, p, 68.5);
        grafo.agregarArista(p, j, 25.4);
        grafo.agregarArista(j, y, 78.9);
        grafo.agregarArista(y, v, 47.6);
        grafo.agregarArista(v, m, 63.7);
        grafo.agregarArista(m, b, 41.2);
        grafo.agregarArista(m, o, 58.5);
        grafo.agregarArista(b, w, 21.9);
        grafo.agregarArista(w, z, 39.8);
        grafo.agregarArista(o, r, 50.2);
        grafo.agregarArista(r, d, 96.1);
        grafo.agregarArista(d, q, 20.2);
        grafo.agregarArista(d, s, 37.6);
        grafo.agregarArista(s, t, 29.1);
        grafo.agregarArista(t, c, 33.6);
        grafo.agregarArista(e, ac, 40.7);
        grafo.agregarArista(e, ad, 109);
        grafo.agregarArista(e, ab, 194);
        grafo.agregarArista(ab, n, 54.3);
        grafo.agregarArista(ac, aa, 52.3);
        grafo.agregarArista(t, h, 104);
        grafo.agregarArista(h, f, 119);
        grafo.agregarArista(h, x, 54.4);
        grafo.agregarArista(k, u, 71.3);
        grafo.agregarArista(w, o, 119);
        grafo.agregarArista(d, aa, 77.4);
        grafo.agregarArista(ab, ad, 132);

        SwingUtilities.invokeLater(() -> {
            Interfaz frame = new Interfaz(grafo);
            frame.setVisible(true);
        });
    }
}
