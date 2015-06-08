package pl.edu.agh.petrinet.gui.submenus;

import com.google.common.primitives.Ints;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.embed.swing.SwingNode;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.algorithms.Attributes;
import pl.edu.agh.petrinet.algorithms.IncidenceMatrix;
import pl.edu.agh.petrinet.gui.visualizationViewers.ReachabilityGraphVisualizationViewer;
import pl.edu.agh.petrinet.model.PetriGraph;

import javax.swing.text.Element;
import javax.swing.text.TableView;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomasz on 6/7/2015.
 */
public class AttributesWindow {

	private Stage primaryStage;
	private PetriGraph petriGraph;
	private BorderPane rootPane;
	private HBox attributesPane;

	Attributes calculatedAttributes;

	public AttributesWindow(PetriGraph petriGraph) {
		this.petriGraph = petriGraph;
		rootPane = new BorderPane();
		attributesPane = new HBox(5);


		rootPane.setCenter(attributesPane);

		calculatedAttributes = new Attributes(petriGraph);

		createNetAtrributesPane();
		createLimitationPane();
		createLivenessPane();
		createIncidenceMatrix();
		createWeightVectorPane();

		primaryStage = new Stage();
		primaryStage.setTitle("Attributes");
		primaryStage.setScene(new Scene(rootPane, 700, 500));
	}

	private void createNetAtrributesPane(){

		Text netLimitationText = new Text("Net limitation:");
		Text isNetSafeText = new Text("Net safe:");
		Text isNetConservativeText = new Text("Net conservative:");
		Text isNetReversible = new Text("Net reversible:");
		Text isNetLive = new Text("Net live:");

		VBox netAttributesPane = new VBox(3);
		netAttributesPane.getChildren().add(new Text("Attributes:"));

		netAttributesPane.getChildren().addAll(netLimitationText, isNetSafeText, isNetConservativeText, isNetReversible, isNetLive);
		netAttributesPane.setFillWidth(true);
		netAttributesPane.setAlignment(Pos.TOP_LEFT);
		netAttributesPane.setPadding(new Insets(10, 10, 10, 10));

		Text netLimitationValue = new Text(calculatedAttributes.netLimitationString());
		Text netSafeValue = new Text(calculatedAttributes.isNetSafe() ? "Yes" : "No");
		Text netConservativeValue = new Text(calculatedAttributes.isNetConservative() ? "Yes" : "No");
		Text netReversibleValue = new Text(calculatedAttributes.isNetReversible() ? "Yes" : "No");
		Text netLiveValue = new Text(calculatedAttributes.isNetLive() ? "Yes" : "No");

		VBox netAttributesValuesPane = new VBox(3);
		netAttributesValuesPane.getChildren().add(new Text(" "));

		netAttributesValuesPane.getChildren().addAll(netLimitationValue, netSafeValue, netConservativeValue, netReversibleValue, netLiveValue);
		netAttributesValuesPane.setFillWidth(true);
		netAttributesValuesPane.setAlignment(Pos.TOP_RIGHT);
		netAttributesValuesPane.setPadding(new Insets(10, 10, 10, 10));

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setMaxWidth(2);

		attributesPane.getChildren().addAll(netAttributesPane, netAttributesValuesPane, separator);
	}

	private void createLimitationPane(){

		VBox limitationsTexts = new VBox(3);
		VBox limitationsValues = new VBox(3);

		limitationsTexts.getChildren().add(new Text("Limitations:"));
		limitationsValues.getChildren().add(new Text(" "));

		Map<Integer, Integer> placesLimitation = calculatedAttributes.getPlacesLimitation();

		placesLimitation.forEach((id, value) -> {

			Text placeIDText = new Text("P[" + id.toString() + "]: ");
			limitationsTexts.getChildren().add(placeIDText);

			Text placeLimitationValue = new Text(value == Integer.MAX_VALUE ? new Character('\u221e').toString() : value.toString());
			limitationsValues.getChildren().add(placeLimitationValue);
		});

		limitationsTexts.setFillWidth(true);
		limitationsTexts.setAlignment(Pos.TOP_LEFT);
		limitationsTexts.setPadding(new Insets(10, 10, 10, 10));

		limitationsValues.setFillWidth(true);
		limitationsValues.setAlignment(Pos.TOP_RIGHT);
		limitationsValues.setPadding(new Insets(10, 10, 10, 10));

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setMaxWidth(2);

		attributesPane.getChildren().addAll(limitationsTexts, limitationsValues, separator);
	}

	private void createLivenessPane(){

		VBox livenessTexts = new VBox(3);
		VBox livenessValues = new VBox(3);

		livenessTexts.getChildren().add(new Text("Liveness:"));
		livenessValues.getChildren().add(new Text(" "));

		Map<Integer, Boolean> placesLiveness = calculatedAttributes.getPlacesLiveness();

		placesLiveness.forEach((id, value) -> {

			Text placeIDText = new Text("P[" + id.toString() + "]: ");
			livenessTexts.getChildren().add(placeIDText);

			Text placeLimitationValue = new Text(value ? "Yes" : "No");
			livenessValues.getChildren().add(placeLimitationValue);
		});

		livenessTexts.getChildren().add(new Text(" "));
		livenessValues.getChildren().add(new Text(" "));

		Map<Integer, Boolean> transitionLiveness = calculatedAttributes.getPlacesLiveness();

		transitionLiveness.forEach((id, value) -> {

			Text transitionIDText = new Text("T[" + id.toString() + "]: ");
			livenessTexts.getChildren().add(transitionIDText);

			Text transitionLivenessValue = new Text(value ? "Yes" : "No");
			livenessValues.getChildren().add(transitionLivenessValue);
		});

		livenessTexts.setFillWidth(true);
		livenessTexts.setAlignment(Pos.TOP_LEFT);
		livenessTexts.setPadding(new Insets(10, 10, 10, 10));

		livenessValues.setFillWidth(true);
		livenessValues.setAlignment(Pos.TOP_RIGHT);
		livenessValues.setPadding(new Insets(10, 10, 10, 10));

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setMaxWidth(2);

		attributesPane.getChildren().addAll(livenessTexts, livenessValues, separator);
	}

	private void createIncidenceMatrix(){

		VBox incidenceMatrixPane = new VBox(3);
		incidenceMatrixPane.setPadding(new Insets(10, 10, 10, 10));
		incidenceMatrixPane.getChildren().add(new Text("Incidence matrix"));


		IncidenceMatrix incidenceMatrix = petriGraph.getIncidenceMatrix();
		int[][] matrix = incidenceMatrix.getIncidenceMatrix();

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(12);
		grid.setVgap(7);
		//grid.setGridLinesVisible(true);


		for(int i = 0; i < matrix.length; i++){
			for(int k = 0; k < matrix[0].length; k++){

				Text text = new Text(Integer.toString(matrix[i][k]));
				text.setTextAlignment(TextAlignment.RIGHT);

				grid.add(text, k, i);
				GridPane.setHalignment(text, HPos.RIGHT);
			}
		}

		incidenceMatrixPane.getChildren().addAll(grid);

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setMaxWidth(2);

		attributesPane.getChildren().addAll(incidenceMatrixPane, separator);
	}


	private void createWeightVectorPane(){

		VBox weightVectorPane = new VBox(3);
		weightVectorPane.setPadding(new Insets(10, 10, 10, 10));
		weightVectorPane.getChildren().add(new Text("Conservative by vector:"));

		weightVectorPane.getChildren().addAll(new Text("Vector (coma separated)"));

		TextField vectorInput = new TextField();
		vectorInput.setPromptText("1,2,3");

		Button checkButton = new Button("Check");

		Text outputText = new Text();

		checkButton.setOnAction(event -> {
			String [] items = vectorInput.getText().split(",");

			if(items.length != petriGraph.getPlacesCount()){
				outputText.setText("Vector length should have length of " + petriGraph.getPlacesCount());
				return;
			}

			List<String> strings = Arrays.asList(items);
			List<Integer> ints = new LinkedList<Integer>();

			try{
				for(String s : strings){
					ints.add(Integer.parseInt(s));
				}
			} catch (NumberFormatException exception) {
				outputText.setText("Input string has got wrong format");
				return;
			}

			outputText.setText((calculatedAttributes.isNetConservative(Ints.toArray(ints)) ? "Net is conservative" : "Net is not conservative") + " by given vector");
			
		});

		weightVectorPane.getChildren().addAll(vectorInput, checkButton, outputText);
		attributesPane.getChildren().add(weightVectorPane);
	}


	public void show() {
		primaryStage.show();
	}

	public void hide() {
		primaryStage.close();
	}

}
