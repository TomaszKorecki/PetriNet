package pl.edu.agh.petrinet.gui.graphs;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Dimension;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;

public class GraphViewer<V, E> extends Region{
    private Relaxer relaxer;
    private Layout<V,E> layout;
    private double CIRCLE_SIZE = 20;

    public GraphViewer(Layout<V, E> layout) {
        this.layout = layout;
    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        layout.setSize(new Dimension(widthProperty().intValue(), heightProperty().intValue()));
        // relax the layout
        if(relaxer != null) {
            relaxer.stop();
            relaxer = null;
        }
        if(layout instanceof IterativeContext) {
            layout.initialize();
            if(relaxer == null) {
                relaxer = new VisRunner((IterativeContext)this.layout);
                relaxer.prerelax();
                relaxer.relax();
            }
        }

        Graph<V, E> graph = layout.getGraph();


        // draw the vertices in the graph
        for (V v : graph.getVertices()) {
            // Get the position of the vertex
            java.awt.geom.Point2D p = layout.transform(v);

            // draw the vertex as a circle

            Circle circle = CircleBuilder.create()
                    .centerX(p.getX())
                    .centerY(p.getY())
                    .radius(CIRCLE_SIZE)
                    .build();

            // add it to the group, so it is shown on screen
            this.getChildren().add(circle);
        }

        // draw the edges
        for (E e : graph.getEdges()) {
            // get the end points of the edge
            edu.uci.ics.jung.graph.util.Pair<V> endpoints = graph.getEndpoints(e);

            // Get the end points as Point2D objects so we can use them in the
            // builder
            java.awt.geom.Point2D pStart = layout.transform(endpoints.getFirst());
            java.awt.geom.Point2D pEnd = layout.transform(endpoints.getSecond());

            // Draw the line
            Line line = LineBuilder.create()
                    .startX(pStart.getX())
                    .startY(pStart.getY())
                    .endX(pEnd.getX())
                    .endY(pEnd.getY())
                    .build();
            // add the edges to the screen
            this.getChildren().add(line);
        }
    }



}