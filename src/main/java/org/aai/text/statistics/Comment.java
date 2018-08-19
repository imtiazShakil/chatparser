package org.aai.text.statistics;

import java.util.ArrayList;

public class Comment {
    public String name;
    public String time;
    public ArrayList<String> textList;

    public Comment(String name, String time) {
        this.name = name;
        this.time = time;
        this.textList = new ArrayList<>();
    }
}
