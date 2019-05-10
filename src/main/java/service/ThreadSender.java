package service;

import mqtt.SendingData;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import settings.AdministratorSettings;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadSender extends Thread {
    private final AdministratorSettings settings;
    private LinkedBlockingQueue<SendingData> outgoingQueue;

    //_________________________________________________
    private String broker;
    private MemoryPersistence persistence = new MemoryPersistence();


    //_______________________________________________
    public ThreadSender(LinkedBlockingQueue<SendingData> outgoingQueue) {
        this.outgoingQueue = outgoingQueue;
        settings = AdministratorSettings.getInstance();

        broker = "tcp://" + settings.getMqttIP() + ":" + settings.getMqttPort();
    }

    public void run() {
        System.out.println("Thread sender is started");
        String clientId = "JavaAdministrationPublisher";
        int qos = 2;
        String topic;

        synchronized (settings) {
            topic = settings.getAlert_topic();
        }

        MqttClient sampleClient = null;
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to topic alert: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            try {
                while (!isInterrupted()) {
                    System.out.println("Sender is waiting for a collision to be produced");
                    System.out.println("Connecting to broker: " + topic);
                    SendingData data = outgoingQueue.take();
                    System.out.println("SENDING: " + data);

                    if (data.getSub_option() == 0) {
                        System.out.println("Terminal 1 crash - Publishing message: " + Integer.toString(data.getSub_option()));
                        MqttMessage message = new MqttMessage(settings.getTerminalID_1().getBytes());
                        message.setQos(qos);
                        sampleClient.publish(topic, message);
                    } else if (data.getSub_option() == 1) {
                        System.out.println("Terminal 2 crash - Publishing message: " + Integer.toString(data.getSub_option()));
                        MqttMessage message = new MqttMessage(settings.getTerminalID_2().getBytes());
                        message.setQos(qos);
                        sampleClient.publish(topic, message);
                    } else {
                        System.out.println("Terminal 1+2 crash - Publishing message: " + Integer.toString(data.getSub_option()));
                        MqttMessage message = new MqttMessage(Integer.toString(0).getBytes());
                        message.setQos(qos);
                        sampleClient.publish(topic, message);
                    }
                }
            } catch (InterruptedException e) {
                sampleClient.disconnect();
                sampleClient = null;
            }
        } catch (MqttException e) {

            //to do na kanw disconect se periptosi pou mou skasi minima edw
        }finally {
            if(sampleClient != null){
                System.out.println("disconnect client");
                try {
                    sampleClient.disconnect();
                } catch (MqttException e) {
                    System.out.println("MqttExeption thread sender client");
                }
            }
        }
    }
}
//To do is correct finally