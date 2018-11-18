package org.aai.text.statistics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatReader {

    private ChatFilter chatFilter = ChatFilter.getInstance();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<Path> explorePath(String directory) {
        ArrayList<Path> filePathList = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        if (path.getFileName().toString().endsWith("txt") == false) return;
                        filePathList.add(path);
                        System.out.println(path);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePathList;
    }


    public ArrayList<Comment> readFile(Path filePath) {
        ArrayList<Comment> commentList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            Comment comment = null;

            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");

                if (validCommentStarts(words)) {
                    comment = new Comment(words[2], words[1]);
                    String commentText = mergeWords(words, 3);
                    if (chatFilter.isUnnecessary(commentText) == false)
                        comment.textList.add(commentText);
                    commentList.add(comment);
                    continue;
                } else if (comment != null) {
                    if (chatFilter.isUnnecessary(line) == false)
                        comment.textList.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        commentList = (ArrayList<Comment>) commentList.stream().filter(comment -> comment.textList.size() > 0).collect(Collectors.toList());
        return commentList;
    }

    public boolean isTime(String time) {
        try {
            timeFormat.parse(time);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isDate(String dateString) {
        try {
            dateFormat.parse(dateString);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean validCommentStarts(String[] words) {
        if (words == null || words.length < 4) return false;
        return (isDate(words[0]) && isTime(words[1]));
    }

    private String mergeWords(String[] words, int indx) {
        if (words.length <= indx) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = indx; i < words.length; i++)
            sb.append(words[i]).append(" ");
        return sb.toString();
    }
}
