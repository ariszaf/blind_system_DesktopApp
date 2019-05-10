package dao;

import database.DbConnection;
import entities.SampleHasGps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SampleHasGpsDAO {

    public void insert(SampleHasGps sampleHasGps) {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        String query = "insert into sample_has_gps values (?, ?, ?, ?)";

        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sampleHasGps.getSensorName());
            preparedStmt.setInt(2, sampleHasGps.getSampleId());
            preparedStmt.setDouble(3, sampleHasGps.getLongitude());
            preparedStmt.setDouble(4, sampleHasGps.getLatitude());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
