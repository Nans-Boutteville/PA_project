package moteur;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class VuePanel extends JPanel {
    private Robot r1;
    private Robot r2;

    public VuePanel(Robot r1, Robot r2){
        super();
        this.r1=r1;
        this.r2=r2;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            r1.seDessiner(g);
            r2.seDessiner(g);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
