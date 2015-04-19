package pl.edu.agh.petrinet.gui.graphs;

/**
 * Created by Tomasz on 4/19/2015.
 */

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import edu.uci.ics.jung.algorithms.layout.Layout;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.util.Pair;
import pl.edu.agh.petrinet.model.*;

import java.awt.*;

public class PetriGraphViewer  {
    private Layout<PetriVertex, PetriEdge> layout;
    private PetriGraph petriGraph;
    private double CIRCLE_SIZE = 20;

    public PetriGraphViewer(PetriGraph graph){
        this.petriGraph = graph;
        layout = new ISOMLayout<PetriVertex, PetriEdge>(petriGraph.getGraph());
        layout.setSize(new Dimension(800, 800));
    }

    public PetriGraphViewer(){
        petriGraph = new PetriGraph();
        layout = new ISOMLayout<PetriVertex, PetriEdge>(petriGraph.getGraph());
        layout.setSize(new Dimension(800, 800));
    }

    public BasicVisualizationServer getVisualization(){
        BasicVisualizationServer<PetriVertex,PetriEdge> vv = new BasicVisualizationServer<PetriVertex,PetriEdge>(layout);
        vv.setPreferredSize(new Dimension(350, 350)); //Sets the viewing area size
        return vv;
    }

    public Layout<PetriVertex, PetriEdge> getLayout(){
        return layout;
    }

    public void drawGraph(Pane pane){

    }


    public static PetriGraphViewer GetTestPetriGraphViewer(){
        PetriGraph ptGrapgh = new PetriGraph();

        PetriPlace p1 = new PetriPlace(1);
        PetriPlace p2 = new PetriPlace(2);

        PetriTransition t1 = new PetriTransition(1);
        PetriTransition t2 = new PetriTransition(2);

        ptGrapgh.addEdge(new PetriEdge(), p1, t1);
        ptGrapgh.addEdge(new PetriEdge(), t1, p2);

        System.out.println(ptGrapgh.getGraph().toString());

        return new PetriGraphViewer(ptGrapgh);
    }
}
