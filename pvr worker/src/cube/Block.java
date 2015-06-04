package cube;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import network.NodeDimension;

public class Block {
	
	private static final int DIFF_TO_CENTER = 5;
	
	private ICube[][][] block;
	private int middleY;
	
	private int skipStartX = 0;
	private int skipEndX = 0;
	
	private NodeDimension dimension;
	
//	public Block() {
//		middleZ =  Main.z / 2;
//		this.headmap = headmap;
//		block = new ICube[Main.x][Main.y][Main.z];
//		createBlock();
//		if (Main.type == EType.BORDER) {
//			setBorderHeat();			
//		}
//		else if (Main.type == EType.MIDDLE) {
//			setMiddleHeat();
//		}
//		else {
//			setBorderSinus();
//		}
//	}
	
	public Block(NodeDimension dimension, boolean hasLowerX, boolean hasHigherX) {
		this.dimension = dimension;
		int xSize = dimension.getEndX() - dimension.getStartX() + 1;
		if (hasLowerX) {
			xSize++;
			skipStartX = 1;
		}
		if (hasHigherX) {
			xSize++;
			skipEndX = 1;
		}
		middleY = dimension.getMaxY() / 2;
		System.out.println("Size of X: " + xSize);
		block = new ICube[xSize][dimension.getMaxY()][dimension.getMaxZ()];
		
		createBlock();
		setBorderHeat();
	}
	
	private void createBlock() {
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				for (int z = 0; z < block[x][y].length; z++) {
					if ((x == 0 && dimension.getStartX() == 0) || y == 0 || z == 0 || 
							(x == block.length - 1 && dimension.getEndX() == dimension.getMaxX() - 1) || 
							y == block[x].length - 1 || z == block[x][y].length - 1) {
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
			for (int y = 0; y < block[x].length; y++) {
				block[x][y][0].setInitTemp(100);
			}
		}
	}
	
//	private void setMiddleHeat() {
//		int startX = Main.x / 2 - DIFF_TO_CENTER;
//		int endX = Main.x / 2 + DIFF_TO_CENTER;
//		int startY = Main.y / 2 - DIFF_TO_CENTER;
//		int endY = Main.y / 2 + DIFF_TO_CENTER;
//		int startZ = Main.z / 2 - DIFF_TO_CENTER;
//		int endZ = Main.z / 2 + DIFF_TO_CENTER;
//		
//		for (int x = startX; x < endX; x ++) {
//			for (int y = startY; y < endY; y++) {
//				for (int z = startZ; z < endZ; z++) {
//					block[x][y][z] = new BorderCube();
//					block[x][y][z].setInitTemp(100);
//				}
//			}
//		}
//	}
	
	private void setBorderSinus() {
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][0].length; z++) {
				block[x][0][z] = new SinusHeatCube();
			}
		}
	}
	
	public void calculate() {
		System.out.println("Skip Start: " + skipStartX + " Skip End: " + skipEndX);
		for (int x = skipStartX; x < block.length - skipEndX; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				for (int z = 0; z < block[x][y].length; z++) {
					block[x][y][z].setNewTemp(block, x, y, z);
				}
			}
		}
	}
	
	public void updateValues() {
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				for (int z = 0; z < block[x][y].length; z++) {
					block[x][y][z].updateTemp();
				}
			}
		}
	}
	
	public void sendToMaster(DataOutputStream dos) throws IOException {
		System.out.println("SendToMaster: " + skipStartX + " " + (block.length - skipEndX));
		for (int x = skipStartX; x < block.length - skipEndX; x ++) {
			for (int z = 0; z < block[x][middleY].length; z++) {
				dos.writeFloat(block[x][middleY][z].getCurrentTemp());
			}
			dos.flush();
		}
	}
	
	public void sendToNeighbor(int x, DataOutputStream dos) throws IOException {
		for (int y = 0; y < block[x].length; y++) {
			for (int z = 0; z < block[x][y].length; z++) {
				dos.writeFloat(block[x][y][z].getCurrentTemp());
			}
			dos.flush();
		}
	}
	
	public void receiveFromNeighbor(int x, DataInputStream dis) throws IOException {
		for (int y = 0; y < block[x].length; y++) {
			for (int z = 0; z < block[x][y].length; z++) {
				block[x][y][z].setNewTemp(dis.readFloat());
				block[x][y][z].updateTemp();
			}
		}
	}
	
	public int getBlockXSize() {
		return block.length - 1;
	}
}
