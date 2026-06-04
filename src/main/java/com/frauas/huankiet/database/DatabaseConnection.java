package com.frauas.huankiet.database;
import java.sql.*;

public class DatabaseConnection {
	private static final String POSTGRES_URL, USERNAME, PASSWORD;

	// Load database credentials
	static{
		DatabaseConfigLoader loader = new DatabaseConfigLoader();
		POSTGRES_URL = loader.getURL();
		USERNAME = loader.getUsername();
		PASSWORD = loader.getPassword();
	}

	public static void initializeDatabase(){
		try (Connection conn = DriverManager.getConnection(POSTGRES_URL, USERNAME, PASSWORD)){
			if (!databaseExists(conn, "FlashcardSystem")){
				System.out.println("First run detected!");
				System.out.println("Setting up database...");
				runSetupScript(conn);
			}
		}
		catch (SQLException e){System.out.println(e.getMessage());}
	}

	private static boolean databaseExists(Connection conn, String dbName) throws SQLException{
		String query = 	"SELECT 1 FROM pg_database WHERE datname = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1, dbName);
			return stmt.executeQuery().next(); // .next() returns True if at least 1 row
		}
	}

	private static void runSetupScript(Connection conn) throws SQLException{
		// CREATE DATABASE cannot run inside a transaction block. setAutoCommit(true) to avoid this
		conn.setAutoCommit(true);
		Statement stmt = conn.createStatement();
		stmt.execute("CREATE DATABASE \"FlashcardSystem\" WITH OWNER = postgres ENCODING 'UTF8'");
	}
}
