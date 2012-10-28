package maze;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class MotionSensors {
	
	//motion sensors are stored in this array
	private boolean[][] motionSensors;
	
	//We use the Set inferface here to prevent duplicates from being created
	//A set cannot contain duplicate elements
	private Set<Point> sensorLocations;
	
	int size;
	int numSensors;
	
	public MotionSensors(int size, int numSensors) {
		this.size = size;
		this.numSensors = numSensors;
		
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
	 * Creates <numSensors> unique motion sensor locations
	 */
	private void generateMotionSensorLocations() {
		int count = 0;
		//create infinte sensor locations until we have <numSensor> unique locations
		while (count < numSensors) {
			int x = randomNumberInRange();
			int y = randomNumberInRange();
			if (sensorLocations.add(new Point(x,y))) {
				count++;
			}
		}
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
	
	//Generate an integer in the range between 1 and size
	private int randomNumberInRange() {
		double ranged =  Math.random() * size;
		Long l = new Long(Math.round(ranged)); //between 0 and size
		return l.intValue() + 1; //between 1 and size+1
	}
	
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
	
	public static void main(String[] args) {
		Point x = new Point(123,25);
		Point y = new Point(123,25);
		System.out.println(x.equals(y));
		MotionSensors ms = new MotionSensors(20, 15);
		System.out.println(ms);
		
		boolean sensors[][] = ms.getMotionSensors();
		for (boolean[] br: sensors) {
			System.out.print("Line: ");
			for (boolean b: br) System.out.print(" " + b);
			System.out.println();
		}
	}
}
