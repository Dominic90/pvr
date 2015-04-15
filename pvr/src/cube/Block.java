package cube;

import javafx.application.Platform;
import main.EType;
import main.Main;
import ui.MainPane;

public class Block {
	
	private static final int DIFF_TO_CENTER = 5;
	
	private MainPane headmap;
	private ICube[][][] block;
	private int middleZ;
	
	public Block(MainPane headmap) {
		middleZ =  Main.z / 2;
		this.headmap = headmap;
		block = new ICube[Main.x][Main.y][Main.z];
		createBlock();
		if (Main.type == EType.BORDER) {
			setBorderHeat();			
		}
		else if (Main.type == EType.MIDDLE) {
			setMiddleHeat();
		}
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
	
	private void setBorderHeat() {
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][0].length; z++) {
				block[x][0][z].setInitTemp(100);
			}
		}
	}
	
	private void setMiddleHeat() {
		int startX = Main.x / 2 - DIFF_TO_CENTER;
		int endX = Main.x / 2 + DIFF_TO_CENTER;
		int startY = Main.y / 2 - DIFF_TO_CENTER;
		int endY = Main.y / 2 + DIFF_TO_CENTER;
		int startZ = Main.z / 2 - DIFF_TO_CENTER;
		int endZ = Main.z / 2 + DIFF_TO_CENTER;
		
		for (int x = startX; x < endX; x ++) {
			for (int y = startY; y < endY; y++) {
				for (int z = startZ; z < endZ; z++) {
					block[x][y][z] = new BorderCube();
					block[x][y][z].setInitTemp(100);
//					System.out.println(block[x][y][z].getCurrentTemp());
				}
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
				System.out.println("end");
			}
		});
	}
}
