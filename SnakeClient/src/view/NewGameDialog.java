package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import model.StartLocation;



//Class used to create a dialog box to start the game
//It displays options like - 
// Number of Players, 
// number of row/column cells on the board & 
// the starting position 
//By James Snee and Razlan Adnan
public class NewGameDialog extends JDialog {

	//Default Serial ID
	private static final long serialVersionUID = 1L;
	//Frame to view the dialog belongs (i.e. will be triggered from an action on this frame)
	private MainFrame frame;
	//Logo to be displayed on the dialog
	private ImageIcon logo;
	//Define a textarea to dislay the text for number of players option
	private JTextArea rules = new JTextArea(300,100);
	//Radio buttons & buttongroup for number of players option
	private JRadioButton playerTwo;
	private JRadioButton playerThree;
	private JRadioButton playerFour;
	private JPanel radioButtonPanel;
	private ButtonGroup bg;
	//Confirm button to get user action
	private JButton confirmButton;
	//Panel to hold the button
	private JPanel okButtonPanel;


	//Labels to be used for number of rows/cols option
	private JLabel lblNumRows, lblNumCols;
	//Comboboxes to provide values for the number of rows/cols option
	private JComboBox cmbNumRows, cmbNumCols;
	//Panel to hold controls used for number of rows/cols option
	private JPanel pBoardRowsColsPanel;

	//Array to define the possible values for the number of Rows/Cols option 
	private final Integer[] rowscolsOptions = new Integer[]{10,20,30,40};

	//HashMap that holds the available options for the Starting position/location of the Snake.
	//This will be provided by the invoking frame to this dialog
	private HashMap<String, StartLocation> availableStartLocs;

	//Label to hold text for the Starting position option
	private JLabel lblStartingPosition;
	//Combobox to hold possible value for the Starting position option
	private JComboBox cmbStartingPosition;
	//Panel to hold all the controls used for the Starting position option
	private JPanel pStartingPositionPanel;

	//A boolean flag to identify if this panel is displayed to the first player. This value will come from the invoking frame. 
	private boolean isFirstPlayer = false;


	//Constructor
	public NewGameDialog (MainFrame frame, ArrayList<StartLocation> avlStartLocs, boolean isFirstPlayer){

		this.frame = frame;
		this.setTitle("New Game");
		this.isFirstPlayer = isFirstPlayer;
		//Cannot resize this dialog window
		this.setResizable(false);

		//Set layout depending on the player. i.e. six rows if first player, else four rows
		if(isFirstPlayer)
			this.setLayout(new GridLayout(6,1));
		else
			this.setLayout(new GridLayout(3,1));

		//Set the logo for the dialog and properties for associated controls
		logo = new ImageIcon("logo.jpg");
		JLabel logoLabel = new JLabel("", logo, JLabel.CENTER);
		JPanel logoPanel = new JPanel(new BorderLayout());
		logoPanel.add(logoLabel,BorderLayout.CENTER);
		logoPanel.setBackground(Color.WHITE);

		//Add the logo to the window
		this.add(logoPanel);

		//Show number of players & number of board rows/cells options only to the first player 
		if(isFirstPlayer)
		{
			//Set the properties of control used for the number of players option 
			rules = new JTextArea("How many players would you like? (You can select up to 4 players)");
			rules.setLineWrap(true);
			rules.setEditable(false);

			//Panel to hold Radio Buttons 
			radioButtonPanel = new JPanel();
			radioButtonPanel.setBackground(Color.WHITE);

			this.add(rules);

			playerTwo = new JRadioButton("2", true);
			playerThree = new JRadioButton("3",false);
			playerFour = new JRadioButton("4",false);
			bg = new ButtonGroup();
			playerTwo.setBackground(Color.WHITE);
			playerThree.setBackground(Color.WHITE);
			playerFour.setBackground(Color.WHITE);
			bg.add(playerTwo);
			bg.add(playerThree);
			bg.add(playerFour);
			radioButtonPanel.add(playerTwo);
			radioButtonPanel.add(playerThree);
			radioButtonPanel.add(playerFour);

			//Set properties for the controls used for Number of rows/columns for the board
			lblNumRows = new JLabel("Select of Rows for Board - ");
			lblNumCols = new JLabel("Select of Columns for Board - ");

			cmbNumRows = new JComboBox(rowscolsOptions);
			cmbNumCols = new JComboBox(rowscolsOptions);

			pBoardRowsColsPanel = new JPanel(new GridLayout(2,2));
			pBoardRowsColsPanel.setBackground(Color.WHITE);
			pBoardRowsColsPanel.add(lblNumRows);
			pBoardRowsColsPanel.add(cmbNumRows);
			pBoardRowsColsPanel.add(lblNumCols);
			pBoardRowsColsPanel.add(cmbNumCols);

			//Add the panels with Number of players and Number of rows/cols option controls to the dialog window
			this.add(radioButtonPanel);
			this.add(pBoardRowsColsPanel);

		}

		//Set the properties of the controls for the action button (Start Game)
		okButtonPanel = new JPanel();
		okButtonPanel.setBackground(Color.WHITE);
		confirmButton = new JButton("Start Game!");
		confirmButton.setPreferredSize(new Dimension(150,50));
		okButtonPanel.add(confirmButton);

		//Set the properties of the controls for the Starting Position option
		lblStartingPosition = new JLabel("Starting Position");

		//Populate the available options hashmap based on the values available from the invoking window  
		availableStartLocs = new HashMap<String, StartLocation>();
		for(StartLocation sl: avlStartLocs)
		{
			if(sl == StartLocation.TOPLEFT)
				availableStartLocs.put("Top Left", StartLocation.TOPLEFT);
			if(sl == StartLocation.TOPRIGHT)
				availableStartLocs.put("Top Right", StartLocation.TOPRIGHT);
			if(sl == StartLocation.BOTTOMLEFT)
				availableStartLocs.put("Bottom Left", StartLocation.BOTTOMLEFT);
			if(sl == StartLocation.BOTTOMRIGHT)
				availableStartLocs.put("Bottom Right", StartLocation.BOTTOMRIGHT);
		}

		//Assign the available options to the Starting position combobox 
		cmbStartingPosition = new JComboBox(availableStartLocs.keySet().toArray());

		pStartingPositionPanel = new JPanel(new GridLayout(1,2));
		pStartingPositionPanel.setBackground(Color.WHITE);
		pStartingPositionPanel.add(lblStartingPosition);
		pStartingPositionPanel.add(cmbStartingPosition);

		//Add the panels with starting position and action button controls to the window
		this.add(pStartingPositionPanel);
		this.add(okButtonPanel);

		//Set the size, location for the dialog window  
		this.setSize(430,400);
		this.setLocation(300,100);
		this.setVisible(false);

		//Assign an action listener to handle the "Start Game!" button event
		confirmButton.addActionListener(new controller.StartGameActionListener(this, this.frame));
	}

	//Returns the number of players selected by the user
	public int getNumOfPlayersSelected()
	{
		if(isFirstPlayer)
		{
			if(playerTwo.isSelected())
				return 2;
			if(playerThree.isSelected())
				return 3;
			if(playerFour.isSelected())
				return 4;
		}
		return 1;
	}

	//Returns the number of rows for board selected by the first player.
	//Else returns -1
	public Integer getNumOfRowsSelected()
	{
		if(isFirstPlayer)
			return (Integer)cmbNumRows.getSelectedItem();
		return -1;
	}

	//Returns the number of cols for board selected by the first player.
	//Else returns -1
	public Integer getNumOfColsSelected()
	{
		if(isFirstPlayer)
			return (Integer)cmbNumCols.getSelectedItem();
		return -1;
	}

	//Returns the starting position selected by the user.
	public StartLocation getStartionLocation()
	{
		return availableStartLocs.get(cmbStartingPosition.getSelectedItem());
	}

}
