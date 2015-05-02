package pl.edu.agh.petrinet.gui;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.scene.shape.Circle;
import org.apache.commons.collections15.Transformer;
import pl.edu.agh.petrinet.model.*;

import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Tomasz on 5/2/2015.
 */
public class PetriNetVisualizationViewer {


    PetriGraph petriGraph;
    VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer;

    public PetriNetVisualizationViewer(PetriGraph petriGraph){
        this.petriGraph = petriGraph;

        visualizationViewer = new VisualizationViewer<>(new ISOMLayout<>(petriGraph.getGraph()));
        visualizationViewer.setBorder(new TitledBorder("Graf testowy"));

        DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<>();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizationViewer.setGraphMouse(gm);

        visualizationViewer.getRenderContext().setVertexFillPaintTransformer(CreateVertexFillPaintTransformer());
        visualizationViewer.getRenderContext().setVertexShapeTransformer(CreateVertexShapeTransformer());
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        visualizationViewer.setPreferredSize(new Dimension(600, 400));
    }

    private Transformer<PetriVertex, Paint> CreateVertexFillPaintTransformer(){
        return petriVertex -> Color.lightGray;
    }

    private Transformer<PetriVertex, Shape> CreateVertexShapeTransformer(){
       return petriVertex -> {
           if(petriVertex instanceof PetriPlace){
               return new Ellipse2D.Double(-15, -15, 30, 30);
           } else if (petriVertex instanceof PetriTransition){
               return new Rectangle2D.Float(-20, -20, 50, 30);
           } else return null;
       };
    }

    public VisualizationViewer<PetriVertex, PetriEdge> getVisualizationViewer(){
        return this.visualizationViewer;
    }
}
