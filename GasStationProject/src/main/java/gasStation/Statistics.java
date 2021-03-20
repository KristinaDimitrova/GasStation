package gasStation;

import db.DBConnector;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Statistics extends  Thread{

    @Override
    public void run() {
        makeStatistics();
    }

    public void makeStatistics(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        makeReportForAllLoadings();
        makeReportAllCars();
        makeReportAllFuel();


    }


    private void makeReportAllFuel() {
        TreeMap <String  , Integer> fuelByType = new TreeMap<>();
        String sql = "SELECT fuel_type, SUM(fuel_quantity) FROM station_loadings GROUP BY fuel_type;";
        try {
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
               fuelByType.put(rs.getString(1), rs.getInt(2));
            }
            PrintStream printStreams = new PrintStream
                    ("report-3-"+ LocalDate.now().toString().
                            replaceAll(":", "-").replaceAll("/.", "-")+".txt");
            for(Map.Entry<String, Integer> entry : fuelByType.entrySet()){
                printStreams.println( entry.getKey() +" - "+ entry.getValue()+" liters");
            }

        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void makeReportAllCars() {
        TreeMap <Integer , Integer> carsOnPump = new TreeMap<>();
        String sql = "SELECT kolonka_id, count(*)FROM station_loadings GROUP BY kolonka_id";
        try {
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
             carsOnPump.put(rs.getInt(1), rs.getInt(2));
            }
            PrintStream printStreams = new PrintStream
                    ("report-2-"+ LocalDate.now().toString().
                            replaceAll(":", "-").replaceAll("/.", "-")+".txt");
            for(Map.Entry<Integer, Integer> entry : carsOnPump.entrySet()){
                printStreams.println("kolonka "+ entry.getKey() +" - "+ entry.getValue()+" avtomobili");
            }

        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void makeReportForAllLoadings() {
        TreeMap <Integer, TreeSet< Loading>> allLoadings = new TreeMap<>();
        String sql = "SELECT * FROM station_loadings WHERE date (loading_time) = CURDATE()";
        try {
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs =  ps.executeQuery();
            while (rs.next()){
                Loading loading = new Loading(rs.getInt("kolonka_id"),
                        rs.getString("fuel_type"),
                        rs.getInt("fuel_quantity"),
                        rs.getTimestamp("loading_time").toLocalDateTime());
                if(!allLoadings.containsKey(loading.getPumpId())){
                    allLoadings.put(loading.getPumpId(), new TreeSet<>(((o1, o2) -> o1.getDateAndTime().compareTo(o2.getDateAndTime()))));
                }
                allLoadings.get(loading.getPumpId()).add(loading);
            }
            PrintStream printStreams = new PrintStream
                    ("report-1-"+ LocalDate.now().toString().
                            replaceAll(":", "-").replaceAll("/.", "-")+".txt");
            for(Map.Entry<Integer, TreeSet<Loading> >entry : allLoadings.entrySet() ){
                printStreams.println("kolonka" + entry.getKey());
                for(Loading l : entry.getValue()){
                    printStreams.println("            " + l);
                }
            }
        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
