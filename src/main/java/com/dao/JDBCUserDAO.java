package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.classes.users.User;


@Repository
public class JDBCUserDAO implements UserDAO{
	
	private static Logger log = Logger.getLogger(JDBCUserDAO.class.getName());
	
	private final static String GET_USER = 
			"select username, userid, hashkey, salts, accesstoken, expirationdate, userauthority, email" +
	        "from users where username = :username";
	
	private final static String INSERT_USER = 
			"insert into users (username, userid, hashkey, salts, accesstoken, expirationdate, userauthority, email)" +
	        "values (:username, :userid, :hashkey, :salts, :accesstoken, :expirationdate, :userauthority, :email)";
	
	private final static String UPDATE_USER =
			"update users" +
			"set" +
			"userid = :userid," +
			"hashkey = :hashkey," +
			"salts = :salts," +
			"accessToken = :accesstoken," +
			"expirationDate = :expirationdate," +
			"userAuthority = :userauthority," +
			"email = :email" +
			"where username = :username";
	
	private final static String DELETE_USER = 
			"delete from users" +
	        "where username = :username";
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	private static final class UserMapper implements RowMapper<User> {

	    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
	        User user = new User();
	        user.setUsername(rs.getString("username"));
	        user.setUserid(rs.getInt("userid"));
	        user.setAccesstoken(rs.getString("accesstoken"));
	        user.setEmail(rs.getString("email"));
	        user.setExpirationdate(rs.getDate("expirationdate"));
	        user.setHashkey(rs.getString("hashkey"));
	        user.setSalts(rs.getString("salts"));
	        user.setUserauthority(rs.getString("userauthority"));
	        return user;
	    }
	}

	public int registerUser(User user) {
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);		
		try{
			return this.namedParameterJdbcTemplate.update(INSERT_USER, namedParameters);
		} catch (Exception e){
			log.error(e);
			return 0;
		}
		
	}

	public int updateUser(User user) {
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);		
		try{
			return this.namedParameterJdbcTemplate.update(UPDATE_USER, namedParameters);
		} catch (Exception e){
			log.error(e);
			return 0;
		}
	}

	public User getUserInfo(String username) {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource("username", username);
		try {
			return this.namedParameterJdbcTemplate.queryForObject(GET_USER, namedParameters, new UserMapper());
		} catch (Exception e){
			log.error(e);
			return null;
		}
	}
	
	
	
	

}

	
