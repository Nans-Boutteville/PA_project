package moteur;

import annotation.Attaque;
import annotation.Dessiner;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Robot {

    private static final int EENERGIEBASE = 100;

    private Point point;
    private int vie;
    private int energie;

    private ArrayList<Object> graphisme;
    private ArrayList<Object> attaque;
    private ArrayList<Object> deplacement;

    public Robot() {
        this(new Point(0, 0));
    }

    public Robot(Point point) {
        this.point = point;
        this.vie = 100;
        this.energie = this.EENERGIEBASE;
        this.attaque = new ArrayList<Object>();
        this.deplacement = new ArrayList<Object>();
    }

    public ArrayList<Object> getAttaque() {
        return this.attaque;
    }

    public ArrayList<Object> getDeplacement() {
        return this.deplacement;
    }

    public void addPLuginsGraphisme(Object graph) {
        graphisme.add(graph);
    }

    public void addPLuginsAttaque(Object attaque) { this.attaque.add(attaque); }

    public void addPlunginsDeplacement(Object deplacement) {this.deplacement.add(deplacement); }

    public Point getCoordonnee() {
        return this.point;
    }

    public int getVie() {
        return this.vie;
    }

    public int getEnergie() {
        return this.energie;
    }

    public void reinitialiseEnergie() {
        this.energie = this.EENERGIEBASE;
        ;
    }

    //Les différentes actions disponibles

    public void seDessiner(Graphics g) throws InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < this.graphisme.size(); i++) {
            ArrayList<Method> allMethodsDessin = this.getMethodDessin(this.graphisme.get(i).getClass().getMethods());
            for (int j = 0; j < allMethodsDessin.size(); j++) {
                this.dessiner(allMethodsDessin.get(j), this.graphisme.get(i), g);
            }

        }
    }

    private void dessiner(Method m, Object o, Graphics g) throws InvocationTargetException, IllegalAccessException {
        if (m.getAnnotation(Dessiner.class) != null) {
            Dessiner dess = (Dessiner) m.getAnnotation(Dessiner.class);
            if (!dess.animationAttaque() && !dess.animationMouvement()) {
                int numArgumentVie = dess.argumentVie();
                int numArguementEnergie = dess.argumentEnergie();
                if (numArgumentVie != -1 || numArguementEnergie != -1) {
                    if (numArgumentVie != -1) {
                        if (numArguementEnergie != -1 && numArguementEnergie <= m.getParameterTypes().length && numArgumentVie <= m.getParameterTypes().length) {
                            this.switchInvoke(m, o, g, numArgumentVie, this.vie, numArguementEnergie, this.energie);
                        } else if (numArgumentVie <= m.getParameterTypes().length) {
                            this.switchInvoke(m, o, g, numArgumentVie, vie);
                        }
                    } else if (numArguementEnergie != -1 && numArguementEnergie <= m.getParameterTypes().length) {
                        this.switchInvoke(m, o, g, numArguementEnergie, energie);
                    }
                } else {
                    invoke(m, o, this.point, g);
                }
            }
        } else {
            invoke(m, o, this.point, g);
        }
    }

    private void invoke(Method m, Object o, Point p, Graphics g) throws InvocationTargetException, IllegalAccessException {
        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
            this.invoke(m, o, (Object) this.point, (Object) g);
        } else {
            this.invoke(m, o, (Object) g, (Object) this.point);
        }
    }

    private void switchInvoke(Method m, Object o, Graphics g, int switchElement, Object arg) throws InvocationTargetException, IllegalAccessException {
        switch (switchElement) {
            case 1:
                if (m.getParameterTypes()[1].getName().equals("java.awt.Point")) {
                    this.invoke(m, o, arg, this.point, g);
                } else {
                    this.invoke(m, o, arg, g, this.point);
                }
                break;
            case 2:
                if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                    this.invoke(m, o, this.point, energie, g);
                } else {
                    this.invoke(m, o, g, arg, this.point);
                }
                break;
            default:
                if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                    m.invoke(o, this.point, arg, g);
                } else {
                    m.invoke(o, g, arg, this.point);
                }
                break;
        }
    }

    private void switchInvoke(Method m, Object o, Graphics g, int switchElement1, Object arg1, int switchElement2, Object arg2) throws InvocationTargetException, IllegalAccessException {
        switch (switchElement1) {
            case 1:
                switch (switchElement2) {
                    case 2:
                        if (m.getParameterTypes()[2].getName().equals("java.awt.Point")) {
                            m.invoke(o, arg1, arg2, this.point, g);
                        } else {
                            m.invoke(o, arg1, arg2, g, this.point);
                        }
                        break;
                    case 3:
                        if (m.getParameterTypes()[1].getName().equals("java.awt.Point")) {
                            m.invoke(o, arg1, this.point, arg2, g);
                        } else {
                            m.invoke(o, arg1, g, arg2, this.point);
                        }
                        break;
                    default:
                        if (m.getParameterTypes()[1].getName().equals("java.awt.Point")) {
                            m.invoke(o, arg1, this.point, g, arg2);
                        } else {
                            m.invoke(o, arg1, g, this.point, arg2);
                        }
                        break;
                }
                break;
            case 2:
                switch (switchElement2) {
                    case 1:
                        if (m.getParameterTypes()[2].getName().equals("java.awt.Point")) {
                            m.invoke(o, arg2, arg1, this.point, g);
                        } else {
                            m.invoke(o, arg2, arg1, g, this.point);
                        }
                        break;
                    case 3:
                        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                            m.invoke(o, this.point, arg1, arg2, g);
                        } else {
                            m.invoke(o, g, arg1, arg2, this.point);
                        }
                        break;
                    default:
                        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                            m.invoke(o, this.point, arg1, g, arg2);
                        } else {
                            m.invoke(o, g, arg1, this.point, arg2);
                        }
                        break;
                }
                break;
            case 3:
                switch (switchElement2) {
                    case 1:
                        if (m.getParameterTypes()[1].getName().equals("java.awt.Point")) {
                            m.invoke(o, arg2, this.point, arg1, g);
                        } else {
                            m.invoke(o, arg2, g, arg1, this.point);
                        }
                        break;
                    case 2:
                        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                            m.invoke(o, this.point, arg2, arg1, g);
                        } else {
                            m.invoke(o, g, arg2, arg1, this.point);
                        }
                        break;
                    default:
                        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                            m.invoke(o, this.point, g, arg1, arg2);
                        } else {
                            m.invoke(o, g, this.point, arg1, arg2);
                        }
                        break;
                }
                break;
            default:
                switch (switchElement2) {
                    case 1:
                        if (m.getParameterTypes()[1].getName().equals("java.awt.Point")) {
                            m.invoke(o, arg2, this.point, g, arg1);
                        } else {
                            m.invoke(o, arg2, g, this.point, arg1);
                        }
                        break;
                    case 2:
                        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                            m.invoke(o, this.point, arg2, g, arg1);
                        } else {
                            m.invoke(o, g, arg2, this.point, arg1);
                        }
                        break;
                    default:
                        if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                            m.invoke(o, this.point, g, arg2, arg1);
                        } else {
                            m.invoke(o, g, this.point, arg2, arg1);
                        }
                        break;
                }
                break;
        }
    }

    private Object invoke(Method m, Object o, Object... args) throws InvocationTargetException, IllegalAccessException {
        return m.invoke(m, o, args);
    }

    private ArrayList<Method> getMethodDessin(Method[] allMethods) {
        ArrayList<Method> returnM = new ArrayList<Method>();
        for (int i = 0; i < allMethods.length; i++) {
            if (allMethods[i].getAnnotation(Dessiner.class) != null) {
                returnM.add(allMethods[i]);
            }
        }
        if (returnM.size() <= 0) {
            for (int i = 0; i < allMethods.length; i++) {
                Class[] allParameters = allMethods[i].getParameterTypes();
                if (allParameters.length == 2) {
                    if ((allParameters[0].getName().equals("java.awt.Point") && allParameters[1].getName().equals("java.awt.Graphics")) || (allParameters[1].getName().equals("java.awt.Point") && allParameters[0].getName().equals("java.awt.Graphics"))) {
                        returnM.add(allMethods[i]);
                    }
                }
            }
        }
        return returnM;
    }

    public void seDeplacer() {

    }

    public void attaquer(Object plugins,Graphics g, Robot r) throws InvocationTargetException, IllegalAccessException {
        if(this.attaque.contains(plugins)){
            Class attaque = plugins.getClass();
            Method[] allMethods = attaque.getMethods();
            for(int i=0;i<allMethods.length;i++){


                if(allMethods[i].getParameterTypes().length==3){
                    if(allMethods[i].getParameterTypes()[0].getName().equals("java.awt.Graphics") && allMethods[i].getReturnType().getName().equals("boolean")){
                        boolean bienAttaque = invokeMethodsAttack(allMethods[i],plugins,g,r.getCoordonnee());
                        if(bienAttaque){
                            Attaque attaqueA = (Attaque)attaque.getAnnotation(Attaque.class);
                            this.energie-=attaqueA.perteEnergie();
                            r.degats(attaqueA.perteVie());
                        }
                    }
                }
            }
        }
    }

    public void degats (int perteVie){
        this.vie-=perteVie;
    }

    private boolean invokeMethodsAttack(Method m,Object o, Graphics g, Point p) throws InvocationTargetException, IllegalAccessException {
        boolean returnA= false;
        int idArgGraphics=-1,idArgPointp1=-1,idArgPointp2=-1;
        Class[] allArgument = m.getParameterTypes();
        for(int i=0;i<allArgument.length;i++){
            if(allArgument[i].getName().equals("java.awt.Graphics")){
                idArgGraphics=i;
            }else if(allArgument[i].getName().equals("java.awt.Point")){
                if(idArgPointp1==-1){
                    idArgPointp1=i;
                }else{
                    idArgPointp2=i;
                }
            }
        }
        if(idArgGraphics!=-1 && idArgPointp1!=-1 && idArgPointp2!=-1){
            if(idArgPointp1<idArgPointp2){
                if(idArgGraphics<idArgPointp1){
                    returnA = (Boolean)this.invoke(m,o,g,this.point,p);
                }else if(idArgGraphics>idArgPointp2){
                    returnA = (Boolean)this.invoke(m,o,this.point,p,g);
                }else{
                    returnA = (Boolean)this.invoke(m,o,this.point,g,p);
                }
            }else{
                if(idArgGraphics<idArgPointp2){
                    returnA = (Boolean)this.invoke(m,o,g,this.point,p);
                }else if(idArgGraphics>idArgPointp1){
                    returnA = (Boolean)this.invoke(m,o,this.point,p,g);
                }else{
                    returnA = (Boolean)this.invoke(m,o,this.point,g,p);
                }
            }
        }

        return returnA;
    }

}
