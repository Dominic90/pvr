//package network;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.util.concurrent.CyclicBarrier;
//
//import cube.Block;
//
//public class InformMaster extends Thread {
//
//	private Block block;
//	private SocketInformation serverSocket;
//	private CyclicBarrier barrier;
//	
//	public InformMaster(Block block, SocketInformation serverSocket, CyclicBarrier barrier) {
//		this.block = block;
//		this.serverSocket = serverSocket;
//		this.barrier = barrier;
//	}
//	
//	@Override
//	public void run() {
//		Socket sender = null;
//		DataOutputStream dos = null;
//		DataInputStream dis = null;
//		while (true) {
//		try {
//				sender = new Socket(serverSocket.getIp(), serverSocket.getPort());
//				if ( sender != null && sender.isConnected() ) {
//					dos = new DataOutputStream( 
//							new BufferedOutputStream(sender.getOutputStream()));
//					barrier.await();
//					
//					block.sendToMaster(dos);
//					
//					dis = new DataInputStream(
//			                new BufferedInputStream(sender.getInputStream()));
//					String answer = dis.readUTF();
//					if (answer.equals("proceed")) {
//						System.out.println(answer);					
//					}
//					else {
//						// TODO: stop simulation
//					}
//					barrier.await();
//				}
//            }
//			catch ( Exception e ) {
//				e.printStackTrace();
//			}
//			finally {
//				try {
//					if (dos != null) {
//						dos.close();
//					}
//					if (dis != null) {
//						dis.close();
//					}
//					if (sender != null) {
//						sender.close();
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//        }
//	}
//}
