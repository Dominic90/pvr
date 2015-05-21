package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import ui.MainPane;

public class DataReceiver extends Thread {

	private SocketInformation serverSocket;
	private MainPane pane;
	private double[][] nodeArea;
	private int startX;
	private int sizeX;
	private int sizeY;
	private CyclicBarrier barrier;
	
	private int counter = 0;
	
	public DataReceiver(NodeDimension dimension, SocketInformation serverSocket, CyclicBarrier barrier) {
		startX = dimension.getStartX();
		sizeX = dimension.getEndX() - dimension.getStartX() + 1;
		sizeY = dimension.getMaxY();
		this.serverSocket = serverSocket;
		this.barrier = barrier;
		nodeArea = new double[sizeX][sizeY];
	}
	
	public void setPane(MainPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void run() {
		while (true) {
			System.out.println("Node: " + startX + " " + counter);
			counter++;
			// TODO: put to top when worker is optimized
			ServerSocket clientConnect = null;
			Socket client = null;
			DataInputStream dis = null;
			DataOutputStream dos = null;
			try {
				// wait for data
				clientConnect = new ServerSocket(serverSocket.getPort());
				client = clientConnect.accept(); // blocks
		        dis = new DataInputStream(
		                new BufferedInputStream(client.getInputStream()));
		       
		        for (int x = 0; x < sizeX; x++) {
		        	for (int y = 0; y < sizeY; y++) {
		        		double d = dis.readFloat();
		        		nodeArea[x][y] = d;
		        	}
		        }
		        
		        // update heatmap
				pane.update(nodeArea, startX);
				
				// wait till all nodes received data
				// TODO: barrier
		        barrier.await();
				
				// inform node to proceed
		        dos = new DataOutputStream( 
						new BufferedOutputStream(client.getOutputStream()));
		        dos.writeUTF("proceed");
		        dos.flush();
		        
		        // TODO: put below when worker is optimized
			}
			catch ( Exception e ) {
		        e.printStackTrace();
		    }
			finally {
				try {
					if (dis != null) {
						dis.close();
					}
					if (dos != null) {
						dos.close();
					}
					if (client != null) {
						client.close();
					}
					if (clientConnect != null) {
						clientConnect.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
