package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import model.Snake;
import model.SnakeDirection;
import model.SnakeProtocol;
import model.SnakeSpeed;
import model.StartLocation;

//Class to hold the board and various operations based on user action & perform client-server communication
//By Jagdeep Kaur
public class Board extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	//Variables to hold the number of rows and columns to be used to create the board
	private int numOfBoardRows = 45;
	private int numOfBoardCols = 45;

	//Boolean flag to indicate if this is the first player
	private boolean isFirstPlayer = false;

	//Arraylist that holds the available options for the Starting position/location of the Snake.
	private ArrayList<StartLocation> availableStartLocs = new ArrayList<StartLocation>();

	//Arraylist that holds the scores for each player
	ArrayList<Integer> scores = new ArrayList<Integer>();

	//Variable to hold the current speed of the snake of this player
	private SnakeSpeed currentSpeed;

	//View that hold this board
	private MainFrame view = null;

	//Timer control to keep snake moving. Set the delay to maximum snake speed. 
	private Timer t = new Timer(SnakeSpeed.MAX.getValue(), this);

	//Variable to hold the current direction of the snake of this player
	private SnakeDirection currentDirection = SnakeDirection.RIGHT;

	//Snake protocol object for client-server communication and holds all the required information 
	//to be passed between client-server for the game to work
	private SnakeProtocol snakeProtocol = new SnakeProtocol();

	//Variable to hold the snake index of this player
	private int snakeIndex;

	//Variables to hold the list of food cells (locations)
	private ArrayList<Integer> foodCells;
	//Variables to hold the Array of snakes
	private ArrayList<Snake> snakes;

	//socket object to connect to the server
	private Socket socket;

	//IO streams to send and receive data between client and server
	private ObjectOutputStream outputToServer;
	private ObjectInputStream inputFromServer;

	//Snake panel object to display the board
	private SnakePanel sp;

	//Boolean flag to check if the current player is alive
	private boolean isPlayerAlive = true;
	//Boolean flag to check if the current player has won the game
	private boolean hasPlayerWon = false;

	//Server IP and Port
	private final String SERVERIPADDRESS = "127.0.0.1";
	private final int SERVERPORT = 8000;

	//constructor
	public Board(MainFrame view){

		try {
			this.view = view;

			// Create a socket to connect to the server
			socket = new Socket(SERVERIPADDRESS, SERVERPORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Get the available starting locations in an array
	public ArrayList<StartLocation> getAvailableStartLocs() {
		return availableStartLocs;
	}

	//Returns if the current player is the first player or not.
	//This information is fetched from the server
	public boolean isFirstPlayer()
	{
		getStartGameDataFromServer();
		return isFirstPlayer;
	}

	//Private method to get initial data from server. i.e. 
	// boolean flag if this is the first player
	// number of rows, columns for the board (if not first player because its set by the first player only)
	// available starting positions
	private void getStartGameDataFromServer()
	{
		// Create data input and output streams
		try {

			//Data stream to receive boolean flag for isFirstPlayer & rows/columns for the board
			DataInputStream startgameInputFromServer = new DataInputStream(
					socket.getInputStream());
			isFirstPlayer = startgameInputFromServer.readBoolean();
			if(!isFirstPlayer)
			{
				numOfBoardRows = startgameInputFromServer.read();
				numOfBoardCols = startgameInputFromServer.read();
			}

			//Object stream to receive an array of StartLocation
			ObjectInputStream startGameObjectInputFromServer = new ObjectInputStream(socket.getInputStream());

			availableStartLocs = (ArrayList<StartLocation>) startGameObjectInputFromServer.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//get the player status from server. i.e. 
	// if this player is alive
	// has the player won
	private void getPlayerStatusFromServer()
	{
		try {
			// Create data input stream
			DataInputStream playerStatusInputFromServer = new DataInputStream(
					socket.getInputStream());
			isPlayerAlive = playerStatusInputFromServer.readBoolean();
			hasPlayerWon = playerStatusInputFromServer.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//get the player scores from server 
	private void getPlayerScoresFromServer()
	{
		try {
			Object obj = null;
			//keep waiting until the game is not finished and then get the final scores from server
			do
			{
				//Object stream to receive an array of Integer with Player Scores
				ObjectInputStream playerScoresObjectInputFromServer = new ObjectInputStream(socket.getInputStream());

				obj = playerScoresObjectInputFromServer.readObject();
			}while (obj == null);
			scores = (ArrayList<Integer>)obj;
			view.refreshScores(snakeIndex, scores);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//starts the game by using the information passed in the parameters
	public void startGame(boolean isFirstPlayer, int numPlayers, int numRows, int numCols, StartLocation startLoc)
	{
		try {
			// Create data output streams
			DataOutputStream startgameOutputToServer = new DataOutputStream(
					socket.getOutputStream());

			//Send number of players and number of rows & columns to server if this is the first player 
			if(isFirstPlayer)
			{
				this.numOfBoardRows = numRows;
				this.numOfBoardCols = numCols;


				startgameOutputToServer.write(numPlayers);
				startgameOutputToServer.write(numOfBoardRows);
				startgameOutputToServer.write(numOfBoardCols);
			}
			startgameOutputToServer.write(startLoc.getValue());
			startgameOutputToServer.flush();

			boolean waitingForOthersToJoin = true;

			//Set the status bar with current status
			view.setStatusBarText("Waiting for others to join...");

			//Loop to check value from server if all players have joined the game
			// keep waiting until others join
			do
			{
				//input stream to fetch boolean value of others have joined
				DataInputStream startgameInputFromServer = new DataInputStream(
						socket.getInputStream());
				if(startgameInputFromServer != null)
					waitingForOthersToJoin = startgameInputFromServer.readBoolean();

			}while(waitingForOthersToJoin);

			//Set the status bar with current status
			view.setStatusBarText("Game On!!");

			//Get the player status from server. i.e. isPlayerAlive and hasPlayerWon
			getPlayerStatusFromServer();

			//ObjectInputStream inputFromServer;

			//Get the snakeProtocol object from server using the ObjectInputStream
			inputFromServer = new ObjectInputStream(socket.getInputStream());

			Object obj = null;
			//Loop to get snakeProtocol data from server until successful. i.e. until all snakes are created on the server.
			do
			{
				obj = inputFromServer.readObject();
				snakeProtocol =  (SnakeProtocol)obj;
				foodCells = snakeProtocol.getFoodCells();

				snakeIndex = snakeProtocol.getSnakeIndex();
				snakes = snakeProtocol.getSnakes();
				currentDirection = snakes.get(snakeIndex).getSnakeDirection();
				currentSpeed = snakes.get(snakeIndex).getSnakeSpeed();
			}while(obj == null);

			//Initialize the scores
			view.refreshScores(snakeIndex, scores);


			//Initialize the SnakePanel object with the number of rows, columns and Snakes received from the server
			sp = new SnakePanel(numOfBoardRows, numOfBoardCols, snakes);
			sp.setSnakes(snakes);

			//Add the snake panel
			this.add(sp);

			//refresh the board based on latest values from the server 

			refreshBoard();

			//start the timer thread 
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//Refresh board used to update the snake panel with the latest information available from the server
	public void refreshBoard()
	{

		setLayout (new GridLayout(1,1));
		this.setBackground(Color.LIGHT_GRAY);
		//Set updated food cells and snakes for the snake panel object
		sp.setFoodCells(foodCells);
		sp.setSnakes(snakes);
		//Refresh/recreate the board with the updated values of food cells and snakes
		sp.refreshSnakePanel();


		// Create an input stream to receive data from the server
		try {
			// Create an output stream to send data to the server
			outputToServer = new ObjectOutputStream(socket.getOutputStream());

			//Send updated values of foodcells and Snakes (with updated locations/cells of current snake) to the server
			snakeProtocol.setSnakeIndex(snakeIndex);
			snakeProtocol.setFoodCells(foodCells);
			snakeProtocol.setSnakes(snakes);

			outputToServer.writeObject(snakeProtocol);
			outputToServer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setFocusable(true);
		setFocusTraversalKeysEnabled(true);

		//paint the panel to refresh the board
		paintAll(getGraphics()); 

		//Get the player status from server. i.e. isPlayerAlive and hasPlayerWon
		getPlayerStatusFromServer();

		try {
			//Stop the game if player is not alive or has won the game.
			// And update the status in the frame
			if(!isPlayerAlive)
			{
				//remove(sp);
				t.stop();
				view.playerLost();
				//get the final scores and refresh the scorecard 
				getPlayerScoresFromServer();
				return;
			}
			if(hasPlayerWon)
			{
				//remove(sp);
				t.stop();
				view.playerWon();
				//get the final scores and refresh the scorecard 
				getPlayerScoresFromServer();
				return;
			}

			//Else, get latest snakeProtocol data from the server. i.e. 
			// any updates to the food cells and updated snake cell locations for other snakes
			inputFromServer = new ObjectInputStream(socket.getInputStream());

			//TODO
			//Loop until the object is received. Sometimes this may take time due to synchronization code on the server.
			Object obj = null;
			//do
			//{
			obj = inputFromServer.readObject();
			//}while (obj == null);

			snakeProtocol =  (SnakeProtocol)obj;
			foodCells = snakeProtocol.getFoodCells();

			snakes = null;
			snakes = snakeProtocol.getSnakes();
			currentDirection = snakes.get(snakeIndex).getSnakeDirection();
			currentSpeed = snakes.get(snakeIndex).getSnakeSpeed();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//To update the value of current direction based on the key action taken by the user
	public void moveDirection(int step)
	{
		int updatedDirectionVal = step;
		//Reset to 1 when the value of updated direction is more than maximum (i.e. 4) 
		if(currentDirection.getValue()-updatedDirectionVal ==2 ||
				updatedDirectionVal-currentDirection.getValue()==2){
			decreaseSpeed();
			return;  
		}
		//Reset to 4 when the value of updated direction is less than minimum (i.e. 1)
		else if(currentDirection.getValue() == updatedDirectionVal){
			increaseSpeed();
		}
		//Update the current direction of the snake for this player
		currentDirection = directionValueToSnakeDirection(updatedDirectionVal);
		snakes.get(snakeIndex).setSnakeDirection(currentDirection);
	}

	//Utility function to convert direction integer value into SnakeDirection enum
	private SnakeDirection directionValueToSnakeDirection(int val)
	{
		if(val == SnakeDirection.RIGHT.getValue())
			return SnakeDirection.RIGHT;
		else if(val == SnakeDirection.UP.getValue())
			return SnakeDirection.UP;
		else if(val == SnakeDirection.DOWN.getValue())
			return SnakeDirection.DOWN;
		else if(val == SnakeDirection.LEFT.getValue())
			return SnakeDirection.LEFT;
		return SnakeDirection.DOWN; 
	}




	//method to increase the speed. i.e. 
	// double the speed when used each time with a maximum of 2 times
	public void increaseSpeed()
	{
		//Do nothing if maximum speed is achieved
		if(currentSpeed == SnakeSpeed.MAX)
			return;
		//Else, increase the speed to next level
		else if(currentSpeed == SnakeSpeed.DEFAULT)
		{
			currentSpeed = SnakeSpeed.MEDIUM;
		}
		else if(currentSpeed == SnakeSpeed.MEDIUM)
		{
			currentSpeed = SnakeSpeed.MAX;
		}

		//Set the updated speed for the snake of the current player  
		snakes.get(snakeIndex).setSnakeSpeed(currentSpeed);
	}
	//decreaseSpeed to decrease the speed when the reverse button is pushed of which direction the snake is heading in
	public void decreaseSpeed()
	{

		if(currentSpeed == SnakeSpeed.MAX)
			currentSpeed = SnakeSpeed.MEDIUM;

		else if(currentSpeed == SnakeSpeed.DEFAULT)
		{
			return;
		}
		else if(currentSpeed == SnakeSpeed.MEDIUM)
		{
			currentSpeed = SnakeSpeed.DEFAULT;
		}


		snakes.get(snakeIndex).setSnakeSpeed(currentSpeed);
	}

	//Reset the speed for current player snake to default/minimum 
	public void resetSpeed()
	{
		currentSpeed = SnakeSpeed.DEFAULT;
		snakes.get(snakeIndex).setSnakeSpeed(currentSpeed);
	}


	@Override
	//Event handler for the Timer action
	public void actionPerformed(ActionEvent e) {
		//Move the snake for each time action
		moveSnake();
	}

	//Updates the snake cells for all the snakes based on their latest snake cells, current speed & current direction
	// Also updates the food cells & snake cells (for current player) if the current player's snake eats any food item
	private void moveSnake()

	{

		//Get the lower & upper limit for the last row of the board. 
		// e.g. for a 20x20 board, these values would be 380 & 400 respectively
		int lowerLimitOfLastRow = numOfBoardRows*numOfBoardCols-numOfBoardCols;
		int upperLimitOfLastRow = numOfBoardRows*numOfBoardCols;

		//Loop through all snakes to update their location
		for(Snake s: snakes)
		{
			//Do nothing if snake is not alive
			if(!s.isSnakeAlive())
				continue;
			//Move the snake number of cells depending on its current speed. i.e. 
			// snake with minimum/default speed will move only one cell while with maximum speed will move 4 cells during the same time.   
			for(int i=s.getSnakeSpeed().getValue(); i<= SnakeSpeed.DEFAULT.getValue(); i=i*2)
			{
				int lastCellLocation = -1;
				SnakeDirection sd = s.getSnakeDirection();
				ArrayList<Integer> scells = s.getSnakeCells();

				if(s.getSnakeIndex() == snakeIndex)
				{
					lastCellLocation = scells.get(scells.size()-1);
				}


				//Move the cells forward except the first one
				for(int j=scells.size()-1;j>0;j--)
				{
					scells.set(j, scells.get(j-1));         
				}

				//Update the first cell based on the current direction of the snake
				if(sd == SnakeDirection.DOWN)
				{
					//Set the value of first cell as the cell in the same column but first row when the current cell is in the last row  
					if(scells.get(0) >=  lowerLimitOfLastRow && scells.get(0) < upperLimitOfLastRow)
						scells.set(0, scells.get(0) - lowerLimitOfLastRow);
					//Else, Set the value of first cell as the cell under the current cell in the next row 
					else
						scells.set(0, scells.get(0) + numOfBoardCols);
				}
				else if(sd == SnakeDirection.UP)
				{
					//Set the value of first cell as the cell in the same column but last row when the current cell is in the first row  
					if(scells.get(0) >= 0 && scells.get(0) < numOfBoardCols)
						scells.set(0, scells.get(0) + lowerLimitOfLastRow);
					//Else, Set the value of first cell as the cell above the current cell in the next row 
					else
						scells.set(0, scells.get(0) - numOfBoardCols);
				}
				else if(sd == SnakeDirection.RIGHT)
				{
					//Set the value of first cell as the current cell value minus the number of columns when the current cell is in the last column  
					if(scells.get(0)%numOfBoardCols == numOfBoardCols - 1)
						scells.set(0, scells.get(0) - (numOfBoardCols - 1));
					//Else, Set the value of first cell as the current value incremented by one 
					else
						scells.set(0, scells.get(0) + 1);
				}
				else if(sd == SnakeDirection.LEFT)
				{
					//Set the value of first cell as the current cell value plus the number of columns when the current cell is in the first column  
					if(scells.get(0)%numOfBoardCols == 0)
						scells.set(0, scells.get(0) + numOfBoardCols -1);
					//Else, Set the value of first cell as the current value decremented by one 
					else
						scells.set(0, scells.get(0) - 1);
				}


				//Update snake length & food cells if its the current player's snake and there's a food item at the same location as snake's head
				if(s.getSnakeIndex() == snakeIndex) 
				{
					if(foodCells.contains(scells.get(0)))
					{
						scells.add(lastCellLocation);
						foodCells.remove(scells.get(0));
					}         
				}
				s.setSnakeCells(scells);

			}
		}
		//refresh the board to send/receive latest snakeProtocol object between client/server and also update the board
		refreshBoard();
	}

	public SnakePanel getSnakePanel(){

		return sp;
	}
}


