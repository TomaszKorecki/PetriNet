package pl.edu.agh.petrinet.gui.customPlugins;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import javafx.scene.control.TextField;
import pl.edu.agh.petrinet.model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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
            AbstractAction[] actions = new AbstractAction[0];

            if(petriVertex != null){
                if(petriVertex instanceof PetriPlace){
                    actions = createActionsForPlace((PetriPlace)petriVertex);
                }else if(petriVertex instanceof  PetriTransition){
                    actions = createActionsForTransition((PetriTransition)petriVertex);
                }
            } else if(petriEdge != null){
                actions = createActionsForEdge(petriEdge);
            } else{
                actions = createDefaultActions(mouseEvent);
            }

            for (AbstractAction action : actions){
                popup.add(action);
            }
        }

        popup.show(visualizationViewer, mouseEvent.getX(), mouseEvent.getY());
    }

    private AbstractAction[] createActionsForPlace(PetriPlace place){
        ArrayList<AbstractAction> abstractActions = new ArrayList<AbstractAction>();

        abstractActions.add(new AbstractAction("Delete place") {
            @Override
            public void actionPerformed(ActionEvent e) {
                petriGraph.removePlace(place);
                visualizationViewer.repaint();
            }
        });
        
        return abstractActions.toArray(new AbstractAction[abstractActions.size()]);
    }

    private AbstractAction[] createActionsForTransition(PetriTransition transition){
        ArrayList<AbstractAction> abstractActions = new ArrayList<AbstractAction>();

        abstractActions.add(new AbstractAction("Delete transition") {
            @Override
            public void actionPerformed(ActionEvent e) {
                petriGraph.removeTransition(transition);
                visualizationViewer.repaint();
            }
        });

        return abstractActions.toArray(new AbstractAction[abstractActions.size()]);
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

    private AbstractAction[] createDefaultActions(MouseEvent mouseEvent){
        return new AbstractAction[]{
            new AbstractAction("Add place") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PetriPlace newPlace = new PetriPlace(petriGraph.getPlacesSmallestUniqId());
                    petriGraph.addPlace(newPlace);
                    Layout layout = visualizationViewer.getModel().getGraphLayout();
                    layout.setLocation(newPlace, visualizationViewer.getRenderContext().getMultiLayerTransformer().inverseTransform(mouseEvent.getPoint()));

                    visualizationViewer.repaint();
                }
            },
                new AbstractAction("Add transition") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PetriTransition petriTransition = new PetriTransition(petriGraph.getTransitionsSmallestUniqId(), petriGraph.getType());
                        petriGraph.addTransition(petriTransition);

                        Layout layout = visualizationViewer.getModel().getGraphLayout();
                        layout.setLocation(petriTransition, visualizationViewer.getRenderContext().getMultiLayerTransformer().inverseTransform(mouseEvent.getPoint()));

                        visualizationViewer.repaint();
                    }
                }
        };
    }
}
