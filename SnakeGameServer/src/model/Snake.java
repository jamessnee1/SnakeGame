package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.awt.Color;

//create a serializable Snake class with all the required snake attributes needed for the game
//By Jagdeep Kaur
public class Snake implements Serializable{
	private static final long serialVersionUID = 1L;
	//Snake index to identify individual snake and bind server to the client
	int snakeIndex;
	//Snake color - different for each snake
	Color snakeColor;
	//flag to identify if snake is still alive
	boolean snakeAlive = true;
	//variable to hold the speed of snake - uses SnakeSpeed enum 
	SnakeSpeed snakeSpeed = SnakeSpeed.DEFAULT;
	//Integer ArrayList to hold the cell locations for the snake 
	ArrayList<Integer> snakeCells = new ArrayList<Integer>();
	//variable to hold the starting location for the snake - uses the StartLocation enum 
	StartLocation snakeStartLocation = StartLocation.TOPLEFT;
	//variable to hold the direction in which snake is moving - uses SnakeDirection enum 
	SnakeDirection snakeDirection = SnakeDirection.DOWN;

	//Accessor for the snakeAlive property
	public boolean isSnakeAlive() {
		return snakeAlive;
	}

	//Setter for the snakeAlive property
	public void setSnakeAlive(boolean snakeAlive) {
		this.snakeAlive = snakeAlive;
	}

	//Accessor for the snakeSpeed property
	public SnakeSpeed getSnakeSpeed() {
		return snakeSpeed;
	}

	//Setter for the snakeSpeed property
	public void setSnakeSpeed(SnakeSpeed snakeSpeed) {
		this.snakeSpeed = snakeSpeed;
	}

	//Accessor for the snakeDirection property
	public SnakeDirection getSnakeDirection() {
		return snakeDirection;
	}

	//Setter for the snakeDirection property
	public void setSnakeDirection(SnakeDirection snakeDirection) {
		this.snakeDirection = snakeDirection;
	}

	//Accessor for the snakeIndex property
	public int getSnakeIndex() {
		return snakeIndex;
	}

	//Setter for the snakeIndex property
	public void setSnakeIndex(int snakeIndex) {
		this.snakeIndex = snakeIndex;
	}

	//Accessor for the snakeStartLocation property
	public StartLocation getSnakeStartLocation() {
		return snakeStartLocation;
	}

	//Setter for the snakeStartLocation property
	public void setSnakeStartLocation(StartLocation snakeStartLocation) {
		this.snakeStartLocation = snakeStartLocation;
	}

	//Accessor for the snakeColor property
	public Color getSnakeColor() {
		return snakeColor;
	}

	//Setter for the snakeColor property
	public void setSnakeColor(Color c) {
		this.snakeColor = c;
	}

	//Accessor for the snakeCells property
	public ArrayList<Integer> getSnakeCells() {
		return snakeCells;
	}

	//Setter for the snakeCells property
	public void setSnakeCells(ArrayList<Integer> snakeCells) {
		this.snakeCells = snakeCells;
	}
}
