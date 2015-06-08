package pl.edu.agh.petrinet.gui.visualizationViewers;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.*;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization3d.decorators.PickableVertexPaintTransformer;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import pl.edu.agh.petrinet.gui.customPlugins.MouseModeChangedCallback;
import pl.edu.agh.petrinet.gui.customPlugins.PetriNetGraphPopup;
import pl.edu.agh.petrinet.gui.customPlugins.PetriNetModalGraphMouse;
import pl.edu.agh.petrinet.model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomasz on 5/2/2015.
 */
public class PetriNetVisualizationViewer {
	private final Color VERTEX_COLOR = Color.lightGray;
	private final Color VERTEX_SELECTED_COLOR = Color.yellow;

	private final Color VERTEX_SIMULATION_COLOR = Color.white;
	private final Color VERTEX_SIMULATION_HIGHLIGHTED_COLOR = Color.yellow;
	private final Color VERTEX_SIMULATION_ACTIVE_COLOR = Color.red;

	private final Color EDGE_LABEL_SELECTED_COLOR = Color.black;
	private final int EDGES_LABEL_FONT_SIZE = 18;
	private final int VERTICES_LABEL_FONT_SIZE = 18;

	private PetriGraph petriGraph;
	private VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer;

	//For simulation purpose
	private List<Integer> highlightedTransitions;
	private int activeTransition;

	private SwingNode swingNode;

	PetriNetModalGraphMouse gm;
	private MouseModeChangedCallback mouseModeChangedCallback;

	public PetriNetVisualizationViewer(PetriGraph petriGraph, SwingNode swingNode) {
		this.petriGraph = petriGraph;
		this.swingNode = swingNode;
		initialize();
	}

	private void initialize() {
		visualizationViewer = new VisualizationViewer<>(new ISOMLayout<>(petriGraph.getGraph()));
		visualizationViewer.setBorder(new TitledBorder("Graf testowy"));

		gm = new PetriNetModalGraphMouse<>(petriGraph, mouseModeChangedCallback);

		visualizationViewer.setGraphMouse(gm);

		gm.add(new PetriNetGraphPopup(visualizationViewer, petriGraph));

		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);

		visualizationViewer.addKeyListener(gm.getModeKeyListener());
		visualizationViewer.addKeyListener(new PetriNetKeyAdapter(petriGraph));
		visualizationViewer.setGraphMouse(gm);


		visualizationViewer.getRenderContext().setVertexShapeTransformer(createVertexShapeTransformer());
		visualizationViewer.getRenderContext().setVertexFillPaintTransformer(createVertexFillPaintTransformer());
		visualizationViewer.getRenderContext().setEdgeFontTransformer(createEdgeFontTransformer());
		visualizationViewer.getRenderContext().setVertexFontTransformer(createVertexFontTransformer());

		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);

		visualizationViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());

		visualizationViewer.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(EDGE_LABEL_SELECTED_COLOR, false));

		visualizationViewer.setPreferredSize(new Dimension(600, 400));

		SwingUtilities.invokeLater(() -> swingNode.setContent(visualizationViewer));
	}

	public PetriGraph getPetriGraph() {
		return this.petriGraph;
	}

	public AbstractModalGraphMouse getModalMousePlugin(){
		return gm;
	}

	public void registerCallback(MouseModeChangedCallback callback){
		this.mouseModeChangedCallback = callback;
		this.gm.setModeChangedCallback(callback);
	}

	public void setPetriGraph(PetriGraph petriGraph) {
		this.petriGraph = petriGraph;
		initialize();
		visualizationViewer.repaint();
	}

	public VisualizationViewer<PetriVertex, PetriEdge> getVisualizationViewer() {
		return this.visualizationViewer;
	}

	public void enterSimulationMode() {
		highlightedTransitions = new LinkedList<>();
		visualizationViewer.getRenderContext().setVertexFillPaintTransformer(createSimulationVertexFillPaintTransformer());
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		visualizationViewer.repaint();
	}

	public void exitSimulationMode() {
		visualizationViewer.getRenderContext().setVertexFillPaintTransformer(createVertexFillPaintTransformer());
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		petriGraph.resetAllVerticesSpecialValues();
		visualizationViewer.repaint();
	}

	public void setHighlightedTransitions(List<Integer> highlightedTransitions) {
		this.highlightedTransitions = highlightedTransitions;
		visualizationViewer.repaint();
	}

	public void setHighlightedTransition(Integer highlightedTransition) {
		this.highlightedTransitions.clear();
		this.activeTransition = highlightedTransition;
		visualizationViewer.repaint();
	}


	private PickableVertexPaintTransformer<PetriVertex> createVertexFillPaintTransformer() {
		return new PickableVertexPaintTransformer<PetriVertex>(
				visualizationViewer.getPickedVertexState(),
				VERTEX_COLOR,
				VERTEX_SELECTED_COLOR);
	}

	private Transformer<PetriVertex, Paint> createSimulationVertexFillPaintTransformer() {
		return (PetriVertex petriVertex) -> {
			if (petriVertex instanceof PetriTransition) {
//				if (highlightedTransitions.contains(petriVertex.getId()))
//					return VERTEX_SIMULATION_HIGHLIGHTED_COLOR;
//				else if (activeTransition == petriVertex.getId()) {
//					return VERTEX_SIMULATION_ACTIVE_COLOR;
//				} else {
//					return VERTEX_SIMULATION_COLOR;
//				}
//			} else {
//				return VERTEX_SIMULATION_COLOR;
//			}
				if (highlightedTransitions.contains(petriVertex.getId()))
					return VERTEX_SIMULATION_HIGHLIGHTED_COLOR;
				else
					return VERTEX_SIMULATION_COLOR;
			} else {
				return VERTEX_SIMULATION_COLOR;
			}

		};
	}

	private Transformer<PetriVertex, Shape> createVertexShapeTransformer() {
		return petriVertex -> {
			if (petriVertex instanceof PetriPlace) {
				return new Ellipse2D.Double(-15, -15, 30, 30);
			} else if (petriVertex instanceof PetriTransition) {
				return new Rectangle2D.Float(0, -20, 15, 40);
			} else return null;
		};
	}

	private Transformer<PetriEdge, Font> createEdgeFontTransformer() {
		return petriEdge -> new Font("Default", 0, EDGES_LABEL_FONT_SIZE);
	}

	private Transformer<PetriVertex, Font> createVertexFontTransformer() {
		return petriVertex -> new Font("Default", 0, VERTICES_LABEL_FONT_SIZE);
	}

	private class PetriNetKeyAdapter extends KeyAdapter {
		private PetriGraph petriGraph;

		public PetriNetKeyAdapter(PetriGraph petriGraph) {
			this.petriGraph = petriGraph;
		}


		public void keyTyped(KeyEvent event) {
			char keyChar = event.getKeyChar();
			int number;

			if (Character.isDigit(keyChar)) {
				number = Character.getNumericValue(keyChar);
			} else {
				return;
			}

			VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer = (VisualizationViewer) event.getSource();

			PetriEdge[] pickedEdges = visualizationViewer.getPickedEdgeState().getPicked().toArray(new PetriEdge[0]);
			PetriVertex[] pickedVertices = visualizationViewer.getPickedVertexState().getPicked().toArray(new PetriVertex[0]);

			if (pickedEdges.length == 1 && pickedVertices.length == 0) {
				PetriEdge pickedEdge = pickedEdges[0];
				if (number > 0) {
					pickedEdge.setMarkersCount(number);
					visualizationViewer.repaint();
				}
			} else if (pickedEdges.length == 0 && pickedVertices.length == 1) {
				PetriVertex pickedVertex = pickedVertices[0];

				if (pickedVertex instanceof PetriPlace) {
					((PetriPlace) pickedVertex).setStartupMarkersCount(number);
				} else if (pickedVertex instanceof PetriTransition) {
					((PetriTransition) pickedVertex).setSpecialTypeValue(number);
				}

				visualizationViewer.repaint();
			}
		}
	}
}
