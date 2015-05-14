package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ui.MainPane;

public class DataReceiver extends Thread {

	private MainPane pane;
	private double[][] nodeArea;
	private int sizeX;
	private int sizeY;
	// TODO needs barrier
	
	public DataReceiver(NodeDimension dimension) {
		sizeX = dimension.getEndX() - dimension.getStartX();
		sizeY = dimension.getMaxY();
		nodeArea = new double[sizeX][sizeY];
	}
	
	public void setPane(MainPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void run() {
		while (true) {
			
			// TODO: put to top when worker is optimized
			ServerSocket clientConnect = null;
			Socket client = null;
			DataInputStream dis = null;
			DataOutputStream dos = null;
			try {
				// wait for data
				clientConnect = new ServerSocket(8080); //TODO: custom port
				client = clientConnect.accept(); // blocks
		        dis = new DataInputStream(
		                new BufferedInputStream(client.getInputStream()));
		       
		        System.out.println(dis.readUTF());
		        for (int x = 0; x < sizeX; x++) {
		        	for (int y = 0; y < sizeY; y++) {
		        		double d = dis.readFloat();
		        		nodeArea[x][y] = d;
//		        		System.out.println(d);
		        	}
		        }
		        
		        // update heatmap
				pane.update(nodeArea);
				
				// wait till all nodes received data
				// TODO: barrier
		        
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
