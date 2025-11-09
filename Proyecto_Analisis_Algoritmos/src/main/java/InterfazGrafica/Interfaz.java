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
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        panelGrafo = new PanelGrafo(grafo);
        buscar = new Busqueda(grafo, panelGrafo);
        add(panelGrafo, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton dfsButton = new JButton("DFS desde vértice");
        JButton bfsButton = new JButton("BFS desde vértice");
        JButton dfsCompletoButton = new JButton("DFS completo");
        JButton bfsCompletoButton = new JButton("BFS completo");
        JButton tablaButton = new JButton("Mostrar tabla");
        dfsButton.addActionListener(e -> ejecutarDFS());
        bfsButton.addActionListener(e -> ejecutarBFS());
        dfsCompletoButton.addActionListener(e -> ejecutarDFSCompleto());
        bfsCompletoButton.addActionListener(e -> ejecutarBFSCompleto());
        tablaButton.addActionListener(e -> mostrarTabla());
        buttonPanel.add(bfsButton);
        buttonPanel.add(dfsButton);
        buttonPanel.add(dfsCompletoButton);
        buttonPanel.add(bfsCompletoButton);
        buttonPanel.add(tablaButton);
        add(buttonPanel, BorderLayout.SOUTH);
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

    private void ejecutarDFSCompleto() {
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            List<Vertice> recorrido = buscar.DFSCompleto();
            SwingUtilities.invokeLater(panelGrafo::repaint);
            JOptionPane.showMessageDialog(this,
                    "Recorrido DFS completo: " + recorrido);
        }).start();
    }

    private void ejecutarBFSCompleto() {
        grafo.formatearColores();
        panelGrafo.repaint();
        new Thread(() -> {
            List<Vertice> recorrido = buscar.BFSCompleto();
            SwingUtilities.invokeLater(panelGrafo::repaint);
            JOptionPane.showMessageDialog(this,
                    "Recorrido BFS completo: " + recorrido);
        }).start();
    }
    
    private void mostrarTabla(){
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
        Vertice a = new Vertice("Nogales", 400, 80);              // Norte
        Vertice b = new Vertice("Cananea", 550, 100);             // Noreste
        Vertice c = new Vertice("Hermosillo", 420, 250);          // Centro
        Vertice d = new Vertice("Guaymas", 420, 380);             // Sur centro
        Vertice e = new Vertice("Cd. Obregon", 420, 470);         // Sur
        Vertice f = new Vertice("Navojoa", 420, 540);             // Sur extremo
        Vertice g = new Vertice("San Luis Río Colorado", 150, 100); // Noroeste
        Vertice h = new Vertice("Puerto Peñasco", 200, 160);      // Noroeste costero
        Vertice i = new Vertice("Caborca", 280, 200);  

        grafo.agregarVertice(a);
        grafo.agregarVertice(b);
        grafo.agregarVertice(c);
        grafo.agregarVertice(d);
        grafo.agregarVertice(e);
        grafo.agregarVertice(f);
        grafo.agregarVertice(g);
        grafo.agregarVertice(h);
        grafo.agregarVertice(i);

        grafo.agregarArista(a, b, 85.0);   // Nogales - Cananea
        grafo.agregarArista(a, c, 270.0);  // Nogales - Hermosillo
        grafo.agregarArista(b, c, 250.0);  // Cananea - Hermosillo
        grafo.agregarArista(c, d, 130.0);  // Hermosillo - Guaymas
        grafo.agregarArista(d, e, 150.0);  // Guaymas - Cd. Obregon
        grafo.agregarArista(e, f, 70.0);   // Cd. Obregon - Navojoa
        grafo.agregarArista(g, a, 290.0);  // San Luis - Nogales
        grafo.agregarArista(g, i, 200.0);  // San Luis - Caborca
        grafo.agregarArista(h, i, 220.0);  // Puerto Peñasco - Caborca
        grafo.agregarArista(i, c, 220.0);  // Caborca - Hermosillo
        grafo.agregarArista(f, e, 70.0);   // Navojoa - Obregón

        SwingUtilities.invokeLater(() -> {
            Interfaz frame = new Interfaz(grafo);
            frame.setVisible(true);
        });
    }
}