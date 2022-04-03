package elevatorSystems.system;

import elevatorSystems.Elevator.Elevator;
import elevatorSystems.Elevator.ElevatorStatus;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ElevatorSystem{
    private final int numOfElevators;
    private ArrayList<Elevator> elevators;
    private ArrayList<ArrayList<Request>> requests;
    private ArrayList<Request> toDo;
    private final int lowestFloor;
    private final int highestFloor;


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

    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void pickup(int floor, int direction){
        Direction d = Direction.STOP;
        if (direction > 0) d = Direction.UP;
        if (direction < 0) d = Direction.DOWN;
        toDo.add(new Request(floor,d));
    }


    public void update(int ID,int currentFloor, int destination){
        elevators.get(ID).setCurrentFloor(currentFloor);
        elevators.get(ID).setDestination(destination);
    }

    public void assignment(){
        //przydziala wszystko z toDo
        //obejmuje algorytm przydziału
        //oblicza minimum po czasie oczekiwania przewidywanym na ten moment i tam przydziela
        //musi zmieniać statusy
        if (toDo.isEmpty()) return;

        //wersja trywialna
        for (Request r : toDo) {
            requests.get(0).add(r);
        }
        toDo.clear();
    }

    public void step(){
        for (int i = 0; i < elevators.size(); i++) {
            Elevator e = elevators.get(i);

            if (requests.get(i).isEmpty()) {
                e.move(Direction.STOP);
            }
            else {
                Request r = requests.get(i).get(0);

                if (e.getCurrentFloor() == r.getFloor() && r.getStatus() == RequestStatus.PICKUP) {
                    r.ordered(0); // tutaj powinno się ustawić faktyczną destynacje
                    requests.get(i).remove(0); // w wersji po dodaniu destynacji nie będzie usuwany
                    e.move(Direction.STOP);
                }

//            if (e.getCurrentFloor() == requests.get(i).get(0).getDestination()) //wykonano, usunąć dany request, sprawdzic nastepny w kolejnosci
            }

            if (!toDo.isEmpty()) assignment();

            if (requests.get(i).isEmpty()) {
                e.move(Direction.STOP);
                continue;
            }

            Request r = requests.get(i).get(0);

            if (r.getStatus() == RequestStatus.PICKUP){
                e.setDestination(r.getFloor());
                if (e.getCurrentFloor() < r.getFloor()) e.move(Direction.UP);
                if (e.getCurrentFloor() > r.getFloor()) e.move(Direction.DOWN);
            }
//            if (r.getStatus() == RequestStatus.ORDER){     //w wersji po dodaniu destynacji
//                if (e.getCurrentFloor() - r.getDestination() < 0) e.move(Direction.UP);
//                if (e.getCurrentFloor() - r.getDestination() > 0) e.move(Direction.DOWN);
//            }

        }
//        System.out.println(Arrays.toString(status().get(0)));
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
