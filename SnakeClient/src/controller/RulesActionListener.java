package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//ActionListener to handle event when "How to play" option is selected from the frame menu
//By James Snee
public class RulesActionListener implements ActionListener {
	//frame where the event will be triggered from
	private view.MainFrame frame;

	public RulesActionListener(view.MainFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//Show rules Dialog window when actioned

		frame.getInstructionDialog().setVisible(true);


	}
}
