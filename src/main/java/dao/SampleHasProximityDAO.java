package dao;

import database.DbConnection;
import entities.SampleHasProximity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SampleHasProximityDAO {

    public void insert(SampleHasProximity sampleHasProximity) {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        String query = "insert into sample_has_proximity values (?, ?, ?)";

        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sampleHasProximity.getSensorName());
            preparedStmt.setInt(2, sampleHasProximity.getSampleId());
            preparedStmt.setFloat(3, sampleHasProximity.getProximityX());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
