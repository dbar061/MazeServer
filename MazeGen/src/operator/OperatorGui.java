package operator;


import java.awt.Point;

import maze.Maze;
import draw.StdDraw;

public class OperatorGui {
	public static final int WINDOW_LENGTH = 600;
	public static final int WINDOW_HEIGHT = WINDOW_LENGTH;
	
	private Point robotPositions[] = new Point[4];
	
	private int tenmintimervalue = 600;
	private int threesectimervalue = 3;
	
	private boolean missionFailed = false;
	private boolean twoMinWarning = false;
	private boolean threeSecTimerWarning = false;
	
	KeySender keySender;
	MazeReceiver mazeReceiver;
	RobotPositionReceiver robotPositionReceivers[] = new RobotPositionReceiver[4];
	private WarningAndCounterReceiver wacr  = new WarningAndCounterReceiver(10997);
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
		
		wacr.start();

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
		drawCounters();
		drawWarnings();
		
		StdDraw.show(100);
	}

	private void drawWarnings() {
		//Draw the warnings
		StdDraw.setPenColor(StdDraw.BLACK);

		if(missionFailed)
		{StdDraw.text(18,0.5, "Mission Fialed!");}
		
		if(threeSecTimerWarning)
		{StdDraw.textLeft(1,0.5, "Not all switches have been turned!");}
		
		if(twoMinWarning)
		{StdDraw.textLeft(10,-0.5, "Please get to the Switch zone!");}
		
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
	
	private void drawCounters() {
		// Draw the 10min & 3s counter
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.textLeft(0,-0.5, "Seconds left:" + String.valueOf(600-tenmintimervalue));
		StdDraw.textLeft(5,-0.5, "3sec timer:" + String.valueOf(threesectimervalue));
		
		}
	
	

	private void update() {
		//update robot positions
		for (int i = 0; i < 4; i++) {
			robotPositions[i] = robotPositionReceivers[i].getRobotPosition();
		}
		
		//update warnings & counters
		tenmintimervalue= wacr.tenmintimervalue;
		threesectimervalue=wacr.threesectimervalue;

		if (wacr.twoMinWarning == 1) {
			twoMinWarning=true;
		}
		else {
			twoMinWarning=false;
		}
		

		if(wacr.threeSecTimerWarning == 1)
		{threeSecTimerWarning=true;}
		else
		{threeSecTimerWarning=false;}
		
		if(wacr.missionFailed==1)
		{missionFailed=true;}
		else
		{missionFailed=false;}
		
		
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
