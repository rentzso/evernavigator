package twitterApi.dao;

import java.util.List;

import twitterApi.classes.Tweet;
import twitterApi.classes.TwitterUser;

public interface TweetDAO {
	/*
	 * getTweets return tweets for a given user 
	 * (include self-tweets and people being followed by user).
	 * if keyword is not null, The results will be filtered
	 * with this keyword.
	 */
	public List<Tweet> getTweets(TwitterUser user, String keyword);

}
