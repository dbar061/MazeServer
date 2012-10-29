package operator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import controller.ServerQueue;


/**
 * This class now supports message passing
 * @author dbar061
 *
 */
public class WarningAndCounterReceiver extends Thread {

	private int receivingPort;
	private ServerQueue<Integer> sq;
	
	//public int missionFailed ;
	//public int tenMinuteTimerValue;
	//public int threeSecTimerValue ;
	//public int twoMinWarning ;
	//public int threeSecTimerWarning ;
	
	public WarningAndCounterReceiver(int port, ServerQueue<Integer> sq) {
		this.receivingPort = port;
		this.sq = sq;
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
				
				sq.put(new Integer(receiveData[0])); //missionFailed
				sq.put(new Integer(bytesToInt(receiveData[1], receiveData[2]))); //tenMinuteTimerValue
				sq.put(new Integer(bytesToInt(receiveData[3], receiveData[4]))); //threeSecTimerValue
				sq.put(new Integer(receiveData[5])); //twoMinWarning
				sq.put(new Integer(receiveData[6])); //threeSecTimerWarning
			
				//TODO: check these values, connect to GUI, maybe merge into position received from robot
				
				
				//tenMinuteTimerValue = bytesToInt(receiveData[1], receiveData[2]);
				//threeSecTimerValue = bytesToInt(receiveData[3], receiveData[4]);
				//twoMinWarning = receiveData[5];
				//threeSecTimerWarning = receiveData[6];
				serverSocket.close();
				//System.out.println("Received wacr");
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
