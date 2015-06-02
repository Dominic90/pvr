package network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import cube.Block;

public class NetworkHandler {

	private int port;
	private Controller controller;
	private Block block;
	
	private CyclicBarrier barrier;
	private MasterHandler masterHandler;
	private InformLowerXNeighbor informLower;
	private InformHigherXNeighbor informHigher;
	
	public NetworkHandler(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
		barrier = new CyclicBarrier(3);
	}
	
	public void waitForInitInformation(Thread controllerThread) {
		informLower = new InformLowerXNeighbor(port + 1, barrier);
		informHigher = new InformHigherXNeighbor(barrier);
		masterHandler = new MasterHandler(controllerThread, controller, port, barrier, informLower, informHigher);
		masterHandler.start();
	}
	
	public void endIteration() {
		try {
			System.out.println("End Iteration 1");
			barrier.await();
			System.out.println("End Iteration 2");
			barrier.await();
			System.out.println("End Iteration 3");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
}
