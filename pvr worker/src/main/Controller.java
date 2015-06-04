package main;

import cube.Block;
import network.NetworkHandler;
import network.NodeDimension;

public class Controller extends Thread {

	private NetworkHandler networkHandler;
	private Block block;
	
	public Controller(int port) {
		this.networkHandler = new NetworkHandler(port, this);
	}
	
	@Override
	public void run() {
		init();
		doCalculation();
	}
	
	private void init() {
		setName("Controller Thread");
		networkHandler.waitForInitInformation(Thread.currentThread());
		networkHandler.waitForStart();
		networkHandler.setBlock(block);
	}
	
	private void doCalculation() {
		while (true) {
			block.calculate();
			block.updateValues();
			networkHandler.endIteration();
		}
	}
	
	public Block createBlock(NodeDimension dimension, boolean hasLowerX, boolean hasHigherX) {
		block = new Block(dimension, hasLowerX, hasHigherX);
		networkHandler.setBlock(block);
		System.out.println("End create of block");
		return block;
	}
	
	public Block getBlock() {
		return block;
	}
}

