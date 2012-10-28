package maze;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import server.ServerQuery;

public class QueryResponder extends Thread {
	private final int QUERY_PORT = 9001;
	private final int RESPONSE_PORT_BASE = 10002;
	
	private Maze maze;
	private ServerQuery sq;

	public QueryResponder(Maze maze) {
		this.maze = maze;
	}

	public void run() {
		byte[] receiveData;
		while (true) {
			try {
				// Listen for query
				DatagramSocket serverSocket = new DatagramSocket(null);
				serverSocket.setReuseAddress(true); // Allow multiple receivers
													// on this port
				serverSocket.bind(new InetSocketAddress("0.0.0.0", QUERY_PORT));

				//be completely certain that we clear the data array everytime
				receiveData = new byte[1024];
				for (int i = 0; i < receiveData.length; i++) {
					receiveData[i] = (byte) 0;
				}

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String query = new String(receivePacket.getData());
				
				sq = new ServerQuery(receiveData); //creat a new server query
				
				System.out.println("RECEIVED: " + query);

				respondToQuery(query);

				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void respondToQuery(String query) throws IOException {
		if (sq.getQuery().equals("obstacle")) {
		
		//String[] tokens = query.split(",");
		//int robotId = Integer.parseInt(tokens[0]);
		//if (tokens[1].equals("obstacle")) {
			//String direction = tokens[2];
			//int x = Integer.parseInt(tokens[3]);
			//int y = Integer.parseInt(tokens[4]);
			String response = "e";

			if (maze.getWall(sq.getDirection(), sq.getX(), sq.getY())) {
				response = "w";
			}

			InetAddress sendingAddress = InetAddress.getByName("localhost");

			byte[] sendData = new byte[1];

			DatagramSocket clientSocket = new DatagramSocket();

			sendData[0] = (byte) response.charAt(0);

			System.out.println("Sending: " + (int) (sendData[0]));

			int responsePort = RESPONSE_PORT_BASE + 1000 * (sq.getRobotID() - 1);

			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, sendingAddress, responsePort);
			clientSocket.send(sendPacket);

			clientSocket.close();
		} else {
			System.out.println("Invalid query: " + query);
		}
	}

}
