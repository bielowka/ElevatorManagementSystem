package elevatorSystems;

import elevatorSystems.GUI.Gui;
import elevatorSystems.system.Direction;
import elevatorSystems.system.ElevatorSystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JFrame {
    private Gui gui;
    public Main(int numOfElevators, int lowestFloor, int highestFloor){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui = new Gui(this,numOfElevators,lowestFloor,highestFloor);
        gui.initialize(this.getContentPane());
        this.setSize(numOfElevators*50+270,(highestFloor-lowestFloor+1)*50+100);
        this.setVisible(true);

    }

    public static void main(String[] args) {
        Main main = new Main(4,-2,8);
        main.gui.initialize(main.getContentPane());
    }
}
