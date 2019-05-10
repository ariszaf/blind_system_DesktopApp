package mymain;

import database.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mqtt.ReceivedData;
import mqtt.SendingData;
import service.ThreadComputationalService;
import service.ThreadReceiver;
import service.ThreadSender;
import settings.AdministratorSettings;


import java.util.concurrent.LinkedBlockingQueue;

public class Main extends Application {
    private LinkedBlockingQueue<ReceivedData> incomingQueue = new LinkedBlockingQueue<ReceivedData>();
    private LinkedBlockingQueue<SendingData> outgoingQueue = new LinkedBlockingQueue<SendingData>();
    private AdministratorSettings administratorSettings = AdministratorSettings.getInstance();


    private final ThreadComputationalService compServiceThread = new ThreadComputationalService(incomingQueue, outgoingQueue);
    private final ThreadReceiver receiverThread = new ThreadReceiver(incomingQueue);
    private final ThreadSender senderThread = new ThreadSender(outgoingQueue);

    @Override
    public void start(Stage primaryStage) throws Exception{

        administratorSettings.loadFromProperteis();

        compServiceThread.start();
        receiverThread.start();
        senderThread.start();

//
//        SensorDAO sensorDAO = new SensorDAO();
//
//        for(int i=0; i<100; i++){
//            Sensor sensor = new Sensor("test"+i,"hsqhj");
//            sensorDAO.insert(sensor);
//        }


        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("maincontroller.fxml"));
        primaryStage.setTitle("Admin");
        primaryStage.getIcons().add(new Image("/icons/login-icon.jpg"));
        primaryStage.setScene(new Scene(root, 1300, 900));

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.out.println("Interrupting threads ... ");

        synchronized(receiverThread) {
            receiverThread.shutdown_signalled = true;
            receiverThread.notify();
        }
        compServiceThread.interrupt();
        receiverThread.interrupt();
        senderThread.interrupt();

        System.out.println("Join threads ... ");
        compServiceThread.join();
        receiverThread.join();
        senderThread.join();
        DbConnection dbConnection = new DbConnection();
        dbConnection.Disconnect();

        System.out.println("All threads have finished");
        administratorSettings.saveToProperteis();
        System.out.println("save to propertesi");
        System.out.println("The end.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}




//        Date date = new Date();
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        try {
//            date = format.parse("2016-12-28 01:10:50.123123");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        java.sql.Date sq = new java.sql.Date(date.getTime());
//        SampleDAO sDAO = new SampleDAO();
//        Sample mymain = new Sample(sq,"2017-12-28 01:00:00.009000",false,"android_1234567891abedfc");
//        sDAO.insert(mymain);

//        ReportDAO reportDAO = new ReportDAO();
//        Report report = new Report();
//        reportDAO.update(report);
//        reportDAO.update(new Report("android_1234567890abedfc", "2017-12-10 10:38:56.567000", "2016-12-10 10:38:56.567000", 23.45, 45.89, 5.8f, 9.0f, 7.0f, 9.9f,false));

//        ReportDAO reportDAO = new ReportDAO();
//        for(int i=0; i<30; i++){
//            reportDAO.insert(new Report("android_1234567890abedfc", "2017-12-10 10:38:56.567000", "2015-12-10 10:38:56.567000", 23.45, 45.89, 5.8f, 9.0f, 7.0f, 9.9f,false));
//        }



