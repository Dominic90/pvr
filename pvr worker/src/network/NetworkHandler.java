package network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import util.SocketInformation;
import cube.Block;

public class NetworkHandler {

	private int port;
	private Controller controller;
	private Block block;
	
	private MasterConnection masterConnection;
	private LowerNeighborConnection lowerNeighborConnection;
	private HigherNeighborConnection higherNeighborConnection;
	
	private CyclicBarrier barrier;
	
	public NetworkHandler(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
	}
	
	public void waitForInitInformation(Thread controllerThread) {
		masterConnection = new MasterConnection(this, controllerThread, controller, port);
		masterConnection.start();
	}
	
	public void waitForStart() {
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startInformLower(SocketInformation lowerXSocket) {
		if (lowerXSocket != null) {
			lowerNeighborConnection = new LowerNeighborConnection(port + 1, block, barrier);
        	lowerNeighborConnection.start();
        }
	}
	
	public void startInformHigher(SocketInformation higherXSocket) {
		if (higherXSocket != null) {
			higherNeighborConnection = new HigherNeighborConnection(higherXSocket, block, barrier);
        	higherNeighborConnection.start();        	
        }
	}
	
	public void endIteration() {
		try {
			System.out.println("Calculation finished");
			barrier.await();
			barrier.await();
			System.out.println("Update finished");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
	
	public void setBarrier(CyclicBarrier barrier) {
		this.barrier = barrier;
	}
}
