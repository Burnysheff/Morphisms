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

public class TestSecond {
    static MorphismChecker morphismChecker;

    static PetriNet first = new PetriNet();
    static PetriNet second = new PetriNet();

    @BeforeAll
    public static void setupNets() {
        for (int i = 0; i < 6; ++i) {
            first.placeList.add(new Place());
        }
        for (int i = 0; i < 7; ++i) {
            first.transitionList.add(new Transition());
        }
        first.placeList.get(0).marking = true;
        first.placeList.get(0).childrenList.add(first.transitionList.get(0));
        first.placeList.get(0).childrenList.add(first.transitionList.get(1));
        first.placeList.get(1).childrenList.add(first.transitionList.get(2));
        first.placeList.get(1).childrenList.add(first.transitionList.get(3));
        first.placeList.get(2).childrenList.add(first.transitionList.get(4));
        first.placeList.get(3).childrenList.add(first.transitionList.get(5));
        first.placeList.get(4).childrenList.add(first.transitionList.get(6));

        first.placeList.get(1).parentList.add(first.transitionList.get(0));
        first.placeList.get(2).parentList.add(first.transitionList.get(1));
        first.placeList.get(3).parentList.add(first.transitionList.get(2));
        first.placeList.get(4).parentList.add(first.transitionList.get(3));
        first.placeList.get(5).parentList.add(first.transitionList.get(4));
        first.placeList.get(5).parentList.add(first.transitionList.get(5));
        first.placeList.get(5).parentList.add(first.transitionList.get(6));

        first.transitionList.get(0).childrenList.add(first.placeList.get(1));
        first.transitionList.get(1).childrenList.add(first.placeList.get(2));
        first.transitionList.get(2).childrenList.add(first.placeList.get(3));
        first.transitionList.get(3).childrenList.add(first.placeList.get(4));
        first.transitionList.get(4).childrenList.add(first.placeList.get(5));
        first.transitionList.get(5).childrenList.add(first.placeList.get(5));
        first.transitionList.get(6).childrenList.add(first.placeList.get(5));

        first.transitionList.get(0).parentList.add(first.placeList.get(0));
        first.transitionList.get(1).parentList.add(first.placeList.get(0));
        first.transitionList.get(2).parentList.add(first.placeList.get(1));
        first.transitionList.get(3).parentList.add(first.placeList.get(1));
        first.transitionList.get(4).parentList.add(first.placeList.get(2));
        first.transitionList.get(5).parentList.add(first.placeList.get(3));
        first.transitionList.get(6).parentList.add(first.placeList.get(4));

        for (int i = 0; i < 3; ++i) {
            second.placeList.add(new Place());
        }
        for (int i = 0; i < 2; ++i) {
            second.transitionList.add(new Transition());
        }
        second.placeList.get(0).marking = true;
        second.placeList.get(0).childrenList.add(second.transitionList.get(0));
        second.placeList.get(1).childrenList.add(second.transitionList.get(1));

        second.placeList.get(1).parentList.add(second.transitionList.get(0));
        second.placeList.get(2).parentList.add(second.transitionList.get(1));

        second.transitionList.get(0).childrenList.add(second.placeList.get(1));
        second.transitionList.get(1).childrenList.add(second.placeList.get(2));

        second.transitionList.get(0).parentList.add(second.placeList.get(0));
        second.transitionList.get(1).parentList.add(second.placeList.get(1));

        List<Node> morph = new ArrayList<>();
        morph.add(first.placeList.get(0));
        first.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(first.transitionList.get(0));
        morph.add(first.transitionList.get(1));
        first.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(first.placeList.get(1));
        morph.add(first.placeList.get(2));
        morph.add(first.placeList.get(3));
        morph.add(first.placeList.get(4));
        morph.add(first.transitionList.get(2));
        morph.add(first.transitionList.get(3));
        first.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(first.transitionList.get(4));
        morph.add(first.transitionList.get(5));
        morph.add(first.transitionList.get(6));
        first.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(first.placeList.get(5));
        first.morphismsList.add(morph);


        morph = new ArrayList<>();
        morph.add(second.placeList.get(0));
        second.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(second.transitionList.get(0));
        second.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(second.placeList.get(1));
        second.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(second.transitionList.get(1));
        second.morphismsList.add(morph);

        morph = new ArrayList<>();
        morph.add(second.placeList.get(2));
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
