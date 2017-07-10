package client.model;

public class SystemConfig {
	private String serverAddress;
	private int serverPort;
	private int pollInterval;
	private int syncInterval;
	
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getPollInterval() {
		return pollInterval;
	}
	public void setPollInterval(int pollInterval) {
		this.pollInterval = pollInterval;
	}
	public int getSyncInterval() {
		return syncInterval;
	}
	public void setSyncInterval(int syncInterval) {
		this.syncInterval = syncInterval;
	}
}
