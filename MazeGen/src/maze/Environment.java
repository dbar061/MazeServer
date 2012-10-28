package maze;

import java.awt.Point;
import java.awt.geom.Point2D;
import draw.StdDraw;


/*************************************************************************
 * 
 * @Author: Devin Barry
 * @Date: 09.10.2012
 * @LastModified: 24.10.2012
 * 
 *                This code is based on maze gen code from an unspecified
 *                author. Original code is available from
 *                introcs.cs.princeton.edu
 * 
 *                Generates a perfect N-by-N maze using depth-first search with
 *                a stack.
 * 
 *                Note: this program generalises nicely to finding a random tree
 *                in a graph.
 * 
 *************************************************************************/

public class Environment {
	private final int MAZE_SIZE = 20; // Should be a multiple of 10 for best performance
	private static Maze maze;

	public static final int WINDOW_LENGTH = 600;
	public static final int WINDOW_HEIGHT = WINDOW_LENGTH;


	// public static final double SCALE = 2.0; //how the maze dimensions relate
	// to the window dimensions
	// private static final double L_SCALE = WINDOW_LENGTH * SCALE;
	// private static final double H_SCALE = WINDOW_HEIGHT * SCALE;

	public Environment() {
		StdDraw.setCanvasSize(WINDOW_HEIGHT, WINDOW_LENGTH); // pixel size of
																// window
		// StdDraw.setXscale(0, L_SCALE);
		// StdDraw.setYscale(0, H_SCALE);

		StdDraw.setXscale(0, MAZE_SIZE + 2);
		StdDraw.setYscale(0, MAZE_SIZE + 2);
		maze = new Maze(MAZE_SIZE);
		
		MazeSender mazeSender = new MazeSender(maze);
		mazeSender.start();
		
		QueryResponder queryResponder = new QueryResponder(maze);
		queryResponder.start();

	}

	public void draw() {
		maze.draw();
	}

	public void solve(boolean draw) {
		maze.draw();
		
		Point robotPositions[] = new Point[4];
		robotPositions[0] = new Point(1, 1); //bottom left
		robotPositions[1] = new Point(1, MAZE_SIZE); //top left
		robotPositions[2] = new Point(MAZE_SIZE, 1); //bottom right
		robotPositions[3] = new Point(MAZE_SIZE, MAZE_SIZE); //top right
		
		for (int i = 0; i < 4; i++) {
			if (robotPositions[i] != null) {
				maze.solve(robotPositions[i], maze.GetCentrePoint(), draw);
				System.out.println("Robot " + (i + 1) + " solved");
				if (draw) {
					StdDraw.clear();
					maze.draw();
				}
			}
		}
	}

	// a test client
	public static void main(String[] args) {
		Environment enviroment = new Environment();
		StdDraw.show(0);
		enviroment.solve(true);
		


		while (true) {
			enviroment.draw();
			StdDraw.show(0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// UDPServer udps = new UDPServer();
		// try {
		// udps.startServer(m);
		// }
		// catch (Exception e) {}
	}

}
