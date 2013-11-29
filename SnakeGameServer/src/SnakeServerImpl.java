import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serializable;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import snakeinterface.INotifySnakeClient;
import snakeinterface.ISnakeServer;

import model.Snake;
import model.SnakeDirection;
import model.SnakeProtocol;
import model.StartLocation;
import model.UserScore;
import model.constants.Constants;
import model.exception.SnakeException;

public class SnakeServerImpl extends UnicastRemoteObject implements
      ISnakeServer, Serializable {
   private static final long serialVersionUID = 1L;
   // Local constants
   private static final int FRAMEHEIGHT = 300;
   private static final int FRAMEWIDTH = 500;

   JFrame frame = new JFrame();
   // Text area for displaying contents
   private JTextArea jta = new JTextArea();
   // ArrayList of Snake objects that will be shared between all Clients
   public static ArrayList<Snake> snakeArrayList = new ArrayList<Snake>();
   // ArrayList of Integer objects to hold the locations for food items and will
   // be shared between all Clients
   public ArrayList<Integer> foodArrayList;
   // ArrayList of Integer objects to hold the available start locations for
   // each client
   // this list will keep on shrinking as the client players join
   private ArrayList<StartLocation> availableStartLocations = new ArrayList<StartLocation>();
   // Hashmap of ClientHandlingThread objects to manage communications with each
   // Client
   // (i.e. one thread object per Client)
   public HashMap<Integer, INotifySnakeClient> clientList;
   // Hashmap of Usernames currently in the game
   public HashMap<Integer, UserScore> userscores;//

   private HashMap<Integer, SnakeProtocol> snakeProtocols = new HashMap<Integer, SnakeProtocol>();

   // variables to hold the number of rows/columns on the board -
   // these values are selected by the first player but are passed from server
   // to others
   private int numRowsOfBoard = 50;
   private int numColsOfBoard = 50;

   // variable to determine how many players are going to be involved in the
   // game -
   // selected by the first player
   private int numOfPlayers = -1;

   private int gameId = -1;
   private int score = 1;
   private final int SCOREINCREMENT = 1;
   private final int MAXFOODITEMS = 5;

   // Database connection object
   private Connection dbConnection;

   // Method to create list of available starting locations when server starts
   private void createInitialAvailableStartLocations() {
      // Add all possible starting locations
      availableStartLocations.add(StartLocation.TOPLEFT);
      availableStartLocations.add(StartLocation.TOPRIGHT);
      availableStartLocations.add(StartLocation.BOTTOMLEFT);
      availableStartLocations.add(StartLocation.BOTTOMRIGHT);
   }

   // Main method to start the server application
   public static void main(String[] args) {
      try {
         // Create a new RMI Registry
         Registry registry = LocateRegistry
               .createRegistry(Constants.SERVERPORT);

         // Set the new security manager for the system
         System.setSecurityManager(new RMISecurityManager());

         // Create a new SnakeServerImpl object that will be used by clients to
         // invoke server methods
         ISnakeServer server = new SnakeServerImpl();

         // Bind the SnakeServerImpl object to the registry using a service name
         registry.rebind(Constants.RMISNAKESERVICENAME, server);

         System.out.println("The Snake server is running...");
      } catch (RemoteException e) {
         e.printStackTrace();
      }

   }

   // Constructor
   public SnakeServerImpl() throws RemoteException {
      super();

      // Initialise the various objects needed by the Server
      foodArrayList = new ArrayList<Integer>();
      clientList = new HashMap<Integer, INotifySnakeClient>();
      userscores = new HashMap<Integer, UserScore>();

      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(new JScrollPane(jta), BorderLayout.CENTER);
      // Set the title and other relevant properties of the frame
      frame.setTitle("Snake Server");
      frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true); // It is necessary to show the frame here!

      try {
         // Create a connection to the database
         dbConnection = DBUtil.Connect();

      } catch (SQLException e) {
         e.printStackTrace();
         jta.append("Error starting the SnakeServer... try again");
      }

      // create an initial list of available locations
      createInitialAvailableStartLocations();

      jta.append("SnakeServer started at " + new Date() + "\n");
   }

   // Method to create the food items at random locations
   // - Maximum of 5 at any given time
   // - Excluding the locations already occupied by the snakes
   private synchronized void createFoodItems() {
      System.out.println("numRowsOfBoard - " + numRowsOfBoard
            + " numColsOfBoard - " + numColsOfBoard);
      jta.append("numRowsOfBoard - " + numRowsOfBoard + " numColsOfBoard - "
            + numColsOfBoard + "\n");

      // loop until food items are equal to MAXFOODITEMS (5)
      while (foodArrayList.size() < MAXFOODITEMS) {
         // get a random location based on the size of the board
         Random rand = new Random();
         int loc = rand.nextInt(numRowsOfBoard * numColsOfBoard);

         // flag to check if a snake cell exist at a given location
         boolean snakecellexist = false;

         // process if the randomly generated location is not already a fooditem
         // else repeat
         if (!foodArrayList.contains(loc)) {
            // check if there is a snakecell at the generated random location
            for (Snake s : snakeArrayList)
               if (s.getSnakeCells().contains(loc)) {
                  snakecellexist = true;
                  break;
               }
            // if no snakecell at the given location then add it the shared food
            // array list
            if (!snakecellexist) {
               foodArrayList.add(loc);
               System.out.println("food item added - " + loc);
               jta.append("food item added - " + loc + "\n");
            }
         }
      }
   }

   // Method to create a new player (i.e. Snake object) based on the
   // StartLocation passed
   // and add it to the shared snakeArrayList object.
   // - returns the index allocated to the new snake
   private synchronized int createNewPlayer(StartLocation startLoc) {
      int snakeIndex;
      StartLocation sl = startLoc;
      Color snakeColor;
      SnakeDirection sd = null;
      // allocate the snakeIndex and color based on the number of snakes already
      // joined
      switch (snakeArrayList.size()) {
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
         snakeIndex = 3;
         snakeColor = Color.DARK_GRAY;
         break;
      default:
         return -1; // Limit reached
      }

      // define a new Snake object
      Snake s = new Snake();
      // define a new Integer Array to hold initial snake cells locations
      ArrayList<Integer> snakeLocs = new ArrayList<Integer>();

      // add two snakecells to the new snake based on the starting location of
      // the snake
      if (sl == StartLocation.TOPLEFT) {
         sd = SnakeDirection.DOWN;
         snakeLocs.add(numColsOfBoard);
         snakeLocs.add(0);
      } else if (sl == StartLocation.BOTTOMLEFT) {
         sd = SnakeDirection.UP;
         snakeLocs.add(((numRowsOfBoard - 1) * numColsOfBoard) - numColsOfBoard
               - 1);
         snakeLocs.add((numRowsOfBoard * numColsOfBoard) - numColsOfBoard - 1);
      } else if (sl == StartLocation.TOPRIGHT) {
         sd = SnakeDirection.DOWN;
         snakeLocs.add(2 * numColsOfBoard - 1);
         snakeLocs.add(numColsOfBoard - 1);
      } else if (sl == StartLocation.BOTTOMRIGHT) {
         sd = SnakeDirection.UP;
         snakeLocs.add(((numRowsOfBoard - 1) * numColsOfBoard) - 1);
         snakeLocs.add((numRowsOfBoard * numColsOfBoard) - 1);
      }

      // set all the attributes for the new snake
      s.setSnakeIndex(snakeIndex);
      s.setSnakeColor(snakeColor);
      s.setSnakeStartLocation(sl);
      s.setSnakeDirection(sd);
      s.setSnakeCells(snakeLocs);

      // add the new snake to the shared snakeArrayList object
      snakeArrayList.add(s);

      // remove the available start location from the ArrayList
      availableStartLocations.remove(startLoc);

      // return the index for the new snake
      return snakeIndex;
   }

   // Method to initialise the snakeProtocol object for all the connected
   // clients.
   // This is used when game is started and uses the notifySnakeProtocol
   // callback method to send this info to all clients
   private void initialiseSnakeProtocols() {
      for (Integer index : clientList.keySet()) {
         SnakeProtocol sp = new SnakeProtocol();
         sp.setSnakeIndex(index);
         sp.setFoodCells(foodArrayList);
         sp.setSnakes(snakeArrayList);

         this.snakeProtocols.put(index, sp);

         INotifySnakeClient client = clientList.get(index);
         try {
            client.notifySnakeProtocol(sp);
         } catch (RemoteException e) {
            e.printStackTrace();
         }
         sp = null;
      }
   }

   // Method to inform the game parameters (rows, cols & players) to all clients
   private void updateGameParameters() {
      for (Integer snakeIndex : clientList.keySet()) {
         INotifySnakeClient client = clientList.get(snakeIndex);
         try {
            client.notifyGameParameters(numRowsOfBoard, numColsOfBoard,
                  numOfPlayers);
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      }
   }

   // Method to notify the scores to all the clients
   // uses the notifyUserScores callback method
   private void notifyScores() {
      for (Integer snakeIndex : clientList.keySet()) {
         INotifySnakeClient client = clientList.get(snakeIndex);
         try {
            client.notifyUserScores(userscores);
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      }
   }

   // Method to refresh the various objects needed by server when the game is
   // finished and server is ready to start new game
   private void refreshServerForNewGame() {
      foodArrayList.clear();
      clientList.clear();
      userscores.clear();
      availableStartLocations.clear();
      createInitialAvailableStartLocations();

      this.numOfPlayers = -1;
      this.numRowsOfBoard = 50;
      this.numColsOfBoard = 50;
      this.gameId = -1;
   }

   // Created the new user in the database with the values passed in parameters
   @Override
   public boolean createNewUser(String fName, String lName, String address,
         String phoneNum, String username, String password)
         throws RemoteException, SnakeException {
      boolean bUserExists = false;
      boolean bUserCreated = false;
      try {

         // check if the user does not already exist in the database
         bUserExists = DBUtil.checkUsername(dbConnection, username);
         if (bUserExists)
            throw new SnakeException("Username " + username
                  + " already exists!");

         // if check passes, create new entry in database
         bUserCreated = DBUtil.insertUser(dbConnection, fName, lName, address,
               phoneNum, username, password);
         if (bUserCreated) {
            jta.append("New User successfully created!\n");
            System.out.println("New User successfully created!");
         }
      } catch (SQLException e2) {
         System.out.println(Constants.REGISTERUSERERROR);
         DBUtil.processException(e2);
         throw new SnakeException(Constants.REGISTERUSERERROR);
      }
      return bUserCreated;
   }

   // Registers the user for the game
   @Override
   public synchronized void joinGame(INotifySnakeClient client,
         String username, String password, int numPlayers, int numOfBoardRows,
         int numOfBoardCols, StartLocation startLoc) throws RemoteException,
         SnakeException {
      // reject join request if a game is in progress
      if (gameId != -1)
         throw (new SnakeException(Constants.SERVERBUSYERROR));

      // reject join request if the username has already joined the game
      for (UserScore uscore : userscores.values())
         if (uscore.getUsername().equals(username))
            throw (new SnakeException(Constants.USERALREADYINGAMEERROR));

      boolean bLoginSuccessful = false;
      try {
         bLoginSuccessful = DBUtil.checkLogin(dbConnection, username, password);
      } catch (SQLException e) {
         e.printStackTrace();
         throw (new SnakeException(Constants.LOGINSERVERERROR));
      }
      if (!bLoginSuccessful) {
         throw (new SnakeException(Constants.INVALIDUSERNAMEPASSWORDERROR));
      }
      System.out.println("this.numOfPlayers - " + numOfPlayers);
      System.out.println("foodArrayList.size() - " + foodArrayList.size());

      // If first player then set all the numOfPlayers, numRowsOfBoard &
      // numOfColsOfBoard attributes
      // And also create the food items
      if (clientList.size() == 0) {
         this.numOfPlayers = numPlayers;
         this.numRowsOfBoard = numOfBoardRows;
         this.numColsOfBoard = numOfBoardCols;
         // create the initial food items to be sent to all the clients
         createFoodItems();
      }

      // Create the snake player for the user joining the game
      int index = createNewPlayer(startLoc);

      // Add the callback object for this player in the arraylist
      clientList.put(index, client);
      System.out.println("clientList index - " + index);

      // Create a initial score for this user and add it into the userscores
      // collection
      UserScore uscore = new UserScore();
      uscore.setUsername(username);
      uscore.setScore(0);
      userscores.put(index, uscore);

      System.out.println("username added - " + username);

      // Send chat message to all clients informing that username has joined the
      // game
      sendChatMessage(username, "has joined the game");

      System.out.println("clientList.size() - " + clientList.size()
            + " this.numOfPlayers - " + this.numOfPlayers);

      // Start the game when all players have joined successfully
      if (clientList.size() == this.numOfPlayers) {
         try {
            // Get the maximum game id from database and increment by one to
            // create a new game id
            gameId = DBUtil.getMaxGameID(dbConnection);
            gameId++;
            jta.append("***********************\n");
            jta.append("New Game Id" + gameId + "\n");
            jta.append("***********************\n");

            // Insert the new game id with all the usernames (with 0 scores)
            // into the database
            DBUtil.insertNewGame(dbConnection, gameId, userscores.values());
         } catch (SQLException e) {
            e.printStackTrace();
            throw (new SnakeException(Constants.STARTGAMEERROR));
         }

         // Send the game parameters to all the clients
         updateGameParameters();
         // Send the dummy scores for all users to all the clients
         notifyScores();
         // Send the initial value of SnakeProtocol to all the clients
         initialiseSnakeProtocols();
      }
   }

   // To provide all available starting locations for the clients
   @Override
   public synchronized ArrayList<StartLocation> getAvailableLocations() {
      return availableStartLocations;
   }

   // To inform server that a food item has been eaten and trigger creation of a
   // new food item
   @Override
   public void eatFoodItem(Integer loc) {
      // Remove the eaten food from the food arraylist
      synchronized (foodArrayList) {
         foodArrayList.remove(loc);
      }

      // Create a new food item
      createFoodItems();
   }

   // Method for clients to advise if the snakes have been killed
   @Override
   public synchronized void killSnake(ArrayList<Integer> snakesKilled) {
      // Update the score of players killed only when it hasn't been updated
      // already
      for (Integer index : snakesKilled)
         if (userscores.get(index).getScore() < SCOREINCREMENT)
            userscores.get(index).setScore(score);

      // Increment the score attribute
      score += SCOREINCREMENT;

      // Variable to check how many players are still alive
      int numOfSnakesAlive = clientList.size();

      // To get the index of the winning player
      int winnerIndex = -1;

      for (SnakeProtocol spc : snakeProtocols.values()) {
         if (!spc.getSnakes().get(spc.getSnakeIndex()).isSnakeAlive())
            numOfSnakesAlive--;
         else
            winnerIndex = spc.getSnakeIndex();
      }

      // If the number of alive players is one then update the scores
      if (numOfSnakesAlive == 1) {
         try {
            // Update the score for the winner
            userscores.get(winnerIndex).setScore(score);

            // Update the final userscores into database
            DBUtil.updateScoresForGame(dbConnection, gameId,
                  userscores.values());
         } catch (SQLException e) {
            e.printStackTrace();
         }

         try {
            // Notify the final scores to all clients
            notifyScores();

            // Notify the winner to all clients
            for (INotifySnakeClient client : clientList.values()) {
               client.notifyWinner(winnerIndex);
            }

            // Clear the server objects for a new game
            refreshServerForNewGame();
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      }
   }

   // Method for clients to send updated snakeprotocol to the server so that it
   // can be passed to other clients
   @Override
   public synchronized void updateSnakeProtocol(SnakeProtocol sp) {
      // Get the updated information (snakecells for all snakes) via parameters
      // and send it to all the clients
      for (Integer index : snakeProtocols.keySet()) {
         SnakeProtocol spc = snakeProtocols.get(index);
         spc.setFoodCells(foodArrayList);
         spc.setSnakes(sp.getSnakes());
         this.snakeProtocols.put(index, spc);

         INotifySnakeClient client = clientList.get(index);
         try {
            client.notifySnakeProtocol(spc);
         } catch (RemoteException e) {
            e.printStackTrace();
         }
      }
   }

   // Method for clients to know if they are the first ones to join the game
   @Override
   public synchronized boolean isFirstPlayers() throws RemoteException {
      return (clientList.size() == 0);
   }

   // To send a chat message from a player/username to the server and then
   // server relays the same message to all the clients
   @Override
   public void sendChatMessage(String username, String message)
         throws RemoteException {
      // Get the message and send it to all the clients
      String msg = username + " : " + message;
      for (Integer index : userscores.keySet()) {
         clientList.get(index).notifyChatMessage(msg);
      }
   }

}
