package twitterApi.dao;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.apache.log4j.*;

import twitterApi.classes.TwitterUser;

@Repository
public class JDBCTwitterUserDAO  implements TwitterUserDAO{
	
	private static Logger log = Logger.getLogger(JDBCTwitterUserDAO.class.getName());
	
	private final String GET_USER = 
			"select realname, apikey " +
			"from users where username = :username";
	
	private final String GET_FOLLOWED =
			"select username " +
			"from followers where follower = :follower";
	
	private final String GET_FOLLOWERS =
			"select follower " +
			"from followers where username = :username";
	
	private final String FOLLOW = 
			"insert into followers (username, follower)" +
			"values ( :username , :follower )";
	
	private final String UNFOLLOW = 
			"delete from followers where " +
			"username = :username " +
			"and follower = :follower";
			
	
	private static final class TwitterUserMapper implements RowMapper<TwitterUser> {

	    public TwitterUser mapRow(ResultSet rs, int rowNum) throws SQLException {
	        TwitterUser user = new TwitterUser();
	        user.setUsername(rs.getString("username"));
	        user.setRealname(rs.getString("realname"));
	        return user;
	    }        
	}	

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	
	/*
	 * getFollowers and getFollowed will return
	 * followers and followed ones by the user 
	 */
	public List<String> getFollowers(TwitterUser user){
		SqlParameterSource namedParameters = new MapSqlParameterSource("username", user.getUsername());
		return this.namedParameterJdbcTemplate.query(GET_FOLLOWERS, 
				namedParameters, new RowMapper<String>(){
						public String mapRow(ResultSet rs, int rowNum) throws SQLException{
							return rs.getString("follower");
						}
		});
	}
	
	public List<String> getFollowed(TwitterUser user){
		SqlParameterSource namedParameters = new MapSqlParameterSource("follower", user.getUsername());
		return this.namedParameterJdbcTemplate.query(GET_FOLLOWED, 
				namedParameters, new RowMapper<String>(){
			public String mapRow(ResultSet rs, int rowNum) throws SQLException{
				return rs.getString("username");
			}
		});
	}
	
	/*
	 * method followUser and unfollowUser will return:
	 * 		1 if successfull
	 * 		0 otherwise
	 */
	public int followUser(TwitterUser user, String followed){
		SqlParameterSource namedParameters = 
				new MapSqlParameterSource("follower", user.getUsername())
		                         .addValue("username", followed);
		return this.namedParameterJdbcTemplate.update(FOLLOW, 
				namedParameters);
	}
	
	public int unfollowUser(TwitterUser user, String followed){
		SqlParameterSource namedParameters = 
				new MapSqlParameterSource("follower", user.getUsername())
		                         .addValue("username", followed);
		return this.namedParameterJdbcTemplate.update(UNFOLLOW, 
				namedParameters);
	}
	
	/*
	 * Method authenticate should return a valid user when found
	 * and authentication is successful otherwise it will return null.
	 * Response status is set accordingly:
	 * not_found --> SC_NOT_FOUND 404
	 * authentication failure --> SC_UNAUTHORIZED 401
	 * success --> SC_OK 200
	 */
	public TwitterUser authenticate(String username, String apiKey, HttpServletResponse response){
		SqlParameterSource namedParameters = new MapSqlParameterSource("username", username);
		Map<String, String> userProperties = new HashMap<String, String>(2);
		try {
			userProperties = this.namedParameterJdbcTemplate.queryForObject(GET_USER,
					namedParameters,
					
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int i) throws SQLException {
						Map<String, String> userProperties = new HashMap<String, String>(2);
						userProperties.put("realname", rs.getString("realname"));
						userProperties.put("apikey", rs.getString("apikey"));
						return userProperties;
					}
				} 
			
			);
		} catch (EmptyResultDataAccessException e){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		if (userProperties.get("apikey").equals(apiKey)){
			TwitterUser user = new TwitterUser();
			user.setUsername(username);
			user.setRealname(userProperties.get("realname"));
			response.setStatus(HttpServletResponse.SC_OK);
			return user;
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
	}

}
