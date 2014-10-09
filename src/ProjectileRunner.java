import javax.swing.JFrame;

/**
 * @author Matt Spooner and Derek Wider
 */
public class ProjectileRunner {
	/*
	 * TODO:
	 * What needs to be done:
	 * draw the scale under each tick mark
	 * draw the arrowheads
	 * draw the actual graph
	 * write the physics equations into methods
	 */
	public static void main(String[] args){
		JFrame frame = new JFrame("Projectile Physics");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ProjectileComponent pc = new ProjectileComponent();
		frame.add(pc);
		frame.setVisible(true);
	}
}
