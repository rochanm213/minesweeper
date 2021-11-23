/*
 * Name: Rochan Muralitharan
 * Date: June 11 2021
 * Filename: Board.java
 * Purpose: Creates the game board
 */
package minesweeper;

public class Board {
	
	public static int BOARDSIZE;//size of the board
	public static int [][] mines;//int array for the placement of the mines and neighbour counts
	public static boolean [][] isChecked;//boolean array to check which squares are clicked
	public static boolean [][] isFlagged;//boolean array to check which squares are flagged
	public static int numOfMines;//number of mines
	
	//constructor-sets up the board
	public Board(int size, int mine) {
		BOARDSIZE=size;//set boardsize to size given(easy-12x12, medium-15x15, hard-20x20)
		mines=new int[BOARDSIZE][BOARDSIZE];//set size of mines array to boardsize
		isChecked=new boolean[BOARDSIZE][BOARDSIZE];//set size of isChecked array to boardsize
		isFlagged=new boolean[BOARDSIZE][BOARDSIZE];//set size of isFlagged array to boardsize
		numOfMines=mine;//set number of mines to number given(easy-12, medium-20, hard-35)
		placeMines();//call the placeBombs method
		neighbourCounts();//call the neighbourCounts method
	}
	
	//randomly places the bombs onto the board
	//pre: none
	//post: no return, places bombs onto the board
	public static void placeMines() {
		//place the number of mines as directed by the difficulty chosen by the user
		for(int i=0; i<numOfMines; i++) {
			boolean unique=false;//boolean value to check if the spot chosen hasn't been chosen already
			while(unique==false) {//if the spot is not unique, continue finding a spot until a unique spot is chosen
				int x=(int)(Math.random()*BOARDSIZE);//find random x value within board(x coordinate)
				int y=(int)(Math.random()*BOARDSIZE);//find random y value within board(y coordinate)
				//if spot chosen does not have a mine there, place a mine in that spot and set unique to true so while loop is broken(unique mine placement was found)
				if(mines[x][y]!=10) {
					mines[x][y]=10;
					unique=true;
				}
			}
		}
	}
	
	//sets the neighbour counts for all squares on the board that do not contain a bomb
	//pre: none
	//post: no return, creates all neighbour counts
	public static void neighbourCounts() {
		//cycles through the board with the mines
		for(int i=0; i<mines.length; i++) {
			for(int j=0; j<mines.length; j++) {
				//if a spot does not have a mine, set the initial neighbour count to 0
				if(mines[i][j]!=10) {
					int count=0;
					//check each spot around the spot that is being checked, if a mine is present at that location, increase the neigbour count by one
					if(i>0 && j>0 && mines[i-1][j-1]==10)//top left
						count++;
					if(j>0 && mines[i][j-1]==10)//top
						count++;
					if(i<BOARDSIZE-1 && j>0 && mines[i+1][j-1]==10)//top right
						count++;
					if(i>0 && mines[i-1][j]==10)//left
						count++;
					if(i<BOARDSIZE-1 && mines[i+1][j]==10)//right
						count++;
					if(i>0 && j<BOARDSIZE-1 && mines[i-1][j+1]==10)//bottom left
						count++;
					if(j<BOARDSIZE-1 && mines[i][j+1]==10)//bottom
						count++;
					if(i<BOARDSIZE-1 && j<BOARDSIZE-1 && mines[i+1][j+1]==10)//bottom right
						count++;
					//set the squares without mines to its neighbour count(the number of bombs touching the square)
					mines[i][j]=count;
				}
			}
		}
	}
}


 
