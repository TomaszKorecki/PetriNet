package pl.edu.agh.petrinet.gui.visualizationViewers;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.embed.swing.SwingNode;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriStateEdge;
import pl.edu.agh.petrinet.model.PetriStateVertex;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Tomasz on 5/17/2015.
 */
public class ReachabilityGraphVisualizationViewer {

	private PetriGraph petriGraph;
	private ReachabilityGraph reachabilityGraph;
	private VisualizationViewer<PetriStateVertex, PetriStateEdge> visualizationViewer;
	private SwingNode swingNode;

	public ReachabilityGraphVisualizationViewer(PetriGraph petriGraph, SwingNode swingNode) {
		this.petriGraph = petriGraph;
		this.reachabilityGraph = petriGraph.getReachabilityGraph();

		if (reachabilityGraph == null) {
			System.out.println("Reachability graph is null");
			return;
		}

		System.out.println(reachabilityGraph.getPetriStateGraph() == null);
		System.out.println(reachabilityGraph.getPetriStateGraph().getGraph() == null);

		this.swingNode = swingNode;
		initialize();
	}

	private void initialize() {
		visualizationViewer = new VisualizationViewer<>(new ISOMLayout<>(reachabilityGraph.getPetriStateGraph().getGraph()));
		visualizationViewer.setBorder(new TitledBorder(getProperTitle()));

		DefaultModalGraphMouse<PetriStateVertex, PetriStateEdge> defaultModalGraphMouse = new DefaultModalGraphMouse<>();
		visualizationViewer.addKeyListener(defaultModalGraphMouse.getModeKeyListener());

		visualizationViewer.setGraphMouse(defaultModalGraphMouse);
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);
		visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
		visualizationViewer.setPreferredSize(new Dimension(600, 400));

		SwingUtilities.invokeLater(() -> swingNode.setContent(visualizationViewer));
	}

	public String getProperTitle(){
		return (this.reachabilityGraph.isCoverabilityGraph() ? "Coverability" : "Reachability") + " graph";
	}
}
