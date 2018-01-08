package moteur;

import annotation.Attaque;

import javax.swing.*;
import java.awt.*;


public class Vue{
    Graphics g;

    public Vue(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Robot Wars Ultimate !");
        frame.setSize(600,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.g = panel.getGraphics();
        System.out.println(g);
    }

    public Graphics getG() {
        System.out.println(g);
        return g;
    }
}
