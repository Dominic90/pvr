package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.MainPane;

public class Main extends Application {

	public static final int x = 500;
	public static final int y = 500;
	public static final int z = 500;
	public static EType type = EType.BORDER;
	public static final int THREADS = 2;
	public static final int iterations = 10000;
    
	private Stage primayStage;
	public MainPane pane;
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	this.primayStage = primaryStage;
        primaryStage.setTitle("Hello World!");
        StackPane root = new StackPane();
        pane = new MainPane();
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefWidth(800);
        pane.setPrefHeight(600);
        root.getChildren().add(pane);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        new Control(pane).start();
    }
}
