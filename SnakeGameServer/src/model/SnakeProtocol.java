package model;

import java.io.Serializable;
import java.util.ArrayList;

//class used pass the game information between server and client 
//By Jagdeep Kaur
public class SnakeProtocol implements Serializable {
	private static final long serialVersionUID = 1L;
	//Snake index to identify individual snake and bind server to the client
	int snakeIndex = 0;
	//ArrayList of Snake objects so that each client has full knowledge about each snake in the game
	ArrayList<Snake> snakes = new ArrayList<Snake>();
	//ArrayList of Integer to be used to locate the food cells for the snakes
	ArrayList<Integer> foodCells = new ArrayList<Integer>();

	//Accessor for the snakeIndex property
	public int getSnakeIndex() {
		return snakeIndex;
	}

	//Setter for the snakeIndex property
	public void setSnakeIndex(int snakeIndex) {
		this.snakeIndex = snakeIndex;
	}

	//Accessor for the snakes property
	public ArrayList<Snake> getSnakes() {
		return snakes;
	}

	//Setter for the snakes property
	public void setSnakes(ArrayList<Snake> snakes) {
		this.snakes = snakes;
	}

	//Accessor for the foodcells property
	public ArrayList<Integer> getFoodCells() {
		return foodCells;
	}

	//Setter for the foodcells property
	public void setFoodCells(ArrayList<Integer> foodCells) {
		this.foodCells = foodCells;
	}
}
