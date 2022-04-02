package elevatorSystems.system;

import static elevatorSystems.system.RequestStatus.*;

public class Request {
    private final int floor;
    private final Direction direction;
    private RequestStatus status;
    private int destination;
    private int waitingTime;

    public Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
        this.status = PICKUP;
        this.waitingTime = 0;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDestination(){
        return destination;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void ordered(int floor){
        this.waitingTime = 0;
        this.status = ORDER;
        this.destination = floor;
    }

    public void completed(){
        this.status = COMPLETE;
    }
}
