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


//Class used to create a dialog box to display the "About" information of the group members
//By James Snee
public class AboutDialog extends JDialog {

	//Default Serial ID
	private static final long serialVersionUID = 1L;
	//Define a textarea to display the "About" text
	private JTextArea about = new JTextArea(5 ,20);
	//Confirm button to close the display dialog
	private JButton confirmButton;
	//Frame to view the dialog belongs (i.e. will be triggered from an action on this frame)
	private MainFrame frame;
	//Panel to hold the button
	private JPanel okButtonPanel;
	//Logo to be displayed on the dialog
	private ImageIcon logo;


	public AboutDialog(MainFrame frame)
	{
		this.frame = frame;

		this.setTitle("About Snake");
		//cannot resize the About dialog
		this.setResizable(false);
		//set dialog to always be on top of other dialogs
		this.setAlwaysOnTop(true);

		//define grid layout with three rows and one column
		this.setLayout(new GridLayout(3,1));


		//Assign a logo image
		ImageIcon logo = new ImageIcon("logo.jpg");
		JLabel logoLabel = new JLabel("", logo, JLabel.CENTER);
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.add(logoLabel,BorderLayout.CENTER );
		logoPanel.setBackground(Color.WHITE);


		//Set information to be displayed in the textarea
		about = new JTextArea("Snake V1.0 " + "By Jagdeep Kaur, James Snee and Razlan Adnan\nCopyright (c) 2013 Team Awesome. All rights reserved.");
		about.setLineWrap(true);
		about.setEditable(false);

		//Set the text for the button and background for the container panel
		okButtonPanel = new JPanel();
		okButtonPanel.setBackground(Color.WHITE);
		confirmButton = new JButton("OK");
		confirmButton.setPreferredSize(new Dimension(100,50));


		//Add all the panels on the dialog window
		this.add(logoPanel);
		this.add(about);
		this.add(okButtonPanel);
		okButtonPanel.add(confirmButton);

		//Set the size, location & visible status of the dialog window
		this.setSize(360,260);
		this.setLocation(300,200);
		this.setVisible(false);

		//Add an action listener to handle the button action event
		confirmButton.addActionListener(new controller.CancelButtonActionListener(frame));
	}
}



