package network;

import cube.Block;

public class NetworkHandler {

	private Block block;
	private InformMaster informMaster;
	
	public NetworkHandler() {
		
	}
	
	public void setEndIteration() {
		// thread sent left
		
		// thread sent right
		
		// thread inform master
		informMaster = new InformMaster(block);
		informMaster.start();
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
}
