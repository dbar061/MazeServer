package operator;

import java.awt.Point;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class RobotPositionReceiver extends Thread {
	private int receivingPort;
	private Point robotPosition;

	public RobotPositionReceiver(int receivingPort) {
		this.receivingPort = receivingPort;
	}

	public void run() {
		while (true) {
			try {
				DatagramSocket serverSocket = new DatagramSocket(null);
				serverSocket.setReuseAddress(true);	 // Allow multiple recievers on this port
				serverSocket.bind(new InetSocketAddress("0.0.0.0", receivingPort));
				
				byte[] receiveData = new byte[1024];

				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				serverSocket.receive(receivePacket);
//				String dataReceived = new String(receivePacket.getData());
//				System.out.println("RECEIVED: " + dataReceived);
			
				int x = bytesToInt(receiveData[0], receiveData[1]);
				int y = bytesToInt(receiveData[2], receiveData[3]);
				
				System.out.println("Received: x: " + x + ", y: " + y);
				
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
