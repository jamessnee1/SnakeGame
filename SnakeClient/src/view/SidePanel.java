package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Class to create a side panel to display the current player and the final scores 
//By Jagdeep Kaur
public class SidePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	//variable to hold the current player index for the player
	private int currentPlayerIndex = -1;
	//ArrayList object to hold the scores for all players - used to display the scores 
	private ArrayList<Integer> playerScores = null;

	//Constructor
	public SidePanel(){
		//Set the background of the panel 
		this.setBackground(Color.LIGHT_GRAY);

	}

	//Method to set the current player index
	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	//Method to set the current player index
	public void setPlayerScores(ArrayList<Integer> playerScores) {
		this.playerScores = playerScores;
	}

	//method to refresh the panel with scores from the playerScores ArrayList and also display the current player 
	public void refreshScores()
	{
		//clear the panel by removing existing controls
		this.removeAll();

		//set the layout and border based on the number of player scores plus 1 for the current player label
		this.setLayout(new GridLayout(playerScores.size() + 1, 2));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		//Add controls to display the current player
		JLabel currentplayerLabel = new JLabel("You are: Player ");
		JLabel currentplayerText = new JLabel("" + (currentPlayerIndex + 1));

		//Create a font to make current player label bold   
		Font newLabelFont = new Font(currentplayerLabel.getFont().getName(),Font.BOLD,currentplayerLabel.getFont().getSize());

		currentplayerLabel.setFont(newLabelFont);
		currentplayerText.setFont(newLabelFont);

		this.add(currentplayerLabel);
		this.add(currentplayerText);

		//loop though the playerScores ArrayList and add controls to display score for each player 
		for(int i=0; i< playerScores.size(); i++)
		{
			JLabel playerScore = new JLabel("Player " + (i + 1) + " Score: ");

			JLabel ptext = new JLabel(playerScores.get(i).toString());

			if(i == currentPlayerIndex)
			{
				//Set JLabel font using new created font  
				playerScore.setFont(newLabelFont);
				ptext.setFont(newLabelFont);
			}

			this.add(playerScore);
			this.add(ptext);
		}
		paintAll(getGraphics());
	}
}
