package snakeinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import model.SnakeProtocol;
import model.UserScore;

//Interface to be used for Callback from server to client - to notify client of various events  
public interface INotifySnakeClient extends Remote {
   // Notifies the snakeProtocol object to the client(s)
   public void notifySnakeProtocol(SnakeProtocol sp) throws RemoteException;

   // Notifies the Winner to the client(s) by using the index passed as
   // parameter
   public void notifyWinner(int winnerIndex) throws RemoteException;

   // Notifies the game starting parameters object to the client(s)
   public void notifyGameParameters(int numRows, int numCols, int numPlayers)
         throws RemoteException;

   // Notifies the score for all the players to the client(s)
   public void notifyUserScores(HashMap<Integer, UserScore> userscores)
         throws RemoteException;

   // Notifies the chat message from server to the client(s)
   public void notifyChatMessage(String message) throws RemoteException;
}
