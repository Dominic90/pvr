package main;

import cube.Block;
import network.NetworkHandler;
import network.NodeDimension;

public class Controller extends Thread {

	private NetworkHandler networkHandler;
	private NodeDimension dimension;
	private Block block;
	
	public Controller() {
		networkHandler = new NetworkHandler();
	}
	
	@Override
	public void run() {
		System.out.println(1);
		synchronized (this) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		block.test();
		System.out.println(2);
		networkHandler.setBlock(block);
		
		while (true) {
			block.calculate();
			block.updateValues();
			
			networkHandler.setEndIteration();
		}
	}
	
	public void createBlock(NodeDimension dimension) {
		this.dimension = dimension;
		block = new Block(dimension);
		System.out.println("End create of block");
	}
}

