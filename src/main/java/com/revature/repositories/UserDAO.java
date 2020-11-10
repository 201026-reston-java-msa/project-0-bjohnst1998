package com.revature.repositories;

import java.util.List;

import com.revature.models.User;

public interface UserDAO {

	public User findUser(int userI);
	public User findUserByName(String username);
	public List<User> findAll();
	public boolean insert(User u);
	public boolean update(User u);
	public boolean checkForUsername(String username);
}
