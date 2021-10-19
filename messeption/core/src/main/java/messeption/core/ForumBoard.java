package messeption.core;

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

   * @param i index
   * @return ForumPost at index i
   */
  public ForumPost getPost(int i) {
    return posts.get(i);
  }

  /**
   * Creates a new post and adds it to the list posts.

   * @param title title of post
   * @param text  text in post
   */
  public void newPost(String title, String text) {
    posts.add(new ForumPost(title, text));
  }


  /**
   * Removed the given post from posts list.

   * @param post ForumPost
   * 
   */
  public void deletePost(ForumPost post) {
    posts.remove(post);
  }

  /**
   * Uses ForumPosts toString method to create a string that consists of all the
   * posts in posts-list.

   * @return String
   */
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (ForumPost post : posts) {
      s.append(post);
    }
    return s.toString();
  }

  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ForumBoard) {
      ForumBoard o = (ForumBoard) obj;

      return this.getPosts().equals(o.getPosts()) && o.hashCode() == this.hashCode();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.posts.size();
  }
}
