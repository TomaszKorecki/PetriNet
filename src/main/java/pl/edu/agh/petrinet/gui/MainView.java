package pl.edu.agh.petrinet.gui;

import com.sun.deploy.panel.RadioPropertyGroup;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.model.*;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;
import java.awt.*;


public class MainView extends Application {
    private Stage primaryStage;
    private BorderPane rootPane;

    private VBox leftPane;
    private StackPane centerPane;

    private SwingNode swingNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createMenuStructure(primaryStage);
    }

    /*
    Creates basic menu structure
     */
    private void createMenuStructure(Stage primaryStage){
        rootPane = new BorderPane();
        createLeftPane();
        createCenterPane();
        primaryStage.setTitle("Petri Net Editor");
        primaryStage.setScene(new Scene(rootPane, 900, 640));
        primaryStage.show();
    }

    private void createLeftPane(){
        leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.setMinWidth(200);

        createPetriNetTypeMenu();


        rootPane.setLeft(leftPane);
    }

    private void createHandleToolMenu(){

    }

    private void createPetriNetTypeMenu(){
        VBox petriNetTypePane = new VBox(5);
        final ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton normalNetRadioButton = new RadioButton("Normal");
        RadioButton priorityNetRadioButton = new RadioButton("Priority");
        RadioButton timeNetRadioButton = new RadioButton("Time");
        normalNetRadioButton.setToggleGroup(toggleGroup);
        priorityNetRadioButton.setToggleGroup(toggleGroup);
        timeNetRadioButton.setToggleGroup(toggleGroup);

        normalNetRadioButton.setSelected(true);

        Text headerText = new Text("Petri Net type");
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setMinHeight(2);

        petriNetTypePane.getChildren().addAll(headerText, normalNetRadioButton, priorityNetRadioButton, timeNetRadioButton, separator);
        leftPane.getChildren().addAll(petriNetTypePane);
    }


    /*
    Creates pane for JUNG's Graph
     */
    private void createCenterPane() {
        centerPane = new StackPane();

        PetriGraph petriGraph = new PetriGraph();
        fillDefaultGraph(petriGraph);

        swingNode = new SwingNode();
        PetriNetVisualizationViewer petriNetVIsualizationViewer = new PetriNetVisualizationViewer(petriGraph);

        SwingUtilities.invokeLater(() -> swingNode.setContent(petriNetVIsualizationViewer.getVisualizationViewer()));

        centerPane.getChildren().add(swingNode);
        rootPane.setCenter(centerPane);
    }

    private void fillDefaultGraph(PetriGraph petriGraph){
        PetriPlace v1 = new PetriPlace(0, "PP0", 1);
        PetriPlace v2 = new PetriPlace(1, "PP1");
        PetriPlace v3 = new PetriPlace(2, "PP2", 1);
        PetriPlace v4 = new PetriPlace(3, "PP3");
        PetriPlace v5 = new PetriPlace(4, "PP4");

        petriGraph.addPlace(v1);
        petriGraph.addPlace(v2);
        petriGraph.addPlace(v3);
        petriGraph.addPlace(v4);
        petriGraph.addPlace(v5);

        PetriTransition t1 = new PetriTransition(0, petriGraph.getType());
        PetriTransition t2 = new PetriTransition(1, petriGraph.getType());
        PetriTransition t3 = new PetriTransition(2, petriGraph.getType());
        PetriTransition t4 = new PetriTransition(3, petriGraph.getType());

        petriGraph.addTransition(t1);
        petriGraph.addTransition(t2);
        petriGraph.addTransition(t3);
        petriGraph.addTransition(t4);

        petriGraph.addEdge(v1, t1);
        petriGraph.addEdge(t1, v2);
        petriGraph.addEdge(v2, t2);
        petriGraph.addEdge(t2, v5, 2);
        petriGraph.addEdge(t2, v1);
        petriGraph.addEdge(v5, t3, 2);
        petriGraph.addEdge(t3, v4);
        petriGraph.addEdge(v4, t4);
        petriGraph.addEdge(t4, v3);
        petriGraph.addEdge(v3, t3);
    }
}
