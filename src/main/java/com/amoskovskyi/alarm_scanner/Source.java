package com.amoskovskyi.alarm_scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class Source {

    private String url;
    private ArrayList<Message> posts;
    private final static Duration minutesOfRelevance = Duration.ofMinutes(15); // 15 minuts
    private Document emptyDoc;
    private Document doc;

    public Source(String url) {
        this.url = url;
        this.posts = new ArrayList<>();
        this.emptyDoc = Jsoup.parse(
                "<html><head><title>Error getting URL</title></head><body>"
                        + "<div class=\"tgme_widget_message_wrap js-widget_message_wrap\">"
                        + "<div class=\"tgme_widget_message_text js-message_text\">"
                        + "Error getting URL"
                        + "</div>"
                        + "<time class=\"time\" datetime=\""
                        + "2024-06-20T00:00:00+00:00"
                        + "\"></time>"
                        + "</div>"
                        + "</body></html>");
    }

    private void getLastPosts() {
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println(e);
            emptyDoc.select("tgme_widget_message_text js-message_text")
                    .html("Error getting URL " + e);
            emptyDoc.select("time")
                    .attr("datetime",
                            String.valueOf(OffsetDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS)));
            doc = emptyDoc;
        }
        posts.clear();
        OffsetDateTime now = OffsetDateTime.now();
        Elements reply;
        Elements txt;
        Elements time;
        OffsetDateTime postTime;
        Duration ageFrom; // post's age from 15 minutes ago
        Elements messages = doc.getElementsByClass("tgme_widget_message_wrap js-widget_message_wrap");
        for (Element message : messages) {
            reply = message.getElementsByClass("tgme_widget_message_text js-message_reply_text");
            txt = message.getElementsByClass("tgme_widget_message_text js-message_text");
            time = message.getElementsByClass("time");
            try {
                postTime = OffsetDateTime.parse(time.attr("datetime"), ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneId.of("EET"))
                        .toOffsetDateTime();
            } catch (Exception e) {
                postTime = OffsetDateTime.now();
            }
            ageFrom = Source.minutesOfRelevance.minus(Duration.between(postTime, now));
            if (ageFrom.negated().isNegative()) {
                posts.add(new Message((reply.isEmpty() ? "" : ("Reply: " + processTxt(reply) + "\n"))
                        + processTxt(txt), url, postTime.toLocalDateTime()));
            }
        }
        if (posts.isEmpty()) {
            posts.add(new Message("No recent posts found", url, LocalDateTime.now()));
        }
    }

    private String processTxt(Elements txt) {
        for (Element el : txt.select("a, tg-emoji, i.emoji")) {
            el.select("*").remove();
            el.remove();
        }
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String str = txt.html().replaceAll("<br>", "\n").replaceAll("&nbsp;", " ");
        str = Jsoup.clean(str, "", Safelist.none(), outputSettings);
        str = str.replaceAll("(\\s*\n+\\s*)+", "\n");
        return str.trim();
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Message> getPosts() {
        getLastPosts();
        return posts;
    }
}
