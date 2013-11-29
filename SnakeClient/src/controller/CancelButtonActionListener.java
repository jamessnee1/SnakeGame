package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//ActionListener to handle event when "OK" is selected from the "About" dialog
//By James Snee
public class CancelButtonActionListener implements ActionListener {
   //frame where the event will be triggered from
   private view.MainFrame frame;

   public CancelButtonActionListener(view.MainFrame frame)
   {
      this.frame = frame;
   }

   public void actionPerformed(ActionEvent e)
   {
      //Hide About Dialog window when actioned
	  if (frame.getAboutDialog().isVisible()){
		  frame.getAboutDialog().setVisible(false);
	  }
	  //Hide Instruction Dialog window when actioned
	  else if (frame.getInstructionDialog().isVisible()){
		  frame.getInstructionDialog().setVisible(false);
		  
	  }
   }
}
