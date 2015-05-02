package pl.edu.agh.petrinet.gui;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import pl.edu.agh.petrinet.model.PetriEdge;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriVertex;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

/**
 * Created by Tomasz on 5/2/2015.
 */
public class PetriNetGraphPopup extends AbstractPopupGraphMousePlugin implements MouseListener {

    private VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer;
    private PetriGraph petriGraph;

    public PetriNetGraphPopup(VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer, PetriGraph petriGraph) {
        super(MouseEvent.BUTTON3_MASK);
        this.visualizationViewer = visualizationViewer;
        this.petriGraph = petriGraph;
    }


    @Override
    protected void handlePopup(MouseEvent mouseEvent) {

        final Point2D p = mouseEvent.getPoint();
        System.out.println("mouse event!");

        JPopupMenu popup = new JPopupMenu();
        GraphElementAccessor<PetriVertex,PetriEdge> pickSupport = visualizationViewer.getPickSupport();

        if(pickSupport != null){
            System.out.println("edge or vertex!");

            final PetriVertex petriVertex = pickSupport.getVertex(visualizationViewer.getGraphLayout(), mouseEvent.getX(), mouseEvent.getY());
            final PetriEdge petriEdge = pickSupport.getEdge(visualizationViewer.getGraphLayout(), mouseEvent.getX(), mouseEvent.getY());
            if(petriVertex != null){
                System.out.println("it's vertex!");
            } else if(petriEdge != null){
                System.out.println("it's edge!");
                for(AbstractAction action : createActionsForEdge(petriEdge)){
                    popup.add(action);
                }
            } else{
                System.out.println("it's nothing!");
            }
        }

        popup.add(new AbstractAction("custom action") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        popup.show(visualizationViewer, mouseEvent.getX(), mouseEvent.getY());
    }

    private AbstractAction[] createActionsForEdge(PetriEdge edge){
        return new AbstractAction[]{
                new AbstractAction("Delete edge") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        petriGraph.removeEdge(edge);
                        visualizationViewer.repaint();
                    }
                }
        };
    }
}
