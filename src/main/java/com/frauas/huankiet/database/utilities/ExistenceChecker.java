package com.frauas.huankiet.database.utilities;
import com.frauas.huankiet.database.connection.FailedConnectionException;
import java.sql.SQLException;

public interface ExistenceChecker {
	public boolean check(String dbName) throws FailedConnectionException, SQLException;	
}
