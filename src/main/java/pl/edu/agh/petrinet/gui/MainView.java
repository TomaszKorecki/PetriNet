package pl.edu.agh.petrinet.gui;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pl.edu.agh.petrinet.model.PetriEdge;
import pl.edu.agh.petrinet.model.PetriGraph;
import pl.edu.agh.petrinet.model.PetriVertex;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class MainView extends Application {

    private final Image PLACE_ICON_IMAGE = new Image(getClass().getResourceAsStream("placeIcon.png"));
    private final Image TRANSITION_ICON_IMAGE =new Image(getClass().getResourceAsStream("transitionIcon.png"));

    private Stage primaryStage;
    private BorderPane rootPane;
    private GridPane leftPane;
    private StackPane topPane;
    private StackPane centerPane;
    private SwingNode swingNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createMenuStructure(primaryStage);
    }

    /*
    Creates basic menu structure
     */
    private void createMenuStructure(Stage primaryStage){
        rootPane = new BorderPane();
        //createLeftPane();
        createCenterPane();
        primaryStage.setTitle("Petri Net Editor");
        primaryStage.setScene(new Scene(rootPane, 900, 640));
        primaryStage.show();
    }


    /*
    Creates left pane with draggable Place and Transition buttons
     */
    private void createLeftPane(){
        leftPane = new GridPane();
        rootPane.setLeft(leftPane);

        DraggableButton placeIcon = new DraggableButton(rootPane);
        placeIcon.setGraphic(new ImageView(PLACE_ICON_IMAGE));
        DraggableButton transitionIcon = new DraggableButton(rootPane);
        transitionIcon.setGraphic(new ImageView(TRANSITION_ICON_IMAGE));

        leftPane.setAlignment(Pos.TOP_CENTER);
        leftPane.setVgap(10);
        leftPane.add(placeIcon, 1, 1);
        leftPane.add(transitionIcon, 1, 2);
    }

    /*
    Creates pane for JUNG's Graph
     */
    private void createCenterPane() {
        centerPane = new StackPane();

        PetriGraph petriGraph = new PetriGraph();
        VisualizationViewer<PetriVertex, PetriEdge> visualizationViewer = new VisualizationViewer<>(new ISOMLayout<>(petriGraph.getGraph()));
        visualizationViewer.setBorder(new TitledBorder("Graf testowy"));
        DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<>();
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());

        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizationViewer.setGraphMouse(gm);
        visualizationViewer.setPreferredSize(new Dimension(600, 400));

        swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> swingNode.setContent(visualizationViewer));

        centerPane.getChildren().add(swingNode);
        rootPane.setCenter(centerPane);
    }
}
