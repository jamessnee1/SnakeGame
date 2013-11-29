package snakeinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import model.SnakeProtocol;
import model.StartLocation;
import model.exception.SnakeException;

//RMI Interface to be used as stub from clients to call various function on the server  
public interface ISnakeServer extends Remote {
   // Created the new user in the database with the values passed in parameters
   public boolean createNewUser(String fName, String lName, String address,
         String phoneNum, String username, String password)
         throws RemoteException, SnakeException;

   // Registers the user for the game
   public void joinGame(INotifySnakeClient client, String username,
         String password, int numOfPlayers, int numOfBoardRows,
         int numOfBoardCols, StartLocation startLoc) throws RemoteException,
         SnakeException;

   // To provide all available starting locations for the clients
   public ArrayList<StartLocation> getAvailableLocations()
         throws RemoteException;

   // For clients to send updated snakeprotocol to the server... so that it can
   // be passed to other clients
   public void updateSnakeProtocol(SnakeProtocol sp) throws RemoteException;

   // To inform server that a food item has been eaten and trigger creation of a
   // new food item
   public void eatFoodItem(Integer loc) throws RemoteException;

   // For clients to know if they are the first ones to join the game
   public boolean isFirstPlayers() throws RemoteException;

   // Clients to advise if snake(s) have been killed
   public void killSnake(ArrayList<Integer> snakesKilled)
         throws RemoteException;

   // To send a chat messagae from a player/username to the server
   public void sendChatMessage(String username, String message)
         throws RemoteException;
}
