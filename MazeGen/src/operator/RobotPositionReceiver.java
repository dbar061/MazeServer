package operator;

import java.awt.Point;
//import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
//import console.DevinConsole;

/**
 * RobotPositionReceiver.java
 * 
 * Receives back from BlokIDE the position of the current robot
 * This data is passed to the client which instantiated this
 * RobotPositionReceiver, which uses the data to draw the robot
 * in its correct position.
 * 
 * @author:			Shafqat Bhuiyan
 * @author:			Devin Barry
 * @date:			27.10.2012
 * @lastModified:	29.10.2012
 * 
 */
public class RobotPositionReceiver extends Thread {
	
	private int receivingPort;
	private Point robotPosition;
	//private DevinConsole dc;
	//private PrintStream out;

	public RobotPositionReceiver(int receivingPort) {
		this.receivingPort = receivingPort;
		
		//Create output console
		//dc = new DevinConsole(new String("Robot Position Receiver - Port: " + receivingPort));
		//dc.createConsole();
		//out = dc.getPrintStream();
	}

	public void run() {
		while (true) {
			try {
				DatagramSocket serverSocket = new DatagramSocket(null);
				serverSocket.setReuseAddress(true);	 // Allow multiple receivers on this port
				serverSocket.bind(new InetSocketAddress("0.0.0.0", receivingPort));
				
				byte[] receiveData = new byte[1024];

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
			
				int x = bytesToInt(receiveData[0], receiveData[1]);
				int y = bytesToInt(receiveData[2], receiveData[3]);
				
				//Print to the unique console for this class
				//out.println("Received: x: " + x + ", y: " + y);
				
				if(robotPosition == null) {
					robotPosition = new Point();
				}
				
				robotPosition.setLocation(x, y);

				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}
	
	public Point getRobotPosition() {
		return robotPosition;
	}
	
	private int bytesToInt(byte b1, byte b2) {
		return (b1 & 0xFF) + (b2 & 0xFF) * 256;
	}

}
