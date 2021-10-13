package messeption.core;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ForumBoard has a collection of posts.
 * 
 */
public class ForumBoard {

  private List<ForumPost> posts = new ArrayList<>();

  /**
   * Getter for posts list.
   * 
   * @return List of ForumPost
   */
  public List<ForumPost> getPosts() {
    if (posts == null || this.posts.size() < 1) {
      return new ArrayList<>();
    }
    return new ArrayList<>(posts);
  }

  /**
   * Getter for specified post at index i.
   * 
   * @param i index
   * @return ForumPost at index i
   */
  public ForumPost getPost(int i) {
    return posts.get(i);
  }

  /**
   * Creates a new post and adds it to the list posts.
   * 
   * @param title title of post
   * @param text  text in post
   */
  public void newPost(String title, String text) {
    posts.add(new ForumPost(title, text));
  }

  /**
   * Saves all the information in ForumBoard on a specified file at a specified
   * path. Forumboard has a collection with the ForumPosts, hence the posts are
   * saved.
   * 
   * @param path     filepath
   * @param fileName filename
   * @throws IOException fileError
   */
  public void savePosts(String path, String fileName) throws IOException {
    JsonReadWrite.fileWrite(path, fileName, this);
  }

  /**
   * Saves all the information in ForumBoard on a default location. Forumboard has
   * a collection with the ForumPosts, hence the posts are saved.
   * 
   * @throws IOException fileError
   */
  public void savePosts() throws IOException {
    JsonReadWrite.fileWrite(this);
  }

  /**
   * Loads the post from a file into the posts list from specified location.
   * 
   * @param path     filePath
   * @param fileName fileName
   * @throws JsonSyntaxException fileError
   * @throws JsonIOException     fileError
   * @throws IOException         fileError
   */
  public void loadPosts(String path, String fileName) throws JsonSyntaxException, JsonIOException, IOException {
    posts = JsonReadWrite.fileRead(path, fileName).getPosts();
  }

  /**
   * Loads the post from a file into the posts list from default location.
   * 
   * @throws JsonSyntaxException fileError
   * @throws JsonIOException     fileError
   * @throws IOException         fileError
   */
  public void loadPosts() throws JsonSyntaxException, JsonIOException, IOException {
    posts = JsonReadWrite.fileRead().getPosts();
  }

  /**
   * Removed the given post from posts list.
   * 
   * @param post ForumPost
   * 
   */
  public void deletePost(ForumPost post) {
    posts.remove(post);
  }

  /**
   * Uses ForumPosts toString method to create a string that consists of all the
   * posts in posts-list.
   * 
   * @return String
   */
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (ForumPost post : posts) {
      s.append(post);
    }
    return s.toString();
  }
}
