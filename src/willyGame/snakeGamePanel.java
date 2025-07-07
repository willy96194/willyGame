package willyGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class snakeGamePanel extends JPanel implements KeyListener{
	private ArrayList<snakeLengh> snack = new ArrayList<>();
	private ArrayList<Food> foods = new ArrayList<>();
	private int snackX = 400, snackY = 200;
	private int viewH;
	private int viewW;
	private int foodX, foodY;
	private final int FOOD_SIZE = 10; // 食物直徑
	private Random random = new Random();
	private int dx=4 ,dy;
	private Timer timer;
	private int growCount = 1;  // 吃到食物後要多長幾節
	private Direction currentDirection = Direction.RIGHT;

	
	public snakeGamePanel() {
		setBackground(Color.green);
		setFocusable(true);
		addKeyListener(this);
		
		timer = new Timer();
		timer.schedule(new RefreshView(),0 ,30);
		
		timer.schedule(new snackMoveTask(), 100, 20);
		
		timer.schedule(new TimerTask() {
		    @Override
		    public void run() {
		        int padding = 20;
		        foodX = padding + random.nextInt(viewW - padding * 2);
		        foodY = padding + random.nextInt(viewH - padding * 2);
		        Food f = new Food(foodX, foodY);
		        foods.add(f);

		        // 設定 5 秒後自動移除這個食物
		        timer.schedule(new TimerTask() {
		            @Override
		            public void run() {
		                foods.remove(f);
		            }
		        }, 10000); // 5000 毫秒 = 5 秒
		    }
		}, 1000, 3000); // 延遲 1 秒後開始，每 3 秒產生一次
		


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
		for (int i = 1; i < snack.size(); i++) {
		    snakeLengh p1 = snack.get(i - 1);
		    snakeLengh p2 = snack.get(i);
		    if (p1.x != p2.x || p1.y != p2.y) {
		        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
		    }
		}
		

		
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
		
		g2d.setColor(Color.white);
		for (Food f : foods) {
		    g2d.fillOval(f.x - FOOD_SIZE / 2, f.y - FOOD_SIZE / 2, FOOD_SIZE, FOOD_SIZE);
		}
	}
	private class snackMoveTask extends TimerTask {
		@Override
		public void run() {

			snackX += dx;
			snackY += dy;
			
			Iterator<Food> it = foods.iterator();
			
			while (it.hasNext()) {
		        Food food = it.next();
		        double distance = Math.hypot(snackX - food.x, snackY - food.y);
		        if (distance < 10) { // 可調整吃到的範圍
		            it.remove(); // 吃掉
		            growCount += 3;
		            System.out.println("吃到食物！");
		            break;

		        }
		    }
			snack.add(0, new snakeLengh(snackX, snackY));
			
			
			for (int i = 1; i < snack.size(); i++) {
			    snakeLengh segment = snack.get(i);
			    if (segment.x == snackX && segment.y == snackY) {
			        timer.cancel();
			        JOptionPane.showMessageDialog(null, "You Lose");
			        System.out.println("Gameover：咬到自己");
			        restartGame();
			        return;
			    }
			}
			
			if (growCount > 0) {
			    growCount--;  // 每次少一段（直到補完）
			} else if (snack.size() > 0) {
			    snack.remove(snack.size() - 1);  // 沒成長的話移除尾巴
			}
			
			
			if (snackX <= 0 || snackY <=0 || snackX+5 >= viewW || snackY+1>= viewH) {
			    timer.cancel();
			    JOptionPane.showMessageDialog(null, "You Lose");
			    System.out.println("Gameover");
			    restartGame();
			}
			

			
		}
	}
	
	enum Direction {
	    UP, DOWN, LEFT, RIGHT
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		switch (key) {
        case KeyEvent.VK_W:
            if (currentDirection != Direction.DOWN) {
                dy = -4;
                dx = 0;
                currentDirection = Direction.UP;
            }
            break;
        case KeyEvent.VK_A:
            if (currentDirection != Direction.RIGHT) {
                dx = -4;
                dy = 0;
                currentDirection = Direction.LEFT;
            }
            break;
        case KeyEvent.VK_S:
            if (currentDirection != Direction.UP) {
                dy = 4;
                dx = 0;
                currentDirection = Direction.DOWN;
            }
            break;
        case KeyEvent.VK_D:
            if (currentDirection != Direction.LEFT) {
                dx = 4;
                dy = 0;
                currentDirection = Direction.RIGHT;
            }
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
	
	private void restartGame() {
	    // 重設蛇的位置
	    snackX = 400;
	    snackY = 200;

	    // 重設方向
	    dx = 4;
	    dy = 0;

	    // 清空蛇身與食物
	    snack.clear();
	    foods.clear();

	    // 重加蛇頭
	    snack.add(new snakeLengh(snackX, snackY));

	    // 重新建立 Timer
	    timer.cancel();
	    timer = new Timer();
	    timer.schedule(new RefreshView(), 0, 30);
	    timer.schedule(new snackMoveTask(), 100, 20);
	    timer.schedule(new TimerTask() {
	        @Override
	        public void run() {
	            int padding = 20;
	            int foodX = padding + random.nextInt(viewW - padding * 2);
	            int foodY = padding + random.nextInt(viewH - padding * 2);
	            Food f = new Food(foodX, foodY);
	            foods.add(f);

	            timer.schedule(new TimerTask() {
	                @Override
	                public void run() {
	                    foods.remove(f);
	                }
	            }, 5000);
	        }
	    }, 1000, 3000);
	}


}


