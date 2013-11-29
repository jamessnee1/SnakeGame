package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.StartLocation;

import controller.SnakeKeyListener;

//class for the frame of the client application
// presents all the menu and other UI components together
//By James Snee
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	//object for menu panel to be added to the frame
	private MenuPanel top;
	//board board for the game
	private Board mainBoard;
	//side panel to show player number and scores
	private SidePanel side;
	//about dialog to be used for "About" function
	private AboutDialog about;
	//rules dialog to be used for "How to play" function
	private InstructionDialog rules;
	//dialog object used to start the game
	private NewGameDialog newGameScreen;
	//panel to hold the mainboard object
	private JPanel middlePanel;
	//panel to display status bar
	private JLabel statusBar;
	//dialog to be displayed when player loses
	private YouLoseDialog lose;
	//dialog to be displayed when player wins
	private YouWinDialog win;


	//flag to check if this is the first player for the game
	private boolean isFirstPlayer = false;

	//constructor
	public MainFrame()
	{

		//set the title for the window/frame
		super("Snake Game");
		//set the relevant properties for the frame to set background and avoid resizing
		this.setResizable(false);
		this.setBackground(Color.GRAY);

		//instantiate various objects declared for the class and set relevant properties
		lose = new YouLoseDialog();
		win = new YouWinDialog();
		top = new MenuPanel(this);
		about = new AboutDialog(this);
		rules = new InstructionDialog(this);
		middlePanel = new JPanel();
		middlePanel.setSize(800,550);
		side = new SidePanel();
		side.setSize(100,668);
		side.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainBoard = new Board(this);
		mainBoard.setBackground(Color.LIGHT_GRAY);
		mainBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK));


		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.NORTH);
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(side, BorderLayout.EAST);

		middlePanel.add(mainBoard, JPanel.CENTER_ALIGNMENT);


		//initial status of the status bar
		statusBar = new JLabel("Starting...");
		this.add(statusBar, BorderLayout.SOUTH);

		this.setSize(1024,668);
		this.setVisible(true); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainBoard.setFocusable(true);
		mainBoard.setFocusTraversalKeysEnabled(true);

		//get data from server via mainboard to see if this is the first player of the game
		this.isFirstPlayer = mainBoard.isFirstPlayer(); 

		//display the new game dialog
		newGameScreen = new NewGameDialog(this, mainBoard.getAvailableStartLocs(), isFirstPlayer);
		newGameScreen.setAlwaysOnTop(true);

		newGameScreen.setVisible(true);
	}

	//method to start the game by passing relevant parameters to the board class' object
	public void startGame(int numOfPlayers, int numOfBoardRows, int numOfBoardCols, StartLocation startLoc)
	{
		mainBoard.startGame(isFirstPlayer, numOfPlayers, numOfBoardRows, numOfBoardCols, startLoc);
		//set the SnakeKeyListener to handle snake movements via keyboard
		mainBoard.addKeyListener(new SnakeKeyListener(mainBoard));

		if (this.getMainBoard().getSnakePanel() != null)
			paintAll(getGraphics());
		else
			remove(this.getMainBoard().getSnakePanel());

	}

	//return the menu panel object for this frame
	public MenuPanel getTopPanel()
	{
		return top;
	}

	//return the board object for this frame
	public Board getMainBoard()
	{
		return mainBoard;

	}

	//return the "About" dialog object for this frame
	public Dialog getAboutDialog() {

		return about;
	}

	//return the new game dialog object for this frame
	public Dialog getNewGameDialog() {

		return newGameScreen;
	}

	//set the status bar text with the value passed
	public void setStatusBarText(String text)
	{
		statusBar.setText(text);
		paintAll(getGraphics());
	}

	//method when player wins and refreshes the status bar & displays the you win dialog 
	public void playerWon()
	{
		setStatusBarText("You won! Hurray!!");
		win = new YouWinDialog();
		win.setVisible(true);
	}

	//method when player loses and refreshes the status bar & displays the you lose dialog 
	public void playerLost()
	{
		setStatusBarText("You Lost! Game Over!!!");
		//lose = new YouLoseDialog();
		lose.setVisible(true);
	}

	//method to refresh the scores in the side panel when game finishes 
	public void refreshScores(int playerIndex, ArrayList<Integer> scores)
	{
		side.setCurrentPlayerIndex(playerIndex);
		side.setPlayerScores(scores);
		side.refreshScores();
	}

	//return the rules InstructionDialog
	public InstructionDialog getInstructionDialog(){

		return rules;
	}
}


