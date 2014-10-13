import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * @author Matt Spooner and Derek Wider
 */
@SuppressWarnings("serial")
public class ProjectileInputPanel extends JPanel implements ActionListener{
	//members
	//Physics Constants (defaults)
	private static final double X_POSITION_INITIAL = 0; //m
	private static final double Y_POSITION_INITIAL = 0; //m
	private static final double VELOCITY_INITIAL = 15.0; //m/s
	private static final double LAUNCH_ANGLE = 45.0; //degrees
	private static final double X_ACCELERATION = 0; //m/s^2
	private static final double Y_ACCELERATION = -9.81;//m/s^2
	private static final String EMPTY = "[EMPTY]";
	private static final Border FIELD_BORDER = BorderFactory.createLineBorder(Color.BLUE, 2, true);
	
	private Object lock;
	private JTextField xPositionField;
	private JTextField yPositionField;
	private JTextField xAccelerationField;
	private JTextField yAccelerationField;
	private JTextField velocityField;
	private JTextField angleField;
	private JButton plotButton;
	//constructor
	protected ProjectileInputPanel(Object lock){
		//will notify the thread waiting on the lock on button click
		this.lock = lock;
		//2 rows, 4 cols, 5px spacing
		setLayout(new GridLayout(2, 4, 5, 5));
		xPositionField = createField("xPos");
		xPositionField.setText(Double.toString(X_POSITION_INITIAL));
		yPositionField = createField("yPos");
		yPositionField.setText(Double.toString(Y_POSITION_INITIAL));
		xAccelerationField = createField("xAccel");
		xAccelerationField.setText(Double.toString(X_ACCELERATION));
		yAccelerationField = createField("yAccel");
		yAccelerationField.setText(Double.toString(Y_ACCELERATION));
		velocityField = createField("velocity");
		velocityField.setText(Double.toString(VELOCITY_INITIAL));
		angleField = createField("angle(deg)");
		angleField.setText(Double.toString(LAUNCH_ANGLE));
		plotButton = new JButton("Plot!");
		plotButton.addActionListener(this);
		
		add(xPositionField);
		add(yPositionField);
		add(xAccelerationField);
		add(yAccelerationField);
		add(velocityField);
		add(angleField);
		add(plotButton);
	}
	//methods
	//private interface
	private JTextField createField(String title){
		JTextField jtf = new JTextField(EMPTY);
		//wrap in border
		jtf.setBorder(new TitledBorder(FIELD_BORDER, title+":"));
		return jtf;
	}
	private boolean verifyDoubleField(JTextField jtf){
		boolean verifyable = true;
		try{
			//try parsing the double
			Double.parseDouble(jtf.getText());
		}
		catch(NumberFormatException nfe){
			//failed
			verifyable = false;
			//get rid of bad text
			jtf.setText(EMPTY);
		}
		return verifyable;
	}
	private boolean verifyFields(){
		boolean verifyable = true;
		//verify each double field
		verifyable &= verifyDoubleField(xPositionField);
		verifyable &= verifyDoubleField(yPositionField);
		verifyable &= verifyDoubleField(xAccelerationField);
		verifyable &= verifyDoubleField(yAccelerationField);
		verifyable &= verifyDoubleField(velocityField);
		verifyable &= verifyDoubleField(angleField);
		return verifyable;
	}
	//protected interface
	protected double getXPostition() throws NumberFormatException{
		return Double.parseDouble(xPositionField.getText());
	}
	protected double getYPosition() throws NumberFormatException{
		return Double.parseDouble(yPositionField.getText());
	}
	protected double getXAccel() throws NumberFormatException{
		return Double.parseDouble(xAccelerationField.getText());
	}
	protected double getYAccel() throws NumberFormatException{
		return Double.parseDouble(yAccelerationField.getText());
	}
	protected double getVelocity() throws NumberFormatException{
		return Double.parseDouble(velocityField.getText());
	}
	protected double getAngle() throws NumberFormatException{
		return Double.parseDouble(angleField.getText());
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		//on click, only if fields are in correct format
		if(verifyFields()){
			//notify thread waiting on the object
			synchronized(lock){
				lock.notify();
			}
		}
		else{
			//show error message
			JOptionPane.showMessageDialog(getTopLevelAncestor(), 
					"One of the fields is incorrectly formated.", "You messed up!", JOptionPane.ERROR_MESSAGE);
		}
	}
}
