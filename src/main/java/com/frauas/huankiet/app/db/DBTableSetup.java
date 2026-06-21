package com.frauas.huankiet.app.db;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DBTableSetup {
	protected static void setup(Connection flashcard){
		try{
			// Load tables.sql 
			InputStream is = DBTableSetup.class.getResourceAsStream("/tables.sql");
			if (is == null){
				System.err.println("DBTableSetup: Unable to access tables.sql");
				return;
			}

			// Extract table names
			String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			List<String> tableNames = extractTableNames(sql);
		
			// Build tables if database is blank
			if (!checkTableNames(flashcard, tableNames.toArray(new String[0]))){
				flashcard.setAutoCommit(false);
				Statement stmt = flashcard.createStatement();
				stmt.execute(sql);
				flashcard.commit();
				System.out.println("DBTableSetup: Tables created successfully!");
			}
		}
		catch (IOException e){System.err.println("DBTableSetup: tables.sql loaded but can't be read");}
		catch (TableCheckException e){System.err.println("DBTableSetup" + e.getMessage());}
		catch (SQLException e){System.err.println("DBTableSetup: Unable to create tables in database FlashCard");}
	}

	private static List<String> extractTableNames(String sql){
		List<String> tableNames = new ArrayList<>();

		// Find all table names in SQL file
		Pattern pattern = Pattern.compile("CREATE TABLE\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);

		// Add them to tableNames list
		while (matcher.find()){
			tableNames.add(matcher.group(1).toLowerCase());
		}
		return tableNames;
	}


	private static boolean checkTableNames(Connection flashcard, String[] tableNames) throws TableCheckException{
		// Build query string out of tableNames array
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tableNames.length; i++){
			if (i > 0) {sb.append(", ");}
			sb.append("?");
		}
		
		String sql = "SELECT COUNT(*) FROM information_schema.tables "
			+ "WHERE table_catalog = ? AND table_schema = 'public' "
			+ "AND table_name IN (" + sb.toString() + ")";


		// Prepare statement
		try{
			PreparedStatement stmt = flashcard.prepareStatement(sql); // SQLException
			stmt.setString(1, "FlashCard");
			for (int i = 0; i < tableNames.length; i++){
				stmt.setString(i + 2, tableNames[i].toLowerCase());
			}

			// Compare hits to requirements
			ResultSet rs = stmt.executeQuery();
			rs.next(); // Skip first line
			return rs.getInt(1) > 0; // True if tables exist, False otherwise
		}
		catch (SQLException e){throw new TableCheckException("Table check failed!", e);}
	} 	
}
