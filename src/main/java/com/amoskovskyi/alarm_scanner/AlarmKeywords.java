package com.amoskovskyi.alarm_scanner;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AlarmKeywords {

    private String keywordsStr;
    private ArrayList<String> keywords = new ArrayList<>();

    public AlarmKeywords() {
        this.keywordsStr = "";
        this.keywords = new ArrayList<>();
    }

    public void updateKeywords(String words) {
        if (!words.isEmpty()) {
            keywordsStr = words;
        }
        keywords.clear();
        keywords.add("Error getting URL".toLowerCase());
        for (String word : keywordsStr.split(";")) {
            keywords.add(word.trim().toLowerCase());
        }
    }

    public void readKeywordFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("keyword.txt"))) {
            updateKeywords(br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            System.out.println("Cannot read 'keyword.txt'");
            updateKeywords("");
        }
    }

    public void saveKeywordFile() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("keyword.txt", false))) {
            out.print(keywordsStr);
        }
    }

    public String getKeywordsStr() {
        return keywordsStr;
    }

    public void setKeywordsStr(String keywordsStr) {
        this.keywordsStr = keywordsStr;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }
}
