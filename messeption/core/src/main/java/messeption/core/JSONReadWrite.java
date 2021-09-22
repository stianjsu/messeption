package messeption.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class JSONReadWrite {
    public static final String path = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/messeption/";
    public static final File DEFAULT_BOARD_FILE = new File("Board.JSON");


    public static ForumBoard fileRead(File file) throws JsonSyntaxException, JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader reader = new FileReader(path + file);
        ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
        reader.close();
        return toReturn;
    }

    public static void fileWrite(File file, ForumBoard board) throws JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (file.equals(DEFAULT_BOARD_FILE)){
            throw new IOException("Cannot save to default board file");
        }
        FileWriter writer = new FileWriter(path + file);
        gson.toJson(board, writer);
        writer.close();
    }

    
    public static ForumBoard fileRead() throws JsonSyntaxException, JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader reader = new FileReader(path + DEFAULT_BOARD_FILE);
        ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
        reader.close();
        return toReturn;
    }

    public static void fileWrite(ForumBoard board) throws JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(path + DEFAULT_BOARD_FILE);
        gson.toJson(board, writer);
        writer.close();
    }
}