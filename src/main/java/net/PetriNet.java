package net;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import netElements.Place;
import netElements.Transition;

import java.util.ArrayList;
import java.util.List;

public class PetriNet {
    public double scaling = 1;

    public List<List<Node>> morphismsList = new ArrayList<>();

    public List<Place> placeList = new ArrayList<>();

    public List<Transition> transitionList = new ArrayList<>();
}
