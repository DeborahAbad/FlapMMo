import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.imageio.*;
import java.io.*;
import java.io.IOException;

public class Menu extends JPanel
{
	private static final long serialVersionUID = 1L;
	int highscore;
	static BufferedImage img = null;
	{
		try
		{
			img = ImageIO.read(new File("Images/background.png"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	boolean startGame = false;

	public Menu()
	{
		setFocusable(true);
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				startGame = true;
			}
		});
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(img, 0, 0, null);
	}
}