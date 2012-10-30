package operator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import controller.ServerQueue;


/*************************************************************************
 * 
 * @author:			Jun-Hong
 * @Author:			Devin Barry
 * @Date:			28.10.2012
 * @LastModified:	29.10.2012
 * 
 * This class receives timer and warning data from BlokIDE. It uses a
 * message passing queue to pass data between threads.
 * 
 * Data comes in a specific order from BlokeIDE and this order is
 * assumed to be unchanging in this class. No checking is performed to
 * ensure that data does not become disordered.
 * 
 *************************************************************************/
public class WarningAndCounterReceiver extends Thread {

	private int receivingPort;
	private ServerQueue<Integer> sq;
	
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
