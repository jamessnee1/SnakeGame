package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.StartLocation;

//ActionListener to handle event when the "Start Game!" button is pressed from the New Game dialog
//By James Snee
public class StartGameActionListener implements ActionListener {
	//frame that holds the New game dialog
	private view.MainFrame frame;
	//dialog object that will invoke the this handler
	private view.NewGameDialog dialog;

	public StartGameActionListener(view.NewGameDialog dialog, view.MainFrame frame)
	{
		this.frame = frame;
		this.dialog = dialog;
	}


	public void actionPerformed(ActionEvent e)
	{
		//Get user selection from New Game dialog window using the relevant 
		// properties/methods of the NewGameDialog class
		int numOfPlayers = dialog.getNumOfPlayersSelected();
		int numOfRowsForBoard = dialog.getNumOfRowsSelected();
		int numOfColsForBoard = dialog.getNumOfColsSelected();
		StartLocation startLoc = dialog.getStartionLocation();

		//Hide the New game dialog and start the game by passing the options selected by the player
		frame.getNewGameDialog().setVisible(false);
		frame.startGame(numOfPlayers, numOfRowsForBoard, numOfColsForBoard, startLoc);
	}
}
