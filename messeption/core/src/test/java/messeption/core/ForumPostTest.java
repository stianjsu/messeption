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

public class ForumPostTest {
	ForumPost post;
	
	@BeforeEach
	public void setup() {
		String title1 = "POST";
		String text1 = "Lorem ipsum dolor sit amet";
		post = new ForumPost(title1, text1);
	}
	
	@Test
	@DisplayName("Test getter")
	public void testGetter() {
		assertEquals("POST", post.getTitle());
		assertEquals("Lorem ipsum dolor sit amet", post.getText());
	}

	@Test
	@DisplayName("Test save/load")
	public void testSaveLoad() throws JsonIOException, IOException {
		File file = new File("test.JSON");
		post.save(file);
		ForumPost post2 = new ForumPost(file);
		assertEquals(post.getText(), post2.getText());
	}
	
}
