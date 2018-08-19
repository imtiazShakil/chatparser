package org.aai.text.statistics;

import org.aai.analytics.ai.keywordExtraction.KeywordExtraction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Commenter {
    public String name;
    public ArrayList<Comment> commentsList;

    int totalMessages = 0, totalWords = 0;
    List<String> keywords = new ArrayList<>();

    public Commenter(String name) {
        this.name = name;
        this.commentsList = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        this.commentsList.add(comment);
    }

    public void generateStatistics() {
        KeywordExtraction keyExtraction = KeywordExtraction.getInstance();
        StringBuilder sb = new StringBuilder();
        for(Comment comment: commentsList) {
            if(comment.name.equals(this.name) == false) continue;
            for(String text: comment.textList) {
                sb.append(text).append(". ");
                String[] wordList = text.split(" ");
                totalWords += wordList.length;
            }
            totalMessages++;
        }
        keywords = keyExtraction.process(sb.toString());
    }

    public void dumpStatistics(String dir) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(dir + "/" + this.name + ".txt" ), "utf-8"))) {
            writer.write("Attendee: "+ this.name);
            ((BufferedWriter) writer).newLine();
            writer.write("Message Count: "+ this.totalMessages);
            ((BufferedWriter) writer).newLine();
            writer.write("Word Count: "+ this.totalWords);
            ((BufferedWriter) writer).newLine();
            writer.write("Top Keywords: ");
            for(String kkword: keywords) writer.write(kkword + ", ");
            ((BufferedWriter) writer).newLine();
            writer.write("Chat Messages:");
            ((BufferedWriter) writer).newLine();

            for(Comment comment: this.commentsList) {
                writer.write("============================================\t\t" + comment.time);
                ((BufferedWriter) writer).newLine();
                for(String line: comment.textList) {
                    writer.write(line);
                    ((BufferedWriter) writer).newLine();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
