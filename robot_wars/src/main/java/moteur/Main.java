package moteur;

import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Thread.currentThread().sleep(1000);
        Moteur m = new Moteur();
        IAsimple player1 = new IAsimple(m.getR1(), m.getR2d2());
        IAsimple player2 = new IAsimple(m.getR2d2(), m.getR1());
        m.run(player1, player2); //Lance une partie complète
    }
}
