package core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class ForumPost {
    private String title;
    private String text;

    
    public ForumPost(String title, String text){
        this.title = title;
        this.text = text;
    }

    public ForumPost(String file) throws JsonSyntaxException, JsonIOException, FileNotFoundException{
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = gson.fromJson(new FileReader("save.JSON"), Map.class);
    }

    public String getTitle(){return title;}
    public String getText(){return text;}

    public void save() throws JsonIOException, IOException{
        Gson gson = new GsonBuilder().create();
        gson.toJson(this, new FileWriter("save.JSON"));
    }
}
