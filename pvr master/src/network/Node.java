package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class Node {
	private SocketInformation nodeSocket;
	private SocketInformation serverSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private NodeDimension dimension;
	private CyclicBarrier barrier;
	
	private DataReceiver receiver;
	
	public Node(SocketInformation nodeSocket, SocketInformation serverSocket, SocketInformation lowerXSocket, 
			SocketInformation higherXSocket, NodeDimension dimension, CyclicBarrier barrier) {
		this.nodeSocket = nodeSocket;
		this.serverSocket = serverSocket;
		this.lowerXSocket = lowerXSocket;
		this.higherXSocket = higherXSocket;
		this.dimension = dimension;
		this.barrier = barrier;
		
		receiver = new DataReceiver(dimension, serverSocket, barrier);
	}
	
	public DataReceiver getReceiver() {
		return receiver;
	}
	
	public void initalizeNode() {
		Socket sender = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			sender = new Socket(nodeSocket.getIp(), nodeSocket.getPort());
			if ( sender != null && sender.isConnected() ) {
				dos = new DataOutputStream( 
						new BufferedOutputStream(sender.getOutputStream()));
				sendServerSocket(dos);
				sendLowerXSocket(dos);
				sendHigherXSocket(dos);
				sendNodeDimensions(dos);
				
				dis = new DataInputStream(
		                new BufferedInputStream(sender.getInputStream()));
				System.out.println(dis.readUTF());
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
		finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (sender != null) {
					sender.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendServerSocket(DataOutputStream dos) throws IOException {
		dos.writeUTF(serverSocket.getIp());
		dos.writeInt(serverSocket.getPort());
		dos.flush();
	}
	
	private void sendLowerXSocket(DataOutputStream dos) throws IOException {
		if (lowerXSocket != null) {
			dos.writeUTF(lowerXSocket.getIp());
			dos.writeInt(lowerXSocket.getPort());					
		}
		else {
			dos.writeUTF("null");
			dos.writeInt(0);
		}
		dos.flush();
	}
	
	private void sendHigherXSocket(DataOutputStream dos) throws IOException {
		if (higherXSocket != null) {
			dos.writeUTF(higherXSocket.getIp());
			dos.writeInt(higherXSocket.getPort());				
		}
		else {
			dos.writeUTF("null");
			dos.writeInt(0);
		}
		dos.flush();
	}
	
	private void sendNodeDimensions(DataOutputStream dos) throws IOException {
		dos.writeInt(dimension.getStartX());
		dos.writeInt(dimension.getEndX());
		dos.writeInt(dimension.getMaxX());
		dos.writeInt(dimension.getMaxY());
		dos.writeInt(dimension.getMaxZ());
		dos.flush();
	}
	
	public void start() {
		Socket sender = null;
		DataOutputStream dos = null;
		try {
			sender = new Socket(nodeSocket.getIp(), nodeSocket.getPort());
			if ( sender != null && sender.isConnected() ) {
				dos = new DataOutputStream( 
						new BufferedOutputStream( sender.getOutputStream() ));
				dos.writeUTF("start");
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
		finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (sender != null) {
					sender.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void receiveData() {
		receiver.start();
	}
}
