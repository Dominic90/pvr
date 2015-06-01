package main;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import network.Node;
import ui.MainPane;

public class Controller extends Thread {

	private MainPane pane;
	private CyclicBarrier barrier;
	
	public Controller(MainPane pane, CyclicBarrier barrier) {
		this.pane = pane;
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		setName("Controller Thread");
		initializeNodes();
		receiveData();
	}
	
	private void initializeNodes() {
		for (Node node : Main.nodes) {
			node.initalizeNode(pane);
		}
		try {
			System.out.println("in cont");
			barrier.await();
			while (true) {
				barrier.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
	private void receiveData() {
		
	}
}
