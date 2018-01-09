package plugins_basique;

import annotation.CalculDeplacement;
import annotation.Deplacement;
import annotation.Deplacer;

import java.awt.*;

@Deplacement
public class Robot_deplace_plugins {

    @Deplacer
    public Point deplacer(Graphics g,Point depart,int deplacementX,int deplacementY){
        Point retour;
        int x =(int) (Math.random() * (deplacementX));
        int y = ((int)Math.random() * (deplacementY));

        int alea = (int)Math.random();
        if(alea==0){
            retour=new Point((int)depart.getX()+x,(int)depart.getY()+y);
        }else{
            retour= new Point((int)depart.getX()-x,(int)depart.getY()-y);
        }
       return retour;
    }

    @CalculDeplacement
    public int calculEnergie(int deplacementX,int deplacementY){
        return deplacementX+deplacementY;
    }
}
