package operator;


import java.awt.Point;

import maze.Maze;
import draw.StdDraw;
import controller.ServerQueue;

public class OperatorGui {
	public static final int WINDOW_LENGTH = 600;
	public static final int WINDOW_HEIGHT = WINDOW_LENGTH;
	
	private Point robotPositions[] = new Point[4];
	
	private int tenMinuteTimerValue = 600;
	private int threeSecTimerValue = 3;
	
	private boolean missionFailed = false;
	private boolean twoMinWarning = false;
	private boolean threeSecTimerWarning = false;
	
	private KeySender keySender;
	private MazeReceiver mazeReceiver;
	private RobotPositionReceiver robotPositionReceivers[] = new RobotPositionReceiver[4];
	private WarningAndCounterReceiver wacr;
	private Maze maze = null;
	
	private ServerQueue<Integer> sq;
	
	public OperatorGui(int robotId) {
		sq = new ServerQueue<Integer>();
		
		StdDraw.setCanvasSize(WINDOW_HEIGHT, WINDOW_LENGTH);
		
		int keySenderPort = 10000 + 1000 * (robotId - 1);
		keySender = new KeySender(keySenderPort);
		StdDraw.getFrame().addKeyListener(keySender);
		keySender.start();
		
		StdDraw.getFrame().setTitle("Robot operator " + robotId + " GUI");
		
		mazeReceiver = new MazeReceiver();
		mazeReceiver.start();
		
		int port = 10003 + (robotId - 1) * 1000;
		wacr = new WarningAndCounterReceiver(port, sq);
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

		if (missionFailed) {
			StdDraw.text(18,0.5, "Mission Fialed!");
		}
		
		if (threeSecTimerWarning) {
			StdDraw.textLeft(1,0.5, "Not all switches have been turned!");
		}
		
		if (twoMinWarning) {
			StdDraw.textLeft(10,-0.5, "Please get to the Switch zone!");
		}
		
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
		StdDraw.textLeft(0,-0.5, "Seconds left:" + String.valueOf(600-tenMinuteTimerValue));
		StdDraw.textLeft(5,-0.5, "3sec timer:" + String.valueOf(threeSecTimerValue));	
	}
	
	

	private void update() {
		//update robot positions
		for (int i = 0; i < 4; i++) {
			robotPositions[i] = robotPositionReceivers[i].getRobotPosition();
		}
		
		int missionFailed;
		//int tenMinuteTimerValue;
		//int threeSecTimerValue;
		int twoMinWarning;
		int threeSecTimerWarning;
		
		missionFailed = sq.get();
		tenMinuteTimerValue = sq.get();
		threeSecTimerValue = sq.get();
		twoMinWarning = sq.get();
		threeSecTimerWarning = sq.get();

		if (twoMinWarning == 1) {
			this.twoMinWarning = true;
		}
		else {
			this.twoMinWarning = false;
		}
		

		if (threeSecTimerWarning == 1) {
			this.threeSecTimerWarning = true;
		}
		else {
			this.threeSecTimerWarning = false;
		}
		
		if (missionFailed == 1) {
			this.missionFailed = true;
		}
		else {
			this.missionFailed  =false;
		}
		
		
		if(maze == null && mazeReceiver.getMaze() != null) {
			maze = mazeReceiver.getMaze();
			StdDraw.setXscale(0, maze.size + 2);
			StdDraw.setYscale(0, maze.size + 2);
		}
	}

	/**
	 * Creates the GUI for a single robot operator
	 * @param args
	 */
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
