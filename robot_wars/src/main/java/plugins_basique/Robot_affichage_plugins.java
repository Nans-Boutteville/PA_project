package plugins_basique;

import annotation.Dessiner;
import annotation.Graphique;


import java.awt.*;
import java.util.Random;

//import pour l'image
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Graphique
public class Robot_affichage_plugins {
    private int taille=40;
    private Color c;

    public Robot_affichage_plugins(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float gr = rand.nextFloat();
        float b = rand.nextFloat();
         c =new Color(r,gr,b);
    }

    @Dessiner(argumentVie = 3, argumentEnergie = 4)
    public void dessiner(Graphics g,Point p, int life, int stamina){
        //Robot_affichage_plugins
       /* Color base = g.getColor();
        g.setColor(this.c);
        g.fillRect((int)p.getX()+taille/2,(int)p.getY()+taille/2,taille,taille);
        g.setColor(base);*/

       //Code de test en dessous à supprimer quand les autres plugins pourront être chargés

        //Robot_affichageImage

        try {
            Image img = ImageIO.read(new File("images/evil-robot.png"));
            g.drawImage(img,(int)p.getX(),(int)p.getY(),100,100,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Robot_affichageBarreDeVie
       // String chaine= "Vie : "+life;
       // g.drawString(chaine, (int) p.getX(), (int) p.getY()+100);

        g.setColor(Color.GREEN);
        g.fillRect((int)p.getX(), (int)p.getY() + 100, life, 10);


        //Robot_affichageBarreDEnergie
        //String chaine2= "Energie : "+stamina;
        //g.drawString(chaine2, (int) p.getX(), (int) p.getY()+110);

        g.setColor(Color.ORANGE);
        g.fillRect((int)p.getX(), (int)p.getY() + 115, stamina, 10);


    }
}
