package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//ActionListener to handle event when the "New" game option is selected
//By James Snee
public class NewGameActionListener implements ActionListener {
	//frame where the event will be triggered from
	private view.MainFrame frame;

	public NewGameActionListener(view.MainFrame frame)
	{
		this.frame = frame;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		//Show New Game Dialog window when actioned
		frame.getNewGameDialog().setVisible(true);
	}
}


