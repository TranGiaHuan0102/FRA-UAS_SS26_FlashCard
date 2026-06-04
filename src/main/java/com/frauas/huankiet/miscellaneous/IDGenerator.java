package com.frauas.huankiet.miscellaneous;
import java.util.UUID;

public class IDGenerator {
	public static String generate_ID(){return UUID.randomUUID().toString().replace("-", "");}	
}
