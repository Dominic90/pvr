package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import cube.Block;

public class HigherNeighborConnection extends Thread {

	protected Block block;
	protected SocketInformation higherNeighborSocket;
	protected int port;
	protected CyclicBarrier barrier;
	
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public HigherNeighborConnection(SocketInformation higherNeighborSocket, Block block, CyclicBarrier barrier) {
		this.higherNeighborSocket = higherNeighborSocket;
		this.block = block;
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("inform higher startet: " + (int)(higherNeighborSocket.getPort() + 1));
			socket = new Socket(higherNeighborSocket.getIp(), higherNeighborSocket.getPort() + 1);
			if (socket != null && socket.isConnected()) {
				System.out.println("connection established");
				dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				update();
			}
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
		finally {
			closeResources();
		}
	}
	
	private void update() throws IOException, InterruptedException, BrokenBarrierException {
		while (true) {
			System.out.println("higher before barrier");
	        barrier.await();
	        
	        System.out.println("start update lower");
	        block.sendToNeighbor(block.getBlockXSize() - 1, dos);
	        block.receiveFromNeighbor(block.getBlockXSize(), dis);
			
	        barrier.await();
		}
	}
	
	private void closeResources() {
		try {
			if (dos != null) {
				dos.close();
			}
			if (dis != null) {
				dis.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
