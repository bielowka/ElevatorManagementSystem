package elevatorSystems;
import elevatorSystems.Elevator.Elevator;
import elevatorSystems.GUI.PickingUpObserver;
import elevatorSystems.system.ElevatorSystem;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElevatorSystemTest {

    @Test
    public void basicsTest(){
        ElevatorSystem test1 = new ElevatorSystem(1, 0, 10);
        test1.setObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        });
        test1.pickup(5,1);
        for (int i = 0; i < 5; i++) test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 5, 5}));
        test1.pickup(8,1);
        test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 6, 8}));
        test1.pickup(10,-1);
        test1.pickup(1,1);
        test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 7, 8}));
        for (int i = 0; i < 3; i++) test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 10, 10}));
        test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 9, 1}));
        for (int i = 0; i < 8; i++) test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 1, 1}));
        test1.step();
        test1.step();
        test1.step();
        assertTrue(Arrays.equals(test1.status().get(0), new int[]{0, 1, 1}));
    }

    @Test
    public void assignmentAlgorithmTest(){
        ElevatorSystem test2 = new ElevatorSystem(1, -5, 5);
        test2.setObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        });
        test2.pickup(3,1);
        test2.pickup(2,1);
        test2.pickup(3,1);
        test2.pickup(-1,-1);
        test2.step();
        assertTrue(Arrays.equals(test2.status().get(0), new int[]{0, 1, 2}));
        test2.step();
        assertTrue(Arrays.equals(test2.status().get(0), new int[]{0, 2, 2}));
        test2.step();
        assertTrue(Arrays.equals(test2.status().get(0), new int[]{0, 3, 3}));
        test2.step();
        assertTrue(Arrays.equals(test2.status().get(0), new int[]{0, 2, -1}));

        test2.update(0,4,3);
        assertTrue(Arrays.equals(test2.status().get(0), new int[]{0, 4, 3}));
    }

    @Test
    public void twoElevators(){
        ElevatorSystem test3 = new ElevatorSystem(2,-5,5);
        test3.setObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        });
        test3.pickup(4,-1);
        test3.pickup(-2,1);
        test3.step();
        assertTrue(Arrays.equals(test3.status().get(0), new int[]{0, -1, -2}));
        assertTrue(Arrays.equals(test3.status().get(1), new int[]{1, 1, 4}));
    }

    @Test
    public void test4(){
        ElevatorSystem test4 = new ElevatorSystem(2,-1,12);
        test4.setObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        });
        test4.pickup(12,-1);
        test4.pickup(12,1);
        test4.pickup(10,1);
        test4.pickup(8,-1);
        test4.step();
        assertTrue(Arrays.equals(test4.status().get(1), new int[]{1, 1, 10}));
    }

}
