package main;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String args[]) {
		try {
			ServerSocket clientConnect = new ServerSocket(8081);
			Socket client = clientConnect.accept(); // blocks
	        DataInputStream dis =
	            new DataInputStream(
	                new BufferedInputStream(client.getInputStream()));
	        String msg = dis.readUTF();
	        System.out.println(msg);
	    }
	    catch ( Exception e ) {
	        e.printStackTrace();
	    }
	}
}
