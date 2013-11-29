package view;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Snake;

//Class to create the board and place food, snakes on it
//By Jagdeep Kaur
public class SnakePanel extends JPanel{

	private static final long serialVersionUID = 1L;

	//Variables to hold the number of rows and columns to be used to create the board
	private int numOfBoardRows = -1;
	private int numOfBoardCols = -1; 

	//Variables to hold the list of food cells (locations)
	private ArrayList<Integer> foodCells;
	//Variables to hold the Array of snakes. This will be used to create all the active snakes using their snake cells (locations)
	private ArrayList<Snake> snakes = new ArrayList<Snake>();

	//Constructor to initiate the snakes array
	SnakePanel(ArrayList<Snake> snakes)
	{
		this.snakes = snakes;
		refreshSnakePanel();
	}

	//Constructor to initiate the snakes array and number of rows/cols for the board
	SnakePanel(int rows, int cols, ArrayList<Snake> snakes)
	{
		this.snakes = snakes;
		this.numOfBoardRows = rows;
		this.numOfBoardCols = cols;
		refreshSnakePanel();
	}

	//Set the number of rows for the board
	public void setNumOfBoardRows(int numOfBoardRows) {
		this.numOfBoardRows = numOfBoardRows;
	}

	//Set the number of columns for the board
	public void setNumOfBoardCols(int numOfBoardCols) {
		this.numOfBoardCols = numOfBoardCols;
	}

	//Get the location of food cells in an array
	public ArrayList<Integer> getFoodCells() {
		return foodCells;
	}

	//Set the array to hold the food cells location 
	public void setFoodCells(ArrayList<Integer> foodCells) {
		this.foodCells = foodCells;
	}

	//Get the array of snakes
	public ArrayList<Snake> getSnakes() {
		return snakes;
	}

	//Set the array of snakes
	public void setSnakes(ArrayList<Snake> snakes) {
		this.snakes = snakes;
	}


	//To refresh the board using the available value of foodcells and snake arrays
	public void refreshSnakePanel()


	{
		//Remove all the components from the panel
		removeAll();

		//Set the gridlayout for the board with number of rows, cols 
		setLayout (new GridLayout(numOfBoardRows,numOfBoardCols));

		boolean snakeCellCreated = false;
		boolean foodCellCreated = false;

		//loop through all the cells of the board
		for(int i=0;i<numOfBoardRows*numOfBoardCols;i++)
		{
			snakeCellCreated = false;
			foodCellCreated = false;
			//for each cell, loop through all the snakes and create a snake cell if it matches with the current cell   
			for(Snake s: snakes)
			{
				//only create alive snakes.. skip the dead snakes
				if(!s.isSnakeAlive())
					continue;
				//loop through the snake cells for each cell
				for(int j=0;j<s.getSnakeCells().size();j++)
				{
					if(s.getSnakeCells().get(j) == i)
					{
						//Add the snake head cell
						if(j==0)
						{
							SnakeHeadCellPanel shcp = new SnakeHeadCellPanel();
							shcp.setSnakeColor(s.getSnakeColor());
							add (shcp);
						}
						//Add the snake body cells
						else
						{
							SnakeCellPanel scp = new SnakeCellPanel();
							scp.setSnakeColor(s.getSnakeColor());
							add (scp);
						}
						snakeCellCreated = true;
						break;
					}
				}
				if(snakeCellCreated)
					break;
			}

			//for each cell, loop through all the food cells to check if it matches with the current cell and create a food cell   
			if(foodCells!=null)
				for(int j=0;j<foodCells.size();j++)
				{
					if(foodCells.get(j) == i)
					{
						FoodCellPanel fcp = new FoodCellPanel();
						add (fcp);
						foodCellCreated = true;
						break;
					}
				}
			//If no snake cell or food cell exist at the current location, create a blank board cell
			if(!snakeCellCreated && !foodCellCreated)
				add (new BoardCellPanel());
		}
	}

}
