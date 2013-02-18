package evernote;

import com.classes.Graph;

public interface TagsDAO {
	public Graph getGraphForUser(int userId);
}
