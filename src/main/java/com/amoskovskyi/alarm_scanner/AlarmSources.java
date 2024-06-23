package com.amoskovskyi.alarm_scanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class AlarmSources {

    private ArrayList<Source> sources;

    public AlarmSources() {
        this.sources = new ArrayList<>();
    }

    public void updateSources(String sourcesStr) {
        sources.clear();
        if (sourcesStr.isEmpty()) {
            sources.add(new Source("Empty list"));
        }
        for (String url : sourcesStr.split("\n")) {
            sources.add(new Source(url.trim()));
        }
    }

    public void readSourcesFile() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(Paths.get("source.txt")), StandardCharsets.UTF_8))) {
            updateSources(br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            System.out.println("Cannot read 'source.txt'");
        }
    }

    public void saveSourcesFile() throws IOException {
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream("source.txt", false), StandardCharsets.UTF_8))) {
            out.print(alarmSourcesToString());
        }
    }

    public String alarmSourcesToString() {
        StringJoiner sj = new StringJoiner("\n");
        for (Source source : sources) {
            sj.add(source.getUrl());
        }
        return sj.toString();
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }
}
