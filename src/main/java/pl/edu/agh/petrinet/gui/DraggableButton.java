package pl.edu.agh.petrinet.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.vecmath.Vector2d;


/**
 * Created by Tomasz on 4/19/2015.
 * Extended  button class with ImageView which can be dragged
 */
public class DraggableButton extends Button {
    private  final Vector2d dragDelta;
    private ActionEvent onMousePressed;
    private Pane draggablePane;
    private ImageView draggableElement;

    /*
    Draggable Pane - root pane for dragging canvas
     */
    public DraggableButton(Pane draggablePane){
        this.draggablePane = draggablePane;
        dragDelta = new Vector2d();

        setOnMousePressed(event -> OnDraggableElementMousePressed(event));
        setOnMouseReleased(event -> OnDraggableElementMouseReleased(event));
        setOnMouseDragged(event -> OnDraggableElementMouseDragged(event));
    }

    private void OnDraggableElementMousePressed(MouseEvent mouseEvent){
        System.out.println("Pressed start");
        setMouseTransparent(true);
        this.draggableElement = new ImageView(((ImageView)getGraphic()).getImage());
        draggablePane.getChildren().addAll(draggableElement);
        draggableElement.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
        draggableElement.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
    }

    private void OnDraggableElementMouseDragged(MouseEvent mouseEvent){
        draggableElement.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
        draggableElement.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
    }

    private void OnDraggableElementMouseReleased(MouseEvent mouseEvent){
        System.out.println("Released");
        setMouseTransparent(false);
        draggablePane.getChildren().remove(draggableElement);
        draggableElement = null;
    }
}
