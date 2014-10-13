import javax.swing.JFrame;
import javax.swing.JSplitPane;

/**
 * @author Matt Spooner and Derek Wider
 */
public class ProjectileRunner {
	/*
	 * TODO:
	 * What needs to be done:
	 * draw the arrowheads
	 * draw the actual graph
	 * optimise drawing to not calculate all the math each time repaint is called
	 */
	//members
	private static Object lock;
	private static ProjectileInputPanel inputPanel;
	private static ProjectileComponent projectileGraph;
	//methods
	public static void main(String[] args){
		//initialize thread lock
		lock = new Object();
		//setup and show the frame
		setupFrame();
		//wait for update from input panel to update projectile component
		waitForUpdate();
	}
	private static void setupFrame(){
		JFrame frame = new JFrame("Projectile Physics");
		inputPanel = new ProjectileInputPanel(lock);
		projectileGraph = new ProjectileComponent(10, 10);
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, projectileGraph);
		split.setContinuousLayout(false);
		frame.add(split);
		
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	private static void waitForUpdate(){
		try{
			//wait for notify from input panel
			while(true){
				synchronized(lock){
					lock.wait();
				}
				//set and graph the new parameters
				try{
					projectileGraph.setXPosition(inputPanel.getXPostition());
					projectileGraph.setYPosition(inputPanel.getYPosition());
					projectileGraph.setXAccel(inputPanel.getXAccel());
					projectileGraph.setYAccel(inputPanel.getYAccel());
					projectileGraph.setLaunch(inputPanel.getVelocity(), inputPanel.getAngle());
					projectileGraph.replot();
				}
				catch(NumberFormatException nfe){
					//should never be called, as fields are verified before thread is notified
					nfe.printStackTrace();
					System.exit(-1);
				}
			}
		}
		catch(InterruptedException ie){
			ie.printStackTrace();
			System.exit(-1);
		}
	}
}
