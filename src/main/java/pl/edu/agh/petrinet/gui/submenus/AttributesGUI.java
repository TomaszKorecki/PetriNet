package pl.edu.agh.petrinet.gui.submenus;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.gui.Console;
import pl.edu.agh.petrinet.gui.MainView;
import pl.edu.agh.petrinet.gui.ReachabilityGraphWindow;
import pl.edu.agh.petrinet.gui.customPlugins.ComputeTask;
import pl.edu.agh.petrinet.gui.visualizationViewers.PetriNetVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tomasz on 5/17/2015.
 */
public class AttributesGUI {
	private MainView mainView;
	private Pane attributesPane;

	private PetriNetVisualizationViewer petriNetVisualizationViewer;


	public AttributesGUI(PetriNetVisualizationViewer petriNetVisualizationViewer, MainView mainView) {
		this.petriNetVisualizationViewer = petriNetVisualizationViewer;
		this.mainView = mainView;
		createAttributesMenu();
	}

	public Pane getAttributesPane() {
		return attributesPane;
	}

	private void createAttributesMenu() {
		VBox attributesVPane = new VBox(5);
		attributesPane = attributesVPane;
		attributesPane.setMinWidth(150);
		attributesVPane.setAlignment(Pos.TOP_RIGHT);
		attributesVPane.setPadding(new Insets(10, 10, 10, 10));

		Text headerText = new Text("Attributes");
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		separator.setMinHeight(2);

		Button refreshAttributesButton = new Button("Show attributes");
		refreshAttributesButton.setOnAction(event -> {
			PetriGraph petriGraph = petriNetVisualizationViewer.getPetriGraph();
			petriGraph.validateGraph();
			if (!petriGraph.isGraphIsValid()) {
				Console.writeGraphValidationResult(petriGraph);
			} else {
                boolean done = true;
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> future = executor.submit(new ComputeTask(petriGraph));
                try{
                    future.get(30, TimeUnit.SECONDS);
                }catch (Exception e){
                    done = false;
                }
                executor.shutdownNow();

				Console.clearConsole();
                if(done){
				//Console.writeOnConsole(calculatedAttributes.oneColumnString());
				new AttributesWindow(petriNetVisualizationViewer.getPetriGraph()).show();
                }
                else{
                    Console.writeOnConsole("Graph calculating was too long and was stopped...");
                }
			}
		});

		Button reachabilityGraphButton = new Button("Reachability/Coverability graph");
		reachabilityGraphButton.setOnAction(event -> {
			PetriGraph petriGraph = petriNetVisualizationViewer.getPetriGraph();
			petriGraph.validateGraph();
			if (!petriGraph.isGraphIsValid()) {
				Console.writeGraphValidationResult(petriGraph);
			} else {

                boolean done = true;
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> future = executor.submit(new ComputeTask(petriGraph));
                try{
                    future.get(30, TimeUnit.SECONDS);
                }catch (Exception e){
                    done = false;
                }
                executor.shutdownNow();

                Console.clearConsole();
                if(done){
                    ReachabilityGraphWindow window = new ReachabilityGraphWindow(petriGraph);
                    window.show();
                }
                else{
                    Console.writeOnConsole("Graph calculating was too long and was stopped...");
                }
			}
		});


		attributesVPane.getChildren().addAll(headerText, refreshAttributesButton, reachabilityGraphButton, separator);
	}
}