package model.constants;

public class Constants {
   public static final String RMISNAKESERVICENAME = "SnakeServerService";

   public static final String LOGOIMAGENAME = "logo.jpg";
   public static final String WAITINGMESSAGE = "Waiting for others to join...";
   public static final String GAMEONMESSAGE = "Game On!!";
   public static final String YOUWONMESSAGE = "You won!! Hurray!!";
   public static final String YOULOSTMESSAGE = "Sorry, you lost! Please stay online to see your scores at the end of the game...";
   public static final String EXITSNAKEMESSAGE = "Are you sure you want to exit?";
   public static final String EXITSNAKWINDOWTITLE = "Exit Snake?";
   public static final String YOUWONDIALOGMESSAGE = "You won! Hurray!!";
   public static final String YOULOSTDIALOGMESSAGE = "You Lost! Game Over!!!";

   // Server IP and Port
   public final static String SERVERIPADDRESS = "127.0.0.1";
   public final static int SERVERPORT = 5999;

   // Registration form constants
   public static final String FIRSTNAMEERROMESSAGE = "Error: Please enter your first name!";
   public static final String LASTNAMEERROMESSAGE = "Error: Please enter your last name!";
   public static final String ADDRESSERROMESSAGE = "Error: Please enter your address!";
   public static final String PHONENUMERROMESSAGE = "Error: Please enter your phone number!";
   public static final String USERNAMEERROMESSAGE = "Error: Please enter a username!";
   public static final String PASSWORDERROMESSAGE = "Error: Please enter a password!";

   // Server errors
   public static final String REGISTERUSERERROR = "Error while creating the user";
   public static final String SERVERBUSYERROR = "Server Busy!! Game is already running... Please try after some time!";
   public static final String USERALREADYINGAMEERROR = "Cannot login twice... user already in the game!";
   public static final String LOGINSERVERERROR = "Problem with the server... please try again!";
   public static final String INVALIDUSERNAMEPASSWORDERROR = "Login unsucessful! Please verify the username/password and try again";
   public static final String STARTGAMEERROR = "Trouble starting game... please try again";
}
