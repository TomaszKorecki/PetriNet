package pl.edu.agh.petrinet.gui;


import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import pl.edu.agh.petrinet.model.PetriGraph;

public class Console {
	private static Console consoleInstance;

	private ScrollPane consoleScrollPane;
	private TextArea consoleTextArea;

	protected Console() {
		createConsole();
	}

	public static Console getConsoleInstance() {
		if (consoleInstance == null) {
			consoleInstance = new Console();
		}

		return consoleInstance;
	}

	private void createConsole() {
		consoleTextArea = new TextArea();
		consoleTextArea.setEditable(false);

		consoleScrollPane = new ScrollPane();
		consoleScrollPane.setContent(consoleTextArea);
		consoleScrollPane.setFitToWidth(true);
		consoleScrollPane.setPrefHeight(180);
	}

	public ScrollPane getConsole() {
		return consoleScrollPane;
	}

	public static void writeOnConsole(String consoleMessage) {
		Platform.runLater(() -> {
			consoleInstance.consoleTextArea.appendText(consoleMessage + "\n");
		});
	}

	public static void clearConsole() {
		consoleInstance.consoleTextArea.clear();
	}

	public static void writeGraphValidationResult(PetriGraph petriGraph) {
		Platform.runLater(() -> {
			petriGraph.validateGraph();
			clearConsole();
			if (!petriGraph.isGraphIsValid()) {
				writeOnConsole("Graph is invalid:");
				petriGraph.getValidationResults().forEach(s -> writeOnConsole(s));
			} else {
				System.out.println("Graph is valid");
				writeOnConsole("Graph is valid:");
				writeOnConsole(petriGraph.toString());
			}
		});
	}


}
