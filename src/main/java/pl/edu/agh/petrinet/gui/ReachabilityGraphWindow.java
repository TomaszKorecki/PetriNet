package pl.edu.agh.petrinet.gui;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.algorithms.ReachabilityGraph;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.gui.visualizationViewers.ReachabilityGraphVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;
import pl.edu.agh.petrinet.model.PetriStateGraph;

/**
 * Created by Tomasz on 5/17/2015.
 */
public class ReachabilityGraphWindow  {
	private Stage primaryStage;
	private SwingNode swingNode;
	private PetriGraph petriGraph;
	private BorderPane rootPane;

	ReachabilityGraphVisualizationViewer reachabilityGraphVisualizationViewer;

	public  ReachabilityGraphWindow(PetriGraph petriGraph){
		this.petriGraph = petriGraph;
		rootPane = new BorderPane();
		swingNode = new SwingNode();
		rootPane.setCenter(swingNode);

		reachabilityGraphVisualizationViewer = new ReachabilityGraphVisualizationViewer(petriGraph, swingNode);

		primaryStage = new Stage();
		primaryStage.setTitle("Reachability graph");
		primaryStage.setScene(new Scene(rootPane, 900, 600));
	}

	public void show(){
		primaryStage.show();
	}

	public void hide(){
		primaryStage.close();
	}
}
