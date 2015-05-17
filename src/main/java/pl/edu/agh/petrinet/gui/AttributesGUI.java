package pl.edu.agh.petrinet.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.algorithms.Attributes;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Created by Tomasz on 5/17/2015.
 */
public class AttributesGUI {

	private Pane attributesPane;

	private PetriNetVisualizationViewer petriNetVisualizationViewer;
	private PetriGraph petriGraph;


	public AttributesGUI(PetriNetVisualizationViewer petriNetVisualizationViewer) {
		this.petriNetVisualizationViewer = petriNetVisualizationViewer;
		this.petriGraph = petriNetVisualizationViewer.getPetriGraph();
		createAttributesMenu();
	}

	public Pane getAttributesPane() {
		return attributesPane;
	}

	private void createAttributesMenu() {
		VBox attributesVPane = new VBox(5);
		attributesPane = attributesVPane;
		attributesPane.setMinWidth(150);
		attributesVPane.setAlignment(Pos.TOP_RIGHT);
		attributesVPane.setPadding(new Insets(10, 10, 10, 10));

		Text headerText = new Text("Attributes");
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		separator.setMinHeight(2);

		Button refreshAttributesButton = new Button("Calcuate");
		refreshAttributesButton.setOnAction(event -> {

			petriGraph.validateGraph();
			if (!petriGraph.isGraphIsValid()) {
				Console.writeGraphValidationResult(petriGraph);
			} else {
				petriGraph.compute();
				Attributes calculatedAttributes = new Attributes(petriGraph);

				Console.clearConsole();
				Console.writeOnConsole(calculatedAttributes.oneColumnString());
			}
		});

		Button reachabilityGraphButton = new Button("Reachability graph");
		reachabilityGraphButton.setOnAction(event -> {
			petriGraph.validateGraph();
			if (!petriGraph.isGraphIsValid()) {
				Console.writeGraphValidationResult(petriGraph);
			} else {
				ReachabilityGraphWindow window = new ReachabilityGraphWindow(petriGraph);
				window.show();
			}
		});


		attributesVPane.getChildren().addAll(headerText, separator, refreshAttributesButton, reachabilityGraphButton);
	}
}