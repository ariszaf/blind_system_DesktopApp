package dao;

import database.DbConnection;
import entities.Device;
import entities.Sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceDAO {

    public void insert(Device device)  {

        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        String query = "insert into device values (?)";

        try {
        // create the mysql insert prepared statement
            preparedStmt = conn.prepareStatement(query);

            preparedStmt.setString(1, device.getTerminalName());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkIfDeviceExist(String terminalName) {
        Boolean exist=true;
        Connection conn = DbConnection.Connect();

        PreparedStatement preparedStmt;

        String query = ("SELECT device.terminal_name FROM device WHERE device.terminal_name = ?");

        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,terminalName);

            ResultSet rs = preparedStmt.executeQuery();
            if(!rs.next()) {
                exist = false;
            }
            rs.close();
            preparedStmt.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;
    }
}
