package messeption.ui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;


/**
 * TestFX App test
 */
public class MesseptionAppTest extends ApplicationTest {

    public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
    public static final String CREATE_POST_PAGE_PATH = "CreatePost.fxml";

    private Scene frontPageScene;
    private Scene createPostScene;

    private FrontPageController frontPageController;
    private CreatePostController createPostController;

    private ForumBoard board;
    private ForumBoard boardBackup;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
        FXMLLoader createPostLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));

        frontPageScene = new Scene(frontPageLoader.load());
        createPostScene = new Scene(createPostLoader.load());

        frontPageController = frontPageLoader.getController();
        createPostController = createPostLoader.getController();

        primaryStage.setScene(frontPageScene);
        primaryStage.setTitle("Messeption");
        primaryStage.show();

        boardBackup = frontPageController.getBoard();

        frontPageController.createPostButton.setOnAction(event -> {
            primaryStage.setScene(createPostScene);
            createPostController.setBoard(frontPageController.getBoard());
        });

        createPostController.cancelButton.setOnAction(event -> {
            createPostController.reloadPage();
            primaryStage.setScene(frontPageScene);
            try { frontPageController.drawPosts(); }
            catch (IOException e) { frontPageController.exceptionAlert(e).show();}
            
        });

    }

    private void click(String... labels) {
        for (var label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }
 
    public void checkNewPost(String title, String text){
        board = frontPageController.getBoard();
        List<ForumPost> posts = board.getPosts();
        ForumPost post = posts.get(posts.size() - 1);
        Assertions.assertEquals(post.getTitle(), title);
        Assertions.assertEquals(post.getText(), text);
    }

    @ParameterizedTest
    @MethodSource
    public void testCreatePostValid(String title, String text){
        click("Create Post");
        clickOn("Title of post").write(title);
        clickOn("content of post").write(text);
        clickOn("Publish");
        clickOn("Quit To Front Page");
        checkNewPost(title, text);
    }
        private static Stream<Arguments> testCreatePostValid() {
        return Stream.of(
            Arguments.of("title", "text"),
            Arguments.of("hello there", "general kenobi"),
            Arguments.of("heiheitittel", "heiheitext")
        );
    }



    public void checkNewPostFail(String title, String text){
        board = frontPageController.getBoard();
        List<ForumPost> posts = board.getPosts();
        ForumPost post = posts.get(posts.size() - 1);
        Assertions.assertFalse(post.getTitle().equals(title) && post.getText().equals(text));
    }

    @ParameterizedTest
    @MethodSource
    public void testCreatePostInvalid(String title, String text){
        click("Create Post");
        clickOn("Title of post").write(title);
        clickOn("content of post").write(text);
        clickOn("Publish");
        clickOn("Cancel");
        checkNewPostFail(title, text);
    }
        private static Stream<Arguments> testCreatePostInvalid() {
        return Stream.of(
            Arguments.of("good title, short text", " "),
            Arguments.of(" ", "short title, good text"),
            Arguments.of(" ", " ")
        );
    }

   /* @AfterAll
    public static void tearDown() {
		frontPageController.board = backupBoard;
	} */
    
}
