import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Main
{
	static JFrame frame = new JFrame();
	
	public static void main(String[] args) throws InterruptedException
	{
		frame.setSize(Game.width, Game.height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		runnit();
	}

	public static void runnit() throws InterruptedException
	{
		final Menu menu = new Menu();
		final Game game = new Game();

		Timer animationTimer = new Timer(20, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				game.repaint();
				game.move();
			}
		});

		frame.add(menu);
		menu.setVisible(true);
		frame.revalidate();
		frame.repaint();

		while(menu.startGame == false)
		{
			Thread.sleep(10);
		}

		frame.remove(menu);
		frame.add(game);
		game.setVisible(true);
		frame.revalidate();

		animationTimer.start();
	}
}

