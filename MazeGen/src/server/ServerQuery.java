package server;

import java.util.regex.Pattern;

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
		
		//Debug
		//System.out.print("tokens: ");
		//for (String s: tokens) {
		//	System.out.println("\"" + s + "\"");
		//}
		
		try {
			robotID = Integer.parseInt(tokens[0].trim());
			x = Integer.parseInt(tokens[3].trim());
			y = Integer.parseInt(tokens[4].trim());
		}
		catch (NumberFormatException e) {
			System.out.println("Number formatting Error!!!");
		}
		
		query = tokens[1];
		direction = tokens[2];
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
