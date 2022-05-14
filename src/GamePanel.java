 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 750;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 25; // items size in the game - when changed - items(snake body parts and apple) changes
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 80; // biger the number the more slower the game is
	// creating 2 arrays 1.x and 2. y - holds all the coordinates for the body and the head
	final int x[] = new int[GAME_UNITS]; // holds all of the x coordinates, as well the snake head
	final int y[] = new int[GAME_UNITS]; // holds all of the y cordinates
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	 
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(new Color(89, 152, 26));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(running) {
		/* creating for loop to create grid - horizontal and vertical lines
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}*/
			g.setColor(new Color(236, 248, 127));
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0;i<bodyParts;i++) { // creating snake's head and body
				if(i == 0) {// head of the snake
					g.setColor(new Color(140, 182, 34));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else { // if not equal with 0 (body of the snake)
					g.setColor(new Color(61, 85, 12));
/* just for fun*/ //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fill3DRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, running);
						
				}		
			}
			// Score text while game is running + counting score, based on how many apple are eaten
			g.setColor(new Color(236, 248, 127));
			g.setFont(new Font("Ink Free",Font.BOLD,30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
		
	}
	public void newApple() { // purpose to generate the coordinates of a new apple when the method is called
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
//..........................................................................important.......................................................................................................
	public void move() { // for loop to iterate all the snakes body parts
		for(int i = bodyParts;i>0;i-- ) {
			//shifting bodyParts of the snake around by one spot
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
//......................................................................................................................................................................................................................
		
		// changes the direction where the snake is headed - direction(cahr value)
		// creating case of each of these directions R - right, L-left, U - up, D - down
		
		switch(direction) {
		// y coordinate for the head of the snake
		case 'U':
			y[0] = y[0] - UNIT_SIZE; 
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE; 
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE; 
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE; 
			break;	
		}
		
	}
	public void checkApple() {
	// 	examine the coordinates of the snake and the coordinates of the apple
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++; // if apple eaten then body of the snake gets biger by one unit
			applesEaten++; // score
			newApple(); // generate new apple after snake eates the apple
		}
	}
	public void checkCollisions() {
		// checks id head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		// checks if head touches left border
		if(x[0] < 0) {
			running = false;
			
		}
		// checks if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// checks if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		// checks if head touches bottom border
		if(x[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		
		this.setBackground(new Color(47, 82, 51));
		// Score
		g.setColor(new Color(177, 216, 183));
		g.setFont(new Font("Ink Free",Font.BOLD,30));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE: "+applesEaten))/2, SCREEN_HEIGHT/3); //g.getFont().getSize()
		// Game Over text
		g.setColor(new Color(148, 201, 115));
		g.setFont(new Font("Ink Free",Font.BOLD,70));
		// creating instance for the font metrics
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) { // what to check
			move();
			checkApple();
			checkCollisions();
			
		}//if game is no more runing
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
		// exmina e key event
			switch(e.getKeyCode()) {
			// dont want to turn 180*, enliminate that user can turn only by 90*, becouse , if 180 - game over, as soon as snake colides with it self
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';					
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';					
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';					
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';					
				}
				break;
			}
			
		}
	}

}
