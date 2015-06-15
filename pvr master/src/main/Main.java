package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import network.Node;
import network.NodeDimension;
import network.SocketInformation;
import ui.MainPane;

public class Main extends Application {

	public static int sizeX = 100; //-x
	public static int sizeY = 100; //-y
	public static int sizeZ = 100; //-z
	public static float leftTemperature = 100; //-left
	public static float rightTemperature = 0; //-right
	public static float topTemperature = 0; //-top
	public static float bottomTemperature = 0; //-bottom
	public static float frontTemperature = 0; //-front
	public static float backTemperature = 0; //-back
	public static float innerTemperature = 0; //-inner
	public static float alpha = (float)0.1; //-a
	public static EType type = EType.BORDER; //-t
	public static List<Node> nodes; //-n
	public static int iterations = 100; //-i
	
	private static CyclicBarrier barrier;
	
	public static void main(String args[]) {
		processArgs(args);
		launch(args);
	}
	
	public static void processArgs(String args[]) {
		List<SocketInformation> sockets = new ArrayList<SocketInformation>();
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
					switch (args[i+1]) {
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
							throw new IllegalArgumentException(args[i] + " is no valid argument");
					}
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
		nodes = new ArrayList<Node>();
		int xSize = sizeX / sockets.size();
		barrier = new CyclicBarrier(sockets.size() + 1);
		for (int i = 0; i < sockets.size(); i++) {
			int startX = xSize * i;
			int endX = xSize * i + xSize - 1;
			SocketInformation lower = null;
			if (i > 0) {
				lower = sockets.get(i - 1);
			}
			SocketInformation higher = null;
			if (i < sockets.size() - 1 && sockets.size() > 1) {
				higher = sockets.get(i + 1);
			}
			Node node = new Node(sockets.get(i), lower, higher, new NodeDimension(startX, endX, sizeX, sizeY, sizeZ), barrier);
			nodes.add(node);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Heatmap!");
        StackPane root = new StackPane();
        MainPane pane = new MainPane();
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefWidth(800);
        pane.setPrefHeight(600);
        root.getChildren().add(pane);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        new Controller(pane, barrier).start();
	}
}
