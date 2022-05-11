package stageActs;

import checker.MorphismChecker;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.PetriNet;
import netElements.Arc;
import netElements.Place;
import netElements.Transition;
import options.Active;
import options.ChangeActivation;
import options.Draggable;
import pnmlParser.Parser;

import java.io.File;
import java.util.*;

public class stageActs extends Group {

    private final Parser parser = new Parser();

    private final Stage stage;

    private final FileChooser fileChooser = new FileChooser();

    private final Random random = new Random();

    private final ChangeActivation changeActivation = new ChangeActivation();

    public final List<Shape> node = new ArrayList<>();

    public PetriNet petriNetFirst = new PetriNet();
    public PetriNet petriNetSecond = new PetriNet();
    private MorphismChecker morphismChecker;

    private final Active active = new Active();

    private final Draggable setDraggable = new Draggable();

    private Button connection;

    private Button place;

    private Button transition;

    private Button delete;

    private Button goFurther;

    private Button names;

    private Label res;

    public stageActs(Stage stage) {
        this.stage = stage;
    }

    public void entranceWindow() {
        this.getChildren().clear();

        Button writeFirst = new Button();
        writeFirst.setText("Create first Petri Net");
        writeFirst.setTranslateX(310);
        writeFirst.setTranslateY(350);
        this.getChildren().add(writeFirst);

        Button writeSecond = new Button();
        writeSecond.setText("Create second Petri Net");
        writeSecond.setTranslateX(560);
        writeSecond.setTranslateY(350);
        this.getChildren().add(writeSecond);

        goFurther = new Button();
        goFurther.setText("Nets Created");
        goFurther.setTranslateX(440);
        goFurther.setTranslateY(420);
        checkFurtherPossible();
        this.getChildren().add(goFurther);

        Button swapNets = new Button();
        swapNets.setText("Swap nets");
        swapNets.setTranslateX(450);
        swapNets.setTranslateY(270);
        this.getChildren().add(swapNets);

        writeFirst.setOnAction(actionEvent -> this.createWindow(this.petriNetFirst));
        writeSecond.setOnAction(actionEvent -> this.createWindow(this.petriNetSecond));
        goFurther.setOnAction(actionEvent -> this.makeMorphisms());
        swapNets.setOnAction(actionEvent -> this.swapNets());
    }

    private void createWindow(PetriNet petriNet) {
        int centralizing;

        if (petriNet == petriNetFirst) {
            centralizing = 250;
        } else {
            centralizing = -250;
        }

        this.getChildren().clear();

        petriNet.scaling = 1;

        for (Place place : petriNet.placeList) {
            this.getChildren().add(place);
            place.setScaleX(1);
            place.setScaleY(1);
            this.getChildren().add(place.label);
            place.label.setScaleX(1);
            place.label.setScaleY(1);
            this.getChildren().add(place.marks);
            place.marks.setScaleX(1);
            place.marks.setScaleY(1);
            place.setCenterX(place.getCenterX() + centralizing);
            place.label.setTranslateX(place.label.getTranslateX() + centralizing);
            place.marks.setTranslateX(place.marks.getTranslateX() + centralizing);
        }
        for (Transition transition : petriNet.transitionList) {
            this.getChildren().add(transition);
            transition.setScaleX(1);
            transition.setScaleY(1);
            this.getChildren().add(transition.label);
            transition.label.setScaleX(1);
            transition.label.setScaleY(1);
            transition.setX(transition.getX() + centralizing);
            transition.label.setTranslateX(transition.label.getTranslateX() + centralizing);
        }

        this.rewriteLines(petriNet);

        place = new Button();
        place.setText("Create place");
        place.setTranslateX(830);
        place.setTranslateY(250);
        this.getChildren().add(place);

        transition = new Button();
        transition.setText("Create transition");
        transition.setTranslateX(830);
        transition.setTranslateY(290);
        this.getChildren().add(transition);

        connection = new Button();
        connection.setText("Create connection");
        connection.setTranslateX(830);
        connection.setTranslateY(330);
        this.getChildren().add(connection);
        this.checkConnectionPossible(petriNet);

        delete = new Button();
        delete.setText("Delete Elements");
        delete.setTranslateX(830);
        delete.setTranslateY(370);
        this.getChildren().add(delete);
        this.checkDeletePossible(petriNet);

        Button chooseFile = new Button();
        chooseFile.setText("download net");
        chooseFile.setTranslateX(830);
        chooseFile.setTranslateY(410);
        this.getChildren().add(chooseFile);

        Button startWindow = new Button();
        startWindow.setText("Petri Net created");
        startWindow.setTranslateX(830);
        startWindow.setTranslateY(450);
        this.getChildren().add(startWindow);

        names = new Button();
        names.setText("Give names to elements");
        names.setTranslateX(830);
        names.setTranslateY(490);
        this.getChildren().add(names);

        Button increaseMarking = new Button();
        increaseMarking.setText("Add initial markings");
        increaseMarking.setTranslateX(830);
        increaseMarking.setTranslateY(530);
        this.getChildren().add(increaseMarking);

        Button decreaseMarking = new Button();
        decreaseMarking.setText("Decrease initial markings");
        decreaseMarking.setTranslateX(830);
        decreaseMarking.setTranslateY(570);
        this.getChildren().add(decreaseMarking);

        Button increaseScaling = new Button();
        increaseScaling.setText("+");
        increaseScaling.setTranslateX(480);
        increaseScaling.setTranslateY(700);
        increaseScaling.setPrefSize(20, 20);
        this.getChildren().add(increaseScaling);

        Button decreaseScaling = new Button();
        decreaseScaling.setText("-");
        decreaseScaling.setTranslateX(510);
        decreaseScaling.setTranslateY(700);
        decreaseScaling.setPrefSize(20, 20);
        this.getChildren().add(decreaseScaling);

        Button deleteEverything = new Button();
        deleteEverything.setText("Clear the window");
        deleteEverything.setTranslateX(830);
        deleteEverything.setTranslateY(610);
        this.getChildren().add(deleteEverything);

        place.setOnAction(actionEvent -> this.createPlace(petriNet));
        transition.setOnAction(actionEvent -> this.createTransition(petriNet));
        connection.setOnAction(actionEvent -> this.createConnection());
        delete.setOnAction(actionEvent -> this.deleteElements(petriNet));
        chooseFile.setOnAction(actionEvent -> this.downloadFile(petriNet));
        startWindow.setOnAction(actionEvent -> this.startWindow());
        names.setOnAction(actionEvent -> this.giveNames());
        increaseMarking.setOnAction(actionEvent -> this.increaseMarking());
        decreaseMarking.setOnAction(actionEvent -> this.decreaseMarking());
        increaseScaling.setOnAction(actionEvent -> this.increaseScaling(petriNet));
        decreaseScaling.setOnAction(actionEvent -> this.decreaseScaling(petriNet));
        deleteEverything.setOnAction(actionEvent -> this.deleteEverything(petriNet));
    }

    private void swapNets() {
        PetriNet swapper = this.petriNetSecond;
        this.petriNetSecond = this.petriNetFirst;
        this.petriNetFirst = swapper;
    }

    private void deleteEverything(PetriNet petriNet) {
        this.getChildren().removeIf(node -> node.getClass() == Place.class || node.getClass() == Transition.class || node.getClass() == Arc.class || node.getClass() == Line.class);
        petriNet.placeList.clear();
        petriNet.transitionList.clear();
        petriNet.morphismsList.clear();
        connection.setText("Create connection");
        this.checkConnectionPossible(petriNet);
        this.checkDeletePossible(petriNet);
        this.node.clear();
    }

    public void increaseScaling(PetriNet petriNet) {
        if (petriNet.scaling < 2) {
            petriNet.scaling += 0.2;
        }
        this.getChildren().removeIf(node -> node.getClass() == Place.class || node.getClass() == Transition.class || node.getClass() == Line.class ||
                node.getClass() == Arc.class);
        Group group = getLittleGroup(petriNet, petriNet.scaling);;
        List<Node> nodes = new ArrayList<>(group.getChildren());
        for (Node node : nodes) {
            if (node.getClass() != Line.class && node.getClass() != Arc.class) {
                node.setScaleX(group.getScaleX());
                node.setScaleY(group.getScaleY());
                this.getChildren().add(node);
            }
        }
        rewriteLines(petriNet);
    }

    public void decreaseScaling(PetriNet petriNet) {
        if (petriNet.scaling > 0.4) {
            petriNet.scaling -= 0.2;
        }
        this.getChildren().removeIf(node -> node.getClass() == Place.class || node.getClass() == Transition.class || node.getClass() == Line.class ||
                node.getClass() == Arc.class);
        Group group = getLittleGroup(petriNet, petriNet.scaling);
        List<Node> nodes = new ArrayList<>(group.getChildren());
        for (Node node : nodes) {
            if (node.getClass() != Line.class && node.getClass() != Arc.class) {
                node.setScaleX(group.getScaleX());
                node.setScaleY(group.getScaleY());
                this.getChildren().add(node);
            }
        }
        rewriteLines(petriNet);
    }

    private void rewriteLines(PetriNet petriNet) {
        for (Place place : petriNet.placeList) {
            place.parentLine.clear();
            place.childrenLine.clear();
        }
        for (Transition transition : petriNet.transitionList) {
            transition.parentLine.clear();
            transition.childrenLine.clear();
        }
        for (Place place : petriNet.placeList) {
            for (Transition transition : place.childrenList) {
                Bounds bounds = place.getBoundsInLocal();
                Point2D coordinates = place.localToScene(bounds.getCenterX(), bounds.getCenterY());
                Arc line = new Arc();
                line.setStartX(coordinates.getX());
                line.setStartY(coordinates.getY());
                bounds = transition.getBoundsInLocal();
                coordinates = transition.localToScene(bounds.getCenterX(), bounds.getCenterY());
                line.setEndX(coordinates.getX());
                line.setEndY(coordinates.getY());
                line.setArrows();
                this.getChildren().remove(place);
                this.getChildren().remove(place.marks);
                this.getChildren().remove(place.label);
                this.getChildren().remove(transition);
                this.getChildren().remove(transition.label);
                this.getChildren().add(line);
                this.getChildren().add(line.firstArrow);
                this.getChildren().add(line.secondArrow);
                this.getChildren().add(place);
                this.getChildren().add(place.marks);
                this.getChildren().add(place.label);
                this.getChildren().add(transition);
                this.getChildren().add(transition.label);
                transition.parentLine.add(line);
                place.childrenLine.add(line);
            }
        }
        for (Transition transition : petriNet.transitionList) {
            for (Place place : transition.childrenList) {
                Bounds bounds = transition.getBoundsInLocal();
                Point2D coordinates = transition.localToScene(bounds.getCenterX(), bounds.getCenterY());
                Arc line = new Arc();
                line.setStartX(coordinates.getX());
                line.setStartY(coordinates.getY());
                bounds = place.getBoundsInLocal();
                coordinates = place.localToScene(bounds.getCenterX(), bounds.getCenterY());
                line.setEndX(coordinates.getX());
                line.setEndY(coordinates.getY());
                line.setArrows();
                this.getChildren().remove(place);
                this.getChildren().remove(place.marks);
                this.getChildren().remove(place.label);
                this.getChildren().remove(transition);
                this.getChildren().remove(transition.label);
                this.getChildren().add(line);
                this.getChildren().add(line.firstArrow);
                this.getChildren().add(line.secondArrow);
                this.getChildren().add(place);
                this.getChildren().add(place.marks);
                this.getChildren().add(place.label);
                this.getChildren().add(transition);
                this.getChildren().add(transition.label);
                place.parentLine.add(line);
                transition.childrenLine.add(line);
            }
        }
    }

    private void increaseMarking() {
        for (Shape shape : this.node) {
            if (shape instanceof Place) {
                ((Place) shape).marking = true;
                ((Place) shape).setLabel();
            }
        }
        this.unActivate();
    }

    private void decreaseMarking() {
        for (Shape shape : this.node) {
            if (shape instanceof Place) {
                ((Place) shape).marking = false;
                ((Place) shape).setLabel();
            }
        }
        this.unActivate();
    }

    private void giveNames() {
        if (Objects.equals(names.getText(), "Give names to elements")) {
            for (int i = 0; i < this.getChildren().size(); ++i) {
                if (this.getChildren().get(i).getClass() == Place.class) {
                    ((Place) this.getChildren().get(i)).textArea.setVisible(true);
                    this.getChildren().add(((Place) this.getChildren().get(i)).textArea);
                }
                if (this.getChildren().get(i).getClass() == Transition.class) {
                    ((Transition) this.getChildren().get(i)).textArea.setVisible(true);
                    this.getChildren().add(((Transition) this.getChildren().get(i)).textArea);
                }
            }
            names.setText("Names given");
        } else {
            for (int i = 0; i < this.getChildren().size(); ++i) {
                if (this.getChildren().get(i).getClass() == Place.class) {
                    ((Place) this.getChildren().get(i)).label.setText(((Place) this.getChildren().get(i)).textArea.getText());
                    this.getChildren().remove(((Place) this.getChildren().get(i)).textArea);
                }
                if (this.getChildren().get(i).getClass() == Transition.class) {
                    ((Transition) this.getChildren().get(i)).label.setText(((Transition) this.getChildren().get(i)).textArea.getText());
                    this.getChildren().remove(((Transition) this.getChildren().get(i)).textArea);
                }
            }
            names.setText("Give names to elements");
        }
    }

    private void downloadFile(PetriNet petriNet) {
        File file = fileChooser.showOpenDialog(this.stage);
        if (file == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        List<String> listTransitions = new ArrayList<>();
        Map<String, String> listArcs = new HashMap<>();
        boolean isNet = parser.Parse(file, map, listTransitions, listArcs);
        if (!isNet) {
            return;
        }
        this.formNet(petriNet, map, listTransitions, listArcs);
    }

    public void createPlace(PetriNet petriNet) {
        Place circle = new Place();
        circle.setCenterX(775);
        circle.setCenterY(370);
        circle.setRadius(20);
        circle.setScaleX(petriNet.scaling);
        circle.setScaleY(petriNet.scaling);
        circle.setElements();
        circle.setFill(Color.LIGHTCORAL);
        this.setDraggable.setDraggable(circle, this, petriNet);
        this.active.changeActive(circle, changeActivation, node, this);
        this.getChildren().add(circle);
        this.getChildren().add(circle.label);
        this.setDraggable.setDraggable(circle.label, this, petriNet);
        this.getChildren().add(circle.marks);
        this.setDraggable.setDraggable(circle.marks, this, petriNet);
        petriNet.placeList.add(circle);
        checkConnectionPossible(petriNet);
        checkDeletePossible(petriNet);
    }

    public void createTransition(PetriNet petriNet) {
        Transition rectangle = new Transition();
        rectangle.setWidth(30);
        rectangle.setHeight(30);
        rectangle.setScaleX(petriNet.scaling);
        rectangle.setScaleY(petriNet.scaling);
        rectangle.setX(760);
        rectangle.setY(430);
        rectangle.setElements();
        rectangle.setFill(Color.LIGHTGREEN);
        this.setDraggable.setDraggable(rectangle, this, petriNet);
        this.active.changeActive(rectangle, changeActivation, node, this);
        this.getChildren().add(rectangle);
        this.getChildren().add(rectangle.label);
        this.setDraggable.setDraggable(rectangle.label, this, petriNet);
        petriNet.transitionList.add(rectangle);
        checkConnectionPossible(petriNet);
        checkDeletePossible(petriNet);
    }

    private void createConnection() {
        this.unActivate();
        place.setVisible(false);
        transition.setVisible(false);
        delete.setVisible(false);
        connection.setText("Cancel");
        connection.setOnAction(actionEvent -> cancelConnection());
    }

    private void cancelConnection() {
        place.setVisible(true);
        transition.setVisible(true);
        delete.setVisible(true);
        connection.setOnAction(actionEvent -> this.createConnection());
        connection.setText("Create connection");
        this.unActivate();
    }

    private void unActivate() {
        while (this.node.size() > 0) {
            this.changeActivation.changeActivation(node.get(0), node);
        }
    }

    private void checkFurtherPossible() {
        this.goFurther.setDisable((petriNetFirst.placeList.size() <= 0 || petriNetFirst.transitionList.size() <= 0) ||
                (petriNetSecond.placeList.size() <= 0 || petriNetSecond.transitionList.size() <= 0));
    }

    private void checkConnectionPossible(PetriNet petriNet) {
        this.connection.setDisable(petriNet.placeList.size() <= 0 || petriNet.transitionList.size() <= 0);
    }

    private void checkDeletePossible(PetriNet petriNet) {
        this.delete.setDisable(petriNet.placeList.size() == 0 && petriNet.transitionList.size() == 0);
    }

    public void buildConnection(boolean childrenCan) {
        if (Objects.equals(connection.getText(), "Cancel")) {
            if (this.node.size() == 2) {
                Place place;
                Transition transition;
                if (this.node.get(0).getClass() == this.node.get(1).getClass()) {
                    unActivate();
                    return;
                }
                if (this.node.get(0).getClass() == Place.class) {
                    place = (Place) this.node.get(0);
                    transition = (Transition) this.node.get(1);
                    if (place.childrenList.contains(transition) && !childrenCan) {
                        unActivate();
                        return;
                    }
                } else {
                    place = (Place) this.node.get(1);
                    transition = (Transition) this.node.get(0);
                    if (transition.childrenList.contains(place) && !childrenCan) {
                        unActivate();
                        return;
                    }
                }
                List<Double> coordX = new ArrayList<>();
                List<Double> coordY = new ArrayList<>();
                for (Node node : this.node) {
                    Bounds bounds = node.getBoundsInLocal();
                    Point2D coordinates = node.localToScene(bounds.getCenterX(), bounds.getCenterY());
                    coordX.add(coordinates.getX());
                    coordY.add(coordinates.getY());
                }
                this.getChildren().remove(this.node.get(0));
                this.getChildren().remove(this.node.get(1));
                Line line = new Line(coordX.get(0), coordY.get(0), coordX.get(1), coordY.get(1));
                Arc arrow = new Arc();
                arrow.setStartX(line.getStartX());
                arrow.setStartY(line.getStartY());
                arrow.setEndX(line.getEndX());
                arrow.setEndY(line.getEndY());arrow.setArrows();
                this.getChildren().add(arrow);
                this.getChildren().add(arrow.firstArrow);
                this.getChildren().add(arrow.secondArrow);
                this.getChildren().add(this.node.get(0));
                this.getChildren().add(this.node.get(1));
                this.getChildren().remove(place.label);
                this.getChildren().remove(place.marks);
                this.getChildren().remove(transition.label);
                this.getChildren().add(place.label);
                this.getChildren().add(place.marks);
                this.getChildren().add(transition.label);
                if (this.node.get(0).getClass() == Place.class) {
                    place.childrenList.add(transition);
                    place.childrenLine.add(arrow);
                    transition.parentList.add(place);
                    transition.parentLine.add(arrow);
                } else {
                    place.parentList.add(transition);
                    place.parentLine.add(arrow);
                    transition.childrenList.add(place);
                    transition.childrenLine.add(arrow);
                }
                unActivate();
            }
        }
    }

    private void deleteElements(PetriNet petriNet) {
        for (Shape shape : this.node) {
            if (shape.getClass() == Place.class) {
                Place place = (Place) shape;
                for (int j = 0; j < place.parentList.size(); ++j) {
                    Arc line = place.parentLine.get(j);
                    place.parentList.get(j).childrenLine.remove(line);
                    place.parentList.get(j).childrenList.remove(place);
                    this.getChildren().remove(line);
                    this.getChildren().remove(line.firstArrow);
                    this.getChildren().remove(line.secondArrow);
                }
                for (int j = 0; j < place.childrenList.size(); ++j) {
                    Arc line = place.childrenLine.get(j);
                    place.childrenList.get(j).parentLine.remove(line);
                    place.childrenList.get(j).parentList.remove(place);
                    this.getChildren().remove(line);
                    this.getChildren().remove(line.firstArrow);
                    this.getChildren().remove(line.secondArrow);
                }
                petriNet.placeList.remove(place);
            } else {
                Transition transition = (Transition) shape;
                for (int j = 0; j < transition.parentList.size(); ++j) {
                    Arc line = transition.parentLine.get(j);
                    transition.parentList.get(j).childrenLine.remove(line);
                    transition.parentList.get(j).childrenList.remove(transition);
                    this.getChildren().remove(line);
                    this.getChildren().remove(line.firstArrow);
                    this.getChildren().remove(line.secondArrow);
                }
                for (int j = 0; j < transition.childrenList.size(); ++j) {
                    Arc line = transition.childrenLine.get(j);
                    transition.childrenList.get(j).parentLine.remove(line);
                    transition.childrenList.get(j).parentList.remove(transition);
                    this.getChildren().remove(line);
                    this.getChildren().remove(line.firstArrow);
                    this.getChildren().remove(line.secondArrow);
                }
                petriNet.transitionList.remove(transition);
            }
            this.getChildren().remove(shape);
        }
        this.node.clear();
        checkConnectionPossible(petriNet);
        checkDeletePossible(petriNet);
    }

    private void startWindow() {
        this.unActivate();
        this.getChildren().clear();
        this.entranceWindow();
    }

    private Group getLittleGroup(PetriNet petriNet, double scale) {
        Group first = new Group();
        for (Place place : petriNet.placeList) {
            first.getChildren().add(place);
            first.getChildren().add(place.marks);
            first.getChildren().add(place.label);
            for (Arc line : place.childrenLine) {
                first.getChildren().add(line);
            }
        }
        for (Transition transition : petriNet.transitionList) {
            first.getChildren().add(transition);
            first.getChildren().add(transition.label);
            for (Arc line : transition.childrenLine) {
                first.getChildren().add(line);
            }
        }
        first.setScaleX(1);
        first.setScaleY(1);
        first.setScaleX(first.getScaleX() * scale);
        first.setScaleY(first.getScaleY() * scale);
        return first;
    }

    private void morphismBack() {
        this.getChildren().clear();
        petriNetFirst.morphismsList.clear();
        petriNetSecond.morphismsList.clear();
        for (Place place : petriNetFirst.placeList) {
            place.exColor = Color.LIGHTCORAL;
        }
        for (Place place : petriNetSecond.placeList) {
            place.exColor = Color.LIGHTCORAL;
        }
        for (Transition transition : petriNetFirst.transitionList) {
            transition.exColor = Color.LIGHTGREEN;
        }
        for (Transition transition : petriNetSecond.transitionList) {
            transition.exColor = Color.LIGHTGREEN;
        }
        this.entranceWindow();
    }

    private void makeMorphisms() {
        this.getChildren().clear();

        Button goBack = new Button();
        goBack.setText("Go back");
        goBack.setTranslateX(100);
        goBack.setTranslateY(100);
        goBack.setOnAction(actionEvent -> this.morphismBack());
        this.getChildren().add(goBack);

        connection.setText("Create Morphism");
        for (Place place : petriNetFirst.placeList) {
            place.setScaleX(1);
            place.setScaleX(0.5);
            place.setScaleY(1);
            place.setScaleY(0.5);
            place.marks.setScaleX(1);
            place.marks.setScaleX(0.5);
            place.marks.setScaleY(1);
            place.marks.setScaleY(0.5);
            place.label.setScaleX(1);
            place.label.setScaleX(0.5);
            place.label.setScaleY(1);
            place.label.setScaleY(0.5);
            place.setCenterX(450);
            place.setTranslateX(place.getTranslateX() / 775 * 450);
            place.marks.setTranslateX(place.getCenterX() - 10);
            place.marks.setTranslateY(place.getCenterY() + 10);
            place.textArea.setTranslateX(place.getCenterX());
            place.textArea.setTranslateY(place.getCenterY());
            place.label.setTranslateX(place.getCenterX() + 10);
            place.label.setTranslateY(place.getCenterY()- 10);
            this.getChildren().add(place);
            this.getChildren().add(place.marks);
            this.getChildren().add(place.label);
        }
        for (Transition transition : petriNetFirst.transitionList) {
            transition.setScaleX(1);
            transition.setScaleX(0.5);
            transition.setScaleY(1);
            transition.setScaleY(0.5);
            transition.label.setScaleX(1);
            transition.label.setScaleX(0.5);
            transition.label.setScaleY(1);
            transition.label.setScaleY(0.5);
            transition.setX(450);
            transition.setTranslateX(transition.getTranslateX() / 760 * 450);
            transition.textArea.setTranslateX(transition.getX());
            transition.textArea.setTranslateY(transition.getY());
            transition.label.setTranslateX(transition.getX() - 5);
            transition.label.setTranslateY(transition.getY() - 5);
            transition.setTranslateX(0);
            this.getChildren().add(transition);
            this.getChildren().add(transition.label);
        }
        rewriteLines(petriNetFirst);
        for (Place place : petriNetSecond.placeList) {
            place.setScaleX(1);
            place.setScaleX(0.5);
            place.setScaleY(1);
            place.setScaleY(0.5);
            place.marks.setScaleX(1);
            place.marks.setScaleX(0.5);
            place.marks.setScaleY(1);
            place.marks.setScaleY(0.5);
            place.label.setScaleX(1);
            place.label.setScaleX(0.5);
            place.label.setScaleY(1);
            place.label.setScaleY(0.5);
            place.setCenterX(550);
            place.setTranslateX(-1 * place.getTranslateX() / 775 * 450);
            place.marks.setTranslateX(place.getCenterX() - 10);
            place.marks.setTranslateY(place.getCenterY() + 10);
            place.textArea.setTranslateX(place.getCenterX());
            place.textArea.setTranslateY(place.getCenterY());
            place.label.setTranslateX(place.getCenterX() + 10);
            place.label.setTranslateY(place.getCenterY() - 10);
            this.getChildren().add(place);
            this.getChildren().add(place.marks);
            this.getChildren().add(place.label);
        }
        for (Transition transition : petriNetSecond.transitionList) {
            transition.setScaleX(1);
            transition.setScaleX(0.5);
            transition.setScaleY(1);
            transition.setScaleY(0.5);
            transition.label.setScaleX(1);
            transition.label.setScaleX(0.5);
            transition.label.setScaleY(1);
            transition.label.setScaleY(0.5);
            transition.setX(550);
            transition.setTranslateX(-1 * transition.getTranslateX() / 760 * 450);
            transition.textArea.setTranslateX(transition.getX());
            transition.textArea.setTranslateY(transition.getY());
            transition.label.setTranslateX(transition.getX() - 5);
            transition.label.setTranslateY(transition.getY() - 5);
            this.getChildren().add(transition);
            this.getChildren().add(transition.label);
        }
        rewriteLines(petriNetSecond);
        goFurther.setText("Check if morphism");
        goFurther.setTranslateX(460);
        connection.setTranslateY(380);
        connection.setOnAction(actionEvent -> this.createMorphism());
        goFurther.setOnAction(actionEvent -> doMath());
        goFurther.setTranslateY(650);
        this.getChildren().add(connection);
        this.getChildren().add(goFurther);
    }

    private void doMath() {
        morphismChecker = new MorphismChecker(petriNetFirst, petriNetSecond);
        String result = morphismChecker.checkResult();
        System.out.println(result);
        res = new Label();
        res.setTextAlignment(TextAlignment.CENTER);
        res.setTranslateX(300);
        res.setTranslateY(200);
        if (Objects.equals(result, "totalSurjective")) {
            res.setText("The relation is not morphism!\nIt should be total and surjective");
            return;
        }
        if (Objects.equals(result, "checkCircles")) {
            res.setText("The relation is not morphism!\nRefined model's subnets should not include circles!");
            return;
        }
        if (Objects.equals(result, "checkPositions")) {
            res.setText("The relation is not morphism!\nPositions should reflect on positions!");
            return;
        }
        if (Objects.equals(result, "checkMarking")) {
            res.setText("The relation is not morphism!\nInitial markings should be saved in the abstract model!");
            return;
        }
        if (Objects.equals(result, "checkTransitionToTransition")) {
            res.setText("The relation is not morphism!\nPre-events and post-events of the model should be saved!");
            return;
        }
        if (Objects.equals(result, "TransitionToPlace")) {
            res.setText("The relation is not morphism!\nNeighbours of transition should be reflected into same position!");
            return;
        }
        if (Objects.equals(result, "checkPreEvents")) {
            res.setText("The relation is not morphism!\nPre-events of the abstract position should contain pre-events of it's refinement!");
            return;
        }
        if (Objects.equals(result, "checkPostEvents")) {
            res.setText("The relation is not morphism!\nPost-events of the abstract position should be the same as its refinement has!");
            return;
        }
        if (Objects.equals(result, "checkInternalPlaces")) {
            res.setText("The relation is not morphism!\nPositions inside (not on the borders!) the refined subnet should have its pre- and post-events reflected on the same position!!");
            return;
        }
        if (Objects.equals(result, "checkSequentialComponent")) {
            res.setText("The relation is not morphism!\nAll positions reflected on positions should have sequential component, which include it and all it neighbours!");
            return;
        }
        res.setText("Congratulations!\nThe reflection is alpha-morphism!");
        this.getChildren().add(res);
    }

    private void createMorphism() {
        res.setVisible(false);
        int first = random.nextInt(255);
        int second = random.nextInt(255);
        int three = random.nextInt(255);
        List<Node> groupFirst = new ArrayList<>();
        List<Node> groupSecond = new ArrayList<>();
        List<List<Node>> empties = new ArrayList<>();
        petriNetFirst.morphismsList.add(groupFirst);
        petriNetSecond.morphismsList.add(groupSecond);
        for (Shape shape : this.node) {
            shape.setFill(Color.rgb(first, second, three));
            for (List<Node> nodeList : petriNetFirst.morphismsList) {
                if (nodeList.contains(shape)) {
                    nodeList.remove(shape);
                    if (nodeList.isEmpty()) {
                        empties.add(nodeList);
                    }
                }
            }
            for (List<Node> nodeList : petriNetSecond.morphismsList) {
                if (nodeList.contains(shape)) {
                    nodeList.remove(shape);
                    if (nodeList.isEmpty()) {
                        empties.add(nodeList);
                    }
                }
            }
            if (shape.getClass() == Place.class) {
                Place place = (Place) shape;
                place.exColor = Color.rgb(first, second, three);
                if (petriNetFirst.placeList.contains(place)) {
                    groupFirst.add(place);
                } else {
                    groupSecond.add(place);
                }
            } else {
                Transition transition = (Transition) shape;
                transition.exColor = Color.rgb(first, second, three);
                if (petriNetFirst.transitionList.contains(transition)) {
                    groupFirst.add(transition);
                } else {
                    groupSecond.add(transition);
                }
            }
        }
        for (List<Node> nodes : empties) {
            if (nodes.isEmpty()) {
                petriNetFirst.morphismsList.remove(nodes);
            }
        }
        for (List<Node> nodes : empties) {
            if (nodes.isEmpty()) {
                petriNetSecond.morphismsList.remove(nodes);
            }
        }
        this.node.clear();
    }

    private void formNet(PetriNet petriNet, Map<String, String> places, List<String> transitions, Map<String, String> cons) {
        Map<String, Integer> placeMap =  new HashMap<>();
        Map<String, Integer> transitionMap = new HashMap<>();
        for (int i = 0; i < places.size(); ++i) {
            placeMap.put(places.keySet().toArray()[i].toString(), i);
        }
        for (int i = 0; i < transitions.size(); ++i) {
            String string = transitions.toArray()[i].toString();
            string = string.replace("\"", "");
            transitionMap.put(string, i);
        }
        double stepPlacesX = 850. / places.size();
        double stepPlacesY = 700. / places.size();
        double starterX = 40;
        for (int i = 0; i < places.size(); ++i) {
            this.createPlace(petriNet);
            ((Place)this.getChildren().get(this.getChildren().size() - 3)).setCenterX(starterX + stepPlacesX);
            ((Place)this.getChildren().get(this.getChildren().size() - 3)).setCenterY(random.nextDouble(stepPlacesY * places.size() + 40));
            ((Place)this.getChildren().get(this.getChildren().size() - 3)).setElements();
            starterX += stepPlacesX;
            if (starterX + stepPlacesX > 850) {
                starterX = stepPlacesX;
            }
        }
        stepPlacesY = 700. / places.size();
        starterX = 0;
        for (int i = 0; i < transitions.size(); ++i) {
            this.createTransition(petriNet);
            ((Transition)this.getChildren().get(this.getChildren().size() - 2)).setX(starterX + stepPlacesX);
            ((Transition)this.getChildren().get(this.getChildren().size() - 2)).setY(random.nextDouble(stepPlacesY * places.size() + 40));
            ((Transition)this.getChildren().get(this.getChildren().size() - 2)).setElements();
            starterX += stepPlacesX;
            if (starterX + stepPlacesX > 850) {
                starterX = stepPlacesX;
            }
        }
        this.connection.setText("Cancel");
        for (int i = 0; i < cons.size(); ++i) {
            String parent = (String) cons.keySet().toArray()[i];
            String child = (String) cons.values().toArray()[i];
            if (placeMap.containsKey(parent)) {
                this.node.add(petriNet.placeList.get(placeMap.get(parent)));
                this.node.add(petriNet.transitionList.get(transitionMap.get(child)));
            } else {
                this.node.add(petriNet.transitionList.get(transitionMap.get(parent)));
                this.node.add(petriNet.placeList.get(placeMap.get(child)));
            }
            this.buildConnection(true);
        }
        this.connection.setText("Create connection");
    }
}
