/*
 * Name: Rochan Muralitharan
 * Date: June 11 2021
 * Filename: Main.java
 * Purpose: Holds main functions of the game
 */
package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import javax.sound.sampled.*;
public class Main extends JFrame implements ActionListener{

	JFrame frame = new JFrame("Minesweeper");//frame for game
	JPanel panel = new JPanel();//panel for game
	JButton[][]buttons = new JButton[Board.BOARDSIZE][Board.BOARDSIZE];//2d array of buttons for board(user clicks buttons to interact with board)
	Clip activeClip;//music for game/audio clip
	AudioInputStream inputstream;//input stream to get audio clip
	
	public Main(boolean isMusic) {//constructor, called in the menu, state of music button is sent into constructor to play the music or not(true or false)
		setClip();//sets up the audio clip for the game
		if(isMusic)//if user selected to play the music, play the music
			playMusic(true);
		frame.setSize(400,400);//size of frame
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);//set default close operation
		panel.setLayout(new GridLayout(Board.BOARDSIZE,Board.BOARDSIZE));//panel with buttons will have grid layout, depending on the size of the board(12x12, 15x15, 20x20)
		for(int i=0; i<buttons.length; i++) {//cycle through the array of buttons
			for(int j=0; j<buttons.length; j++) {
				//at each data spot, create a button and add the action listener
				buttons[i][j]=new JButton();
				buttons[i][j].addActionListener(this);
				//current i and j set to final ints, overrided method in mouse adapter needs to have final ints
				final int finalI=i;
				final int finalJ=j;
				buttons[i][j].addMouseListener(new MouseAdapter() {//add the mouse listener to each button, so buttons can be flagged(right click)
					@Override
					public void mousePressed(MouseEvent event) {//override the mouse pressed method
						if(SwingUtilities.isRightMouseButton(event))//if the button is right clicked, flag that button
							mineFlagger(finalI,finalJ);
					}	
				});
				panel.add(buttons[i][j]);//add all buttons to the panel
			}
		}
		
		frame.add(panel);//add panel to the frame
		frame.requestFocusInWindow();//frame is refreshed so that every element is visible
		frame.setVisible(true);//frame is visible
	}

	//pre: x coordinate, y coordinate
	//post: no return, if a 0 is pressed, will reveal all squares around it(saves time for user)
	//if the user clicks a square with a neighbour count of 0(no bombs touching it), all the squares around it will be revealed(if a revealed square also has count of 0, squares around that will be revealed as well)
	public void revealZeros(int x, int y) {
		for(int i=-1; i<2; i++) {//cycle through the squares around the square user chose(left:-1, middle:0, right:1
			for(int j=-1; j<2; j++) {//(up:-1, middle:0, down:1)
				
				if(i==0 && j==0)//if at original square/square user chose, skip
					continue;
				int r=x+i;//get the coordinates of the squares around the square chosen
				int c=y+j;
				
				if(r<0 || r>=Board.BOARDSIZE || c<0 || c>=Board.BOARDSIZE)//if new square is outside of board, check next possible square
					continue;
				
				if(Board.isChecked[r][c]==false && Board.mines[r][c]!=10) {//if new square is not checked yet and has no mine at that spot
					Board.isChecked[r][c]=true;//check the mine
					buttons[r][c].removeActionListener(this);//remove the action listener from the square
					buttons[r][c].setForeground(countColour(r,c));//get the colour of the neighbour count, depends on the value of the neighbour count
					buttons[r][c].setText(String.valueOf(Board.mines[r][c]));//set the label of the button to its neighbour count
					//if revealed square has a neighbour count of 0, reveal all squares around it as well(recursive method/calls itself)
					if(Board.mines[r][c]==0)
						revealZeros(r, c);
				}
				
			}
		}
	}
	
	//pre: none
	//post: no return, checks if game has been won
	//checks each button to see if the only buttons left are the ones with bombs, if this condition is met, user has won the game
	public void gameWin() {
		boolean winner = true;//initially set winner to true
		for(int i=0; i<Board.BOARDSIZE; i++) {//cycle through board
			for(int j=0; j<Board.BOARDSIZE; j++) {
				//if a button is not a bomb and has not been checked yet, user has not won game yet, set winner to false and break loop
				if(Board.mines[i][j]!=10 && Board.isChecked[i][j]==false) {
					winner=false;
					break;
				}
			}
		}
		
		if(winner) {//if winner is true, stop the music and use dialog box to ask if user wants to go back to main menu
			playMusic(false);
			int response =JOptionPane.showConfirmDialog(null, "YOU WIN! Would you like to go back to the main menu?");
			if(response==0) //if yes is chosen, call menu constructor
				new Menu();
		}		
	}
	
	//pre: none
	//post: no return, reveal all mines
	//if user hits a button with a mine in it, user loses game and all mines are revealed(losing game method)
	public void revealMines() {
		for(int i=0; i<Board.BOARDSIZE; i++) {//cycle through board
			for(int j=0; j<Board.BOARDSIZE; j++) {
				if(Board.mines[i][j]==10 && Board.isChecked[i][j]==false) {//if button has bomb and is not checked yet
					//set text colour to black, text set to B for bomb, and remove action listener
					buttons[i][j].setForeground(Color.BLACK);
					buttons[i][j].setText("B");
					buttons[i][j].removeActionListener(this);
				}
			}
		}
		//game is lost so stop music and ask user if they want to go back to the main menu(using dialog box)
		playMusic(false);
		int response =JOptionPane.showConfirmDialog(null, "YOU LOSE! Would you like to go back to the main menu?");
		if(response==0) {//if user chooses yes, call menu constructor
			new Menu();
		}
	}
	
	//pre: x coordinate, y coordinate
	//post: returns colour depending on neighbour count
	//returns the colour of the text of a button depending on its neighbour count
	public Color countColour(int i, int j) {
		if(Board.mines[i][j]==1)//if count is 1, colour is blue
			return Color.BLUE;
		else if(Board.mines[i][j]==2)//if count is 2, colour is green
			return Color.GREEN;
		else if(Board.mines[i][j]==3)//if count is 3, colour is red
			return Color.RED;
		else if(Board.mines[i][j]==4)//if count is 4, colour is magenta
			return Color.MAGENTA;
		else
			return Color.GRAY;//any other neighbour count, colour is gray
		
	}
	
	//pre: event
	//post: no return, has different outcomes depending on the buttons pressed(depending on value of button, or whether it is a bomb or not)
	public void actionPerformed(ActionEvent event) {
		for(int i=0; i<Board.BOARDSIZE; i++) {//cycle through board
			for(int j=0; j<Board.BOARDSIZE; j++) {
				if(event.getSource()==buttons[i][j]) {//if specific button is pressed
					buttons[i][j].removeActionListener(this);//remove its action listener
					Board.isChecked[i][j]=true;//set that button to checked
					if(Board.mines[i][j]!=10) {//if the button doesn't contain a bomb, find the colour of the text of the button and set the text of the button to its neighbour count
						buttons[i][j].setForeground(countColour(i, j));
						buttons[i][j].setText(String.valueOf(Board.mines[i][j]));
						gameWin();//after each button press(that is not a bomb), check if the game has been won
					}
					else {//if button pressed is a bomb
						//set text colour to black, set text of button to B for bomb, and reveal all mines using revealMines method(game lose scenario as game has been lost)
						buttons[i][j].setForeground(Color.BLACK);
						buttons[i][j].setText("B");
						revealMines();
					}
					
					if(Board.mines[i][j]==0) {
						//if button pressed is a 0, reveal all the squares around it using the revealZeros method, and then check if the game has been won
						revealZeros(i,j);
						gameWin();
					}
				}
			}
		}
	}
	
	//pre: x coordinate, y coordinate
	//post: no return, flags mines if they are right clicked
	//if the user right clicks a button, a flag is put onto the button, if a button is right clicked and was already flagged, the flag is taken off
	public void mineFlagger(int x, int y) {
		if(Board.isFlagged[x][y]==false && Board.isChecked[x][y]==false) {//if the button isn't checked or flagged
			//set text colour to black, set text to F for flagged and set that the square is flagged(isFlagged at the square is true)
			buttons[x][y].setForeground(Color.BLACK);
			buttons[x][y].setText("F");
			Board.isFlagged[x][y]=true;
		}
		//removes the flag if one is already there
		else if(Board.isFlagged[x][y]==true && Board.isChecked[x][y]==false) {//if button isn't checked, but is flagged
			//set text to nothing and set that the square isn't flagged(isFlagged at the square is false)
			buttons[x][y].setText("");
			Board.isFlagged[x][y]=false;
		}
	}
	
	//pre: none
	//post: no return, sets up music
	//sets up music so that computer can play the music if user pleases
	public void setClip() {
		try {
			//set inputstream to game music
			inputstream = AudioSystem.getAudioInputStream(new File("minesweeperaudio.wav").getAbsoluteFile());
			activeClip = AudioSystem.getClip();//set active clip to get audio
			activeClip.open(inputstream);//active clip gets the audio/inputstream
		} catch(Exception e) {}//input stream must be put in try and catch, but no catch needed as no errors will occur with the audio
	}	

	
	//pre: whether user wants music or not(true or false)
	//post: no return, starts or stops music
	//will start the music if user chose music, and will stop the music when the game is done
	public void playMusic(boolean play) {
		if(play)//if play is true, loop the music endlessly
			activeClip.loop(Clip.LOOP_CONTINUOUSLY);
		else//if play is false, or when the game ends, stop the music
			activeClip.stop();
	}
}
	

