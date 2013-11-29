package model;

import java.io.Serializable;

//enum to define the possible directions in which snake can move
//By Razlan Adnan
public enum SnakeDirection implements Serializable{
	RIGHT(1), DOWN(2), LEFT(3),UP(4);
	private int value;
	private SnakeDirection(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
