package main;

import cube.Block;
import network.NetworkHandler;
import network.NodeDimension;

public class Controller extends Thread {

	private NetworkHandler networkHandler;
	private Block block;
	private long calculationTime = 0;
	private long networkingTime = 0;
	
	public volatile static boolean run = true;
	
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
		while (run) {
			long calcStart = System.currentTimeMillis();
			block.calculate();
			block.updateValues();
			calculationTime += System.currentTimeMillis() - calcStart;
			long networkStart = System.currentTimeMillis();
			networkHandler.endIteration();
			networkingTime += System.currentTimeMillis() - networkStart;
		}
		
		System.out.println("Calculation Time: " + calculationTime + " Networking Time: " + networkingTime);
		System.out.println("Calculation finished");
	}
	
	public Block createBlock(NodeDimension dimension, boolean hasLowerX, boolean hasHigherX) {
		block = new Block(dimension, hasLowerX, hasHigherX);
		networkHandler.setBlock(block);
		System.out.println("End creation of block");
		return block;
	}
	
	public Block getBlock() {
		return block;
	}
}

