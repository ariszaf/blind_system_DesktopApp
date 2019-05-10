package gui;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import settings.AdministratorSettings;
import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable{

    private final AdministratorSettings administratorSettings = AdministratorSettings.getInstance();

    @FXML
    private TextField settings_device1,settings_device2,settings_alert_topic,settings_mqtt_port, settings_mqtt_ip;


    @FXML
    private TextField settings_acceleration_z_value,settings_acceleration_y_value,settings_acceleration_x_value,settings_proximity_value,settings_frequncy;

    //_________________________________________________________________________________________________________check_box
    @FXML
    private CheckBox settings_acceleration_x_check_button,settings_proximity_check_button,settings_acceleration_y_check_button,settings_acceleration_z_check_button;

    //http://www.regular-expressions.info/floatingpoint.html
    //http://regexlib.com/Search.aspx?k=float&c=-1&m=5&ps=50
    private ChangeListener<String> forfloatpositive = (observable, oldValue, newValue) -> {
        if (!newValue.matches("[+]?[0-9]*\\.?[0-9]*")) {
            ((StringProperty) observable).set(oldValue);
        }
    };

    private ChangeListener<String> forallfloat = (observable, oldValue, newValue) -> {
        if (!newValue.matches("[-]?\\d*\\.?\\d* {0,9}")) {
            ((StringProperty) observable).set(oldValue);
        }
    };


    private ChangeListener<String> forallineger = (observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d{0,10}$")) {
            ((StringProperty) observable).set(oldValue);
        }
    };


    public void initialize(URL location, ResourceBundle resources) {

        settings_proximity_value.setText(Float.toString(administratorSettings.getThresholdProximitySensor()));
        settings_acceleration_x_value.setText(Float.toString(administratorSettings.getThreshold_sensor_acceleration_x()));
        settings_acceleration_y_value.setText(Float.toString(administratorSettings.getThreshold_sensor_acceleration_y()));
        settings_acceleration_z_value.setText(Float.toString(administratorSettings.getThreshold_sensor_acceleration_z()));
        settings_frequncy.setText(Integer.toString(administratorSettings.getFrequency()));


        settings_proximity_check_button.setSelected(!administratorSettings.isAlarm_when_below_p());
        settings_acceleration_x_check_button.setSelected(!administratorSettings.isAlarm_when_below_x());
        settings_acceleration_y_check_button.setSelected(!administratorSettings.isAlarm_when_below_y());
        settings_acceleration_z_check_button.setSelected(!administratorSettings.isAlarm_when_below_z());


        System.out.println(administratorSettings.isAlarm_when_below_p());
        System.out.println(administratorSettings.isAlarm_when_below_x());
        System.out.println(administratorSettings.isAlarm_when_below_y());
        System.out.println(administratorSettings.isAlarm_when_below_z());
        

        settings_device1.setText(administratorSettings.getTerminalID_1());
        settings_device2.setText(administratorSettings.getTerminalID_2());
        settings_alert_topic.setText(administratorSettings.getAlert_topic());
        settings_mqtt_port.setText(administratorSettings.getMqttPort());
        settings_mqtt_ip.setText(administratorSettings.getMqttIP());


        settings_proximity_value.textProperty().addListener(forfloatpositive);
        settings_acceleration_x_value.textProperty().addListener(forallfloat);
        settings_acceleration_y_value.textProperty().addListener(forallfloat);
        settings_acceleration_z_value.textProperty().addListener(forallfloat);
        settings_frequncy.textProperty().addListener(forallineger);


        settings_device1.setDisable(true);
        settings_device2.setDisable(true);
        settings_alert_topic.setDisable(true);
        settings_mqtt_port.setDisable(true);
        settings_mqtt_ip.setDisable(true);
    }

    @FXML
    private void proximityCheckButtonClick() {
        if(settings_proximity_check_button.isSelected()){
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_p(!settings_acceleration_x_check_button.isSelected());//true
            }
        }else {
            synchronized (administratorSettings) {
                administratorSettings.setAlarm_when_below_p(settings_acceleration_x_check_button.isSelected());//false
            }
        }
        System.out.println(administratorSettings.isAlarm_when_below_p() + " alarm when below proximity");
    }

    @FXML
    private void accelerationXCheckButtonClick() {
        if(!settings_acceleration_x_check_button.isSelected()){
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_x(true);
            }
        }else{
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_x(false);
            }
        }
        System.out.println(administratorSettings.isAlarm_when_below_x() + " alarm when below x");
    }

    @FXML
    private void accelerationYCheckButtonClick() {
        if(!settings_acceleration_y_check_button.isSelected()){
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_y(true);
            }
        }else {
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_y(false);
            }
        }
        System.out.println(administratorSettings.isAlarm_when_below_y() + " alarm when below y");
    }

    @FXML
    private void accelerationZCheckButtonClick() {
        if(!settings_acceleration_z_check_button.isSelected()){
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_z(true);
            }
        }else{
            synchronized (administratorSettings){
                administratorSettings.setAlarm_when_below_z(false);
            }
        }
        System.out.println(administratorSettings.isAlarm_when_below_z() + " alarm when below z");
    }

    @FXML
    private void onClickSaveButton() {

        float proximity = 0;
        float acc_x = 0;
        float acc_y = 0;
        float acc_z = 0;
        int counter = 0;
        int freq = 0;

        try {
            proximity = Float.parseFloat(settings_proximity_value.getText());
            counter++;
        }catch (NumberFormatException e){
            System.out.println("proximity error");
            settings_proximity_value.setPromptText("error");
        }

        try {
            acc_x = Float.parseFloat(settings_acceleration_x_value.getText());
            counter++;

        }catch (NumberFormatException e){
            System.out.println("acc x error");
            settings_acceleration_x_value.setPromptText("error");
        }

        try {
            acc_y = Float.parseFloat(settings_acceleration_y_value.getText());
            counter++;

        }catch (NumberFormatException e){
            System.out.println("acc y error");
            settings_acceleration_y_value.setPromptText("error");

        }
        try {
            acc_z = Float.parseFloat(settings_acceleration_z_value.getText());
            // System.out.println(acc_z + " acc z");
            counter++;
        }catch (NumberFormatException e){
            System.out.println("acc z error");
            settings_acceleration_z_value.setPromptText("error");
        }
        try{
            freq = Integer.parseInt(settings_frequncy.getText());
            counter++;
        }catch (NumberFormatException e){
            System.out.print("frequncy error");
            settings_frequncy.setPromptText("error");
        }

        if(counter == 5) {
            System.out.println("ok insert");
            synchronized (administratorSettings) {
                administratorSettings.setThresholdProximitySensor(proximity);
                administratorSettings.setThreshold_sensor_acceleration_x(acc_x);
                administratorSettings.setThreshold_sensor_acceleration_y(acc_y);
                administratorSettings.setThreshold_sensor_acceleration_z(acc_z);
                administratorSettings.setFrequency(freq);
            }
        }

        System.out.println("stored in singleton");
        System.out.println(administratorSettings.getThresholdProximitySensor());
        System.out.println(administratorSettings.getThreshold_sensor_acceleration_x());
        System.out.println(administratorSettings.getThreshold_sensor_acceleration_y());
        System.out.println(administratorSettings.getThreshold_sensor_acceleration_z());
        System.out.println(administratorSettings.isAlarm_when_below_p());
        System.out.println(administratorSettings.isAlarm_when_below_x());
        System.out.println(administratorSettings.isAlarm_when_below_y());
        System.out.println(administratorSettings.isAlarm_when_below_z());
        System.out.println(administratorSettings.getFrequency());

    }
}


//        System.out.println("stored in singleton");
//        System.out.println(administratorSettings.getThresholdProximitySensor());
//        System.out.println(administratorSettings.getThreshold_sensor_acceleration_x());
//        System.out.println(administratorSettings.getThreshold_sensor_acceleration_y());
//        System.out.println(administratorSettings.getThreshold_sensor_acceleration_z());

//______________________________________________________________________________________________________________
//        System.out.println(administratorSettings.getThresholdProximitySensor());
//        System.out.println(administratorSettings.getThreshold_sensor_acceleration_x());
//        System.out.println(administratorSettings.getThreshold_sensor_acceleration_y());
//        System.out.println(administratorSettings.getThreshold_sensor_acceleration_z());
//        System.out.println(administratorSettings.isAlarm_when_below_p());
//        System.out.println(administratorSettings.isAlarm_when_below_x());
//        System.out.println(administratorSettings.isAlarm_when_below_y());
//        System.out.println(administratorSettings.isAlarm_when_below_z());
