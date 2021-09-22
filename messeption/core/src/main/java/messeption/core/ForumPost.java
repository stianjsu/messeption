package messeption.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ForumPost {
    private String title;
    private String text;
    private String timeStamp;

    public ForumPost(String title, String text) {
        this.title = title;
        this.text = text;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String toString() {
        return "Title: " + title + "\tText: " + text + "\n TimeStamp: " + timeStamp;
    }
}
