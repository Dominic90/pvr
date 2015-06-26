package util;

public class NodeDimension {

	private int startX;
	private int endX;
	private int maxX;
	private int maxY;
	private int maxZ;
	
	public NodeDimension(int startX, int endX, int maxX, int maxY, int maxZ) {
		this.startX = startX;
		this.endX = endX;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public int getStartX() {
		return startX;
	}

	public int getEndX() {
		return endX;
	}
	
	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}
	
	public int getMiddleX() {
		return maxX / 2;
	}
	
	public int getMiddleY() {
		return maxY / 2;
	}
	
	public int getMiddleZ() {
		return maxZ / 2;
	}
}
