package com.amoskovskyi.alarm_scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        } else {
            keywordsStr = "Empty list";
        }
        keywords.clear();
        keywords.add("Error getting URL".toLowerCase());
        for (String word : keywordsStr.split(";")) {
            keywords.add(word.toLowerCase());
        }
    }

    public void readKeywordFile() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(Paths.get("keyword.txt")), StandardCharsets.UTF_8))) {
            updateKeywords(br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            System.out.println("Cannot read 'keyword.txt'");
            updateKeywords("");
        }
    }

    public void saveKeywordFile() throws IOException {
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream("keyword.txt", false), StandardCharsets.UTF_8))) {
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
