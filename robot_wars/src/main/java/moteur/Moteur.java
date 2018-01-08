package moteur;

import plugins_basique.Robot_affichage_plugins;
import plugins_basique.Robot_attaque_plugins;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Moteur {
    private Robot r1;
    private Robot r2d2;

    public Moteur(Graphics g) throws InvocationTargetException, IllegalAccessException {
       r1=new Robot(new Point(10,10),g);
       r2d2 = new Robot(new Point(100,10),g);
        System.out.println(g+"test");
       this.implementsPlugins();
    }

    private void implementsPlugins() throws InvocationTargetException, IllegalAccessException {
        Robot_affichage_plugins graph= new Robot_affichage_plugins();
        Robot_attaque_plugins attaque= new Robot_attaque_plugins();
        r1.addPLuginsGraphisme(graph);
        r2d2.addPLuginsGraphisme(graph);
        r1.addPLuginsAttaque(attaque);
        r2d2.addPLuginsAttaque(attaque);
    }
}
