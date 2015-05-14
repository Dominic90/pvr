package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Node {
	private SocketInformation ownSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private NodeDimension dimension;
	
	private DataReceiver receiver;
	
	public Node(SocketInformation ownSocket, SocketInformation lowerXSocket, SocketInformation higherXSocket, NodeDimension dimension) {
		this.ownSocket = ownSocket;
		this.lowerXSocket = lowerXSocket;
		this.higherXSocket = higherXSocket;
		this.dimension = dimension;
		
		receiver = new DataReceiver(dimension);
	}
	
	public DataReceiver getReceiver() {
		return receiver;
	}
	
	public void initalizeNode() {
		Socket sender = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			sender = new Socket("localhost", 8081);
			if ( sender != null && sender.isConnected() ) {
				dos = new DataOutputStream( 
						new BufferedOutputStream(sender.getOutputStream()));
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
		dos.writeInt(dimension.getMaxY());
		dos.writeInt(dimension.getMaxZ());
		dos.flush();
	}
	
	public void start() {
		Socket sender = null;
		DataOutputStream dos = null;
		try {
			sender = new Socket("localhost", 8081);
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
