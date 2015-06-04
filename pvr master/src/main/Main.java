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

	public static int x;
	public static int y;
	public static int z;
	public static EType type = EType.BORDER;
	public static List<Node> nodes;
	public static int iterations = 10;
	
	private static CyclicBarrier barrier;
	
	public static void main(String args[]) {
		processArgs(args);
		launch(args);
	}
	
	public static void processArgs(String args[]) {
		x = 100;
		y = 100;
		z = 100;
		type = EType.BORDER;
		List<SocketInformation> sockets = new ArrayList<SocketInformation>();
		sockets.add(new SocketInformation("localhost", 8000));
		sockets.add(new SocketInformation("localhost", 8010));
//		sockets.add(new SocketInformation("localhost", 8020));
//		sockets.add(new SocketInformation("localhost", 8030));
		nodes = new ArrayList<Node>();
		int xSize = x / sockets.size();
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
			Node node = new Node(sockets.get(i), new SocketInformation("localhost", 7000 + i), 
					lower, higher, new NodeDimension(startX, endX, x, y, z), barrier);
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
