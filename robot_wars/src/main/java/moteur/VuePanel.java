package moteur;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VuePanel extends JPanel {
    private ArrayList<Robot> robots;

    public VuePanel(ArrayList<Robot> robots){
        super();
        this.robots= robots;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
            for(Robot robot : this.robots){
                robot.seDessiner(g);
            }
    }

}
