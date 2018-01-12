package moteur;

import annotation.Attaque;
import annotation.CalculDeplacement;
import annotation.Deplacer;
import annotation.Dessiner;

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Robot {

    private final int EENERGIEBASE = 100;
    private final String annotationName = "annotation.";
    private final String annotationGraphiqueName = "Graphique";
    private final String annotationAttaqueName = "Attaque";
    private final String annotationDeplacementName = "Deplacement";
    private final String pointClassName = "java.awt.Point";
    private final String graphicsClassName = "java.awt.Graphics";
    private final String booleanClassName = "boolean";


    private Point point;
    private int vie;
    private int energie;

    private ArrayList<Object> graphisme;
    private ArrayList<Object> attaque;
    private ArrayList<Object> deplacement;

    private JPanel panel;

    /**
     * Constructeur par défault (met les coordonnées du robot en x=0, y=0)
     */
    public Robot() {
        this(new Point(0, 0));
    }

    /**
     * Constructeur
     *
     * @param point point ou va se trouve le Robot
     */
    public Robot(Point point) {
        this.point = point;
        this.vie = 100;
        this.energie = this.EENERGIEBASE;
        this.graphisme = new ArrayList<Object>();
        this.attaque = new ArrayList<Object>();
        this.deplacement = new ArrayList<Object>();
        this.panel = new JPanel();
    }

    /**
     * CHange le panel
     *
     * @param panel nouveau panel
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * récupère un tableau contenant tous les plugins possible pour faire des attaques
     *
     * @return un tableau contenant tous les plugins possible pour faire des attaques
     */
    public ArrayList<Object> getAttaque() {
        return this.attaque;
    }

    /**
     * récupère un tableau contenant tous les plugins possible pour faire des déplacements
     *
     * @return un tableau contenant tous les plugins possible pour faire des déplacements
     */
    public ArrayList<Object> getDeplacement() {
        return this.deplacement;
    }

    /**
     * récupère un tableau contenant tous les plugins possible pour afficher le robot
     *
     * @return un tableau contenant tous les plugins possible pour afficher le robot
     */
    public ArrayList<Object> getGraphisme() {
        return graphisme;
    }

    /**
     * Récupère les coordonnées du robot
     *
     * @return les coorodonnées du robot
     */
    public Point getCoordonnee() {
        return this.point;
    }

    /**
     * Récupère la vie du robot
     *
     * @return la vie du robot
     */
    public int getVie() {
        return this.vie;
    }

    /**
     * Récupère l'énergie du robot
     *
     * @return l'énergie du robot
     */
    public int getEnergie() {
        return this.energie;
    }

    /**
     * Ajout d'un nouveau plugins graphique
     *
     * @param plugins le nouveau plugins graphiques
     */
    public void addPLuginsGraphisme(Object plugins) {
        Annotation[] allAnotation = plugins.getClass().getAnnotations();
        for (Annotation ann : allAnotation) {
            if (ann.annotationType().getName().split(this.annotationName)[1].equals(this.annotationGraphiqueName)) {
                graphisme.add(plugins);
                this.panel.repaint();
                break;
            }
        }
    }

    /**
     * Ajout d'un nouveau plugins d'attaque
     *
     * @param plugins le nouveau plugins d'attaque
     */
    public void addPLuginsAttaque(Object plugins) {
        Annotation[] allAnotation = plugins.getClass().getAnnotations();
        for (Annotation ann : allAnotation) {
            if (ann.annotationType().getName().split(this.annotationName)[1].equals(this.annotationAttaqueName)) {
                this.attaque.add(plugins);
                this.panel.repaint();
                break;
            }
        }
    }

    /**
     * Ajout d'un nouveau plugins de déplacement
     *
     * @param plugins le nouveau plugins de déplacement
     */
    public void addPlunginsDeplacement(Object plugins) {
        Annotation[] allAnotation = plugins.getClass().getAnnotations();
        for (Annotation ann : allAnotation) {
            if (ann.annotationType().getName().split(this.annotationName)[1].equals(this.annotationDeplacementName)) {
                this.deplacement.add(plugins);
                this.panel.repaint();
                break;
            }
        }
    }

    /**
     * Reinitialise l'nergie du robot à celle de base
     */
    public void reinitialiseEnergie() {
        this.energie = this.EENERGIEBASE;
        this.panel.repaint();
    }

    /**
     * Permet au Robot de se Dessiner dans un graphique
     *
     * @param g Graphqiue dans lequel le robot doit s'afficher
     */
    public void seDessiner(Graphics g) {
        for (Object o : this.graphisme) {
            ArrayList<Method> allMethodsDessin = this.getMethodDessin(o.getClass().getMethods());
            this.appelMethodsDessin(allMethodsDessin, o, g);
        }
    }

    /**
     * Appel de toutes les methodes dessiner se trouvant dans un objet afin de l'afficher dans un graphqiue
     *
     * @param methods   toutes les méthodes dessiner à appeler
     * @param graphique Objet graphqiue ou se trouve toutes les méthodes à appeler
     * @param g         Graphqiue dans lequel le robot doit s'afficher
     */
    private void appelMethodsDessin(ArrayList<Method> methods, Object graphique, Graphics g) {
        for (Method m : methods) {
            this.dessiner(m, graphique, g);
        }
    }

    /**
     * Dessiner le robot en appelant une méthode spécifique se trouvant dans un plugins
     *
     * @param m Method a appeler pour déssiner le robot
     * @param o Plugins o ou se trouve la méthode
     * @param g Graphqiue dans lequel le robot doit s'afficher
     */
    private void dessiner(Method m, Object o, Graphics g) {
        if (m.getAnnotation(Dessiner.class) != null) {
            Dessiner dess = m.getAnnotation(Dessiner.class);
            if (!dess.animationAttaque() && !dess.animationMouvement()) {
                int numArgumentVie = dess.argumentVie();
                int numArguementEnergie = dess.argumentEnergie();
                this.dessinerWithVieEtEnergie(m, o, g, numArgumentVie, numArguementEnergie);
            }
        } else {
            invoke(m, o, g);
        }
    }

    /**
     * Dessiner un Robot en appelant une méthode qui demande la vie ou l'énergie
     *
     * @param m                   Method qui demande la vie ou l'énergie à appeler
     * @param o                   Plugins contenant la méthode à appeler
     * @param g                   Graphqiue dans lequel le robot doit s'afficher
     * @param numParametreVie     Numéro du paramètre, ou il se trouve dans la méthode à appeler
     * @param numParametreEnergie Numéro du paramètre, ou il se trouve dans la méthode à appeler
     */
    private void dessinerWithVieEtEnergie(Method m, Object o, Graphics g, int numParametreVie, int numParametreEnergie) {
        if (numParametreVie != -1 || numParametreEnergie != -1) {
            if (numParametreVie != -1) {
                if (numParametreEnergie != -1 && numParametreEnergie <= m.getParameterTypes().length && numParametreVie <= m.getParameterTypes().length) {
                    this.switchInvoke(m, o, g, numParametreVie, this.vie, numParametreEnergie, this.energie);
                } else if (numParametreVie <= m.getParameterTypes().length) {
                    this.switchInvoke(m, o, g, numParametreVie, vie);
                }
            } else if (numParametreEnergie != -1 && numParametreEnergie <= m.getParameterTypes().length) {
                this.switchInvoke(m, o, g, numParametreEnergie, energie);
            }
        } else {
            invoke(m, o, g);
        }
    }

    /**
     * Appelle une méthode qui a besoin que de deux paramètres, un point et un graphique
     *
     * @param m méthode qui a besoin que de deux paramètres
     * @param o plugins contenant la méthode a appeler
     * @param g Graphqiue que la méthode à besoin pour effectuer ses actions
     */
    private void invoke(Method m, Object o, Graphics g) {
        if (m.getParameterTypes().length == 2 && m.getParameterTypes()[0].getName().equals(this.pointClassName)) {
            this.invoke(m, o, this.point, g);
        } else if (m.getParameterTypes().length == 2) {
            this.invoke(m, o, g, this.point);
        }
    }

    /**
     * Invoque une méthode qui possède un objet arg en parmètre i (3 paramètre maximum dans la méthode)
     *
     * @param m   Method a appeler
     * @param o   Plugins contenant la méthode à appeler
     * @param g   Graphique que la méthode à besoin
     * @param i   numéro du paramètre ou se trouve l'objet arg
     * @param arg Objet que la méthode à appeler à besoin
     */
    private void switchInvoke(Method m, Object o, Graphics g, int i, Object arg) {
        if (m.getParameterTypes().length == 3) {
            Object[] args = new Object[3];
            int numParametre = 0;
            for (Class parameters : m.getParameterTypes()) {
                if (i == numParametre) {
                    args[numParametre] = arg;
                } else if (parameters.getName().equals(this.pointClassName)) {
                    args[numParametre] = this.point;
                } else if (parameters.getName().equals(this.graphicsClassName)) {
                    args[numParametre] = g;
                }
                numParametre++;
            }
            this.invoke(m, o, args[0], args[1], args[2]);
        }
    }

    /**
     * Invoque une méthode qui à besoin de deux arguments en plus que le graphique et le point du robot (4 paramètre maximum dans la méthode)
     *
     * @param m       Method a appeler
     * @param o       Plugins contenant la méthode à appeler
     * @param g       Graphique que la méthode à besoin
     * @param numarg1 numéro du paramètre ou se trouve l'objet arg1
     * @param arg1    Objet que la méthode à appeler à besoin
     * @param numarg2 numéro du paramètre ou se trouve l'objet arg2
     * @param arg2    Objet que la méthode à appeler à besoin
     */
    private void switchInvoke(Method m, Object o, Graphics g, int numarg1, Object arg1, int numarg2, Object arg2) {
        if (m.getParameterTypes().length == 4) {
            Object[] args = new Object[4];
            int numParametre = 0;
            for (Class parameters : m.getParameterTypes()) {
                if (numarg1 - 1 == numParametre) {
                    args[numParametre] = arg1;
                } else if (numarg2 - 1 == numParametre) {
                    args[numParametre] = arg2;
                }
                if (parameters.getName().equals(this.pointClassName)) {
                    args[numParametre] = this.point;
                } else if (parameters.getName().equals(this.graphicsClassName)) {
                    args[numParametre] = g;
                }
                numParametre++;
            }
            this.invoke(m, o, args[0], args[1], args[2], args[3]);
        }
    }

    /**
     * Invoque une méthode
     *
     * @param m    méthode a appeler
     * @param o    Plugins possédant la méthode à appeler
     * @param args args dont la méthode à besoin pour être appelé
     * @return Objet renvoyé par la méthode
     */
    private Object invoke(Method m, Object o, Object... args) {
        Object r = null;
        try {
            r = m.invoke(o, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * Récupère toutes les méthodes qui permètent à un robot de se déssiner
     *
     * @param allMethods toutes les méthodes disponible
     * @return un tableau contant toutes les méthodes permettant de dessiner un robot
     */
    private ArrayList<Method> getMethodDessin(Method[] allMethods) {
        ArrayList<Method> returnM = new ArrayList<Method>();
        for (int i = 0; i < allMethods.length; i++) {
            if (allMethods[i].getAnnotation(Dessiner.class) != null) {
                returnM.add(allMethods[i]);
            }
        }
        if (returnM.size() <= 0) {
            returnM = this.getDefaultMethodDessin(allMethods);
        }
        return returnM;
    }

    /**
     * Récupère toutes les méthodes par défault (donc qu'il n'y a aucune class ayant une annotation Dessiner) qui permètent à un robot de se déssiner
     *
     * @param allMethods toutes les méthodes disponible
     * @return un tableau contant toutes les méthodes par défault permettant de dessiner un robot
     */
    private ArrayList<Method> getDefaultMethodDessin(Method[] allMethods) {
        ArrayList<Method> returnM = new ArrayList<Method>();
        for (Method m : allMethods) {
            Class[] allParameters = m.getParameterTypes();
            if (allParameters.length == 2) {
                if ((allParameters[0].getName().equals(this.pointClassName) && allParameters[1].getName().equals(this.graphicsClassName)) || (allParameters[1].getName().equals(this.pointClassName) && allParameters[0].getName().equals(this.graphicsClassName))) {
                    returnM.add(m);
                }
            }
        }
        return returnM;
    }

    /**
     * Permet à un robot d'attaquer
     * @param plugins plugins avec lequel le robot va attaquer
     * @param r robot ennemis avec lequel on va essayer d'attaquer
     */
    public void attaque(Object plugins, Robot r) {
        if (this.attaque.contains(plugins)) {
            Class attaque = plugins.getClass();
            Method[] allMethods = attaque.getMethods();
            for (Method m : allMethods) {
                if (this.goodMethodAttack(m)) {
                    boolean bienAttaque = invokeMethodsAttack(m, plugins, this.panel.getGraphics(), r.getCoordonnee());
                    if (bienAttaque) {
                        Attaque attaqueA = (Attaque) attaque.getAnnotation(Attaque.class);
                        this.energie -= attaqueA.perteEnergie();
                        r.degats(attaqueA.perteVie());
                    }
                }
            }
        }
    }

    /**
     * Verifie si la méthode est une méthode d'attaque ou non
     * @param m La méthode à vérifier
     * @return true si c'est une méthode d'attaque false sinon
     */
    private boolean goodMethodAttack(Method m) {

        boolean goodMethodAttack = false;
        if (m.getParameterTypes().length == 3) {
            Object[] args = new Object[3];
            int numArg = 0;
            for (Class c : m.getParameterTypes()) {
                if (c.getName().equals(this.graphicsClassName)) {
                    args[numArg] = this.panel.getGraphics();
                } else if (c.getName().equals(this.pointClassName)) {
                    args[numArg] = this.point;
                }
                numArg++;
            }
            if (args[0] != null && args[1] != null && args[2] != null && m.getReturnType().getName().equals(this.booleanClassName)) {
                goodMethodAttack = true;
            }
        }
        return goodMethodAttack;
    }

    /**
     * Regarde si le robot peut attaquer avec le plugins contre un robot ennemis
     * @param plugins contenant les méthode d'attaque
     * @param r le robot ennemis
     * @return si le robot peut attaquer son ennemi ou non
     */
    public boolean peutAttaquer(Object plugins, Robot r) {
        boolean returnAttack = false;
        if (this.attaque.contains(plugins) && plugins.getClass().getAnnotation(Attaque.class).perteEnergie() <= this.energie) {
            Class attaque = plugins.getClass();
            Method[] allMethods = attaque.getMethods();
            for (Method m:allMethods) {
                    if (this.goodMethodAttack(m)) {
                        returnAttack = invokeMethodsAttack(m, plugins, this.panel.getGraphics(), r.getCoordonnee());
                    }
                }
            }
        return returnAttack;
    }

    /**
     * Quand le robot subit des degats
     * @param perteVie nombre de dégats reçus
     */
    public void degats(int perteVie) {
        this.vie -= perteVie;
        if (vie < 0) {
            vie = 0;
        }
        this.panel.repaint();
    }

    /**
     * Invoque une method d'attaque
     * @param m Methode à invoquer
     * @param o Plugins contenant la méthode à invoquer
     * @param g Graphics que la méthode à besoin
     * @param p Point du robot ennemi
     * @return si l'attaque a pu se faire ou non
     */
    private boolean invokeMethodsAttack(Method m, Object o, Graphics g, Point p) {
        boolean returnA = false;
        Object[] args = new Object[3];
        int increment =0;
        boolean pointRobotPlacer = false;
        Class[] allArgument = m.getParameterTypes();
        for (Class argument : allArgument) {
            if (argument.getName().equals(this.graphicsClassName)) {
                args[increment]=this.panel.getGraphics();
            } else if (argument.getName().equals(this.pointClassName)) {
                if (!pointRobotPlacer) {
                    pointRobotPlacer=true;
                    args[increment] = this.point;
                } else {
                    args[increment] = p;
                }
            }
            increment++;
        }
        if (this.goodMethodAttack(m)) {
            returnA = (Boolean) this.invoke(m,o,args[0],args[1],args[2]);
        }

        return returnA;
    }

    /**
     * Methode permettant au robot de se déplacer
     * @param plugins plugins a appeler pour se déplacer
     * @param deplacementX Sur conmbien de pixel sur X le robot doit se déplacer
     * @param deplacementY Sur combien de pixel su Y le robot doit se déplacer
     */
    public void seDeplacer(Object plugins, int deplacementX, int deplacementY) {
        if (this.deplacement.contains(plugins)) {
            Class deplacemen = plugins.getClass();
            this.invokeMethodDeplacement(deplacemen.getMethods(), plugins, deplacementX, deplacementY);
        }
    }

    /**
     * Invoque une méthode de déplacmeent
     * @param methods la méthode à appeler
     * @param o plugins contenant la méthode à appeler
     * @param deplacementX nombre de pixel pour le déplacment sur X
     * @param deplacementY nombre de pixel pour le déplacement sur Y
     */
    private void invokeMethodDeplacement(Method[] methods, Object o, int deplacementX, int deplacementY) {
        Method methodDeplacement = this.getMethodDeplacement(methods,deplacementX,deplacementY);

        if (methodDeplacement != null) {
            int coutEnergie = this.coutEnergieDeplacement(o, deplacementX, deplacementY);
            ArrayList<Object> args = getArgumentofDeplacementMethod(methodDeplacement, deplacementX, deplacementY);
            if (coutEnergie > -1 && args.size() == 4) {
                Point p = (Point) this.invoke(methodDeplacement, o, args.get(0), args.get(1), args.get(2), args.get(3));
                int largeur = this.panel.getWidth();
                int hauteur = this.panel.getHeight();
                int x = (int) p.getX();
                int y = (int) p.getY();
                if (p.getX() > largeur - 40) {
                    x = largeur - 40;
                } else if (p.getX() < 40) {
                    x = 40;
                }
                if (p.getY() > hauteur - 40) {
                    y = hauteur - 40;
                } else if (p.getY() < 40) {
                    y = 40;
                }
                this.point.setLocation(new Point(x, y));
                try {
                    Thread.currentThread().sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.energie -= coutEnergie;


            }

        }

    }

    /**
     * Récupéré une méthode de déplacement parmis une liste de ceux-ci
     * @param methods liste de méthdodes comprenant peut-être une méthode de dpélacement
     * @param deplacementX nombre de pixel pour le déplacment sur X
     * @param deplacementY nombre de pixel pour le déplacement sur Y
     * @return la méthode de déplacement
     */
    private Method getMethodDeplacement(Method[] methods,int deplacementX,int deplacementY){
        Method methodDeplacement = null;
        for (Method method : methods) {
            if (method.getAnnotation(Deplacer.class) != null && method.getReturnType().getName().equals(this.pointClassName)) {
                methodDeplacement = method;
                break;
            }
        }

        if (methodDeplacement == null) {
            for (Method method : methods) {
                if (this.getArgumentofDeplacementMethod(method, deplacementX, deplacementY).size() == 4 && method.getReturnType().getName().equals(this.pointClassName)) {
                    methodDeplacement = method;
                    break;
                }
            }
        }
        return methodDeplacement;
    }

    /**
     * Caclul d'un déplacement pour un robot (pour une IA plus complexe par exemple)
     * @param plugins plugins de déplacmeent à appeler
     * @param deplacementX nombre de pixel de dpélacement sur X
     * @param deplacementY nombre de pixel de déplacmenent sur Y
     * @return ce qui pourrait être le nouveau point du robot
     */
    public Point CalculPointDeplacement(Object plugins, int deplacementX, int deplacementY) {
        if(this.deplacement.contains(plugins)){
            Method[] methods = plugins.getClass().getMethods();
            Method methodDeplacement = null;
            for (Method method : methods) {
                if (method.getAnnotation(Deplacer.class) != null && method.getReturnType().getName().equals("java.awt.Point")) {
                    methodDeplacement = method;
                    break;
                }
            }

            if (methodDeplacement == null) {
                for (Method method : methods) {
                    if (this.getArgumentofDeplacementMethod(method, deplacementX, deplacementY).size() == 4 && method.getReturnType().getName().equals("java.awt.Point")) {
                        methodDeplacement = method;
                        break;
                    }
                }
            }
            if (methodDeplacement != null) {
                int coutEnergie = this.coutEnergieDeplacement(plugins, deplacementX, deplacementY);
                ArrayList<Object> args = getArgumentofDeplacementMethod(methodDeplacement, deplacementX, deplacementY);
                if (coutEnergie > -1 && args.size() == 4) {

                    return (Point) this.invoke(methodDeplacement, plugins, args.get(0), args.get(1), args.get(2), args.get(3));
                }

            }
        }
        return null;
    }

    /**
     * Récupère tous les arguments d'une méthode à invoquer
     * @param method la méthode ou on récupère tous les parmaètres à envoyer
     * @param deplacementX nombre de déplacement du robot sur X
     * @param deplacementY nombre de déplacement du robot sur Y
     * @return tableau contenant tus les arguments à mettre lors de l'appel à la méthode
     */
    private ArrayList<Object> getArgumentofDeplacementMethod(Method method, int deplacementX, int deplacementY) {
        ArrayList<Object> args = new ArrayList<Object>();
        Class[] allParameters = method.getParameterTypes();
        boolean deplacementXplacer = false;
        if (allParameters.length == 4) {
            for (Class parameters : allParameters) {
                if (parameters.getName().equals(this.graphicsClassName)) {
                    args.add(this.panel.getGraphics());
                } else if (parameters.getName().equals(this.pointClassName)) {
                    args.add(this.point);
                } else if (parameters.getName().equals("int")) {
                    int deplacement;
                    if (deplacementXplacer) {
                        deplacement = deplacementX;
                        deplacementXplacer = true;
                    } else {
                        deplacement = deplacementY;
                    }
                    args.add(deplacement);
                }
            }
        }
        return args;
    }

    /**
     * Calcul le cout d'énergie pour un déplacement
     * @param plugins plugins a appeler pour le déplacement
     * @param deplacementX nombre de déplacement du robot sur X
     * @param deplacementY nombre de déplacement du robot sur Y
     * @return cout de l'energie si on utilice ce plugins
     */
    public int coutEnergieDeplacement(Object plugins, int deplacementX, int deplacementY) {
        if (this.deplacement.contains(plugins)) {
            Class deplacemen = plugins.getClass();
            return this.invokeMethodDeplacementCoutEnergie(deplacemen.getMethods(), plugins, deplacementX, deplacementY);

        }
        return -1;
    }

    /**
     * Invoque Method de cour d'energie
     * @param methods toutes les methodes qui peuvent (ou non) calculer le cout de l'energie
     * @param o plugins contenant toutes les méthodes de clacul de déplacmeent
     * @param deplacementX nombre de déplacement du robot sur X
     * @param deplacementY nombre de déplacement du robot sur Y
     * @return cout de l'energie si on utilice ce plugins
     */
    private int invokeMethodDeplacementCoutEnergie(Method[] methods, Object o, int deplacementX, int deplacementY) {
        Method methodDeplacement = null;
        for (Method method : methods) {
            if (method.getAnnotation(CalculDeplacement.class) != null && method.getReturnType().getName().equals("int")) {
                methodDeplacement = method;
                break;
            }
        }

        if (methodDeplacement == null) {
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].getName().equals("int") && method.getReturnType().getName().equals("int")) {
                    methodDeplacement = method;
                    break;
                }
            }
        }
        if (methodDeplacement != null) {
            return (Integer) this.invoke(methodDeplacement, o, deplacementX, deplacementY);
        }
        return -1;
    }


}
