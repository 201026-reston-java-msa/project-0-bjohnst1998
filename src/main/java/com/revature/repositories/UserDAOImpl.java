package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDAOImpl implements UserDAO {
	private static Logger log = Logger.getLogger(EmployeeDAOImpl.class);

	@Override
	public User findUser(int userI) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM user_info WHERE user_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userI);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("user_password"),
						rs.getString("first_Name"), rs.getString("last_name"), rs.getString("email"),
						rs.getString("permissionLevel"));
				return u;
			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);
		}
		return null;
	}

	@Override
	public List<User> findAll() {
		List<User> list = new ArrayList<User>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM user_info";

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("user_password"),
						rs.getString("first_Name"), rs.getString("last_name"), rs.getString("email"),
						rs.getString("permissionLevel"));
				list.add(u);
			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);
		}

		return list;
	}

	@Override
	public boolean insert(User u) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO user_info(username,user_password,email,first_name,last_name,permissionLevel) VALUES(?,?,?,?,?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getFirstName());
			ps.setString(5, u.getLastName());
			ps.setString(6, u.getPermissionLevel());

			ps.executeUpdate();
			log.debug(u.getUsername()+ " was inserted successfully.");

		} catch (SQLException e) {
			log.warn("Could not access database", e);
		}
		return true;
	}

	@Override
	public boolean update(User u) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE user_info SET username = ?,user_password=?,email = ?,first_name =?,"
					+ " last_name = ?, permissionLevel = ? WHERE user_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(1, u.getPassword());
			ps.setString(1, u.getEmail());
			ps.setString(1, u.getFirstName());
			ps.setString(1, u.getLastName());
			ps.setString(1, u.getPermissionLevel());
			ps.setInt(1, u.getId());

			ps.executeUpdate();
			log.debug(u.getUsername()+ " was updated successfully.");

		} catch (SQLException e) {
			log.warn("Could not access database", e);
			return false;

		}

		return true;
	}

	@Override
	public User findUserByName(String username) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM user_info WHERE username = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				log.debug(username+ " was found in DB.");

				User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("user_password"),
						rs.getString("first_Name"), rs.getString("last_name"), rs.getString("email"),
						rs.getString("permissionLevel"));
				return u;
			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);
		}
		return null;
	}

	@Override
	public boolean checkForUsername(String username) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM user_info WHERE username = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				log.debug(username+ " was found in DB.");
				return true;
			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);
		}
		return false;

	}

}
