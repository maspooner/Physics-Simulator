import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;

/**
 * @author Matt Spooner and Derek Wider
 */
@SuppressWarnings("serial")
public class ProjectileComponent extends JComponent{
	//Physics Constants
	private static final double X_POSITION_INITIAL = 0; //m
	private static final double Y_POSITION_INITIAL = 0; //m
	private static final double VELOCITY_INITIAL = 5.0; //m/s
	private static final double LAUNCH_ANGLE = 45; //degrees
	private static final double X_ACCELERATION = 0.0; //m/s^2
	private static final double Y_ACCELERATION = 9.81;//m/s^2
	
	//Graph Constants
	private static final int DEFAULT_SCALE = 2; //m
	private static final int DEFAULT_OFFSET = 25; //px
	private static final int TICK_LENGTH = 5; //px
	private static final int TICK_SPACING = 15; //px
	private static final int ARROW_LENGTH = 15; //px
	private static final int ARROW_WIDTH = 8; //px
	private static final int NUM_POINTS=100;
	private static final int X_LABEL_DISTANCE = 5; //px
	private static final int Y_LABEL_DISTANCE = 15; //px
	private static final int LABEL_PLACES = 3;
	
	//Members
	private int xScale;
	private int yScale;
	private int xOffset;
	private int yOffset;
	
	//Constructors
	protected ProjectileComponent(){
		this(DEFAULT_SCALE, DEFAULT_SCALE);
	}
	protected ProjectileComponent(int xScale, int yScale, int xOffset, int yOffset){
		this.xScale = xScale;
		this.yScale = yScale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	protected ProjectileComponent(int xScale, int yScale){
		this(xScale, yScale, DEFAULT_OFFSET, DEFAULT_OFFSET);
	}
	//Methods
	@Override
	protected void paintComponent(Graphics g) {
		drawAxis(g);
		plot(g);
	}
	private void drawAxis(Graphics g){
		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();
		//width and height of the graphable plane
		final int P_WIDTH = WIDTH - xOffset - ARROW_LENGTH;
		final int P_HEIGHT = HEIGHT - yOffset - ARROW_LENGTH;
		//draw the x axis
		g.drawLine(xOffset, P_HEIGHT, WIDTH, P_HEIGHT);
		//draw the y axis
		g.drawLine(xOffset, ARROW_LENGTH, xOffset, P_HEIGHT);
		//draw the x tick marks
		for(int x = xOffset; x <= P_WIDTH; x += TICK_SPACING){
			g.drawLine(x, P_HEIGHT, x, P_HEIGHT + TICK_LENGTH);
		}
		//draw the y tick marks
		for(int y = P_HEIGHT; y >= ARROW_LENGTH; y -= TICK_SPACING){
			g.drawLine(yOffset, y, yOffset - TICK_LENGTH, y);
		}
		//draw the x arrow
		drawArrow(g, false);
		//draw the y arrow
		drawArrow(g, true);
		//draw the x labels
		for(int x = xOffset; x <= P_WIDTH; x += TICK_SPACING){
			int scaleMark = (x - xOffset) / TICK_SPACING;
			String label = getLabel(scaleMark * xScale);
			g.drawString(label, x, HEIGHT - yOffset + X_LABEL_DISTANCE);
		}
		//draw the y labels
		//TODO
		for(int y = P_HEIGHT; y >= ARROW_LENGTH; y -= TICK_SPACING){
			int scaleMark = (P_HEIGHT - y) / TICK_SPACING;
			String label = getLabel(scaleMark * yScale);
			g.drawString(label, xOffset - Y_LABEL_DISTANCE, y);
		}
	}
	private void drawArrow(Graphics g, boolean isUp){
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		if(isUp){
			//left point
			xPoints[0] = xOffset - (ARROW_WIDTH / 2);
			yPoints[0] = ARROW_LENGTH;
			//right point
			xPoints[1] = xOffset + (ARROW_WIDTH / 2);
			yPoints[1] = ARROW_LENGTH;
			//top point
			xPoints[2] = xOffset;
			yPoints[2] = 0;
		}
		else{
			//TODO
		}
		//create the triangle
		Polygon p = new Polygon(xPoints, yPoints, 3);
		g.fillPolygon(p);
	}
	private void plot(Graphics g){
		//TODO
		//calculate the time for the projectile to hit the ground
		//divide that by the NUM_POINTS to get the time between each point
		//Iterate through each time and find the x and y position at it
		//Draw and arc or line through multiple points to draw the graph
	}
	private double calculateXPosition(double time){
		//TODO
		return 0.0d;
	}
	private double calculateYPosition(double time){
		//TODO
		return 0.0d;
	}
	private String getLabel(int scale){
		//TODO
		//adds spaces to line everything up
		String label = Integer.toString(scale);
		while(label.length() < LABEL_PLACES){
			label = " " + label;
		}
		return label;
	}
}
