import java.io.*;
import java.net.*;

public class ChatServer {

  private static ServerSocket serverSocket = null;
  private static Socket clientSocket = null;
  private static int maxUsers = 0;
  private static clientThread[] threads;
  private static int portNumber;
  private static String host;


  public static void main(String args[]) {

    try
      {
        host = args[0];
        portNumber = Integer.valueOf(args[1]).intValue();
        maxUsers = Integer.valueOf(args[2]).intValue();
      }catch(ArrayIndexOutOfBoundsException e)
      {
         System.out.println("Usage: java ChatServer <server ip> <port no.> <max no. of users>");
      }

      threads = new clientThread[maxUsers];

    // opening server socket using the port number
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }

    // client threads
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        int i = 0;
        for (i = 0; i < maxUsers; i++) {
          if (threads[i] == null) {
            (threads[i] = new clientThread(clientSocket, threads)).start();
            break;
          }
        }
        if (i == maxUsers) {
          PrintStream os = new PrintStream(clientSocket.getOutputStream());
          os.println("Sorry, server is full.");
          os.close();
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}

class clientThread extends Thread {

  private String clientName = null;
  private BufferedReader input = null;
  private PrintStream output = null;
  private Socket clientSocket = null;
  private final clientThread[] threads;
  private int maxUsers;

  public clientThread(Socket clientSocket, clientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    maxUsers = threads.length;
  }

  public void run() {
    int maxUsers = this.maxUsers;
    clientThread[] threads = this.threads;

    try {
      input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      output = new PrintStream(clientSocket.getOutputStream());
      String name;
      while (true) {
        output.println("Enter your name.");
        name = input.readLine().trim();
        break;
      }
      output.println("Welcome! To leave the chatroom type /quit.");
      synchronized (this) {
        for (int i = 0; i < maxUsers; i++) {
          if (threads[i] != null && threads[i] == this) {
            clientName = "@" + name;
            break;
          }
        }
        for (int i = 0; i < maxUsers; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].output.println("User " + name
                + " entered the chat room.");
          }
        }
      }
      /* Start the conversation. */
      while (true) {
        String line = input.readLine();
        if(line.startsWith("/quit")){
          break;
        }
        /* If the message is private sent it to the given client. */
        if (line.startsWith("@")) {
          String[] words = line.split("\\s", 2);
          if (words.length > 1 && words[1] != null) {
            words[1] = words[1].trim();
            if (!words[1].isEmpty()) {
              synchronized (this) {
                for (int i = 0; i < maxUsers; i++) {
                  if (threads[i] != null && threads[i] != this
                      && threads[i].clientName != null
                      && threads[i].clientName.equals(words[0])) {
                    threads[i].output.println("<" + name + "> " + words[1]);
                    this.output.println(">" + name + "> " + words[1]);
                    break;
                  }
                }
              }
            }
          }
        } else {
          //broadcast message to other users
          synchronized (this) {
            for (int i = 0; i < maxUsers; i++) {
              if (threads[i] != null && threads[i].clientName != null) {
                threads[i].output.println("<" + name + "> " + line);
              }
            }
          }
        }
      }
      synchronized (this) {
        for (int i = 0; i < maxUsers; i++) {
          if (threads[i] != null && threads[i] != this
              && threads[i].clientName != null) {
            threads[i].output.println("*** The user " + name
                + " is leaving the chat room !!! ***");
          }
        }
      }
      output.println("*** Bye " + name + " ***");

      synchronized (this) {
        for (int i = 0; i < maxUsers; i++) {
          if (threads[i] == this) {
            threads[i] = null;
          }
        }
      }

      // close everything
      input.close();
      output.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}