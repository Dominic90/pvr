package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import cube.Block;

public class InformMaster extends Thread {

	private Block block;
	private SocketInformation serverSocket;
	
	public InformMaster(Block block, SocketInformation serverSocket) {
		this.block = block;
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		Socket sender = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			sender = new Socket(serverSocket.getIp(), serverSocket.getPort());
			if ( sender != null && sender.isConnected() ) {
				dos = new DataOutputStream( 
						new BufferedOutputStream(sender.getOutputStream()));
				
				block.send(dos);
				
				
				dis = new DataInputStream(
		                new BufferedInputStream(sender.getInputStream()));
				String answer = dis.readUTF();
				if (answer.equals("proceed")) {
					System.out.println(answer);					
				}
				else {
					// TODO: stop simulation
				}
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
}
