package messeption.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ForumPost {
    private String title;
    private String text;
    private int likes;
    private int dislikes;
    private String timeStamp;

    public ForumPost(String title, String text) {
        this.title = title;
        this.text = text;
        this.likes = 0;
        this.dislikes = 0;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public int getLikes(){
        return this.likes;
    }

    public int getDislikes(){
        return this.dislikes;
    }

    public void incrementLikes(){
        this.likes++;
    }

    public void incrementDislikes(){
        this.dislikes++;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public String toString() {
        return "Title: " + this.title + "\tText: " + this.text + "\n TimeStamp: " + this.timeStamp;
    }
}
