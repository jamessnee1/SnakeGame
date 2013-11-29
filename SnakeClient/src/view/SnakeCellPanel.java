package view;
import java.awt.*;

import javax.swing.JPanel;

//Class to create a single snake cell for the board 
//By James Snee (Double Buffering)
public class SnakeCellPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	//creates variables for the purposes of double buffering
	private Image doubleBufferImage;
	private Graphics doubleBufferGraphics;

	//color to be used to create the cell
	private Color snakeColor = Color.BLUE;

	//default constructor
	public SnakeCellPanel() {
		super(true);
	}
	//overriding the paint method, to load graphics faster. Creates an image of the game screen's width
	//and height, then gets the contents, saves into the image and calls the paintComponent method.
	public void paint(Graphics g){

		doubleBufferImage = createImage(getWidth(), getHeight());
		doubleBufferGraphics = doubleBufferImage.getGraphics();
		paintComponent(doubleBufferGraphics);
		g.drawImage(doubleBufferImage, 0,0,this);
	}

	//create a filled square of a color defined by the snakeColor variable when painted
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth();
		int height = getHeight();

		g.setColor(snakeColor);
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


