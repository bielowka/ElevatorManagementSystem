package elevatorSystems;
import elevatorSystems.system.ElevatorSystem;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElevatorSystemTest {

    @Test
    public void basicsTest(){
        ElevatorSystem test1 = new ElevatorSystem(1, 0, 10);
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

}
