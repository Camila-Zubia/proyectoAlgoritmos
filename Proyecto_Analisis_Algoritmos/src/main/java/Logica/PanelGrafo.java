package Logica;

import Entidades.Grafo;
import Entidades.Vertice;
import Entidades.Arista;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class PanelGrafo extends JPanel {
    
    private Grafo grafo;
    private Vertice verticeSeleccionado = null;
    private static final int RADIO = 10;
    private Image imagenFondo;

    public PanelGrafo(Grafo grafo) {
        this.grafo = grafo;
        setPreferredSize(new Dimension(790,790));
        imagenFondo = new ImageIcon(getClass().getResource("/chiapas.jpeg")).getImage();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Vertice v = getVerticeEn(e.getX(), e.getY());
                if (v != null) {
                    verticeSeleccionado = v;
                    repaint();
                }
            }
        });
    }

    public Vertice getVerticeSeleccionado() {
        return verticeSeleccionado;
    }

    private Vertice getVerticeEn(int x, int y) {
        List<Vertice> vertices = grafo.getVertices();
        for (Vertice v : vertices) {
            int dx = x - v.getX();
            int dy = y - v.getY();
            if (dx * dx + dy * dy <= RADIO * RADIO) {
                return v;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Arista a : grafo.getAristas()) {
            Vertice origen = a.getOrigen();
            Vertice destino = a.getDestino();
            Color colorArista = origen.getEstado() != null ? origen.getEstado() : Color.LIGHT_GRAY;
            g2.setColor(colorArista);
            g2.setStroke(new BasicStroke(2));
            dibujarAristaDirigida(g2, origen.getX(), origen.getY(), destino.getX(), destino.getY());
            double peso = a.getPeso();
            if (!Double.isNaN(peso)) {
                int medioX = (origen.getX() + destino.getX()) / 2;
                int medioY = (origen.getY() + destino.getY()) / 2;
                g2.setColor(Color.BLACK);
                String textoPeso = (peso == 0) ? "" : String.format("%.1f", peso);
                if (!textoPeso.isEmpty()) g2.drawString(textoPeso, medioX + 6, medioY + 6);
            }
        }
        for (Vertice v : grafo.getVertices()) {
            Color relleno = v.getEstado() != null ? v.getEstado() : Color.RED;
            g2.setColor(relleno);
            g2.fillOval(v.getX() - RADIO, v.getY() - RADIO, RADIO * 2, RADIO * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(v.getX() - RADIO, v.getY() - RADIO, RADIO * 2, RADIO * 2);
            if (v == verticeSeleccionado) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.BLUE);
                g2.drawOval(v.getX() - RADIO - 3, v.getY() - RADIO - 3, (RADIO + 3) * 2, (RADIO + 3) * 2);
            }
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            String nombre = v.getNombre();
            int textWidth = fm.stringWidth(nombre);
            g2.drawString(nombre, v.getX() - textWidth / 2, v.getY() + fm.getAscent() / 2);
        }
    }

    // Dibuja la línea con flecha al final (punto final en x2,y2)
    private void dibujarAristaDirigida(Graphics2D g2, int x1, int y1, int x2, int y2) {
        // Calcular el punto de salida y llegada un poco desplazados para no dibujar sobre la burbuja
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dist = Math.hypot(dx, dy);
        if (dist == 0) return;

        // ajustar para que la línea empiece/termine en el borde del círculo
        double ux = dx / dist;
        double uy = dy / dist;
        int startX = (int) Math.round(x1 + ux * RADIO);
        int startY = (int) Math.round(y1 + uy * RADIO);
        int endX = (int) Math.round(x2 - ux * RADIO);
        int endY = (int) Math.round(y2 - uy * RADIO);

        // Línea principal
        g2.drawLine(startX, startY, endX, endY);

        // Flecha
        double phi = Math.toRadians(25);
        int barb = 12;
        double theta = Math.atan2(endY - startY, endX - startX);
        double rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            double x = endX - barb * Math.cos(rho);
            double y = endY - barb * Math.sin(rho);
            g2.drawLine(endX, endY, (int) x, (int) y);
            rho = theta - phi;
        }
    }
}