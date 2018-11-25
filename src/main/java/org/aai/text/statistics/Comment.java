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

    /**
     * Merges consecutive comments coming from same user
     * @param commentList
     * @return
     */
    public static ArrayList<Comment> groupCommentsByUser(ArrayList<Comment> commentList) {
        if (commentList == null || commentList.isEmpty()) return commentList;

        ArrayList<Comment> groupCommentList = new ArrayList<>();
        Comment lastComment = null;
        for(Comment comment: commentList) {
            if(lastComment == null) {
                lastComment = comment;
                groupCommentList.add(lastComment);
                continue;
            }

            if(lastComment.name.equals(comment.name)) {
                lastComment.textList.addAll(comment.textList);
            } else {
                lastComment = comment;
                groupCommentList.add(lastComment);
            }
        }
        return groupCommentList;
    }
}
