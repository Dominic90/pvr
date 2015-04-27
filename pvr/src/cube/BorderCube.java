package cube;

public class BorderCube implements ICube {

	private float temp = 0;
	
	public BorderCube() {
		
	}

	@Override
	public float getCurrentTemp() {
		return temp;
	}
	
	@Override
	public void setInitTemp(float temp) {
		this.temp = temp;
	}
	
	@Override
	public void setNewTemp(float temp) {
		
	}
	
	@Override
	public void setNewTemp(ICube[][][] block, int x, int y, int z) {
		
	}
	
	@Override
	public void updateTemp() {
			
	}
}
