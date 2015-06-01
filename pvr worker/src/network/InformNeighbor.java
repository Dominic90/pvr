package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import cube.Block;

public abstract class InformNeighbor extends Thread {

	protected Block block;
	protected SocketInformation clientSocket;
	protected int port;
	
	public InformNeighbor(Block block, SocketInformation clientSocket, int port) {
		this.block = block;
		this.clientSocket = clientSocket;
		this.port = port;
	}
	
	@Override
	public void run() {
		Socket sender = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			sendAndReceive(dos, dis);
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
		finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (dis != null) {
					dis.close();
				}
				if (sender != null) {
					sender.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void sendAndReceive(DataOutputStream dos, DataInputStream dis) throws Exception;
}
