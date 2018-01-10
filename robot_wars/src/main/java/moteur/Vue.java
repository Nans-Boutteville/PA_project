package moteur;

import annotation.Attaque;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Vue extends JPanel{
    JPanel panel;

    public Vue(ArrayList<Robot> robots){
        JFrame frame = new JFrame();
        this.panel = new VuePanel(robots);
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
