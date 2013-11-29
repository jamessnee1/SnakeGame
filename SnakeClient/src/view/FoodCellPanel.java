package view;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

//Class to create a single food cell for the board
//By James Snee
public class FoodCellPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	//creates bufferedImage of foodItem
	private BufferedImage foodItem;


	//default constructor
	public FoodCellPanel() {

		try {
			//finds the graphics file
			foodItem = ImageIO.read(new File("fooditem.jpg"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//create a filled square of Yellow color when painted
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		//draws foodItem graphic
		if (foodItem != null){
			g.drawImage(foodItem,0,0,null);
		}
	}

	public Dimension getPrefferedSize(){
		return new Dimension(80, 80);
	}
}


