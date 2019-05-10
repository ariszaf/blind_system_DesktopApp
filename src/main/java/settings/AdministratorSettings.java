package settings;

import javafx.beans.property.*;

import java.io.*;
import java.util.Properties;

public class AdministratorSettings {
    //con
    private String terminalID_1 = null;
    private String terminalID_2 = null;
    private String alert_topic = null;
    private String mqttIP = null;
    private String mqttPort = null;
    //val
    private FloatProperty thresholdProximitySensor = null;
    private FloatProperty threshold_sensor_acceleration_x = null;
    private FloatProperty threshold_sensor_acceleration_y = null;
    private FloatProperty threshold_sensor_acceleration_z = null;
    private IntegerProperty frequency = null;
    //thres
    private BooleanProperty alarm_when_below_p = null;
    private BooleanProperty alarm_when_below_x = null;
    private BooleanProperty alarm_when_below_y = null;
    private BooleanProperty alarm_when_below_z = null;



    //_________________________________singleton
    private static AdministratorSettings instance = null;

    private AdministratorSettings() {

        thresholdProximitySensor = new SimpleFloatProperty(this,"thresholdProximitySensor",0);
        threshold_sensor_acceleration_x = new SimpleFloatProperty(this,"threshold_sensor_acceleration_x",20);
        threshold_sensor_acceleration_y = new SimpleFloatProperty(this,"threshold_sensor_acceleration_y",20);
        threshold_sensor_acceleration_z = new SimpleFloatProperty(this,"threshold_sensor_acceleration_z",20);
        alarm_when_below_p = new SimpleBooleanProperty(this,"alarm_when_below_p",true);
        alarm_when_below_x = new SimpleBooleanProperty(this,"alarm_when_below_x",false);
        alarm_when_below_y = new SimpleBooleanProperty(this,"alarm_when_below_y",false);
        alarm_when_below_z = new SimpleBooleanProperty(this,"alarm_when_below_z",false);
        frequency = new SimpleIntegerProperty(this,"frequency",1);

        terminalID_1 = "ffffffff-dd60-3fae-a032-38a447d2a861";
        terminalID_2 = "ffffffff-c203-6117-ffff-ffffc500be32";
        mqttIP = "localhost";
        mqttPort = "1883";
        alert_topic = "COMMON_ALERTS";


    }

    public static synchronized AdministratorSettings getInstance() {
        if (instance == null) {
            instance = new AdministratorSettings();
        }
        return instance;
    }

    public float getThresholdProximitySensor() {
        return thresholdProximitySensor.get();
    }


    public void setThresholdProximitySensor(float thresholdProximitySensor) {
        this.thresholdProximitySensor.set(thresholdProximitySensor);
    }

    public float getThreshold_sensor_acceleration_x() {
        return threshold_sensor_acceleration_x.get();
    }


    public void setThreshold_sensor_acceleration_x(float threshold_sensor_acceleration_x) {
        this.threshold_sensor_acceleration_x.set(threshold_sensor_acceleration_x);
    }

    public float getThreshold_sensor_acceleration_y() {
        return threshold_sensor_acceleration_y.get();
    }


    public void setThreshold_sensor_acceleration_y(float threshold_sensor_acceleration_y) {
        this.threshold_sensor_acceleration_y.set(threshold_sensor_acceleration_y);
    }

    public float getThreshold_sensor_acceleration_z() {
        return threshold_sensor_acceleration_z.get();
    }


    public void setThreshold_sensor_acceleration_z(float threshold_sensor_acceleration_z) {
        this.threshold_sensor_acceleration_z.set(threshold_sensor_acceleration_z);
    }

    public boolean isAlarm_when_below_p() {
        return alarm_when_below_p.get();
    }


    public void setAlarm_when_below_p(boolean alarm_when_below_p) {
        this.alarm_when_below_p.set(alarm_when_below_p);
    }

    public boolean isAlarm_when_below_x() {
        return alarm_when_below_x.get();
    }


    public void setAlarm_when_below_x(boolean alarm_when_below_x) {
        this.alarm_when_below_x.set(alarm_when_below_x);
    }

    public boolean isAlarm_when_below_y() {
        return alarm_when_below_y.get();
    }


    public void setAlarm_when_below_y(boolean alarm_when_below_y) {
        this.alarm_when_below_y.set(alarm_when_below_y);
    }

    public boolean isAlarm_when_below_z() {
        return alarm_when_below_z.get();
    }


    public void setAlarm_when_below_z(boolean alarm_when_below_z) {
        this.alarm_when_below_z.set(alarm_when_below_z);
    }

    @Override

    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new CloneNotSupportedException();
    }

    public String getTerminalID_1() {
        return terminalID_1;
    }

    public void setTerminalID_1(String terminalID_1) {
        this.terminalID_1 = terminalID_1;
    }

    public String getTerminalID_2() {
        return terminalID_2;
    }

    public void setTerminalID_2(String terminalID_2) {
        this.terminalID_2 = terminalID_2;
    }

    public String getMqttIP() {
        return mqttIP;
    }

    public void setMqttIP(String mqttIP) {
        this.mqttIP = mqttIP;
    }

    public String getMqttPort() {
        return mqttPort;
    }

    public void setMqttPort(String mqttPort) {
        this.mqttPort = mqttPort;
    }

    //https://www.mkyong.com/java/java-properties-file-examples/
    public void loadFromProperteis(){
        Properties prop = new Properties();
        InputStream input = null;

        float DEFAULT_PROXIMITY = 0;
        float DEFAULT_ACCELERATION_X = 20;
        float DEFAULT_ACCELERATION_Y = 20;
        float DEFAULT_ACCELERATION_Z = 20;
        boolean DEFAULT_ALAR_WHEN_BELOW_P = true;
        boolean DEFAULT_ALAR_WHEN_BELOW_X = false;
        boolean DEFAULT_ALAR_WHEN_BELOW_Y = false;
        boolean DEFAULT_ALAR_WHEN_BELOW_Z = false;
        String DEFAULT_TERMINAL_ID1 = "ffffffff-dd60-3fae-a032-38a447d2a861";
        String DEFAULT_TERMINAL_ID2 = "ffffffff-c203-6117-ffff-ffffc500be32";
        String DEFAULT_MQTT_IP = "localhost";
        String DEFAULT_MQTT_PORT= "1883";
        String DEFAULT_ALERT_TOPIC = "COMMON_ALERTS";
        int DEFAULT_FREQUENCY = 1;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            DEFAULT_PROXIMITY = Float.parseFloat(prop.getProperty("thresholdProximitySensor"));
            DEFAULT_ACCELERATION_X = Float.parseFloat(prop.getProperty("threshold_sensor_acceleration_x"));
            DEFAULT_ACCELERATION_Y = Float.parseFloat(prop.getProperty("threshold_sensor_acceleration_y"));
            DEFAULT_ACCELERATION_Z = Float.parseFloat(prop.getProperty("threshold_sensor_acceleration_z"));
            DEFAULT_ALAR_WHEN_BELOW_P = Boolean.parseBoolean(prop.getProperty("alarm_when_below_p"));
            DEFAULT_ALAR_WHEN_BELOW_X = Boolean.parseBoolean(prop.getProperty("alarm_when_below_x"));
            DEFAULT_ALAR_WHEN_BELOW_Y = Boolean.parseBoolean(prop.getProperty("alarm_when_below_y"));
            DEFAULT_ALAR_WHEN_BELOW_Z = Boolean.parseBoolean(prop.getProperty("alarm_when_below_z"));
            DEFAULT_TERMINAL_ID1 = prop.getProperty("terminalID_1");
            DEFAULT_TERMINAL_ID2 = prop.getProperty("terminalID_2");
            DEFAULT_MQTT_IP = prop.getProperty("mqttIP");
            DEFAULT_MQTT_PORT = prop.getProperty("mqttPort");
            DEFAULT_ALERT_TOPIC = prop.getProperty("alert_topic");
            DEFAULT_FREQUENCY = Integer.parseInt(prop.getProperty("frequency"));

        } catch (IOException ex) {
            System.out.println("Io exeption in write in prop");
        } finally {
            thresholdProximitySensor = new SimpleFloatProperty(this,"thresholdProximitySensor",DEFAULT_PROXIMITY);
            threshold_sensor_acceleration_x = new SimpleFloatProperty(this,"threshold_sensor_acceleration_x",DEFAULT_ACCELERATION_X);
            threshold_sensor_acceleration_y = new SimpleFloatProperty(this,"threshold_sensor_acceleration_y",DEFAULT_ACCELERATION_Y);
            threshold_sensor_acceleration_z = new SimpleFloatProperty(this,"threshold_sensor_acceleration_z",DEFAULT_ACCELERATION_Z);

            alarm_when_below_p = new SimpleBooleanProperty(this,"alarm_when_below_p",DEFAULT_ALAR_WHEN_BELOW_P);
            alarm_when_below_x = new SimpleBooleanProperty(this,"alarm_when_below_x",DEFAULT_ALAR_WHEN_BELOW_X);
            alarm_when_below_y = new SimpleBooleanProperty(this,"alarm_when_below_y",DEFAULT_ALAR_WHEN_BELOW_Y);
            alarm_when_below_z = new SimpleBooleanProperty(this,"alarm_when_below_z",DEFAULT_ALAR_WHEN_BELOW_Z);

            terminalID_1 = new String(DEFAULT_TERMINAL_ID1);
            terminalID_2 = new String(DEFAULT_TERMINAL_ID2);
            mqttIP = new String (DEFAULT_MQTT_IP);
            mqttPort = new String(DEFAULT_MQTT_PORT);
            alert_topic = new String(DEFAULT_ALERT_TOPIC);

            frequency = new SimpleIntegerProperty(this, "frequency",DEFAULT_FREQUENCY);

//            System.out.println(thresholdProximitySensor);
//            System.out.println(threshold_sensor_acceleration_x);
//            System.out.println(threshold_sensor_acceleration_y);
//            System.out.println(threshold_sensor_acceleration_z);
//            System.out.println(alarm_when_below_p);
//            System.out.println(alarm_when_below_x);
//            System.out.println(alarm_when_below_y);
//            System.out.println(alarm_when_below_z);
//            System.out.println(terminalID_1);
//            System.out.println(terminalID_2);
//            System.out.println(mqttPort);
//            System.out.println(mqttPort);

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public int getFrequency() {
        return frequency.get();
    }

    public IntegerProperty frequencyProperty() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency.set(frequency);
    }

    public void saveToProperteis(){
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("config.properties");


            prop.setProperty("thresholdProximitySensor", Float.toString(getThresholdProximitySensor()));
            prop.setProperty("threshold_sensor_acceleration_x",Float.toString(getThreshold_sensor_acceleration_x()));
            prop.setProperty("threshold_sensor_acceleration_y",Float.toString(getThreshold_sensor_acceleration_y()));
            prop.setProperty("threshold_sensor_acceleration_z",Float.toString(getThreshold_sensor_acceleration_z()));

            prop.setProperty("alarm_when_below_p", Boolean.toString(isAlarm_when_below_p()));
            prop.setProperty("alarm_when_below_x",  Boolean.toString(isAlarm_when_below_x()));
            prop.setProperty("alarm_when_below_y", Boolean.toString(isAlarm_when_below_y()));
            prop.setProperty("alarm_when_below_z",  Boolean.toString(isAlarm_when_below_z()));

            prop.setProperty("terminalID_1",getTerminalID_1());
            prop.setProperty("terminalID_2",getTerminalID_2());
            prop.setProperty("mqttIP",getMqttIP());

            prop.setProperty("mqttPort",getMqttPort());
            prop.setProperty("alert_topic",getAlert_topic());
            prop.setProperty("frequency",Integer.toString(getFrequency()));

            System.out.println(thresholdProximitySensor);
            System.out.println(threshold_sensor_acceleration_x);
            System.out.println(threshold_sensor_acceleration_y);
            System.out.println(threshold_sensor_acceleration_z);
            System.out.println(alarm_when_below_p);
            System.out.println(alarm_when_below_x);
            System.out.println(alarm_when_below_y);
            System.out.println(alarm_when_below_z);
            System.out.println(terminalID_1);
            System.out.println(terminalID_2);
            System.out.println(mqttPort);
            System.out.println(mqttPort);
            System.out.println(frequency);

            // save properties to project root folder
            prop.store(output, null);


        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getAlert_topic() {
        return alert_topic;
    }

    public void setAlert_topic(String alert_topic) {
        this.alert_topic = alert_topic;
    }
}

