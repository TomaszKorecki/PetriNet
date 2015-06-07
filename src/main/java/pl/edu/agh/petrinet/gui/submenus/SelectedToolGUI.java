package pl.edu.agh.petrinet.gui.submenus;

import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.gui.MainView;
import pl.edu.agh.petrinet.gui.customPlugins.MouseModeChangedCallback;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;


/**
 * Created by Tomasz on 6/7/2015.
 */
public class SelectedToolGUI {



//	private final String TRANSFORM_SPRITE_FILENAME = "file:resources/transformSprite.PNG";
//	private final String PICK_SPRITE_FILENAME = "file:resources/pickSprite.PNG";
//	private final String EDGE_SPRITE_FILENAME = "file:resources/edgeSprite.PNG";


	private PetriNetVisualizationViewer petriNetVisualizationViewer;
	private MainView mainView;
	private VBox selectedToolPane;

	public SelectedToolGUI(PetriNetVisualizationViewer petriNetVisualizationViewer, MainView mainView) {
		this.petriNetVisualizationViewer = petriNetVisualizationViewer;
		this.mainView = mainView;
		createMenu();
	}


	private void createMenu() {

		selectedToolPane = new VBox(5);

		Text headerText = new Text("Mouse tools");
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		separator.setMinHeight(2);

		final ToggleGroup handlesGroup = new ToggleGroup();

		ToggleButton transformTooggle = new ToggleButton("Transform");
		transformTooggle.setToggleGroup(handlesGroup);
		transformTooggle.setMinSize(16,16);
		ToggleButton pickTooggle = new ToggleButton("Pick");
		pickTooggle.setToggleGroup(handlesGroup);
		pickTooggle.setMinSize(16,16);
		ToggleButton edgeTooggle = new ToggleButton("Edit");
		edgeTooggle.setToggleGroup(handlesGroup);
		edgeTooggle.setMinSize(16,16);

		transformTooggle.setSelected(true);

		HBox handles = new HBox();
		handles.getChildren().addAll(transformTooggle, pickTooggle, edgeTooggle);

		handlesGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == transformTooggle) {
				petriNetVisualizationViewer.getModalMousePlugin().setMode(ModalGraphMouse.Mode.TRANSFORMING);
			} else if (newValue == pickTooggle) {
				petriNetVisualizationViewer.getModalMousePlugin().setMode(ModalGraphMouse.Mode.PICKING);
			} else if (newValue == edgeTooggle) {
				petriNetVisualizationViewer.getModalMousePlugin().setMode(ModalGraphMouse.Mode.EDITING);
			}

			mainView.refreshSimulationMenu();
		});

		petriNetVisualizationViewer.registerCallback(mode -> {
			Platform.runLater(() -> {
				if(mode == ModalGraphMouse.Mode.TRANSFORMING){
					transformTooggle.setSelected(true);
				} else if (mode == ModalGraphMouse.Mode.PICKING){
					pickTooggle.setSelected(true);
				} else if(mode == ModalGraphMouse.Mode.EDITING){
					edgeTooggle.setSelected(true);
				}
			});
		});

		selectedToolPane.getChildren().addAll(headerText, handles, separator);
	}

	public Pane getPane() {
		return selectedToolPane;
	}

}
