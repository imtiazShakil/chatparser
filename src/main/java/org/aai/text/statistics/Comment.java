package org.aai.text.statistics;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Comment implements Cloneable {
    public String name;
    public String time;
    public ArrayList<String> textList;

    public Comment(String name, String time) {
        this.name = name;
        this.time = time;
        this.textList = new ArrayList<>();
    }

    /**
     * Merges consecutive comments coming from same user
     *
     * @param commentList
     * @return
     */
    public static ArrayList<Comment> groupCommentsByUser(ArrayList<Comment> commentList) {
        if (commentList == null || commentList.isEmpty()) return commentList;

        ArrayList<Comment> groupCommentList = new ArrayList<>();
        Comment lastComment = null;
        for (Comment comment : commentList) {
            if (lastComment == null) {
                lastComment = comment.clone();
                groupCommentList.add(lastComment);
                continue;
            }

            if (lastComment.name.equals(comment.name)) {
                lastComment.textList.addAll(comment.textList.stream()
                        .map(text -> new String(text)).collect(Collectors.toList())
                );
            } else {
                lastComment = comment.clone();
                groupCommentList.add(lastComment);
            }
        }
        return groupCommentList;
    }

    @Override
    public Comment clone() {
        Comment cloneComment = new Comment(this.name, this.time);
        cloneComment.textList.addAll(this.textList.stream().map(text -> new String(text)).collect(Collectors.toList()));

        return cloneComment;
    }
}
