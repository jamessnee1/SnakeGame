package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.ChatPanel;
import model.StartLocation;

//ActionListener to handle event when the "Send" button is pressed from the Chat panel
public class ChatActionListener implements ActionListener {
   // panel object that will invoke the this handler
   private ChatPanel view;

   public ChatActionListener(ChatPanel view) {
      this.view = view;
   }

   public void actionPerformed(ActionEvent e) {
      view.sendMessage();
   }
}
