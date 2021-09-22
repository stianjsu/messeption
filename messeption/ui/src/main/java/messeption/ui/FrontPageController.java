package messeption.ui;


import java.io.IOException;
import java.util.List;


import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.JSONReadWrite;

public class FrontPageController {


    @FXML
    Accordion postsContainer;
    Button createPostButton;

    //private ForumBoard forumBoard;

    public void initialize() throws IOException {
        //ForumBoard test = JSONReadWrite.fileRead();
        System.out.println("forumBoard adawdaw fgqawrfawd\n\n\n\n\n HEIEIEIEIE");
        //forumBoard = JSONReadWrite.fileRead();
        
        //drawPosts();
    }


/*
    public void drawPosts() {
        postsContainer.getPanes().clear();
        List<ForumPost> posts = forumBoard.getPosts();

        for (ForumPost post : posts) {

            TitledPane postContainer = new TitledPane();
            postContainer.setAnimated(false);
            postContainer.setText(post.getTitle());

            AnchorPane postTextContainer = new AnchorPane();
            postContainer.setMinHeight(0.0);
            postContainer.setMinWidth(0.0);
            postContainer.setPrefHeight(180.0);
            postContainer.setPrefWidth(200.0);

            Label postText = new Label();
            postText.setPrefHeight(200.0);
            postText.setPrefWidth(600.0);
            postText.setText(post.getText());

            postTextContainer.getChildren().add(postText);
            postContainer.setContent(postTextContainer);
            postsContainer.getPanes().add(postContainer);
        }
    }
    */
}
