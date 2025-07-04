package willyGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;


public class snakeGamePanel extends JPanel implements KeyListener{
	private ArrayList<snakeLengh> snack;
	private int snackX = 400, snackY = 200;
	private int viewH;
	private int viewW;
	private int foodX, foodY;
	private final int FOOD_SIZE = 10; // 食物直徑
	private Random random = new Random();
	private int dx=4 ,dy;
	private Timer timer;
	
	public snakeGamePanel() {
		setBackground(Color.green);
		setFocusable(true);
		addKeyListener(this);
		
		timer = new Timer();
		timer.schedule(new RefreshView(),0 ,30);
		
		timer.schedule(new snackMoveTask(), 100, 30);
		
		timer.schedule(new newFood(), 100,1000000000);
	}
	private class RefreshView extends TimerTask{
		@Override
		public void run() {
			repaint();
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		viewW = getWidth(); viewH = getHeight();
		
		Graphics2D g2d = (Graphics2D)g;
		
		
		g2d.setColor(Color.blue);
		g2d.setStroke(new BasicStroke(10));
		
		

		
//		int headX = snackX;
//	    int headY = snackY;
		
		if (dx != 0) {
//		    g2d.drawLine(snackX, snackY, snackX + 10* (dx / Math.abs(dx))  , snackY);
			g2d.drawLine(snackX-  10* (dx / Math.abs(dx)), snackY, snackX   , snackY);
//			headX = snackX + 10 * (dx / Math.abs(dx));
		} else if (dy != 0) {
//		    g2d.drawLine(snackX, snackY, snackX, snackY + 10 * (dy / Math.abs(dy)));
			g2d.drawLine(snackX, snackY -  10* (dy / Math.abs(dy)), snackX   , snackY);
//			headY = snackY + 10 * (dy / Math.abs(dy));
		}
		g2d.setColor(Color.RED);
		g2d.fillOval(snackX-5 , snackY-5 , 10, 10);
		
		// 畫出食物
		g2d.setColor(Color.white);
		g2d.fillOval(foodX - FOOD_SIZE / 2, foodY - FOOD_SIZE / 2, FOOD_SIZE, FOOD_SIZE);

	}
	private class snackMoveTask extends TimerTask {
		@Override
		public void run() {

			snackX += dx;
			snackY += dy;
			if (snackX-5 <= 0 || snackY-5 <=0 || snackX+5 >= viewW || snackY+5 >= viewH) {
			    timer.cancel();
			    System.out.println("Gameover");
			}
			
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch (key) {
        case KeyEvent.VK_W:
            dy = -4;
            dx = 0;
            break;
        case KeyEvent.VK_A:
            dx = -4;
            dy = 0 ;
            break;
        case KeyEvent.VK_S:
            dy = 4;
            dx = 0;
            break;
        case KeyEvent.VK_D:
            dx = 4;
            dy = 0;
            break;
    }
	}
	public void keyReleased(KeyEvent e) {
	    // 可留空
	}

	@Override
	public void keyTyped(KeyEvent e) {
	    // 可留空
	}
	
	private class newFood extends TimerTask{
		@Override
		public void run() {
			generateFood();
		}
	}
	
	private void generateFood() {
	    int padding = 20; // 為了避免食物貼邊
	        foodX = padding + random.nextInt(viewW - padding * 2);
	        foodY = padding + random.nextInt(viewH - padding * 2);

	}

}


