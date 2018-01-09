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
     * Tant qu'il reste de l'énergie au robot du joueur,
     * on regarde toutes les attaques qu'il peut faire,
     * l'IA attaque son ennemi tant qu'il peut attaquer
     * sinon il se déplace
     * Quand le robot n'a plus d'énergie le tour s'arrête
     * @return true, signifiant que le tour de l'IA est fini
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public boolean jouerTour() throws InvocationTargetException, IllegalAccessException {
        while(robot.getEnergie() != 0){
            //ATTAQUE
            ArrayList<Object> attaques = robot.getAttaque();

            for(Object o : attaques){
                if(robot.peutAttaquer(o, ennemi)){
                    robot.attaque(o, ennemi);
                }
            }

            //DEPLACEMENT
            ArrayList<Object> deplacement = robot.getDeplacement();

            int randDistanceX = (int) (Math.random() * robot.getEnergie()/2);
            int randDistanceY = (int) (Math.random() * robot.getEnergie()/2);

            if(deplacement.size() == 1 ){
                robot.seDeplacer(deplacement.get(0), randDistanceX, randDistanceY);
            } else if (deplacement.size() > 1) {
                int rand = (int) (Math.random() * deplacement.size());
                robot.seDeplacer(deplacement.get(rand), randDistanceX, randDistanceY);
            }
        } return tourfini = true;
    }
}
