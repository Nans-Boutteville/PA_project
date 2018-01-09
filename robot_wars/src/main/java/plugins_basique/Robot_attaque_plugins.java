package plugins_basique;

import annotation.Attaque;

import java.awt.*;

@Attaque(perteEnergie = 10, perteVie = 5)
public class Robot_attaque_plugins {
    private int portee = 10;

    public boolean attaque(Point p1, Point p2, Graphics g) {
        boolean returnF = false;
        if (p1.getX() > p2.getX()) {
            if (p1.getX() - p2.getX() < portee) {
                returnF = true;
            }
        }else{
            if (p2.getX() - p1.getX() < portee) {
                returnF = true;
            }
        }
        if (returnF) {
            annimation(p1,p2,g);
        }
        return returnF;
    }

    public void annimation(Point p1,Point p2,Graphics g){
        //fonction qui fait l'annimation entre les deux robots
    }
}
