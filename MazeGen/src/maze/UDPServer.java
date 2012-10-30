package maze;

import java.net.*;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * There is a bug in the server where strings from the past are
 * not overwritten properly. Find it and destory it!
 * @author: Devin Barry
 * @date: 24.10.2012
 *
 */
public class UDPServer {
	
	public UDPServer() {
	}
	
	/**
	 * starts a server to host the environment data
	 * Specifically it hosts the maze and motion sensors
	 * @param m
	 * @throws Exception
	 */
	public void startServer(Maze m) {
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(10001);
		}
		catch (IOException ioe) {
			System.out.println("Unable to create UDP Socket! Exiting.....");
			System.exit(1);
		}
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		for (int i = 0; i < 5; i++) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
			}
			catch (IOException ioe) {
				System.out.println("Error during socket read! Exiting.....");
				System.exit(1);
			}
			String sentence = new String( receivePacket.getData());
			
			System.out.println("RECEIVED: " + sentence);
			String direction = "";
			String x = "";
			String y = "";
			int xInt = 0, yInt = 0;
			
			StringTokenizer st = new StringTokenizer(sentence, ",");
			while (st.hasMoreTokens()) {
				direction = st.nextToken();
				//System.out.println(direction);
				x = st.nextToken();
				//System.out.println(x);
				y = st.nextToken();
				//System.out.println(y);
			}
			try {
				xInt = Integer.parseInt(x.trim());
				yInt = Integer.parseInt(y.trim());
			}
			catch (NumberFormatException e) {
				System.out.println("Error");
			}
			//System.out.println("Intx " + xInt + " Inty " + yInt);
			//System.out.println(direction + " " + x + " " + y);
			boolean result = m.getWall(direction, xInt, yInt);
			
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String response = new Boolean(result).toString();
			sendData = response.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			try {
				serverSocket.send(sendPacket);
			}
			catch (IOException ioe) {
				System.out.println("Error during socket write! Exiting.....");
				System.exit(1);
			}
		}
		serverSocket.close();
	}
}
