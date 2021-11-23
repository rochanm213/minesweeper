/*
 * Name: Rochan Muralitharan
 * Date: June 11 2021
 * Filename: IPanel.java
 * Purpose: Draws the background image of the menu screen
 */
package minesweeper;
import java.awt.*;
import javax.swing.*;
public class IPanel extends JPanel{
	
	
	Image back;//background image
	Image back2;//resized background image
	
	public IPanel() {//constructor
		super();
		//use toolkit to get image
		Toolkit kit = Toolkit.getDefaultToolkit();
		//get image
		back=kit.getImage("background.jpeg");
		//resize image to 1000x500 px
		back2=back.getScaledInstance(1000, 500, java.awt.Image.SCALE_SMOOTH);
	}
	
	//paints the background onto the panel
	//pre: comp(Graphics)
	//post: no return, paints the background onto the panel
	public void paintComponent(Graphics comp) {
		//set graphics to 2d graphics
		Graphics2D comp2D = (Graphics2D) comp;
		//paint the resized image onto the panel
		comp2D.drawImage(back2, 0, 0, this);
	}
}
