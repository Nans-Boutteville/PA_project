package moteur;

import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Robot t = new Robot();
        Vue v = new Vue();
        t.addPLuginsGraphisme(v);
        t.seDessiner();
    }

}
