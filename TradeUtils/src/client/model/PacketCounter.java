package client.model;

public class PacketCounter {
	private int packetError = 0;
	
	private int packetSent = 0;
	private int packetReceived = 0;
	
	private int packetQaip = 0;
	private int packetQair = 0;
	
	private int packetQolp = 0;
	private int packetQolr = 0;
	
	private int packetQslp = 0;
	private int packetQslr = 0;

	private int packetUtp = 0;
	private int packetUtr = 0;
	
	private int packetCop = 0;
	private int packetCor = 0;
	
	private int packetPop = 0;
	private int packetPor = 0;
	
	private int packetSosp = 0;
	private int packetSosr = 0;
	
	private int packetGpp = 0;
	private int packetGpr = 0;

	private int packetSpp = 0;
	private int packetSpr = 0;
	
	private int packetDelay = 0;
	
	public void zero()
	{
		packetSent = 0;
		packetReceived = 0;
		
		packetQaip = 0;
		packetQair = 0;
		
		packetQolp = 0;
		packetQolr = 0;
		
		packetQslp = 0;
		packetQslr = 0;

		packetUtp = 0;
		packetUtr = 0;
		
		packetCop = 0;
		packetCor = 0;
		
		packetPop = 0;
		packetPor = 0;
		
		packetSosp = 0;
		packetSosr = 0;
		
		packetGpp = 0;
		packetGpr = 0;

		packetSpp = 0;
		packetSpr = 0;
	}
	
	public int getPacketSent() {
		return packetSent;
	}
	public int getPacketReceived() {
		return packetReceived;
	}
	public int getPacketQaip() {
		return packetQaip;
	}
	public int getPacketQair() {
		return packetQair;
	}
	public int getPacketQolp() {
		return packetQolp;
	}
	public int getPacketQolr() {
		return packetQolr;
	}
	public int getPacketQslp() {
		return packetQslp;
	}
	public int getPacketQslr() {
		return packetQslr;
	}
	public int getPacketUtp() {
		return packetUtp;
	}
	public int getPacketUtr() {
		return packetUtr;
	}
	public int getPacketCop() {
		return packetCop;
	}
	public int getPacketCor() {
		return packetCor;
	}
	public int getPacketPop() {
		return packetPop;
	}
	public int getPacketPor() {
		return packetPor;
	}
	public int getPacketSosp() {
		return packetSosp;
	}
	public int getPacketSosr() {
		return packetSosr;
	}
	public int getPacketGpp() {
		return packetGpp;
	}
	public int getPacketGpr() {
		return packetGpr;
	}
	public int getPacketSpp() {
		return packetSpp;
	}
	public int getPacketSpr() {
		return packetSpr;
	}
	public int getPacketError() {
		return packetError;
	}
	
	public void addPacketSent() {
		this.packetSent++;
	}
	public void addPacketReceived() {
		this.packetReceived++;
	}
	public void addPacketQaip() {
		this.packetQaip++;
	}
	public void addPacketQair() {
		this.packetQair++;
	}
	public void addPacketQolp() {
		this.packetQolp++;
	}
	public void addPacketQolr() {
		this.packetQolr++;
	}
	public void addPacketQslp() {
		this.packetQslp++;
	}
	public void addPacketQslr() {
		this.packetQslr++;
	}
	public void addPacketUtp() {
		this.packetUtp++;
	}
	public void addPacketUtr() {
		this.packetUtr++;
	}
	public void addPacketCop() {
		this.packetCop++;
	}
	public void addPacketCor() {
		this.packetCor++;
	}
	public void addPacketPop() {
		this.packetPop++;
	}
	public void addPacketPor() {
		this.packetPor++;
	}
	public void addPacketSosp() {
		this.packetSosp++;
	}
	public void addPacketSosr() {
		this.packetSosr++;
	}
	public void addPacketGpp() {
		this.packetGpp++;
	}
	public void addPacketGpr() {
		this.packetGpr++;
	}
	public void addPacketSpp() {
		this.packetSpp++;
	}
	public void addPacketSpr() {
		this.packetSpr++;
	}
	public void addPacketError() {
		this.packetError++;
	}

	public int getPacketDelay() {
		return packetDelay;
	}
}
