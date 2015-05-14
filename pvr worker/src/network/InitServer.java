package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.Controller;

public class InitServer {

	private int port;
	private Controller controller;
	
	public InitServer(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
	}
	
	public void waitForInitInformation() {
		ServerSocket clientConnect = null;
		Socket client = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
			clientConnect = new ServerSocket(port);
			client = clientConnect.accept(); // blocks
	        dis = new DataInputStream(
	                new BufferedInputStream(client.getInputStream()));
	        receiveLowerXSocket(dis);
	        receiveHigherXSocket(dis);
	        receiveNodeDimension(dis);
	        
	        dos = new DataOutputStream( 
					new BufferedOutputStream(client.getOutputStream()));
	        dos.writeUTF("ready init");
	        dos.flush();

	    }
	    catch ( Exception e ) {
	        e.printStackTrace();
	    }
		finally {
			try {
				if (dis != null) {
					dis.close();
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
	
	private void receiveLowerXSocket(DataInputStream dis) throws IOException {
		String lowerXSocketIp = dis.readUTF();
        int lowerXSocketPort = dis.readInt();
        if ( ! lowerXSocketIp.equals("null")) {
        	// create Socket object
        }
        else {
        	// set socket = null
        }
        System.out.println(lowerXSocketIp + ":" + lowerXSocketPort);
	}
	
	private void receiveHigherXSocket(DataInputStream dis) throws IOException {
		String higherXSocketIp = dis.readUTF();
        int higherXSocketPort = dis.readInt();
        if ( ! higherXSocketIp.equals("null")) {
        	// create Socket object
        }
        else {
        	// set socket = null
        }
        System.out.println(higherXSocketIp + ":" + higherXSocketPort);
	}
	
	private void receiveNodeDimension(DataInputStream dis) throws IOException {
		int startX = dis.readInt();
        int endX = dis.readInt();
        int maxY = dis.readInt();
        int maxZ = dis.readInt();
        
        NodeDimension dimension = new NodeDimension(startX, endX, maxY, maxZ);
        controller.createBlock(dimension);
        
        System.out.println(startX + " " + endX + " " +  maxY + " " + maxZ);
	}
	
	public void waitForStartCommand() {
		ServerSocket clientConnect = null;
		Socket client = null;
		DataInputStream dis = null;
		try {
			clientConnect = new ServerSocket(port);
			client = clientConnect.accept(); // blocks
	        dis = new DataInputStream(
	                new BufferedInputStream(client.getInputStream()));
	        String command = dis.readUTF();
	        System.out.println(command);
	    }
	    catch ( Exception e ) {
	        e.printStackTrace();
	    }
		finally {
			try {
				if (dis != null) {
					dis.close();
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
		synchronized (controller) {
			controller.notify();			
		}
	}
}
