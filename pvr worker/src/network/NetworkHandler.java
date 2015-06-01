package network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import cube.Block;

public class NetworkHandler {

	private int port;
	private Controller controller;
	private Block block;
	
	private SocketInformation serverSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	
	private CyclicBarrier barrier;
	private MasterHandler masterHandler;
	private InformLowerXNeighbor informLower;
	
	public NetworkHandler(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
		barrier = new CyclicBarrier(2);
	}
	
	public void waitForInitInformation(Thread controllerThread) {
		masterHandler = new MasterHandler(controllerThread, controller, port, barrier);
		masterHandler.start();
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
}
