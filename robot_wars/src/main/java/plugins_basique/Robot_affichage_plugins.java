package plugins_basique;

import annotation.Dessiner;
import annotation.Graphique;

import java.awt.*;
import java.util.Random;

@Graphique
public class Robot_affichage_plugins {
    private int taille=40;

    @Dessiner
    public void dessiner(Graphics g,Point p){
        Random rand = new Random();
        float r = rand.nextFloat();
        float gr = rand.nextFloat();
        float b = rand.nextFloat();
        Color c = g.getColor();
        g.setColor(new Color(r,gr,b));
        g.fillRect((int)p.getX()+taille/2,(int)p.getY()+taille/2,taille,taille);
        g.setColor(c);
    }
}
