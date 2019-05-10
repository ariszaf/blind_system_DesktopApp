package dao;

import database.DbConnection;
import entities.Device;
import entities.Sample;
import java.sql.*;

public class SampleDAO {

    public void insert(Sample sample) {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        DeviceDAO deviceDAO = new DeviceDAO();

        if(!deviceDAO.checkIfDeviceExist(sample.getTerminalName())){
            Device device = new Device(sample.getTerminalName());
            deviceDAO.insert(device);
        }

        String query = "insert into sample (server_datetime,client_datetime,confirmed,terminal_name) values (?, ?, ?, ?)";

        try {
            preparedStmt = conn.prepareStatement(query);

            preparedStmt.setString(1, sample.getServerDatetime());
            preparedStmt.setString(2, sample.getClientDatetime());
            preparedStmt.setBoolean(3, sample.getConfirmed());
            preparedStmt.setString(4, sample.getTerminalName());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer selectSampleId(Sample sample) {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        Integer sampleId = null;
        String query = ("SELECT sample.id FROM sample WHERE sample.server_datetime = ? AND sample.client_datetime = ? AND sample.terminal_name = ?");


        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,sample.getServerDatetime());
            preparedStmt.setString(2,sample.getClientDatetime());
            preparedStmt.setString(3,sample.getTerminalName());

            ResultSet rs = preparedStmt.executeQuery();
            while(rs.next()) {

                sampleId = rs.getInt(1);
            }
            rs.close();
            preparedStmt.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return sampleId;
    }

}
