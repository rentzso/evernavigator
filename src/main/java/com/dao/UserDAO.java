package com.dao;

import java.util.HashMap;

import com.classes.users.User;

public interface UserDAO {
	public int registerUser(User user);
	
	public int updateUser(User user);
	
	//public int deleteUser(User user);
	
	public User getUserInfo(String username);

}
