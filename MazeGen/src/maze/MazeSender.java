package maze;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Sends the maze to the waiting clients
 * This class is called by the environment
 * @author Shafqat
 *
 */
public class MazeSender extends Thread {
	
	private final int SERVER_PORT = 9000;
	private Maze maze; // The maze to be sent

	public MazeSender(Maze maze) {
		this.maze = maze;
	}

	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);

			while (true) {

				Socket connectionSocket = serverSocket.accept();

				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));

				String request = inFromClient.readLine();
				if (request.equals("GetMazeData")) {
					ObjectOutputStream reply = new ObjectOutputStream(
							connectionSocket.getOutputStream());

					System.out.println("Sending reply");
					reply.writeObject(maze);
					reply.close();
				}
				connectionSocket.close();
				//serverSocket.close(); We resuse this socket so we cannot close it
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
