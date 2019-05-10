package service;

import mqtt.ReceivedData;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import parser.ParserReceiver;
import settings.AdministratorSettings;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadReceiver extends Thread implements MqttCallback {
    private final AdministratorSettings settings;

    private String broker;
    private MemoryPersistence persistence = new MemoryPersistence();

    public volatile boolean shutdown_signalled = false;

    private LinkedBlockingQueue<ReceivedData> incomingQueue;


    //__________________________________________________________________________________________________________________
    public ThreadReceiver(LinkedBlockingQueue<ReceivedData> incomingQueue) {
        this.incomingQueue=incomingQueue;

        settings = AdministratorSettings.getInstance();
        //to do crisimi perioxi
        broker = "tcp://" + settings.getMqttIP() + ":" + settings.getMqttPort();
    }

    //to ask not takes Interrupted exeption
    public void run() {
        System.out.println("receiver is started");
        String topic1;
        String topic2;
        String clientId     = "JavaAdministrationSubscriber";
        int qos = 2;
        synchronized (settings) {
            topic1 = settings.getTerminalID_1();
            topic2 = settings.getTerminalID_2();
        }

        MqttClient sampleClient = null;
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(this);
            sampleClient.connect(connOpts);

            System.out.println("subscribing to topic " + topic1);
            sampleClient.subscribe(topic1, qos);
            System.out.println("Done");

            System.out.println("subscribing to topic " + topic2);
            sampleClient.subscribe(topic2, qos);
            System.out.println("Done");

            synchronized (this) {
                System.out.println("ThreadReceiver is waiting for shutdown");
                while (!shutdown_signalled) {
                    this.wait();

                }
            }
            // e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println( "InterruptedException in receiver");
        } catch (MqttException e) {
            System.out.println("MqttException in receiver");
        } finally {
            try {
                if (sampleClient != null) {
                    System.out.println("unsubscribing to topic " + topic1);
                    sampleClient.unsubscribe(topic1);
                    System.out.println("Done");

                    System.out.println("unsubscribing to topic " + topic2);
                    sampleClient.unsubscribe(topic2);
                    System.out.println("Done");
                }
            } catch (Exception e) {

            }
        }
    }

    public void connectionLost(Throwable throwable) {

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println( "  Message:\t" + new String(mqttMessage.getPayload()) + "  QoS:\t" + mqttMessage.getQos());
        String[] parser =  new String (mqttMessage.getPayload()).split("#");
        ParserReceiver parserReceiver = new ParserReceiver();
        ReceivedData receivedData =  parserReceiver.createObjectReceivedData(parser);
        System.out.println("incoming messaged added to queue from receiver");
        incomingQueue.put(receivedData);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}

//________________________parsing____________________________________________________
//                String message = "id500 10.00 100.00 proximity 10.10 Acceleration 7.1 8.1 9.1";
//                String[] parser = message.split(" ");
//               ReceivedData receivedData = createObjectReceivedData(parser);
//              System.out.println(receivedData);
//__________________________________________________________________________________


