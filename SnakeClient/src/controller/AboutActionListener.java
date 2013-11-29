package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//ActionListener to handle event when "About" option is selected from the frame menu
//By James Snee
public class AboutActionListener implements ActionListener {
	//frame where the event will be triggered from
	private view.MainFrame frame;

	public AboutActionListener(view.MainFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//Show About Dialog window when actioned

		frame.getAboutDialog().setVisible(true);


	}
}
