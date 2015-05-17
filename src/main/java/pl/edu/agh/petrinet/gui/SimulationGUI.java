package pl.edu.agh.petrinet.gui;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
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

    private FlowPane availableTransitionsPane;

    Button runSimulationButton;
    Button stopSimulationButton;

    public SimulationGUI(PetriNetVisualizationViewer petriNetVisualizationViewer) {
        this.petriNetVisualizationViewer = petriNetVisualizationViewer;
        this.petriGraph = petriNetVisualizationViewer.getPetriGraph();
        createSimulationMenu();


    }

    public Pane getNewSimulationPane() {
        createSimulationMenu();
        return simulationPane;
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

        runSimulationButton = new Button("Start");
        runSimulationButton.setOnAction(event -> onRunSimulation());

        stopSimulationButton = new Button("Stop");
        stopSimulationButton.setOnAction(event -> {
            endManualSimulation();
        });

        stopSimulationButton.setDisable(true);

        petriSimulationMenu.getChildren().addAll(headerText, isSimulationAutomaticRadioButton, runSimulationButton, stopSimulationButton, separator);
    }



    private void onRunSimulation() {
        Console.clearConsole();

        petriGraph.validateGraph();
        if(!petriGraph.isGraphIsValid()){
            Console.writeGraphValidationResult(petriGraph);
            return;
        }


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
            Console.writeOnConsole(consoleMessage);
        }
    }

    private void runManualDefaultSimulation() {
        petriGraph.compute();
        runSimulationButton.setDisable(true);
        stopSimulationButton.setDisable(false);
        availableTransitionsPane = new FlowPane();
        availableTransitionsPane.setMaxWidth(180);
        simulationPane.getChildren().add(availableTransitionsPane);

        System.out.println(petriGraph);
        DefaultSimulation simulation = new DefaultSimulation(petriGraph);

        petriNetVisualizationViewer.enterSimulationMode();

        List<Integer> availableTransitions;
        availableTransitions = simulation.getPossibleTransitions();

        petriNetVisualizationViewer.setHighlightedTransitions(availableTransitions);
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
                Console.writeOnConsole(consoleMessage);

                List<Integer> availableTransitions;
                availableTransitions = simulation.getPossibleTransitions();

                petriNetVisualizationViewer.setHighlightedTransitions(availableTransitions);
                petriNetVisualizationViewer.getVisualizationViewer().repaint();

                prepareForNextManualSimulationStep(availableTransitions, simulation);
            });
        }
    }



    private void endManualSimulation() {
        runSimulationButton.setDisable(false);
        stopSimulationButton.setDisable(true);
        availableTransitionsPane.getChildren().clear();
        simulationPane.getChildren().remove(availableTransitionsPane);
        petriNetVisualizationViewer.exitSimulationMode();
    }
}
