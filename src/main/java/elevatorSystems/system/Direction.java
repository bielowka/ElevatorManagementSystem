package elevatorSystems.system;

public enum Direction {
    UP,
    DOWN,
    STOP;

    public int toInt(){
        if (this == UP) return 1;
        if (this == DOWN) return -1;
        else return 0;
    }

    public static Direction intToDirection(int i){
        if (i > 0) return UP;
        if (i < 0) return DOWN;
        else return STOP;
    }
}
