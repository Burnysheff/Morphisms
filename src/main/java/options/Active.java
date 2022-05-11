package options;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Shape;
import netElements.Place;
import stageActs.*;

import java.util.List;

public class Active {
    public void changeActive(Shape shape, ChangeActivation changeActivation, List<Shape> node, stageActs acts) {
        shape.setOnMouseClicked(mouseEvent -> {
            changeActivation.changeActivation(shape, node);
            acts.buildConnection(false);
        });
    }
}
