package pl.edu.agh.petrinet.gui;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization3d.decorators.PickableVertexPaintTransformer;
import org.apache.commons.collections15.Transformer;
import pl.edu.agh.petrinet.gui.customPlugins.PetriNetEditingEdgeMousePlugin;
import pl.edu.agh.petrinet.gui.customPlugins.PetriNetGraphPopup;
import pl.edu.agh.petrinet.gui.customPlugins.PetriNetModalGraphMouse;
import pl.edu.agh.petrinet.model.*;

import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Tomasz on 5/2/2015.
 */
public class PetriNetVisualizationViewer {
    private final Color VERTEX_COLOR = Color.lightGray;
    private final Color VERTEX_SELECTED_COLOR = Color.yellow;

    private PetriGraph petriGraph;
    private VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer;

    public PetriNetVisualizationViewer(PetriGraph petriGraph) {
        this.petriGraph = petriGraph;

        visualizationViewer = new VisualizationViewer<>(new ISOMLayout<>(petriGraph.getGraph()));
        visualizationViewer.setBorder(new TitledBorder("Graf testowy"));

        AbstractModalGraphMouse gm = new PetriNetModalGraphMouse<>(petriGraph);

        visualizationViewer.setGraphMouse(gm);

        gm.add(new PetriNetGraphPopup(visualizationViewer, petriGraph));

        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        visualizationViewer.addKeyListener(gm.getModeKeyListener());
        visualizationViewer.setGraphMouse(gm);


        visualizationViewer.getRenderContext().setVertexShapeTransformer(createVertexShapeTransformer());
        visualizationViewer.getRenderContext().setVertexFillPaintTransformer(createVertexFillPaintTransformer());

        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        visualizationViewer.setPreferredSize(new Dimension(600, 400));
    }

    private PickableVertexPaintTransformer<PetriVertex> createVertexFillPaintTransformer() {
        return new PickableVertexPaintTransformer<PetriVertex>(
                visualizationViewer.getPickedVertexState(),
                VERTEX_COLOR,
                VERTEX_SELECTED_COLOR);
    }

    private Transformer<PetriVertex, Shape> createVertexShapeTransformer() {
        return petriVertex -> {
            if (petriVertex instanceof PetriPlace) {
                return new Ellipse2D.Double(-15, -15, 30, 30);
            } else if (petriVertex instanceof PetriTransition) {
                return new Rectangle2D.Float(-20, -20, 50, 30);
            } else return null;
        };
    }

    public VisualizationViewer<PetriVertex, PetriEdge> getVisualizationViewer() {
        return this.visualizationViewer;
    }
}
