package checker;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import net.PetriNet;
import netElements.Place;
import netElements.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorphismChecker {

    public MorphismChecker(PetriNet first, PetriNet second) {
        this.first = first;
        this.second = second;
    }

    private final PetriNet first;
    private final PetriNet second;

    public String checkResult() {
        if (!totalSurjective()) {
            return "totalSurjective";
        }
        if (!checkCircles()) {
            return "checkCircles";
        }
        if (!checkPositions()) {
            return "checkPositions";
        }
        if (!checkMarking()) {
            return "checkMarking";
        }
        if (!checkTransitionToTransition()) {
            return "checkTransitionToTransition";
        }
        if (!TransitionToPlace()) {
            return "TransitionToPlace";
        }
        if (!checkPreEvents()) {
            return "checkPreEvents";
        }
        if (!checkPostEvents()) {
            return "checkPostEvents";
        }
        if (!checkInternalPlaces()) {
            return "checkInternalPlaces";
        }
        if (!checkSequentialComponent()) {
            return "checkSequentialComponent";
        }
        return "True";
    }

    public boolean totalSurjective() {
        if (first.morphismsList.size() != second.morphismsList.size()) {
            return false;
        }
        for (int i = 0; i < first.morphismsList.size(); ++i) {
            if (first.morphismsList.get(i).size() == 0 || second.morphismsList.get(i).size() == 0) {
                return false;
            }
            if (second.morphismsList.get(i).size() != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean recurseCircles(Node node, Map<Shape, Boolean> map) {
        if (map.get((Shape) node)) {
            return false;
        } else {
            map.put((Shape) node, true);
        }
        boolean result = true;
        if (node.getClass() == Place.class) {
            Place place = (Place) node;
            for (Transition transition : place.parentList) {
                if (map.containsKey(transition)) {
                    result = recurseCircles(transition, map);
                }
            }
        } else {
            Transition transition = (Transition) node;
            for (Place place : transition.parentList) {
                if (map.containsKey(place)) {
                    result = recurseCircles(place, map);
                }
            }
        }
        return result;
    }

    public boolean checkCircles() {
        boolean result = true;
        for (List<Node> group : first.morphismsList) {
            Map<Shape, Boolean> map = new HashMap<>();
            for (Node node : group) {
                if (node.getClass() == Place.class) {
                    map.put((Place)node, false);
                } else {
                    map.put((Transition)node, false);
                }
            }
            result = recurseCircles((Node) map.keySet().toArray()[0], map);
        }
        return result;
    }

    public boolean checkPositions() {
        for (int i = 0 ; i < first.morphismsList.size(); ++i) {
            boolean isPlace = false;
            for (int j = 0; j < first.morphismsList.get(i).size(); ++j) {
                if (first.morphismsList.get(i).get(j).getClass() == Place.class) {
                    isPlace = true;
                }
            }
            if (second.morphismsList.get(i).get(0).getClass() != Place.class) {
                if (isPlace) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkMarking() {
        for (int i = 0 ; i < first.morphismsList.size(); ++i) {
            int markings = 0;
            for (int j = 0; j < first.morphismsList.get(i).size(); ++j) {
                if (first.morphismsList.get(i).get(j).getClass() == Place.class) {
                    if (((Place) first.morphismsList.get(i).get(j)).marking) {
                        markings += 1;
                    }
                }
            }
            if (second.morphismsList.get(i).get(0).getClass() == Place.class) {
                if (((Place)second.morphismsList.get(i).get(0)).marking && markings != 1) {
                    return false;
                }
                if (!((Place)second.morphismsList.get(i).get(0)).marking && markings != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkTransitionToTransition() {
        for (int i = 0 ; i < first.morphismsList.size(); ++i) {
            if (second.morphismsList.get(i).get(0).getClass() == Place.class) {
                continue;
            }
            List<Place> parents = new ArrayList<>();
            List<Place> children = new ArrayList<>();
            if (second.morphismsList.get(i).get(0).getClass() == Transition.class) {
                parents.addAll(((Transition) second.morphismsList.get(i).get(0)).parentList);
                children.addAll(((Transition) second.morphismsList.get(i).get(0)).childrenList);
            }
            List<Integer> parentsMorphisms = new ArrayList<>();
            List<Integer> childrenMorphisms = new ArrayList<>();
            for (Place place : parents) {
                for (int j = 0; j < second.morphismsList.size(); ++j) {
                    if (second.morphismsList.get(j).get(0) == place) {
                        parentsMorphisms.add(j);
                    }
                }
            }
            for (Place place : children) {
                for (int j = 0; j < second.morphismsList.size(); ++j) {
                    if (second.morphismsList.get(j).get(0) == place) {
                        childrenMorphisms.add(j);
                    }
                }
            }
            List<Integer> parentsMorphismsRefine = new ArrayList<>();
            List<Integer> childrenMorphismsRefine = new ArrayList<>();
            List<Transition> refine = new ArrayList<>();
            for (Node node : first.morphismsList.get(i)) {
                if (node.getClass() == Transition.class) {
                    refine.add((Transition) node);
                }
            }
            for (Transition transition : refine) {
                for (Place place : transition.parentList) {
                    for (int j = 0; j < first.morphismsList.size(); ++j) {
                        if (first.morphismsList.get(j).contains(place)) {
                            parentsMorphismsRefine.add(j);
                        }
                    }
                }
                for (Place place : transition.childrenList) {
                    for (int j = 0; j < first.morphismsList.size(); ++j) {
                        if (first.morphismsList.get(j).contains(place)) {
                            childrenMorphismsRefine.add(j);
                        }
                    }
                }
            }
            for (int number : parentsMorphismsRefine) {
                if (!parentsMorphisms.contains(number)) {
                    return false;
                }
            }
            for (int number : childrenMorphismsRefine) {
                if (!childrenMorphisms.contains(number)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean TransitionToPlace() {
        for (int i = 0 ; i < second.morphismsList.size(); ++i) {
            List<Place> parents = new ArrayList<>();
            List<Place> children = new ArrayList<>();
            boolean isTransition = false;
            if (second.morphismsList.get(i).get(0).getClass() == Place.class) {
                for (Node node : first.morphismsList.get(i)) {
                    if (node.getClass() == Transition.class) {
                        parents.addAll(((Transition) node).parentList);
                        children.addAll(((Transition) node).childrenList);
                        isTransition = true;
                        break;
                    }
                }
            }
            if (!isTransition) {
                continue;
            }
            for (Place place : parents) {
                if (!first.morphismsList.get(i).contains(place)) {
                    return false;
                }
            }
            for (Place place : children) {
                if (!first.morphismsList.get(i).contains(place)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkPreEvents() {
        for (int i = 0; i < second.morphismsList.size(); ++i) {
            if (second.morphismsList.get(i).get(0).getClass() == Transition.class) {
                continue;
            }
            List<Node> inElements = getInElements(i);
            List<Integer> abstractParentMorphisms = new ArrayList<>();
            for (Transition transition : ((Place)second.morphismsList.get(i).get(0)).parentList) {
                for (int j = 0; j < second.morphismsList.size(); ++j) {
                    if (second.morphismsList.get(j).contains(transition)) {
                        abstractParentMorphisms.add(j);
                    }
                }
            }
            for (Node inElement : inElements) {
                List<Integer> parentPlace = new ArrayList<>();
                Place place = (Place) inElement;
                for (Transition transition : place.parentList) {
                    for (int k = 0; k < first.morphismsList.size(); ++k) {
                        if (first.morphismsList.get(k).contains(transition)) {
                            parentPlace.add(k);
                            break;
                        }
                    }
                }
                if (!abstractParentMorphisms.isEmpty() && parentPlace.isEmpty()) {
                    return false;
                }
                for (Integer parent : parentPlace) {
                    if (!abstractParentMorphisms.contains(parent)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkPostEvents() {
        for (int i = 0; i < second.morphismsList.size(); ++i) {
            if (second.morphismsList.get(i).get(0).getClass() == Transition.class) {
                continue;
            }
            List<Node> outElements = getOutElements(i);
            List<Integer> abstractChildrenMorphisms = new ArrayList<>();
            for (Transition transition : ((Place)second.morphismsList.get(i).get(0)).childrenList) {
                for (int j = 0; j < second.morphismsList.size(); ++j) {
                    if (second.morphismsList.get(j).contains(transition)) {
                        abstractChildrenMorphisms.add(j);
                    }
                }
            }
            for (Node outElement : outElements) {
                List<Integer> placeChildren = new ArrayList<>();
                Place place = (Place) outElement;
                for (Transition transition : place.childrenList) {
                    for (int k = 0; k < first.morphismsList.size(); ++k) {
                        if (first.morphismsList.get(k).contains(transition)) {
                            placeChildren.add(k);
                        }
                    }
                }
                for (Integer placeChild : placeChildren) {
                    if (!abstractChildrenMorphisms.contains(placeChild)) {
                        return false;
                    }
                }
                for (Integer abstractChild : abstractChildrenMorphisms) {
                    if (!placeChildren.contains(abstractChild)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private List<Node> getInElements(int numberMorphism) {
        List<Node> refine = first.morphismsList.get(numberMorphism);
        List<Node> startRefine = new ArrayList<>();
        for (Node node : refine) {
            boolean isStart = true;
            if (node.getClass() == Place.class) {
                Place place = (Place) node;
                for (int j = 0; j < place.parentList.size(); ++j) {
                    if (refine.contains(place.parentList.get(j))) {
                        isStart = false;
                        break;
                    }
                }
            } else {
                Transition transition = (Transition) node;
                for (int j = 0; j < transition.parentList.size(); ++j) {
                    if (refine.contains(transition.parentList.get(j))) {
                        isStart = false;
                        break;
                    }
                }
            }
            if (isStart) {
                startRefine.add(node);
            }
        }
        return startRefine;
    }

    private List<Node> getOutElements(int numberMorphism) {
        List<Node> refine = first.morphismsList.get(numberMorphism);
        List<Node> outRefine = new ArrayList<>();
        for (Node node : refine) {
            boolean isOut = true;
            if (node.getClass() == Place.class) {
                Place place = (Place) node;
                for (int j = 0; j < place.childrenList.size(); ++j) {
                    if (refine.contains(place.childrenList.get(j))) {
                        isOut = false;
                        break;
                    }
                }
            } else {
                Transition transition = (Transition) node;
                for (int j = 0; j < transition.childrenList.size(); ++j) {
                    if (refine.contains(transition.childrenList.get(j))) {
                        isOut = false;
                        break;
                    }
                }
            }
            if (isOut) {
                outRefine.add(node);
            }
        }
        return outRefine;
    }

    public boolean checkInternalPlaces() {
        for (int i = 0; i < second.morphismsList.size(); ++i) {
            if (second.morphismsList.get(i).get(0).getClass() == Transition.class) {
                continue;
            }
            List<Node> inElements = getInElements(i);
            List<Node> outElements = getOutElements(i);
            for (int j = 0; j < first.morphismsList.get(i).size(); ++j) {
                Node node = first.morphismsList.get(i).get(j);
                if (node.getClass() == Transition.class) {
                    continue;
                }
                Place place = (Place) node;
                if (!inElements.contains(node)) {
                    for (Transition transition : place.parentList) {
                        if (!first.morphismsList.get(i).contains(transition)) {
                            return false;
                        }
                    }
                }
                if (!outElements.contains(node)) {
                    for (Transition transition : place.childrenList) {
                        if (!first.morphismsList.get(i).contains(transition)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkSequentialComponent() {
        List<Place> markers = new ArrayList<>();
        for (int i = 0; i < first.morphismsList.size(); ++i) {
            for (int j = 0; j < first.morphismsList.get(i).size(); ++j) {
                if (first.morphismsList.get(i).get(j).getClass() == Place.class) {
                    Place place = (Place) first.morphismsList.get(i).get(j);
                    if (place.marking) {
                        markers.add(place);
                    }
                }
            }
        }
        boolean oneForEvery = true;
        for (int i = 0; i < second.morphismsList.size(); ++i) {
            if (second.morphismsList.get(i).get(0).getClass() == Transition.class) {
                continue;
            }
            List<Transition> closers = new ArrayList<>();
            List<Node> inElements = getInElements(i);
            List<Node> outElements = getOutElements(i);
            for (Node inElement : inElements) {
                for (Transition transition : ((Place) inElement).parentList) {
                    if (!closers.contains(transition)) {
                        closers.add(transition);
                    }
                }
            }
            for (Node outElement : outElements) {
                for (Transition transition : ((Place) outElement).childrenList) {
                    if (!closers.contains(transition)) {
                        closers.add(transition);
                    }
                }
            }
            List<Place> insidePlaces = new ArrayList<>();
            for (Node node : first.morphismsList.get(i)) {
                if (node.getClass() == Place.class) {
                    insidePlaces.add((Place) node);
                }
            }
            boolean result = true;
            for (Place place : insidePlaces) {
                boolean oneMarking = false;
                for (Place place1 : markers) {
                    List<Shape> shapeList = new ArrayList<>();
                    shapeList.add(place1);
                    List<Place> placeList = new ArrayList<>();
                    List<Transition> transitionList = new ArrayList<>();
                    List<Transition> transitionList1 = new ArrayList<>();
                    if (goTrough(shapeList, closers, place, placeList, transitionList, transitionList1)) {
                        oneMarking = true;
                    }
                    for (Place place2 : first.placeList) {
                        place2.isSequential = false;
                    }
                    for (Transition transition : first.transitionList) {
                        transition.isParent = false;
                        transition.isChild = false;
                    }
                }
                result = oneMarking;
            }
            oneForEvery = result;
        }
        return oneForEvery;
    }

    private boolean goTrough(List<Shape> shapes, List<Transition> edges, Place place, List<Place> seques, List<Transition> kids, List<Transition> parent) {
        if (checkTrue(edges, place)) {
            return true;
        }
        if (shapes.isEmpty()) {
            return checkTrue(edges, place);
        }
        List<Shape> goFurther = new ArrayList<>();
        if (shapes.get(0).getClass() == Place.class) {
            boolean areClear = false;
            for (Shape shape : shapes) {
                if (!((Place)shape).isSequential) {
                    areClear = true;
                    for (Transition transition : ((Place)shape).parentList) {
                        if (transition.isParent) {
                            return false;
                        } else {
                            transition.isParent = true;
                            parent.add(transition);
                        }
                    }
                    for (Transition transition : ((Place)shape).childrenList) {
                        if (transition.isChild) {
                            return false;
                        } else {
                            transition.isChild = true;
                            kids.add(transition);
                            if (!transition.isParent) {
                                goFurther.add(transition);
                            }
                        }
                    }
                }
                ((Place)shape).isSequential = true;
                seques.add((Place) shape);
            }
            if (!areClear) {
                return checkTrue(edges, place);
            }
            return goTrough(goFurther, edges, place, seques, kids, parent);
        } else {
            boolean passBy = true;
            boolean overallResult;
            List<Integer> indexes = new ArrayList<>();
            for (Shape ignored : shapes) {
                indexes.add(0);
            }
            List<Shape> placeList = new ArrayList<>();
            while (!checkAllIndexes(shapes, indexes) || passBy) {
                if (!passBy) {
                    break;
                }
                if (checkAllIndexes(shapes, indexes)) {
                    passBy = false;
                }
                placeList.clear();
                for (int i = 0; i < shapes.size(); ++i) {
                    placeList.add(((Transition) shapes.get(i)).childrenList.get(indexes.get(i)));
                }
                int maxes = 0;
                for (int i = shapes.size() - 1; i >= 0; --i) {
                    if (indexes.get(i) != ((Transition) shapes.get(i)).childrenList.size() - 1) {
                        indexes.set(i, indexes.get(i) + 1);
                        if (maxes != 0) {
                            for (int j = i + 1; j < i + maxes + 1; ++j) {
                                indexes.set(j, 0);
                            }
                        }
                        break;
                    } else {
                        ++maxes;
                    }
                }
                List<Place> sequences = new ArrayList<>();
                List<Transition> children = new ArrayList<>();
                List<Transition> parents = new ArrayList<>();
                overallResult = goTrough(placeList, edges, place, sequences, children, parents);
                if (overallResult) {
                    return true;
                } else {
                    for (Place place1 : sequences) {
                        place1.isSequential = false;
                    }
                    for (Transition transition : children) {
                        transition.isChild = false;
                    }
                    for (Transition transition : parents) {
                        transition.isParent = false;
                    }
                }
            }
            return false;
        }
    }

    private boolean checkAllIndexes(List<Shape> listChildren, List<Integer> indexes) {
        List<Transition> transitionList = new ArrayList<>();
        for (Shape shape : listChildren) {
            transitionList.add((Transition) shape);
        }
        for (int i = 0; i < transitionList.size(); ++i) {
            if (indexes.get(i) < transitionList.get(i).childrenList.size() - 1) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTrue(List<Transition> edges, Place place) {
        if (!place.isSequential) {
            return false;
        }
        for (Transition transition : edges) {
            if (!transition.isChild || !transition.isParent) {
                return false;
            }
        }
        for (Transition transition : first.transitionList) {
            if ((transition.isParent && !transition.isChild) || (!transition.isParent && transition.isChild)) {
                return false;
            }
        }
        return true;
    }
}
