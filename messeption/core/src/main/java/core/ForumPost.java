package core;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;


public class ForumPost {
    private String title;
    private String text;

    
    public ForumPost(String title, String text){
        this.title = title;
        this.text = text;
    }

    public ForumPost(String path){
        //TODO implement construct from path
    }

    public String getTitle(){return title;}
    public String getText(){return text;}

    public void save(){
        String path = System.getProperty("user.dir") + "\\resources\\JSON.JSON";
        String p = Paths.get("").toAbsolutePath().toString() + "\\resources\\JSON.JSON";
        Gson.toJson(this, new FileWriter(path));
    }
}
