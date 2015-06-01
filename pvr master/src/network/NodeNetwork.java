package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import ui.MainPane;

public class NodeNetwork extends Thread {

	private SocketInformation nodeSocket;
	private SocketInformation serverSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private NodeDimension dimension;
	private CyclicBarrier barrier;
	private MainPane pane;
	
	private Socket sender;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private int counter;
	
	public NodeNetwork(SocketInformation nodeSocket, SocketInformation serverSocket, SocketInformation lowerXSocket, 
			SocketInformation higherXSocket, NodeDimension dimension, CyclicBarrier barrier, MainPane pane) {
		this.nodeSocket = nodeSocket;
		this.serverSocket = serverSocket;
		this.lowerXSocket = lowerXSocket;
		this.higherXSocket = higherXSocket;
		this.dimension = dimension;
		this.barrier = barrier;
		this.pane = pane;
	}
	
	@Override
	public void run() {
		try {
			sender = new Socket(nodeSocket.getIp(), nodeSocket.getPort());
			if ( sender != null && sender.isConnected() ) {
				sendInitData();
				System.out.println("init finished");
				barrier.await();
				System.out.println("send start");
				setStart();
				System.out.println("started");
				receiveData();
            }
        } catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (dis != null) {
					dis.close();
				}
				if (sender != null) {
					sender.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendServerSocket(DataOutputStream dos) throws IOException {
		dos.writeUTF(serverSocket.getIp());
		dos.writeInt(serverSocket.getPort());
		dos.flush();
	}
	
	private void sendLowerXSocket(DataOutputStream dos) throws IOException {
		if (lowerXSocket != null) {
			dos.writeUTF(lowerXSocket.getIp());
			dos.writeInt(lowerXSocket.getPort());					
		}
		else {
			dos.writeUTF("null");
			dos.writeInt(0);
		}
		dos.flush();
	}
	
	private void sendHigherXSocket(DataOutputStream dos) throws IOException {
		if (higherXSocket != null) {
			dos.writeUTF(higherXSocket.getIp());
			dos.writeInt(higherXSocket.getPort());				
		}
		else {
			dos.writeUTF("null");
			dos.writeInt(0);
		}
		dos.flush();
	}
	
	private void sendNodeDimensions(DataOutputStream dos) throws IOException {
		dos.writeInt(dimension.getStartX());
		dos.writeInt(dimension.getEndX());
		dos.writeInt(dimension.getMaxX());
		dos.writeInt(dimension.getMaxY());
		dos.writeInt(dimension.getMaxZ());
		dos.flush();
	}
	
	private void sendInitData() throws IOException {
		dos = new DataOutputStream(new BufferedOutputStream(sender.getOutputStream()));
		sendServerSocket(dos);
		sendLowerXSocket(dos);
		sendHigherXSocket(dos);
		sendNodeDimensions(dos);
		
		dis = new DataInputStream(new BufferedInputStream(sender.getInputStream()));
		System.out.println(dis.readUTF());
	}
	
	private void setStart() throws IOException {
		dos.writeUTF("start");
		dos.flush();
	}
	
	private void receiveData() throws IOException, InterruptedException, BrokenBarrierException {
		int sizeX = dimension.getEndX() - dimension.getStartX() + 1; //TODO +1
		int sizeY = dimension.getMaxY();
		double[][] nodeArea = new double[sizeX][sizeY];
		counter = 0;
		while (true) {
			System.out.println("Node: " + dimension.getStartX() + " " + counter);
			counter++;
			for (int x = 0; x < sizeX; x++) {
				for (int y = 0; y < sizeY; y++) {
					double d = dis.readFloat();
					nodeArea[x][y] = d;
				}
			}
			pane.update(nodeArea, dimension.getStartX());
			barrier.await();
			dos.writeUTF("proceed");
	        dos.flush();
		}
	}
}
