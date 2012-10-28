package devin.maze;

import java.awt.Point;
import java.awt.geom.Point2D;

import maze.Maze;

import devin.udp.UDPServer;
import draw.StdDraw;

/*************************************************************************
 * 
 * @Author: Devin Barry
 * @Date: 09.10.2012
 * @LastModified: 24.10.2012
 * 
 * This code is based on maze gen code from an unspecified author.
 * Original code is available from introcs.cs.princeton.edu
 * 
 * Generates a perfect N-by-N maze using depth-first search with a stack.
 * 
 * Note: this program generalises nicely to finding a random tree in a graph.
 * 
 *************************************************************************/

public class MazeGen {
	private int N; // dimension of maze
	private static Maze m;
	
	//locations of the 4 robots
	private Point robot1;
	private Point robot2;
	private Point robot3;
	private Point robot4;
	
	public static final int WINDOW_LENGTH = 600;
	public static final int WINDOW_HEIGHT = WINDOW_LENGTH;
	
	//public static final double SCALE = 2.0; //how the maze dimensions relate to the window dimensions
	//private static final double L_SCALE = WINDOW_LENGTH * SCALE;
	//private static final double H_SCALE = WINDOW_HEIGHT * SCALE; 
	
	public MazeGen(int N) {
		this.N = N;
		robot1 = new Point(1, 1); //bottom left
		robot2 = new Point(1, N); //top left
		robot3 = new Point(N, 1); //bottom right
		robot4 = new Point(N, N); //top right
		
		StdDraw.setCanvasSize(WINDOW_HEIGHT, WINDOW_LENGTH); //pixel size of window
		//StdDraw.setXscale(0, L_SCALE);
		//StdDraw.setYscale(0, H_SCALE);
		
		StdDraw.setXscale(0, N + 2);
		StdDraw.setYscale(0, N + 2);
		m = new Maze(N);
	}
	
	// display the maze in turtle graphics
	public void draw() {
		//Draw the four robots
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.filledCircle(robot1.getX() + 0.5, robot1.getY() + 0.5, 0.375);
		StdDraw.filledCircle(robot2.getX() + 0.5, robot2.getY() + 0.5, 0.375);
		StdDraw.filledCircle(robot3.getX() + 0.5, robot3.getY() + 0.5, 0.375);
		StdDraw.filledCircle(robot4.getX() + 0.5, robot4.getY() + 0.5, 0.375);
		
		//Draw the centre of the maze
		Point2D.Double centrePoint = m.GetCentrePoint();
		//if size is a factor of 10, this will be a multiple of 2
		int centreSize = m.getCentreSize();
		StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
		StdDraw.filledSquare(centrePoint.getX(), centrePoint.getY(), centreSize / 2);
		
		//Get the data from the maze
		boolean south[][] = m.getSouth();
		boolean north[][] = m.getNorth();
		boolean east[][] = m.getEast();
		boolean west[][] = m.getWest();

		StdDraw.setPenColor(StdDraw.BLACK);
		for (int x = 1; x <= N; x++) {
			for (int y = 1; y <= N; y++) {
				if (south[x][y]) {
					StdDraw.line(x, y, x + 1, y);
				}
				if (north[x][y]) {
					StdDraw.line(x, y + 1, x + 1, y + 1);
				}
				if (west[x][y]) {
					StdDraw.line(x, y, x, y + 1);
				}
				if (east[x][y]) {
					StdDraw.line(x + 1, y, x + 1, y + 1);
				}
			}
		}
		//was 1000
		StdDraw.show(0);
	}
	
	public void solve(boolean draw) {
		this.draw();
		m.solve(robot1, m.GetCentrePoint(), draw);
		System.out.println("Robot 1 solved");
		if (draw) {
			StdDraw.clear();
			this.draw();
		}
		m.solve(robot2, m.GetCentrePoint(), draw);
		System.out.println("Robot 2 solved");
		if (draw) {
			StdDraw.clear();
			this.draw();
		}
		m.solve(robot3, m.GetCentrePoint(), draw);
		System.out.println("Robot 3 solved");
		if (draw) {
			StdDraw.clear();
			this.draw();
		}
		m.solve(robot4, m.GetCentrePoint(), draw);
		System.out.println("Robot 4 solved");
	}

	// a test client
	public static void main(String[] args) {
		int N = 30; //Number should be a multiple of 10 for best performance
		MazeGen maze = new MazeGen(N);
		StdDraw.show(0);
		//maze.draw();
		maze.solve(false);
		UDPServer udps = new UDPServer();
		try {
			udps.startServer(m);
		}
		catch (Exception e) {}
	}

}
