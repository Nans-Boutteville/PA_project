package plugins_basique;

import annotation.Dessiner;

import java.awt.*;

public class Robot_affichageBarreDEnergie_plugins {

    @Dessiner(argumentEnergie = 3)
    public void dessiner(Graphics g, Point p, int stamina){
        //JLabel vie = new JLabel("");
        // vie.setOpaque(true);
        //vie.setBounds(new Rectangle((int)point.getX(), (int)point.getY() + 90, life, 10));
        // vie.setBackground(Color.green);
        String chaine= "Energie : "+stamina;
        g.drawString(chaine, (int) p.getX(), (int) p.getY()+100);

    }
}
