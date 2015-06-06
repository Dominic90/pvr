package main;


public class Main {
	
	public static int port;
	
	public static void main(String args[]) {
		port = Integer.parseInt(args[0]);
		Controller controller = new Controller(port);
		controller.start();
	}
}
