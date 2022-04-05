package elevatorSystems.system;

import elevatorSystems.Elevator.Elevator;

import java.util.ArrayList;
import java.util.Observer;

public class ElevatorSystem{
    private final int numOfElevators;
    private ArrayList<Elevator> elevators;
    private ArrayList<ArrayList<Request>> requests;
    private ArrayList<Request> toDo;
    private final int lowestFloor;
    private final int highestFloor;

    private Observer observer;


    public ElevatorSystem(int numOfElevators, int lowestFloor, int highestFloor) {
        this.numOfElevators = numOfElevators;
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;

        this.elevators = new ArrayList<Elevator>();
        this.requests = new ArrayList<ArrayList<Request>>();

        this.toDo = new ArrayList<Request>();



        for (int i = 0; i < this.numOfElevators; i++){
            elevators.add(new Elevator(this.lowestFloor,this.highestFloor));
            requests.add(new ArrayList<Request>());
        }
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void pickup(int floor, int direction){
        Direction d = Direction.STOP;
        if (direction > 0) d = Direction.UP;
        if (direction < 0) d = Direction.DOWN;
        toDo.add(new Request(floor,d,observer));
    }

    public void deliver(int floor, int destination){
        toDo.add(new Request(floor,destination,observer));
    }


    public void update(int ID,int currentFloor, int destination){
        elevators.get(ID).setCurrentFloor(currentFloor);
        elevators.get(ID).setDestination(destination);
    }

    public void assignment(){
        //przydziala wszystko z toDo
        //obejmuje algorytm przydziału
        //oblicza minimum po czasie oczekiwania przewidywanym na ten moment i tam przydziela
        //poprawia rowniez kolejnosc bo sa ORDERED
        if (toDo.isEmpty()) return;
        //  WAŻNE - w trybie ORDER muszą być przydzielone windzie na danym floor

        //wersja trywialna
        for (Request r : toDo) {
            requests.get(0).add(r);
        }
        toDo.clear();
    }

    public void step(){
        for (int i = 0; i < elevators.size(); i++) {
            Elevator e = elevators.get(i);
            Request r;
            if (requests.get(i).isEmpty()) {
                e.move(Direction.STOP);
            }
            else {
                r = requests.get(i).get(0);

                if (r.getStatus() == RequestStatus.PICKUP){
                    e.setDestination(r.getFloor());
                }
                else if (r.getStatus() == RequestStatus.ORDER){
                    e.setDestination(r.getDestination());
                }

                if (e.getCurrentFloor() < e.getDestination()) e.move(Direction.UP);
                if (e.getCurrentFloor() > e.getDestination()) e.move(Direction.DOWN);


                r = requests.get(i).get(0);

                while (r != null && (e.getCurrentFloor() == r.getDestination() && r.getStatus() == RequestStatus.ORDER)) {
                    r.completed();
                    requests.get(i).remove(0);
                    e.move(Direction.STOP);
                    if (requests.get(i).isEmpty()) r = null;
                    else r = requests.get(i).get(0);
                }

                while (r != null && (e.getCurrentFloor() == r.getFloor() && r.getStatus() == RequestStatus.PICKUP)) {
                    r.ordered();
                    requests.get(i).remove(0);
                    e.move(Direction.STOP);
                    if (requests.get(i).isEmpty()) r = null;
                    else r = requests.get(i).get(0);
                }

            }

            if (!toDo.isEmpty()) assignment();

            if (requests.get(i).isEmpty()) {
                e.move(Direction.STOP);
                continue;
            }



        }

    }

    public ArrayList<int[]> status(){
        ArrayList<int[]> statuses = new ArrayList<>();
        for (int i = 0; i < elevators.size(); i++) {
            Elevator e = elevators.get(i);
            statuses.add(new int[]{i, e.getCurrentFloor(), e.getDestination()});
        }
        return statuses;
    }


}
