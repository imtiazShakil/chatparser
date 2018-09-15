package org.aai.text.statistics;

import java.io.*;
import java.util.ArrayList;

public class HtmlGenerator {

    private ArrayList<Comment> commentList;

    private HtmlGenerator() {
    }

    HtmlGenerator(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public void writeHtml(File htmlFile) {
        ChatStats chatStats = new ChatStats();
        ArrayList<User> userList = chatStats.generateUserStatistics(commentList, null);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(htmlFile), "utf-8"))) {

            writer.write("<!doctype html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <!-- Required meta tags -->\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                    "\n" +
                    "    <!-- Bootstrap CSS -->\n" +
                    "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\"\n" +
                    "          crossorigin=\"anonymous\">");

            // writing the title of html
            String title = "<title>%s</title>";
            writer.write(String.format(title, htmlFile.getName()));

            // ending head
            writer.write("</head>");

            // body begins
            writer.write("<body>\n" +
                    "<div class=\"container-fluid\">");

            // main content starts
            writer.write("<div class=\"row\">\n" +
                    "<div class=\"card w-100\">");

            // card header starts
            writer.write("<div class=\"card-header\">\n" +
                    "<nav>\n" +
                    "<div class=\"nav nav-tabs\" id=\"nav-tab\" role=\"tablist\">");

            // write class nav header
            writer.write("<a class=\"nav-item nav-link active\" id=\"nav-class-tab\" data-toggle=\"tab\" href=\"#nav-class\" role=\"tab\" aria-controls=\"nav-class\" aria-selected=\"true\">Class Chats</a>\n");

            // write nav header for each user
            for (int i = 0; i < userList.size(); i++)
                writeNavItems(userList.get(i), writer);

            // card header ends
            writer.write("</div>\n" +
                    "</nav>\n" +
                    "</div>");

            // card body starts
            writer.write("<div class=\"card-body\">\n" +
                    "\t<div class=\"tab-content\" id=\"nav-tabContent\">");

            // write class logs
            writer.write("<div class=\"tab-pane fade show active\" id=\"nav-class\" role=\"tabpanel\" aria-labelledby=\"nav-class-tab\">");
            chatStats.writeHtmlChatComments(commentList, writer);
            writer.write("</div>");


            // write comments for each user
            for(User user: userList) writeUserChat(user, writer);

            // card body ends
            writer.write("</div></div>");

            //main content ends
            writer.write("</div></div>");


            // html ends
            writer.write("</div>\n" +
                    "<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\"\n" +
                    "        crossorigin=\"anonymous\"></script>\n" +
                    "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\"\n" +
                    "        crossorigin=\"anonymous\"></script>\n" +
                    "\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeNavItems(User user, BufferedWriter writer) throws IOException {
        String anchor = "<a class=\"nav-item nav-link\" id=\"nav-%s-tab\" data-toggle=\"tab\"" +
                " href=\"#nav-%s\" role=\"tab\" aria-controls=\"nav-%s\" aria-selected=\"true\">%s  " +
                "<span class=\"badge badge-primary\">%d</span>" +
                "</a>";

        writer.write(String.format(anchor, user.getName(), user.getName(), user.getName(), user.getName(), user.getCommentsList().size()));
    }

    private void writeUserChat(User user, BufferedWriter writer) throws IOException {
        String div = "<div class=\"tab-pane fade show\" id=\"nav-%s\" role=\"tabpanel\" aria-labelledby=\"nav-%s-tab\">";
        writer.write(String.format(div, user.getName(), user.getName()));
        user.writeCommentsinHtml(writer);
        writer.write("</div>");
    }
}
