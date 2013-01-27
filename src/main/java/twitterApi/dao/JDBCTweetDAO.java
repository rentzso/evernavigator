package twitterApi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import twitterApi.classes.Tweet;
import twitterApi.classes.TwitterUser;

import org.springframework.stereotype.Repository;

@Repository
public class JDBCTweetDAO implements TweetDAO {
	
	private final static String GET_TWEETS_FROM_USER = 
			"select t.id, t.username, t.text, t.date " +
			"from tweets t where username = :username ";
	
	private final static String GET_TWEETS_FROM_FOLLOWERS = 
			"select t.id, t.username, t.text, t.date " +
			"from tweets t, followers f where f.username = t.username " +
			"and f.follower = :username ";
	
	private final static String UNION = " UNION ";
	
	private final static String WITH_KEYWORD = 
			" and t.text like :pattern ";
	
	
	private static final class TweetMapper implements RowMapper<Tweet> {

	    public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Tweet tweet = new Tweet();
	        tweet.setId(rs.getLong("id"));
	        tweet.setUsername(rs.getString("username"));
	        tweet.setText(rs.getString("text"));
	        tweet.setDate(rs.getDate("date"));
	        return tweet;
	    }        
	}	

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	/*
	 * getTweets return tweets for a given user 
	 * (include self-tweets and people being followed by user).
	 * if keyword is not null, The results will be filtered
	 * with this keyword.
	 */
	public List<Tweet> getTweets(TwitterUser user, String keyword){
		String sql;
		if (keyword == null){
			sql = GET_TWEETS_FROM_FOLLOWERS + UNION + GET_TWEETS_FROM_USER;
		} else {
			sql = GET_TWEETS_FROM_FOLLOWERS + WITH_KEYWORD + 
					UNION + GET_TWEETS_FROM_USER + WITH_KEYWORD;
		}
		
		SqlParameterSource namedParameters = new MapSqlParameterSource("username",user.getUsername())
			.addValue("pattern", "%" + keyword + "%");
		return this.namedParameterJdbcTemplate.query(sql, 
				namedParameters, new TweetMapper());
		
	}

}
