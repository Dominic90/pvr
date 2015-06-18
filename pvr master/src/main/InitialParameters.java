package main;

import java.util.ArrayList;
import java.util.List;

import ui.MainPane;
import network.Node;
import network.SocketInformation;

public class InitialParameters {

	public int sizeX = 100; //-x
	public int sizeY = 100; //-y
	public int sizeZ = 100; //-z
	public float leftTemperature = 100; //-left
	public float rightTemperature = 0; //-right
	public float topTemperature = 0; //-top
	public float bottomTemperature = 0; //-bottom
	public float frontTemperature = 0; //-front
	public float backTemperature = 0; //-back
	public float innerTemperature = 0; //-inner
	public float alpha = (float)0.1; //-a
	public EType type = EType.BORDER; //-t
	public List<SocketInformation> sockets;
	public List<Node> nodes; //-n
	public int iterations = 100; //-i
	
	public InitialParameters(String[] args) {
		sockets = new ArrayList<SocketInformation>();
		for (int i = 0; i < args.length; i += 2) {
			switch(args[i]) {
				case "-x":
					sizeX = Integer.parseInt(args[i+1]);
					break;
				case "-y":
					sizeY = Integer.parseInt(args[i+1]);
					break;
				case "-z":
					sizeZ = Integer.parseInt(args[i+1]);
					break;
				case "-left":
					leftTemperature = Float.parseFloat(args[i+1]);
					MainPane.MAX_TEMP = (int)leftTemperature;
					break;
				case "-right":
					rightTemperature = Float.parseFloat(args[i+1]);
					break;
				case "-top":
					topTemperature = Float.parseFloat(args[i+1]);
					break;
				case "-bottom":
					bottomTemperature = Float.parseFloat(args[i+1]);
					break;
				case "-front":
					frontTemperature = Float.parseFloat(args[i+1]);
					break;
				case "-back":
					backTemperature = Float.parseFloat(args[i+1]);
					break;
				case "-inner":
					innerTemperature = Float.parseFloat(args[i+1]);
					MainPane.MIN_TEMP = (int)innerTemperature;
					break;
				case "-alpha":
					alpha = Float.parseFloat(args[i+1]);
					break;
				case "-type":
					switchType(args[i+1]);
					break;
				case "-n":
					sockets.add(new SocketInformation(args[i+1]));
					break;
				case "-i":
					iterations = Integer.parseInt(args[i+1]);
					break;
				default:
					throw new IllegalArgumentException(args[i] + " is no valid argument");
			}
		}
	}
	
	private void switchType(String arg) {
		switch (arg) {
			case "border":
				type = EType.BORDER;
				break;
			case "middle_left":
				type = EType.MIDDLE_LEFT;
				break;
			case "middle":
				type = EType.MIDDLE;
				break;
			case "border_sinus":
				type = EType.BORDER_SINUS;
				break;
			default:
				throw new IllegalArgumentException(arg + " is no valid argument");
		}
	}
}
