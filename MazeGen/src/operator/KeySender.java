package operator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

public class KeySender extends Thread implements KeyListener {
	private Queue<Integer> keyQueue = new LinkedList<Integer>();
	private int sendingPort;

	public KeySender(int sendingPort) {
		this.sendingPort = sendingPort;
	}

	public void run() {

		while (true) {
			try {
				InetAddress sendingAddress = InetAddress.getByName("localhost");

				if (!keyQueue.isEmpty()) {
					byte[] sendData = new byte[1];

					DatagramSocket clientSocket = new DatagramSocket();

					Integer key = keyQueue.poll();
					sendData[0] = key.byteValue();

					System.out.println("Sending: " + (int)(sendData[0]));

					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, sendingAddress, sendingPort);
					clientSocket.send(sendPacket);

					clientSocket.close();
				} else {
					Thread.sleep(50);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Integer key = -1;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Up pressed");
			key = 0;
			break;
		case KeyEvent.VK_DOWN:
			System.out.println("Down pressed");
			key = 1;
			break;
		case KeyEvent.VK_LEFT:
			System.out.println("Left pressed");
			key = 2;
			break;
		case KeyEvent.VK_RIGHT:
			System.out.println("Right pressed");
			key = 3;
			break;
		}
		
		if(key != -1) {
			keyQueue.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
