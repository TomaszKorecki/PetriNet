package pl.edu.agh.petrinet.gui.submenus;

import javafx.geometry.Orientation;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.gui.MainView;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Created by Tomasz on 6/7/2015.
 */
public class PetriNetTypeGUI {

	private PetriNetVisualizationViewer petriNetVisualizationViewer;
	private MainView mainView;
	private VBox petriNetTypePane;

	RadioButton defaultNetRadioButton;
	RadioButton priorityNetRadioButton;
	RadioButton timeNetRadioButton;

	public PetriNetTypeGUI(PetriNetVisualizationViewer petriNetVisualizationViewer, MainView mainView) {
		this.petriNetVisualizationViewer = petriNetVisualizationViewer;
		this.mainView = mainView;
		createSelectionMenu();
	}

	private void createSelectionMenu() {

		petriNetTypePane = new VBox(5);

		Text headerText = new Text("Petri Net type");
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		separator.setMinHeight(2);

		final ToggleGroup toggleGroup = new ToggleGroup();
		defaultNetRadioButton = new RadioButton("Default");
		priorityNetRadioButton = new RadioButton("Priority");
		timeNetRadioButton = new RadioButton("Time");
		defaultNetRadioButton.setToggleGroup(toggleGroup);
		priorityNetRadioButton.setToggleGroup(toggleGroup);
		timeNetRadioButton.setToggleGroup(toggleGroup);

		defaultNetRadioButton.setSelected(true);
		toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("listener here...!");
			if (newValue == defaultNetRadioButton) {
				petriNetVisualizationViewer.getPetriGraph().setType(PetriGraph.Type.DEFAULT);
			} else if (newValue == priorityNetRadioButton) {
				petriNetVisualizationViewer.getPetriGraph().setType(PetriGraph.Type.PRIORYTY);
			} else if (newValue == timeNetRadioButton) {
				petriNetVisualizationViewer.getPetriGraph().setType(PetriGraph.Type.TIME);
			}

			mainView.refreshSimulationMenu();
		});


		petriNetTypePane.getChildren().addAll(headerText, defaultNetRadioButton, priorityNetRadioButton, timeNetRadioButton, separator);

	}

	public void setManuallyType(PetriGraph.Type type) {
		if (type == PetriGraph.Type.DEFAULT) {
			defaultNetRadioButton.setSelected(true);
		} else if (type == PetriGraph.Type.PRIORYTY) {
			priorityNetRadioButton.setSelected(true);
		} else if (type == PetriGraph.Type.TIME) {
			timeNetRadioButton.setSelected(true);
		}
	}

	public Pane getPane() {
		return petriNetTypePane;
	}
}
