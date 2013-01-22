package twitterApi.dao;

import twitterApi.classes.TwitterUser;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TwitterUserDAO {
	
	/*
	 * getFollowers and getFollowed will return
	 * followers and followed ones by the user 
	 */
	public List<String> getFollowers(TwitterUser user);
	public List<String> getFollowed(TwitterUser user);
	
	/*
	 * method followUser and unfollowUser will return:
	 * 		1 if successfull
	 * 		0 otherwise
	 */
	public int followUser(TwitterUser user, String followed);
	public int unfollowUser(TwitterUser user, String followed);
	
	/*
	 * Method authenticate should return a valid authenticated user when found
	 * and authentication is successful otherwise it will return null.
	 * Response status is set accordingly:
	 * not_found --> SC_NOT_FOUND 404
	 * authentication failure --> SC_UNAUTHORIZED 401
	 * success --> SC_OK 200
	 */
	public TwitterUser authenticate(String username, String apiKey, HttpServletResponse response);
}
