package options;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import netElements.Place;
import netElements.Transition;

import java.util.List;

public class ChangeActivation {
    public void changeActivation(Shape shape, List<Shape> node) {
        if (node.contains(shape)) {
            if (shape.getClass() == Transition.class) {
                shape.setFill(((Transition)shape).exColor);
            } else {
                shape.setFill(((Place)shape).exColor);
            }
            node.remove(shape);
        } else {
            shape.setFill(Color.LIGHTGREY);
            node.add(shape);
        }
    }
}
