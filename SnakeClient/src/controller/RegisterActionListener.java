package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//Login Box Action Listener
//By James Snee

import view.MainFrame;

public class RegisterActionListener implements ActionListener {

   // frame where the event will be triggered from
   private MainFrame frame;

   public RegisterActionListener(MainFrame frame) {

      this.frame = frame;

   }

   // @Override
   public void actionPerformed(ActionEvent e) {
      frame.getNewGameDialog().setVisible(false);
      frame.getRegisterDialog().setVisible(true);
   }
}
