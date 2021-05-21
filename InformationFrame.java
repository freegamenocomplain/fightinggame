/*class InformationFrame
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class displays a window where the controls are displayed
 * this class extends JFrame
*/

//imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

//class starts here
class InformationFrame extends JFrame{
  
  //Constructor - this runs first
  InformationFrame(){
    
    //call to super
    super("Controls");
    
    //configure the window
    this.setSize(1000,1000);     
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setResizable (false);
    
    //Create a Panel for stuff
    //this uses a decPanel which has a picture attached to it
    JPanel decPanel = new DecoratedPanel();
    decPanel.setBorder(BorderFactory.createEmptyBorder());
    decPanel.setLayout(null);
    
    //add the main panel to the frame
    this.add(decPanel);
    
    //Start the app
    this.setVisible(true);
  }
  
  //INNER CLASS - Overide Paint Component for JPANEL
  //same one as in startingFrame but with a different picture
  private class DecoratedPanel extends JPanel {
    private BufferedImage image;
    DecoratedPanel() {
      try {
        image = ImageIO.read(new File("controls.png"));
      } catch (Exception e) {}
      this.setBackground(new Color(0,0,0,0));
    }
    public void paintComponent(Graphics g) { 
      super.paintComponent(g);     
      g.drawImage(image,0,-20,null); 
    }
  }
}