package network;

public class NodeDimension {

	private int startX;
	private int endX;
	private int maxY;
	private int maxZ;
	
	public NodeDimension(int startX, int endX, int maxY, int maxZ) {
		this.startX = startX;
		this.endX = endX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public int getStartX() {
		return startX;
	}

	public int getEndX() {
		return endX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}
	
	
}
