package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ForumBoardTest {
	ForumPost post;
	ForumBoard board;

	@BeforeEach
	public void setup() {
		String title1 = "POST";
		String text1 = "Lorem ipsum dolor sit amet";
		board = new ForumBoard();
		board.newPost(title1, text1);
	}

	@Test
	@DisplayName("Test getter")
	public void testGetter() {
		post = board.getPost(0);
		assertEquals("POST", post.getTitle());
		assertEquals("Lorem ipsum dolor sit amet", post.getText());
	}

	@Test
	@DisplayName("Test save and load")
	public void testSaveLoad() throws IOException {
		String path = "core/";
		String fileName = "BoardTest.JSON";
		board.savePosts(path, fileName);
		ForumBoard board2 = new ForumBoard();
		board2.loadPosts(path, fileName);
		assertEquals(board.toString(), board2.toString());
	}

	@AfterAll
	public static void tearDown() {
		File testFile = new File(JSONReadWrite.ROOT_PATH +"core/BoardTest.JSON");
		testFile.delete();
	}

}
