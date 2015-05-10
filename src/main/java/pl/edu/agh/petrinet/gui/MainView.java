package pl.edu.agh.petrinet.gui;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;

import javax.swing.*;


public class MainView extends Application {
    private Stage primaryStage;
    private BorderPane rootPane;
    private VBox leftPane;
    private StackPane centerPane;
    private SwingNode swingNode;
    private Pane simulationPane;

    private PetriGraph petriGraph;



    private PetriNetVisualizationViewer petriNetVIsualizationViewer;

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

        simulationPane = new  SimulationGUI(petriGraph).getSimulationPane();
        leftPane.getChildren().addAll(simulationPane);

        rootPane.setLeft(leftPane);
    }

    private void createHandleToolMenu(){

    }

    private void createPetriNetTypeMenu(){
        VBox petriNetTypePane = new VBox(5);

        Text headerText = new Text("Petri Net type");
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setMinHeight(2);

        final ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton defaultNetRadioButton = new RadioButton("Default");
        RadioButton priorityNetRadioButton = new RadioButton("Priority");
        RadioButton timeNetRadioButton = new RadioButton("Time");
        defaultNetRadioButton.setToggleGroup(toggleGroup);
        priorityNetRadioButton.setToggleGroup(toggleGroup);
        timeNetRadioButton.setToggleGroup(toggleGroup);

        defaultNetRadioButton.setSelected(true);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == defaultNetRadioButton) {
                petriNetVIsualizationViewer.getPetriGraph().setType(PetriGraph.Type.DEFAULT);
            } else if (newValue == priorityNetRadioButton) {
                petriNetVIsualizationViewer.getPetriGraph().setType(PetriGraph.Type.PRIORYTY);
            } else if (newValue == timeNetRadioButton) {
                petriNetVIsualizationViewer.getPetriGraph().setType(PetriGraph.Type.TIME);
            }

            refreshSimulationMenu();
        });


        petriNetTypePane.getChildren().addAll(headerText, defaultNetRadioButton, priorityNetRadioButton, timeNetRadioButton, separator);
        leftPane.getChildren().addAll(petriNetTypePane);
    }

    private void refreshSimulationMenu(){
        leftPane.getChildren().remove(simulationPane);
        simulationPane = new  SimulationGUI(petriGraph).getSimulationPane();
        leftPane.getChildren().addAll(simulationPane);
    }

    /*
    Creates pane for JUNG's Graph
     */
    private void createCenterPane() {
        centerPane = new StackPane();

        petriGraph = PetriGraphUtils.createTestPetriGraph();

        swingNode = new SwingNode();
        petriNetVIsualizationViewer = new PetriNetVisualizationViewer(petriGraph);

        SwingUtilities.invokeLater(() -> swingNode.setContent(petriNetVIsualizationViewer.getVisualizationViewer()));

        centerPane.getChildren().add(swingNode);
        rootPane.setCenter(centerPane);
    }
}
