import javax.swing.JFrame;

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
	public static void main(String[] args){
		JFrame frame = new JFrame("Projectile Physics");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ProjectileComponent pc = new ProjectileComponent(10, 10, 55, 55);
		frame.add(pc);
		frame.setVisible(true);
	}
}
