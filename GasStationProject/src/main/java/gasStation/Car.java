package gasStation;

import java.util.Random;

public class Car extends Thread{

    private GasStation gasStation;
    private String fuelType;
    private int fuelLiters;
    private boolean isFull;
    private GasPump gasPump;

    public Car(GasStation gasStation){
        this.gasStation = gasStation;
        int r = new Random().nextInt(3);
        switch (r){
            case 1 -> this.fuelType = "GAS";
            case 2 -> this.fuelType = "DIESEL";
            case 0 -> this.fuelType = "BENZINE";
        }
        this.fuelLiters = new Random().nextInt(40-10)+10;
    }

    public int getFuelLiters() {
        return fuelLiters;
    }

    public String getFuelType() {
        return fuelType;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setGasPump(GasPump gasPump) {
        this.gasPump = gasPump;
    }

    public GasPump getGasPump() {
        return gasPump;
    }

    public void setFull(boolean full) {
        System.out.println("car is full");
        isFull = full;
    }

    @Override
    public void run() {
        gasStation.loadVehicle(this);
    }
}
