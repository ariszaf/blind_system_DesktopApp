package dao;

import database.DbConnection;
import entities.Sample;
import entities.SampleHasAccelerometer;
import entities.SampleHasGps;
import entities.SampleHasProximity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import report.Report;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReportDAO {

    public ObservableList<Report> getAllReports() {
        Connection conn = DbConnection.Connect();

        ObservableList<Report> reports = null;
        reports = FXCollections.observableArrayList();

        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT sample.id, sample.terminal_name, " +
                    "sample.server_datetime, sample.client_datetime, sample_has_gps.longitude, sample_has_gps.latitude, " +
                    "sample_has_proximity.proximity_x, sample_has_accelerometer.acceleration_x, " +
                    "sample_has_accelerometer.acceleration_y, sample_has_accelerometer.acceleration_z, " +
                    "sample.confirmed FROM sample JOIN sample_has_gps ON sample.id=sample_has_gps.sample_id" +
                    " JOIN sample_has_proximity ON sample.id=sample_has_proximity.sample_id JOIN " +
                    "sample_has_accelerometer on sample.id=sample_has_accelerometer.sample_id ");
            while(rs.next()){
                reports.add(new Report(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
                        rs.getDouble(5),rs.getDouble(6),rs.getFloat(7),
                        rs.getFloat(8), rs.getFloat(9),rs.getFloat(10),
                        rs.getBoolean(11)));
            }

            rs.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return reports;
    }

    public ObservableList<Report> getFilteringReports(Integer sampleId, String terminalName,
                                                      String fromServerDate,String toServerDate,String fromClientDate,String toClientDate, Double fromLongitude,
                                                      Double toLongitude, Double fromLatitude, Double toLatitude,
                                                      Float proximity, Float accelerationX, Float accelerationY,
                                                      Float accelerationZ, Boolean confirmed) {
        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;


        ObservableList<Report> reports = null;
        reports = FXCollections.observableArrayList();

            String query= "SELECT sample.id, sample.terminal_name, sample.server_datetime, " +
                    "sample.client_datetime, sample_has_gps.longitude, sample_has_gps.latitude, sample_has_proximity.proximity_x, " +
                    "sample_has_accelerometer.acceleration_x, sample_has_accelerometer.acceleration_y, " +
                    "sample_has_accelerometer.acceleration_z, sample.confirmed FROM sample " +
                    "JOIN sample_has_gps ON sample.id=sample_has_gps.sample_id JOIN sample_has_proximity " +
                    "ON sample.id=sample_has_proximity.sample_id JOIN sample_has_accelerometer on " +
                    "sample.id=sample_has_accelerometer.sample_id WHERE " +
                    "(sample.id ="+ sampleId + " OR " + sampleId + " IS NULL )" +
                    "AND (sample.terminal_name = ? OR ? IS NULL)" +
                    "AND ((sample.server_datetime BETWEEN ? AND ?) OR ? IS NULL ) " +
                    "AND ((sample.client_datetime BETWEEN ? AND ?) OR ? IS NULL ) " +
                    "AND ((sample_has_gps.longitude BETWEEN " + fromLongitude + " AND " + toLongitude + ") OR " + fromLongitude  + " IS NULL) " +
                    "AND ((sample_has_gps.latitude BETWEEN " + fromLatitude + " AND " + toLatitude + " ) OR " + toLatitude + " IS NULL) " +
                    "AND ( sample_has_proximity.proximity_x = " + proximity + " OR " +proximity+ " IS NULL) " +
                    "AND (sample_has_accelerometer.acceleration_x = " + accelerationX + " OR " + accelerationX + " IS NULL) " +
                    "AND (sample_has_accelerometer.acceleration_y =" + accelerationY + " OR " + accelerationY + " IS NULL) " +
                    "AND (sample_has_accelerometer.acceleration_z = " + accelerationZ + " OR " + accelerationZ +  " IS NULL) " +
                    "AND (sample.confirmed = " + confirmed + " OR " + confirmed + " IS NULL) ";

        try {
            preparedStmt = conn.prepareStatement(query);

            if(terminalName==null){
                preparedStmt.setNull(1, Types.VARCHAR);
                preparedStmt.setNull(2, Types.VARCHAR);
            }
            else{
                preparedStmt.setString(1, terminalName);
                preparedStmt.setString(2, terminalName);
            }

            if(fromServerDate==null){
                preparedStmt.setNull(3,Types.VARCHAR);
                preparedStmt.setNull(5,Types.VARCHAR);
            }
            else{
                preparedStmt.setString(3,fromServerDate);
                preparedStmt.setString(5,fromServerDate);
            }
            if (toServerDate==null)
                preparedStmt.setNull(4,Types.VARCHAR);
            else
                preparedStmt.setString(4,toServerDate);

            if(fromClientDate==null){
                preparedStmt.setNull(6,Types.VARCHAR);
                preparedStmt.setNull(8,Types.VARCHAR);
            }
            else{
                preparedStmt.setString(6,fromClientDate);
                preparedStmt.setString(8,fromClientDate);
            }
            if (toClientDate==null)
                preparedStmt.setNull(7,Types.VARCHAR);
            else
                preparedStmt.setString(7,toClientDate);


            ResultSet rs = preparedStmt.executeQuery();

            while(rs.next()){
                reports.add(new Report(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),
                        rs.getDouble(5),rs.getDouble(6),rs.getFloat(7),
                        rs.getFloat(8), rs.getFloat(9),rs.getFloat(10),
                        rs.getBoolean(11)));
            }
            rs.close();
            preparedStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    public void insert(Report report) {
        SampleDAO sampleDAO = new SampleDAO();
        Sample sample = new Sample(report.getServerDatetime(), report.getClientDatetime(), report.getConfirmed(), report.getTerminalName());
        sampleDAO.insert(sample);
        Integer sampleId = sampleDAO.selectSampleId(sample);

        SampleHasGpsDAO sampleHasGpsDAO = new SampleHasGpsDAO();
        sampleHasGpsDAO.insert(new SampleHasGps(report.getLongitude(),report.getLatitude(),"Gps",sampleId));

        SampleHasAccelerometerDAO sampleHasAccelerometerDAO = new SampleHasAccelerometerDAO();
        sampleHasAccelerometerDAO.insert(new SampleHasAccelerometer(report.getAccelerationX(),report.getAccelerationY(),report.getAccelerationZ(),"Accelerometer",sampleId));

        SampleHasProximityDAO sampleHasProximityDAO = new SampleHasProximityDAO();
        sampleHasProximityDAO.insert(new SampleHasProximity(report.getProximityX(),"Proximity",sampleId));
    }

    public void update(Report report) {
        Connection conn = DbConnection.Connect();
        PreparedStatement preparedStmt=null;

        String query= "UPDATE sample SET sample.confirmed=1 where (year(?)-year(sample.server_datetime) = 0)  and TIMEDIFF(?, sample.server_datetime) <= '00:00:01.000000' and sample.confirmed=0";

        try {
            preparedStmt = conn.prepareStatement(query);

            preparedStmt.setString(1, report.getServerDatetime());
            preparedStmt.setString(2, report.getServerDatetime());

            // execute the preparedstatement
            preparedStmt.execute();

            preparedStmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}