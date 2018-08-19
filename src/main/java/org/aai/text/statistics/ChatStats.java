package org.aai.text.statistics;

import org.aai.analytics.ai.keywordExtraction.KeywordExtraction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatStats {
    private HashMap<String, Commenter> commenterMap;

    public void generateUserStatistics(ArrayList<Comment> commentList, String directory) {
        commenterMap = new HashMap<>();
        for (Comment comment : commentList) {
            if (commenterMap.containsKey(comment.name) == false)
                commenterMap.put(comment.name, new Commenter(comment.name));

            Commenter commenter = commenterMap.get(comment.name);
            commenter.addComment(comment);
        }
        commenterMap.forEach((name, commenter) -> {
            System.out.println(commenter.name + "--" + commenter.commentsList.size());
            commenter.generateStatistics();
            commenter.dumpStatistics(directory);
        });
    }

    public void writeChatLog(ArrayList<Comment> commentList, File textFile) {
        KeywordExtraction keywordExtraction = KeywordExtraction.getInstance();
        StringBuilder sb = new StringBuilder();
        commentList.forEach(comment -> comment.textList.forEach(sentence -> sb.append(sentence).append(" . ")));
        List<String> kkwords = keywordExtraction.process(sb.toString());

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(textFile), "utf-8"))) {

            writer.write("============ Top Keywords ============");
            ((BufferedWriter) writer).newLine();
            for(String kword: kkwords) {
                writer.write(kword);
                ((BufferedWriter) writer).newLine();
            }
            writer.write("======================================= ");
            ((BufferedWriter) writer).newLine();

            for (Comment comment : commentList) {
                writer.write(comment.name + "\t\t\t\t\t\t\t\t\t\t" + comment.time);
                ((BufferedWriter) writer).newLine();
                for (String text : comment.textList) {
                    writer.write(text);
                    ((BufferedWriter) writer).newLine();
                }
                writer.write("==========================================================");
                ((BufferedWriter) writer).newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
