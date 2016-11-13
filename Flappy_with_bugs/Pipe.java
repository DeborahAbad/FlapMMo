import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pipe
{
	Random randomNo = new Random();

	int x;
	int y = randomNo.nextInt(Game.height - 400) + 200;
	static int speed = -6;
	int width = 45;
	int height = Game.height - y;
	int gap = 200;

	static BufferedImage img = null;
	{
		try
		{
			img = ImageIO.read(new File("Images/Pipe1.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public Pipe(int i)
	{
		this.x = 1;
	}

	public void paint(Graphics g)
	{
		g.drawImage(img, x, y, null);
		g.drawImage(img, x, (-Game.height) + (y - gap), null);
	}

	public void move()
	{
		x += speed;

		Rectangle pipeBounds = new Rectangle(x, y, width, height);
		Rectangle pipeBoundsTop = new Rectangle(x, 0, width, Game.height - (height + gap));

		if((pipeBounds.intersects(Bird.getBounds())) || (pipeBoundsTop.intersects(Bird.getBounds())))
		{
			Bird.reset();
			died();
		}	

		if(x <= 0 - width)
		{
			x = Game.width;
			y = randomNo.nextInt(Game.height - 400) + 200;
			height = Game.height - y;
		}
	}

	public void died()
	{
		y = randomNo.nextInt(Game.height - 400) + 200;
		height = Game.height - y;
		Game.dead = true;
	}
}