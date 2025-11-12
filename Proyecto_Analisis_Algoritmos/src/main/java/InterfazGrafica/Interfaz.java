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
import Logica.Busqueda;
import Logica.PanelGrafo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lagar
 */
public class Interfaz extends JFrame {

    private Grafo grafo;
    private PanelGrafo panelGrafo;
    private Busqueda buscar;

    public Interfaz(Grafo grafo) {
        this.grafo = grafo;
        setTitle("Grafo");
        setSize(1000, 790);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        panelGrafo = new PanelGrafo(grafo);
        buscar = new Busqueda(grafo, panelGrafo);
        add(panelGrafo, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton dfsButton = new JButton("DFS desde vértice");
        JButton bfsButton = new JButton("BFS desde vértice");
        JButton kruskalButton = new JButton("Kruskal (MST)");
        JButton tablaButton = new JButton("Mostrar tabla");
        JButton diskstrButton = new JButton("Diskstra (Camino más corto)");
        dfsButton.addActionListener(e -> ejecutarDFS());
        bfsButton.addActionListener(e -> ejecutarBFS());
        kruskalButton.addActionListener(e -> ejecutarKruskal());
        tablaButton.addActionListener(e -> mostrarTabla());
        diskstrButton.addActionListener(e -> ejecutarDisktra());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(bfsButton);
        buttonPanel.add(dfsButton);
        buttonPanel.add(kruskalButton);
        buttonPanel.add(diskstrButton);
        buttonPanel.add(tablaButton);
        add(buttonPanel, BorderLayout.EAST);
    }

    private void ejecutarDFS() {
        Vertice seleccionado = panelGrafo.getVerticeSeleccionado();
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
                    "Recorrido DFS desde " + seleccionado.getNombre() + ": " + recorrido);
        }).start();
    }

    private void ejecutarBFS() {
        Vertice seleccionado = panelGrafo.getVerticeSeleccionado();
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
                    "Recorrido BFS desde " + seleccionado.getNombre() + ": " + recorrido);
        }).start();
    }

    private void ejecutarKruskal() {
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            List<Arista> mst = buscar.kruskal();
            SwingUtilities.invokeLater(panelGrafo::repaint);
            double pesoTotal = mst.stream().mapToDouble(Arista::getPeso).sum();
            JOptionPane.showMessageDialog(this, "Arbol de expansion minima (Kruskal):\n"
                    + mst + "\n\nPeso total:" + String.format("%.2f", pesoTotal));
        }).start();
    }

    private void ejecutarDisktra() {
        Vertice seleccionado = panelGrafo.getVerticeSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccionar un vertice de inicio para Dijkstra.");
            return;
        }

        grafo.formatearColores();
        panelGrafo.repaint();

        new Thread(() -> {
            Map<Vertice, Double> distancias = buscar.dijkstra(seleccionado);

            //Crear una tabla con los resultados 
            String[] columnas = {"Vertice", "Distancia desde" + seleccionado.getNombre()};
            Object[][] datos = new Object[distancias.size()][2];
            int i = 0;
            for (Map.Entry<Vertice, Double> entry : distancias.entrySet()) {
                datos[i][0] = entry.getKey().getNombre();
                datos[i][1] = entry.getValue().isInfinite() ? "∞" : String.format("%.2f", entry.getValue());
                i++;
            }

            JTable tabla = new JTable(datos, columnas);
            tabla.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setPreferredSize(new Dimension(300, 300));

            SwingUtilities.invokeLater(() -> {
                panelGrafo.repaint();
                JOptionPane.showMessageDialog(this, scrollPane,
                        "Distancias minimas despues " + seleccionado.getNombre(),
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }).start();

    }

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
        Vertice k = new Vertice("Villaflores", 180, 360);
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
        Vertice w = new Vertice("Cacahoatán", 430, 600);
        Vertice x = new Vertice("Yajalón", 450, 150);
        Vertice y = new Vertice("Pijijiapan", 210, 460);
        Vertice z = new Vertice("Ciudad Hidalgo", 420, 690);
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
        grafo.agregarArista(a, u, 19.8);
        grafo.agregarArista(ab, n, 54.3);
        grafo.agregarArista(ac, aa, 52.3);
        grafo.agregarArista(t, h, 104);
        grafo.agregarArista(h, f, 119);
        grafo.agregarArista(h, x, 54.4);

        SwingUtilities.invokeLater(() -> {
            Interfaz frame = new Interfaz(grafo);
            frame.setVisible(true);
        });
    }
}
