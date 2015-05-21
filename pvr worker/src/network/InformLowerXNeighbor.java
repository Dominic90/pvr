package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import cube.Block;

public class InformLowerXNeighbor extends Thread {

	protected Block block;
	protected int port;
	protected CyclicBarrier barrier;
	
	public InformLowerXNeighbor(Block block, int port, CyclicBarrier barrier) {
		this.block = block;
		this.port = port;
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		while (true) {
			ServerSocket clientConnect = null;
			Socket client = null;
			DataInputStream dis = null;
			DataOutputStream dos = null;
			try {
				clientConnect = new ServerSocket(port);
				client = clientConnect.accept(); // blocks
		        dis = new DataInputStream(
		                new BufferedInputStream(client.getInputStream()));
		        barrier.await();
		        
		        block.receiveFromNeighbor(0, dis);
		        block.sendToNeighbor(0, dos);
				
		        barrier.await();
			}
			catch ( IOException e ) {
		        e.printStackTrace();
		    } catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
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
