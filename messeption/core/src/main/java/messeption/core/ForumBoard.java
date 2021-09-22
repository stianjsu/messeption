package messeption.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ForumBoard {
    private List<ForumPost> posts = new ArrayList<>();

    public ForumBoard(){}

    public List<ForumPost> getPosts() {return new ArrayList<>(posts);}
    public ForumPost getPost(int i) {return posts.get(i);}

    public void newPost(String title, String text){
        posts.add(new ForumPost(title, text));
    }    

    public void loadPost(File file) throws JsonSyntaxException, JsonIOException, IOException{
        posts.add(new ForumPost(file));
    }

    public void deletePost(ForumPost post){
        posts.remove(post);
    }

}
