package elevatorSystems.GUI;

import elevatorSystems.system.ElevatorSystem;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.JavaBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;

public class Gui extends JPanel implements ActionListener, ChangeListener{
    private Timer timer;
    private int initDelay = 1000;
    private ElevatorSystem system;
    private JFrame frame;

    private JComponent scrolls;
    private ArrayList<JScrollBar> scrollsList;

    private final int numOfElevators;
    private final int lowestFloor;
    private final int highestFloor;

    private final Integer[] floors;

    private JPanel choice;
    private JComboBox<Integer> destinationChoice;
    private JLabel where;

    private PickingUpObserver observer;

    private ArrayList<Integer> orders;

    public Gui(JFrame frame,int numOfElevators,int lowestFloor,int highestFloor){
        this.numOfElevators = numOfElevators;
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        this.frame = frame;
        timer = new Timer(initDelay, this);
        timer.stop();

        this.scrollsList = new ArrayList<JScrollBar>();

        this.floors = new Integer[highestFloor-lowestFloor+1];
        for (int i = lowestFloor; i <= highestFloor; i++){
            floors[highestFloor - i] = i;
        }

        this.observer = new PickingUpObserver(this);

        this.orders = new ArrayList<>();
    }


    public void initialize(Container container){
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(numOfElevators*50+270,(highestFloor-lowestFloor+1)*50+100));

        system = new ElevatorSystem(numOfElevators,lowestFloor,highestFloor);
        system.setObserver((Observer) observer);

        int size = (int)( (float)((highestFloor-lowestFloor+1)*50 - 32) / (float) (highestFloor-lowestFloor+1) + 0.5);

        scrolls = new JPanel();
        scrolls.setSize(numOfElevators*50,(highestFloor-lowestFloor+1)*50);

        for (int i = 0; i < numOfElevators; i++) {
            JScrollBar s = new JScrollBar(Adjustable.VERTICAL,(highestFloor-lowestFloor)*50,50,0,(highestFloor-lowestFloor+1)*50);
            s.setBounds(i*50,0,40,(highestFloor-lowestFloor+1)*50);
            s.setUnitIncrement(50);
            s.setBlockIncrement(50);
            s.setValue(highestFloor*50);
            scrollsList.add(s);
            scrolls.add(s);
        }


        JPanel requesters = new JPanel();
        requesters.setSize(100,(highestFloor-lowestFloor+1)*50);

        for (int i = lowestFloor; i < highestFloor + 1; i++) {
            Requester r = new Requester(highestFloor - i + lowestFloor, this,size);
            r.setBounds(numOfElevators * 50, i * 50, 100, 50);
            requesters.add(r);
        }
        requesters.setBounds(numOfElevators*50,16,100,(highestFloor-lowestFloor+1)*50);

        requesters.setVisible(true);
        requesters.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
        container.add(requesters);


        scrolls.setVisible(true);
        scrolls.setLayout(new BorderLayout());
        container.add(scrolls);

        destinationChoice = new JComboBox<Integer>(this.floors);
        destinationChoice.setVisible(true);
        destinationChoice.addActionListener(this);
        destinationChoice.setActionCommand("choice");
        destinationChoice.setSize(75,200);

        where = new JLabel("");

        choice = new JPanel();
        choice.add(where);
        choice.add(destinationChoice);
        choice.setBounds(numOfElevators*50+50,0,50,200);
        choice.setLayout(new FlowLayout(FlowLayout.RIGHT));
        choice.setVisible(false);
        container.add(choice);

        JButton getInfo = new JButton("Print information");
        getInfo.setActionCommand("getinfo");
        getInfo.addActionListener(this);
        container.add(getInfo,BorderLayout.SOUTH);

        timer.start();
    }

    public void refresh(){
        ArrayList<int[]> status = system.status();
        for (int i = 0; i < status.size(); i++) {
            int[] e = status.get(i);
            scrollsList.get(i).setValue(highestFloor*50 - e[1]*50);
        }
    }

    public void destinationChoosing(int floor){
        timer.stop();
        orders.add(floor);
        choice.setVisible(true);
        where.setText("Ordering on: " + String.valueOf(orders.get(0)) + "f");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)) {
            system.step();
            this.refresh();
        }
        else {
            String command = e.getActionCommand();
            if (e.getSource() instanceof JButton){
                if (command.contains("UP")){
                    int floor = Integer.parseInt(command.replace("UP",""));
                    if (floor != highestFloor) system.pickup(floor,1);
                }
                else if (command.contains("DOWN")){
                    int floor = Integer.parseInt(command.replace("DOWN",""));
                    if (floor != lowestFloor) system.pickup(floor,-1);
                }
                else if (command.equals("getinfo")){
                    System.out.println(system);
                }
            }
            else if (command.equals("choice")){
                if(!orders.isEmpty()){
                    timer.stop();
                    where.setText("Ordering on: " + String.valueOf(orders.get(0)) + "f");
                    int f = orders.get(0);
                    int d = (Integer) destinationChoice.getSelectedItem();
                    system.deliver(f,d); //people who requested an elevator on the same floor do not know which one precisely, so they enter elevators randomly
                    orders.remove(0);
                }
                if (orders.isEmpty()){
                    choice.setVisible(false);
                    timer.start();
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
