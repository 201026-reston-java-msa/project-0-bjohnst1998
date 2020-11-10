package com.revature.services;

import java.util.List;

import com.revature.models.User;

public interface UserService {
	public User findUser(int userId);
	public List<User> findAll();
	public boolean insert(User u);
	public boolean update(User u);
	public User loginUser(String username, String password);
	public boolean checkIfUsernameExists(String username);
}
