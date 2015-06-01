package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import cube.Block;

public class NetworkHandler {

	private int port;
	private Controller controller;
	private Block block;
	
	private SocketInformation serverSocket;
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	
	private CyclicBarrier barrier;
	private MasterHandler masterHandler;
//	private InformMaster informMaster;
	private InformLowerXNeighbor informLower;
//	private InformHigherXNeighbor informHigher;
	
	public NetworkHandler(int port, Controller controller) {
		this.port = port;
		this.controller = controller;
		barrier = new CyclicBarrier(2);
	}
	
	public void waitForInitInformation(Thread controllerThread) {
		masterHandler = new MasterHandler(controllerThread, controller, port, barrier);
		masterHandler.start();
//		ServerSocket clientConnect = null;
//		Socket client = null;
//		DataInputStream dis = null;
//		DataOutputStream dos = null;
//		try {
//			clientConnect = new ServerSocket(port);
//			client = clientConnect.accept(); // blocks
//	        dis = new DataInputStream(
//	                new BufferedInputStream(client.getInputStream()));
//	        receiveServerSocket(dis);
//	        receiveLowerXSocket(dis);
//	        receiveHigherXSocket(dis);
//	        receiveNodeDimension(dis);
//	        
//	        createInformThreads();
//	        
//	        dos = new DataOutputStream( 
//					new BufferedOutputStream(client.getOutputStream()));
//	        dos.writeUTF("ready init");
//	        dos.flush();
//
//	    }
//	    catch ( Exception e ) {
//	        e.printStackTrace();
//	    }
//		finally {
//			try {
//				if (dis != null) {
//					dis.close();
//				}
//				if (client != null) {
//					client.close();
//				}
//				if (clientConnect != null) {
//					clientConnect.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
//	private void receiveServerSocket(DataInputStream dis) throws IOException {
//		String serverSocketIP = dis.readUTF();
//        int serverSocketPort = dis.readInt();
//        serverSocket = new SocketInformation(serverSocketIP, serverSocketPort);
//        System.out.println(serverSocketIP + ":" + serverSocketPort);
//	}
//	
//	private void receiveLowerXSocket(DataInputStream dis) throws IOException {
//		String lowerXSocketIp = dis.readUTF();
//        int lowerXSocketPort = dis.readInt();
//        if ( ! lowerXSocketIp.equals("null")) {
//        	lowerXSocket = new SocketInformation(lowerXSocketIp, lowerXSocketPort);
//        }
//        else {
//        	lowerXSocket = null;
//        }
//        System.out.println(lowerXSocketIp + ":" + lowerXSocketPort);
//	}
//	
//	private void receiveHigherXSocket(DataInputStream dis) throws IOException {
//		String higherXSocketIp = dis.readUTF();
//        int higherXSocketPort = dis.readInt();
//        if ( ! higherXSocketIp.equals("null")) {
//        	higherXSocket = new SocketInformation(higherXSocketIp, higherXSocketPort);
//        }
//        else {
//        	higherXSocket = null;
//        }
//        System.out.println(higherXSocketIp + ":" + higherXSocketPort);
//	}
//	
//	private void receiveNodeDimension(DataInputStream dis) throws IOException {
//		int startX = dis.readInt();
//        int endX = dis.readInt();
//        int maxX = dis.readInt();
//        int maxY = dis.readInt();
//        int maxZ = dis.readInt();
//        
//        NodeDimension dimension = new NodeDimension(startX, endX, maxX, maxY, maxZ);
//        boolean hasLowerX = false;
//        if (lowerXSocket != null) {
//        	hasLowerX = true;
//        }
//        
//        boolean hasHigherX = false;
//        if (higherXSocket != null) {
//        	hasHigherX = true;
//        }
//        block = controller.createBlock(dimension, hasLowerX, hasHigherX);
//        
//        System.out.println(startX + " " + endX + " " +  maxY + " " + maxZ);
//	}
//	
//	private void createInformThreads() {
//		barrier = new CyclicBarrier(2);
//		informMaster = new InformMaster(block, serverSocket, barrier);
//		informMaster.start();
//	}
	
//	public void waitForStartCommand() {
//		ServerSocket clientConnect = null;
//		Socket client = null;
//		DataInputStream dis = null;
//		try {
//			clientConnect = new ServerSocket(port);
//			client = clientConnect.accept(); // blocks
//	        dis = new DataInputStream(
//	                new BufferedInputStream(client.getInputStream()));
//	        String command = dis.readUTF();
//	        System.out.println(command);
//	    }
//	    catch ( Exception e ) {
//	        e.printStackTrace();
//	    }
//		finally {
//			try {
//				if (dis != null) {
//					dis.close();
//				}
//				if (client != null) {
//					client.close();
//				}
//				if (clientConnect != null) {
//					clientConnect.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		synchronized (controller) {
//			controller.notify();			
//		}
//	}
	
	public void endIteration() {
		try {
			barrier.await();
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
//		List<Thread> threads = new ArrayList<Thread>();
		// thread sent lowerX
//		if (lowerXSocket != null) {
//			InformLowerXNeighbor informLower = new InformLowerXNeighbor(block, lowerXSocket);
//			threads.add(informLower);
//			informLower.start();
//		}
		
		// thread sent higherX
//		if (higherXSocket != null) {
//			InformHigherXNeighbor informHeigher = new InformHigherXNeighbor(block, higherXSocket);
//			threads.add(informHeigher);
//			informHeigher.start();
//		}
		
		// thread inform master
//		informMaster = new InformMaster(block, serverSocket);
//		threads.add(informMaster);
//		informMaster.start();
//		
//		for (Thread thread : threads) {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
}
