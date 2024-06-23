package com.amoskovskyi.alarm_scanner;

import java.io.*;
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
        for (String url : sourcesStr.split("\n")) {
            sources.add(new Source(url.trim()));
        }
    }

    public void readSourcesFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("source.txt"))) {
            updateSources(br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            System.out.println("Cannot read 'source.txt'");
        }
    }

    public void saveSourcesFile() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("source.txt", false))) {
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
