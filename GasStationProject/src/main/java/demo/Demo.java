package demo;

import gasStation.Car;
import gasStation.GasStation;
import gasStation.Statistics;


public class Demo {
    public static void main(String[] args) {

        GasStation g = new GasStation();
        g.start();
        Statistics s = new Statistics();
        s.setDaemon(true);
        s.start();

        while (true){
            new Car(g).start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
}
