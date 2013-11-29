package model;

import java.io.Serializable;

//enum to define the possible speeds in which snake can move
//By Jagdeep Kaur
public enum SnakeSpeed implements Serializable{
	//the integer values indicate the delay in milliseconds  
	DEFAULT(40), MEDIUM(20), MAX(10);
	private int value;
	private SnakeSpeed(int value)
	{
		this.value = value;
	}
	public int getValue()
	{
		return this.value;
	}

}
