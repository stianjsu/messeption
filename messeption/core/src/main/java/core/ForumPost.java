package core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class ForumPost {
    private String title;
    private String text;
    private ForumPostReadWrite readWrite = new JSONReadWrite();

    
    public ForumPost(String title, String text){
        this.title = title;
        this.text = text;
    }

    public ForumPost(File file) throws JsonSyntaxException, JsonIOException, IOException{
        Map<String, String> map = readWrite.fileRead(file);
        this.title = map.get("title");
        this.text = map.get("text");
    }

    public String getTitle(){return title;}
    public String getText(){return text;}

    public void save(File file) throws JsonIOException, IOException{
        readWrite.fileWrite(file, this);
    }
}
