package network;

import java.util.concurrent.CyclicBarrier;

import ui.MainPane;
import util.NodeDimension;
import util.SocketInformation;

public class Node {
	private SocketInformation nodeSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private NodeDimension dimension;
	private CyclicBarrier barrier;
	
	private NodeNetwork nodeNetwork;
	
	public Node(SocketInformation nodeSocket, SocketInformation lowerXSocket, 
			SocketInformation higherXSocket, NodeDimension dimension, CyclicBarrier barrier) {
		this.nodeSocket = nodeSocket;
		this.lowerXSocket = lowerXSocket;
		this.higherXSocket = higherXSocket;
		this.dimension = dimension;
		this.barrier = barrier;
	}
	
	public void initalizeNode(MainPane pane) {
		nodeNetwork = new NodeNetwork(nodeSocket, lowerXSocket, higherXSocket, dimension, barrier, pane);
		nodeNetwork.start();
	}
}
