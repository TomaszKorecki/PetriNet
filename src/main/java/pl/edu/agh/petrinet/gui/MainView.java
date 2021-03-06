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
import pl.edu.agh.petrinet.gui.submenus.*;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriGraphUtils;


public class MainView extends Application {
    private Stage primaryStage;
    private BorderPane rootPane;

    private VBox leftPane;
    private VBox rightPane;
    private StackPane centerPane;
    private Pane bottomPane;

    private SwingNode swingNode;

    private PetriGraph petriGraph;

    private PetriNetVisualizationViewer petriNetVIsualizationViewer;

    private SelectedToolGUI selectedToolGUI;
    private PetriNetTypeGUI netTypeGUI;
    private SimulationGUI simulationGUI;
    private Pane simulationPane;

    private AttributesGUI attributesGUI;
    private ArchivingGUI archivingGUI;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

		swingNode = new SwingNode();
        petriGraph = PetriGraphUtils.createTestPetriGraph();
        petriNetVIsualizationViewer = new PetriNetVisualizationViewer(petriGraph, swingNode);

        createMenuStructure(primaryStage);
    }

    /*
    Creates basic menu structure
     */
    private void createMenuStructure(Stage primaryStage){
        rootPane = new BorderPane();
        createCenterPane();
        createLeftPane();
        createRightPane();
        createBottomPane();
        primaryStage.setTitle("Petri Net Editor");
        primaryStage.setScene(new Scene(rootPane, 900, 640));
        primaryStage.show();
    }


    /*
    Creates left pane with simulation gui
     */
    private void createLeftPane(){
        leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.setMinWidth(200);

        selectedToolGUI  = new SelectedToolGUI(petriNetVIsualizationViewer, this);
        netTypeGUI = new PetriNetTypeGUI(petriNetVIsualizationViewer, this);
        simulationGUI = new SimulationGUI(petriNetVIsualizationViewer, this);
        simulationPane = simulationGUI.getNewSimulationPane();

        leftPane.getChildren().addAll(selectedToolGUI.getPane(),netTypeGUI.getPane(), simulationPane);
        rootPane.setLeft(leftPane);
    }

    private void createRightPane(){
        rightPane = new VBox(10);
        rightPane.setPadding(new Insets(10, 10, 10, 10));
        rightPane.setMinWidth(200);


        archivingGUI = new ArchivingGUI(petriNetVIsualizationViewer, primaryStage, this);
        attributesGUI = new AttributesGUI(petriNetVIsualizationViewer, this);

        rightPane.getChildren().addAll(
                archivingGUI.getArchivingPane(),
                attributesGUI.getAttributesPane());

        rootPane.setRight(rightPane);
    }

    /*
   Creates pane for JUNG's Graph
    */
    private void createCenterPane() {
        centerPane = new StackPane();

        centerPane.getChildren().add(swingNode);
        rootPane.setCenter(centerPane);
    }

    private void createBottomPane(){
        rootPane.setBottom(Console.getConsoleInstance().getConsole());
    }


    public void refreshSimulationMenu(){
        leftPane.getChildren().remove(simulationPane);
        simulationPane = simulationGUI.getNewSimulationPane();
        leftPane.getChildren().addAll(simulationPane);
    }

    public void setGraphType(PetriGraph.Type type){
        netTypeGUI.setManuallyType(type);
    }


}
