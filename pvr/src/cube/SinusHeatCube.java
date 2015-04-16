package cube;

public class SinusHeatCube implements ICube {

	private static final int STEP_WIDHT = 64;
	private double temp = 0;
	private int iteration = 0;
	
	public SinusHeatCube() {
		
	}

	@Override
	public double getCurrentTemp() {
		return temp;
	}
	
	@Override
	public void setInitTemp(double temp) {
	}
	
	@Override
	public void setNewTemp(double temp) {
		
	}
	
	@Override
	public void setNewTemp(ICube[][][] block, int x, int y, int z) {
		iteration = iteration % STEP_WIDHT;
		iteration = iteration + 1;
		temp = Math.sin(Math.PI * ((double)iteration / STEP_WIDHT)) * 100;
	}
	
	@Override
	public void updateTemp() {
			
	}
}
