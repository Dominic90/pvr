package ui;

import java.io.DataInputStream;
import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Main;


public class MainPane extends ScrollPane {

	private static final double elementSize = 3;
	public static int MIN_TEMP = 0;
	public static int MAX_TEMP = 100;
	private final static double BLUE_HUE = Color.BLUE.getHue() ;
    private final static double RED_HUE = Color.RED.getHue() ;
	
	private ImageView imageView;
	private WritableImage heatmap;
	
	public MainPane() {
		imageView = new ImageView();
		imageView.setFitWidth(getPrefWidth());
		setContent(imageView);
		initHeatmap();
	}
	
	private void initHeatmap() {

		heatmap = new WritableImage(Main.initialParameters.sizeX, Main.initialParameters.sizeY);
		imageView.setImage(heatmap);
		PixelWriter writer = heatmap.getPixelWriter();
		for (int x = 0; x < heatmap.getWidth(); x++) {
			for (int y = 0; y < heatmap.getHeight(); y ++) {
				writer.setColor(x, y, Color.web("#0000FF"));
			}
		}
		
	}
	
	public void update(DataInputStream dis, final double[][] nodeArea, int sizeX, int sizeY, int startX) throws IOException {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				double d = dis.readFloat();
				nodeArea[x][y] = d;
			}
		}
		update(nodeArea, startX);
	}
	
	private void update(final double[][] nodeArea, int startX) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				for (int x = 0; x < nodeArea.length; x++) {
		        	for (int y = 0; y < nodeArea[x].length; y++) {
		        		setRectangleColor(x + startX, y, (float)nodeArea[x][y]);
		        	}
		        }
			}
		});
	}
	
	private void setRectangleColor(int x, int y, float temp) {
		Color color = null;
		if (temp < MIN_TEMP || temp > MAX_TEMP) {
			 color = Color.BLACK ;
		}
		else {
			double hue = BLUE_HUE + (RED_HUE - BLUE_HUE) * (temp - MIN_TEMP) / (MAX_TEMP - MIN_TEMP) ;
			color =  Color.hsb(hue, 1.0, 1.0);			
		}
		PixelWriter writer = heatmap.getPixelWriter();
		writer.setColor(x, y, color);
	}
	
	public void updateSize(double width, double height) {
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
	}
}
