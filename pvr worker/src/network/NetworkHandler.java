package network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import cube.Block;

public class NetworkHandler {

	private int port;
	private Controller controller;
	private Block block;
	
	private MasterConnection masterHandler;
	private LowerNeighborConnection informLower;
	private HigherNeighborConnection informHigher;
	
	private CyclicBarrier barrier;
	
	public NetworkHandler(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
	}
	
	public void waitForInitInformation(Thread controllerThread) {
		masterHandler = new MasterConnection(this, controllerThread, controller, port);
		masterHandler.start();
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
			informLower = new LowerNeighborConnection(port + 1, block, barrier);
        	informLower.start();
        }
	}
	
	public void startInformHigher(SocketInformation higherXSocket) {
		if (higherXSocket != null) {
			informHigher = new HigherNeighborConnection(higherXSocket, block, barrier);
        	informHigher.start();        	
        }
	}
	
	public void endIteration() {
		try {
			barrier.await();
			barrier.await();
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
