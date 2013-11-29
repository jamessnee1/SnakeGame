package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


//Class used to create a dialog box to be displayed when player wins the game
//By James Snee
public class YouWinDialog extends JDialog {

	//Default Serial ID
	private static final long serialVersionUID = 1L;
	//Define a panel to hold the controls for "You lost" section
	private JPanel textPanel;
	//Define a label to display the "You lost" text
	private JLabel text;
	//Logo to be displayed on the dialog
	private ImageIcon logo;


	public YouWinDialog()
	{
		this.setTitle("You Won!");
		//cannot resize the About dialog
		this.setResizable(false);

		//define grid layout with three rows and one column
		this.setLayout(new GridLayout(3,1));


		//Assign a logo image
		logo = new ImageIcon("logo.jpg");
		JLabel logoLabel = new JLabel("", logo, JLabel.CENTER);
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.add(logoLabel,BorderLayout.CENTER );
		logoPanel.setBackground(Color.WHITE);


		//Set information to be displayed in the main area
		textPanel = new JPanel();
		text = new JLabel("You Won!", JLabel.CENTER);
		textPanel.add(text, BorderLayout.CENTER);

		//Add all the panels on the dialog window
		this.add(logoPanel);
		this.add(textPanel);

		//Set the size, location & visible status of the dialog window
		this.setSize(360,260);
		this.setLocation(300,200);
		this.setVisible(false);
	}
}

