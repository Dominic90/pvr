package cube;

public class BorderCube implements ICube {

	private double temp = 0;
	
	public BorderCube() {
		
	}

	@Override
	public double getCurrentTemp() {
		return temp;
	}
	
	@Override
	public void setInitTemp(double temp) {
		this.temp = temp;
	}
	
	@Override
	public void setNewTemp(double temp) {
		
	}
	
	@Override
	public void setNewTemp(ICube[][][] block, int x, int y, int z) {
		
	}
	
	@Override
	public void updateTemp() {
			
	}
}
