package gasStation;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GasPump {
    private BlockingQueue<Car> cars ;
    private volatile boolean busy;
    private int id;

     public GasPump(int id){
         cars = new LinkedBlockingQueue<>();
         busy = false;
         this.id = id;
    }

    public void addCar(Car car){
         cars.offer(car);
    }

    public Car getNextCar(){
       return cars.peek();
    }

    public void removeFirstCar(){
         cars.poll();
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isBusy(){
         return busy;
    }

    public boolean isEmpty() {
        return cars.isEmpty();
    }

    public int getId() {
        return id;
    }
}
