package com.revature.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil {
	private static Logger log = Logger.getLogger(ConnectionUtil.class);
	
	
	public static Connection getConnection()
	{
		Connection conn = null;
		Properties prop = new Properties();
		try {
			prop.load(new FileReader("C:\\Users\\Warlock\\Documents\\git_repos\\Project_0\\src\\main\\resources\\application.properties"));						
			conn = DriverManager.getConnection(
					prop.getProperty("url"),
					prop.getProperty("username"),
					prop.getProperty("password")
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.warn("Unable to obtain connection to database",e);
		}catch(IOException e)
		{
			log.warn("Could not read file: application.properties.",e);
		}
		
		return conn;
	}
	
}
