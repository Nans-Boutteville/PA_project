package moteur;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Moteur m = new Moteur(3);
        ArrayList<Robot> allRobots=m.getRobots();
        ArrayList<IAsimple> players = new ArrayList<IAsimple>();
        for(int i=0;i<allRobots.size()-1;i++){
            players.add(new IAsimple(allRobots.get(i),allRobots.get(i+1)));
        }
        players.add(new IAsimple(allRobots.get(allRobots.size()-1),allRobots.get(0)));
        m.run(players); //Lance une partie complète
    }
}
