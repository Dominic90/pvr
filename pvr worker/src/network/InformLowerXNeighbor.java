package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import cube.Block;

public class InformLowerXNeighbor extends InformNeighbor {

	public InformLowerXNeighbor(Block block, SocketInformation clientSocket) {
		super(block, clientSocket);
	}
	
	@Override
	protected void sendAndReceive(DataOutputStream dos, DataInputStream dis) throws Exception {
		
	}
}
