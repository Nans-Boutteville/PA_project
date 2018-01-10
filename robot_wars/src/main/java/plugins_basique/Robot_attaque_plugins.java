package plugins_basique;

import annotation.Attaque;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Attaque(perteEnergie = 10, perteVie = 5)
public class Robot_attaque_plugins {
    private int portee = 100;

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
        double newX=0;
        double newY=0;
        newX=(p1.getX()+p2.getX())/2;
        newY=(p1.getY()+p2.getY())/2;

        try {
            Image img = ImageIO.read(new File("images/explosion3.png"));
            g.drawImage(img,(int) newX,(int)newY,50,50,null);
        } catch (IOException e) {
            System.out.println("n'affiche pas l'attaque");
        }
    }
}
