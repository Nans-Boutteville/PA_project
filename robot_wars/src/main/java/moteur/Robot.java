package moteur;

import annotation.Attaque;
import annotation.CalculDeplacement;
import annotation.Deplacer;
import annotation.Dessiner;

import javax.swing.*;
import java.awt.*;
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

    private JPanel graph;

    public Robot() {
        this(new Point(0, 0));
    }

    public Robot(Point point) {
        this.point = point;
        this.vie = 100;
        this.energie = this.EENERGIEBASE;
        this.graphisme= new ArrayList<Object>();
        this.attaque = new ArrayList<Object>();
        this.deplacement = new ArrayList<Object>();
    }

    public void setGraph(JPanel panel){
        this.graph=panel;
    }

    public ArrayList<Object> getAttaque() {
        return this.attaque;
    }

    public ArrayList<Object> getDeplacement() {
        return this.deplacement;
    }

    public ArrayList<Object> getGraphisme() {
        return graphisme;
    }

    public void addPLuginsGraphisme(Object graph) throws InvocationTargetException, IllegalAccessException {
        graphisme.add(graph);
        this.graph.repaint();
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

    public void reinitialiseEnergie() throws InvocationTargetException, IllegalAccessException {
        this.energie = this.EENERGIEBASE;
        this.graph.repaint();
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
                    this.invoke(m, o, this.point, arg, g);
                } else {
                    this.invoke(m, o, g, arg, this.point);
                }
                break;
            default:
                if (m.getParameterTypes()[0].getName().equals("java.awt.Point")) {
                    m.invoke(o, this.point, g, arg);
                } else {
                    m.invoke(o, g, this.point,arg);
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
        return m.invoke( o, args);
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



    public void attaque(Object plugins, Robot r) throws InvocationTargetException, IllegalAccessException {
        if(this.attaque.contains(plugins)){
            Class attaque = plugins.getClass();
            Method[] allMethods = attaque.getMethods();
            for(int i=0;i<allMethods.length;i++){

                if(allMethods[i].getParameterTypes().length==3){
                    Object arg1=null;
                    Object arg2=null;
                    Object arg3=null;
                    for(Class c : allMethods[i].getParameterTypes()){
                        if(c.getName().equals("java.awt.Graphics")){
                            if(arg1==null){
                                arg1=c;
                            }else if(arg2==null){
                                arg2=c;
                            }else if(arg3==null){
                                arg3=c;
                            }
                        }else if(c.getName().equals("java.awt.Point")){
                            if(arg1==null){
                                arg1=c;
                            }else if(arg2==null){
                                arg2=c;
                            }else if(arg3==null){
                                arg3=c;
                            }
                        }

                    }
                    if((arg1!=null && arg2!=null && arg3!=null)  && allMethods[i].getReturnType().getName().equals("boolean")){
                        boolean bienAttaque = invokeMethodsAttack(allMethods[i],plugins,this.graph.getGraphics(),r.getCoordonnee());
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

    public boolean peutAttaquer(Object plugins,Robot r) throws InvocationTargetException, IllegalAccessException {
        boolean returnAttack =false;
        if(this.attaque.contains(plugins) && plugins.getClass().getAnnotation(Attaque.class).perteEnergie()<=this.energie){

            Class attaque = plugins.getClass();
            Method[] allMethods = attaque.getMethods();
            for(int i=0;i<allMethods.length;i++){


                if(allMethods[i].getParameterTypes().length==3){
                    Object arg1=null;
                    Object arg2=null;
                    Object arg3=null;
                    for(Class c : allMethods[i].getParameterTypes()){
                        if(c.getName().equals("java.awt.Graphics")){
                            if(arg1==null){
                                arg1=c;
                            }else if(arg2==null){
                                arg2=c;
                            }else if(arg3==null){
                                arg3=c;
                            }
                        }else if(c.getName().equals("java.awt.Point")){
                            if(arg1==null){
                                arg1=c;
                            }else if(arg2==null){
                                arg2=c;
                            }else if(arg3==null){
                                arg3=c;
                            }
                        }

                    }

                    if((arg1!=null && arg2!=null && arg3!=null) && allMethods[i].getReturnType().getName().equals("boolean")){
                         returnAttack = invokeMethodsAttack(allMethods[i],plugins,this.graph.getGraphics(),r.getCoordonnee());

                    }
                }
            }
        }
        return returnAttack;
    }

    public void degats (int perteVie) throws InvocationTargetException, IllegalAccessException {
        this.vie-=perteVie;
        if(vie<0){
            vie=0;
        }
        this.graph.repaint();
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

    public void seDeplacer(Object plugins,int deplacementX,int deplacementY) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        if(this.deplacement.contains(plugins)){
            Class deplacemen = plugins.getClass();
            this.invokeMethodDeplacement(deplacemen.getMethods(),plugins,deplacementX,deplacementY);
        }
    }

    private void invokeMethodDeplacement(Method[] methods,Object o,int deplacementX,int deplacementY) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Method methodDeplacement=null;
        for(Method method:methods){
            if(method.getAnnotation(Deplacer.class)!=null && method.getReturnType().getName().equals("java.awt.Point")){
                methodDeplacement=method;
                break;
            }
        }

        if(methodDeplacement==null){
            for(Method method:methods){
                if(this.getArgumentofDeplacementMethod(method,deplacementX,deplacementY).size()==4 && method.getReturnType().getName().equals("java.awt.Point")){
                    methodDeplacement=method;
                    break;
                }
            }
        }
        if(methodDeplacement!=null){
            int coutEnergie = this.coutEnergieDeplacement(o,deplacementX,deplacementY);
            ArrayList<Object> args = getArgumentofDeplacementMethod(methodDeplacement,deplacementX,deplacementY);
            if(coutEnergie>-1 && args.size()==4){
                Point p = (Point)this.invoke(methodDeplacement,o,args.get(0),args.get(1),args.get(2),args.get(3));
                int largeur = this.graph.getWidth();
                int hauteur = this.graph.getHeight();
                int x = (int)p.getX();
                int y = (int)p.getY();
                if(p.getX()>largeur-40){
                    x=largeur-40;
                }else if(p.getX()<40){
                    x=40;
                }
                if(p.getY()>hauteur-40){
                    y=hauteur-40;
                }else if(p.getY()<40){
                    y=40;
                }
                this.point.setLocation(new Point(x,y));
                Thread.currentThread().sleep(5);
                this.energie-=coutEnergie;


            }

        }

    }

    public Point CalculPointDeplacement(Method[] methods,Object o,int deplacementX, int deplacementY) throws InvocationTargetException, IllegalAccessException {
        Method methodDeplacement=null;
        for(Method method:methods){
            if(method.getAnnotation(Deplacer.class)!=null && method.getReturnType().getName().equals("java.awt.Point")){
                methodDeplacement=method;
                break;
            }
        }

        if(methodDeplacement==null){
            for(Method method:methods){
                if(this.getArgumentofDeplacementMethod(method,deplacementX,deplacementY).size()==4 && method.getReturnType().getName().equals("java.awt.Point")){
                    methodDeplacement=method;
                    break;
                }
            }
        }
        if(methodDeplacement!=null){
            int coutEnergie = this.coutEnergieDeplacement(o,deplacementX,deplacementY);
            ArrayList<Object> args = getArgumentofDeplacementMethod(methodDeplacement,deplacementX,deplacementY);
            if(coutEnergie>-1 && args.size()==4){

                return (Point)this.invoke(methodDeplacement,o,args.get(0),args.get(1),args.get(2),args.get(3));
            }

        }
        return null;

    }

    private ArrayList<Object> getArgumentofDeplacementMethod(Method method,int deplacementX,int deplacementY){
        ArrayList<Object> args = new ArrayList<Object>();
        Class[] allParameters = method.getParameterTypes();
        boolean deplacementXplacer=false;
        if(allParameters.length==4){
            for(Class parameters : allParameters){
                if(parameters.getName().equals("java.awt.Graphics")){
                    args.add(this.graph.getGraphics());
                }else if(parameters.getName().equals("java.awt.Point")){
                    args.add(this.point);
                }else if (parameters.getName().equals("int")){
                    int deplacement;
                    if(deplacementXplacer) {
                        deplacement = deplacementX;
                        deplacementXplacer=true;
                    }else{
                        deplacement =deplacementY;
                    }
                    args.add(deplacement);
                }
            }
        }
        return args;
    }

    public int coutEnergieDeplacement(Object plugins,int deplacementX, int deplacementY) throws InvocationTargetException, IllegalAccessException {
        if(this.deplacement.contains(plugins)){
            Class deplacemen = plugins.getClass();
            return this.invokeMethodDeplacementCoutEnergie(deplacemen.getMethods(),plugins,deplacementX,deplacementY);

        }
        return -1;
    }

    private int invokeMethodDeplacementCoutEnergie(Method[] methods,Object o,int deplacementX,int deplacementY) throws InvocationTargetException, IllegalAccessException {
        Method methodDeplacement=null;
        for(Method method:methods){
            if(method.getAnnotation(CalculDeplacement.class)!=null && method.getReturnType().getName().equals("int")){
                methodDeplacement=method;
                break;
            }
        }

        if(methodDeplacement==null){
            for(Method method:methods){
                if(method.getParameterTypes().length==1 && method.getParameterTypes()[0].getName().equals("int") && method.getReturnType().getName().equals("int")){
                    methodDeplacement=method;
                    break;
                }
            }
        }
        if(methodDeplacement!=null){
            return (Integer) this.invoke(methodDeplacement,o,deplacementX,deplacementY);
        }
        return -1;
    }


}
