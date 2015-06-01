package main;

import cube.Block;
import network.NetworkHandler;
import network.NodeDimension;

public class Controller extends Thread {

	private NetworkHandler networkHandler;
	private NodeDimension dimension;
	private Block block;
	
	public Controller(int port) {
		this.networkHandler = new NetworkHandler(port, this);
	}
	
	@Override
	public void run() {
		setName("Controller Thread");
		System.out.println(1);
		networkHandler.waitForInitInformation(Thread.currentThread());
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		networkHandler.waitForStartCommand();
		block.test();
		System.out.println(2);
		networkHandler.setBlock(block);
//		
		while (true) {
			block.calculate();
			block.updateValues();
			
			networkHandler.endIteration();
		}
	}
	
	public Block createBlock(NodeDimension dimension, boolean hasLowerX, boolean hasHigherX) {
		this.dimension = dimension;
		block = new Block(dimension, hasLowerX, hasHigherX);
		System.out.println("End create of block");
		return block;
	}
	
	public Block getBlock() {
		return block;
	}
}

