package com.revature.services;

import java.util.List;

import com.revature.models.User;
import com.revature.repositories.UserDAO;
import com.revature.repositories.UserDAOImpl;

public class UserServiceImpl implements UserService {
	UserDAO uDao = null;

	 public UserServiceImpl() {
		 uDao = new UserDAOImpl();
	}
	
	@Override
	public User findUser(int userId) {
		// TODO Auto-generated method stub
		return uDao.findUser(userId);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return uDao.findAll();
	}

	@Override
	public boolean insert(User u) {
		// TODO Auto-generated method stub
		return uDao.insert(u);
	}

	@Override
	public boolean update(User u) {
		// TODO Auto-generated method stub
		return uDao.update(u);
	}

	@Override
	public User loginUser(String username, String password) {

		User u = uDao.findUserByName(username);
		if (u != null) {
			//Check to make sure the password is correct
			if(password.equals(u.getPassword()))
			{
				return u;
			}
		}

		return null;
	}

	@Override
	public boolean checkIfUsernameExists(String username) {
		// TODO Auto-generated method stub
		return uDao.checkForUsername(username);
	}

}
