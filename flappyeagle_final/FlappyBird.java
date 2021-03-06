import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.util.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.net.*;
import java.io.*;

public class FlappyBird extends JPanel implements Runnable, Constants, ActionListener, KeyListener, MouseListener {
    
    public static final int FPS = 200, WIDTH = 900, HEIGHT = 480;
    
    private Bird bird;
    JFrame frame;
    private JPanel panel;
    private ArrayList<Rectangle> rects;
    private int time, scroll;
    private Timer t;
    public static final Color bg = new Color(255, 255, 255);
    private Font scoreFont;
    
    private boolean paused;
    public int end = 0;

    JTextField chatBox = new JTextField();
    JTextArea chatMessage = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(chatMessage);

    JPanel chatPanel = new JPanel();
    JPanel mainPanel = new JPanel();
    JFrame sudoFrame = new JFrame("Flappy Eagle MENU");
    JPanel extraPanel = new JPanel();
    JPanel btnPanel = new JPanel();
    JPanel picPanel = new JPanel();
    JButton startBtn = new JButton("Start");
    JButton aboutBtn = new JButton("About");
    JButton howBtn = new JButton("How To Play");
    JLabel label = new JLabel("Enter total number of players: ");
    Image birdImage;
    public static int checker = 0;
    
    //added

    Thread t1 = new Thread(this);
    static String name="Wency";
    String pname;
    String server="localhost";
    boolean connected=false;
    DatagramSocket socket = new DatagramSocket();
    String serverData;
    static float x, y;
    static boolean otherPlayers = false;

    //Container c = sudoFrame.getContentPane();

    BufferedImage offscreen;

    private static Socket clientSocket = null;
    private static PrintStream os = null;
    private static DataInputStream is = null;

    private static BufferedReader inputLine = null;
    private static boolean closed = false;

    public FlappyBird(String server, String name) throws Exception{
        super();       
        this.server=server;
        this.name=name;
        socket.setSoTimeout(5000);
        menuInit();
    }

    public void menuInit(){
        sudoFrame.setPreferredSize(new Dimension(900,480));
        JLayeredPane menuPane = sudoFrame.getLayeredPane();
        startBtn.setBackground(Color.decode("#28B463"));
        startBtn.setPreferredSize(new Dimension(70,50));
        howBtn.setBackground(Color.decode("#2E86C1"));
        howBtn.setPreferredSize(new Dimension(150,50));
        aboutBtn.setBackground(Color.decode("#F4D03F"));
        aboutBtn.setPreferredSize(new Dimension(100,50));

        startBtn.addActionListener(this);
        howBtn.addActionListener(this);
        aboutBtn.addActionListener(this);

        btnPanel.setOpaque(false);
        btnPanel.setLocation(200,320);
        btnPanel.add(startBtn);
        btnPanel.add(howBtn);
        btnPanel.add(aboutBtn);

        ImageIcon menuBG = new ImageIcon("menu.png", "Menu Background");
        Image stlogo = menuBG.getImage();
        Image starlogo = stlogo.getScaledInstance(900,480, java.awt.Image.SCALE_SMOOTH);
        ImageIcon finalLogo = new ImageIcon(starlogo);
        JLabel menulogo = new JLabel(finalLogo);

        extraPanel.setSize(new Dimension(900,480));
        btnPanel.setSize(new Dimension(500,350));
        extraPanel.setLayout(new BorderLayout());
        btnPanel.setBackground(new Color(0,0,0,0));
        extraPanel.add(menulogo);
        menuPane.add(extraPanel, Integer.valueOf(1));
        menuPane.add(btnPanel, Integer.valueOf(2));
        
        sudoFrame.pack();
        sudoFrame.setVisible(true);
        sudoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void send(String msg){
        try{
            byte[] buf = msg.getBytes();
            InetAddress address = InetAddress.getByName(server);
            System.out.println("Server1234567890" + server);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
            socket.send(packet);
        }catch(Exception e){}
        
    }

    public void run(){
        while(true){
            System.out.println("inside rrrruuuunnn");
            try{
                Thread.sleep(1);
            }catch(Exception ioe){}
            
            System.out.println("checkkkk");

            //Get the data from players
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            System.out.println("pak");
            try{
                 System.out.println("pak1");
                 socket.receive(packet);
                 System.out.println("pak2");
            }catch(Exception ioe){/*lazy exception handling :)*/System.out.println("err");}

            serverData=new String(buf);
            serverData=serverData.trim();

            System.out.println("check1");
            
            if(serverData.contains("dead")){
				System.out.println("woohooOOOoOoOo!");				
				end++;
			}

            //Study the following kids. 
            if (!connected && serverData.startsWith("CONNECTED")){
                System.out.println("check2");
                connected=true;
                System.out.println("Connected.");
            }else if (!connected){
                System.out.println("check3");
                System.out.println("Connecting..");             
                send("CONNECT "+name);
            }else if (connected){
                System.out.println("check4");
                //offscreen.getGraphics().clearRect(0, 0, WIDTH, HEIGHT);
                if (serverData.startsWith("PLAYER")){
                    String[] playersInfo = serverData.split(":");
                    for (int i=0;i<playersInfo.length;i++){
                        String[] playerInfo = playersInfo[i].split(" ");
                        String pname =playerInfo[1];

                        x = Float.parseFloat(playerInfo[2]);
                        y = Float.parseFloat(playerInfo[3]);

                        //offscreen.getGraphics().drawImage(birdImage, (int)x, (int)y, 20, 20, null);
                        //offscreen.getGraphics().drawString(pname, (int)x,(int)y+30);
                        otherPlayers = true;
                        
                    }
                    System.out.println("qwertyuiopp");
                    frame.repaint();
                }           
            }
            /*String responseLine;
            try
            {
                while((responseLine = is.readLine()) != null)
                {
                    System.out.println(responseLine);
                    if(responseLine.indexOf("BYE") != - 1)
                    {
                        break;
                    }
                }
                closed = true;
            } catch (IOException e) {
                System.err.println(e);
            }    */       
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        System.out.println("YAHELLO!SDSADSADSAD");
         try
                        {
                            birdImage = ImageIO.read(new File("sticker,375x360.u2.png"));
                        } catch(Exception e) {}
        g.drawImage(birdImage,(int)x,(int)y,20,20,null);
    }


    public void go() {
        frame = new JFrame("Flappy Eagle");
        bird = new Bird();
        rects = new ArrayList<Rectangle>();
        panel = new GamePanel(this, bird, rects);
        frame.add(panel, BorderLayout.CENTER);
    //    mainPanel.add(panel);
        frame.setPreferredSize(new Dimension(900,480));
    //    JLayeredPane mainPane = frame.getLayeredPane();

        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        chatMessage.setSize(new Dimension(350,350));
        chatMessage.setBackground(Color.decode("#5D6D7E"));

        chatBox.setSize(new Dimension(300,250));
        chatBox.setBackground(Color.decode("#EBEDEF"));
        chatBox.setFocusable(true);
        chatBox.addKeyListener(this);

        mainPanel.setSize(new Dimension(900,480));
        chatPanel.setSize(new Dimension(500,400));
        chatPanel.setLayout(new GridLayout(0,2));
    //    chatPanel.setOpaque(false);
    //    chatPanel.setLocation(100,700);
        chatPanel.add(chatMessage);
        chatPanel.add(chatBox);
        frame.add(chatPanel, BorderLayout.SOUTH);
    //    mainPane.add(mainPanel, Integer.valueOf(1));
    //    mainPane.add(chatPanel, Integer.valueOf(2));

        //frame.setSize(WIDTH, HEIGHT);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //offscreen=(BufferedImage)this.createImage(WIDTH, HEIGHT);
        frame.addKeyListener(this);
        frame.addMouseListener(this);

        /*try
        {
            clientSocket = new Socket(server, PORT);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        } catch(IOException ioe) {
            System.err.println("Couldn't get I/O for the connection to the host "
          + server);  
        }

        if(clientSocket != null && os != null && is != null)
        {
            try
            {
                new Thread(this).start();
                while(!closed)
                {
                    os.println(inputLine.readLine().trim());
                }
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }*/
        
        paused = true;
        
        
        //t = new Timer(1000/FPS, this);
        //t = new Timer(5000/FPS, this);
        t = new Timer(3000/FPS, this);
        t.start();

        t1.start();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startBtn){
            sudoFrame.setVisible(false);
            go();
            //t1.start();
        }
        else if(e.getSource()==howBtn){
            JDialog howDialog = new JDialog();
            howDialog.setTitle("Flappy Eagle HOW-TO-PLAY");
            howDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            howDialog.setSize(900,480);
            ImageIcon howToPlayBG = new ImageIcon("howtoplay.png", "How To Play Background");
            Image stlogo = howToPlayBG.getImage();
            Image starlogo = stlogo.getScaledInstance(900,480, java.awt.Image.SCALE_SMOOTH);
            ImageIcon finalLogo = new ImageIcon(starlogo);
            JLabel howlogo = new JLabel(finalLogo);
            howDialog.add(howlogo);
            howDialog.setVisible(true);
        }   
        else if(e.getSource()==aboutBtn){
            JDialog aboutDialog = new JDialog();
            aboutDialog.setTitle("Flappy Eagle ABOUT THE AUTHORS");
            aboutDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            aboutDialog.setSize(900,480);
            ImageIcon aboutAuthorsBG = new ImageIcon("abouttheauthor.png", "About the Authors");
            Image stlogo = aboutAuthorsBG.getImage();
            Image starlogo = stlogo.getScaledInstance(900,480, java.awt.Image.SCALE_SMOOTH);
            ImageIcon finalLogo = new ImageIcon(starlogo);
            JLabel aboutlogo = new JLabel(finalLogo);
            aboutDialog.add(aboutlogo);
            aboutDialog.setVisible(true);
            
        }
        else{
            //t1.start();
            play();
        }
    }
    
    public void play(){
        panel.repaint();
            if(!paused) {
                bird.physics();
                send("PLAYER "+name+" "+bird.x+" "+bird.y);
                System.out.println("REPAINTING!");
                if(scroll % 90 == 0) {
                    Rectangle r = new Rectangle(WIDTH, 0, GamePanel.PIPE_W, (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT));
                    int h2 = (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT);
                    Rectangle r2 = new Rectangle(WIDTH, HEIGHT - h2, GamePanel.PIPE_W, h2);
                    rects.add(r);
                    rects.add(r2);
                }
                ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
                boolean game = true;
                for(Rectangle r : rects) {
                    r.x-=3;
                    if(r.x + r.width <= 0) {
                        toRemove.add(r);
                    }
                    if(r.contains(bird.x, bird.y)) {
						send("dead");

                        JOptionPane.showMessageDialog(frame, "Game over!\n"+"Your score is: "+time+".\nWait for other players...");

						while(end < 2){
						}
                        game = false;
                    }
                    
                }
                rects.removeAll(toRemove);
                time++;
                scroll++;

                if(bird.y > HEIGHT || bird.y+bird.RAD < 0) {
                    game = false;
                }

                if(!game) {
                    rects.clear();
                    bird.reset();
                    time = 0;
                    scroll = 0;
                    paused = true;
                }
            }
    }
    
    public int getScore() {
        return time;
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            bird.jump();
            send("PLAYER "+name+" "+bird.x+" "+bird.y);
            System.out.print("client sent\n");
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            if(checker == 0){
                paused = false;
            }
            else{
                paused = true;   
            }

            if(checker == 0){
                checker = 1;
            }
            else {
                checker = 0;
            }

        }
        else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
            System.out.println("For chatting purposes.");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==10) {
                    String newline = "\n";
                    String message = chatBox.getText();
                    chatMessage.append(message+newline);
                    chatMessage.setCaretPosition(chatMessage.getDocument().getLength());
                    chatBox.setText(null);
                }
    }

    public void keyTyped(KeyEvent e) {}
    
    public boolean paused() {
        return paused;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        bird.jump();
        send("PLAYER "+name+" "+bird.x+" "+bird.y);
        System.out.print("client sent");
    }

    @Override
    public void mousePressed(MouseEvent e){}

    @Override
    public void mouseReleased(MouseEvent e){}

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}

    public static void main(String[] args) throws Exception{
        if (args.length != 2){
            System.out.println("Usage: java -jar circlewars-client <server> <player name>");
            System.exit(1);
        }
        FlappyBird flappyEagle = new FlappyBird(args[0],args[1]);
    }
    
}
