package cube;

public interface ICube {

	public float getCurrentTemp();
	public void setInitTemp(float temp);
	public void setNewTemp(float temp);
	public void setNewTemp(ICube[][][] block, int x, int y, int z);
	public void updateTemp();
}
