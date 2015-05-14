package main;

import java.util.ArrayList;
import java.util.List;

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
	
	public static void main(String args[]) {
		processArgs(args);
		launch(args);
	}
	
	public static void processArgs(String args[]) {
		x = 100;
		y = 100;
		z = 100;
		type = EType.BORDER;
		nodes = new ArrayList<Node>();
		Node node = new Node(new SocketInformation("localhost", 8081), null, null, new NodeDimension(0, 100, y, z));
		nodes.add(node);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Heatmap!");
        StackPane root = new StackPane();
        MainPane pane = new MainPane();
        for (Node node : nodes) {
        	node.getReceiver().setPane(pane);
        }
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefWidth(800);
        pane.setPrefHeight(600);
        root.getChildren().add(pane);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        new Controller().start();
	}
}
