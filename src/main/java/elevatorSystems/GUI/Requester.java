package elevatorSystems.GUI;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Requester extends JPanel{
    private JButton UP;
    private JButton DOWN;
    private final int floor;
    private ActionListener gui;
    private final int buttonSize;

    public Requester(int floor, ActionListener gui, int buttonSize){
        this.buttonSize = buttonSize;
        this.gui = gui;
        this.floor = floor;
        JButton UP = new JButton("^");
        UP.setPreferredSize(new Dimension(buttonSize, buttonSize));
        UP.setActionCommand("UP"+Integer.toString(floor));
        UP.addActionListener(gui);

        JButton DOWN = new JButton("v");
        DOWN.setPreferredSize(new Dimension(buttonSize, buttonSize));
        DOWN.setActionCommand("DOWN"+Integer.toString(floor));
        DOWN.addActionListener(gui);

        this.add(UP);
        this.add(DOWN);

        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        this.setPreferredSize(new Dimension(100, buttonSize));

    }
}
