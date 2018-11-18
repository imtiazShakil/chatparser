package org.aai.text.statistics;

import org.aai.analytics.ai.keywordExtraction.KeywordExtraction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatStats {
    private HashMap<String, User> userMap;

    public ArrayList<User> generateUserStatistics(ArrayList<Comment> commentList) {
        userMap = new HashMap<>();
        for (Comment comment : commentList) {
            if (userMap.containsKey(comment.name) == false)
                userMap.put(comment.name, new User(comment.name));

            User user = userMap.get(comment.name);
            user.addComment(comment);
        }

        ArrayList<User> userList = new ArrayList<>();
        userMap.forEach((name, user) -> {
            System.out.println(user.getName() + "--" + user.getCommentsList().size());
            user.generateStatistics();
            userList.add(user);
//            user.dumpStatistics(directory);
        });
        return userList;
    }
    public List<String> getKeywords(ArrayList<Comment> commentList) {
        KeywordExtraction keywordExtraction = KeywordExtraction.getInstance();
        StringBuilder sb = new StringBuilder();
        commentList.forEach(comment -> comment.textList.forEach(sentence -> sb.append(sentence).append(" . ")));
        return keywordExtraction.process(sb.toString());
    }

    public void writeChatLog(ArrayList<Comment> commentList, File textFile) {
        List<String> kkwords = getKeywords(commentList);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(textFile), "utf-8"))) {

            writer.write("============ Top Keywords ============");
            ((BufferedWriter) writer).newLine();
            for (String kword : kkwords) {
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

    public void writeHtmlChatComments(ArrayList<Comment> commentList, BufferedWriter writer) throws IOException {

        boolean color = false;
        writer.write("<div class=\"list-group\">");
        for (Comment comment : commentList) {
            color = !color;
            String groupItem = "<div class=\"list-group-item list-group-item-action list-group-item-%s flex-column align-items-start\">";
            writer.write(String.format(groupItem, (color) ? "info":"dark" ));

            writer.write("<div class=\"d-flex w-100 justify-content-between\">");

            // writing the name and timestamp
            writer.write("<h5 class=\"mb-1\">");
            writer.write(comment.name);
            writer.write("</h5>");

            writer.write("<small>");
            writer.write(comment.time);
            writer.write("</small>");

            writer.write("</div>");
            writer.newLine();

            writer.write("<p class=\"mb-1\">");
            for (String text : comment.textList) {
                writer.write(text);
                writer.write("</br>");
            }
            writer.write("</p></div>");
            writer.newLine();
        }
        writer.write("</div>");

    }
}
