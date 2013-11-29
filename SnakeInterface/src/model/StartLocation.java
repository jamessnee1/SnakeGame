package model;

import java.io.Serializable;

//enum to define the possible Starting Locations for the snake when game starts
public enum StartLocation implements Serializable {
   TOPLEFT(1), TOPRIGHT(2), BOTTOMLEFT(3), BOTTOMRIGHT(4);
   private int value;

   private StartLocation(int value) {
      this.value = value;
   }

   public int getValue() {
      return value;
   }
}
