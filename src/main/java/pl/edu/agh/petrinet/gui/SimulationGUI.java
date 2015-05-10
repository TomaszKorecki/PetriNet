package pl.edu.agh.petrinet.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.agh.petrinet.model.PetriGraph;


public class SimulationGUI {

    private TextField simulationDelayTextField;
    private RadioButton isSimulationAutomaticRadioButton;
    private Pane simulationPane;

    private PetriGraph petriGraph;
    private PetriGraph.Type simulationType = PetriGraph.Type.DEFAULT;

    public SimulationGUI(PetriGraph petriGraph){
        this.petriGraph = petriGraph;
        createSimulationMenu();
    }

    public Pane getSimulationPane(){
        return simulationPane;
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

        simulationDelayTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    simulationDelayTextField.setText(oldValue);
                }
            }
        });



        HBox delayPane = new HBox();
        delayPane.getChildren().addAll(delayText, simulationDelayTextField);
        delayPane.setSpacing(10);

        isSimulationAutomaticRadioButton = new RadioButton("Automatic");

        Button runSimulationButton = new Button("Start");
        runSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        Button oneSimulationStepButton = new Button("One Step");
        oneSimulationStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        petriSimulationMenu.getChildren().addAll(headerText, delayPane, isSimulationAutomaticRadioButton,runSimulationButton, oneSimulationStepButton, separator);
    }


}
