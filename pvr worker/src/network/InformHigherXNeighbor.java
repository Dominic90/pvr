package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

import cube.Block;

public class InformHigherXNeighbor extends Thread {

	protected Block block;
	protected SocketInformation clientSocket;
	protected int port;
	protected CyclicBarrier barrier;
	
	public InformHigherXNeighbor(Block block, SocketInformation clientSocket, int port, CyclicBarrier barrier) {
		this.block = block;
		this.clientSocket = clientSocket;
		this.port = port;
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		Socket sender = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
//			sendAndReceive(dos, dis);
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

//	@Override
//	protected void sendAndReceive(DataOutputStream dos, DataInputStream dis)
//			throws Exception {
//		block.sendToNeighbor(block.getBlockXSize(), dos);
//		block.receiveFromNeighbor(block.getBlockXSize(), dis);
//	}

}
