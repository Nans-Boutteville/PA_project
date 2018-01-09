package moteur;

import plugins_basique.Robot_affichage_plugins;
import plugins_basique.Robot_attaque_plugins;

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

    public Moteur(Graphics g) throws InvocationTargetException, IllegalAccessException {
        r1=new Robot(new Point(10,10),g);
        r2d2 = new Robot(new Point(300,10),g);
        this.implementsPlugins();
    }

    private void implementsPlugins() throws InvocationTargetException, IllegalAccessException {
        this.implementsDefaultPlugins();
        this.classGraphique=new ArrayList<Class>();
        this.classAttaque=new ArrayList<Class>();
        this.classDeplacement=new ArrayList<Class>();
        this.implementsOtherPlugins();
    }

    private void implementsDefaultPlugins() throws InvocationTargetException, IllegalAccessException {
        Robot_affichage_plugins graph= new Robot_affichage_plugins();
        Robot_attaque_plugins attaque= new Robot_attaque_plugins();
        r1.addPLuginsGraphisme(graph);
        r2d2.addPLuginsGraphisme(graph);
        r1.addPLuginsAttaque(attaque);
        r2d2.addPLuginsAttaque(attaque);
    }

    private void implementsOtherPlugins() throws InvocationTargetException, IllegalAccessException {
        //appel au classLoader qui retourne ArrayList<Class>
        //changer le allPlugins (new) par le retour du classLoader
        ArrayList<Class> allPlugins = new ArrayList<Class>(); // return classloader
        for(Class c:allPlugins){
            this.addClassWithAnnotation(c);
        }
        for(Class graphique : this.classGraphique){
            Object o = implementsClass(graphique);
            if(o!=null){
                r1.addPLuginsGraphisme(o);
                r2d2.addPLuginsGraphisme(o);
            }
        }
        for(Class attaque : this.classAttaque){
            Object o = implementsClass(attaque);
            if(o!=null){
                r1.addPLuginsAttaque(o);
                r2d2.addPLuginsAttaque(o);
            }
        }
        for(Class deplacement : this.classDeplacement){
            Object o = implementsClass(deplacement);
            if(o!=null){
                r1.addPlunginsDeplacement(o);
                r2d2.addPlunginsDeplacement(o);
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
