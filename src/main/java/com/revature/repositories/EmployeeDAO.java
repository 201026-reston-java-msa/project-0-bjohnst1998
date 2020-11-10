package com.revature.repositories;

import java.util.List;

import com.revature.models.Employee;

public interface EmployeeDAO {
	
	public List<Employee> findAll();
	public Employee findByID(int id);
	public Employee findByEmail(String email);
	public List<Employee> findBySupervisor(int supervisorID);
	public boolean insert(Employee e);
	public boolean update(Employee e);
	public Employee findByUsername(String userName);
	
	
	
}
