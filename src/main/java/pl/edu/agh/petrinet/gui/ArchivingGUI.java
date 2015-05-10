package pl.edu.agh.petrinet.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.model.PetriGraph;

/**
 * Created by Tomasz on 5/10/2015.
 */
public class ArchivingGUI {
    private VBox archivingPane;

    private PetriNetVisualizationViewer petriNetVisualizationViewer;
    private PetriGraph petriGraph;


    public ArchivingGUI(PetriNetVisualizationViewer petriNetVisualizationViewer){
        this.petriNetVisualizationViewer = petriNetVisualizationViewer;
        this.petriGraph = petriGraph;
        createArchivingMenu();
    }

    public Pane getArchivingPane(){
        return archivingPane;
    }

    void createArchivingMenu(){
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

    void onSaveButton(){
        //Save petriGraph object to file...
    }

    void onLoadButton(){
        //Load petriGragh from file, setup new graph to visualization viewer and repaint it
    }


}
