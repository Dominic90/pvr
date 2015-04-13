package cube;

import javafx.application.Platform;
import main.Main;
import ui.MainPane;

public class Block {
	
	private MainPane headmap;
	private ICube[][][] block;
	private int middleZ;
	
	public Block(MainPane headmap) {
		middleZ =  Main.z / 2;
		this.headmap = headmap;
		block = new ICube[Main.x][Main.y][Main.z];
		createBlock();
		setInitHeat();
	}
	
	private void createBlock() {
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				for (int z = 0; z < block[x][y].length; z++) {
					if (x == 0 || y == 0 || z == 0 || 
							x == block.length - 1 || y == block[x].length - 1 || z == block[x][y].length - 1) {
						block[x][y][z] = new BorderCube();
					}
					else {
						block[x][y][z] = new NormalCube();						
					}
				}
			}
		}
	}
	
	private void setInitHeat() {
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][0].length; z++) {
				block[x][0][z].setInitTemp(100);
			}
		}
	}
	
	public void calculate(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		for (int x = startX; x < endX; x ++) {
			for (int y = startY; y < endY; y++) {
				for (int z = startZ; z < endZ; z++) {
					block[x][y][z].setNewTemp(block, x, y, z);
				}
			}
		}
	}
	
	public void updateValues(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		for (int x = startX; x < endX; x ++) {
			for (int y = startY; y < endY; y++) {
				for (int z = startZ; z < endZ; z++) {
					block[x][y][z].updateTemp();
				}
			}
		}
	}
	
	public void updateHeadmap() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				for (int x = 0; x < block.length; x ++) {
					for (int y = 0; y < block[x].length; y++) {
						headmap.update(x, y, block[x][y][middleZ].getCurrentTemp());
					}
				}
			}
		});
	}
}
