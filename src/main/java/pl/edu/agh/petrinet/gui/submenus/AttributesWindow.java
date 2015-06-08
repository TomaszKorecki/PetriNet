package pl.edu.agh.petrinet.gui.submenus;

import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.algorithms.Attributes;
import pl.edu.agh.petrinet.gui.visualizationViewers.ReachabilityGraphVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Created by Tomasz on 6/7/2015.
 */
public class AttributesWindow {

	private Stage primaryStage;
	private PetriGraph petriGraph;
	private BorderPane rootPane;

	public AttributesWindow(PetriGraph petriGraph){
		this.petriGraph = petriGraph;
		rootPane = new BorderPane();

		Attributes calculatedAttributes = new Attributes(petriGraph);

		primaryStage = new Stage();
		primaryStage.setTitle("Attributes");
		primaryStage.setScene(new Scene(rootPane, 900, 600));
	}

	public void show(){
		primaryStage.show();
	}

	public void hide(){
		primaryStage.close();
	}

}
