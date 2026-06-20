package com.frauas.huankiet.app.db;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class DBConnectionController {
	// Configurations	
	private static final Dotenv dotenv = Dotenv.load();
	private static final String POSTGRES_URL = dotenv.get("POSTGRES_URL");
	private static final String DB_URL = dotenv.get("DB_URL");
	private static final String USERNAME = dotenv.get("DB_USERNAME");
	private static final String PASSWORD = dotenv.get("DB_PASSWORD");

	// Return a connection to admin PostgreSQL DB
	protected static Connection PostgreSQLConnection(){
		try{
			return DriverManager.getConnection(POSTGRES_URL, USERNAME, PASSWORD);
		}
		catch (SQLException e){
			System.err.println("DBConnectionController: Unable to access PostgreSQL!");
			e.printStackTrace();
			return null;
		}
	}

	// Return a connection to any DB
	protected static Connection AnyConnection(String dbName){
		try{
			return DriverManager.getConnection(DB_URL + dbName, USERNAME, PASSWORD);
		}
		catch (SQLException e){
			System.err.println("DBConnectionController: Unable to access database " + dbName);
			e.printStackTrace();
			return null;
		}
	}

	protected static void CloseConnection(Connection conn) throws DBConnectionException{
		try{
			conn.close();
		}
		catch (SQLException e){throw new DBConnectionException("Unable to close connection to DB", e);}
	}
}
