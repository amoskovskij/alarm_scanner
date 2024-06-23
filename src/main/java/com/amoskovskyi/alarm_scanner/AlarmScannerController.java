package com.amoskovskyi.alarm_scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
    private CheckBox noSoundCheckBox;
    @FXML
    public TextArea keywordsInput;
    @FXML
    private Text messageQueueText;
    @FXML
    private Button refreshButton;
    @FXML
    private Label alarmLabel;
    @FXML
    private TextArea messageQueueOutput;

    boolean alarm;
    boolean isSoundOn;
    boolean isNeedRefresh;
    AlarmKeywords alarmKeywords;
    AlarmSources alarmSources;

    public void initialize() {
        this.messageQueueOutput.setScrollTop(Double.MAX_VALUE);
        this.isSoundOn = !noSoundCheckBox.isSelected();
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
        refreshButton.setText("Updating...");
        refreshButton.setDisable(true);
        isNeedRefresh = true;
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
            alarm = false;
            int begin = 0;
            int end = 0;
            for (String word : alarmKeywords.getKeywords()) {
                begin = messageQueueOutput.toLowerCase().lastIndexOf(word);
                if (begin != -1) {
                    alarm = true;
                    alarmLabel.setText("Alarm: " + word + " !!!");
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

    public void setAlarmVisible() {
        this.alarmLabel.setVisible(true);
    }

    public void setAlarmInvisible() {
        this.alarmLabel.setVisible(false);
    }

    public void onEndUpdating() {
        refreshButton.setText("Refresh");
        refreshButton.setDisable(false);
        setKeywordsInput(alarmKeywords.getKeywordsStr());
        setSourcesInput(alarmSources.alarmSourcesToString());
    }

    @FXML
    protected void onNoSoundCheckBoxChange() {
        isSoundOn = !isSoundOn;
    }
}