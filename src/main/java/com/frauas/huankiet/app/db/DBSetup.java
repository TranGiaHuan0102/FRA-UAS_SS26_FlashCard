package com.frauas.huankiet.app.db;
import java.sql.*;

public class DBSetup {
	public static Connection setup(){
		try{
			// Attempt connection to FlashCard DB
			Connection flashcard = DBConnectionController.AnyConnection("FlashCard");
			if (flashcard == null){
				System.out.println("First run detected!");
				System.out.println("Setting up database FlashCard...");
				
				// Create FlashCard DB if not exist
				Connection postgres = DBConnectionController.PostgreSQLConnection();
				if (postgres == null){
					System.err.println("DBSetup: Unable to access PostgreSQL!");
					return null;
				}
				postgres.setAutoCommit(true);
				Statement stmt = postgres.createStatement();
				stmt.execute("CREATE DATABASE \"FlashCard\" WITH OWNER = postgres ENCODING 'UTF8'");

				// Attempt connection to newly created FlashCard
				flashcard = DBConnectionController.AnyConnection("FlashCard");
				if (flashcard == null){
					System.err.println("DBSetup: FlashCard created but failed to access!");
					return null;
				}
				System.out.println("Database FlashCard successfully created!");
			}
			DBTableSetup.setup(flashcard);
			return flashcard;
		}
		catch (SQLException e){
			System.err.println("DBSetup: Unable to create database FlashCard\n");
			return null;
		}
	}
}
