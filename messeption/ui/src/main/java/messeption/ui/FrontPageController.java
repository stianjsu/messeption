package messeption.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.JSONReadWrite;

public class FrontPageController {

    @FXML
    Accordion postsContainer;
    @FXML
    Button createPostButton;

    private ForumBoard forumBoard;

    public void initialize() throws IOException {
        forumBoard = JSONReadWrite.fileRead();

        drawPosts();
    }

    public void drawPosts() {
        postsContainer.getPanes().clear();
        List<ForumPost> posts = forumBoard.getPosts();

        for (ForumPost post : posts) {

            TitledPane postContainer = new TitledPane();
            postContainer.setAnimated(false);
            postContainer.setText(post.getTitle());
            postContainer.setFont(new Font(16));

            AnchorPane postTextContainer = new AnchorPane();
            postTextContainer.setMinHeight(0.0);
            postTextContainer.setMinWidth(0.0);
            postTextContainer.setPrefHeight(180.0);
            postTextContainer.setPrefWidth(200.0);

            TextArea postText = new TextArea();
            postText.setPrefHeight(200.0);
            postText.setPrefWidth(598.0);
            postText.setText(post.getText());
            postText.setDisable(true);
            postText.setStyle("-fx-opacity: 1");

            postTextContainer.getChildren().add(postText);
            postContainer.setContent(postTextContainer);
            postsContainer.getPanes().add(postContainer);
        }
    }

}
