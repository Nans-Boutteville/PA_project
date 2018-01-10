package moteur;

import annotation.Attaque;

import javax.swing.*;
import java.awt.*;


public class Vue extends JPanel{
    JPanel panel;

    public Vue(Robot r1, Robot r2){
        JFrame frame = new JFrame();
        this.panel = new VuePanel(r1,r2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Robot Wars Ultimate !");
        frame.setSize(800,800);
        frame.setLocationRelativeTo(null);
        frame.add(panel, BorderLayout.CENTER );
        frame.setVisible(true);;
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
