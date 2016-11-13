import java.awt.*;
import java.io.*;
import java.awt.event.MouseAdapter;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Game extends JPanel
{
	static int height = 800;
	static int width = 600;
	static int score = 0;
	int scrollScreen = 0;
	static boolean dead = false;
	static String gameOverMessage = "";
	Bird bird = new Bird();
	Pipe pipe = new Pipe(width);
	Pipe pipe2 = new Pipe(width + (width / 2));

	BufferedImage img = null;
	{
		try
		{
			img = ImageIO.read(new File("Images/background.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public Game()
	{
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent arg0)
			{
				bird.jump();
			}
		});
	}

	@SuppressWarnings("static-access")
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(img, scrollScreen, 0, null);
		g.drawImage(img, scrollScreen + 1800, 0, null);

		pipe.paint(g);
		pipe2.paint(g);
		bird.paint(g);

		g.setFont(new Font("comicsans", Font.BOLD, 40));
		g.drawString("" + score, width / 2 - 20, 700);
		g.drawString(gameOverMessage, 200, 200);
	}

	@SuppressWarnings("static-access")
	public void move()
	{
		pipe.move();
		pipe2.move();
		bird.move();

		scrollScreen += Pipe.speed;

		if(scrollScreen == -1800) scrollScreen = 0;
		if(dead)
		{
			pipe.x = 600;
			pipe2.x = 600 + (width / 2);
			dead = false;
		}

		if((pipe.x == bird.x) || (pipe2.x == bird.x))
		{
			score();
		}
	}

	public static void score()
	{
		score += 1;
	}
}