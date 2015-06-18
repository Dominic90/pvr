package cube;

public class NormalCube implements Cube {

	public static float alpha = (float)0.1;
	
	private float currentTemp = 0;
	private float newTemp = 0;
	
	public NormalCube() {}
	
	@Override
	public float getCurrentTemp() {
		return currentTemp;
	}
	
	@Override
	public void setInitTemp(float temp) {
		this.currentTemp = temp;
		this.newTemp = temp;
	}
	
	@Override
	public void setNewTemp(float temp) {
		newTemp = temp;
	}
	
	@Override
	public void setNewTemp(Cube[][][] block, int x, int y, int z) {
		float factor = alpha * 1 / (1 * 1);
		float sum = block[x+1][y][z].getCurrentTemp() + block[x-1][y][z].getCurrentTemp() + 
				block[x][y+1][z].getCurrentTemp() + block[x][y-1][z].getCurrentTemp() + 
				block[x][y][z+1].getCurrentTemp() + block[x][y][z-1].getCurrentTemp()
				- 6 * block[x][y][z].getCurrentTemp();
		newTemp = block[x][y][z].getCurrentTemp() + factor * sum;
	}
	
	@Override
	public void updateTemp() {
		currentTemp = newTemp;
	}
}
