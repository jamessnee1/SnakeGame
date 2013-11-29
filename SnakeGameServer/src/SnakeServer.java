import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import model.Snake;
import model.SnakeDirection;
import model.SnakeProtocol;
import model.SnakeSpeed;
import model.StartLocation;

//Server class that will initiate the socket and will hold the shared resources
//By Jagdeep Kaur

public class SnakeServer extends JFrame {
	private static final long serialVersionUID = 1L;
	// Text area for displaying contents
	private JTextArea jta = new JTextArea();
	//ArrayList of Snake objects that will be shared between all Clients
	public static ArrayList<Snake> snakeArrayList = new ArrayList<Snake>(); 
	//ArrayList of Integer objects to hold the locations for food items and will be shared between all Clients
	private ArrayList<Integer> foodArrayList = new ArrayList<Integer>();
	//ArrayList of Integer objects to hold the available start locations for each client
	// this list will keep on shrinking as the client players join
	private ArrayList<StartLocation> availableStartLocations = new ArrayList<StartLocation>();
	//ArrayList of Integer objects to hold the final scores for the game
	private ArrayList<Integer> scoresArrayList = new ArrayList<Integer>();
	//ArrayList of ClientHandlingThread objects to manage communications with each Client 
	// (i.e. one thread object per Client)
	private ArrayList<ClientHandlingThread> clientList = new ArrayList<ClientHandlingThread>();

	//variables to hold the number of rows/columns on the board - 
	// these values are selected by the first player but are passed from server to others
	private int numRowsOfBoard = 50;
	private int numColsOfBoard = 50;

	//variable to determine how many players are going to be involved in the game - 
	// selected by the first player
	private int numOfPlayers = -1;

	private final int WINSCORE = 1;
	private final int LOSESCORE = 0;
	private final int MAXFOODITEMS = 5;

	//Start the server
	public static void main(String[] args) {
		new SnakeServer();
	}

	//Setter for NumOfPlayers property
	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
	}

	//Method to create list of available starting locations when server starts
	public void createInitialAvailableStartLocations()
	{
		//Add all possible starting locations
		availableStartLocations.add(StartLocation.TOPLEFT);
		availableStartLocations.add(StartLocation.TOPRIGHT);
		availableStartLocations.add(StartLocation.BOTTOMLEFT);
		availableStartLocations.add(StartLocation.BOTTOMRIGHT);
	}

	//Constructor
	public SnakeServer () {
		// Place text area on the frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(jta), BorderLayout.CENTER);

		//Set the title and other relevant properties of the frame
		setTitle("Snake Server");    setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // It is necessary to show the frame here!

		//create an initial list of available locations
		createInitialAvailableStartLocations();

		jta.append("food items - " + foodArrayList.size() + "\n");

		try {
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			jta.append("SnakeServer started at " + new Date() + '\n');

			int clientCount = 1;
			//Wait for all players to join - specified by numOfPlayers (received from first player) 
			do{
				// Listen for a connection request
				Socket socket = serverSocket.accept();

				jta.append("Starting thread for client " + clientCount +
						" at " + new Date() + "\n");
				InetAddress inetAddress = socket.getInetAddress();        
				jta.append("Client " + clientCount +
						" host address is  " + inetAddress.getHostAddress() + "\n");
				//create a thread object for each client/player
				ClientHandlingThread thread = new ClientHandlingThread(socket);
				clientList.add(thread);
				//if the numOfPlayers variable is still -1, then its the first player
				boolean bFirstPlayer = (numOfPlayers == -1);
				//initiate the game for the client/player
				thread.startGame(bFirstPlayer);
				jta.append("numOfPlayers - " + numOfPlayers + "\n");

				//increment the player counter
				clientCount++;
			}while (clientCount <= numOfPlayers);


			//create the initial food items to be sent to all the clients
			createFoodItems();

			//For each client - start the game
			for(ClientHandlingThread cht: clientList)
			{
				//set the food items for each client using the shared foodArrayList
				cht.setFoodList(foodArrayList);
				cht.start();
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}

	//Method to create the food items at random locations
	// - Maximum of 5 at any given time
	// - Excluding the locations already occupied by the snakes
	public synchronized void createFoodItems()
	{
		//flag to check if a new food has been added 
		boolean foodAdded = false;

		//loop until food items are equal to MAXFOODITEMS (5)
		while(foodArrayList.size() < MAXFOODITEMS)
		{
			//get a random location based on the size of the board
			Random rand = new Random();
			int loc = rand.nextInt(numRowsOfBoard * numColsOfBoard);

			//flag to check if a snake cell exist at a given location 
			boolean snakecellexist = false;

			//process if the randomly generated location is not already a fooditem
			// else repeat
			if(!foodArrayList.contains(loc))
			{
				//check if there is a snakecell at the generated random location
				for(Snake s : snakeArrayList)
					if(s.getSnakeCells().contains(loc))
					{
						snakecellexist = true;
						break;
					}
				//if no snakecell at the given location then add it the shared food array list
				if(!snakecellexist)
					foodArrayList.add(loc);
			}
			//set food added flag to true
			foodAdded = true;
		}
		//set the local fooArrayList objects of each client when new food is created 
		if(foodAdded)
			for(ClientHandlingThread cht: clientList)
			{
				cht.setFoodList(foodArrayList);
			}

	}

	//Setting the winner in the game by assigning 1 to the player passed as parameters 
	private void setWinner(int index)
	{
		for(int i=0; i<numOfPlayers; i++)
		{
			if(i==index)
				scoresArrayList.add(WINSCORE);
			else
				scoresArrayList.add(LOSESCORE);
		}
	}

	//Method to kill the snake when - 
	// - the current player's snake hits itself 
	//    - current player will be marked dead
	// - the current snake hits other snake
	//    - if current player is greater in length than other snake, the other player is marked dead
	//    - if current player is less in length than other snake, the current player is marked dead
	//    - when both the collided snakes are of equal length, mark both dead
	public synchronized void killSnake(int snakeIndex)
	{
		//get the head location of the current player's snake
		Integer currentSnakeHeadLocation = snakeArrayList.get(snakeIndex).getSnakeCells().get(0);
		//get the length of the current player's snake
		int currentSnakeLength = snakeArrayList.get(snakeIndex).getSnakeCells().size();

		//loop through all the snakes
		for(Snake s : snakeArrayList)
		{
			//get next snake if the snake is already dead 
			if(!s.isSnakeAlive())
				continue;
			//if it's the current player's snake 
			if(s.getSnakeIndex() == snakeIndex)
			{
				//check if any of the snake cell location is repeated and hence kill the snake
				for(int i=0; i< s.getSnakeCells().size(); i++)
					if(s.getSnakeCells().indexOf(s.getSnakeCells().get(i)) != i && s.getSnakeCells().indexOf(s.getSnakeCells().get(i)) >=0)
					{
						jta.append("Matched SELF snake\n");
						//mark the current player's snake as dead and exit the method 
						snakeArrayList.get(snakeIndex).setSnakeAlive(false);
						return;
					}
			}
			//if the current snake's head matches with any of the locations of other player's snake. i.e. collision detected
			else if(s.getSnakeCells().contains(currentSnakeHeadLocation))
			{
				jta.append("Matched snake - " + s.getSnakeIndex() + "\n");
				//if other player's snake length is greater than current player's snake  
				if(s.getSnakeCells().size() > currentSnakeLength)
				{
					//mark the current player's snake as dead and exit the method 
					snakeArrayList.get(snakeIndex).setSnakeAlive(false);
					jta.append("killing snake - " + snakeIndex);
				}
				//if other player's snake length is equal to the current player's snake  
				else if(s.getSnakeCells().size() == currentSnakeLength)
				{
					//mark both the snakes as dead 
					snakeArrayList.get(snakeIndex).setSnakeAlive(false);
					s.setSnakeAlive(false);

					jta.append("killing snakes - " + snakeIndex + " and  " + s.getSnakeIndex());
				}
				//if other player's snake length is less than current player's snake  
				else
				{
					//mark the other player's snake as dead and exit the method 
					s.setSnakeAlive(false);
					jta.append("killing snake - " + s.getSnakeIndex() + "\n");
				}
				break;
			}
		}         
	}

	//Method to create a new player (i.e. Snake object) based on the StartLocation passed 
	// and add it to the shared snakeArrayList object.
	// - returns the index allocated to the new snake
	public synchronized int createNewPlayer(StartLocation startLoc)
	{
		int snakeIndex; 
		StartLocation sl = startLoc; 
		Color snakeColor;
		SnakeDirection sd = null;
		//allocate the snakeIndex and color based on the number of snakes already joined
		switch (snakeArrayList.size())
		{
		case 0:
			snakeIndex = 0;
			snakeColor = Color.RED;
			break;
		case 1:
			snakeIndex = 1;
			snakeColor = Color.BLUE;
			break;
		case 2:
			snakeIndex = 2;
			snakeColor = Color.GREEN;
			break;
		case 3:
			return -1; //Limit reached 
		default:
			snakeIndex = 3;
			snakeColor = Color.DARK_GRAY;
			break;
		}

		//define a new Snake object
		Snake s = new Snake();
		//define a new Integer Array to hold initial snake cells locations
		ArrayList<Integer> snakeLocs = new ArrayList<Integer>();

		//add two snakecells to the new snake based on the starting location of the snake  
		if(sl == StartLocation.TOPLEFT)
		{
			sd = SnakeDirection.DOWN;
			snakeLocs.add(numColsOfBoard);
			snakeLocs.add(0);
		}
		else if(sl == StartLocation.BOTTOMLEFT)
		{
			sd = SnakeDirection.UP;
			snakeLocs.add(((numRowsOfBoard-1) * numColsOfBoard) - numColsOfBoard -1);
			snakeLocs.add((numRowsOfBoard * numColsOfBoard) - numColsOfBoard -1);
		}
		else if(sl == StartLocation.TOPRIGHT)
		{
			sd = SnakeDirection.DOWN;
			snakeLocs.add(2 * numColsOfBoard -1);
			snakeLocs.add(numColsOfBoard -1);
		}
		else if(sl == StartLocation.BOTTOMRIGHT)
		{
			sd = SnakeDirection.UP;
			snakeLocs.add(((numRowsOfBoard-1) * numColsOfBoard) - 1);
			snakeLocs.add((numRowsOfBoard * numColsOfBoard) - 1);
		}

		//set all the attributes for the new snake   
		s.setSnakeIndex(snakeIndex);
		s.setSnakeColor(snakeColor);
		s.setSnakeStartLocation(sl);
		s.setSnakeDirection(sd);
		s.setSnakeCells(snakeLocs);

		//add the new snake to the shared snakeArrayList object
		snakeArrayList.add(s);

		//return the index for the new snake
		return snakeIndex;
	}

	//Method used to inform all players about the game is finished and the final scores can be sent
	public void sendScoresToClients()
	{
		for(ClientHandlingThread cht: clientList)
		{
			cht.setCanSendScores(true);
		}
	}

	//Inner class to handle communication with each player 
	class ClientHandlingThread extends Thread  // inner class
	{
		//socket to communicate with the client
		private Socket socket;
		//SnakeProtocol object to send relevant information between client and server
		private SnakeProtocol snakeProtocol = new SnakeProtocol();
		//snake index for the player
		private int snakeIndex = -1;
		//flag to check if the current player is still alive
		private boolean playerAlive = true;
		//flag to check if the current player has won
		private boolean playerWon = false;

		//Object streams to send/receive data to/from the client 
		private ObjectOutputStream outputToClient;
		private ObjectInputStream inputFromClient;

		//local ArrayList objects to hold the copy of all the snakes and food items
		// - these are synced with the shared resources defined in the SnakeServer class
		private ArrayList<Integer> foodList = new ArrayList<Integer>();
		private ArrayList<Snake> snakeList = new ArrayList<Snake>();

		//hold the speed for the current player
		private SnakeSpeed snakeSpeed;
		//hold the starting location for the current player
		private StartLocation snakeStartLoc = StartLocation.TOPLEFT;

		//flag to check if the scores can be sent to the client
		private boolean canSendScores = false;

		//constructor
		public ClientHandlingThread(Socket socket)
		{  
			this.socket = socket;
		}

		//Setter for the Snake Start Location
		// also create a new Snake for this player and assign the index at the same time
		public void setSnakeStartLoc(StartLocation snakeStartLoc) {
			this.snakeStartLoc = snakeStartLoc;
			this.snakeIndex = createNewPlayer(snakeStartLoc);
		}

		//Accessor for the Snake Index
		public int getSnakeIndex() {
			return snakeIndex;
		}

		//Setter for the local Snake ArrayList List object
		public void setSnakeList(ArrayList<Snake> snakeList) {
			this.snakeList = snakeList;
		}

		//Setter for the local food ArrayList List object
		public void setFoodList(ArrayList<Integer> foodList) {
			this.foodList = foodList;
		}

		//Setter for the canSendScores flag
		public void setCanSendScores(boolean canSendScores) {
			this.canSendScores = canSendScores;
		}


		//Method to communicate with the client when the game is started and initialize all the required objects
		public void startGame(boolean isFirstPlayer)
		{
			try {
				// Create data output stream to send following information
				// - A flag if the current player is the first player to join the game
				// - If not first player, then also send the number of rows & columns needed for each client 
				DataOutputStream startgameOutputToClient = new DataOutputStream(
						socket.getOutputStream());

				startgameOutputToClient.writeBoolean(isFirstPlayer);
				if(!isFirstPlayer)
				{
					startgameOutputToClient.write(numRowsOfBoard);
					startgameOutputToClient.write(numColsOfBoard);
				}

				// Create Object output stream to send following information
				// - list of all the available starting locations 
				ObjectOutputStream startgameOutputObjectToClient = new ObjectOutputStream(
						socket.getOutputStream());

				startgameOutputObjectToClient.writeObject(availableStartLocations);

				// Create data input stream to receive the following information
				// - If first player, then 
				//       - receive the number of players for the game 
				//       - receive the number of rows & columns needed for each client 
				// - receive the start location selected by the player 
				DataInputStream startgameInputFromClient = new DataInputStream(
						socket.getInputStream());
				if(isFirstPlayer)
				{
					int numPlayers = -1;
					//Keep waiting until the information is received from the first client/player 
					do
					{
						numPlayers = startgameInputFromClient.read();
						numRowsOfBoard = startgameInputFromClient.read();
						numColsOfBoard = startgameInputFromClient.read();

						//Set the number of players for the SnakeServer object
						setNumOfPlayers(numPlayers);
					}while(numPlayers == -1);
				}
				int startLoc = startgameInputFromClient.read();
				//set the received start location for the player on the server side
				setSnakeStartLoc(getLocation(startLoc));

				jta.append("Starting location for Snake " + snakeIndex + " is - " + startLoc + "\n");

				//remove the already occupied Start Location from the shared ArrayList of start locations
				synchronized (availableStartLocations) {
					for(StartLocation sl: availableStartLocations)
					{
						if(sl.getValue() == startLoc)
						{
							availableStartLocations.remove(sl);
							break;
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//run method when the Thread starts
		public void run()
		{
			try {
				// Create data output stream to send following information
				// - A flag if the current player needs to wait for others to join
				DataOutputStream startgameOutputToClient = new DataOutputStream(
						socket.getOutputStream());

				startgameOutputToClient.writeBoolean(false);
				startgameOutputToClient.flush();
				jta.append("Sent false to - " + snakeIndex + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			//Set the snake index within the SnakeProtocol object used for this player
			snakeProtocol.setSnakeIndex(snakeIndex);

			//Send the status of the current player
			sendPlayerStatus();

			//Game on while the player is alive
			do {
				//Send and receive game data between server and client
				sendAndReceiveData();

				//Send the status of the current player
				sendPlayerStatus();

				//if the player has won, then set the current player as winner and break out of the loop
				if(playerWon)
				{
					//set the current player as winner
					setWinner(snakeIndex);
					break;
				}

				//Introduce a sleep of the maximum possible speed a snake can travel
				// this will help utilize less resources like CPU/Memory on the server
				try {
					this.sleep(SnakeSpeed.MAX.getValue());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}while (playerAlive);

			//Send notification to all the clients to send the final scores 
			if(playerWon)
				sendScoresToClients();

			//Wait until the game is still on
			while(!canSendScores)
			{
				//don't do anything until it's ready to send the scores
			}

			//Send scores to the current player
			sendScores();
		}

		//Method to send & receive data between server and client while the game is running
		private void sendAndReceiveData()
		{
			try {
				//Set the food cells array within the Snake Protocol object 
				snakeProtocol.setFoodCells(foodList);

				//Set the Snakes Array within the Snake Protocol object 
				synchronized(snakeArrayList)
				{
					snakeProtocol.setSnakes(snakeArrayList);
				}

				// Create object output streams to send snakeProtocol object to the client
				outputToClient = new ObjectOutputStream(
						socket.getOutputStream());

				// Send snake protocol to the client
				outputToClient.writeObject(snakeProtocol);
				outputToClient.flush();

				// Create object input streams to receive snakeProtocol object from the client
				inputFromClient = new ObjectInputStream(
						socket.getInputStream());

				// Receive updated foodcells
				snakeProtocol = (SnakeProtocol)inputFromClient.readObject();
				synchronized (foodArrayList) {
					foodArrayList = snakeProtocol.getFoodCells();
				}
				// Receive updated snake ArrayList and refresh the local objects
				synchronized (snakeArrayList) {
					snakeList.clear();
					snakeList = snakeProtocol.getSnakes();
					snakeSpeed = snakeList.get(snakeIndex).getSnakeSpeed();
					snakeArrayList.get(snakeIndex).setSnakeCells(snakeList.get(snakeIndex).getSnakeCells());
					snakeArrayList.get(snakeIndex).setSnakeDirection(snakeList.get(snakeIndex).getSnakeDirection());
					snakeArrayList.get(snakeIndex).setSnakeSpeed(snakeSpeed);
				}
			}
			catch(IOException ex) {
				System.err.println(ex);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			//invoke createFoodItems method to create new food items if necessary
			createFoodItems();
			//invoke killSnake method to kill the snake if there's a collision
			killSnake(snakeIndex);

			//set the local flag if the player is still alive 
			synchronized (snakeArrayList) {
				playerAlive = snakeArrayList.get(snakeIndex).isSnakeAlive();
			}
		}

		//Method to send the player staus to the client
		// - if player is alive
		// - has the player won
		public void sendPlayerStatus()
		{
			// Create data output stream to send following information
			// - A flag if the current player is alive
			// - A flag if the current player has won
			DataOutputStream playerStatusOutputToClient;
			try {
				playerStatusOutputToClient = new DataOutputStream(
						socket.getOutputStream());
				playerStatusOutputToClient.writeBoolean(playerAlive);

				//check if the player has won the game
				if(!playerAlive)
				{
					playerWon = false;
				}
				else
				{
					int counter = 0;
					synchronized (snakeArrayList) {
						for(Snake s : snakeArrayList)
						{
							if(s.isSnakeAlive())
								counter++;
						}
					}
					if(counter > 1)
						playerWon = false;
					else
						playerWon = true;
				}
				playerStatusOutputToClient.writeBoolean(playerWon);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Utility method to convert the int value into a StartLocation enum
		public StartLocation getLocation(int startLoc)
		{
			if (startLoc == StartLocation.TOPLEFT.getValue())
				return StartLocation.TOPLEFT;
			if (startLoc == StartLocation.TOPRIGHT.getValue())
				return StartLocation.TOPRIGHT;
			if (startLoc == StartLocation.BOTTOMLEFT.getValue())
				return StartLocation.BOTTOMLEFT;
			if (startLoc == StartLocation.BOTTOMRIGHT.getValue())
				return StartLocation.BOTTOMRIGHT;
			return StartLocation.TOPLEFT;
		}

		//Method to send the final scores to the client
		public void sendScores()
		{
			try {
				// Create object output stream to send following information
				// - the scoresArrayList object with the final scores
				ObjectOutputStream playerScoresOutputObjectToClient = new ObjectOutputStream(
						socket.getOutputStream());

				playerScoresOutputObjectToClient.writeObject(scoresArrayList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
