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

        //String chaine= "Vie : "+life;
        //g.drawString(chaine, (int) p.getX(), (int) p.getY()+90);

        g.setColor(Color.GREEN);
        g.fillRect((int)p.getX(), (int)p.getY() + 100, life, 10);

    }
}
