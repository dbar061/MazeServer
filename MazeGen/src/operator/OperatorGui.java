package operator;


import java.awt.Point;

import maze.Maze;
import maze.StdDraw;

public class OperatorGui {
	public static final int WINDOW_LENGTH = 600;
	public static final int WINDOW_HEIGHT = WINDOW_LENGTH;
	
	private Point robotPositions[] = new Point[4];
	KeySender keySender;
	MazeReceiver mazeReceiver;
	RobotPositionReceiver robotPositionReceivers[] = new RobotPositionReceiver[4];
	Maze maze = null;
	
	public OperatorGui(int robotId) {
		StdDraw.setCanvasSize(WINDOW_HEIGHT, WINDOW_LENGTH);
		
		int keySenderPort = 10000 + 1000 * (robotId - 1);
		keySender = new KeySender(keySenderPort);
		StdDraw.getFrame().addKeyListener(keySender);
		keySender.start();
		
		StdDraw.getFrame().setTitle("Robot operator " + robotId + " GUI");
		
		mazeReceiver = new MazeReceiver();
		mazeReceiver.start();

		for (int i = 0; i < 4; i++) {
			int portNumber = 10001 + (i * 1000);
			robotPositionReceivers[i] = new RobotPositionReceiver(portNumber);
			robotPositionReceivers[i].start();
		}
	}
	
	private void draw() {
		StdDraw.clear();
		
		if(maze != null) {
			maze.draw();
		}
		
		drawRobots();
		
		StdDraw.show(100);
	}

	private void drawRobots() {
		// Draw the four robots
		StdDraw.setPenColor(StdDraw.RED);
		for (int i = 0; i < 4; i++) {
			if (robotPositions[i] != null) {
				StdDraw.filledCircle(robotPositions[i].getX() + 0.5,
						robotPositions[i].getY() + 0.5, 0.375);
			}
		}
	}
	

	private void update() {
		for (int i = 0; i < 4; i++) {
			robotPositions[i] = robotPositionReceivers[i].getRobotPosition();
		}
		
		if(maze == null && mazeReceiver.getMaze() != null) {
			maze = mazeReceiver.getMaze();
			StdDraw.setXscale(0, maze.size + 2);
			StdDraw.setYscale(0, maze.size + 2);
		}
	}

	public static void main(String[] args) {
		int robotId = 1;
		if(args.length > 0) {
			robotId = Integer.parseInt(args[0]);
		}
		
		OperatorGui operatorGui = new OperatorGui(robotId);
		
		StdDraw.show(0);

		while (true) {
			operatorGui.update();
			operatorGui.draw();
		}
	}

}
