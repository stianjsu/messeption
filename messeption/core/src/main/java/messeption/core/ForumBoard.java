package messeption.core;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ForumBoard {
    private List<ForumPost> posts = new ArrayList<>();

    public ForumBoard(){}

    public List<ForumPost> getPosts() {
        return new ArrayList<>(posts);
    }

    public ForumPost getPost(int i) {
        return posts.get(i);
    }

    public void newPost(String title, String text) {
        posts.add(new ForumPost(title, text));
    }

    public void savePosts(String path, String fileName) throws IOException {
        JSONReadWrite.fileWrite(path, fileName, this);
    }

    public void savePosts() throws IOException {
        JSONReadWrite.fileWrite(this);
    }

    public void loadPosts(String path, String fileName) throws JsonSyntaxException, JsonIOException, IOException {
        posts = JSONReadWrite.fileRead(path, fileName).getPosts();
    }

    public void loadPosts() throws JsonSyntaxException, JsonIOException, IOException {
        posts = JSONReadWrite.fileRead().getPosts();
    }


    public void deletePost(ForumPost post) {
        posts.remove(post);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ForumPost post : posts) {
            s.append(post);
        }
        return s.toString();
    }

}
