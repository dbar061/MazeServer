package server;

import java.util.regex.Pattern;


/*************************************************************************
 * 
 * @Author:			Devin Barry
 * @Date:			27.10.2012
 * @LastModified:	29.10.2012
 * 
 * This class processes queries from the BlokIDE robot. The robot requests
 * data from the environment in the form of a query string. This data is
 * defined in our protocol document.
 * 
 * This class breaks the query into its respective strings and performs
 * error checking and other functionality to ensure the data from BlokIDE
 * can be processed correctly in Java.
 * 
 *************************************************************************/
public class ServerQuery {
	
	byte[] rawBytes;
	String rawData;
	int robotID;
	String query;
	String direction;
	int x;
	int y;

	public ServerQuery(byte[] rawBytes) {
		this.rawBytes = rawBytes;
		x = 0;
		y = 0;
		robotID = 0;
		//convert raw bytes into a string
		rawData = new String(rawBytes);
		//split string into tokens
		parseTokens();
	}
	
	private void parseTokens() {
		Pattern p = Pattern.compile("[,\\s\0]"); //comma or all white space and null (sometimes null is sent from C)
		String regex = p.pattern();
		String[] tokens = rawData.split(regex, 0); //apply pattern an infinite number of times
		
		query = tokens[1];
		robotID = Integer.parseInt(tokens[0].trim());
		
		if(query.equals("obstacle")) {
			//Debug
			//System.out.print("tokens: ");
			//for (String s: tokens) {
			//	System.out.println("\"" + s + "\"");
			//}
			
			try {
				x = Integer.parseInt(tokens[3].trim());
				y = Integer.parseInt(tokens[4].trim());
			}
			catch (NumberFormatException e) {
				System.out.println("Number formatting Error!!!");
			}
			
			direction = tokens[2];
		} else if(query.equals("sensor") || query.equals("switch")) {
			try {
				x = Integer.parseInt(tokens[2].trim());
				y = Integer.parseInt(tokens[3].trim());
			}
			catch (NumberFormatException e) {
				System.out.println("Number formatting Error!!!");
			}
		}
		//discard all other tokens, they are not needed
	}
	
	public String getQuery() {
		return new String(query);
	}
	
	public String getDirection() {
		return new String(direction);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRobotID() {
		return robotID;
	}
}
