package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Employee;
import com.revature.util.ConnectionUtil;

public class EmployeeDAOImpl implements EmployeeDAO {

	private static Logger log = Logger.getLogger(EmployeeDAOImpl.class);
	
	//Lists of Objects
	public List<Employee> findAll() {
		List<Employee> list = new ArrayList<Employee>();
		//Establish connection
		try {
		Connection conn = ConnectionUtil.getConnection();
		//Get list of info
		String sql = "SELECT * FROM employee";
		
		PreparedStatement s = conn.prepareStatement(sql);
		ResultSet rs = s.executeQuery();
		
		while(rs.next())
		{
			Employee e =new Employee(rs.getInt("id"), rs.getString("first_name"), 
					rs.getString("last_name"), rs.getString("email"), rs.getDouble("salary"));
			Employee supervisor = findByID(rs.getInt("supervisor"));
			e.setSupervisor(supervisor);
			list.add(e);
		}
		
		}catch (SQLException e)
		{
			log.warn("Could not complete request",e);
		}
		
		return list;
	}
	public List<Employee> findBySupervisor(int supervisorID) {
		List<Employee> list = new ArrayList<Employee>();
		
		return list;
	}

	//Single Objects
	public Employee findByID(int id) {
		Employee emp = new Employee();

		try {
			Connection conn = ConnectionUtil.getConnection();
			
			
			//Get list of info
			String sql = "SELECT * FROM employee WHERE id = ?";
			
			PreparedStatement s = conn.prepareStatement(sql);
			s.setInt(1, id);
			ResultSet rs = s.executeQuery();
			
			while(rs.next())
			{
				emp =new Employee(rs.getInt("id"), rs.getString("first_name"), 
						rs.getString("last_name"), rs.getString("email"), rs.getDouble("salary"));
			}
			
			}catch (SQLException e)
			{
				log.warn("Could not complete request",e);

			}	
		return emp;
	}

	public Employee findByEmail(String email) {
		Employee e = new Employee();
		return null;
	}

	//Data manipulation
	public boolean insert(Employee e) {
		try {
			Connection conn = ConnectionUtil.getConnection();
			
			
			//Get list of info
			String sql = "INSERT INTO employee(first_name,last_name,email,salary) VALUES (?,?,?,?)";
			
			PreparedStatement s = conn.prepareStatement(sql);
			s.setString(1, e.getFirstName());
			s.setString(2, e.getLastName());
			s.setString(3, e.getEmail());
			s.setDouble(4, e.getSalary());

			s.executeUpdate();
			return true;
			
			}catch (SQLException ex)
			{
				log.warn("Could not complete request",ex);

			}	
		return false;
	}

	public boolean update(Employee e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Employee findByUsername(String userName) {
		
		
		return null;
	}

}
