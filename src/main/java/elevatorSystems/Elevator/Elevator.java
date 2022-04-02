package elevatorSystems.Elevator;

import elevatorSystems.system.Direction;
import static elevatorSystems.system.Direction.*;



public class Elevator {

    private int currentFloor;
    private Direction status;
    private int destination;

    private final int highestFloor;
    private final int lowestFloor;

    public Elevator(int lowestFloor, int highestFloor){
        this.status = STOP;
        this.currentFloor = 0;
        this.highestFloor = highestFloor;
        this.lowestFloor = lowestFloor;
    }


    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getStatus() {
        return status;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }


    public void move(Direction movement){
        this.status = movement;
        if (movement == Direction.UP){
            if (currentFloor + 1 <= highestFloor){
                currentFloor = currentFloor + 1;
            }
            else System.out.println("Cannot go higher");
        }
        else if (movement == Direction.DOWN){
            if (currentFloor - 1 >= lowestFloor) {
                currentFloor = currentFloor - 1;
            }
            else System.out.println("Cannot go lower");
        }
        else if (movement == STOP){
            destination = currentFloor;
        }
    }



}
