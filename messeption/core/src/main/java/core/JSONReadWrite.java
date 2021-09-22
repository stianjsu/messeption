package core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class JSONReadWrite implements ForumPostReadWrite {
    public final String path = Paths.get("").toAbsolutePath().toString() + "/src/main/java/resources/";
    
    
    public JSONReadWrite(){}

    public Map<String, String> fileRead(File file) throws JsonSyntaxException, JsonIOException, IOException{
        Gson gson = new GsonBuilder().create();
        FileReader reader = new FileReader(path + file);
        Map<String, String> map = gson.fromJson(reader, Map.class);
        reader.close();
        return map;
    }

    public void fileWrite(File file, ForumPost post) throws JsonIOException, IOException{
        Gson gson = new GsonBuilder().create();
        FileWriter writer = new FileWriter(path + file);
        gson.toJson(post, writer);
        writer.close();
    }
}