package pl.edu.agh.petrinet.gui;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.scene.shape.Circle;
import org.apache.commons.collections15.Transformer;
import pl.edu.agh.petrinet.model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.html.ObjectView;
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

        //DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<>();
        /*EditingModalGraphMouse<PetriVertex, PetriEdge> gm = new EditingModalGraphMouse<>(
                visualizationViewer.getRenderContext(),
                new PetriVertexFactory(petriGraph),
                new PetriEdgeFactory(petriGraph));*/


        AbstractModalGraphMouse gm = new DefaultModalGraphMouse<PetriVertex, PetriEdge>();
        visualizationViewer.setGraphMouse(gm);

        gm.add(new PickingGraphMousePlugin<PetriVertex, PetriEdge>());
        gm.add(new PetriNetGraphPopup(visualizationViewer, petriGraph));

        gm.setMode(ModalGraphMouse.Mode.EDITING);

        visualizationViewer.addKeyListener(gm.getModeKeyListener());
        visualizationViewer.setGraphMouse(gm);

        visualizationViewer.getRenderContext().setVertexFillPaintTransformer(createVertexFillPaintTransformer());
        visualizationViewer.getRenderContext().setVertexShapeTransformer(createVertexShapeTransformer());

        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        visualizationViewer.setPreferredSize(new Dimension(600, 400));
    }

    private Transformer<PetriVertex, Paint> createVertexFillPaintTransformer(){
        return petriVertex -> Color.lightGray;
    }

    private Transformer<PetriVertex, Shape> createVertexShapeTransformer(){
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
