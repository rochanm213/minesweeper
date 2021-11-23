/*
 * Name: Rochan Muralitharan
 * Date: June 11 2021
 * Filename: Menu.java
 * Purpose: Menu of the game
 */
package minesweeper;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class Menu extends JFrame implements ActionListener{
	
	JFrame frame = new JFrame("Main Menu");//frame for menu
	JButton easy = new JButton("Easy");//easy difficulty button
	JButton medium = new JButton("Medium");//medium difficulty button
	JButton hard = new JButton("Hard");//hard difficulty button
	JButton instructions = new JButton("Instructions");//instructions button
	JButton music = new JButton("Music");//Music button
	//instructions panel with html padding of 5 pixels
	JLabel instructionspanel = new JLabel("<html><p style=padding:5px;>" + "Choose the game difficulty. Click on a square to start the game. The number that is revealed when a square is clicked on is the number of bombs that are touching that square. AVOID THE BOMBS. Right-click a square to flag the square if you believe there is a bomb there. If all the square without a bomb are revealed, YOU WIN. Hit a bomb, YOU LOSE." + "</p></html>");
	JLabel title= new JLabel("MINESWEEPER");//title for menu screen
	IPanel background = new IPanel();//IPanel for background of menu screen
	boolean isInstructionsVisible=false;//checks if the instructions panel is visible
	boolean isMusic=false;//checks if the music button is selected

	//constructor for the menu
	public Menu() {
		frame.setSize(1000,500);//set size of frame(1000x500)
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);//set close option
		background.setLayout(null);//no Layout for this panel
		background.setBounds(0,0,1000,500);//set size of background(1000x500)
		frame.add(background);//add background panel to frame
		//set title font, colour and put it onto screen
		title.setFont(new Font("Monospaced", Font.BOLD, 65));
		title.setBounds(285, 100, 430, 100);
		title.setForeground(Color.WHITE);
		//easy button-add action listener, set bounds and make background blue and writing black
		easy.addActionListener(this);
		setButton(easy, Color.BLUE, Color.BLACK);
		easy.setBounds(350, 250, 75, 40);
		//medium button-add action listener, set bounds and make background green and writing black
		medium.addActionListener(this);
		setButton(medium, Color.GREEN, Color.BLACK);
		medium.setBounds(450, 250, 100, 40);
		//hard button-add action listener, set bounds and make background red and writing black
		hard.addActionListener(this);
		setButton(hard, Color.RED, Color.BLACK);
		hard.setBounds(575, 250, 75, 40);
		//instructions button-add action listener, set bounds and make background yellow and writing black
		instructions.addActionListener(this);
		setButton(instructions, Color.YELLOW, Color.BLACK);
		instructions.setBounds(435, 300, 130, 40);
		//set bounds of instruction panel, set writing to black and set background to transparent white colour
		instructionspanel.setBounds(300, 350, 400, 100);
		instructionspanel.setForeground(Color.BLACK);
		instructionspanel.setBackground(new Color (255,255,255,150));
		instructionspanel.setOpaque(true);
		//music button-add action listener, set bounds and make background white and writing black
		music.addActionListener(this);
		setButton(music, Color.WHITE, Color.BLACK);
		music.setBounds(900, 420, 90, 40);
		//add all buttons, title and instructions panel to panel
		background.add(title);
		background.add(easy);
		background.add(medium);
		background.add(hard);
		background.add(instructions);
		background.add(instructionspanel);
		background.add(music);
		instructionspanel.setVisible(false);//initially hide instructions panel(not visible)
		//add background panel to frame and make it visible
		frame.add(background);
		frame.setVisible(true);

	}
	
	//sets the background and foreground colours of the buttons(last 2 lines of code are for Mac as the buttons are different, take out for windows)
	//pre: button, background color, foreground color
	//post: no return, sets the colours for the button
	public void setButton(JButton button, Color a, Color b) {
		//sets given colours as background and foreground colours of buttons respectively
		button.setBackground(a);
		button.setForeground(b);
		//for mac button settings
		button.setOpaque(true);
		button.setBorderPainted(false);
	}
	
	//action listener for each button, determines what happens when a button is pressed
	//pre: event
	//post: no return, has different outcomes/functions for the different buttons that are pressed
	public void actionPerformed(ActionEvent event) {
		
		//if easy button pressed, set new board to 12x12 with 12 bombs, and set new Main and pass through whether the user wants music or not
		if(event.getSource()==easy) {
			new Board(12, 12);
			new Main(isMusic);
		}
		//if medium button pressed, set new board to 15x15 with 20 bombs, and set new Main and pass through whether the user wants music or not
		if(event.getSource()==medium) {
			new Board(15, 20);
			new Main(isMusic);
		}
		//if hard button pressed, set new board to 20x20 with 35 bombs, and set new Main and pass through whether the user wants music or not
		if(event.getSource()==hard) {
			new Board(20, 35);
			new Main(isMusic);
		}
		
		//if instructions button is pressed
		if(event.getSource()==instructions) {
			
			//if instructions panel is not visible, make the panel visible and set isInstructionsVisible to true
			if(!isInstructionsVisible) {
				instructionspanel.setVisible(true);
				isInstructionsVisible=true;
			}
			
			//if instructions panel is visible, hide the panel(not visible) and set isInstructionsVisible to false
			else {
				instructionspanel.setVisible(false);
				isInstructionsVisible=false;
			}	
		}
		//if music button is pressed
		if(event.getSource()==music) {
			//if music button is not selected, set isMusic to true(sent to main to start music) and set background colour to magenta
			if(!isMusic) {
				isMusic=true;
				music.setBackground(Color.MAGENTA);
			}
			//if music button is selection, set isMusic to false(sent to main to make sure music is not started) and set background colour to white
			else {
				isMusic=false;
				music.setBackground(Color.WHITE);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//call the menu constructor
		new Menu();
	}

}
