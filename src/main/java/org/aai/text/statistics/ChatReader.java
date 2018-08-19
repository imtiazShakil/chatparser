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
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

    public List<Path> explorePath(String directory) {
        ArrayList<Path> filePathList = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        if(path.getParent().toString().endsWith("Statistics")) return;
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
            boolean timeFound = false;
            String time = null;
            Comment comment = null;

            while ((line = br.readLine()) != null) {
                if (chatFilter.isUnnecessary(line)) continue;
                if (isTime(line)) {
                    timeFound = true;
                    time = line;
                    continue;
                }
                if (timeFound) {
                    timeFound = false;
                    if(line.endsWith("joined the channel")) continue;
                    if(line.endsWith("left the channel (quit)")) continue;
                    if(line.endsWith("left the channel (timeout)")) continue;
                    if(line.length()>15) continue;

                    comment = new Comment(line, time);
                    commentList.add(comment);

                    continue;
                }
                if (comment != null) {
                    comment.textList.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        commentList = (ArrayList<Comment>) commentList.stream().filter(comment -> comment.textList.size()>0).collect(Collectors.toList());
        return commentList;
    }

    public boolean isTime(String time) {
        try {
            timeFormat.parse(time);
            return true;
        } catch (Exception ex) {
            return  false;
        }
    }
}
