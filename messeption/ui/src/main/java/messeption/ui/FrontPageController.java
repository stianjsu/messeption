package messeption.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
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

    public void initialize() throws IOException {

        drawPosts();

    }

    public ForumBoard getBoard() {
        return forumBoard;
    }

    public void drawPosts() throws IOException {

        forumBoard = JSONReadWrite.fileRead();
        List<ForumPost> posts = forumBoard.getPosts();

        postsContainer.getChildren().clear();

        int i = 0;
        for (ForumPost post : posts) {
            String title = post.getTitle();
            String text = post.getText();

            Pane pane = generatePostPane(title, text);
            pane.setLayoutY(POSITION + i * OFFSET);
            pane.setLayoutX(POSITION);

            postsContainer.getChildren().add(pane);
            i++;
        }
        postsContainer.setPrefHeight((2 * POSITION + OFFSET) * i);
    }

    private Pane generatePostPane(String title, String text) throws IOException {

        Pane toReturn = FXMLLoader.load(getClass().getResource("PaneTemplate.fxml"));
        List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
        toReturn.getChildren().clear();

        tempChildren.forEach(e -> {
            String id = e.getId();
            if (id != null && id.equals("postTitle")) {
                Label titleLable = (Label) e;
                titleLable.setText(title);
                toReturn.getChildren().add(titleLable);
            } else if (id != null && id.equals("postText")) {
                TextArea postTextArea = (TextArea) e;
                postTextArea.setText(text);
                postTextArea.setDisable(true);
                postTextArea.setStyle("-fx-opacity: 1;");
                toReturn.getChildren().add(postTextArea);
            } else {
                toReturn.getChildren().add(e);
            }

        });
        return toReturn;
    }

    public Alert exceptionAlert(Exception e) {
        Alert toReturn = new Alert(AlertType.ERROR);
        toReturn.setContentText(e.toString() + "\n" + e.getCause());
        System.err.println(e.toString() + "\n" + e.getCause());
        toReturn.setTitle("Error");
        return toReturn;
    }

}
