package main;


public class Main {
	
	public static final int port = 8000;
	
	public static void main(String args[]) {
		Controller controller = new Controller(port);
		controller.start();
	}
}
