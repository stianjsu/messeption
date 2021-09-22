package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	
}
