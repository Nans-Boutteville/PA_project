package plugins_basique;

import annotation.Dessiner;
import annotation.Graphique;

import java.awt.*;

@Graphique
public class Robot_affichageBarreDEnergie_plugins {

    @Dessiner(argumentEnergie = 3)
    public void dessiner(Graphics g, Point p, int stamina){

        //String chaine= "Energie : "+stamina;
        //g.drawString(chaine, (int) p.getX(), (int) p.getY()+100);

        g.setColor(Color.ORANGE);
        g.fillRect((int)p.getX(), (int)p.getY() + 115, stamina, 10);


    }
}
