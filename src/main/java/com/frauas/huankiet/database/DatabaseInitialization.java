package com.frauas.huankiet.database;
import com.frauas.huankiet.database.utilities.ExistenceChecker;
import com.frauas.huankiet.database.utilities.DatabaseChecker;
import com.frauas.huankiet.database.connection.PostgresConnection;
import com.frauas.huankiet.database.connection.ConnectionManager;
import com.frauas.huankiet.database.connection.FailedConnectionException;
import java.sql.*;

public class DatabaseInitialization {
	public static void initializeDatabase(){
		ExistenceChecker checker = new DatabaseChecker();
		try{
			if (!checker.check("FlashCard")){
				System.out.println("First run detected!");
				System.out.println("Setting up DB...");
				runSetupScript();
			}
		}
		catch(FailedConnectionException e){System.err.println(e.getMessage());}
		catch(SQLException e){System.err.println("DB Initialization Failed!\n" + e.getMessage());}
	}

	private static void runSetupScript() throws FailedConnectionException, SQLException {
		// CREATE DATABASE cannot run inside a transaction block. setAutoCommit(true) to avoid this
		ConnectionManager cm = new PostgresConnection();
		Connection conn = cm.returnConnection();
		conn.setAutoCommit(true);
		Statement stmt = conn.createStatement();
		stmt.execute("CREATE DATABASE \"FlashCard\" WITH OWNER = postgres ENCODING 'UTF8'");
	}
}
