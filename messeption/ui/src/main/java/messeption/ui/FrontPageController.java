package messeption.ui;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;



import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.JSONReadWrite;

public class FrontPageController {

    private static final int OFFSET = 230;
    private final int POSITION = 15;

    
    @FXML
    AnchorPane postsContainer;
    @FXML
    Button createPostButton;
    @FXML
    Pane postPaneTemplateFXML;

    Pane postPaneTemplate;
    
    private ForumBoard forumBoard;

    public void initialize(){

        List<Node> postPaneTemplateChildren = postPaneTemplateFXML.getChildren().stream()
                                                .map(e -> {
                                                    if(e instanceof Label)
                                                        return new Label(e);
                                                    if(e instanceof Line)
                                                        return new Line(e);
                                                    if(e instanceof TextArea)
                                                        return new TextArea(e);
                                                    if(e instanceof Button)
                                                        return new Button(e);
                                                    return new Node(e);
                                                }).collect(Collectors.toList());
                                                

        postPane = new Pane(postPaneTemplate.getChildren());
        drawPosts();
    }

    public ForumBoard getBoard(){
        return forumBoard;
    }

    public void drawPosts() {
        try{
            forumBoard = JSONReadWrite.fileRead();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        List<ForumPost> posts = forumBoard.getPosts();

        int i = 0;
        for(ForumPost post : posts){
            String title = post.getTitle();
            String text = post.getText();
            
            Pane pane = generatePostPane(title, text);
            pane.setLayoutY(POSITION+i*OFFSET);
            pane.setLayoutX(POSITION);

            postsContainer.getChildren().add(pane);
            i++;
        }
        postsContainer.setPrefHeight((2 * POSITION + OFFSET) * i);
    }

    private Pane generatePostPane(String title, String text){
        /* Pane pane = new Pane(postPane);
        Label titleLabel = new Label();
        Label likeLabel = new Label();
        Label dislikeLabel = new Label();
        Button like = new Button();
        Button dislike = new Button();
        Button goToThread = new Button();
        TextArea textArea = new TextArea();
        Line line = new Line();
        */

        return pane;
    }

}
