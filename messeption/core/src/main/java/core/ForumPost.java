package core;

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
}
