package network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import main.Controller;
import cube.Block;

public class MasterHandler extends Thread {

	private Thread controllerThread;
	private Controller controller;
	private CyclicBarrier barrier;
	private InformLowerXNeighbor informLower;
	private InformHigherXNeighbor informHigher;
	
	private ServerSocket clientConnect;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private Block block;
	
	public MasterHandler(Thread controllerThread, Controller controller, int port, CyclicBarrier barrier, 
			InformLowerXNeighbor informLower, InformHigherXNeighbor informHigher) {
		this.controllerThread = controllerThread;
		this.controller = controller;
		this.barrier = barrier;
		this.informLower = informLower;
		this.informHigher = informHigher;
		try {
			System.out.println(port);
			clientConnect = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		setName("Master handler Thread");
		try {
			receiveDataFromMaster();
			System.out.println("Data received from master");
			waitForStart();
			updateMaster();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (dos != null) {
					dos.close();
				}
				if (client != null) {
					client.close();
				}
				if (clientConnect != null) {
					clientConnect.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void receiveDataFromMaster() throws IOException {
		client = clientConnect.accept();
		dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        receiveServerSocket(dis);
        receiveLowerXSocket(dis);
        receiveHigherXSocket(dis);
        receiveNodeDimension(dis);
        
        if (lowerXSocket != null) {
        	informLower.setBlock(block);
        	informLower.start();        	
        }
        
        dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        dos.writeUTF("ready init");
        dos.flush();
	}
	
	@Deprecated
	private void receiveServerSocket(DataInputStream dis) throws IOException {
		String serverSocketIP = dis.readUTF();
        int serverSocketPort = dis.readInt();
//        serverSocket = new SocketInformation(serverSocketIP, serverSocketPort);
        System.out.println(serverSocketIP + ":" + serverSocketPort);
	}
	
	private void receiveLowerXSocket(DataInputStream dis) throws IOException {
		String lowerXSocketIp = dis.readUTF();
        int lowerXSocketPort = dis.readInt();
        if ( ! lowerXSocketIp.equals("null")) {
        	lowerXSocket = new SocketInformation(lowerXSocketIp, lowerXSocketPort);
        }
        else {
        	lowerXSocket = null;
        }
        System.out.println(lowerXSocketIp + ":" + lowerXSocketPort);
	}
	
	private void receiveHigherXSocket(DataInputStream dis) throws IOException {
		String higherXSocketIp = dis.readUTF();
        int higherXSocketPort = dis.readInt();
        if ( ! higherXSocketIp.equals("null")) {
        	higherXSocket = new SocketInformation(higherXSocketIp, higherXSocketPort);
        	informHigher.setHigherNeighborSocket(higherXSocket);
        }
        else {
        	higherXSocket = null;
        }
        System.out.println(higherXSocketIp + ":" + higherXSocketPort);
	}
	
	private void receiveNodeDimension(DataInputStream dis) throws IOException {
		int startX = dis.readInt();
        int endX = dis.readInt();
        int maxX = dis.readInt();
        int maxY = dis.readInt();
        int maxZ = dis.readInt();
        
        NodeDimension dimension = new NodeDimension(startX, endX, maxX, maxY, maxZ);
        boolean hasLowerX = false;
        if (lowerXSocket != null) {
        	hasLowerX = true;
        }
        
        boolean hasHigherX = false;
        if (higherXSocket != null) {
        	hasHigherX = true;
        }
        block = controller.createBlock(dimension, hasLowerX, hasHigherX);
        
        System.out.println(startX + " " + endX + " " +  maxY + " " + maxZ);
	}
	
	private void waitForStart() throws IOException {
		String command = dis.readUTF();
        System.out.println(command);
        
        if (higherXSocket != null) {
        	informHigher.setBlock(block);
        	informHigher.start();        	
        }
        
        synchronized (controllerThread) {
        	controllerThread.notify();			
		}
	}
	
	private void updateMaster() throws InterruptedException, BrokenBarrierException, IOException {
		while(true) {
			System.out.println("master before barrier");
			barrier.await();
			System.out.println("Start update master");
			block.sendToMaster(dos);
			String answer = dis.readUTF();
			if (answer.equals("proceed")) {
				System.out.println(answer);					
			}
			else {
				// TODO: stop simulation
			}
			barrier.await();
		}
	}
}
