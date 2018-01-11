package moteur;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class IAsimple implements Runnable {
    private Robot robot;
    private ArrayList<Robot> ennemis;
    private boolean tourfini = false;

    public IAsimple(Robot robot, ArrayList<Robot> ennemis) {
        this.robot = robot;
        this.ennemis=new ArrayList<Robot>();
        for(Robot r : ennemis){
            this.ennemis.add(r);
        }
    }

    public Robot getRobot() {
        return robot;
    }

    /**
     * Tant qu'il reste de l'énergie au robot du joueur,
     * on regarde toutes les attaques qu'il peut faire,
     * l'IA attaque son ennemi tant qu'il peut attaquer
     * sinon il se déplace
     * Quand le robot n'a plus d'énergie le tour s'arrête
     * @return true, signifiant que le tour de l'IA est fini
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InterruptedException
     */
    public boolean jouerTour() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        while(robot.getEnergie() > 1){
            //ATTAQUE
            ArrayList<Object> attaques = robot.getAttaque();

            for(Object o : attaques){
                for(Robot ennemi: this.ennemis){
                    if(robot.peutAttaquer(o, ennemi)){
                        robot.attaque(o, ennemi);
                    }
                }
            }

            //DEPLACEMENT
            ArrayList<Object> deplacement = robot.getDeplacement();

            //System.out.println(robot.getEnergie());

            int randDistanceX = (int) (Math.random() * robot.getEnergie());
            int randDistanceY = (int) (Math.random() * robot.getEnergie());

            //System.out.println("X = " + randDistanceX);
            //System.out.println("Y = " + randDistanceY);

            if(deplacement.size() == 1 ){
                robot.seDeplacer(deplacement.get(0), randDistanceX, randDistanceY);
            } else if (deplacement.size() > 1) {
                int rand = (int) (Math.random() * deplacement.size());
                robot.seDeplacer(deplacement.get(rand), randDistanceX, randDistanceY);
            }
        } return tourfini = true;
    }

    public int getVieRobot(){
        return this.robot.getVie();
    }

    public void run() {
        try {
            this.jouerTour();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeEnnemi(Robot robot) {
        this.ennemis.remove(robot);
    }
}
