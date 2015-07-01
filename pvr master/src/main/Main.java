package main;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import network.Node;
import ui.MainPane;
import util.NodeDimension;
import util.SocketInformation;

public class Main extends Application {

	public static InitialParameters initialParameters;	
	private static CyclicBarrier barrier;
	
	public static void main(String args[]) {
		initialParameters = new InitialParameters(args);
		initializeNodes();
		launch(args);
	}
	
	public static void initializeNodes() { //TODO rename
		initialParameters.nodes = new ArrayList<Node>();
		int xSize = initialParameters.sizeX / initialParameters.sockets.size();
		barrier = new CyclicBarrier(initialParameters.sockets.size() + 1);
		for (int i = 0; i < initialParameters.sockets.size(); i++) {
			int startX = xSize * i;
			int endX = xSize * i + xSize - 1;
			SocketInformation lower = null;
			if (i > 0) {
				lower = initialParameters.sockets.get(i - 1);
			}
			SocketInformation higher = null;
			if (i < initialParameters.sockets.size() - 1 && initialParameters.sockets.size() > 1) {
				higher = initialParameters.sockets.get(i + 1);
			}
			NodeDimension nodeDimension = new NodeDimension(startX, endX, initialParameters.sizeX, 
					initialParameters.sizeY, initialParameters.sizeZ);
			Node node = new Node(initialParameters.sockets.get(i), lower, higher, nodeDimension, barrier);
			initialParameters.nodes.add(node);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Heatmap!");
        StackPane root = new StackPane();
        final MainPane pane = new MainPane();
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefWidth(initialParameters.sizeX);
        pane.setPrefHeight(initialParameters.sizeY);
        root.getChildren().add(pane);
        Scene scene = new Scene(root, 505, 505);
        pane.updateSize(500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
           
        	@Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                double rel = initialParameters.sizeY / initialParameters.sizeX;
                pane.updateSize(newSceneWidth.doubleValue() - 15, newSceneWidth.doubleValue() * rel - 15);
                
            }
        });
        
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            
        	@Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                double rel = initialParameters.sizeX / initialParameters.sizeY;
                pane.updateSize(newSceneHeight.doubleValue() * rel - 15, newSceneHeight.doubleValue() - 15);
            }
        });
        new Controller(pane, barrier).start();
	}
}
