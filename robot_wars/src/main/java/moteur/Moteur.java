package moteur;

import classloader.MyClassLoader;
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


    /**
     * Constructor
     * @param nbRobot nombre de robot à créer
     */
    public Moteur(int nbRobot) throws ClassNotFoundException {
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
            robot.setPanel(v.getPanel());
        }
        this.implementsPlugins();
    }

    /**
     * Récupéré tous les robots disponible
     * @return tous les robots disponible
     */
    public ArrayList<Robot> getRobots() {
        return this.robots;
    }

    /**
     * Les IA jouent tour par tour jsuqu'à qu'il reste un survivant
     * @param ias Tableau contenant toutes les IA
     */
    public void run(ArrayList<IAsimple> ias) {
        boolean fin =false;
        ArrayList<Robot> rob = (ArrayList<Robot>) this.getRobots().clone();
        while (!fin) {
            reinitialiseEnergieAllRobots();
            fin = this.playAllIA(ias,rob);
        }
    }

    /**
     * Fait jouer tous les IA et renvois à la fin si il ne reste qu'un robot vivant ou non
     * @param ias tableau contenant tous les IA disponible
     * @param allPlayRobots tableau contant tous les robots qui sont encore vivant
     * @return true si il ne reste qu'un robot vivant
     */
    private boolean playAllIA(ArrayList<IAsimple> ias,ArrayList<Robot> allPlayRobots){
        boolean fin =false;
            for(IAsimple ia : ias){
                fin = playIA(ia,allPlayRobots,ias);
            }
        return fin;
    }

    /**
     * Fait jouer un IA et retourne si il ne reste qu'un robot vivant
     * @param ia IA qui doit jouer
     * @param allPlayRobots tableau contenant tous les robots qui sont encore vivant
     * @param ias tableau contenat tous les IA disponible
     * @return true si il ne reste qu'un robot vivant
     */
    private boolean playIA(IAsimple ia, ArrayList<Robot> allPlayRobots,ArrayList<IAsimple> ias){
        boolean fin = false;
        try {
            if(ia.getVieRobot()>0){
                Thread p1 = new Thread(ia);
                p1.run();
                p1.join();
            }else{
                allPlayRobots.remove(ia.getRobot());
                this.removeEnnemi(ias, ia);
                if(allPlayRobots.size() == 1){
                    fin=true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fin;
    }

    /**
     * Reinitialise l'énergie de tout les robots
     */
    private void reinitialiseEnergieAllRobots(){
        for(Robot r:robots){
            r.reinitialiseEnergie();
        }
    }

    /**
     * enleve un ennemi mort dans toutes les autres IA pour plus qu'il n'attaque celui-ci
     * @param arrayIA tableau contenant tous les IA disponible
     * @param ia IA dont l'ennemi est mort
     */
    private void removeEnnemi (ArrayList<IAsimple> arrayIA,IAsimple ia){
        for(IAsimple iasimple : arrayIA){
            if (!iasimple.equals(ia)){
                iasimple.removeEnnemi(ia.getRobot());
            }
        }
    }

    /**
     * implemente tous les plugins dans les robots
     */
    private void implementsPlugins() throws ClassNotFoundException {
        this.implementsDefaultPlugins();
        this.classGraphique=new ArrayList<Class>();
        this.classAttaque=new ArrayList<Class>();
        this.classDeplacement=new ArrayList<Class>();
        this.implementsOtherPlugins();
    }

    /**
     * implemente tous les plugins par défault dans les robots
     */
    private void implementsDefaultPlugins(){
        for(Robot r : robots){
            Robot_affichage_plugins graph= new Robot_affichage_plugins();
            Robot_attaque_plugins attaque= new Robot_attaque_plugins();
            Robot_deplace_plugins deplace = new Robot_deplace_plugins();
            r.addPLuginsGraphisme(graph);
            r.addPLuginsAttaque(attaque);
            r.addPlunginsDeplacement(deplace);
        }
    }

    /**
     * add un plugins graphique a tous les robots
     * @param graphique plugins graphique
     */
    private void addGraphique(Class graphique)  {
        for(Robot r : robots){
            Object o = implementsClass(graphique,r);
            if(o!=null){
                r.addPLuginsGraphisme(o);
            }
        }
    }

    /**
     * add un plugins attaque a tous les robots
     * @param attaque plugins attaque
     */
    private void addAttaque(Class attaque){
        for(Robot r : robots){
            Object o = implementsClass(attaque,r);
            if(o!=null){
                r.addPLuginsAttaque(o);
            }
        }
    }

    /**
     * add un plugins deplacement a tous les robots
     * @param deplacement plugins deplacement
     */
    private void addDeplacement(Class deplacement){
        for(Robot r : robots){
            Object o = implementsClass(deplacement,r);
            if(o!=null){
                r.addPlunginsDeplacement(o);
            }
        }
    }

    /**
     * Implmente tous les plugins se trouvant dans un dossier distant
     */
    private void implementsOtherPlugins() throws ClassNotFoundException {
        //appel au classLoader qui retourne ArrayList<Class>
        //MyClassLoader Loader = new MyClassLoader();
        //ArrayList<Class> allPlugins =Loader.loadingJar();
        ArrayList<Class> allPlugins = new ArrayList<Class>();
        /*System.out.println("nbAutrePlugins : "+allPlugins.size());
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
        }*/

    }

    /**
     * Implement une class qui possède une anotation
     * @param c class qui possède peut-être une annotation
     */
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

    /**
     * Implemente une class avec son constructeur
     * @param c la class a implémenter
     * @param r le robot qui possède les information qui peuvent aider à implémenter l'objet
     * @return l'objet qui correpond à l'implémentation de la class demandé
     */
    private Object implementsClass(Class c,Robot r){
        Object newObj = null;
        try {
            newObj =c.newInstance();
        } catch (InstantiationException e) {
            newObj=this.implementsClassWithOtherClass(c,r);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return newObj;
    }

    /**
     * Implemente une CLass qui a besoin d'une autre Class en argument
     * @param c Class qui a besoin d'une autre Class en argument
     * @param r Robot qui possèdent toutes les Class qui peuvent permettre d'instancier l'Objet
     * @return l'objet construit si il y'a eu une erreur renvois null
     */
    private Object implementsClassWithOtherClass(Class c,Robot r){
        Object newObj=null;
        Constructor[] allCOnstrucotr = c.getConstructors();
        for(Constructor construc : allCOnstrucotr){
            if(construc.getParameterTypes().length==1){
                Class parameter = construc.getParameterTypes()[0];
                Object implemnents = this.compareClassToObjectGraphique(parameter,r);
                if(implemnents!=null){
                    try {
                        newObj=construc.newInstance(implemnents);
                        break;
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
        return newObj;
    }

    /**
     * Compare une class à tous les objets graphiques d'un robot pour l'implémenter
     * @param c Class a comparé
     * @param r le robot qui possède tous les objets graphiques
     * @return
     */
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
