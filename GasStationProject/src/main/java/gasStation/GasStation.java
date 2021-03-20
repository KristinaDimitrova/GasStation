package gasStation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class GasStation {

    private static final int GAS_PUMPS_COUNT = 5;
    private static final int CASH_DESKS_COUNT = 2;
    private static final int WORKERS_COUNT = 2;
    private ArrayList<GasPump> gasPumps;
    private  ArrayList<CashDesk> cashDesks;
    private ArrayList <Worker> workers;
    private HashSet<Loading> loadings;


    public GasStation(){
        loadings = new HashSet<>();
        gasPumps = new ArrayList<>();
        for (int i = 0; i < GAS_PUMPS_COUNT; i++) {
            gasPumps.add(new GasPump(i+1));
        }
        cashDesks = new ArrayList<>();
        for (int i = 0; i < CASH_DESKS_COUNT; i++) {
            cashDesks.add(new CashDesk("cash desk" + (i+1), this));
        }
        workers = new ArrayList<>();
        for (int i = 0; i < WORKERS_COUNT; i++) {
            workers.add(new Worker(this ));
        }
    }

    public void start(){
        for(Worker worker : workers){
            worker.start();
        }
    }

    public  void addNewLoading(Loading l){
        loadings.add(l);
    }

    private boolean isEmpty(){
        for (GasPump gasPump : gasPumps){
            if(!gasPump.isEmpty() && !gasPump.isBusy()){
                return false;
            }
        }
        return true;
    }

    private GasPump getFreeGasPump() throws Exception {
        for (GasPump gasPump : gasPumps){
            if(!gasPump.isEmpty() && !gasPump.isBusy()){
                return gasPump;
            }
        }
        throw new Exception("you are trying to get car from empty gas station");
    }

    public synchronized void loadVehicle(Car car){
        Random r = new Random();
        gasPumps.get(r.nextInt(GAS_PUMPS_COUNT)).addCar(car);
        notifyAll();
        while (!car.isFull()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(r.nextBoolean()){
            cashDesks.get(0).addCar(car);
        }
        cashDesks.get(1).addCar(car);
    }

    public void fillVehicle() throws Exception {
        GasPump gasPump ;
        synchronized (this){
            while (isEmpty()){
                try {
                    wait();
                    System.out.println("worker is waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
             gasPump = getFreeGasPump();
             gasPump.setBusy(true);

        }
        Car car = gasPump.getNextCar();
        car.setGasPump(gasPump);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        car.setFull(true);
        synchronized (this){
            notifyAll();
        }


    }





}
