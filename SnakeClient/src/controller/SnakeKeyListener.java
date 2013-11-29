package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.SnakeDirection;

import view.Board;

//KeyListener to handle keyevent when game is on and player is moving the snake 
// using UP/LEFT/DOWN/RIGHT arrow keys or increasing/decreasing the snake speed
//By Razlan Adnan
public class SnakeKeyListener implements KeyListener {
	//view to which the KeyListener will be binded to
	private Board view;
	public SnakeKeyListener(Board board){
		this.view = board;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//get the key pressed
		int code = e.getKeyCode();

		//invoke the moveDirection method of the Board class by passing 
		// relevant direction value based on the key pressed
		if(code == KeyEvent.VK_UP)
			view.moveDirection(SnakeDirection.UP.getValue());
		else if(code == KeyEvent.VK_DOWN)
			view.moveDirection(SnakeDirection.DOWN.getValue());
		else if(code == KeyEvent.VK_RIGHT)
			view.moveDirection(SnakeDirection.RIGHT.getValue());
		else if(code == KeyEvent.VK_LEFT)
			view.moveDirection(SnakeDirection.LEFT.getValue());
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	@Override
	public void keyTyped(KeyEvent e) {

	}

}
