package ui;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Main;


public class MainPane extends ScrollPane {

	private static final double elementSize = 3;
	private static final int MIN_TEMP = 0;
	private static final int MAX_TEMP = 100;
	private final static double BLUE_HUE = Color.BLUE.getHue() ;
    private final static double RED_HUE = Color.RED.getHue() ;
	
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
	
	public void update(final double[][] nodeArea, int startX) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("node area lenght: " + nodeArea.length);
				for (int x = 0; x < nodeArea.length; x++) {
		        	for (int y = 0; y < nodeArea[x].length; y++) {
		        		update(x + startX, y, (float)nodeArea[x][y]);
		        	}
		        }
			}
		});
	}
	
	public void update(int x, int y, float temp) {
		Color color = null;
		if (temp < MIN_TEMP || temp > MAX_TEMP) {
			 color = Color.BLACK ;
		}
		else {
			double hue = BLUE_HUE + (RED_HUE - BLUE_HUE) * (temp - MIN_TEMP) / (MAX_TEMP - MIN_TEMP) ;
			color =  Color.hsb(hue, 1.0, 1.0);			
		}
		headmap[x][y].setFill(color);
	}
}
