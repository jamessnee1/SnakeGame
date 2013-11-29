package view;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

//Class to create a single snake head cell for the board 
//By James Snee (Double Buffering)
public class SnakeHeadCellPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	//creates variables for the purposes of double buffering
	private Image doubleBufferImage;
	private Graphics doubleBufferGraphics;

	//color to be used to create the cell
	private Color snakeColor = Color.BLUE;

	//default constructor
	public SnakeHeadCellPanel() {
		super(true);
		this.setOpaque(false);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}
	
	//overwriting the paint method, to load graphics faster. Creates an image of the game screen's width
	//and height, then gets the contents, saves into the image and calls the paintComponent method.
	public void paint(Graphics g){
		
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		doubleBufferImage = createImage(getWidth(), getHeight());
		doubleBufferGraphics = doubleBufferImage.getGraphics();
		paintComponent(doubleBufferGraphics);
		g.drawImage(doubleBufferImage, 0,0,this);

	}

	//create a filled square of a color defined by the snakeColor variable when painted with a thicker border than a normal snake cell
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth();
		int height = getHeight();

		g.setColor(snakeColor);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		g.fillRect((int) (0.01 * width), (int) (0.01 * height),
				(int) (width), (int) (height));
		
	}

	//Accessor to get the Snake Color
	public Color getSnakeColor() {
		return snakeColor;
	}

	//Set the Snake Color
	public void setSnakeColor(Color snakeColor) {
		this.snakeColor = snakeColor;
	}

	public Dimension getPrefferedSize(){
		return new Dimension(80, 80);
	}
}


