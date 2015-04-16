package main;

import ui.MainPane;
import cube.Block;

public class Control extends Thread {

	private MainPane headmap;
	
	private Block block;
	private int middleZ = Main.z / 2;
	
	public Control(MainPane headmap) {
		this.headmap = headmap;
	}
	
	@Override
	public void run() {
		Thread t = new Thread();
		block = new Block(headmap);
		for (int i = 0; i < Main.iterations; i++) {
			block.calculate(0, 0, 0, Main.x, Main.y, Main.z);
			block.updateValues(0, 0, 0, Main.x, Main.y, Main.z);
			block.updateHeadmap();
			System.out.println("Iteraion: " + i);
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}
