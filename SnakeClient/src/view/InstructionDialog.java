package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


//Class used to create a dialog box to display the "How to play" information of the game
//By James Snee
public class InstructionDialog extends JDialog {

	//Default Serial ID
	private static final long serialVersionUID = 1L;
	//Confirm button to close the display dialog
	private JButton confirmButton;
	//Frame to view the dialog belongs (i.e. will be triggered from an action on this frame)
	private MainFrame frame;
	//Panel to hold the button
	private JPanel okButtonPanel;
	//rules to be displayed on the dialog
	private ImageIcon rules;


	public InstructionDialog(MainFrame frame)
	{
		this.frame = frame;

		this.setTitle("How to play");
		//cannot resize the Rules dialog
		this.setResizable(false);
		this.setAlwaysOnTop(true);

		//define border layout for the popup with three rows and one column
		this.setLayout(new BorderLayout(2,1));


		//Assign a rules image to a new JPanel
		ImageIcon rules = new ImageIcon("rules.jpg");
		JLabel rulesLabel = new JLabel("", rules, JLabel.CENTER);
		JPanel rulesPanel = new JPanel(new BorderLayout());
		rulesPanel.setPreferredSize(new Dimension(700, 500));
		rulesPanel.add(rulesLabel,BorderLayout.CENTER);
		rulesPanel.setBackground(Color.WHITE);


		//Set the text for the button and background for the container panel
		okButtonPanel = new JPanel();
		okButtonPanel.setBackground(Color.WHITE);
		confirmButton = new JButton("OK");
		confirmButton.setPreferredSize(new Dimension(100,50));


		//Add all the panels on the dialog window
		this.add(rulesPanel, BorderLayout.CENTER);
		this.add(okButtonPanel, BorderLayout.SOUTH);
		okButtonPanel.add(confirmButton);

		//Set the size, location & visible status of the dialog window
		this.setSize(800,660);
		this.setLocation(100,0);
		this.setVisible(false);

		//Add an action listener to handle the button action event
		confirmButton.addActionListener(new controller.CancelButtonActionListener(frame));
	}
}
