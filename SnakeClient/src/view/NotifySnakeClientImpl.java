package view;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import model.SnakeProtocol;
import model.UserScore;

import snakeinterface.INotifySnakeClient;

public class NotifySnakeClientImpl extends UnicastRemoteObject implements
      INotifySnakeClient, Serializable {

   private static final long serialVersionUID = 1L;
   private Board mainBoard;

   protected NotifySnakeClientImpl(Board brd) throws RemoteException {
      super();
      this.mainBoard = brd;
   }

   @Override
   public void notifySnakeProtocol(SnakeProtocol sp) throws RemoteException {
      // TODO Auto-generated method stub
      mainBoard.receiveSnakeProtocol(sp);
   }

   @Override
   public void notifyWinner(int winnerIndex) throws RemoteException {
      mainBoard.stopGame(winnerIndex);
   }

   @Override
   public void notifyGameParameters(int numRows, int numCols, int numPlayers)
         throws RemoteException {
      mainBoard.receiveGameParameters(numRows, numCols, numPlayers);

   }

   @Override
   public void notifyUserScores(HashMap<Integer, UserScore> userscores)
         throws RemoteException {
      mainBoard.receiveUserScores(userscores);

   }

   @Override
   public void notifyChatMessage(String message) throws RemoteException {
      // TODO Auto-generated method stub
      mainBoard.receiveChatMessage(message);
   }

}
