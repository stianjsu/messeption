package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ForumPostTest {
	ForumPost post;
	String title1;
	String text1;
	String testDate;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
	
	
	@BeforeEach
	public void setup() {
		title1 = "POST";
		text1 = "Lorem ipsum dolor sit amet";
		post = new ForumPost(title1, text1);
		testDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
	}
	
	@Test
	@DisplayName("Test string getters")
	public void testGetter() {

		assertEquals(title1, post.getTitle(), "Wrong title from getter");
		assertEquals(text1, post.getText(), "Wrong text from getter");
		
		
	}
	@Test
	@DisplayName("Test time getters")
	public void testTimeGetter(){

		try {
			Date postDate = sdf.parse(post.getTimeStamp());
			Date testDateFormated = sdf.parse(testDate);

			long diff = Math.abs(postDate.getTime() - testDateFormated.getTime());

			assertTrue(diff < 10000, "Time difference is more than 10 seconds");

		} catch (ParseException e){
			assertTrue(false, "Exception thrown when parsing dates from string. \n" + e.getMessage());
		}
		
	}
}
