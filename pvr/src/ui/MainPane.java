package ui;

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
	
	public void update(int x, int y, double temp) {
		String colorCode = "";
		if (temp < 20) {
			colorCode = "#0000FF";
		}
		else if (temp > 20 && temp < 40) {
			colorCode = "#00FFFF";
		}
		else if (temp > 40 && temp < 60) {
			colorCode = "#FFFF00";
		}
		else if (temp > 60 && temp < 80) {
			colorCode = "#FF0000";
		}
		else if (temp > 80) {
			colorCode = "#FFFFFF";
		}
		headmap[x][y].setFill(Color.web(colorCode));
	}
}
