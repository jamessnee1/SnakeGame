package view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controller.AboutActionListener;
import controller.ExitActionListener;
import controller.NewGameActionListener;
import controller.RulesActionListener;

//Class to create the menu for the game
//By James Snee
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	//menubar to be used for the game menu
	private JMenuBar menuBar;
	//frame that will hold/display the menu
	private MainFrame frame;

	public MenuPanel(MainFrame frame)
	{
		this.frame = frame;
		//Set relevant properties for the MenuBar and assign it to the frame
		menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);

		//Add File menu and add New Game & Exit Snake menu items to it 
		JMenu file = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New Game");
		file.add(newGame);
		//Assign an action listener to the New Game menu item
		newGame.addActionListener(new NewGameActionListener(frame));
		JMenuItem exit = new JMenuItem("Exit Snake");
		file.add(exit);
		//Add the File menu to the menubar
		menuBar.add(file);
		//Assign an action listener to the Exit Snake menu item
		exit.addActionListener(new ExitActionListener(frame));


		//Add Help menu and add About and how to play menu items to it 
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		JMenuItem rules = new JMenuItem("How to play");
		help.add(about);
		help.add(rules);
		//Assign an action listener to the About and rules menu items
		about.addActionListener(new AboutActionListener(frame));
		rules.addActionListener(new RulesActionListener(frame));
		//Add the About menu to the menubar
		menuBar.add(help);
	}
}
