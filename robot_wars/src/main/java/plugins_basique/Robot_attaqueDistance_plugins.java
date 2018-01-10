package plugins_basique;

import annotation.Attaque;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Attaque(perteEnergie = 30, perteVie = 5)
public class Robot_attaqueDistance_plugins {

    private int portee = 70;

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


        g.setColor(Color.RED);
        g.drawLine((int)p1.getX()+50,(int)p1.getY()+30, (int)p2.getX()+50,(int) p2.getY()+50);




        try {
            Image img = ImageIO.read(new File("images/explosion1.png"));
            g.drawImage(img,(int) p2.getX(),(int)p2.getY(),50,50,null);
        } catch (IOException e) {
            System.out.println("n'affiche pas l'attaque");
        }
        try {
            Image img = ImageIO.read(new File("images/explosion2.png"));
            g.drawImage(img,(int) p2.getX(),(int)p2.getY(),50,50,null);
        } catch (IOException e) {
            System.out.println("n'affiche pas l'attaque");
        }
        try {
            Image img = ImageIO.read(new File("images/explosion3.png"));
            g.drawImage(img,(int) p2.getX(),(int)p2.getY(),50,50,null);
        } catch (IOException e) {
            System.out.println("n'affiche pas l'attaque");
        }
        try {
            Image img = ImageIO.read(new File("images/explosion3.png"));
            g.drawImage(img,(int) p2.getX(),(int)p2.getY(),50,50,null);
        } catch (IOException e) {
            System.out.println("n'affiche pas l'attaque");
        }
    }
}
