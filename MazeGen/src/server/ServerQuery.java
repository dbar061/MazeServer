package server;

import java.util.regex.Pattern;

public class ServerQuery {
	
	String rawData;
	int robotID;
	String query;
	String direction;
	int x;
	int y;

	public ServerQuery(String rawData) {
		this.rawData = rawData;
		parseTokens();
	}
	
	private void parseTokens() {
		Pattern p = Pattern.compile("[, ]"); //comma or space
		String regex = p.pattern();
		String[] tokens = rawData.split(regex, 0); //apply pattern an infinite number of times
		
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
