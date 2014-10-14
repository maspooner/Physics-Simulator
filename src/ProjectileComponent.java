import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;

/**
 * @author Matt Spooner and Derek Wider
 */
@SuppressWarnings("serial")
public class ProjectileComponent extends JComponent{
	//Graph Constants
	private static final int DEFAULT_SCALE = 2; //m
	private static final int DEFAULT_OFFSET = 35; //px
	private static final int TICK_LENGTH = 10; //px
	private static final int TICK_SPACING = 45; //px
	private static final int ARROW_LENGTH = 25; //px
	private static final int ARROW_WIDTH = 10; //px
	private static final int NUM_POINTS=50;
	private static final int X_LABEL_DISTANCE = 20; //px
	private static final int Y_LABEL_DISTANCE = 30; //px
	private static final int X_LABEL_OFFSET = 15; //px
	private static final int MAX_SCALE_DECIMAL_PLACES = 4;
	private static final Font GRAPH_FONT = new Font("monospaced", Font.PLAIN, 11);
	private static final int POINT_LENGTH = 1; //px
	private static final int CONSTANT_DRAW = 80; //milliseconds
	private static final boolean IS_CONSTANT_DRAW = false;
	
	//Members
	//Point arrays
	private double[] xCoord = new double[NUM_POINTS];
	private double[] yCoord = new double[NUM_POINTS];
	
	//Physics
	private double xScale;
	private double yScale;
	private int xOffset;
	private int yOffset;
	private double xPosition;
	private double yPosition;
	private double xAcceleration;
	private double yAcceleration;
	private double initialVelocity;
	private double angle;
	
	private double xVelocity;
	private double yVelocity;
	private double time;
	
	private int drawIndex;
	
	//Constructors
	protected ProjectileComponent(){
		this(DEFAULT_SCALE, DEFAULT_SCALE);
	}
	protected ProjectileComponent(double xScale, double yScale, int xOffset, int yOffset){
		this.xScale = xScale;
		this.yScale = yScale;
		//minimum offset is default offset
		this.xOffset = xOffset + DEFAULT_OFFSET;
		this.yOffset = yOffset + DEFAULT_OFFSET;
		//set all physics fields to 0 to start (will not graph until button on input panel is pressed)
		this.xPosition = 0.0;
		this.yPosition = 0.0;
		this.xAcceleration = 0.0;
		this.yAcceleration = 0.0;
		this.initialVelocity = 0.0;
		this.angle = 0.0;
		this.xVelocity = 0.0;
		this.yVelocity = 0.0;
		this.time = 0;
		//This means do not start in drawing mode
		this.drawIndex = -1;
	}
	protected ProjectileComponent(double xScale, double yScale){
		this(xScale, yScale, DEFAULT_OFFSET, DEFAULT_OFFSET);
	}
	protected ProjectileComponent(int xOffset, int yOffset){
		this(DEFAULT_SCALE, DEFAULT_SCALE, xOffset, yOffset);
	}
	//Methods
	//private interface
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
	private void plot(Graphics g, boolean isDrawing) throws InterruptedException{
		//if in the process of drawing
		if(isDrawing){
			//Draw at the draw index
			plotPointAtIndex(g);
		}
		else{
			//Draw all points at once
			plotPoints(g);
		}
	}
	private void plotPoints(Graphics g){
		//Draw all the points
		for(int i = 0; i < NUM_POINTS; i++){
			int pointX = getScaledX(i);
			int pointY = getScaledY(i);
			g.drawRect(pointX, pointY, POINT_LENGTH, POINT_LENGTH);
		}
		//Draw line through multiple points to draw the graph
		for(int i = 0; i < NUM_POINTS - 1; i++){
			int x1 = getScaledX(i);
			int y1 = getScaledY(i);
			int x2 = getScaledX(i + 1);
			int y2 = getScaledY(i + 1);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	private void plotPointAtIndex(Graphics g) throws InterruptedException{
		//just in case repaint is called by the AWT thread during this process
		if(drawIndex >= NUM_POINTS) return;
		//Draw 1 point
		int x1 = getScaledX(drawIndex);
		int y1 = getScaledY(drawIndex);
		g.drawRect(x1, y1, POINT_LENGTH, POINT_LENGTH);
		//Draw a connecting line to the point before (if not the first point)
		if(drawIndex != 0){
			int x2 = getScaledX(drawIndex - 1);
			int y2 = getScaledY(drawIndex - 1);
			g.drawLine(x1, y1, x2, y2);
		}
		//Wait to imitate real-time drawing (converts to millis)
		long wait;
		//quick draw speeds up long draws
		if(IS_CONSTANT_DRAW){
			wait = CONSTANT_DRAW;
		}
		else{
			//actual timing
			wait = (long) (calculatePointTime() * 1000);
		}
		Thread.sleep(wait);
		//increment the draw index
		drawIndex++;
	}
	private int getScaledX(int i){
		double scaledX = ((xCoord[i] / xScale) * TICK_SPACING) + yOffset;
		return (int) scaledX;
	}
	private int getScaledY(int i){
		double scaledY = getHeight() - xOffset - ((yCoord[i] / yScale) * TICK_SPACING);
		return (int) scaledY;
	}
	private String getLabel(double scale){
		//reformat the scale to fix rounding errors
		String label = String.format("%." + MAX_SCALE_DECIMAL_PLACES + "f", scale);
		//while (it has insignificant 0s OR the .) AND has a . AND is not a single 0
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
	/**
	 * @param None!
	 * @return The time at which a projectile is in the air. (untested)
	 */
	private double calculateTime(){ 
//		double time = ((-1*xVelocity) + Math.sqrt((Math.pow(xVelocity, 2)) + ((-4)*(.5*xAcceleration)*(-1 * calculateRange(false)))))/xAcceleration; //This should get the time, MATT CHECK MY MATH PLS (Them parenthesis!!!!)
		//I don't know how you got your equation, but it did not work. The one bellow seems to work.
		double time = (-Math.sqrt(-yPosition * 2 * yAcceleration + Math.pow(yVelocity, 2)) - yVelocity) / yAcceleration;
		return time;
	}
	private double calculatePointTime(){
		double unitTime = time/(NUM_POINTS-1);
		return unitTime;
	}
	private double calcPointX(double unitTime){
		double pointX = xPosition + (xVelocity)*(unitTime) + (.5)*(xAcceleration)*(Math.pow(unitTime, 2));
		return pointX;
	}
	private double calcPointY(double unitTime){
		double pointY = yPosition + (yVelocity)*(unitTime) + (.5)*(yAcceleration)*(Math.pow(unitTime, 2));
		return pointY;
	}
	private void calcPoints(double unitTime){
		double jumpTime = 0;
		double incrementTime = unitTime;
		for(int i = 0; i < NUM_POINTS; i++){
			double thisXCoord = calcPointX(jumpTime);
			double thisYCoord = calcPointY(jumpTime);
			xCoord[i] = thisXCoord;
			yCoord[i] = thisYCoord;
			jumpTime = jumpTime + incrementTime;
		}
	}
	//protected interface
	protected void setXPosition(double xPosition){this.xPosition = xPosition;}
	protected void setYPosition(double yPosition){this.yPosition = yPosition;}
	protected void setXAccel(double xAccel){this.xAcceleration = xAccel;}
	protected void setYAccel(double yAccel){this.yAcceleration = yAccel;}
	protected void setLaunch(double velocity, double angle){
		this.initialVelocity = velocity;
		this.angle = angle;
		this.xVelocity = velocity * Math.cos(Math.toRadians(angle));
		this.yVelocity = velocity * Math.sin(Math.toRadians(angle));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		try{
			g.setFont(GRAPH_FONT);
			drawAxis(g);
			boolean isDrawing = drawIndex != -1;
			plot(g, isDrawing);
		}
		catch(InterruptedException ie){
			//Should never happen
			ie.printStackTrace();
			System.exit(-1);
		}
	}
	protected void replot(){
		//find the new time
		time = calculateTime();
		//calculate the points
		calcPoints(calculatePointTime());
		//Enter redraw mode
		drawIndex = 0;
		//for all the points
		while(drawIndex < NUM_POINTS){
			repaint();
		}
		//Exit redraw mode
		drawIndex = -1;
	}
}
