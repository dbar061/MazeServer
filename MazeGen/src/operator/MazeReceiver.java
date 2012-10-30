package operator;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.Socket;

import maze.Maze;
import controller.SimpleServerQueue;

public class MazeReceiver extends Thread {
	
	private final int SERVER_PORT = 9000;
	private SimpleServerQueue<Maze> sq;

	public MazeReceiver(SimpleServerQueue<Maze> sq) {
		this.sq = sq;
	}

	public void run() {
		boolean mazeReceived = false;

		while (!mazeReceived) {

			try {
				Socket clientSocket = new Socket("localhost", SERVER_PORT);

				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				System.out.println("Sending request");
				outToServer.writeBytes("GetMazeData\n");

				ObjectInputStream mazeStream = new ObjectInputStream(clientSocket.getInputStream());
				
				//put the maze on the queue
				sq.put((Maze) mazeStream.readObject()); //always returns maze
				mazeReceived = true;
				
				System.out.println("Maze Received");
				clientSocket.close();

			} catch (ConnectException e) {
				System.out.println("Could not connect to environment...retrying in 1 second");
				sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e1) {
			// Ignore exception
		}
	}
}
