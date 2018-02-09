package Visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MySlider extends JPanel implements MouseMotionListener, ComponentListener, MouseListener {
	
	// COLORING SECTION
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private final Color BORDER_COLOR = Color.BLACK;
	private final Color SLIDERLINE_COLOR = Color.BLACK;
	private final Color HAIRSTROKES_COLOR = Color.BLACK;
	
	// COEFFICIENTS SECTION
	private final double MY_BUTTON_HEIGHT_K = 0.5;	// this.getHeight()*MY_BUTTON_HEIGHT_K
	private final double HAIRSTROKE_HEIGHT_K = 0.1;	// this.getHeight()*HAIRSTROKE_HEIGHT_K
	private final double SLIDER_LINE_K = 0.9;		// this.getWidth()*SLIDER_LINE_K
	
	/**
	 * True - MyButton width will be equal to distance between two hairstrokes.<br>
	 * False - MyButton width will be equal to MY_BUTTON_DEFAULT_WIDTH
	 */
	private final boolean ADJUST_BUTTON_WIDTH = false;
	private final int MY_BUTTON_DEFAULT_WIDTH = 10;
	
	private Screen scr;
	private JButton myButton;
	private int startValue;
	private int endValue;
	private int capacity;
	/**
	 * Draw hairstrokes or not.
	 */
	private boolean isHairstrokes = true;
	/**
	 * Current slider's position.
	 */
	private int value;
	/**
	 * Array of hairstroke's X coordinates.
	 */
	private ArrayList<Integer> hsXX = new ArrayList<>();
	/**
	 * Distance between two hairstrokes.
	 */
	private float dX;
	/**
	 * Needs to calculate slider's value after MyButton moving.<br>
	 * 1 - if startValue > endValue <br>
	 * -1 - if startValue < endValue <br>
	 */
	private int incOrDec = 1;
	private int mouseX;
	private boolean enabled = true;
	
	public MySlider(int startValue, int endValue, Screen scr) {
		this.scr = scr;
		this.startValue = startValue;
		this.endValue = endValue;
		setBackground(BACKGROUND_COLOR);
		setLayout(null);
		capacity = endValue - startValue;
			if (capacity<0){
				capacity *= -1;
				incOrDec = -1;
			}
		myButton = new JButton();
		myButton.setFocusable(false);
		add(myButton);
		addMouseMotionListener(this);
		addMouseListener(this);
		addComponentListener(this);
		myButton.addMouseMotionListener(this);
		myButton.addMouseListener(this);
	}
	
	private void setUpMyButton(){
		int width = setUpMyButtonWidth();
		int height = (int)(getHeight()*MY_BUTTON_HEIGHT_K);
		int x = getLineX1() - width/2;
		int y = getLineY() - height/2;
		value = startValue;
		myButton.setBounds(x, y, width, height);
//		myButton.repaint();
	}
	
	private int setUpMyButtonWidth() {
		int width;
			if (ADJUST_BUTTON_WIDTH)
				if (capacity == 0)
					width = MY_BUTTON_DEFAULT_WIDTH;
				else
					width = (int)(getWidth()*SLIDER_LINE_K)/capacity;
			else
				width = MY_BUTTON_DEFAULT_WIDTH;
		return width;
	}

	@Override
	public void paint(Graphics g) {
			super.paint(g);
			if (isHairstrokes){
				drawHairstrokes(g);
			}
			g.setColor(SLIDERLINE_COLOR);
			g.drawLine(getLineX1(), getLineY(), getLineX2(), getLineY());
			
			g.setColor(BORDER_COLOR);
			g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}

	private int getLineX1(){
		return (int)(getWidth()*(1-SLIDER_LINE_K)/2);
	}
		private int getLineX2(){
			return (int)(getWidth()*(1+SLIDER_LINE_K)/2);
		}
			private int getLineY(){
				return getHeight()/2;
			}
			
	/**
	 * Enables (true) or disables (false) hairstrokes on slider's line.
	 * @param b - boolean.
	 */
	public void setHairstrokes(boolean b){
		isHairstrokes = b;
	}
		public boolean isHairstrokes(){
			return isHairstrokes;
		}
		
	private void drawHairstrokes(Graphics g) {
		hsXX.clear();
		g.setColor(HAIRSTROKES_COLOR);
		int height = (int)(getHeight()*HAIRSTROKE_HEIGHT_K);
		dX = (getLineX2()-getLineX1())/(float)capacity;
		for (int i = 0; i <= capacity; i++) {
			int x = (int)(getLineX1()+dX*i);
				x = (i == capacity)? getLineX2():x;
			hsXX.add(x);
			g.drawLine(x, getLineY()-height/2, x, getLineY()+height/2);
		}
		
	}

	private void connectMouseXtoHairstrokeX() {
		if (enabled)
			for (Integer hairstrokeX : hsXX) {
				if (mouseX >= hairstrokeX-dX/2
						&& mouseX < hairstrokeX+dX/2){
					value = startValue+incOrDec*hsXX.indexOf(hairstrokeX);
					// THIS IS THE PLACE TO CATCH SLIDER's VALUE
					// I.E.: scr.lbl2.setText(valueString());
					scr.lvlLbl.setText("Уровень "+valueToString());
					myButton.setLocation(hairstrokeX-myButton.getWidth()/2, myButton.getY());
				}
			}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getSource().equals(myButton)){
			mouseX = e.getX()+myButton.getX();
			connectMouseXtoHairstrokeX();
		}
	}
	public void mouseMoved(MouseEvent e) {
		if (e.getSource().equals(this)){
			mouseX = e.getX();
		}
		if (e.getSource().equals(myButton)){
			mouseX = e.getX()+myButton.getX();
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void componentResized(ComponentEvent e) {
		setUpMyButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(this)){
			connectMouseXtoHairstrokeX();
		}
	}
	
	public String valueToString(){
		return Integer.toString(value);
	}
		public Integer valueToInteger(){
			return value;
		}
		
	public void setEnabled(boolean b){
		enabled = b;
		myButton.setEnabled(enabled);
	}
		public boolean isEnabled(){
			return enabled;
		}
	
	public void setValue(int newValue){
		if ((newValue < startValue && newValue < endValue)
			|| (newValue > startValue && newValue > endValue))
				System.out.println("New value is out of the MySlider's range");
		else{
			value = newValue;
			int dValue = newValue - startValue;
				dValue = (dValue < 0)? -dValue:dValue;
			myButton.setLocation((int)(hsXX.get(0)+dX*dValue-myButton.getWidth()/2), myButton.getY());
		}
	}
}
