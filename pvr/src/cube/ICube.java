package cube;

public interface ICube {

	public double getCurrentTemp();
	public void setInitTemp(double temp);
	public void setNewTemp(double temp);
	public void setNewTemp(ICube[][][] block, int x, int y, int z);
	public void updateTemp();
}
