package network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import cube.Block;

public class NetworkHandler {

	private int port;
	private Controller controller;
	private Block block;
	
	private MasterHandler masterHandler;
	private InformLowerXNeighbor informLower;
	private InformHigherXNeighbor informHigher;
	
	private CyclicBarrier barrier;
	
	public NetworkHandler(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
	}
	
	public void waitForInitInformation(Thread controllerThread) {
		informLower = new InformLowerXNeighbor(port + 1);
		informHigher = new InformHigherXNeighbor();
		masterHandler = new MasterHandler(this, controllerThread, controller, port, informLower, informHigher);
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
	
	public void setBarrier(CyclicBarrier barrier) {
		this.barrier = barrier;
	}
}
