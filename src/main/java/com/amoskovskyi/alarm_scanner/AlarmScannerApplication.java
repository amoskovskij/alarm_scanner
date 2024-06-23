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

public class AlarmScannerApplication extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AlarmScannerApplication.class.getResource("alarm_scanner-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 706);
        stage.setTitle("Alarm scanner");
        AlarmScannerController controller = fxmlLoader.getController();

        Task<Integer> task = new Task<Integer>() {
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
    }

    public static void myMain() {
        launch();
    }

    private void onCloseStage(AlarmScannerController controller, Task<Integer> task) {
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
        Deque<ArrayList<Message>> messageQueue = new ArrayDeque<>();
        ArrayList<Message> messages;
        while (true) {

            try {
                for (Source source : controller.alarmSources.getSources()) {
                    
                    controller.alarm = false;
                    if (controller.isNeedRefresh) {
                        controller.isNeedRefresh = false;
                        controller.alarmKeywords.updateKeywords(controller.getKeywordsInput());
                        controller.alarmSources.updateSources(controller.getSourcesInput());
                        Platform.runLater(controller::onEndUpdating);
                        break;
                    }

                    messages = source.getPosts();
                    messageQueue.add(messages);
                    if (messageQueue.size() > controller.alarmSources.getSources().size()) {
                        messageQueue.pop();
                    }

                    messages = new ArrayList<>();
                    for (ArrayList<Message> messagesOfSource : messageQueue) {
                        messages.addAll(messagesOfSource);
                    }
                    messages.sort(Comparator.comparing(Message::getDateTime));
                    StringJoiner sj = new StringJoiner("\n\n");
                    for (Message message : messages) sj.add(message.getMessageStr());
                    Platform.runLater(() -> controller.setMessageQueueOutput(sj.toString()));

                    int jEnd = sleepDuration / (blink*3) * 2;
                    for (int j = 0; j < jEnd; j++) {
                        if (controller.alarm && j % 2 == 0) {
                            if (controller.isSoundOn) Toolkit.getDefaultToolkit().beep();
                            Platform.runLater(controller::setAlarmVisible);
                        } else if (controller.alarm) {
                            if (controller.isSoundOn) Toolkit.getDefaultToolkit().beep();
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