package gasStation;

public class Worker extends  Thread{
    private GasStation gasStation;


    public Worker(GasStation gasStation){
        this.gasStation = gasStation;
    }
    @Override
    public void run() {
        while (true){
            try {
                gasStation.fillVehicle();
            } catch (Exception e) {
                System.out.println("something went wrong" + e.getMessage());
            }
        }
    }

}
