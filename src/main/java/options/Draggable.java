package options;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import net.PetriNet;
import netElements.Place;
import netElements.Transition;
import stageActs.stageActs;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for dragging the elements
 * */
public class Draggable {
    int isLabel = 0;

    private double placeX;
    private double placeY;

    private Point2D coords;

    Place place;
    Transition transition;

    private final List<Double> intoX = new ArrayList<>();
    private final List<Double> intoY = new ArrayList<>();
    private final List<Double> fromX = new ArrayList<>();
    private final List<Double> fromY = new ArrayList<>();

    private final ChangeActivation changeActivation = new ChangeActivation();

    public void setDraggable(Node shape, stageActs stageActs, PetriNet petriNet) {
        shape.setOnMousePressed(mouseEvent -> {
            isLabel = 0;
            Bounds bounds = shape.getBoundsInLocal();
            coords = shape.localToScene(bounds.getCenterX(), bounds.getCenterY());
            intoX.clear();
            intoY.clear();
            fromX.clear();
            fromY.clear();
            placeX = mouseEvent.getSceneX() - shape.getTranslateX();
            placeY = mouseEvent.getSceneY() - shape.getTranslateY();
            if (shape.getClass() == Label.class) {
                for (Place place : petriNet.placeList) {
                    if (place.label == shape || place.marks == shape) {
                        this.place = place;
                        isLabel = 1;
                        break;
                    }
                }
                for (Transition transition : petriNet.transitionList) {
                    if (transition.label == shape) {
                        this.transition = transition;
                        isLabel = 2;
                        break;
                    }
                }
            }
            if (shape.getClass() == Place.class || isLabel == 1) {
                if (isLabel != 1) {
                    place = (Place) shape;
                }
                for (Line line : place.childrenLine) {
                    fromX.add(mouseEvent.getSceneX() - line.getStartX());
                    fromY.add(mouseEvent.getSceneY() - line.getStartY());
                }
                for (Line line : place.parentLine) {
                    intoX.add(mouseEvent.getSceneX() - line.getEndX());
                    intoY.add(mouseEvent.getSceneY() - line.getEndY());
                }
            } else {
                if (shape.getClass() == Transition.class) {
                    transition = (Transition) shape;
                }
                for (Line line : transition.childrenLine) {
                    fromX.add(mouseEvent.getSceneX() - line.getStartX());
                    fromY.add(mouseEvent.getSceneY() - line.getStartY());
                }
                for (Line line : transition.parentLine) {
                    intoX.add(mouseEvent.getSceneX() - line.getEndX());
                    intoY.add(mouseEvent.getSceneY() - line.getEndY());
                }
            }
        });

        shape.setOnMouseDragged(mouseEvent -> {
            if (isLabel == 1) {
                place.setTranslateX(mouseEvent.getSceneX() - placeX - 800);
                place.setTranslateY(mouseEvent.getSceneY() - placeY - 350);
            } else {
                if (isLabel == 2) {
                    transition.setTranslateX(mouseEvent.getSceneX() - placeX - 800);
                    transition.setTranslateY(mouseEvent.getSceneY() - placeY - 400);
                } else {
                    shape.setTranslateX(mouseEvent.getSceneX() - placeX);
                    shape.setTranslateY(mouseEvent.getSceneY() - placeY);
                }
            }
            if (shape.getClass() == Place.class || isLabel == 1) {
                if (isLabel != 1) {
                    place = (Place) shape;
                    place.marks.setTranslateX(place.getTranslateX() + placeX);
                    place.marks.setTranslateY(place.getTranslateY() + placeY + 15);
                    place.textArea.setTranslateX(place.getTranslateX() + placeX + 20);
                    place.textArea.setTranslateY(place.getTranslateY() + placeY - 20);
                    place.label.setTranslateX(place.getTranslateX() + placeX + 20);
                    place.label.setTranslateY(place.getTranslateY() + placeY - 20);
                } else {
                    place.marks.setTranslateX(place.getTranslateX() + placeX + 800);
                    place.marks.setTranslateY(place.getTranslateY() + placeY + 350);
                    place.textArea.setTranslateX(place.getTranslateX() + placeX + 800);
                    place.textArea.setTranslateY(place.getTranslateY() + placeY + 350);
                    place.label.setTranslateX(place.getTranslateX() + placeX + 800);
                    place.label.setTranslateY(place.getTranslateY() + placeY + 350);
                }
                for (int i = 0; i < place.childrenLine.size(); ++i) {
                    place.childrenLine.get(i).setStartX(mouseEvent.getSceneX() - fromX.get(i));
                    place.childrenLine.get(i).setStartY(mouseEvent.getSceneY() - fromY.get(i));
                    place.childrenLine.get(i).setArrows();
                }
                for (int i = 0; i < place.parentLine.size(); ++i) {
                    place.parentLine.get(i).setEndX(mouseEvent.getSceneX() - intoX.get(i));
                    place.parentLine.get(i).setEndY(mouseEvent.getSceneY() - intoY.get(i));
                    place.parentLine.get(i).setArrows();
                }
            } else {
                if (isLabel != 2) {
                    transition = (Transition) shape;
                    transition.textArea.setTranslateX(transition.getTranslateX() + placeX + 20);
                    transition.textArea.setTranslateY(transition.getTranslateY() + placeY - 20);
                    transition.label.setTranslateX(transition.getTranslateX() + placeX + 20);
                    transition.label.setTranslateY(transition.getTranslateY() + placeY - 20);
                } else {
                    transition.textArea.setTranslateX(transition.getTranslateX() + placeX + 800);
                    transition.textArea.setTranslateY(transition.getTranslateY() + placeY + 400);
                    transition.label.setTranslateX(transition.getTranslateX() + placeX + 800);
                    transition.label.setTranslateY(transition.getTranslateY() + placeY + 400);
                }
                for (int i = 0; i < transition.childrenLine.size(); ++i) {
                    transition.childrenLine.get(i).setStartX(mouseEvent.getSceneX() - fromX.get(i));
                    transition.childrenLine.get(i).setStartY(mouseEvent.getSceneY() - fromY.get(i));
                    transition.childrenLine.get(i).setArrows();
                }
                for (int i = 0; i < transition.parentLine.size(); ++i) {
                    transition.parentLine.get(i).setEndX(mouseEvent.getSceneX() - intoX.get(i));
                    transition.parentLine.get(i).setEndY(mouseEvent.getSceneY() - intoY.get(i));
                    transition.parentLine.get(i).setArrows();
                }
            }
        });

        shape.setOnMouseReleased(mouseEvent -> {
            if (shape.getClass() == Place.class || shape.getClass() == Transition.class) {
                Bounds bounds = shape.getBoundsInLocal();
                Point2D newCoords = shape.localToScene(bounds.getCenterX(), bounds.getCenterY());
                if (Math.abs(newCoords.getX() - coords.getX()) > 5 || Math.abs(newCoords.getY() - coords.getY()) > 5) {
                    changeActivation.changeActivation((Shape) shape, stageActs.node);
                }
            }
        });
    }
}
