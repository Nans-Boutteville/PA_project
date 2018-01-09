package moteur;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class IAsimple {
    private Robot robot;
    private Robot ennemi;
    private boolean tourfini = false;

    public IAsimple(Robot robot, Robot ennemi) {
        this.robot = robot;
        this.ennemi = ennemi;
    }

    /**
     *
     * @return true, signifiant que le tour de l'IA est fini
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public boolean jouerTour() throws InvocationTargetException, IllegalAccessException {
        robot.reinitialiseEnergie(); //remet l'énergie à 100
        while(robot.getEnergie() != 0){
            ArrayList<Object> attaques = robot.getAttaque();
            for(Object o : attaques){
                if(robot.peuAttaquer(o, ennemi)){
                    robot.attaque(o, ennemi);
                }
            }
            ArrayList<Object> deplacement = robot.getDeplacement();

            int randDistance = (int) Math.random() * robot.getEnergie();

            if(deplacement.size() == 1 ){
                robot.seDeplacer(deplacement.get(0), randDistance);
            } else if (deplacement.size() > 1) {
                int rand = (int) (Math.random() * deplacement.size());
                robot.seDeplacer(deplacement.get(rand), randDistance);
            }
        } return tourfini = true;
    }
}
