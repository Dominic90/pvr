package ui;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Main;


public class MainPane extends ScrollPane {

	private static final double elementSize = 3;
	
	private Pane inner;
	
	private Rectangle[][] headmap;
	
	public MainPane() {
		inner = new Pane();
		inner.setLayoutX(0);
		inner.setLayoutY(0);
		inner.setPrefWidth(Main.x * elementSize);
		inner.setPrefHeight(Main.y * elementSize);
		setContent(inner);
		headmap = new Rectangle[Main.x][Main.y];
		for (int x = 0; x < headmap.length; x++) {
			for (int y = 0; y < headmap[x].length; y ++) {
				headmap[x][y] = new Rectangle(x * elementSize, y * elementSize, elementSize, elementSize);
				headmap[x][y].setFill(Color.web("#0000FF"));
				inner.getChildren().add(headmap[x][y]);
			}
		}
	}
	
	public void update(final double[][] nodeArea) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				for (int x = 0; x < nodeArea.length; x++) {
		        	for (int y = 0; y < nodeArea[x].length; y++) {
		        		update(x, y, (float)nodeArea[x][y]);
		        	}
		        }
			}
		});
	}
	
	public void update(int x, int y, float temp) {
		float ratio = 2 * (temp - 0) / (100 - 0);
		int b = Math.max(0, (int)(255*(1-ratio)));
		int r = Math.max(0, (int)(255*(ratio-1)));
		int g = 255 - b - r;
		String blue = Integer.toHexString(b);
		String red = Integer.toHexString(r);
		String green = Integer.toHexString(g);
		if (blue.length() < 2) {
			blue = "0" + blue;
		}
		if (red.length() < 2) {
			red = "0" + red;
		}
		if (green.length() < 2) {
			green = "0" + green;
		}
		try {
			headmap[x][y].setFill(Color.web(red + green + blue));
		}
		catch (Exception e) {
			headmap[x][y].setFill(Color.web(red.substring(red.length() - 2) + green.substring(green.length() - 2) + blue.substring(blue.length() - 2)));
			System.out.println("ERROR: " + red + " " + green + " " + blue);
		}
	}
}
