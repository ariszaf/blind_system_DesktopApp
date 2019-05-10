package dao;

import database.DbConnection;
import entities.SampleHasAccelerometer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SampleHasAccelerometerDAO {

    public void insert(SampleHasAccelerometer sampleHasAccelerometer) {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        String query = "insert into sample_has_accelerometer values (?, ?, ?, ?, ?)";

        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sampleHasAccelerometer.getSensorName());
            preparedStmt.setInt(2, sampleHasAccelerometer.getSampleId());
            preparedStmt.setFloat(3, sampleHasAccelerometer.getAccelerationX());
            preparedStmt.setFloat(4, sampleHasAccelerometer.getAccelerationY());
            preparedStmt.setFloat(5, sampleHasAccelerometer.getAccelerationZ());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}