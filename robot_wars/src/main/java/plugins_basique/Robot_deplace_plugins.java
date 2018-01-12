package plugins_basique;

import annotation.CalculDeplacement;
import annotation.Deplacement;
import annotation.Deplacer;

import java.awt.*;
import java.util.Random;

@Deplacement
public class Robot_deplace_plugins {

    @Deplacer
    public Point deplacer(Graphics g,Point depart,int deplacementX,int deplacementY){
        Point retour;
        int x =deplacementX;
        int y = deplacementY;

        Random random = new Random();
        boolean alea= random.nextBoolean();
        boolean alea2 = random.nextBoolean();
        if(alea){
            if (alea2){
                retour=new Point((int)depart.getX()+x,(int)depart.getY()+y);
            } else {
                retour=new Point((int)depart.getX()+x,(int)depart.getY()-y);
            }

        }else{
            if(alea2){
                retour= new Point((int)depart.getX()-x,(int)depart.getY()+y);
            } else{
                retour= new Point((int)depart.getX()-x,(int)depart.getY()-y);
            }

        }

       return retour;
    }

    @CalculDeplacement
    public int calculEnergie(int deplacementX,int deplacementY){
        int retourEnergie=deplacementX;
        if(deplacementY<deplacementX){
            retourEnergie=deplacementY;
        }
        return retourEnergie;
    }
}
