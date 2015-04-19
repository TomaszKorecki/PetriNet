package pl.edu.agh.petrinet.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class MainView extends Application {

    private final Image PLACE_ICON_IMAGE = new Image(getClass().getResourceAsStream("placeIcon.png"));
    private final Image TRANSITION_ICON_IMAGE =new Image(getClass().getResourceAsStream("transitionIcon.png"));

    private Stage primaryStage;
    private BorderPane rootPane;
    private GridPane leftPane;
    private StackPane topPane;
    private StackPane centerPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createMenuStructure(primaryStage);
    }

    private void createMenuStructure(Stage primaryStage){
        rootPane = new BorderPane();
        createLeftMenu();
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(rootPane, 600, 320));
        primaryStage.show();
    }

    private void createLeftMenu(){
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

}
