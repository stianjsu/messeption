package core;

import java.util.ArrayList;
import java.util.List;

public class ForumBoard {
    private List<ForumPost> posts = new ArrayList<>();

    public ForumBoard(){}

    public List<ForumPost> getPosts() {return new ArrayList<>(posts);}
    public ForumPost getPost(int i) {return posts.get(i);}

    public void newPost(String title, String text){
        posts.add(new ForumPost(title, text));
    }    

    public void deletePost(ForumPost post){
        posts.remove(post);
    }

}
