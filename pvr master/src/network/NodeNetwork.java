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

import main.Controller;
import main.Main;
import ui.MainPane;

public class NodeNetwork extends Thread {

	private SocketInformation nodeSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private NodeDimension dimension;
	private CyclicBarrier barrier;
	private MainPane pane; // may put in controller and update from there
	
	private Socket sender;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private int counter;
	
	public NodeNetwork(SocketInformation nodeSocket, SocketInformation lowerXSocket, SocketInformation higherXSocket, 
			NodeDimension dimension, CyclicBarrier barrier, MainPane pane) {
		this.nodeSocket = nodeSocket;
		this.lowerXSocket = lowerXSocket;
		this.higherXSocket = higherXSocket;
		this.dimension = dimension;
		this.barrier = barrier;
		this.pane = pane;
	}
	
	@Override
	public void run() {
		try {
			sendData();
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
			closeResources();
		}
	}
	
	private void sendData() throws UnknownHostException, IOException, InterruptedException, BrokenBarrierException {
		sender = new Socket(nodeSocket.getIp(), nodeSocket.getPort());
		if (sender != null && sender.isConnected()) {
			sendInitData();
			System.out.println("init finished");
			barrier.await();
			System.out.println("send start");
			sendStart();
			System.out.println("started");
			receiveData();
        }
	}
	
	private void sendInitData() throws IOException {
		dos = new DataOutputStream(new BufferedOutputStream(sender.getOutputStream()));
		sendLowerXSocket(dos);
		sendHigherXSocket(dos);
		sendInitialValues(dos);
		sendNodeDimensions(dos);
		
		dis = new DataInputStream(new BufferedInputStream(sender.getInputStream()));
		System.out.println(dis.readUTF());
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
	
	private void sendInitialValues(DataOutputStream dos) throws IOException {		
		
	}
	
	private void sendNodeDimensions(DataOutputStream dos) throws IOException {
		dos.writeInt(dimension.getStartX());
		dos.writeInt(dimension.getEndX());
		dos.writeInt(dimension.getMaxX());
		dos.writeInt(dimension.getMaxY());
		dos.writeInt(dimension.getMaxZ());
		dos.flush();
		
		dos.writeFloat(Main.leftTemperature);
		dos.writeFloat(Main.rightTemperature);
		dos.writeFloat(Main.topTemperature);
		dos.writeFloat(Main.bottomTemperature);
		dos.writeFloat(Main.frontTemperature);
		dos.writeFloat(Main.backTemperature);
		dos.writeFloat(Main.innerTemperature);
		dos.writeFloat(Main.alpha);
		dos.flush();
		
		dos.writeUTF(Main.type.getType());
		dos.flush();
	}
	
	private void sendStart() throws IOException {
		dos.writeUTF("start");
		dos.flush();
	}
	
	private void receiveData() throws IOException, InterruptedException, BrokenBarrierException {
		int sizeX = dimension.getEndX() - dimension.getStartX() + 1; // TODO calculate for 1 worker
		int sizeY = dimension.getMaxY();
		System.out.println("Thread: " + nodeSocket.getPort() + " sizeX: " + sizeX + " sizeY: " + sizeY);
		double[][] nodeArea = new double[sizeX][sizeY];
		counter = 0;
		while (Controller.run) {
			System.out.println("Node: " + dimension.getStartX() + " " + counter);
			counter++;
			pane.update(dis, nodeArea, sizeX, sizeY, dimension.getStartX());
			barrier.await();
			if (Main.iterations - counter > 0) {
				dos.writeUTF("proceed");				
			}
			else {
				dos.writeUTF("finished");
				Controller.run = false;
			}
	        dos.flush();
		}
	}
	
	private void closeResources() {
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
