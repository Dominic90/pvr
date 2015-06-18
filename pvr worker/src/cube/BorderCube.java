package cube;

public class BorderCube implements Cube {

	private float temp = 0;
	
	public BorderCube() {}

	@Override
	public float getCurrentTemp() {
		return temp;
	}
	
	@Override
	public void setInitTemp(float temp) {
		this.temp = temp;
	}
	
	@Override
	public void setNewTemp(float temp) {}
	
	@Override
	public void setNewTemp(Cube[][][] block, int x, int y, int z) {}
	
	@Override
	public void updateTemp() {}
}
