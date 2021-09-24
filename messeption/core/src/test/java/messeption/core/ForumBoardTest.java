package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonIOException;

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
		File file = new File("BoardTest.JSON");
		board.savePosts(file);
		ForumBoard board2 = new ForumBoard();
		board2.loadPosts(file);
		assertEquals(board.toString(), board2.toString());
	}

	@Test
	@DisplayName("Test save and load to default file")
	public void testSaveLoadDefault() throws IOException {
		JSONReadWrite.fileWrite(board);
		ForumBoard board2 = JSONReadWrite.fileRead();

		assertEquals(board2.toString(), board.toString());
	}

}
