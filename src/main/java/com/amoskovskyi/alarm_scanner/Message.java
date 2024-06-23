package com.amoskovskyi.alarm_scanner;

import org.jsoup.Jsoup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private String message;
    private String url;
    private LocalDateTime dateTime;

    public Message(String message, String url, LocalDateTime dateTime) {
        this.message = message;
        this.url = url;
        this.dateTime = dateTime;
    }

    public String getMessageStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        return url + ":\n" + message + "\n" + dateTime.format(formatter);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
