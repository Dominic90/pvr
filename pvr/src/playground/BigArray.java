package playground;

public class BigArray {

	private float[][][] block;
	
	public static void main(String args[]) {
		BigArray ar = new BigArray();
	}
	
	public BigArray() {
		block = new float[500][500][500];
		for (int x = 0; x < block.length; x ++) {
			for (int y = 0; y < block[x].length; y++) {
				for (int z = 0; z < block[x][y].length; z++) {
					block[x][y][z] = (float)0.0;
				}
			}
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
