package view;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

//Class to create an empty single cell for the board 
//By James Snee
public class BoardCellPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	//default constructor
	public BoardCellPanel() {

		this.setBackground(Color.LIGHT_GRAY);

	}

	//create an empty square when painted
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth();
		int height = getHeight();

		g.setColor(Color.BLACK);
		g.drawRect((int) (0.01 * width), (int) (0.01 * height),
				(int) (width), (int) (height));
	}

	public Dimension getPrefferedSize(){
		return new Dimension(80, 80);
	}
}


