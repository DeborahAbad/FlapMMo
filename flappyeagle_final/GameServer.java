import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.net.*;
import java.io.*;

public class GameServer implements Runnable, Constants{
	String playerData;
	int playerCount=0;

	DatagramSocket serverSocket = null;

	private static ServerSocket sServerSocket = null;
	private static Socket clientSocket = null;

	private static final int maxClient = 5;
	private static final clientThread[] threads = new clientThread[maxClient];

	GameState game;
	String pname;
	int numPlayers;
	String serverData;

	int gameStage = WAITING_FOR_PLAYERS;

	Thread t = new Thread(this);

	public GameServer(int numPlayers){
		this.numPlayers = numPlayers;
		try{
			serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(1000);
		} catch (IOException e){
			System.err.println("Could not listen on port: "+PORT);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		game = new GameState();
		
		System.out.println("Game 1234567 created...");
		
		//Start the game thread
		t.start();
		
		/*try
		{
			sServerSocket = new ServerSocket(PORT);
		} catch(IOException e) {
			System.out.println(e);
		}

		while(true)
		{
			try
			{
				clientSocket = sServerSocket.accept();
				int i = 0;
				for(i = 0; i < maxClient; i++)
				{
					if(threads[i] == null) 
					{
						(threads[i] = new clientThread(clientSocket, threads, pname)).start();
						break;
					}
				}
				if(i == maxClient)
				{
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					os.println("Server Busy.");
					os.close();
					clientSocket.close();
				}
			} catch(IOException e) {
				System.out.println(e);
			}
		}*/
	}

	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}

	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
			System.out.println("sent");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public void run(){
		while(true){
			// Get the data from players

			byte[] buf = new byte[256];
			byte[] buff = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			//System.out.println(packet);
			try{
     			serverSocket.receive(packet);
				System.out.println("received: " + packet);
			}catch(Exception ioe){}

			buff = packet.getData();
			serverData= new String(buff, 0, packet.getLength()); 
			if(serverData.contains("dead")){
				//System.out.println("INCREASE ENDDDDDDDDDDDDDDDDDDDDD\n");
				broadcast("dead");
			}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			System.out.println(playerData);
			//remove excess bytes
			playerData = playerData.trim();
			//if (!playerData.equals("")){
			//	System.out.println("Player Data:"+playerData);
			//}
		
			// process
			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
				  		//System.out.println("check waiting for players");
						if (playerData.startsWith("CONNECT")){
				  		//System.out.println("Nummmm "+numPlayers);
						//if(playerCount < numPlayers){
							System.out.println("inside case 1");
							String tokens[] = playerData.split(" ");
							NetPlayer player1=new NetPlayer(tokens[1],packet.getAddress(),packet.getPort());
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player1);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
							System.out.println(playerCount);
							System.out.println(tokens[0]);
							System.out.println(tokens[1]);
						} 
						//else if(playerCount == numPlayers){
						//	gameStage = GAME_START;
						//}
					  break;	
				  case GAME_START:
				  System.out.println("inside case 2");
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_GAME;
					  break;
				  case IN_GAME:
					  //System.out.println("Game State: IN_PROGRESS");
					  System.out.println("inside case 3");
					  //Player data was received!
					   
					  if (playerData.startsWith("PLAYER")){
						  //Tokenize:
						  //The format: PLAYER <player name> <x> <y>
						  //String[] playerInfo = playerData.split(" ");
						  String[] playerInfo = playerData.split(" ");					  
						  String pname =playerInfo[1];
						  float x = Float.parseFloat(playerInfo[2].trim());
						  float y = Float.parseFloat(playerInfo[3].trim());
						  //Get the player from the game state
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);		
						  System.out.println("created player");			  
						  player.setX(x);
						  player.setY(y);
						  //Update the game state
						  game.update(pname, player);
						  //Send to all the updated game state
						  broadcast(game.toString());
					  }
					  break;
			}				  
		}
	}	
	
	
	public static void main(String args[]){
		if (args.length != 1){
			System.out.println("Usage: java -jar circlewars-server <number of players>");
			System.exit(1);
		}
		new GameServer(Integer.parseInt(args[0]));
	}
}

/*class clientThread extends Thread
{
	private String clientName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private int maxClient;
	String name;

	public clientThread(Socket clientSocket, clientThread[] threads, String pname)
	{
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.name = pname;
		maxClient = threads.length;
	}

	public void run()
	{
		int maxClient = this.maxClient;
		clientThread[] threads = this.threads;

		try
		{
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			String name;
			/*while(true)
			{
				os.println("Enter your name: ");
				name = is.readLine().trim();
				if(name.indexOf('@') == -1) 
				{
					break;
				}
				else
				{
					os.println("The name should not contain '@' character.");
				}
			}

			name = FlappyBird.name;
			os.println("Welcome " + name + " to that chat room.\nTo leave enter /quit");

			synchronized(this)
			{
				for(int i = 0; i < maxClient; i++)
				{
					if(threads[i] != null && threads[i] == this)
					{
						clientName = "@" + name;
						break;
					}
				}
				for(int i = 0; i < maxClient; i++)
				{
					if(threads[i] != null && threads[i] != this)
					{
						threads[i].os.println("User " + name + " has joined the room!");
					}
				}
			}

			while(true)
			{
				String line = is.readLine();
				if(line.startsWith("/quit")) break;
				if(line.startsWith("@"))
				{
					String[] words = line.split("\\s", 2);
					if(words.length > 1 && words[1] != null)
					{
						words[1] = words[1].trim();
						if(!words[1].isEmpty())
						{
							synchronized(this)
							{
								for(int i = 0; i < maxClient; i++)
								{
									if(threads[i] != null && threads[i] != this && threads[i].clientName != null && threads[i].clientName.equals(words[0]))
									{
										threads[i].os.println("<" + name + ">: " + words[1]);
										this.os.println(">" + name + ">: " + words[1]);
										break;
									}
								}
							}
						}
					}
				}
				else
				{
					synchronized(this)
					{
						for(int i = 0; i < maxClient; i++)
						{
							if(threads[i] != null && threads[i].clientName != null) threads[i].os.println("<" + name + ">: " + line);
						}
					}
				}
			}

			synchronized(this)
			{
				for(int i = 0; i < maxClient; i++)
				{
					if(threads[i] != null && threads[i] != this && threads[i].clientName != null)
					{
						threads[i].os.println("*** The user " + name
	                				 		+ " is leaving the chat room !!! ***");
					}
				}
			}
			os.println("--Bye" + name + "!--");

			synchronized(this)
			{
				for(int i = 0; i < maxClient; i++)
				{
					if(threads[i] == this)
					{
						threads[i] = null;
					}
				}
			}

			is.close();
			os.close();
			clientSocket.close();
		} catch (IOException e) {}
	}
}*/