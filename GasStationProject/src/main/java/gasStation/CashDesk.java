package gasStation;

import db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CashDesk {
    private GasStation gasStation;
    private BlockingQueue<Car> cars = new LinkedBlockingQueue<>(1);
    private Cashier cashier;
    private String name;


    public CashDesk(String name, GasStation gasStation){
        this.cashier = new Cashier(this);
        cashier.start();
        this.name = name;
        this.gasStation = gasStation;
    }

    public synchronized  void addCar(Car car){
        System.out.println("car is added to " + name);
        cars.offer(car);
        notifyAll();
    }

    public synchronized void serveCar(){
        if(cars.isEmpty()){
            try {
                System.out.println("cashier is waiting");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Car car = cars.peek();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("car is served");
        cars.poll();
        car.getGasPump().removeFirstCar();
        car.getGasPump().setBusy(false);
        Loading l = new Loading(car.getGasPump().getId(), car.getFuelType(), car.getFuelLiters(), LocalDateTime.now());
        gasStation.addNewLoading(l);
        addRowIntoDB(l);
    }

    private void addRowIntoDB(Loading loading){
        String sql = "INSERT INTO station_loadings (kolonka_id, fuel_type, fuel_quantity, loading_time ) VALUES(?,?,?,?)";
        try{
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,loading.getPumpId());
            ps.setString(2, loading.getFuelType());
            ps.setInt(3, loading.getQty());
            ps.setTimestamp(4, Timestamp.valueOf(loading.getDateAndTime()));
            ps.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("error inserting into DB " + throwables.getMessage());;
        }
    }
}
