package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.constants.Constants;
//Register Dialog Box
//By James Snee

import controller.RegisterUserActionListener;

public class RegisterDialog extends JDialog {

   private static final long serialVersionUID = 1L;
   private JLabel information;
   private JTextField fname;
   private JTextField lname;
   private JTextField address;
   private JTextField phoneNum;
   private JTextField regUsername;
   private JPasswordField regPassword;
   private JButton registerButton;

   public RegisterDialog(MainFrame frame) {

      // set properties of registerDialog
      this.setTitle("Register");
      this.setSize(800, 600);
      this.setLocation(250, 75);
      this.setResizable(false);
      this.setVisible(false);
      this.setLayout(new BorderLayout());

      // Assign a logo image
      ImageIcon logo = new ImageIcon(Constants.LOGOIMAGENAME);
      JLabel logoLabel = new JLabel("", logo, JLabel.CENTER);
      JPanel logoPanel = new JPanel(new BorderLayout());
      logoPanel.add(logoLabel, BorderLayout.CENTER);
      logoPanel.setBackground(Color.WHITE);

      // set up properties of form
      information = new JLabel("Enter your information:", JLabel.CENTER);
      JPanel middlePanel = new JPanel(new GridLayout(8, 1));
      JLabel field1 = new JLabel("First Name:");
      JLabel field2 = new JLabel("Last Name:");
      JLabel field3 = new JLabel("Address:");
      JLabel field4 = new JLabel("Phone Number:");
      JLabel field5 = new JLabel("Username:");
      JLabel field6 = new JLabel("Password:");

      fname = new JTextField(25);
      fname.setBorder(BorderFactory.createBevelBorder(1));
      lname = new JTextField(25);
      lname.setBorder(BorderFactory.createBevelBorder(1));
      address = new JTextField(50);
      address.setBorder(BorderFactory.createBevelBorder(1));
      phoneNum = new JTextField(25);
      phoneNum.setBorder(BorderFactory.createBevelBorder(1));
      regUsername = new JTextField(25);
      regUsername.setBorder(BorderFactory.createBevelBorder(1));
      regPassword = new JPasswordField(25);
      regPassword.setBorder(BorderFactory.createBevelBorder(1));

      // create JPanels and add textFields to them
      JPanel fieldPanel1 = new JPanel(new FlowLayout());
      JPanel fieldPanel2 = new JPanel(new FlowLayout());
      JPanel fieldPanel3 = new JPanel(new FlowLayout());
      JPanel fieldPanel4 = new JPanel(new FlowLayout());
      JPanel fieldPanel5 = new JPanel(new FlowLayout());
      JPanel fieldPanel6 = new JPanel(new FlowLayout());

      fieldPanel1.add(field1);
      fieldPanel1.add(fname);
      fieldPanel2.add(field2);
      fieldPanel2.add(lname);
      fieldPanel3.add(field3);
      fieldPanel3.add(address);
      fieldPanel4.add(field4);
      fieldPanel4.add(phoneNum);
      fieldPanel5.add(field5);
      fieldPanel5.add(regUsername);
      fieldPanel6.add(field6);
      fieldPanel6.add(regPassword);

      // add textfield panels to middlepanel
      middlePanel.add(information);
      middlePanel.add(fieldPanel1);
      middlePanel.add(fieldPanel2);
      middlePanel.add(fieldPanel3);
      middlePanel.add(fieldPanel4);
      middlePanel.add(fieldPanel5);
      middlePanel.add(fieldPanel6);

      // create panel of buttons and add the buttons to it
      JPanel buttonPanel = new JPanel();
      registerButton = new JButton("Register Now!");
      registerButton.setPreferredSize(new Dimension(150, 50));
      buttonPanel.add(registerButton);

      // Add all the panels on the dialog window
      this.add(logoPanel, BorderLayout.NORTH);
      this.add(middlePanel, BorderLayout.CENTER);
      this.add(buttonPanel, BorderLayout.SOUTH);

      // add register action listener here
      registerButton.addActionListener(new RegisterUserActionListener(frame));

   }

   // getter for all textfields

   public JLabel getInformation() {
      return information;
   }

   public void setInformation(String message) {
      information.setText(message);
      information.setForeground(Color.RED);
   }

   // getter methods
   public String getFname() {
      return fname.getText();
   }

   public String getLname() {
      return lname.getText();
   }

   public String getAddress() {
      return address.getText();
   }

   public String getPhoneNum() {
      return phoneNum.getText();
   }

   public String getRegUsername() {
      return regUsername.getText();
   }

   @SuppressWarnings("deprecation")
   public String getRegPassword() {
      return regPassword.getText();
   }

}
