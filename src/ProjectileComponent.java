import java.awt.Font;
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
	private static final double VELOCITY_INITIAL = 10.0; //m/s
	private static final double LAUNCH_ANGLE = 50.0; //degrees
	private static final double X_ACCELERATION = -0.6; //m/s^2
	private static final double Y_ACCELERATION = -2.81;//m/s^2
	
	private static final double X_VELOCITY_INITIAL = VELOCITY_INITIAL * Math.cos(Math.toRadians(LAUNCH_ANGLE));; //m/s
	private static final double Y_VELOCITY_INITIAL = VELOCITY_INITIAL * Math.sin(Math.toRadians(LAUNCH_ANGLE)); //m/s
	
	//Graph Constants
	private static final int DEFAULT_SCALE = 2; //m
	private static final int DEFAULT_OFFSET = 25; //px
	private static final int TICK_LENGTH = 10; //px
	private static final int TICK_SPACING = 45; //px
	private static final int ARROW_LENGTH = 25; //px
	private static final int ARROW_WIDTH = 10; //px
	private static final int NUM_POINTS=30;
	private static final int X_LABEL_DISTANCE = 20; //px
	private static final int Y_LABEL_DISTANCE = 30; //px
	private static final int MAX_SCALE_DECIMAL_PLACES = 4;
	private static final int X_LABEL_OFFSET = 15; //px
	private static final Font GRAPH_FONT = new Font("monospaced", Font.PLAIN, 11);
	
	//Members
	private double xScale;
	private double yScale;
	private int xOffset;
	private int yOffset;
	
	//Constructors
	protected ProjectileComponent(){
		this(DEFAULT_SCALE, DEFAULT_SCALE);
	}
	protected ProjectileComponent(double xScale, double yScale, int xOffset, int yOffset){
		this.xScale = xScale;
		this.yScale = yScale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	protected ProjectileComponent(double xScale, double yScale){
		this(xScale, yScale, DEFAULT_OFFSET, DEFAULT_OFFSET);
	}
	protected ProjectileComponent(int xOffset, int yOffset){
		this(DEFAULT_SCALE, DEFAULT_SCALE, xOffset, yOffset);
	}
	//Methods
	@Override
	protected void paintComponent(Graphics g) {
		g.setFont(GRAPH_FONT);
		drawAxis(g);
		plot(g);
	}
	private void drawAxis(Graphics g){
		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();
		//width and height of the offset plane (includes arrow length)
		final int OFF_WIDTH = WIDTH - ARROW_LENGTH;
		final int OFF_HEIGHT = HEIGHT - yOffset;
		//draw the x axis
		g.drawLine(xOffset, OFF_HEIGHT, OFF_WIDTH, OFF_HEIGHT);
		//draw the y axis
		g.drawLine(xOffset, ARROW_LENGTH, xOffset, OFF_HEIGHT);
		//draw the x tick marks
		for(int x = xOffset; x < OFF_WIDTH; x += TICK_SPACING){
			g.drawLine(x, OFF_HEIGHT, x, OFF_HEIGHT + TICK_LENGTH);
		}
		//draw the y tick marks
		for(int y = OFF_HEIGHT; y > ARROW_LENGTH; y -= TICK_SPACING){
			g.drawLine(xOffset, y, xOffset - TICK_LENGTH, y);
		}
		//draw the x arrow
		drawArrow(g, false);
		//draw the y arrow
		drawArrow(g, true);
		//draw the x labels
		for(int x = xOffset; x < OFF_WIDTH; x += TICK_SPACING){
			int scaleMark = (x - xOffset) / TICK_SPACING;
			String label = getLabel(scaleMark * xScale);
			g.drawString(label, x - X_LABEL_OFFSET, OFF_HEIGHT + X_LABEL_DISTANCE);
		}
		//draw the y labels
		for(int y = OFF_HEIGHT; y > ARROW_LENGTH; y -= TICK_SPACING){
			int scaleMark = (OFF_HEIGHT - y) / TICK_SPACING;
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
		else{ //work in progress FOR DEREK ONLY
			/*xPoints[0] = getWidth();
			yPoints[0] = getHeight() - 20;
			
			xPoints[1] = getWidth() - xOffset;
			yPoints[1] = getHeight() - yOffset/2;
			
			xPoints[2] = getWidth() - xOffset;
			yPoints[2] = getHeight() + yOffset/2;*/
		}
		//create the triangle
		Polygon p = new Polygon(xPoints, yPoints, 3);
		g.fillPolygon(p);
	}
	private void plot(Graphics g){
		//TODO
		//TODO only do calculation once, not everytime paint is called
		//calculate the time for the projectile to hit the ground
		
		//divide that by the NUM_POINTS to get the time between each point
		
		//Iterate through each time and find the x and y position at it
		
		//Draw and arc or line through multiple points to draw the graph
		//TODO
	}
	
	private String getLabel(double scale){
		//adds spaces to line everything up and reformats the scale
		String label = String.format("%." + MAX_SCALE_DECIMAL_PLACES + "f", scale);
		//while it has insignificant 0s or the . and has a . and is not a single 0
		while((label.endsWith("0") || label.endsWith(".")) && label.contains(".") && label.length() > 1){
			//chop off last digit
			label = label.substring(0, label.length()-1);
		}
		//if starts with a 0 and not a single number,
		if(label.startsWith("0") && label.length() > 1){
			//chop off the leading 0
			label = label.substring(1);
		}
		return label;
	}
	//TODO this (just plot points), use range equation, get final distance, divide by number of points, and then add interval from that to initial, then repeat for number of points. 
	/**
	 * @return returns a double
	 * TODO compensate for displacement on computer graph
	 */
	private double calculateRange(){
		double range = ((Math.pow(VELOCITY_INITIAL, 2))*(Math.sin(Math.toRadians(2*LAUNCH_ANGLE))))/Y_ACCELERATION; //dat range equation
		return range; //return it!
	}
}
