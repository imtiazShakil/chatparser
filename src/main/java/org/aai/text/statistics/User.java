package org.aai.text.statistics;

import org.aai.analytics.ai.keywordExtraction.KeywordExtraction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private ArrayList<Comment> commentsList;

    private int totalWords = 0;
    private List<String> keywords = new ArrayList<>();

    public User(String name) {
        this.name = name;
        this.commentsList = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        if (comment.name.equals(this.name))
            this.commentsList.add(comment);
    }
    public ArrayList<Comment> getCommentsList() {
        return this.commentsList;
    }
    public String getName() {
        return this.name;
    }
    public void generateStatistics() {
        KeywordExtraction keyExtraction = KeywordExtraction.getInstance();
        StringBuilder sb = new StringBuilder();
        for (Comment comment : commentsList) {
            for (String text : comment.textList) {
                sb.append(text).append(". ");
                String[] wordList = text.split(" ");
                totalWords += wordList.length;
            }
        }
        keywords = keyExtraction.process(sb.toString());
    }

    public void dumpStatistics(String dir) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(dir + "/" + this.name + ".txt"), "utf-8"))) {
            writer.write("Attendee: " + this.name);
            writer.newLine();
            writer.write("Message Count: " + this.commentsList.size());
            writer.newLine();
            writer.write("Word Count: " + this.totalWords);
            writer.newLine();
            writer.write("Top Keywords: ");
            for (String kkword : keywords) writer.write(kkword + ", ");
            writer.newLine();
            writer.write("Chat Messages:");
            writer.newLine();

            for (Comment comment : this.commentsList) {
                writer.write("============================================\t\t" + comment.time);
                writer.newLine();
                for (String line : comment.textList) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCommentsinHtml(BufferedWriter writer) throws IOException {
        writer.write("<ul class=\"list-group\">");
        for(Comment comment: this.commentsList) {
            writer.write("<li class=\"list-group-item list-group-item-light d-flex justify-content-between align-items-center\">");
            for(String line: comment.textList) {
                writer.write(line);
                writer.write("</br>");
            }
            writer.write(String.format("<span class=\"badge badge-light badge-pill\">%s</span>", comment.time));
            writer.write("</li>");
        }
        writer.write("</ul>");
    }
}
