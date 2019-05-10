package mqtt;


import java.text.SimpleDateFormat;
import java.util.Date;


public class ReceivedData {
    private final String terminal_name;
    private final double latitude;
    private final double longitude;
    private final String proximity_name;
    private final float proximity;
    private final String acceleration_name;
    private final float acceleration_x;
    private final float acceleration_y;
    private final float acceleration_z;
    private final Date date_time;


    public ReceivedData(String terminal_name, double latitude, double longitude, String proximity_name, float proximity, String acceleration_name, float acceleration_x, float acceleration_y, float acceleration_z, Date date_time) {
        this.terminal_name = terminal_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.proximity_name = proximity_name;
        this.proximity = proximity;
        this.acceleration_name = acceleration_name;
        this.acceleration_x = acceleration_x;
        this.acceleration_y = acceleration_y;
        this.acceleration_z = acceleration_z;
        this.date_time = date_time;
    }

    public String getTerminal_name() {

        return terminal_name;
    }



    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public float getProximity() {
        return proximity;
    }


    public float getAcceleration_x() {
        return acceleration_x;
    }


    public float getAcceleration_y() {
        return acceleration_y;
    }


    public float getAcceleration_z() {
        return acceleration_z;
    }


    public Date getDate_time() {
        return date_time;
    }


    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String t = format.format(date_time);
        return "ReceivedData{" +
                "terminal_name='" + terminal_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", proximity_name='" + proximity_name + '\'' +
                ", proximity=" + proximity +
                ", acceleration_name='" + acceleration_name + '\'' +
                ", acceleration_x=" + acceleration_x +
                ", acceleration_y=" + acceleration_y +
                ", acceleration_z=" + acceleration_z +
                " " + t +
                '}';
    }
}
