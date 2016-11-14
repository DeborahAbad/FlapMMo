import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.awt.Font;
import java.awt.Graphics;
import java.util.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener, MouseListener {
    
    public static final int FPS = 200, WIDTH = 900, HEIGHT = 480;
    
    private Bird bird;
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Rectangle> rects;
    private int time, scroll;
    private Timer t;
    
    private boolean paused;

    JFrame sudoFrame = new JFrame("Start?");
    JPanel extraPanel = new JPanel();
    JPanel btnPanel = new JPanel();
    JPanel picPanel = new JPanel();
    JButton startBtn = new JButton("Start");
    JButton aboutBtn = new JButton("About");
    JButton howBtn = new JButton("How To Play");
    JLabel label = new JLabel("Enter total number of players: ");
    public static int checker = 0;
    
    public FlappyBird(){
        extraPanel.setPreferredSize(new Dimension(900,480));
        extraPanel.setLayout(new BorderLayout());
        btnPanel.setBackground(new Color(0,0,0,0));

        startBtn.addActionListener(this);
        howBtn.addActionListener(this);
        aboutBtn.addActionListener(this);
        btnPanel.add(startBtn);
        btnPanel.add(howBtn);
        btnPanel.add(aboutBtn);

        extraPanel.add(picPanel, BorderLayout.NORTH);
        extraPanel.add(btnPanel, BorderLayout.CENTER);
        sudoFrame.getContentPane().add(extraPanel);
        sudoFrame.setVisible(true);
        sudoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sudoFrame.pack();
    }


    public void go() {
        frame = new JFrame("Flappy Eagle");
        bird = new Bird();
        rects = new ArrayList<Rectangle>();
        panel = new GamePanel(this, bird, rects);
        frame.add(panel);
        
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        
        paused = true;
        
        //t = new Timer(1000/FPS, this);
        //t = new Timer(5000/FPS, this);
        t = new Timer(3000/FPS, this);
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startBtn){
            sudoFrame.setVisible(false);
            go();
        }
        else if(e.getSource()==howBtn){
            System.out.println("insert how to play here");
        }   
        else if(e.getSource()==aboutBtn){
            System.out.println("insert about here");
        }
        else{
            panel.repaint();
            if(!paused) {
                bird.physics();
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
                        JOptionPane.showMessageDialog(frame, "Back to start!\n"+"Your previous score is: "+time+".");
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
    }
    
    public int getScore() {
        return time;
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            bird.jump();
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
    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
    
    public boolean paused() {
        return paused;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        bird.jump();
    }

    @Override
    public void mousePressed(MouseEvent e){}

    @Override
    public void mouseReleased(MouseEvent e){}

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}


    public static void main(String[] args) {
        FlappyBird flappyEagle = new FlappyBird();
    }
}
