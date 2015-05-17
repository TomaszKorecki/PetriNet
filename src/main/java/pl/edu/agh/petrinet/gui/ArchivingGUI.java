package pl.edu.agh.petrinet.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.serialization.PetriSerialization;

import java.io.File;

/**
 * Created by Tomasz on 5/10/2015.
 */
public class ArchivingGUI {
	private VBox archivingPane;
	private Stage primaryStage;
	private PetriNetVisualizationViewer petriNetVisualizationViewer;
	private PetriGraph petriGraph;


	public ArchivingGUI(PetriNetVisualizationViewer petriNetVisualizationViewer, Stage primaryStage) {
		this.petriNetVisualizationViewer = petriNetVisualizationViewer;
		this.primaryStage = primaryStage;
		this.petriGraph = petriNetVisualizationViewer.getPetriGraph();
		createArchivingMenu();
	}

	public Pane getArchivingPane() {
		return archivingPane;
	}

	void createArchivingMenu() {
		archivingPane = new VBox(5);
		archivingPane.setPadding(new Insets(10, 10, 10, 10));
		archivingPane.setMinWidth(150);
		archivingPane.setAlignment(Pos.TOP_RIGHT);

		Text headerText = new Text("Archiving");
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		separator.setMinHeight(2);

		Button saveButton = new Button("Save graph");
		saveButton.setOnAction(event -> onSaveButton());

		Button loadButton = new Button("Load graph");
		loadButton.setOnAction(event -> onLoadButton());

		archivingPane.getChildren().addAll(headerText, saveButton, loadButton, separator);
	}

	void onSaveButton() {
		petriGraph.compute();

		PetriSerialization petriSerialization = new PetriSerialization(petriGraph);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName("petriGraph.xml");
		File currentDir = new File(new File(".").getAbsolutePath());
		fileChooser.setInitialDirectory(currentDir);
		fileChooser.setTitle("Saving");
		File file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			try {
				if (!file.exists()) {
					file.createNewFile();
					file.setWritable(true);

				}

				petriSerialization.serialize(file);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	void onLoadButton(){
		PetriSerialization petriSerialization = new PetriSerialization();

		FileChooser fileChooser = new FileChooser();
		File currentDir = new File(new File(".").getAbsolutePath());
		fileChooser.setInitialDirectory(currentDir);
		fileChooser.setTitle("Opening");
		File file = fileChooser.showOpenDialog(primaryStage);

		if (file != null) {
			try {
				petriGraph = petriSerialization.deserialize(file);
				petriNetVisualizationViewer.setPetriGraph(petriGraph);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}