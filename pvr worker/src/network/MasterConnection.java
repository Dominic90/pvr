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
import main.EType;
import cube.Block;
import cube.NormalCube;

public class MasterConnection extends Thread {

	private NetworkHandler networkHandler;
	private Thread controllerThread;
	private Controller controller;
	private CyclicBarrier barrier;
	
	private ServerSocket clientConnect;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private SocketInformation lowerXSocket;
	private SocketInformation higherXSocket;
	private Block block;
	
	public MasterConnection(NetworkHandler networkHandler, Thread controllerThread, Controller controller, int port) {
		this.networkHandler = networkHandler;
		this.controllerThread = controllerThread;
		this.controller = controller;
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
			closeResources();
		}
	}
	
	private void receiveDataFromMaster() throws IOException {
		client = clientConnect.accept();
		dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        receiveLowerXSocket(dis);
        receiveHigherXSocket(dis);
        receiveInitialValues(dis);
        receiveNodeDimension(dis);
        
        int barrierCount = 2;
        if (higherXSocket != null) {
        	barrierCount++;
        }
        if (lowerXSocket != null) {
        	barrierCount++;
        }
        barrier = new CyclicBarrier(barrierCount);
        networkHandler.setBarrier(barrier);
    	networkHandler.startInformLower(lowerXSocket);
        
        dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        dos.writeUTF("ready init");
        dos.flush();
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
        }
        else {
        	higherXSocket = null;
        }
        System.out.println(higherXSocketIp + ":" + higherXSocketPort);
	}
	
	private void receiveInitialValues(DataInputStream dis) throws IOException {
		
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
        
        float leftTemperature = dis.readFloat();
		float rightTemperature = dis.readFloat();
		float topTemperature = dis.readFloat();
		float bottomTemperature = dis.readFloat();
		float frontTemperature = dis.readFloat();
		float backTemperature = dis.readFloat();
		float innerTemperature = dis.readFloat();
		float alpha = dis.readFloat();
		
		block.setInitValues(leftTemperature, rightTemperature, topTemperature, bottomTemperature, frontTemperature, 
				backTemperature, innerTemperature);
		NormalCube.alpha = alpha;
        
        String type = dis.readUTF();
        if (type.equals(EType.BORDER.getType())) {
        	block.setCalculationType(EType.BORDER);
        }
        else if (type.equals(EType.MIDDLE.getType())) {
        	block.setCalculationType(EType.MIDDLE);
        }
        else {
        	block.setCalculationType(EType.BORDER_SINUS);
        }
        
        System.out.println(startX + " " + endX + " " +  maxY + " " + maxZ);
	}
	
	private void waitForStart() throws IOException {
		String command = dis.readUTF();
        System.out.println(command);
        networkHandler.startInformHigher(higherXSocket);
        synchronized (controllerThread) {
        	controllerThread.notify();			
		}
	}
	
	private void updateMaster() throws InterruptedException, BrokenBarrierException, IOException {
		while(Controller.run) {
			System.out.println("master before barrier");
			barrier.await();
			System.out.println("Start update master");
			block.sendToMaster(dos);
			String answer = dis.readUTF();
			if (answer.equals("proceed")) {
				System.out.println(answer);					
			}
			else {
				System.out.println("Stop: " + answer);
				Controller.run = false;
			}
			barrier.await();
		}
	}
	
	private void closeResources() {
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
