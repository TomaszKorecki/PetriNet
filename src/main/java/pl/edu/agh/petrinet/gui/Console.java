package pl.edu.agh.petrinet.gui;


import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class Console {
	private static Console consoleInstance;

	private ScrollPane consoleScrollPane;
	private TextArea consoleTextArea;

	protected Console(){
		createConsole();
	}

	public static Console getConsoleInstance(){
		if(consoleInstance == null){
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
		consoleInstance.consoleTextArea.appendText("\n" + consoleMessage);
	}

	public static void clearConsole() {
		consoleInstance.consoleTextArea.clear();
	}



}
