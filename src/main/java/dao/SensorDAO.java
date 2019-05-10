package dao;

import database.DbConnection;
import entities.Sensor;

import java.sql.*;

public class SensorDAO {

    public void insert(Sensor sensor) {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        String query = "insert into sensor values (?, ?)";

        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sensor.getName());
            preparedStmt.setString(2, sensor.getType());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
