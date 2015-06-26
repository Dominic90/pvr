package util;

public class SocketInformation {

	private String ip;
	private int port;
	
	public SocketInformation(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public SocketInformation(String socket) {
		ip = socket.substring(0, socket.indexOf(":"));
		port = Integer.parseInt(socket.substring(socket.indexOf(":") + 1));
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
}
