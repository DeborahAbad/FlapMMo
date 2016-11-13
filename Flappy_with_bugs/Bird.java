import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bird
{
	static int diameter = 20;
	static int x = (Game.width / 2) - (diameter / 2);
	static int y = Game.height / 2;
	static int accel = 1;
	static int speed = 2;

	static BufferedImage img = null;
	{
		try
		{
			img = ImageIO.read(new File("Images/Bird1.png"));
			img.resize(50, 50);
		} catch (IOException e) {
			System.out.println(e);	
		}
	}

	public Bird()
	{

	}

	public void jump()
	{
		speed = -17;
	}

	public static void move()
	{
		if((y > 0) && (y < Game.height))
		{
			speed += accel;
			y += speed;
		}
		else
		{
			reset();
			Game.dead = true;
		}
	}

	public static void reset()
	{
		y = Game.height / 2;
		speed = 2;
		Game.score = 0;

		Game.gameOverMessage = "Try again!";

		Timer gameOverTimer = new Timer(3000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Game.gameOverMessage = "";
			}
		});

		gameOverTimer.start();
	}

	public static void paint(Graphics g)
	{
		g.drawImage(img, x, y, null);
	}

	public static Rectangle getBounds()
	{
		return new Rectangle(x, y, diameter, diameter);
	}
	
}