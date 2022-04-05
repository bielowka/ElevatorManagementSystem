package elevatorSystems.system;

import elevatorSystems.GUI.PickingUpObserver;

import java.util.Observable;
import java.util.Observer;

import static elevatorSystems.system.RequestStatus.*;

public class Request extends Observable {
    private final int floor;
    private Direction direction;
    private RequestStatus status;
    private int destination;
    private int waitingTime;

    private Observer observer;

    public Request(int floor, Direction direction, Observer observer) {
        this.floor = floor;
        this.direction = direction;
        this.status = PICKUP;
        this.waitingTime = 0;
        this.observer = observer;
    }

    public Request(int floor,int destination, Observer observer) {
        this.floor = floor;
        this.status = ORDER;
        this.waitingTime = 0;
        this.observer = observer;
        this.destination = destination;
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

    void changeData() {
        observer.update(this,this);
    }

    public void ordered(){
        this.waitingTime = 0;
        changeData();
    }

    public void completed(){
        this.status = COMPLETE;
    }


    @Override
    public String toString() {
        return "{" + "f=" + floor + "d=" + destination + "s=" + status + '}';
    }
}
