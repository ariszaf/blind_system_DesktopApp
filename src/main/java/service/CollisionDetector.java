package service;

import mqtt.ReceivedData;
import settings.AdministratorSettings;


public class CollisionDetector {
    public boolean checkCollision(ReceivedData data, AdministratorSettings settings) {
        float currentProximityValue = data.getProximity();
        float x = data.getAcceleration_x();
        float y = data.getAcceleration_y();
        float z = data.getAcceleration_z();

        boolean alarm = false;

        synchronized (settings) {
            //______________________proximity___________________________________________________
            if (settings.isAlarm_when_below_p()) {
                if (currentProximityValue <= settings.getThresholdProximitySensor()) {
                    alarm = true;
                }
            } else {
                if (currentProximityValue >= settings.getThresholdProximitySensor()) {
                    alarm = true;
                }
            }
            //_________________acceleration_____________________________________________________
            if (settings.isAlarm_when_below_x()) {
                if (x <= settings.getThreshold_sensor_acceleration_x()) {
                    alarm = true;
                }
            } else {
                if (x >= settings.getThreshold_sensor_acceleration_x()) {
                    alarm = true;
                }
            }

            if (settings.isAlarm_when_below_y()) {
                if (y <= settings.getThreshold_sensor_acceleration_y()) {
                    alarm = true;
                }
            } else {
                if (y >= settings.getThreshold_sensor_acceleration_y()) {
                    alarm = true;
                }
            }

            if (settings.isAlarm_when_below_z()) {
                if (z <= settings.getThreshold_sensor_acceleration_z()) {
                    alarm = true;
                }
            } else {
                if (z >= settings.getThreshold_sensor_acceleration_z()) {
                    alarm = true;
                }
            }
        }
        //__________________________________________________________________________________

        return alarm;
    }
}
