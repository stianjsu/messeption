package core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ForumPostReadWrite{
	public Map<String, String> fileRead(File file) throws IOException;
	public void fileWrite(File file, ForumPost post) throws IOException;
}