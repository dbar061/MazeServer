package operator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


/**
 * This class needs fixing
 * @author dbar061
 *
 */
public class WarningAndCounterReceiver extends Thread {

	private int receivingPort;
	public int missionFailed ;
	public int tenMinuteTimerValue;
	public int threeSecTimerValue ;
	public int twoMinWarning ;
	public int threeSecTimerWarning ;
	
	public WarningAndCounterReceiver(int port) {
		this.receivingPort = port;
	}


	
	public void run() {
		while (true) {
			try {
				DatagramSocket serverSocket = new DatagramSocket(null);
				serverSocket.setReuseAddress(true); // Allow multiple receivers on this port
				serverSocket.bind(new InetSocketAddress("0.0.0.0", receivingPort));
				
				byte[] receiveData = new byte[1024];

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
			
				//TODO: check these values, connect to GUI, maybe merge into position received from robot
				missionFailed = receiveData[0];
				tenMinuteTimerValue = bytesToInt(receiveData[1], receiveData[2]);
				threeSecTimerValue = bytesToInt(receiveData[3], receiveData[4]);
				twoMinWarning = receiveData[5];
				threeSecTimerWarning = receiveData[6];
				serverSocket.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private int bytesToInt(byte b1, byte b2) {
		return (b1 & 0xFF) + (b2 & 0xFF) * 256;
	}

}
