import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;


public class ProjectileComponent extends JComponent{
	private static final double X_VELOCITY=5.0;
	private static final double Y_VELOCITY_INITIAL=5.0;
	private static final int TICK_LENGTH = 5;
	private static final int ARROW_LENGTH = 10;
	private static final int NUM_POINTS=100;
	
	private int xScale;
	private int yScale;
	private int xOffset;
	private int yOffset;
	
	private ProjectileComponent(){
		//Every 5 pixels = 1 unit
		xScale = 5;
		yScale = 5;
		//10 pixels from the bottom of the screen
		xOffset = 10;
		yOffset = 10;
	}
	@Override
	protected void paintComponent(Graphics g) {
		drawAxis(g);
		plot(g);
	}
	private void drawAxis(Graphics g){
		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();
		//width and height of the graphable plane
		final int P_WIDTH = WIDTH - xOffset;
		final int P_HEIGHT = HEIGHT - yOffset;
		//draw the x axis
		g.drawLine(xOffset, P_HEIGHT, WIDTH, P_HEIGHT);
		//draw the y axis
		g.drawLine(xOffset, 0, xOffset, P_HEIGHT);
		//TODO comment
		int xTicks = P_WIDTH / xScale;
		int yTicks = P_HEIGHT / yScale;
		int xTickDistance = P_WIDTH / xTicks;
		int yTickDistance = P_HEIGHT / yTicks;
		//draw the x tick marks
		for(int x = xOffset; x <= P_WIDTH; x += xTickDistance){
			g.drawLine(x, P_HEIGHT, x, P_HEIGHT + TICK_LENGTH);
		}
		//draw the y tick marks
		for(int y = P_HEIGHT; y >= 0; y -= yTickDistance){
			g.drawLine(yOffset, y, yOffset - TICK_LENGTH, y);
		}
		//draw the x arrow
		drawArrow(g, false);
	}
	private void drawArrow(Graphics g, boolean isUp){
		if(isUp){
			
		}
		else{
			
		}
	}
	private void plot(Graphics g){
		
	}
	public static void main(String[] args){
		JFrame frame = new JFrame("Projectile Physics");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ProjectileComponent pc = new ProjectileComponent();
		frame.add(pc);
		frame.setVisible(true);
	}
}
