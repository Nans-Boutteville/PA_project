package moteur;

import plugins_basique.Robot_affichage_plugins;
import plugins_basique.Robot_attaque_plugins;
import plugins_basique.Robot_deplace_plugins;

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Moteur {
    private Robot r1;
    private Robot r2d2;
    private ArrayList<Class> classGraphique;
    private ArrayList<Class> classAttaque;
    private ArrayList<Class> classDeplacement;

    public Moteur() throws InvocationTargetException, IllegalAccessException {
        r1=new Robot(new Point(10,10));
        r2d2 = new Robot(new Point(300,10));
        Vue v = new Vue(r1,r2d2);
        r1.setGraph(v.getPanel());
        r2d2.setGraph(v.getPanel());
        this.implementsPlugins();
    }

    public Robot getR1() {
        return r1;
    }

    public Robot getR2d2() {
        return r2d2;
    }

    /**
     * Les deux IA jouent tour par tour jusqu'à la mort d'une des deux
     * @param player1
     * @param player2
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InterruptedException
     */
    public void run(IAsimple player1, IAsimple player2) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        while (r1.getVie() > 0 && r2d2.getVie() > 0) {
            r1.reinitialiseEnergie();
            r2d2.reinitialiseEnergie();
            Thread p1 = new Thread(player1);
            p1.run();
            p1.join();
            if (r2d2.getVie() > 0){
                Thread p2 = new Thread(player2);
                p2.run();
                p2.join();
            }
        }
    }

    private void implementsPlugins() throws InvocationTargetException, IllegalAccessException {
        this.implementsDefaultPlugins();
        this.classGraphique=new ArrayList<Class>();
        this.classAttaque=new ArrayList<Class>();
        this.classDeplacement=new ArrayList<Class>();
        this.implementsOtherPlugins();
    }

    private void implementsDefaultPlugins() throws InvocationTargetException, IllegalAccessException {
        Robot_affichage_plugins graph1= new Robot_affichage_plugins();
        Robot_attaque_plugins attaque1= new Robot_attaque_plugins();
        Robot_deplace_plugins deplace1 = new Robot_deplace_plugins();
        Robot_affichage_plugins graph2= new Robot_affichage_plugins();
        Robot_attaque_plugins attaque2= new Robot_attaque_plugins();
        Robot_deplace_plugins deplace2 = new Robot_deplace_plugins();
        r1.addPLuginsGraphisme(graph1);
        r2d2.addPLuginsGraphisme(graph2);
        r1.addPLuginsAttaque(attaque1);
        r2d2.addPLuginsAttaque(attaque2);
        r1.addPlunginsDeplacement(deplace1);
        r2d2.addPlunginsDeplacement(deplace2);
    }

    private void implementsOtherPlugins() throws InvocationTargetException, IllegalAccessException {
        //appel au classLoader qui retourne ArrayList<Class>
        //changer le allPlugins (new) par le retour du classLoader
        ArrayList<Class> allPlugins = new ArrayList<Class>(); // return classloader
        for(Class c:allPlugins){
            this.addClassWithAnnotation(c);
        }
        for(Class graphique : this.classGraphique){
            Object o1 = implementsClass(graphique);
            Object o2 = implementsClass(graphique);
            if(o1!=null && o2!=null){
                r1.addPLuginsGraphisme(o1);
                r2d2.addPLuginsGraphisme(o2);
            }
        }
        for(Class attaque : this.classAttaque){
            Object o1 = implementsClass(attaque);
            Object o2 = implementsClass(attaque);
            if(o1!=null && o2!=null){
                r1.addPLuginsAttaque(o1);
                r2d2.addPLuginsAttaque(o2);
            }
        }
        for(Class deplacement : this.classDeplacement){
            Object o1 = implementsClass(deplacement);
            Object o2 = implementsClass(deplacement);
            if(o1!=null && o2!=null){
                r1.addPlunginsDeplacement(o1);
                r2d2.addPlunginsDeplacement(o2);
            }
        }

    }

    private void addClassWithAnnotation(Class c){
        Annotation[] allAnnotations = c.getAnnotations();
        for(Annotation a : allAnnotations) {
            if(a.getClass().getName().equals("Graphique")){
                this.classGraphique.add(c);
            }else if(a.getClass().getName().equals("Attaque")){
                this.classAttaque.add(c);
            }else if(a.getClass().getName().equals("Deplacement")){
                this.classDeplacement.add(c);
            }
        }
    }

    private Object implementsClass(Class c){
        Object newObj = null;
        try {
            newObj =c.newInstance();
        } catch (InstantiationException e) {
            Constructor[] allCOnstrucotr = c.getConstructors();
            for(Constructor construc : allCOnstrucotr){
                if(construc.getParameterTypes().length==1){
                    Class parameter = construc.getParameterTypes()[0];
                    Object implemnents = this.compareClassToObjectGraphique(parameter);
                    if(implemnents!=null){
                        try {
                            newObj=construc.newInstance(implemnents);
                        } catch (InstantiationException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        } catch (InvocationTargetException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return newObj;
    }

    private Object compareClassToObjectGraphique(Class c){
        ArrayList<Object> allObjectGraphique = r1.getGraphisme();
        Object returnCompare = null;
        for(Object o : allObjectGraphique){
            if(o.getClass().getName().equals(c.getName())){
                returnCompare = o;
                break;
            }
        }
        return returnCompare;
    }

}
