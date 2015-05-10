package pl.edu.agh.petrinet.gui;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.simulation.BasicSimulation;
import pl.edu.agh.petrinet.simulation.DefaultSimulation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SimulationGUI {
    //private TextField simulationDelayTextField;
    private RadioButton isSimulationAutomaticRadioButton;
    private Pane simulationPane;

    private PetriNetVisualizationViewer petriNetVisualizationViewer;
    private PetriGraph petriGraph;

    private ScrollPane consoleScrollPane;
    private TextArea consoleTextArea;

    private FlowPane availableTransitionsPane;

    public SimulationGUI(PetriNetVisualizationViewer petriNetVisualizationViewer) {
        this.petriNetVisualizationViewer = petriNetVisualizationViewer;
        this.petriGraph = petriNetVisualizationViewer.getPetriGraph();
        createSimulationMenu();
    }

    public Pane getNewSimulationPane() {
        createSimulationMenu();
        return simulationPane;
    }

    public ScrollPane getSimulationConsole() {
        return consoleScrollPane;
    }

    public void writeOnConsole(String consoleMessage) {
        consoleTextArea.appendText("\n" + consoleMessage);
    }

    public void clearConsole() {
        consoleTextArea.clear();
    }

    private void createSimulationMenu() {
        VBox petriSimulationMenu = new VBox(5);
        simulationPane = petriSimulationMenu;

        Text headerText = new Text("Simulation");
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setMinHeight(2);

//        Text delayText = new Text("Delay [s]:");
//        simulationDelayTextField = new TextField();
//        simulationDelayTextField.setPromptText("1");

//        simulationDelayTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                simulationDelayTextField.setText(oldValue);
//            }
//        });

//        HBox delayPane = new HBox();
//        delayPane.getChildren().addAll(delayText, simulationDelayTextField);
//        delayPane.setSpacing(10);

        isSimulationAutomaticRadioButton = new RadioButton("Automatic");
        isSimulationAutomaticRadioButton.setSelected(true);

        Button runSimulationButton = new Button("Start");
        runSimulationButton.setOnAction(event -> onRunSimulation());

        Button oneSimulationStepButton = new Button("Stop");
        oneSimulationStepButton.setOnAction(event -> {
            endManualSimulation();
        });

        petriSimulationMenu.getChildren().addAll(headerText, isSimulationAutomaticRadioButton, runSimulationButton, oneSimulationStepButton, separator);

        createConsole();
    }

    private void createConsole() {
        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);

        consoleScrollPane = new ScrollPane();
        consoleScrollPane.setContent(consoleTextArea);
        consoleScrollPane.setFitToWidth(true);
        consoleScrollPane.setPrefHeight(180);
    }

    private void onRunSimulation() {
        clearConsole();

        if (petriGraph.getType() == PetriGraph.Type.DEFAULT) {
            if (isSimulationAutomaticRadioButton.isSelected()) {
                runAutomateDefaultSimulation();
            } else {
                runManualDefaultSimulation();
            }
        }
    }

    private void runAutomateDefaultSimulation() {
        petriGraph.compute();
        DefaultSimulation simulation = new DefaultSimulation(petriGraph);

        List<Integer> transitions;
        Random r = new Random();
        int transition;
        for (int i = 0; i < 15; i++) {
            if (simulation.isSimulationEnded()) {
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

    private void runManualDefaultSimulation() {
        availableTransitionsPane = new FlowPane();
        availableTransitionsPane.setMaxWidth(180);
        simulationPane.getChildren().add(availableTransitionsPane);

        petriGraph.compute();
        DefaultSimulation simulation = new DefaultSimulation(petriGraph);

        petriNetVisualizationViewer.enterSimulationMode();

        List<Integer> availableTransitions;
        availableTransitions = simulation.getPossibleTransitions();

        petriNetVisualizationViewer.setHighlightedTransitions(availableTransitions);
        petriNetVisualizationViewer.getVisualizationViewer().repaint();
        prepareForNextManualSimulationStep(availableTransitions, simulation);

    }

    private void prepareForNextManualSimulationStep(List<Integer> transitions, BasicSimulation simulation) {
        if (simulation.isSimulationEnded()) return;

        availableTransitionsPane.getChildren().clear();

        for (int i = 0; i < transitions.size(); i++) {
            Button button = new Button(transitions.get(i).toString());
            button.setMaxSize(32, 32);
            availableTransitionsPane.getChildren().add(button);

            final int transitionId = new Integer(transitions.get(i));

            button.setOnAction(event -> {
                simulation.stepSimulate(transitionId);
                String consoleMessage = "Transition " + transitionId + ": ";
                consoleMessage += Arrays.toString(petriGraph.getCurrentState());
                System.out.println(consoleMessage);
                writeOnConsole(consoleMessage);

                List<Integer> availableTransitions;
                availableTransitions = simulation.getPossibleTransitions();

                petriNetVisualizationViewer.setHighlightedTransitions(availableTransitions);
                petriNetVisualizationViewer.getVisualizationViewer().repaint();

                prepareForNextManualSimulationStep(availableTransitions, simulation);
            });
        }
    }



    private void endManualSimulation() {
        availableTransitionsPane.getChildren().clear();
        simulationPane.getChildren().remove(availableTransitionsPane);
        petriNetVisualizationViewer.exitSimulationMode();
    }
}
