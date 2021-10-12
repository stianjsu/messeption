package messeption.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class UserTextSubmission {
    
    protected String text;
    protected int likes;
    protected int dislikes;
    protected String timeStamp;
    

    public UserTextSubmission(String text) {
        this.text = text;
        this.likes = 0;
        this.dislikes = 0;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
    }

    public String getText() {
        return this.text;
    }

    public int getLikes() {
        return this.likes;
    }

    public int getDislikes() {
        return this.dislikes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void incrementDislikes() {
        this.dislikes++;
    }

    public void setLikes(int likes) {
        if (likes < 0) {
            throw new IllegalArgumentException("Can't set negative likes");
        }
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        if (dislikes < 0) {
            throw new IllegalArgumentException("Can't set negative dislikes");
        }
        this.dislikes = dislikes;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public abstract String toString();
        
}
