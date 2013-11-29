package model;

import java.io.Serializable;

//Class used pass the user score data between server and client 
public class UserScore implements Serializable {
   private static final long serialVersionUID = 1L;
   // User Name
   private String username;
   // User score
   private int score;

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public int getScore() {
      return score;
   }

   public void setScore(int score) {
      this.score = score;
   }

}
