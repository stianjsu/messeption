package messeption.core;

import java.util.ArrayList;
import java.util.List;

/**
 * ForumBoard has a collection of posts.
 * 
 */
public class ForumBoard {

  private List<ForumPost> posts;

  public ForumBoard() {
    this.posts = new ArrayList<>();
  }

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
   * Getter for specified post with the indentifier id.

   * @param id unique id
   * @return ForumPost with the correct id
   */
  public ForumPost getPost(String id) {
    for (int index = 0; index < posts.size(); index++) {
      if (posts.get(index).getId().equals(id)) {
        return posts.get(index);
      }
    }
    return null;
  } 

  /**
   * Creates a new post and adds it to the list posts.

   * @param title title of post
   * @param text  text in post
   * @param author author of post
   * @param postedAnonymously If post should be displayed as anonymous post
   */
  public void newPost(String title, String text, User author, Boolean postedAnonymously) {
    posts.add(new ForumPost(title, text, author, postedAnonymously));
  }

  /**
   * Adds an already existing post to the posts list.

   * @param post the post to be added
   */
  public void newPost(ForumPost post) {
    posts.add(post);
  }

  
  /**
   * Removes a post object from list of posts.

   * @param post ForumPost
   * 
   * @throws IllegalArgumentException Post not in list of posts
   */
  public void deletePost(ForumPost post) throws IllegalArgumentException {
    if (!this.posts.contains(post)) {
      throw new IllegalArgumentException("Post is not in list of posts");
    }
    this.posts.remove(post);
  }

  /**
   * Removes a post object from list of posts.

   * @param id PostId
   * @throws IllegalArgumentException Post not in list of posts
   */
  public void deletePost(String id) throws IllegalArgumentException {
    this.deletePost(this.getPost(id));
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o instanceof ForumBoard) {
      ForumBoard other = (ForumBoard) o;

      return this.getPosts().equals(other.getPosts()) && other.hashCode() == this.hashCode();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.posts.stream().mapToInt(e -> e.hashCode()).sum();
  }
}
