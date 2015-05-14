package network;

public class SocketInformation {

	private String ip;
	private int port;
	
	public SocketInformation(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
}
