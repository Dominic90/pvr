package network;

import java.util.concurrent.CyclicBarrier;

import ui.MainPane;

public class Node {
	private SocketInformation nodeSocket;
	private SocketInformation serverSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private NodeDimension dimension;
	private CyclicBarrier barrier;
	
	private NodeNetwork nodeNetwork;
	
	public Node(SocketInformation nodeSocket, SocketInformation serverSocket, SocketInformation lowerXSocket, 
			SocketInformation higherXSocket, NodeDimension dimension, CyclicBarrier barrier) {
		this.nodeSocket = nodeSocket;
		this.serverSocket = serverSocket;
		this.lowerXSocket = lowerXSocket;
		this.higherXSocket = higherXSocket;
		this.dimension = dimension;
		this.barrier = barrier;
	}
	
	public void initalizeNode(MainPane pane) {
		nodeNetwork = new NodeNetwork(nodeSocket, serverSocket, lowerXSocket, higherXSocket, dimension, barrier, pane);
		nodeNetwork.start();
	}
}
