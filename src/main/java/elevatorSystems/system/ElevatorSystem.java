package elevatorSystems.system;

import elevatorSystems.Elevator.Elevator;

import java.util.ArrayList;
import java.util.Observer;

import static java.lang.Math.abs;

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
        this.elevators = new ArrayList<Elevator>();
        this.requests = new ArrayList<ArrayList<Request>>();
        this.toDo = new ArrayList<Request>();

        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;

        for (int i = 0; i < this.numOfElevators; i++){
            elevators.add(new Elevator(this.lowestFloor,this.highestFloor));
            requests.add(new ArrayList<Request>());
        }
    }

    public ArrayList<ArrayList<Request>> getRequests() {
        return requests;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public void pickup(int floor, int direction){
        Direction d = Direction.intToDirection(direction);
        toDo.add(new Request(floor,d,observer));
    }

    public void deliver(int floor, int destination){
        toDo.add(new Request(floor,destination,observer));
    }

    public void update(int ID,int currentFloor, int destination){
        elevators.get(ID).setCurrentFloor(currentFloor);
        elevators.get(ID).setDestination(destination);
    }

    public void step(){
        for (int i = 0; i < elevators.size(); i++) {
            Elevator e = elevators.get(i);
            Request r;

            if (!toDo.isEmpty()) assignment();

            if (requests.get(i).isEmpty()) {
                e.move(Direction.STOP);
            }
            else {
                movement(i, e);
            }
        }

        for (ArrayList<Request> l: requests){
            for (Request r : l){
                r.addWaitingTime();
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

    private void movement(int i, Elevator e) {
        Request r;
        r = requests.get(i).get(0);

        e.setDestination(r.getDestination());

        if (e.getCurrentFloor() < e.getDestination()) e.move(Direction.UP);
        if (e.getCurrentFloor() > e.getDestination()) e.move(Direction.DOWN);

        r = requests.get(i).get(0);

        while (r != null && (e.getCurrentFloor() == r.getDestination() && r.getStatus() == RequestStatus.ORDER)) {
            requests.get(i).remove(0);
            e.move(Direction.STOP);
            if (requests.get(i).isEmpty()) r = null;
            else r = requests.get(i).get(0);
        }

        while (r != null && (e.getCurrentFloor() == r.getDestination() && r.getStatus() == RequestStatus.PICKUP)) {
            r.ordered();
            requests.get(i).remove(0);
            e.move(Direction.STOP);
            if (requests.get(i).isEmpty()) r = null;
            else r = requests.get(i).get(0);
        }
    }

    private void assignment(){
        if (toDo.isEmpty()) return;
        for (Request r : toDo) {
            int eID = 0;
            int queuePos = requests.get(0).size();
            int minTime = (highestFloor-lowestFloor)*1000 + 1;


            for (int i = 0; i < elevators.size(); i++) {
                Elevator e = elevators.get(i);
                ArrayList<Request> l = requests.get(i);
                int t = 0;
                int p = -1;
                if (l.isEmpty()) {
                    p = 0;
                    t = abs(r.getDestination() - e.getCurrentFloor());
                }
                else {
                    if (e.getStatus() == r.getDirection() || e.getStatus() == Direction.STOP){
                        if ((r.getDestination() - e.getCurrentFloor())*r.getDirection().toInt() >= 0){
                            if ((l.get(0).getDestination() - r.getDestination())*r.getDirection().toInt() >= 0){
                                p = 0;
                                t = abs(r.getDestination() - e.getCurrentFloor());
                            }
                        }
                    }
                    if (r.getStatus() == RequestStatus.ORDER && e.getStatus() != r.getDirection()){//prioritize people inside, then person entering
                        boolean isOrdered = false;
                        for (Request o : l) {
                            if (o.getStatus() == RequestStatus.ORDER) isOrdered = true;
                        }
                        if (!isOrdered) {
                            p = 0;
                            t = abs(r.getDestination() - e.getCurrentFloor());
                        }
                    }
                    if (p == -1) {
                        p = 1;
                        while (p < l.size()) {
                            if (l.get(p).getDirection() == r.getDirection()){
                                if ((r.getDestination() - l.get(p-1).getDestination())*r.getDirection().toInt() >= 0){
                                    if ((l.get(p).getDestination() - r.getDestination())*r.getDirection().toInt() >= 0){
                                        break;
                                    }
                                }
                            }
                            p = p + 1;
                        }
                        t = abs(l.get(0).getDestination() - e.getCurrentFloor());
                    }

                    int j = 1;
                    while (j < p){
                        t = t + abs(l.get(j).getDestination() - l.get(j-1).getDestination());
                        j = j + 1;
                    }
                    if (p > 0) t = t + abs(l.get(p-1).getDestination() - r.getDestination());

                }
                if (r.getStatus() == RequestStatus.ORDER && r.getFloor() != e.getCurrentFloor()) {
                    t = (highestFloor-lowestFloor)*1000;
                }
                if (t <= minTime) {
                    minTime = t;
                    eID = i;
                    queuePos = p;
                }

            }
            requests.get(eID).add(queuePos,r);
        }

        toDo.clear();
    }

    @Override
    public String toString(){
        StringBuilder inf= new StringBuilder();
        ArrayList<int[]> status = status();
        for (int i = 0; i < elevators.size(); i++){
            inf.append("Elevetor ID:");
            inf.append(status.get(i)[0]);
            inf.append(" is on ");
            inf.append(status.get(i)[1]);
            inf.append(" floor and is heading to ");
            inf.append(status.get(i)[2]);
            inf.append("\n");
        }
        return inf.toString();
    }

}
