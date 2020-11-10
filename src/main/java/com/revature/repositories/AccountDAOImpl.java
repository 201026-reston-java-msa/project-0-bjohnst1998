package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {
	private static Logger log = Logger.getLogger(EmployeeDAOImpl.class);

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		List<Account> list = new ArrayList<Account>();

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM account";

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Account a = new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id"),rs.getString("account_type"),rs.getString("account_status"));

				list.add(a);
			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);
		}

		return list;
	}

	@Override
	public List<Account> findByUser(int userId) {
		// TODO Auto-generated method stub
		List<Account> list = new ArrayList<>();
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "SELECT * FROM account WHERE user_id = ?";
			
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
			 Account a = new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id"),rs.getString("account_type"),rs.getString("account_status"));
			 list.add(a);
				log.debug(userId+ "'s account #"+a.getId() + "was found successfully");

			}
			return list;
			
		}catch (SQLException e) {
			// TODO: handle exception
			log.warn("Failed to access database", e);
		}
		
		
		return null;
	}

	@Override
	public Account findAccount(int accountID) {
		// TODO Auto-generated method stub
		Account a = new Account();
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM account WHERE account_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, accountID);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				a = new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id"),rs.getString("account_type"),rs.getString("account_status"));
				
				log.debug(a.getId() + "was found successfully");


			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);

		}
		return a;
	}

	@Override
	public int insert(Account a) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO account(balance,user_id,account_type, account_status) VALUES (?,?,?,?) RETURNING account_id";

			PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, a.getBalance());
			ps.setInt(2, a.getUser_id());
			ps.setString(3, a.getAccountType());
			ps.setString(4, a.getAccountStatus());
			ps.executeUpdate();
			log.debug("New account was inserted successfully");
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next())
			{
				return rs.getInt("account_id");
			}

		} catch (SQLException e) {
			log.warn("Could not access database", e);
			return 0;

		}
		return 0;
	}

	@Override
	public boolean update(Account a) {
		// TODO Auto-generated method stub
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE account SET balance = ?,user_id=?,account_status = ? WHERE account_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, a.getBalance());
			ps.setInt(2, a.getUser_id());
			ps.setInt(3, a.getId());
			ps.setString(4, a.getAccountStatus());

			ps.executeUpdate();
			log.debug(a.getId() + "was updated successfully");
		} catch (SQLException e) {
			log.warn("Could not access database", e);
			return false;

		}

		return true;
	}

	public boolean transact(Account a, Account b) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "BEGIN; UPDATE account SET balance = ? WHERE account_id = ?; UPDATE account SET balance = ? WHERE account_id = ?;COMMIT;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, a.getBalance());		
			ps.setInt(2, a.getId());
			ps.setDouble(3, b.getBalance());
			ps.setInt(4, b.getId());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			log.warn("Could not complete transaction", e);
			return false;

		}

		return true;
	}

	@Override
	public List<Account> findByStatus(String status) {
		
		List<Account> list = new ArrayList<>();
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "SELECT * FROM account WHERE account_status = ?";
			
			PreparedStatement ps= connection.prepareStatement(sql);
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
			 Account a = new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id"),rs.getString("account_type"),rs.getString("account_status"));
			 list.add(a);

			}
			log.debug("All accounts with status " + status +" have been found");
			return list;
			
		}catch (SQLException e) {
			// TODO: handle exception
			log.warn("Failed to access database", e);
		}
		
		
		return null;
	}

}
