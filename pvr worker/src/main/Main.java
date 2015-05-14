package main;

import network.InitServer;

public class Main {
	
	public static void main(String args[]) {
		Controller controller = new Controller();
		controller.start();
		InitServer init = new InitServer(8081, controller);
		init.waitForInitInformation();
		init.waitForStartCommand();
	}
}
