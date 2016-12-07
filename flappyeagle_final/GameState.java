import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameState{

	private Map players = new HashMap();

	public GameState(){}

	public void update(String name, NetPlayer player){
		players.put(name,player);
	}

	public String toString(){
		String retval="";
		for(Iterator iterator = players.keySet().iterator();iterator.hasNext();){
			String name=(String)iterator.next();
			NetPlayer player = (NetPlayer) players.get(name);
			retval+=player.toString()+":";
		}
		return retval;
	}

	public Map getPlayers(){
		return players;
	}
}