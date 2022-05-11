package testController;

import checker.MorphismChecker;
import javafx.scene.Node;
import net.PetriNet;
import netElements.Place;
import netElements.Transition;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFalse {
    static MorphismChecker morphismChecker;

    static PetriNet first = new PetriNet();
    static PetriNet second = new PetriNet();

    @BeforeAll
    public static void setupNets() {
        for (int i = 0; i < 2; ++i) {
            first.placeList.add(new Place());
        }
        for (int i = 0; i < 1; ++i) {
            first.transitionList.add(new Transition());
        }
        first.placeList.get(0).childrenList.add(first.transitionList.get(0));
        first.placeList.get(1).childrenList.add(first.transitionList.get(0));

        first.transitionList.get(0).parentList.add(first.placeList.get(0));
        first.transitionList.get(0).parentList.add(first.placeList.get(1));

        for (int i = 0; i < 1; ++i) {
            second.placeList.add(new Place());
        }
        for (int i = 0; i < 2; ++i) {
            second.transitionList.add(new Transition());
        }

        second.placeList.get(0).parentList.add(second.transitionList.get(0));
        second.placeList.get(0).parentList.add(second.transitionList.get(1));

        second.transitionList.get(0).childrenList.add(second.placeList.get(0));
        second.transitionList.get(1).childrenList.add(second.placeList.get(0));

        List<Node> morph = new ArrayList<>();
        morph.add(first.placeList.get(0));
        morph.add(first.transitionList.get(0));
        first.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(first.placeList.get(1));
        first.morphismsList.add(morph);


        morph = new ArrayList<>();
        morph.add(second.transitionList.get(0));
        second.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(second.placeList.get(0));
        morph.add(second.transitionList.get(1));
        second.morphismsList.add(morph);

        morphismChecker = new MorphismChecker(first, second);
    }

    @Test
    void checkTotal() {
        assertTrue(morphismChecker.totalSurjective());
    }

    @Test
    void checkCircle() {
        assertTrue(morphismChecker.checkCircles());
    }

    @Test
    void checkPositions() {
        assertTrue(morphismChecker.checkPositions());
    }

    @Test
    void checkMarking() {
        assertTrue(morphismChecker.checkMarking());
    }

    @Test
    void checkTransitionToTransition() {
        assertTrue(morphismChecker.checkTransitionToTransition());
    }

    @Test
    void TransitionToPlace() {
        assertTrue(morphismChecker.TransitionToPlace());
    }

    @Test
    void checkPreEvents() {
        assertTrue(morphismChecker.checkPreEvents());
    }

    @Test
    void checkPostEvents() {
        assertTrue(morphismChecker.checkPostEvents());
    }

    @Test
    void checkInternalPlaces() {
        assertTrue(morphismChecker.checkInternalPlaces());
    }

    @Test
    void checkSequentialComponent() {
        assertTrue(morphismChecker.checkSequentialComponent());
    }
}
