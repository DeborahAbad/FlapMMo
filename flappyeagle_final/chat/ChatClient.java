import java.io.*;
import java.net.*;

public class ChatClient implements Runnable {

  private static Socket clientSocket = null;
  private static PrintStream output = null;
  private static BufferedReader input = null;
  private static BufferedReader inputLine = null;
  private static boolean closed = false;
  private static String host;
  private static int portNumber;
  
  public static void main(String[] args) {
    try
      {
        host = args[0];
        portNumber = Integer.valueOf(args[1]).intValue();
      }catch(ArrayIndexOutOfBoundsException e)
      {
         System.out.println("Usage: java ChatClient <server ip> <port no.> ");
      }

    //socket, input, output, message
    try {
      clientSocket = new Socket(host, portNumber);
      inputLine = new BufferedReader(new InputStreamReader(System.in));
      output = new PrintStream(clientSocket.getOutputStream());
      input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch(UnknownHostException e) {
      System.err.println("Unknown host: " + host);
    } catch(IOException e){
      System.err.println(e);
    }

    if (clientSocket != null && output != null && input != null) {
      try {

        // thread from the server
        new Thread(new ChatClient()).start();
        while (!closed) {
          output.println(inputLine.readLine().trim());
        }
        // close everything
        output.close();
        input.close();
        clientSocket.close();
      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      }
    }
  }

  public void run() {
    String responseLine;
    try {
      while ((responseLine = input.readLine()) != null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("*** Bye") != -1)
          break;
      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }
}