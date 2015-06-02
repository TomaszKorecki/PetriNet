package pl.edu.agh.petrinet.gui.submenus;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import pl.edu.agh.petrinet.gui.Console;
import pl.edu.agh.petrinet.gui.MainView;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.simulation.BasicSimulation;
import pl.edu.agh.petrinet.simulation.DefaultSimulation;
import pl.edu.agh.petrinet.simulation.PetriNetSimulationFactory;

import javax.print.attribute.standard.NumberUp;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SimulationGUI {
	private MainView mainView;
	private TextField simulationDelayTextField;
	private TextField simulationStepsTextField;
	private RadioButton isSimulationAutomaticRadioButton;
	private Pane simulationPane;

	private PetriNetVisualizationViewer petriNetVisualizationViewer;

	private FlowPane availableTransitionsPane;

	private Button runSimulationButton;
	private Button stopSimulationButton;

	private float automaticSimulationDelayValue = 0;
	private int simulationStepsValue = 10;

	private Thread automaticSimulationThread;

	public SimulationGUI(PetriNetVisualizationViewer petriNetVisualizationViewer, MainView mainView) {
		this.petriNetVisualizationViewer = petriNetVisualizationViewer;
		this.mainView = mainView;
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


		simulationDelayTextField = new TextField();
		simulationDelayTextField.setPromptText(Float.toString(automaticSimulationDelayValue));

		simulationDelayTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (NumberUtils.isNumber(newValue)) {
				try {
					automaticSimulationDelayValue = Float.parseFloat(newValue);
				} catch (Exception e) {
					simulationDelayTextField.setText(oldValue);
				}
			} else {
				simulationDelayTextField.setText(oldValue);
			}
		});

		simulationStepsTextField = new TextField();
		simulationStepsTextField.setPromptText(Integer.toString(simulationStepsValue));

		simulationStepsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (StringUtils.isNumeric(newValue)) {
				try {
					simulationStepsValue = Integer.parseInt(newValue);
				} catch (Exception e) {
					simulationStepsTextField.setText(oldValue);
				}
			} else {
				simulationStepsTextField.setText(oldValue);
			}
		});


		Text delayText = new Text("Delay [s]:");
		HBox delayPane = new HBox();
		delayPane.getChildren().addAll(delayText, simulationDelayTextField);
		delayPane.setSpacing(10);

		Text simulationStepsText = new Text("Steps: ");
		HBox stepsPane = new HBox();
		stepsPane.getChildren().addAll(simulationStepsText, simulationStepsTextField);
		stepsPane.setSpacing(10);

		isSimulationAutomaticRadioButton = new RadioButton("Automatic");
		isSimulationAutomaticRadioButton.setSelected(false);

		isSimulationAutomaticRadioButton.setOnAction(event1 -> {
			if (isSimulationAutomaticRadioButton.isSelected()) {
				petriSimulationMenu.getChildren().addAll(delayPane, stepsPane);
			} else {
				petriSimulationMenu.getChildren().removeAll(delayPane, stepsPane);
			}

			petriSimulationMenu.requestLayout();
		});

		runSimulationButton = new Button("Start");
		runSimulationButton.setOnAction(event -> onRunSimulation());

		stopSimulationButton = new Button("Stop");
		stopSimulationButton.setOnAction(event -> {
			endSimulation();

			if (!isSimulationAutomaticRadioButton.isSelected()) {
				clearPossibleTransitionsPane();
			}
		});

		stopSimulationButton.setDisable(true);

		petriSimulationMenu.getChildren().addAll(headerText, isSimulationAutomaticRadioButton, runSimulationButton, stopSimulationButton, separator);
	}


	private void onRunSimulation() {
		Console.clearConsole();

		PetriGraph petriGraph = petriNetVisualizationViewer.getPetriGraph();

		petriGraph.validateGraph();
		if (!petriGraph.isGraphIsValid()) {
			Console.writeGraphValidationResult(petriGraph);
			return;
		}


		if (isSimulationAutomaticRadioButton.isSelected()) {
			runAutomateDefaultSimulation();
		} else {
			runManualDefaultSimulation();
		}
	}

	private void runAutomateDefaultSimulation() {
		PetriGraph petriGraph = petriNetVisualizationViewer.getPetriGraph();
		petriGraph.compute();

		runSimulationButton.setDisable(true);
		stopSimulationButton.setDisable(false);

		BasicSimulation simulation = PetriNetSimulationFactory.createSimulation(petriGraph);

		petriNetVisualizationViewer.enterSimulationMode();

		Thread currentThread = Thread.currentThread();

		automaticSimulationThread = new Thread(() -> {
			List<Integer> transitions;
			Random r = new Random();
			int transition;
			for (int i = 0; i < simulationStepsValue; i++) {
				if (simulation.isSimulationEnded()) {
					break;
				}

				transitions = simulation.getPossibleTransitions();
				transition = transitions.get(r.nextInt(transitions.size()));

				petriNetVisualizationViewer.setHighlightedTransitions(transitions);

				try {
					if (automaticSimulationDelayValue > 0) {
						Thread.sleep((int) (automaticSimulationDelayValue * 1000));
					}
				} catch (InterruptedException e) {
				}

//				petriNetVisualizationViewer.setHighlightedTransition(transition);
//
//				try {
//					if (automaticSimulationDelayValue > 0) {
//						Thread.sleep((int) (automaticSimulationDelayValue * 1000));
//					}
//				} catch (InterruptedException e) {
//				}

				simulation.stepSimulate(transition);

				String consoleMessage = "Transition " + transition + ": ";
				consoleMessage += Arrays.toString(petriGraph.getCurrentState());
				System.out.println(consoleMessage);
				Console.writeOnConsole(consoleMessage);
			}


			Platform.runLater(() -> {
				endSimulation();
			});
		});


		automaticSimulationThread.start();
	}

	private void runManualDefaultSimulation() {
		PetriGraph petriGraph = petriNetVisualizationViewer.getPetriGraph();
		petriGraph.compute();
		runSimulationButton.setDisable(true);
		stopSimulationButton.setDisable(false);
		availableTransitionsPane = new FlowPane();
		availableTransitionsPane.setMaxWidth(180);
		simulationPane.getChildren().add(availableTransitionsPane);

		System.out.println(petriGraph);

		BasicSimulation simulation = PetriNetSimulationFactory.createSimulation(petriGraph);

		petriNetVisualizationViewer.enterSimulationMode();

		List<Integer> availableTransitions;
		availableTransitions = simulation.getPossibleTransitions();

		petriNetVisualizationViewer.setHighlightedTransitions(availableTransitions);
		prepareForNextManualSimulationStep(availableTransitions, simulation, petriGraph);
	}

	private void prepareForNextManualSimulationStep(List<Integer> transitions, BasicSimulation simulation, PetriGraph petriGraph) {
		if (simulation.isSimulationEnded()) {
			endSimulation();
			clearPossibleTransitionsPane();
			return;
		}

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

				prepareForNextManualSimulationStep(availableTransitions, simulation, petriGraph);
			});
		}
	}


	private void endSimulation() {
		runSimulationButton.setDisable(false);
		stopSimulationButton.setDisable(true);
		petriNetVisualizationViewer.exitSimulationMode();
		Console.writeOnConsole("Simulation is ended");
	}

	private void clearPossibleTransitionsPane() {
		availableTransitionsPane.getChildren().clear();
		simulationPane.getChildren().remove(availableTransitionsPane);
	}

}
