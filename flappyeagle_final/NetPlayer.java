import java.net.InetAddress;

public class NetPlayer {

	private InetAddress address;
	private int port;
	private String name;
	private float x,y;

	public NetPlayer(String name, InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.name = name;
	}

	public InetAddress getAddress(){
		return address;
	}

	public int getPort(){
		return port;
	}

	public String getName(){
		return name;
	}

	public void setX(float x){
		this.x=x;
	}
	
	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}
	
	public void setY(float y){
		this.y=y;		
	}


	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=x+" ";
		retval+=y;
		return retval;
	}	
}