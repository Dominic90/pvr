package cube;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import main.EType;
import network.NodeDimension;

public class Block {
	private float leftTemperature;
	private float rightTemperature; 
	private float topTemperature;
	private float bottomTemperature;
	private float frontTemperature;
	private float backTemperature;
	private float innerTemperature;
	
	private Cube[][][] block;
	private int middleZ;
	
	private int skipStartX = 0;
	private int skipEndX = 0;
	
	private NodeDimension dimension;
	private EType type;
	
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
		middleZ = dimension.getMiddleZ();
		System.out.println("Size of X: " + xSize);
		
		createBlock(xSize);
	}
	
	private void createBlock(int xSize) {
		block = new Cube[xSize][dimension.getMaxY()][dimension.getMaxZ()];
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
		this.type = type;
		System.out.println("Calculation Type: " + type.getType());
		if (type.equals(EType.BORDER)) {
			setBorderHeat();
		}
		else if (type.equals(EType.MIDDLE)) {
			setMiddleHeat();
		}
		else if (type.equals(EType.MIDDLE_LEFT)) {
			setMiddleLeftHeat();
		}
		else if (type.equals(EType.BORDER_SINUS)) {
			setSinusHeat();
		}
	}
	
	private void setBorderHeat() {
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][0].length; z++) {
				block[x][0][z].setInitTemp(leftTemperature);
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
	
	private void setMiddleLeftHeat() {
		int lowestTemperature = (int)innerTemperature;
		int highestTemperature = (int)leftTemperature;
		int temperatureDifference = highestTemperature - lowestTemperature;
		double maxDistanzeToMiddle = getMaxDistanceToMiddle();
		for (int x = 0; x < block.length; x++) {
			for (int z = 0; z < block[x][0].length; z++) {
				double distanceToMiddle = getPythagorean(x + dimension.getStartX() - dimension.getMiddleX(), z - dimension.getMiddleZ());
				double relDistanceToMiddle = (distanceToMiddle / maxDistanzeToMiddle);
				double temp = temperatureDifference * relDistanceToMiddle;
				block[x][0][z].setInitTemp((int)(highestTemperature - temp));
			}
		}
	}
	
	private double getPythagorean(int a, int b) {
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}
	
	private double getMaxDistanceToMiddle() {
		return getPythagorean(dimension.getMiddleX(), dimension.getMiddleZ());
	}
	
	private void setSinusHeat() {
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][0].length; z++) {
				block[x][0][z] = new SinusHeatCube();
			}
		}
	}
	
	public void setInitValues(float leftTemperature, float rightTemperature, float topTemperature, float bottomTemperature,
			float frontTemperature, float backTemperature, float innerTemperature) {
		this.leftTemperature = leftTemperature;
		this.rightTemperature = rightTemperature;
		this.topTemperature = topTemperature;
		this.bottomTemperature = bottomTemperature;
		this.frontTemperature = frontTemperature;
		this.backTemperature = backTemperature;
		this.innerTemperature = innerTemperature;
		
		setBorderTemperatures();
	}
	
	private void setBorderTemperatures() {
		setLeftTemperature();
		setRightTemperature();
		setTopTemperature();
		setBottomTemperature();
		setFrontTemperature();
		setBackTemperature();
		setInnerTemperature();
	}
	
	private void setLeftTemperature() {
		int y = 0;
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][y].length; z++) {
				block[x][y][z].setInitTemp(rightTemperature);
			}
		}
	}
	
	private void setRightTemperature() {
		int y = block[0].length - 1;
		for (int x = 0; x < block.length; x ++) {
			for (int z = 0; z < block[x][y].length; z++) {
				block[x][y][z].setInitTemp(rightTemperature);
			}
		}
	}
	
	private void setTopTemperature() {
		int z = block[0][0].length - 1;
		for (int x = 1; x < block.length - 1; x++) {
			for (int y = 1; y < block[x].length - 1; y++) {
				block[x][y][z].setInitTemp(topTemperature);
			}
		}
	}
	
	private void setBottomTemperature() {
		int z = 0;
		for (int x = 1; x < block.length - 1; x++) {
			for (int y = 1; y < block[x].length - 1; y++) {
				block[x][y][z].setInitTemp(bottomTemperature);
			}
		}
	}
	
	private void setFrontTemperature() {
		if (dimension.getMaxX() == dimension.getEndX() + 1) {
			int x = block.length - 1;
			for (int y = 1; y < block[x].length - 1; y++) {
				for (int z = 1; z < block[x][y].length - 1; z++) {
					block[x][y][z].setInitTemp(frontTemperature);
				}
			}			
		}
	}
	
	private void setBackTemperature() {
		if (dimension.getStartX() == 0) {
			int x = 0;
			for (int y = 1; y < block[x].length - 1; y++) {
				for (int z = 1; z < block[x][y].length - 1; z++) {
					block[x][y][z].setInitTemp(backTemperature);
				}
			}			
		}
	}
	
	private void setInnerTemperature() {
		for (int x = 1; x < block.length - 1; x++) {
			for (int y = 1; y < block[x].length - 1; y++) {
				for(int z = 1; z < block[x][y].length - 1; z++) {
					block[x][y][z].setInitTemp(innerTemperature);
				}
			}
		}
	}
	
	public void calculate() {
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
		for (int x = skipStartX; x < block.length - skipEndX; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				dos.writeFloat(block[x][y][middleZ].getCurrentTemp()); //TODO
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
