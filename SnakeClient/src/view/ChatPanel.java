package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.ChatActionListener;

//Class to create a Chat panel to display the chat communication between players 
public class ChatPanel extends JPanel {
   private static final long serialVersionUID = 1L;
   // Define local constants
   private static final int CHATAREAHEIGHT = 300;
   private static final int CHATAREAWIDTH = 199;
   private static final int CHATTEXTHEIGHT = 75;
   private static final int CHATTEXTWIDTH = 199;
   private static final int BUTTONHEIGHT = 25;
   private static final int BUTTONWIDTH = 199;

   // Add control to display the chat messaging
   private JTextArea currentChat;
   // Add control to enter the chat message
   private JTextArea chatText;
   // Add button to submit chat message
   private JButton btnSendMessage;

   // Frame object where the ChatPanel is used
   private MainFrame frame;

   // Constructor
   public ChatPanel(MainFrame frame) {
      this.frame = frame;
      // Set the background of the panel
      this.setBackground(Color.WHITE);

      // set the layout based on the number of player scores plus 1 for the
      // current player label
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      currentChat = new JTextArea("");
      currentChat.setLineWrap(true);
      currentChat.setEditable(false);
      currentChat.setSize(CHATAREAWIDTH, CHATAREAHEIGHT);
      currentChat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      // embed in scroll pane to make the text area scrollable
      JScrollPane areaScrollPane = new JScrollPane(currentChat);
      areaScrollPane
            .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      areaScrollPane
            .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      areaScrollPane.setPreferredSize(new Dimension(199, 300));
      areaScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory
            .createCompoundBorder(
                  BorderFactory.createTitledBorder("Chat Window: "),
                  BorderFactory.createEmptyBorder(5, 5, 5, 5)), areaScrollPane
            .getBorder()));

      chatText = new JTextArea("");
      chatText.setSize(CHATTEXTWIDTH, CHATTEXTHEIGHT);
      chatText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      btnSendMessage = new JButton("Send");
      btnSendMessage.setSize(BUTTONWIDTH, BUTTONHEIGHT);
      btnSendMessage.addActionListener(new ChatActionListener(this));

      // add scrollable text pane to the panel
      this.add(areaScrollPane);
      // this.add(currentChat);
      this.add(chatText);
      this.add(btnSendMessage);
   }

   // method to refresh the panel with new chat messages
   public void refreshChat(String message) {
      currentChat.setText(currentChat.getText() + "\n" + message);
      paintAll(getGraphics());
   }

   // method to send the new chat message to server
   public void sendMessage() {
      frame.sendChatMessage(chatText.getText());
      chatText.setText("");
   }
}
