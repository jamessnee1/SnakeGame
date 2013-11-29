package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import view.MainFrame;

//ActionListener to handle event when the main window/frame is exited
//By James Snee
public class ExitActionListener implements ActionListener {
	//frame where the event will be triggered from
	private MainFrame frame;

	public ExitActionListener(MainFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {


		//get user input if they are sure to exit

		int result = JOptionPane.showConfirmDialog(frame,
				"Are you sure you want to exit?", "Exit Snake?", JOptionPane.YES_NO_OPTION);


		//close the window if "Yes"
		if (result == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}
}


