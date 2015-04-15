package cube;

public class NormalCube implements ICube {

	private static final double alpha = 0.2;
	
	private double currentTemp = 0;
	private double newTemp = 0;
	
	@Override
	public double getCurrentTemp() {
		return currentTemp;
	}
	
	@Override
	public void setInitTemp(double temp) {
		this.currentTemp = temp;
	}
	
	@Override
	public void setNewTemp(double temp) {
		newTemp = temp;
	}
	
	@Override
	public void setNewTemp(ICube[][][] block, int x, int y, int z) {
		double factor = alpha * 1 / (1 * 1); // alpha, delta t, delta s 
		double sum = block[x+1][y][z].getCurrentTemp() + block[x-1][y][z].getCurrentTemp() + 
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
