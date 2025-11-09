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
        Vertice a = new Vertice("Tuxtla Gutiérrez", 350, 330);
        Vertice b = new Vertice("Tapachula de Córdova y Ordóñez", 470, 600);
        Vertice c = new Vertice("San Cristóbal de Las Casas", 430, 330); 
        Vertice d = new Vertice("Comitán de Domínguez", 560, 400); 
        Vertice e = new Vertice("Heroica Chiapa de Corzo", 370, 330); 
        Vertice f = new Vertice("Palenque", 560, 120); 
        Vertice g = new Vertice("Cintalapa de Figueroa", 130, 300);
        Vertice h = new Vertice("Ocosingo", 510, 280);
        Vertice i = new Vertice("Ocozocoautla de Espinosa", 290, 320);
        Vertice j = new Vertice("Tonalá", 150, 360);
        Vertice k = new Vertice("Villaflores", 150, 350);
        Vertice l = new Vertice("Berriozábal", 330, 290);
        Vertice m = new Vertice("Huixtla", 440, 550);
        Vertice n = new Vertice("Reforma", 310, 90);
        Vertice o = new Vertice("Motozintla de Mendoza", 480, 560);
        Vertice p = new Vertice("Arriaga", 100, 320);
        Vertice q = new Vertice("Las Margaritas", 570, 370);
        Vertice r = new Vertice("Frontera Comalapa", 490, 530);
        Vertice s = new Vertice("Las Rosas", 540, 360);
        Vertice t = new Vertice("Teopisca", 500, 340);
        Vertice u = new Vertice("Suchiapa", 350, 345);
        Vertice v = new Vertice("Mapastepec", 330, 520);
        Vertice w = new Vertice("Cacahoatán", 500, 570);
        Vertice x = new Vertice("Yajalón", 480, 150);
        Vertice y = new Vertice("Pijijiapan", 270, 460);
        Vertice z = new Vertice("Ciudad Hidalgo", 470, 630);
        Vertice aa = new Vertice("Venustiano Carranza", 500, 410);
        Vertice ab = new Vertice("Pichucalco", 310, 130);
        Vertice ac = new Vertice("Acala", 230, 360);
        Vertice ad = new Vertice("Simojovel de Allende", 380, 230);

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
        grafo.agregarArista(a, u);
        grafo.agregarArista(a, l);
        grafo.agregarArista(l, i);
        grafo.agregarArista(i, k);
        grafo.agregarArista(i, g);
        grafo.agregarArista(g, p);
        grafo.agregarArista(p, j);
        grafo.agregarArista(j, y);
        grafo.agregarArista(y, v);
        grafo.agregarArista(v, m);
        grafo.agregarArista(m, b);
        grafo.agregarArista(m, o);
        grafo.agregarArista(b, w);
        grafo.agregarArista(w, z);
        grafo.agregarArista(o, r);
        grafo.agregarArista(r, d);
        grafo.agregarArista(d, q);
        grafo.agregarArista(d, s);
        grafo.agregarArista(s, t);
        grafo.agregarArista(t, c);
        grafo.agregarArista(e, ac);
        grafo.agregarArista(e, ad);
        grafo.agregarArista(e, ab);
        grafo.agregarArista(a, u);
        grafo.agregarArista(ab, n);
        grafo.agregarArista(ac, aa);
        grafo.agregarArista(t, h);
        grafo.agregarArista(h, f);
        grafo.agregarArista(h, x);

        SwingUtilities.invokeLater(() -> {
            Interfaz frame = new Interfaz(grafo);
            frame.setVisible(true);
        });
    }
}