package plugins_basique;

import annotation.Dessiner;
import annotation.Graphique;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Graphique
public class Robot_affichageImage_plugins {

    @Dessiner
    public void dessiner(Graphics g, Point p){
        try {
            Image img = ImageIO.read(new File("images/evil-robot.png"));
            g.drawImage(img,(int)p.getX(),(int)p.getY(),100,100,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
