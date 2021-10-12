package messeption.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.JSONReadWrite;

public class FrontPageController {

    private static final int OFFSET = 230;
    private static final int POSITION = 15;

    @FXML
    AnchorPane postsContainer;
    @FXML
    Button createPostButton;

    private ForumBoard forumBoard;

    public void initialize() throws IOException {

        drawPosts();

    }

    public ForumBoard getBoard() {
        return forumBoard;
    }

    public void setBoard(ForumBoard board) {
        this.forumBoard = board;
        writeBoard();
    }

    public void writeBoard() {
        try {
            JSONReadWrite.fileWrite(this.forumBoard);
        } catch (IOException e) {
            Alert alert = exceptionAlert(e);
            alert.show();
        }
    }

    public void drawPosts() throws IOException {

        forumBoard = JSONReadWrite.fileRead();
        List<ForumPost> posts = forumBoard.getPosts();

        postsContainer.getChildren().clear();

        int indexId = 0;
        for (ForumPost post : posts) {
            String title = post.getTitle();
            String text = post.getText();
            int likes = post.getLikes();
            int dislikes = post.getDislikes();

            Pane pane = generatePostPane(title, text, likes, dislikes, indexId);
            pane.setLayoutY(POSITION + indexId * OFFSET);
            pane.setLayoutX(POSITION);
            postsContainer.getChildren().add(pane);

            indexId++;
        }
        postsContainer.setPrefHeight((2 * POSITION + OFFSET) * indexId);
    }

    private Pane generatePostPane(String title, String text, int likes,
                                     int dislikes, int indexId) throws IOException {

        Pane toReturn = FXMLLoader.load(getClass().getResource("PaneTemplate.fxml"));
        List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
        toReturn.getChildren().clear();

        Label titleLabel = (Label) getNodeFromId(tempChildren, "titleLabel");
        TextArea postTextArea = (TextArea) getNodeFromId(tempChildren, "postTextArea");
        Line titleLine = (Line) getNodeFromId(tempChildren, "titleLine");

        Label likeLabel = (Label) getNodeFromId(tempChildren, "likeLabel");
        Label dislikeLabel = (Label) getNodeFromId(tempChildren, "dislikeLabel");
        Label replyLabel = (Label) getNodeFromId(tempChildren, "replyLabel");

        Button likeButton = (Button) getNodeFromId(tempChildren, "likeButton");
        Button dislikeButton = (Button) getNodeFromId(tempChildren, "dislikeButton");
        Button threadButton = (Button) getNodeFromId(tempChildren, "threadButton");

        if (titleLabel != null) {
            titleLabel.setText(title);
        }
        if (postTextArea != null) {

            postTextArea.setText(text);
            postTextArea.setDisable(true);
            postTextArea.setStyle("-fx-opacity: 1;");
        }
        if (likeButton != null) {
            likeButton.setOnAction(e -> {
                ForumPost postToUpdate = forumBoard.getPost(indexId);
                int prevLikes = postToUpdate.getLikes();

                try {
                    postToUpdate.incrementLikes();
                    JSONReadWrite.fileWrite(forumBoard);
                } catch (IOException error) {
                    postToUpdate.setLikes(prevLikes);
                }

                likeLabel.setText(postToUpdate.getLikes() + " likes");
            });
        }
        if (dislikeButton != null) {

            dislikeButton.setOnAction(e -> {
                ForumPost postToUpdate = forumBoard.getPost(indexId);
                int prevDislikes = postToUpdate.getDislikes();

                try {
                    postToUpdate.incrementDislikes();
                    JSONReadWrite.fileWrite(forumBoard);
                } catch (IOException error) {
                    postToUpdate.setDislikes(prevDislikes);
                }

                dislikeLabel.setText(postToUpdate.getDislikes() + " dislikes");
            });
        }
        if (likeLabel != null) {
            likeLabel.setText(likes + " likes");
        }
        if (dislikeLabel != null) {
            dislikeLabel.setText(dislikes + " dislikes");
        }

        toReturn.getChildren().addAll(new ArrayList<Node>(Arrays.asList(titleLabel, titleLine, 
                                        postTextArea, likeLabel, dislikeLabel, replyLabel, 
                                        likeButton, dislikeButton, threadButton)));
        return toReturn;
    }

    public Node getNodeFromId(List<Node> children, String id) {

        for (Node child : children) {
            if (child.getId() != null && child.getId().equals(id)) {
                return child;
            }   
        }
        return null;
    }

    public Alert exceptionAlert(Exception e) {

        Alert toReturn = new Alert(AlertType.ERROR);
        toReturn.setContentText(e.toString() + "\n" + e.getCause());
        System.err.println(e.toString() + "\n" + e.getCause());
        toReturn.setTitle("Error");
        return toReturn;
    }

}
