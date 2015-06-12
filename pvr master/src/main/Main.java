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

	public static int sizeX;
	public static int sizeY;
	public static int sizeZ;
	public static float leftTemperature;
	public static float rightTemperature;
	public static float topTemperature;
	public static float bottomTemperature;
	public static float frontTemperature;
	public static float backTemperature;
	public static float innerTemperature;
	public static float alpha;
	public static EType type;
	public static List<Node> nodes;
	public static int iterations;
	
	private static CyclicBarrier barrier;
	
	public static void main(String args[]) {
		processArgs(args);
		launch(args);
	}
	
	public static void processArgs(String args[]) {
		sizeX = 100;
		sizeY = 100;
		sizeZ = 100;
		leftTemperature = 100;
		rightTemperature = 100;
		topTemperature = 100;
		bottomTemperature = 100;
		frontTemperature = 100;
		backTemperature = 100;
		innerTemperature = 0;
		alpha = (float)0.1;
		type = EType.MIDDLE;
		iterations = 5000;
		List<SocketInformation> sockets = new ArrayList<SocketInformation>();
		sockets.add(new SocketInformation("localhost", 8000));
//		sockets.add(new SocketInformation("localhost", 8010));
//		sockets.add(new SocketInformation("localhost", 8020));
//		sockets.add(new SocketInformation("localhost", 8030));
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
