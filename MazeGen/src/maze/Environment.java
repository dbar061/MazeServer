package maze;

import java.awt.Point;
import draw.StdDraw;


/*************************************************************************
 * 
 * @Author:			Devin Barry
 * @Date:			09.10.2012
 * @LastModified:	29.10.2012
 * 
 * This class emulates the environment on the submarine. It first creates
 * the submarine maze and then starts the maze sender and maze responder
 * servers.
 * 
 * The maze sender server sends the created maze to the other clients. The
 * maze responder server allow the robots to question the environment to
 * detect if they are about to collide with walls, switches, sensors etc.
 * 
 * This class also ensure that all submarine mazes which are built actually
 * have solutions for all of the robots! It does this by solving the maze
 * for all robot starting positions first, before it OKs the maze.
 * 
 *************************************************************************/

public class Environment {
	private final int MAZE_SIZE = 20; // Should be a multiple of 10 for best performance
	private static Maze maze;

	public static final int WINDOW_LENGTH = 800;
	public static final int WINDOW_HEIGHT = WINDOW_LENGTH;
	
	private boolean validMaze;
	
	public Environment() {
		StdDraw.setCanvasSize(WINDOW_HEIGHT, WINDOW_LENGTH); // pixel size of window
		
		//Determines how the maze scales within the window
		StdDraw.setXscale(0, MAZE_SIZE + 2);
		StdDraw.setYscale(0, MAZE_SIZE + 2);
		
		validMaze = false;
		
		maze = new Maze(MAZE_SIZE); //create a new maze object
		
		StdDraw.show(0); //Show the maze window we have just created
		maze.draw(); //Draw the maze
		StdDraw.show(200); //Show this for 200 ms
		validMaze = solve(false); //solve the maze
	}

	/**
	 * Solves the maze for four robot starting positions
	 * @param draw
	 */
	private boolean solve(boolean draw) {
		maze.draw();
		
		Point robotPositions[] = new Point[4];
		robotPositions[0] = new Point(1, 1); //bottom left
		robotPositions[1] = new Point(1, MAZE_SIZE); //top left
		robotPositions[2] = new Point(MAZE_SIZE, 1); //bottom right
		robotPositions[3] = new Point(MAZE_SIZE, MAZE_SIZE); //top right
		
		//one position for each robot
		boolean[] solved = {false, false, false, false};
		
		for (int i = 0; i < 4; i++) {
			if (robotPositions[i] != null) {
				solved[i] = maze.solve(robotPositions[i], maze.GetCentrePoint(), draw);
				//System.out.println("Robot " + (i + 1) + " path solved: " + solved[i]);
				if (draw) {
					StdDraw.clear();
					maze.draw();
				}
			}
		}
		
		return (solved[0] && solved[1] && solved[2] && solved[3]);
	}
	
	public boolean getValidMaze() {
		return validMaze;
	}

	/**
	 * Main method for the environment server.
	 * This server mimics environmental variables inside the 
	 * submarine
	 * @param args
	 */
	public static void main(String[] args) {
		Environment environment = new Environment();
		//no need for explicit animation calls
		//animation is dealt with internally inside StdDraw
		
		if (!environment.getValidMaze()) {
			throw new RuntimeException("Maze cannot be solved! Create it again!");
		}
		else {
			System.out.println("All maze routes have been solved!");
		}
		
		//Create the maze sender server
		MazeSender mazeSender = new MazeSender(maze);
		mazeSender.start();
		//Create the query responder server
		QueryResponder queryResponder = new QueryResponder(maze);
		queryResponder.start();
	}

}
