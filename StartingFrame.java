/*class StartingFrame
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this is the menu for the game, able to start the main game and display a controls page
 * this class extends JFrame
*/

//Imports
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;


//class starts here
class StartingFrame extends JFrame { 

  //this was here in the original template
  JFrame thisFrame;
  
  //Constructor - this runs first
  StartingFrame() { 
    
    //call to super
    super("Start Screen");
    this.thisFrame = this; //lol  
    
    //configure the window
    this.setSize(550,450);     
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    this.setResizable (false);
    
    //Create a Panel for stuff
    //this uses a decPanel which has a picture attached to it
    JPanel decPanel = new DecoratedPanel();
    decPanel.setBorder(BorderFactory.createEmptyBorder());
    decPanel.setLayout(null);
    
    //Create a JButton whose purpose is to open the main game
    ImageIcon sb =new ImageIcon("button1.png");
    JButton startButton = new JButton(sb);
    startButton.setBackground(new Color(0, 0, 0, 0));
    startButton.setBorder(BorderFactory.createEmptyBorder());
    startButton.setFocusPainted(false);
    startButton.addActionListener(new StartButtonListener());
    startButton.setBounds(300, 190, 240, 60);
    
    //Create a JButton whose purpose is to open the controls page
    ImageIcon cb =new ImageIcon("button2.png");
    JButton controlButton = new JButton(cb);
    controlButton.setBackground(new Color(0, 0, 0, 0));
    controlButton.setBorder(BorderFactory.createEmptyBorder());
    controlButton.setFocusPainted(false);
    controlButton.addActionListener(new ControlButtonListener());
    controlButton.setBounds(300, 260, 240, 60);
    
    //Create a JButton whose purpose is to close the program
    ImageIcon eb =new ImageIcon("button3.png");
    JButton exitButton = new JButton(eb);
    exitButton.setBackground(new Color(0, 0, 0, 0));
    exitButton.setBorder(BorderFactory.createEmptyBorder());
    exitButton.setFocusPainted(false);
    exitButton.addActionListener(new ExitButtonListener());
    exitButton.setBounds(300, 330, 240, 60);
    
    //Add all buttons to the panel
    decPanel.add(startButton);
    decPanel.add(controlButton);
    decPanel.add(exitButton);
    
    //add the main panel to the frame
    this.add(decPanel);
    
    //Start the app
    this.setVisible(true);
  }
  
  //INNER CLASS - Overide Paint Component for JPANEL
  private class DecoratedPanel extends JPanel {
    
    DecoratedPanel() {
      this.setBackground(new Color(0,0,0,0));
    }
    
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);     
        Image pic = new ImageIcon( "menuScreen.png" ).getImage();
        g.drawImage(pic,0,20,null); 
   }
  }
  
  //These are inner classes that is used to detect a button press
  //this ActionListener is used to start the main game
  class StartButtonListener implements ActionListener {  //this is the required class definition
    /*
     * actionPerformed
     * this method disposes of this frame and starts the main game
     * @param none
     * @return none
     */ 
    public void actionPerformed(ActionEvent event)  {  
      System.out.println("Starting new Game");
      thisFrame.dispose();
      new GameFrame(); //create a new GameFrame (another file that extends JFrame)
    }
  }
  //this ActionListener is used to open the controls page
  class ControlButtonListener implements ActionListener {  //this is the required class definition
    /*
     * actionPerformed
     * this method starts the controls frame
     * @param none
     * @return none
     */ 
    public void actionPerformed(ActionEvent event)  {  
      new InformationFrame(); //create a new InformationFrame (another file that extends JFrame)
    }
  }
  //this ActionListener is used to close this program
  class ExitButtonListener implements ActionListener {  //this is the required class definition
    /*
     * actionPerformed
     * this method closes this program
     * @param none
     * @return none
     */ 
    public void actionPerformed(ActionEvent event)  { 
      //close the program
      System.exit(0);
    }
  }
  
  //Main method starts this application
  public static void main(String[] args) { 
    new StartingFrame();

  }
  
}