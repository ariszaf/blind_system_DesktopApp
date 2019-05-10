package parser;

import mqtt.ReceivedData;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserReceiver {

    public ReceivedData createObjectReceivedData(String[] parser) {
        String terminal_name = null;
        double latitude = 0;
        double longitude = 0;
        String proximity_name = null;
        float proximity_value = 0;
        String acceleration_name = null;
        float acceleration_x = 0;
        float acceleration_y = 0;
        float acceleration_z = 0;
        Date date_time = new Date();


        //To do: data for time and update ReceivedData

        for (int i = 0; i < parser.length; i++) {
            switch (i) {
                case 0:
                    terminal_name = new String(parser[0]);
                    break;
                case 1:
                    latitude = Double.parseDouble(parser[1]);
                    break;
                case 2:
                    longitude = Double.parseDouble(parser[2]);
                    break;
                case 3:
                    proximity_name = new String(parser[3]);
                    break;
                case 4:
                    proximity_value = Float.parseFloat(parser[4]);
                    break;
                case 5:
                    acceleration_name = new String(parser[5]);
                    break;
                case 6:
                    acceleration_x = Float.parseFloat(parser[6]);
                    break;
                case 7:
                    acceleration_y = Float.parseFloat(parser[7]);
                    break;
                case 8:
                    acceleration_z = Float.parseFloat(parser[8]);
                    break;
                case 9:
                    try {
                      //  Timestamp date = new Timestamp(System.currentTimeMillis());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        System.out.println(parser[9]);
                        date_time = format.parse(parser[9]);
                    } catch (Exception e) {
                        System.out.println("parser error in date");
                    }
                    break;
                default:
                    System.out.println("Something goes wrong");
                    return null;
            }
        }
        return new ReceivedData(terminal_name,latitude,longitude,proximity_name,proximity_value,acceleration_name,acceleration_x,acceleration_y,acceleration_z, date_time);
    }
}

//To do check the correct format in Date