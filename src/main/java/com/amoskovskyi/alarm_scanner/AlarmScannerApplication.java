package com.amoskovskyi.alarm_scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class AlarmScannerApplication extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init()");
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AlarmScannerApplication.class.getResource("alarm_scanner-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 706);
        stage.setTitle("Alarm scanner");
        AlarmScannerController controller = fxmlLoader.getController();

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                return alarmScanner(controller);
            }
        };

        stage.setOnCloseRequest(event -> onCloseStage(controller, task));
        stage.setScene(scene);
        stage.show();
        new Thread(task).start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop()");
    }

    public static void main(String[] args) {
        launch();
    }

    private void onCloseStage(AlarmScannerController controller, Task<Integer> task) {
        System.out.println("Stage is closing");
        task.cancel(true);
        // Save files
        controller.alarmKeywords.updateKeywords(controller.getKeywordsInput());
        controller.alarmSources.updateSources(controller.getSourcesInput());
        try {
            controller.alarmKeywords.saveKeywordFile();
            controller.alarmSources.saveSourcesFile();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private int alarmScanner(AlarmScannerController controller) {
        int iter = 0;
        int sleepDuration = 30000; // microseconds
        int blink = 1000; // microseconds
        boolean alarm;
        Deque<ArrayList<Message>> messageQueue = new ArrayDeque<>();
        ArrayList<Message> lastMessages;
        while (true) {

            try {
                for (Source source : controller.alarmSources.getSources()) {

                    alarm = false;
                    if (controller.isNeedRefresh) {
                        controller.isNeedRefresh = false;
                        controller.alarmKeywords.updateKeywords(controller.getKeywordsInput());
                        controller.alarmSources.updateSources(controller.getSourcesInput());
                        Platform.runLater(controller::setRefreshedLabelInvisible);
                        break;
                    }

                    lastMessages = source.getPosts();
                    messageQueue.add(lastMessages);
                    if (messageQueue.size() > controller.alarmSources.getSources().size()) {
                        messageQueue.pop();
                    }

                    List<Message> messagesList = new ArrayList<>();
                    for (ArrayList<Message> messagesOfSource : messageQueue) {
                        for (Message message : messagesOfSource) {
                            messagesList.add(message);
                            for (String word : controller.alarmKeywords.getKeywords()) {
                                if (message.getMessage().toLowerCase().contains(word)) {
                                    alarm = true;
                                    Platform.runLater(() -> controller.setAlarmText("Alarm: " + word + " !!!"));
                                    break;
                                }
                            }
                        }
                    }
                    messagesList.sort(Comparator.comparing(Message::getDateTime));
                    StringJoiner sj = new StringJoiner("\n\n");
                    for (Message message : messagesList) sj.add(message.getMessageStr());
                    Platform.runLater(() -> controller.setMessageQueueOutput(sj.toString()));

                    int jEnd = sleepDuration / (blink*3) * 2;
                    for (int j = 0; j < jEnd; j++) {
                        if (alarm && j % 2 == 0) {
                            Toolkit.getDefaultToolkit().beep();
                            Platform.runLater(controller::setAlarmVisible);
                        } else if (alarm) {
                            Toolkit.getDefaultToolkit().beep();
                            Platform.runLater(controller::setAlarmInvisible);
                        }

                        if (j % 2 == 0) {
                            try {
                                Thread.sleep(blink * 2);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                Thread.sleep(blink);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    iter++;
                }
            } catch (Exception e) {
                Platform.runLater(() -> controller.setMessageQueueOutput(String.valueOf(e)));
                break;
            }

        }
        return iter;
    }
}