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

import main.Controller;
import cube.Block;

public class LowerNeighborConnection extends Thread {

	protected Block block;
	protected int port;
	protected CyclicBarrier barrier;
	
	private ServerSocket clientConnect;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public LowerNeighborConnection(int port, Block block, CyclicBarrier barrier) {
		this.port = port;
		this.block = block;
		this.barrier = barrier;
		System.out.println("ServerSocketPort: " + port);
		try {
			clientConnect = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		setName("Lower Client handler Thread");
		try {
			System.out.println("lower client started");
			establishConnection();
			System.out.println("connection established");
			update();
		} 
		catch (IOException e) {
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
	
	private void establishConnection() throws IOException {
		client = clientConnect.accept();
		dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
	}
	
	private void update() throws IOException, InterruptedException, BrokenBarrierException {
		while (Controller.run) {
			System.out.println("Lower before barrier");
	        barrier.await();
	        
	        System.out.println("start update lower");
	        block.receiveFromNeighbor(0, dis);
	        block.sendToNeighbor(1, dos);
			
	        barrier.await();
		}
	}
}
