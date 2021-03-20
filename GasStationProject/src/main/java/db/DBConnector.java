package db;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static DBConnector instance;
    private Connection connection;
    private DBCredentials c;

    private DBConnector(){
        loadCredentials();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + c.host + ":" + c.port + "/" + c.name, c.user, c.password);
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry, driver not found!" +  e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error connecting to DB!" +  e.getMessage());
        }
    }

    private class DBCredentials {
        private String host;
        private int port;
        private String name;
        private String user;
        private String password;
    }

    private void loadCredentials() {
        Gson gson = new Gson();
        try {
            this.c = gson.fromJson(new FileReader("db_settings.json"), DBCredentials.class);
        } catch (FileNotFoundException e) {
            System.out.println("Error reading credentials!");
        }
    }

    public static DBConnector getInstance(){
        if(instance == null){
            instance = new DBConnector();
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }

}
