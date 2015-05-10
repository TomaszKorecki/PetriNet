package pl.edu.agh.petrinet.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.simulation.DefaultSimulation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SimulationGUI {
    private TextField simulationDelayTextField;
    private RadioButton isSimulationAutomaticRadioButton;
    private Pane simulationPane;



    private PetriNetVisualizationViewer petriNetVisualizationViewer;
    private PetriGraph petriGraph;

    private ScrollPane consoleScrollPane;
    private TextArea consoleTextArea;

    public SimulationGUI(PetriNetVisualizationViewer petriNetVisualizationViewer){
        this.petriNetVisualizationViewer = petriNetVisualizationViewer;
        this.petriGraph = petriNetVisualizationViewer.getPetriGraph();
        createSimulationMenu();
    }

    public Pane getNewSimulationPane(){
        createSimulationMenu();
        return simulationPane;
    }

    public ScrollPane getSimulationConsole(){
        return consoleScrollPane;
    }

    public void writeOnConsole(String consoleMessage){
        consoleTextArea.appendText("\n" + consoleMessage);
    }

    public void clearConsole(){
        consoleTextArea.clear();
    }

    private void createSimulationMenu(){
        VBox petriSimulationMenu = new VBox(5);
        simulationPane = petriSimulationMenu;

        Text headerText = new Text("Simulation");
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setMinHeight(2);

        Text delayText = new Text("Delay [ms]:");
        simulationDelayTextField = new TextField();
        simulationDelayTextField.setPromptText("100");

        simulationDelayTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                simulationDelayTextField.setText(oldValue);
            }
        });

        HBox delayPane = new HBox();
        delayPane.getChildren().addAll(delayText, simulationDelayTextField);
        delayPane.setSpacing(10);

        isSimulationAutomaticRadioButton = new RadioButton("Automatic");
        isSimulationAutomaticRadioButton.setSelected(true);

        Button runSimulationButton = new Button("Start");
        runSimulationButton.setOnAction(event -> onRunSimulation());

        Button oneSimulationStepButton = new Button("One Step");
        oneSimulationStepButton.setOnAction(event -> {

        });

        petriSimulationMenu.getChildren().addAll(headerText, delayPane, isSimulationAutomaticRadioButton, runSimulationButton, oneSimulationStepButton, separator);

        createConsole();
    }

    private void createConsole(){
        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);

        consoleScrollPane = new ScrollPane();
        consoleScrollPane.setContent(consoleTextArea);
        consoleScrollPane.setFitToWidth(true);
        consoleScrollPane.setPrefHeight(180);
    }

    private void onRunSimulation(){
        clearConsole();

        if(petriGraph.getType() == PetriGraph.Type.DEFAULT){
            if(isSimulationAutomaticRadioButton.isSelected()){
                runAutomateDefaultSimulation();
            }
        }
    }

    private void runAutomateDefaultSimulation(){
        petriGraph.compute();

        DefaultSimulation simulation = new DefaultSimulation(petriGraph);

        List<Integer> transitions;
        Random r = new Random();
        int transition;
        for(int i = 0; i < 15; i++){
            if(simulation.isSimulationEnded()){
                break;
            }

            transitions = simulation.getPossibleTransitions();
            transition = transitions.get(r.nextInt(transitions.size()));


            simulation.stepSimulate(transition);
            String consoleMessage = "Transition " + transition + ": ";
            consoleMessage += Arrays.toString(petriGraph.getCurrentState());
            System.out.println(consoleMessage);
            writeOnConsole(consoleMessage);
        }
    }
}
