package cube;

public interface Cube {

	public float getCurrentTemp();
	public void setInitTemp(float temp);
	public void setNewTemp(float temp);
	public void setNewTemp(Cube[][][] block, int x, int y, int z);
	public void updateTemp();
}
