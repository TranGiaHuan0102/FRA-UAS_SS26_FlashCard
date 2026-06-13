package com.frauas.huankiet.database.connection;
import java.sql.*;

public class PostgresConnection implements ConnectionManager{
	@Override
	public Connection returnConnection() throws FailedConnectionException{
		try{return DriverManager.getConnection(POSTGRES_URL, USERNAME, PASSWORD);}
		catch (SQLException e){throw new FailedConnectionException("postgres");}
	}	
}
