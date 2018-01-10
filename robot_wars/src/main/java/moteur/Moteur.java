package moteur;

import plugins_basique.Robot_affichage_plugins;
import plugins_basique.Robot_attaque_plugins;
import plugins_basique.Robot_deplace_plugins;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Moteur {
    private ArrayList<Robot> robots;
    private ArrayList<Class> classGraphique;
    private ArrayList<Class> classAttaque;
    private ArrayList<Class> classDeplacement;

    public Moteur(int nbRobot) throws InvocationTargetException, IllegalAccessException {
        int x=10;
        int y=10;
        this.robots=new ArrayList<Robot>();
        for(int i=0;i<nbRobot;i++){
            Robot r = new Robot(new Point(x,y));
            this.robots.add(r);
            x= (int) (Math.random() * 800);
            y= (int) (Math.random() * 800);
        }

        Vue v = new Vue(this.robots);
        for(Robot robot: this.robots){
            robot.setGraph(v.getPanel());
        }
        this.implementsPlugins();
    }

    public ArrayList<Robot> getRobots() {
        return this.robots;
    }


    /**
     * Les deux IA jouent tour par tour jusqu'à la mort d'une des deux
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InterruptedException
     */
    public void run(ArrayList<IAsimple> ias) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        boolean fin =false;
        while (!fin) {
            for(Robot r:robots){
                r.reinitialiseEnergie();
            }
            for(IAsimple ia : ias){
                if(ia.getVieRobot()>0){
                    Thread p1 = new Thread(ia);
                    p1.run();
                    p1.join();
                }else{
                    fin=true;
                    break;
                }
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
        for(Robot r : robots){
            Robot_affichage_plugins graph= new Robot_affichage_plugins();
            Robot_attaque_plugins attaque= new Robot_attaque_plugins();
            Robot_deplace_plugins deplace = new Robot_deplace_plugins();
            r.addPLuginsGraphisme(graph);
            r.addPLuginsAttaque(attaque);
            r.addPlunginsDeplacement(deplace);
        }
    }

    private void addGraphique(Class graphique) throws InvocationTargetException, IllegalAccessException {
        for(Robot r : robots){
            Object o = implementsClass(graphique,r);
            if(o!=null){
                r.addPLuginsGraphisme(o);
            }
        }
    }

    private void addAttaque(Class attaque){
        for(Robot r : robots){
            Object o = implementsClass(attaque,r);
            if(o!=null){
                r.addPLuginsAttaque(o);
            }
        }
    }

    private void addDeplacement(Class deplacement){
        for(Robot r : robots){
            Object o = implementsClass(deplacement,r);
            if(o!=null){
                r.addPlunginsDeplacement(o);
            }
        }
    }

    private void implementsOtherPlugins() throws InvocationTargetException, IllegalAccessException {
        //appel au classLoader qui retourne ArrayList<Class>
        //changer le allPlugins (new) par le retour du classLoader
        ArrayList<Class> allPlugins = new ArrayList<Class>(); // return classloader
        for(Class c:allPlugins){
            this.addClassWithAnnotation(c);
        }
        for(Class graphique : this.classGraphique){
            this.addGraphique(graphique);
        }
        for(Class attaque : this.classAttaque){
            this.addAttaque(attaque);
        }
        for(Class deplacement : this.classDeplacement){
            this.addDeplacement(deplacement);
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

    private Object implementsClass(Class c,Robot r){
        Object newObj = null;
        try {
            newObj =c.newInstance();
        } catch (InstantiationException e) {
            Constructor[] allCOnstrucotr = c.getConstructors();
            for(Constructor construc : allCOnstrucotr){
                if(construc.getParameterTypes().length==1){
                    Class parameter = construc.getParameterTypes()[0];
                    Object implemnents = this.compareClassToObjectGraphique(parameter,r);
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

    private Object compareClassToObjectGraphique(Class c,Robot r){
        ArrayList<Object> allObjectGraphique = r.getGraphisme();
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
