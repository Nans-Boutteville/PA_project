package moteur;

import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Vue v = new Vue();
        Thread.currentThread().sleep(1000);
        Moteur m  = new Moteur(v.getG());
    }

}
