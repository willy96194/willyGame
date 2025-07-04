package willyGame;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.Panel;

import javax.swing.JFrame;

public class snakeGame extends JFrame{
	private snakeGamePanel sgp;

	public snakeGame() {
		super("貪食蛇");
		
		
		sgp = new snakeGamePanel();
		setLayout(new BorderLayout());
		add(sgp);
		
		setSize(800,640);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
		new snakeGame();


	}

}
