package moteur;

import annotation.Attaque;

import javax.swing.*;
import java.awt.*;


public class Vue extends JPanel{
    Graphics g;

    public Vue(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Robot Wars Ultimate !");
        frame.setSize(600,600);
        frame.setLocationRelativeTo(null);
        frame.add(panel, BorderLayout.CENTER );
        frame.setVisible(true);
        this.g = panel.getGraphics();
    }

    public Graphics getG() {
        return g;
    }
}
