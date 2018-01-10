package moteur;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Main {

    private ArrayList<Robot> robotsEnnemis;
    private Moteur m;

    public Main(Moteur moteur){
        this.robotsEnnemis= new ArrayList<Robot>();
        this.m=moteur;
        this.reinitialiseRobotsEnnemis();
    }

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Moteur m = new Moteur(3);
        ArrayList<Robot> allRobots=m.getRobots();
        ArrayList<IAsimple> players = new ArrayList<IAsimple>();
        Main main = new Main(m);
        ArrayList<Robot> robotsEnnemis = main.getRobotsEnnemis();
        for(int i=0;i<allRobots.size();i++){
            robotsEnnemis.remove(allRobots.get(i));
            System.out.println(i+" "+robotsEnnemis.size());
            players.add(new IAsimple(allRobots.get(i),robotsEnnemis));
            main.reinitialiseRobotsEnnemis();
        }
        m.run(players); //Lance une partie complète
    }

    public void reinitialiseRobotsEnnemis(){
        ArrayList<Robot> robots = m.getRobots();
        robotsEnnemis.clear();
        for(Robot r:robots){
            robotsEnnemis.add(r);
        }
    }

    public ArrayList<Robot> getRobotsEnnemis() {
        return robotsEnnemis;
    }
}
