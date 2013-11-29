import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.UserScore;
//DBUtil Database Method Class
//By James Snee

public class DBUtil {

   // Declaring access to the database
   private static final String USERNAME = "dbuser";
   private static final String PASSWORD = "dbpassword";
   // If we are connecting to a remote database not on localhost, replace
   // localhost in the following string with the IP address
   private static final String CONN_STRING = "jdbc:mysql://localhost/logincredentials";

   // Constructor
   public DBUtil() {
   }

   // Method to get the connection for the database
    public static Connection Connect() throws SQLException {
       return DriverManager.getConnection(CONN_STRING, USERNAME,
             PASSWORD);
       }

   // Method to close various objects like resultset, statement & connection
   // passed as parameters
   public static void closeConnection(ResultSet result, Statement stmt,
         Connection conn) throws SQLException {

      // Not closing resources creates problems over a network

      // if there is a result, close it
      if (result != null) {
         result.close();
      }
      // if there is a statement, close it
      if (stmt != null) {
         stmt.close();
      }
      // if there is an established connection, close it
      if (conn != null) {
         conn.close();
      }

   }

   // This method returns exceptions in a printable form
   public static void processException(SQLException e) {

      // Set a string with all errors in it for JDialog
      String error = new String("Error: " + e.getMessage() + "\nError code: "
            + e.getErrorCode() + "\nSQL State: " + e.getSQLState());

      JOptionPane errorPane = new JOptionPane(error, JOptionPane.ERROR_MESSAGE);
      JDialog errorDialog = errorPane.createDialog("Error");
      errorDialog.setAlwaysOnTop(true);
      errorDialog.setVisible(true);

      System.err.println("Error message: " + e.getMessage());
      System.err.println("Error code: " + e.getErrorCode());
      System.err.println("SQL State: " + e.getSQLState());

   }

   // this method sends the register user data entered into the database
   public static boolean insertUser(Connection conn, String fname,
         String lname, String address, String phoneNum, String username,
         String password) throws SQLException {

      // create statement to get count of userID's
      Statement getUserID = conn.createStatement(
            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

      // Create preparedStatement with variable placeholders
      PreparedStatement insert = conn
            .prepareStatement(
                  "insert into users (userid, fname, lname, address, "
                        + "phonenumber, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)",
                  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      ResultSet returnedUserID = null;

      try {
         // get count of UserID's currently in the database, so we can add 1 to
         // it when we add a new user
         System.out.println("Executing UserID Query");
         returnedUserID = getUserID
               .executeQuery("SELECT userid, COUNT(userid) FROM users;");

      } catch (SQLException e) {
         // If something happens with the retrieve, then process the exception
         processException(e);
         return false;
      }

      while (returnedUserID.next()) {

         int countUserID = returnedUserID.getInt("COUNT(userid)");
         System.out.println("The value of countUserID is " + countUserID);

         // increment the userID by 1 for insertion
         countUserID++;

         // insert all values into prepared statement
         insert.setInt(1, countUserID);
         insert.setString(2, fname);
         insert.setString(3, lname);
         insert.setString(4, address);
         insert.setString(5, phoneNum);
         insert.setString(6, username);
         insert.setString(7, password);
         System.out
               .println("inserted countUserID into insert statement. Value is: "
                     + countUserID);
         System.out.println("prepared statement insert is " + insert);
         System.out.println("All values passed into insert successfully");
         // execute query
         insert.execute();

      }
      return true;
   }

   // this method checks the login credentials against the database and returns
   // true/false based on success/failure of login request
   public static boolean checkLogin(Connection conn, String username,
         String password) throws SQLException {

      System.out.println("The value of username in checkLogin is: " + username);
      System.out.println("The value of password in checkLogin is: " + password);

      // Create preparedStatement with variable placeholders
      PreparedStatement checkLoginStmt = conn
            .prepareStatement("SELECT USERNAME, PASSWORD FROM users "
                  + "WHERE USERNAME = ? AND PASSWORD = ?;");
      ResultSet userPassResult = null;
      try {
         // add variables and execute
         checkLoginStmt.setString(1, username);
         checkLoginStmt.setString(2, password);
         System.out.println("Prepared statement is " + checkLoginStmt);
         System.out.println("Executing Query");
         userPassResult = checkLoginStmt.executeQuery();

      } catch (SQLException e) {
         processException(e);
         return false;

      }

      // if no results found, return false
      if (!userPassResult.next()) {
         return false;
      }
      // while loop to loop through the ResultSet, if it exists
      while (userPassResult.next()) {
         String retrievedUserName = userPassResult.getString("username");
         String retrievedPassword = userPassResult.getString("password");
         System.out.println("data in database is " + retrievedUserName
               + retrievedPassword);
      }

      // if all went well, login was successful, function will return true
      System.out.println("Login successful!");
      return true;

   }

   // Method to check if a given username already exist in database
   // throws true if user already exists
   public static boolean checkUsername(Connection conn, String username)
         throws SQLException {

      // check username hasnt already been added to database
      System.out.println("The username entered is: " + username);

      Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);

      ResultSet rs = null;
      try {
         System.out.println("Executing Query");
         rs = stmt.executeQuery("SELECT USERNAME from users");
      } catch (SQLException e) {
         // If something happens with the retrieve, then process the exception
         processException(e);
      }

      // while loop to loop through the ResultSet
      while (rs.next()) {
         // new string is set from the username of the ResultSet
         String retrievedUser = rs.getString("username");
         // if the strings match, a user is found
         if (retrievedUser.compareTo(username) == 0) {
            System.out.println("Username found!");
            closeConnection(rs, stmt, conn);
            return true;
         } else {
            // all is well, continue with the game
            System.out.println("Username NOT found! Start game");
         }
      }
      rs.last();
      System.out.println("Number of entries are: " + rs.getRow());
      return false;

   }

   // Method to return the maximum game Id from the database
   public static int getMaxGameID(Connection conn) throws SQLException {
      Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);

      ResultSet rs = null;
      try {
         System.out.println("Executing Query");
         rs = stmt.executeQuery("SELECT Max(gameid) maxgameid from game");
      } catch (SQLException e) {
         // If something happens with the retrieve, then process the exception
         processException(e);
      }

      int retrievedGameId = 0;

      // while loop to loop through the ResultSet
      while (rs.next()) {
         // new string is set from the max gameid of the ResultSet
         retrievedGameId = rs.getInt("maxgameid");
      }
      closeConnection(rs, stmt, null);
      return retrievedGameId;
   }

   // this method adds the new game record into database
   public static boolean insertNewGame(Connection conn, int gameid,
         Collection<UserScore> userscores) throws SQLException {

      // Create preparedStatement with variable placeholders
      PreparedStatement insert = conn.prepareStatement(
            "insert into game (gameid, username, score) " + "VALUES (?, ?, ?)",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      for (UserScore uscore : userscores) {
         // insert all values into prepared statement
         insert.setInt(1, gameid);
         insert.setString(2, uscore.getUsername());
         insert.setInt(3, uscore.getScore());
         insert.addBatch();
         System.out.println("inserted new game " + gameid);
         System.out.println("All values passed into insert successfully");
      }
      // execute query
      insert.executeBatch();
      insert.close();

      return true;

   }

   // this method updates the scores for the game into the database
   public static boolean updateScoresForGame(Connection conn, int gameid,
         Collection<UserScore> userscores) throws SQLException {

      // Create preparedStatement with variable placeholders
      PreparedStatement update = conn.prepareStatement(
            "update game set score=? where gameid=? and username=? ",
            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      for (UserScore uscore : userscores) {
         // insert all values into prepared statement
         update.setInt(1, uscore.getScore());
         update.setInt(2, gameid);
         update.setString(3, uscore.getUsername());
         update.addBatch();
         //System.out.println("updated new game " + gameid);
      }
      // execute query
      update.executeBatch();
      update.close();

      return true;

   }
}
