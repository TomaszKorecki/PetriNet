package pl.edu.agh.petrinet.gui.customPlugins;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.util.ArrowFactory;
import javafx.application.Platform;
import pl.edu.agh.petrinet.gui.Console;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriPlace;
import pl.edu.agh.petrinet.model.PetriTransition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;


public class PetriNetEditingEdgeMousePlugin<PetriVertex, PetriEdge> extends AbstractGraphMousePlugin implements MouseListener, MouseMotionListener {
    protected PetriVertex startVertex;
    protected Point2D down;
    protected CubicCurve2D rawEdge;
    protected Shape edgeShape;
    protected Shape rawArrowShape;
    protected Shape arrowShape;
    protected VisualizationServer.Paintable edgePaintable;
    protected VisualizationServer.Paintable arrowPaintable;
    protected EdgeType edgeIsDirected;

    private PetriGraph petriGraph;

    public PetriNetEditingEdgeMousePlugin(PetriGraph petriGraph) {
        this(16, petriGraph);
    }

    public PetriNetEditingEdgeMousePlugin(int modifiers, PetriGraph petriGraph) {
        super(modifiers);
        this.rawEdge = new CubicCurve2D.Float();
        this.rawEdge.setCurve(0.0D, 0.0D, 0.33000001311302185D, 100.0D, 0.6600000262260437D, -50.0D, 1.0D, 0.0D);
        this.rawArrowShape = ArrowFactory.getNotchedArrow(20.0F, 16.0F, 8.0F);
        this.edgePaintable = new PetriNetEditingEdgeMousePlugin.EdgePaintable();
        this.arrowPaintable = new PetriNetEditingEdgeMousePlugin.ArrowPaintable();
        this.cursor = Cursor.getPredefinedCursor(1);
        this.petriGraph = petriGraph;
    }

    public boolean checkModifiers(MouseEvent e) {
        return (e.getModifiers() & this.modifiers) != 0;
    }

    public void mousePressed(MouseEvent e) {
        if (this.checkModifiers(e)) {
            VisualizationViewer<PetriVertex, PetriEdge> vv = (VisualizationViewer) e.getSource();
            Point p = e.getPoint();
            GraphElementAccessor<PetriVertex, PetriEdge> pickSupport = vv.getPickSupport();
            if (pickSupport != null) {
                Graph graph = vv.getModel().getGraphLayout().getGraph();
                if (graph instanceof DirectedGraph) {
                    this.edgeIsDirected = EdgeType.DIRECTED;
                } else {
                    this.edgeIsDirected = EdgeType.UNDIRECTED;
                }

                final PetriVertex vertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
                if (vertex != null) {
                    this.startVertex = vertex;
                    this.down = e.getPoint();
                    this.transformEdgeShape(this.down, this.down);
                    vv.addPostRenderPaintable(this.edgePaintable);
                    if ((e.getModifiers() & 1) != 0 && !(vv.getModel().getGraphLayout().getGraph() instanceof UndirectedGraph)) {
                        this.edgeIsDirected = EdgeType.DIRECTED;
                    }

                    if (this.edgeIsDirected == EdgeType.DIRECTED) {
                        this.transformArrowShape(this.down, e.getPoint());
                        vv.addPostRenderPaintable(this.arrowPaintable);
                    }
                }
            }

            vv.repaint();
        }

    }

    public void mouseReleased(MouseEvent e) {
        VisualizationViewer<PetriVertex, PetriEdge> vv = (VisualizationViewer) e.getSource();
        if (this.checkModifiers(e)) {
            Point p = e.getPoint();
            GraphElementAccessor<PetriVertex, PetriEdge> pickSupport = vv.getPickSupport();
            if (pickSupport != null) {
                PetriVertex endVertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());

                if (endVertex != null && this.startVertex != null) {
                    if (startVertex instanceof PetriPlace && endVertex instanceof PetriTransition) {
                        petriGraph.addEdge((PetriPlace) startVertex, (PetriTransition) endVertex);
                    } else if (endVertex instanceof PetriPlace && startVertex instanceof PetriTransition) {
                        petriGraph.addEdge((PetriTransition) startVertex, (PetriPlace) endVertex);
                    }

                    Console.writeGraphValidationResult(petriGraph);
                }

                vv.repaint();
            }
        }

        this.startVertex = null;
        this.down = null;
        this.edgeIsDirected = EdgeType.UNDIRECTED;
        vv.removePostRenderPaintable(this.edgePaintable);
        vv.removePostRenderPaintable(this.arrowPaintable);
    }

    public void mouseDragged(MouseEvent e) {
        if (this.checkModifiers(e)) {
            if (this.startVertex != null) {
                this.transformEdgeShape(this.down, e.getPoint());
                if (this.edgeIsDirected == EdgeType.DIRECTED) {
                    this.transformArrowShape(this.down, e.getPoint());
                }
            }

            VisualizationViewer vv = (VisualizationViewer) e.getSource();
            vv.repaint();
        }

    }

    private void transformEdgeShape(Point2D down, Point2D out) {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();
        AffineTransform xform = AffineTransform.getTranslateInstance((double) x1, (double) y1);
        float dx = x2 - x1;
        float dy = y2 - y1;
        float thetaRadians = (float) Math.atan2((double) dy, (double) dx);
        xform.rotate((double) thetaRadians);
        float dist = (float) Math.sqrt((double) (dx * dx + dy * dy));
        xform.scale((double) dist / this.rawEdge.getBounds().getWidth(), 1.0D);
        this.edgeShape = xform.createTransformedShape(this.rawEdge);
    }

    private void transformArrowShape(Point2D down, Point2D out) {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();
        AffineTransform xform = AffineTransform.getTranslateInstance((double) x2, (double) y2);
        float dx = x2 - x1;
        float dy = y2 - y1;
        float thetaRadians = (float) Math.atan2((double) dy, (double) dx);
        xform.rotate((double) thetaRadians);
        this.arrowShape = xform.createTransformedShape(this.rawArrowShape);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();
        c.setCursor(this.cursor);
    }

    public void mouseExited(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();
        c.setCursor(Cursor.getPredefinedCursor(0));
    }

    public void mouseMoved(MouseEvent e) {
    }

    class ArrowPaintable implements VisualizationServer.Paintable {
        ArrowPaintable() {
        }

        public void paint(Graphics g) {
            if (PetriNetEditingEdgeMousePlugin.this.arrowShape != null) {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D) g).fill(PetriNetEditingEdgeMousePlugin.this.arrowShape);
                g.setColor(oldColor);
            }

        }

        public boolean useTransform() {
            return false;
        }
    }

    class EdgePaintable implements VisualizationServer.Paintable {
        EdgePaintable() {
        }

        public void paint(Graphics g) {
            if (PetriNetEditingEdgeMousePlugin.this.edgeShape != null) {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D) g).draw(PetriNetEditingEdgeMousePlugin.this.edgeShape);
                g.setColor(oldColor);
            }

        }

        public boolean useTransform() {
            return false;
        }
    }
}
