package cube;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import main.EType;
import network.NodeDimension;

public class Block {
		
	private ICube[][][] block;
	private int middleY;
	
	private int skipStartX = 0;
	private int skipEndX = 0;
	
	private NodeDimension dimension;
	
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
		middleY = dimension.getMiddleY();
		System.out.println("Size of X: " + xSize);
		
		createBlock(xSize);
	}
	
	private void createBlock(int xSize) {
		block = new ICube[xSize][dimension.getMaxY()][dimension.getMaxZ()];
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				for (int z = 0; z < block[x][y].length; z++) {
					if (needsBorderCube(x, y, z)) {
						block[x][y][z] = new BorderCube();
					}
					else {
						block[x][y][z] = new NormalCube();						
					}
				}
			}
		}
	}
	
	private boolean needsBorderCube(int x, int y, int z) {
		return ((x == 0 && dimension.getStartX() == 0) || y == 0 || z == 0 || 
				(x == block.length - 1 && dimension.getEndX() == dimension.getMaxX() - 1) || 
				y == block[x].length - 1 || z == block[x][y].length - 1);
	}
	
	public void setCalculationType(EType type) {
		System.out.println("Calculation Type: " + type.getType());
		if (type.equals(EType.BORDER)) {
			setBorderHeat();
		}
		else if (type.equals(EType.MIDDLE)) {
			setMiddleHeat();
		}
		else if (type.equals(EType.BORDER_SINUS)) {
			setSinusHeat();
		}
	}
	
	private void setBorderHeat() {
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				block[x][y][0].setInitTemp(100);
			}
		}
	}
	
	private void setMiddleHeat() {
		if (isMiddleHeatFullInNode()) {
			System.out.println("Middle Heat Full");
			int startX = dimension.getMiddleX() - 5 - dimension.getStartX();
			int endX = dimension.getEndX() - dimension.getMiddleX() + 5;
			for (int x = startX; x < endX; x++) {
				for (int y = dimension.getMiddleY() - 5; y < dimension.getMiddleY() + 5; y++) {
					for (int z = dimension.getMiddleZ() - 5; z < dimension.getMiddleZ() + 5; z++) {
						block[x][y][z] = new BorderCube();
						block[x][y][z].setInitTemp(100);
					}
				}
			}
		}
		else if (isMiddleHeatPartiallyInNode()) {
			setPartiallyMiddleHeat();
		}
	}
	
	private boolean isMiddleHeatFullInNode() {
		int middleX = dimension.getMiddleX();
		return (middleX - 5 > dimension.getStartX() && middleX + 5 < dimension.getEndX());
	}
	
	private boolean isMiddleHeatPartiallyInNode() {
		int middleX = dimension.getMiddleX();
		System.out.println("Middle: " + dimension.getMiddleX() + " start: " + dimension.getStartX() + " end: " + dimension.getEndX());
		return middleX - 5 < dimension.getStartX() && middleX + 5 > dimension.getStartX() ||
				middleX - 5 < dimension.getEndX() && middleX + 5 > dimension.getEndX();
	}
	
	private void setPartiallyMiddleHeat() {
		int middleX = dimension.getMiddleX();
		if (middleX - 5 < dimension.getStartX() && middleX + 5 > dimension.getStartX()) {
			int xSize = middleX + 5 - dimension.getStartX();
			System.out.println("XSize Start: " + xSize);
			for (int x = 0; x < xSize; x++) {
				System.out.println(x);
				for (int y = dimension.getMiddleY() - 5; y < dimension.getMiddleY() + 5; y++) {
					for (int z = dimension.getMiddleZ() - 5; z < dimension.getMiddleZ() + 5; z++) {
						block[x][y][z] = new BorderCube();
						block[x][y][z].setInitTemp(100);
					}
				}
			}
		}
		else {
			int xSize = dimension.getEndX() - middleX - 5;
			System.out.println("XSize End: " + xSize + " " + middleX + " " + (middleX + xSize) + " " + block.length);
			for (int x = block.length + xSize; x < block.length; x++) {
				System.out.println(x);
				for (int y = dimension.getMiddleY() - 5; y < dimension.getMiddleY() + 5; y++) {
					for (int z = dimension.getMiddleZ() - 5; z < dimension.getMiddleZ() + 5; z++) {
						block[x][y][z] = new BorderCube();
						block[x][y][z].setInitTemp(100);
					}
				}
			}
		}
	}
	
	private void setSinusHeat() {
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				block[x][y][0] = new SinusHeatCube();
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
