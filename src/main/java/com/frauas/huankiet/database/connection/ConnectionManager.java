package com.frauas.huankiet.database.connection;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class ConnectionManager {
	// Configurations	
	private static final Dotenv dotenv = Dotenv.load();
	private static final String POSTGRES_URL = dotenv.get("POSTGRES_URL");
	private static final String DB_URL = dotenv.get("DB_URL");
	private static final String USERNAME = dotenv.get("DB_USERNAME");
	private static final String PASSWORD = dotenv.get("DB_PASSWORD");

	// Return a connection to admin PostgreSQL DB
	public static Connection PostgreSQLConnection(){
		try{
			return DriverManager.getConnection(POSTGRES_URL, USERNAME, PASSWORD);
		}
		catch (SQLException e){
			System.err.println("Connection Manager: Unable to access PostgreSQL!");
			return null;
		}
	}

	// Return a connection to any DB
	public static Connection AnyConnection(String dbName){
		try{
			return DriverManager.getConnection(DB_URL + dbName, USERNAME, PASSWORD);
		}
		catch (SQLException e){
			System.err.println("AnyConnection: Unable to access database " + dbName);
			return null;
		}
	}
}
