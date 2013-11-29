package model;

import java.io.Serializable;

//enum to define the possible speeds in which snake can move
public enum SnakeSpeed implements Serializable {
   // the integer values indicate the delay in milliseconds
   DEFAULT(20), MEDIUM(10), MAX(5);
   private int value;

   private SnakeSpeed(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

}
