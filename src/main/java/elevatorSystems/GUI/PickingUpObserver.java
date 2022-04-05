package elevatorSystems.GUI;

import elevatorSystems.system.Direction;
import elevatorSystems.system.Request;

import java.util.Observable;
import java.util.Observer;

public class PickingUpObserver implements Observer {
    private final Gui gui;
    public PickingUpObserver(Gui gui){
        this.gui = gui;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.gui.destinationChoosing(((Request)arg).getFloor());
    }
}
