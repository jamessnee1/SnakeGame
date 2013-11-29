package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.constants.Constants;
//Register Button Action Listener
//By James Snee

import view.MainFrame;

public class RegisterUserActionListener implements ActionListener {

   // frame where the event will be triggered from
   private MainFrame frame;

   public RegisterUserActionListener(MainFrame frame) {
      this.frame = frame;
   }

   @Override
   public void actionPerformed(ActionEvent e) {

      if (frame.getRegisterDialog().getFname().trim().equals("")) {
         frame.getRegisterDialog().setInformation(
               Constants.FIRSTNAMEERROMESSAGE);
      }

      else if (frame.getRegisterDialog().getLname().trim().equals("")) {
         frame.getRegisterDialog()
               .setInformation(Constants.LASTNAMEERROMESSAGE);
      }

      else if (frame.getRegisterDialog().getAddress().trim().equals("")) {
         frame.getRegisterDialog().setInformation(Constants.ADDRESSERROMESSAGE);
      }

      else if (frame.getRegisterDialog().getPhoneNum().trim().equals("")) {
         frame.getRegisterDialog()
               .setInformation(Constants.PHONENUMERROMESSAGE);
      }

      else if (frame.getRegisterDialog().getRegUsername().trim().equals("")) {
         frame.getRegisterDialog()
               .setInformation(Constants.USERNAMEERROMESSAGE);
      }

      else if (frame.getRegisterDialog().getRegPassword().trim().equals("")) {
         frame.getRegisterDialog()
               .setInformation(Constants.PASSWORDERROMESSAGE);
      }

      else {
         frame.registerUser(frame.getRegisterDialog().getFname(), frame
               .getRegisterDialog().getLname(), frame.getRegisterDialog()
               .getAddress(), frame.getRegisterDialog().getPhoneNum(), frame
               .getRegisterDialog().getRegUsername(), frame.getRegisterDialog()
               .getRegPassword());
      }

   }

}
