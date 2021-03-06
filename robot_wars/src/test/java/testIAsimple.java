import moteur.IAsimple;
import moteur.Moteur;
import moteur.Robot;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestIAsimple {

    private IAsimple testIA;

    /**
     * Mise en place d'un environnement de test pour la classe IAsimple
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Before
    public void environnement() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Moteur testMoteur = new Moteur(2);
        ArrayList<Robot> testEnnemis = new ArrayList<Robot>();
        ArrayList<Robot> robots = (ArrayList<Robot>) testMoteur.getRobots().clone();
        robots.remove(testMoteur.getRobots().get(0));
        testIA = new IAsimple(testMoteur.getRobots().get(0), robots);
    }

    @Test
    public void testJouerTour() throws Exception{
        Thread testThread = new Thread(testIA);
        testThread.start();
        assertEquals(true, testIA.jouerTour());
    }

}
