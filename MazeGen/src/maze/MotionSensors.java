package maze;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;


/*************************************************************************
 * MotionSensors.java
 * 
 * @Author:			Devin Barry
 * @Date:			28.10.2012
 * @LastModified: 	28.10.2012
 * 
 * The MotionSensors class contains the data structures and methods for
 * creating motion sensors. It is very closely related to Maze.java and
 * is designed to create motion sensors in the maze.
 *************************************************************************/
public class MotionSensors {
	
	//motion sensors are stored in this array
	private boolean[][] motionSensors;
	
	//We use the Set interface here to prevent duplicates from being created
	//A set cannot contain duplicate elements
	private Set<Point> sensorLocations;
	
	int size;
	int numSensors;
	
	int centreMin, centreMax;
	
	public MotionSensors(int size, int numSensors, int centreMin, int centreMax) {
		this.size = size;
		this.numSensors = numSensors;
		this.centreMin = centreMin;
		this.centreMax = centreMax;
		
		//create the array of motion sensors
		motionSensors = new boolean[size + 2][size + 2];
		
		//create the array of positions of the motion sensors
		sensorLocations = new HashSet<Point>(numSensors);
		
		for (int x = 0; x < size + 2; x++) {
			for (int y = 0; y < size + 2; y++) {
				//initialise motion sensors to all false
				motionSensors[x][y] = false;
			}
		}
		
		generateMotionSensorLocations();
		createMotionSensors();
	}
	
	/**
	 * Creates <numSensors> unique motion sensor locations.
	 * Motion sensors cannot be created inside the centre
	 * of the maze.
	 */
	private void generateMotionSensorLocations() {
		int count = 0;
		//create infinite sensor locations until we have <numSensor> unique locations
		while (count < numSensors) {
			int x = randomNumberInRange();
			int y = randomNumberInRange();
			Point newPoint = new Point(x,y);
			if (!insideCentre(newPoint)) {
				if (sensorLocations.add(newPoint)) {
					count++;
				}
			}
		}
	}
	
	/**
	 * The centre square extends from centreMin to centreMax-1
	 * This function checks if the motion sensor would be created
	 * inside the centre square
	 * @param newPoint
	 * @return
	 */
	private boolean insideCentre(Point newPoint) {
		if (newPoint.x < centreMax && newPoint.x >= centreMin) {
			if (newPoint.y < centreMax && newPoint.y >= centreMin) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates all the motion sensors in the motion sensor
	 * array.
	 */
	private void createMotionSensors() {
		for (Point p: sensorLocations) {
			//place a motion sensor at the motion sensor location
			motionSensors[p.x][p.y] = true;
		}
	}
	
	//Generate an integer in the range between 1 and size inclusive
	private int randomNumberInRange() {
		double ranged =  Math.random() * (size-1); //between 0 and size-1
		Long l = new Long(Math.round(ranged)); //between 0 and size-1
		return l.intValue() + 1; //between 1 and size
	}
	
	/**
	 * Returns the data structure for motion sensors
	 * @return
	 */
	public boolean[][] getMotionSensors() {
		return motionSensors;
	}
	
	@Override
	public String toString() {
		String output = new String("MotionSensors:\n");
		for (Point p: sensorLocations) {
			output += p.toString() + "\n";
		}
		return output;
	}
	
	/**
	 * Test main method to test the creation of motion sensors
	 * @param args
	 */
	public static void main(String[] args) {
		Point x = new Point(123,25);
		Point y = new Point(123,25);
		System.out.println(x.equals(y));
		MotionSensors ms = new MotionSensors(20, 15, 12, 15);
		System.out.println(ms);
		
		boolean sensors[][] = ms.getMotionSensors();
		for (boolean[] br: sensors) {
			System.out.print("Line: ");
			for (boolean b: br) System.out.print(" " + b);
			System.out.println();
		}
	}
}
