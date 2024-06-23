package com.amoskovskyi.alarm_scanner;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class AlarmScannerController {

    @FXML
    private Text sourcesText;
    @FXML
    private TextArea sourcesInput;
    @FXML
    private Text keywordsText;
    @FXML
    public TextArea keywordsInput;
    @FXML
    private Text messageQueueText;
    @FXML
    private Label refreshedLabel;
    @FXML
    private Label alarm;
    @FXML
    private TextArea messageQueueOutput;

    boolean isNeedRefresh;
    AlarmKeywords alarmKeywords;
    AlarmSources alarmSources;

    public void initialize() {
        this.messageQueueOutput.setScrollTop(Double.MAX_VALUE);
        this.isNeedRefresh = false;
        this.alarmKeywords = new AlarmKeywords();
        this.alarmKeywords.readKeywordFile();
        this.alarmSources = new AlarmSources();
        this.alarmSources.readSourcesFile();
        setKeywordsInput(alarmKeywords.getKeywordsStr());
        setSourcesInput(alarmSources.alarmSourcesToString());
    }

    @FXML
    protected void onRefreshButtonClick() {
        isNeedRefresh = true;
        refreshedLabel.setVisible(true);
    }

    public String getKeywordsInput() {
        return keywordsInput.getText();
    }

    public void setKeywordsInput(String keywordsInput) {
        this.keywordsInput.setText(keywordsInput);
    }

    public String getSourcesInput() {
        return sourcesInput.getText();
    }

    public void setSourcesInput(String sourcesInput) {
        this.sourcesInput.setText(sourcesInput);
    }

    public void setMessageQueueOutput(String messageQueueOutput) {
        try {
            int begin = 0;
            int end = 0;
            for (String word : alarmKeywords.getKeywords()) {
                begin = messageQueueOutput.toLowerCase().lastIndexOf(word);
                if (begin != -1) {
                    end = begin + word.length();
                    break;
                }
            }
            this.messageQueueOutput.setText(messageQueueOutput);
            this.messageQueueOutput.selectRange(begin,end);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setAlarmText(String alarmText) {
        try {
            this.alarm.setText(alarmText);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setAlarmVisible() {
        this.alarm.setVisible(true);
    }

    public void setAlarmInvisible() {
        this.alarm.setVisible(false);
    }

    public void setRefreshedLabelInvisible() {
        this.refreshedLabel.setVisible(false);
    }
}