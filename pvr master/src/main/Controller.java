package main;

import network.Node;

public class Controller extends Thread {

	public Controller() {
		
	}
	
	@Override
	public void run() {
		initializeNodes();
	}
	
	private void initializeNodes() {
		for (Node node : Main.nodes) {
			node.initalizeNode();
		}
		for (Node node : Main.nodes) {
			node.start();
		}
		
		for (Node node : Main.nodes) {
			node.receiveData();
		}
	}
}
