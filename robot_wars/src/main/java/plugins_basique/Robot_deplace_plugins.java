package plugins_basique;

import annotation.CalculDeplacement;
import annotation.Deplacement;
import annotation.Deplacer;

import java.awt.*;

@Deplacement
public class Robot_deplace_plugins {

    @Deplacer
    public Point deplacer(Graphics g,Point depart,int deplacement){
        Point retour;
        int x =(int) Math.random() * (deplacement);
        int y = (int)Math.random() * (deplacement-x);

        int alea = (int)Math.random();
        if(alea==0){
            retour=new Point((int)depart.getX()+x,(int)depart.getY()+y);
        }else{
            retour= new Point((int)depart.getX()-x,(int)depart.getY()-y);
        }
       return retour;
    }

    @CalculDeplacement
    public int calculEnergie(Graphics g,int deplacement){
        return deplacement;
    }
}
