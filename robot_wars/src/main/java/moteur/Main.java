package moteur;

import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Robot t = new Robot();
        Vue v = new Vue();
        Thread.currentThread().sleep(1000);
        t.seDessiner(v.getG());
    }

}
