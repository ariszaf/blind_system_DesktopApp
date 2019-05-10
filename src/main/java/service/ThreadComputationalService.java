package service;

import dao.ReportDAO;
import mqtt.ReceivedData;
import mqtt.SendingData;
import report.Report;
import settings.AdministratorSettings;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;


public class ThreadComputationalService extends Thread {
    private final AdministratorSettings settings;
    private LinkedBlockingQueue<ReceivedData> incomingQueue;
    private LinkedBlockingQueue<SendingData> outgoingQueue;
    private ReceivedData lastDataTerm1 = null;
    private ReceivedData lastDataTerm2 = null;
    private boolean lastConfirmed = false;

    public ThreadComputationalService(LinkedBlockingQueue<ReceivedData> incomingQueue, LinkedBlockingQueue<SendingData> outgoingQueue) {
        this.incomingQueue = incomingQueue;
        this.outgoingQueue = outgoingQueue;
        this.settings = AdministratorSettings.getInstance();
    }

    public void run() {
        CollisionDetector detector = new CollisionDetector();

        System.out.println("thread compute is started");
        try {
            while (!isInterrupted()) {
               // long start = System.currentTimeMillis();
                do {
                    ReceivedData data = incomingQueue.take();
                    System.out.println("COMPUTATIONAL SERVICE - RECEIVED: " + data);

                    boolean collisiondected = detector.checkCollision(data, settings);

                    int x = 0;

                    if (collisiondected) {
                        if (data.getTerminal_name().equals(settings.getTerminalID_1())) {
                            x = 0;
                        }
                        if (data.getTerminal_name().equals(settings.getTerminalID_2())) {
                            x = 1;
                        }
                        if (x == 0 && lastDataTerm2 != null) {
                            long dt = data.getDate_time().getTime() - lastDataTerm2.getDate_time().getTime();
                            if (dt <= 1000) {
                                x = 2;
                            }
                        } else if (x == 1 && lastDataTerm1 != null) {
                            long dt = data.getDate_time().getTime() - lastDataTerm1.getDate_time().getTime();
                            if (dt <= 1000) {
                                x = 2;
                            }
                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        Timestamp temp = new Timestamp(System.currentTimeMillis());
                        String serverDate = dateFormat.format(temp);

                        String clientDate = dateFormat.format(data.getDate_time());

                        System.out.println(clientDate);
                        System.out.println(serverDate);


                        boolean confirmed = (x == 2);
                        ReportDAO dao = new ReportDAO();
                        Report report = new Report(data.getTerminal_name(), serverDate,
                                clientDate,
                                data.getLongitude(),
                                data.getLatitude(),
                                data.getProximity(),
                                data.getAcceleration_x(),
                                data.getAcceleration_y(),
                                data.getAcceleration_z(),
                                confirmed);

                        switch (x) {
                            case 0: // mono to term1
                            case 1:
                                dao.insert(report);
                                break;
                            case 2:
                                dao.insert(report);
                                if (confirmed != lastConfirmed) {
                                    dao.update(report);
                                }
                                break;
                        }

                        // check if .... within 1 sec
                        System.out.println("COMPUTATIONAL SERVICE - x= " + x + " PRODUCED: " + data);

                        SendingData sendingData = new SendingData(x);
                        outgoingQueue.put(sendingData);

                        lastConfirmed = confirmed;

                        if (data != null) {
                            if (data.getTerminal_name().equals(settings.getTerminalID_1())) {
                                lastDataTerm1 = data;
                            }

                            if (data.getTerminal_name().equals(settings.getTerminalID_2())) {
                                lastDataTerm2 = data;
                            }
                        }
                    } else {
                        System.out.println("Received data ignored ");
                        lastConfirmed = false;
                    }

                } while (incomingQueue.size()>0);//the list to be empty

                int frequency;
                synchronized (settings){
                    frequency = settings.getFrequency();
                }

                Thread.sleep(frequency);

//                long stop = System.currentTimeMillis();
//                long diff = stop-start;
//
//                if (frequency-diff > 0) {
//                    Thread.sleep(frequency-diff);
//                }
            }
        } catch (InterruptedException e) {
        }
    }
}


