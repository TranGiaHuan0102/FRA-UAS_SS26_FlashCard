package com.frauas.huankiet.database.utilities;
import com.frauas.huankiet.database.connection.PostgresConnection;
import com.frauas.huankiet.database.connection.ConnectionManager;
import com.frauas.huankiet.database.connection.FailedConnectionException;
import java.sql.*;

public class DatabaseChecker implements ExistenceChecker {
	@Override
	public boolean check(String dbName) throws FailedConnectionException, SQLException{
		ConnectionManager cm = new PostgresConnection();
		String query = 	"SELECT 1 FROM pg_database WHERE datname = ?";
		
		Connection postgres = cm.returnConnection(); // FailedConnectionException
		PreparedStatement stmt = postgres.prepareStatement(query); // SQLException
		stmt.setString(1, dbName);
		return stmt.executeQuery().next(); // .next() returns True if at least 1 row
	}	
}
