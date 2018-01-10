package plugins_basique;

import annotation.Dessiner;
import annotation.Graphique;

import javax.swing.*;
import java.awt.*;

/**
 * Permet l'affichage de la vie du robot
 */
@Graphique
public class Robot_affichageBarreDeVie_plugins {

    @Dessiner(argumentVie = 3)
    public void dessiner(Graphics g, Point p, int life){
        //JLabel vie = new JLabel("");
       // vie.setOpaque(true);
        //vie.setBounds(new Rectangle((int)point.getX(), (int)point.getY() + 90, life, 10));
       // vie.setBackground(Color.green);
        String chaine= "Vie : "+life;
        g.drawString(chaine, (int) p.getX(), (int) p.getY()+90);

    }
}
