package model.exception;

import java.io.Serializable;

//Custom Exception class to be used to pass exceptions between server and client
public class SnakeException extends Exception implements Serializable {
   private static final long serialVersionUID = 1L;

   public SnakeException() {
   }

   public SnakeException(String message) {
      super(message);
   }
}
