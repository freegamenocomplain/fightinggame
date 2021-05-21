/**
 * This template can be used as reference or a starting point
* for your final summative project
* @author Mangat
**/

/*class GameFrame
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this is the main frame that runs the actual game and contains the game loop
 * this class extends JFrame
*/

//Graphics &GUI imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
  
//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

//Other imports
import java.util.ArrayList;
import java.io.File;
import java.util.Random;

// class starts here
class GameFrame extends JFrame { 

  //declare the inner class gamePanel
  private static GameAreaPanel gamePanel;
  
  //double nextTick, used to store the time the next frame occurs
  private double nextTick;
  
  //Variables to store the player objects
  private Player player1;
  private Player player2;
  
  //input arrays, Array used to record the keys pressed in the time between the last frame and this frame. One is used for each player
  //these act as a "buffer" for the inputs of the 2 players, making sure they are processed at the same time in a frame and allowing for multi key commands
  //index:
  //0 - up
  //1 - down
  //2 - right
  //3 - left
  //4 - light attack
  //5 - heavy attack
  //6 - grab
  //7 - special attack
  //8 - block
  //9 - super attack
  private boolean [] input;
  private boolean [] input2;
  
  //InGame and Winner variables to track if game is currently going on and who won the game if the game is over
  private boolean inGame = true;
  private boolean starting;
  private boolean loading;
  private boolean paused;
  private String winner;
  
  //ArrayList to record all of the effects currently active in the game
  private ArrayList<Effect> effects = new ArrayList<Effect>();
  //ArrayList to record all of the messages currently active in the game
  private ArrayList<Image> messages = new ArrayList<Image>();
  
  //image variables to store various images needed for the game
  private Image [] pauseEffects = new Image [2];
  private Image background;
  private Image loadingScreen;
  private Image [] startMessage = new Image [4];
  private Image [] endMessage = new Image[4];
  
  //boolean to track if hitboxes are to be drawn or not
  private boolean drawHitbox;
  
  //variables to store the screen size (currently only supports 1920x1080
  private int screenWidth;
  private int screenHeight;
  
  //variable that points to this JFrame
  private JFrame thisFrame;
  
  //clip that stores the background music
  private Clip music;
  
  //Constructor - this runs first
  GameFrame() { 
    
    //call to super
    super("Stick Fighter");  
    this.thisFrame = this; //lol
    
    //set screen width and height
    screenWidth = 1920;
    screenHeight = 1080;
    Dimension screenSize = new Dimension(1920, 1080);
    
    //load the loading screen image (this is done here so it will be already loaded when the loading screen is required for loading other stuff)
    try {
     loadingScreen = ImageIO.read(new File("loadingScreen.png"));
    } catch (Exception E){}
    
    // Set the frame to full screen 
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(screenSize);
    thisFrame.setResizable(false);


    
    //Set up the game panel (where we put our graphics)
    gamePanel = new GameAreaPanel();
    this.add(new GameAreaPanel());
    
    //set up the keyListener to enable keyboard input
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);

    this.requestFocusInWindow(); //make sure the frame has focus   
    
    this.setVisible(true);
    
    //initiate the input arrays
    input = new boolean[10];
    input2 = new boolean[10];
      
    //set the time for the next frame to be 33 milliseconds after this (so it maintains 30 fps
    nextTick = System.currentTimeMillis() + 33;
    
    //set loading to true and not to draw hitboxes
    loading = true;
    drawHitbox = false;
    
    //Start the game loop in a separate thread
    Thread t = new Thread(new Runnable() { public void run() { animate(); }}); //start the gameLoop 
    t.start();
   
  } //End of Constructor

  //the main gameloop - this is where the game state is updated
  /*
   * animate
   * this is the method where the main game loop takes place
   * @param none
   * @return none
   */ 
  public void animate() { 
    
    //initiate the 2 players on the opposite sides of the screen facing each other, player 1 being black and player2 being blue
    player1 = new Player(200, -500, "right", 1);
    player2 = new Player(1200, -500, "left", 2);
    
    //set paused to false
    paused = false;
    
    //load up the music
    Random rand = new Random();
    File musicFile = new File("music"+(rand.nextInt(5)+1)+".wav");
    try {
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
      DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
      music = (Clip) AudioSystem.getLine(info);
      music.open(audioStream);
      music.start();
    }catch(Exception e){}
    
    //load all of the various images needed for the game
    try {
     background = ImageIO.read(new File("Background2.png"));
    } catch (Exception E){}
    try {
      pauseEffects [0] = ImageIO.read(new File("pause.png"));
      pauseEffects [1] = ImageIO.read(new File("pauseMessage.png"));
    } catch (Exception E){}
    for (int i = 1; i<5; i++){
      try {
        startMessage [i-1] = ImageIO.read(new File("startMessage000"+i+".png"));
      } catch (Exception e){}
    }
    for (int i = 1; i<5; i++){
      try {
        endMessage [i-1] = ImageIO.read(new File("endMessage000"+i+".png"));
      } catch (Exception e){}
    }
    
    //loading finished, set loading to false so loading screen can go away
    loading = false;
    //booleans used in the starting animations of the game
    starting = true;
    boolean player1Completed = false;
    boolean player2Completed = false;
    int counter = 0;
    
    //loop for the starting animations
    while(starting){
      
      //call the process start methods in the players to go through their start animations
      //the processStart methods return a boolean, true when it has finished going through the fill starting animations
      if ((player1.processStart())){
        //when the first player finishes their starting animation it goes onto the 2nd player's starting animation
        if ((player2.processStart())){
          
          //when the 2nd player finishes, a counter that goes for 4 seconds (120 frames) starts and adds a countdown image to the messages array to be displayed depending on the amount of time the counter has gone for
          counter++;
          messages.clear();
          if (counter<30){
            messages.add(startMessage[0]);
          } else if (counter<60){
            messages.add(startMessage[1]);
          } else if (counter<90){
            messages.add(startMessage[2]);
          } else if (counter<120){
            messages.add(startMessage[3]);
          } else {
            starting = false;
          }
        }
      } 
      
      //calculate the sleepTime (delay until the next frame) depending on the current time and the planned next frame
      int sleepTime = (int)(nextTick - System.currentTimeMillis());
      //if there is still time left until the next planned frame activate a delay
      if (sleepTime>=0){
        try{ Thread.sleep(sleepTime);} catch (Exception exc){}  //delay
      }
      //calculate the next frame and update the screen
      nextTick = System.currentTimeMillis() + 33;
      this.repaint();
    }
    
    //clear out the input arrays before the main game starts
    input = new boolean[10];
    input2 = new boolean[10];
    
    //main game loop where the 2 players do their battle, it ends when one of the player's health reaches 0
    while(inGame){
      
      //check if the game is paused
      if (!paused){
        
        //call the various processing methods of the players in a specific order (the ordering is important to make sure the 2 players are synced up)
        //first call processAerial and animationCounter
        player1.processAerial();
        player2.processAerial();
        player1.processAnimationCounter();
        player2.processAnimationCounter();
        
        //then take the input array gathered from the last frame to this frame and call the processInput of the players with it as an argument
        player1.processInput(input);
        //clear out the input arrays to obtain new inputs for the next frame
        input = new boolean[10];
        player2.processInput(input2);
        input2 = new boolean[10];
        
        //at this point, determine if the first player is to the left or right of the 2nd player. this is used later in collision detection to make sure the players do not plase through each other
        String playerLocation = "right";
        if (player1.getX()<player2.getX()){
          playerLocation = "left";
        }
        
        //call the rest of the various methods in the player class in order
        player1.processResources();
        player2.processResources();
        player1.processMovement(screenWidth);
        player2.processMovement(screenWidth);
        player1.processProjectiles(player2);
        player2.processProjectiles(player1);
        player1.processHitBox();
        player2.processHitBox();
        if (!(player1.getPhase()) && !(player2.getPhase())){
          calculateCollision(player1, player2, playerLocation); //call collision detection
        }
        player1.processHitBox();
        player2.processHitBox();
        player1.processHits(player2, effects);
        player2.processHits(player1, effects);
        
        //if a player is hit by an attack cancel their current attack, this is done here to make sure player 1 doesn't win all same frame trades
        if (player1.getStatus().equals("hurt")){
          player1.setAttack();
        }
        if (player2.getStatus().equals("hurt")){
          player2.setAttack();
        }
        
        //loop through the effects array and call process on all of it's content
        for (int i = 0; i<effects.size(); i++){
          //Effect.processEffect returns a boolean that determins if the effect is completed, if that is false remove the effect
          if (!effects.get(i).processEffect()) {
            effects.remove(i);
            i--;
          } 
        }
      } else {
        //even when paused, clear out the input arrays to make sure no commands made during pause transfer to the game after unpause
        input = new boolean[10];
        input2 = new boolean[10];
      }
      
      //do the delay thing detailed earlier in the starting loop to maintain 30fps
      int sleepTime = (int)(nextTick - System.currentTimeMillis());
      if (sleepTime>=0){
        try{ Thread.sleep(sleepTime);} catch (Exception exc){}  //delay
      }
      nextTick = System.currentTimeMillis() + 33;
      this.repaint();
      
      //Detect if a player's health reached 0, and if so stop the game and determine the winner based on which player's health is 0
      if ((player1.getHealth()<=0) && (player2.getHealth()<=0)){
        winner = "DRAW";
        //call the setWin method in the players to make inform them if they've won or lost
        player1.setWin(false);
        player2.setWin(false);
        inGame = false;
      } else if (player1.getHealth()<=0){
        winner = "PLAYER 2";
        player1.setWin(false);
        player2.setWin(true);
        inGame = false;
      } else if (player2.getHealth()<=0){
        winner = "PLAYER 1";
        player1.setWin(true);
        player2.setWin(false);
        inGame = false;
      }
    }    
    
    //reset the counter
    counter = 0;
    
    //postgame loop that plays the ending animations, it continues until the ESC key is pressed
    while(true){
      //increase counter
      counter++;
      
      //call the processEnd methods in the players to go through their ending animations
      if (player1!=null){
        player1.processEnd(screenWidth, player2, effects);
      }
      if (player2!=null){
        player2.processEnd(screenWidth, player1, effects);
      }
      
      //continue processing any effects on screen
      for (int i = 0; i<effects.size(); i++){
          if (effects.get(i).processEffect() == false) {
            effects.remove(i);
            i--;
          } 
        }
      
      //display messages corrisponding to the winner of the game
      messages.clear();
      if (counter<60){
        messages.add(endMessage[0]);
      } else {
        if (winner.equals("PLAYER 1")){
          messages.add(endMessage[1]);
        } else if (winner.equals("PLAYER 2")){
          messages.add(endMessage[2]);
        } else if (winner.equals("DRAW")){
          messages.add(endMessage[3]);
        }
      }
      
      //same delay thing again
      int sleepTime = (int)(nextTick - System.currentTimeMillis());
      if (sleepTime>=0){
        try{ Thread.sleep(sleepTime);} catch (Exception exc){}  //delay
      }
      nextTick = System.currentTimeMillis() + 33;
      this.repaint();
    }
  }
  
  /*
   * calculateCollision
   * this method acts as the collision detection between players, and pushes each other away if their hurtboxes collide. this is done after the players moved
   * @param Player objects player and otherplayer, the 2 players to calculate collision between and location, the location of player compared to otherplayer before they moved
   * @return none
   */ 
  public void calculateCollision (Player player, Player otherplayer, String location){
    //check that both players current have hurtboxes
    if((player.getHurtBox() != null) && (otherplayer.getHurtBox() != null)){
      //loop through all collision relevant hurtboxes (this is currently set to only the first hurtbox, which is usually the "main" hurtbox that encompasses the body.
      //these loops are left here despite that for future proofing, because there may be more complex collision hurtboxes later
      for (int i = 0; i<1; i++){
        //check that the players have hurtboxes again (this time for the location in the array)
        if (player.getHurtBox()[i]!=null){
          for (int j = 0; j<1; j++){
            if (otherplayer.getHurtBox()[j]!=null){
              //determine if the hurtboxes collide
              if (player.getHurtBox()[i].intersects(otherplayer.getHurtBox()[j])){
                //if the first player is to the left of the 2nd player (this is to be kept so the players don't phase through each other)
                if (location.equals("left")){
                  //calculate the overlap of the 2 hurtboxes
                  int overlap = (int)((player.getHurtBox()[i].getX()+player.getHurtBox()[i].getWidth()) - otherplayer.getHurtBox()[j].getX());
                  if (overlap>=1){
                    //push the first player left and the 2nd player right
                    player1.addX(-(overlap/2));
                    player2.addX(overlap/2);
                  }
                }
                //if the first player is to the right of the 2nd player
                if (location.equals("right")){
                  int overlap = (int)((otherplayer.getHurtBox()[j].getX()+otherplayer.getHurtBox()[j].getWidth()) - player.getHurtBox()[i].getX());
                  if (overlap>=1){
                    //push the first player right and the 2nd player left
                    player1.addX(overlap/2);
                    player2.addX(-(overlap/2));
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  /** --------- INNER CLASSES ------------- **/
  
  // Inner class for the the game area - This is where all the drawing of the screen occurs
  private class GameAreaPanel extends JPanel {
    
    /*
     * paintComponent
     * this overwirtes the paintComponent in JPanel to display the content of this game
     * @param Graphics g, which is required
     * @return none
     */ 
    public void paintComponent(Graphics g) {   
      super.paintComponent(g); //required
      setDoubleBuffered(true);
      
      //draw the loading screen if game is loading
      if (loading) {
        g.drawImage(loadingScreen, 0, -30, this);
      } else {
        //draw the background of the game
        g.drawImage(background, 0, -30, 2160, 1080, this);
        
         //if there are any images in the messages array, display them
        for (int i = 0; i<messages.size(); i++){
          g.drawImage(messages.get(i), screenWidth/2-(messages.get(i).getWidth(null)/2), screenHeight/2-(messages.get(i).getHeight(null)/2), this);
        }
        
        //display the resource bars (HP, MP and BP)
        displayResourceBars(g, player1, player2);
        
        //display the 2 players
        displayPlayer(player1, g);
        displayPlayer(player2, g);
        
         //if hitboxes are to be displayed display them
        if (drawHitbox){
          displayHitbox(player1, g);
          displayHitbox(player2, g);
        }
        
        //display the projectiles
        displayProjectiles(player1, g);
        displayProjectiles(player2, g);
        
        //display the effects
        displayEffects(g);
        
        //if game is paused display the pause screen
        if (paused){
          g.drawImage(pauseEffects[0], 0, 0, this);
          g.drawImage(pauseEffects[1], screenWidth/2-(pauseEffects[1].getWidth(null)/2), screenHeight/2-(pauseEffects[1].getHeight(null)/2), this);
        }
      }
    }
      
    /*
     * displayProjectiles
     * method that loops through the projectiles ArrayList of a player and displays them
     * @param Player object player, and Graphics g (required)
     * @return none
     */ 
    public void displayProjectiles(Player player, Graphics g){
        
      //loops through the player's projectiles ArrayList and draw them to the screen depending on their direction
      for (int i = 0; i<player.getProjectiles().size(); i++){
        //if the projectile is facing the left direction negative scale it's width to reflect the image
        if (player.getProjectiles().get(i).getDirection().equals("left")){
          //the getImage method of the projectile class provides the image
          g.drawImage(player.getProjectiles().get(i).getImage(), player.getProjectiles().get(i).getX()+player.getProjectiles().get(i).getImage().getWidth(null), player.getProjectiles().get(i).getY(), -player.getProjectiles().get(i).getImage().getWidth(null), player.getProjectiles().get(i).getImage().getHeight(null), this);
        } else {
          g.drawImage(player.getProjectiles().get(i).getImage(), player.getProjectiles().get(i).getX(), player.getProjectiles().get(i).getY(), this);
        }
      }
    }
    
    /*
     * displayEffects
     * method that loops through the effects array and draws the effects
     * @param Graphics g (required)
     * @return none
     */ 
    public void displayEffects (Graphics g){
      
      //loop though the effects array and draw them to the screen
      for (int i = 0; i<effects.size(); i++){
        //negative scale if facing left
        if (effects.get(i).getDirection().equals("left")){
          g.drawImage(effects.get(i).getImage(), effects.get(i).getX()+effects.get(i).getImage().getWidth(null), effects.get(i).getY(), -effects.get(i).getImage().getWidth(null), effects.get(i).getImage().getHeight(null), this);
        } else {
          g.drawImage(effects.get(i).getImage(), effects.get(i).getX(), effects.get(i).getY(), this);
        }
      }
    }
    
    /*
     * displayResourceBars
     * method that displays the Health, Mana and Barrier points of the 2 players
     * @param Player object player and otherplayer, the 2 players whose resource bars are to be displayed and Graphics g (required)
     * @return none
     */ 
    public void displayResourceBars(Graphics g, Player player, Player otherplayer){
      
      //draw the outlines of the health, mana and barrier bars based on the maximum health/mana/barrier of the players
      g.drawRect(10, 10, player.getMaxHP()/3, 50);
      g.drawRect(10, 60, player.getMaxMP(), 30);
      g.drawRect(10, 90, player.getMaxBP()/3, 20);
      g.drawRect(screenWidth-30-(otherplayer.getMaxHP()/3), 10, otherplayer.getMaxHP()/3, 50);
      g.drawRect(screenWidth-30-otherplayer.getMaxMP(), 60, otherplayer.getMaxMP(), 30);
      g.drawRect(screenWidth-30-(otherplayer.getMaxBP()/3), 90, otherplayer.getMaxBP()/3, 20);
      
      //fill the resource bars depending on the current health/mana/barrier of the players
      Graphics2D g2d = (Graphics2D) g;
      //set color to red and draw health bars
      g2d.setColor(new Color(255, 0, 0));
      g2d.fillRect(10, 10, player.getHealth()/3, 50);
      g2d.fillRect(screenWidth-30-(otherplayer.getHealth()/3), 10, otherplayer.getHealth()/3, 50);
      //set color to blue and draw mana bars
      g2d.setColor(new Color(0, 0, 255));
      g2d.fillRect(10, 60, player.getMP(), 30);
      g2d.fillRect(screenWidth-30-(otherplayer.getMP()), 60, otherplayer.getMP(), 30);
      //set color to green and draw barrier bars
      g2d.setColor(new Color(0, 255, 0));
      g2d.fillRect(10, 90, player.getBP()/3, 20);
      g2d.fillRect(screenWidth-30-(otherplayer.getBP()/3), 90, otherplayer.getBP()/3, 20);
    }
    
    /*
     * displayPlayer
     * method that displays the images of the players to the screen
     * @param Player object currPlayer, the player to be displayed and Graphics g (required)
     * @return none
     */ 
    public void displayPlayer(Player currPlayer, Graphics g){
      //draw the player's current sprite (given by the getImage method of the players) to the screen
      if (currPlayer.getDirection() == "left"){
        //negative scale if left
        g.drawImage(currPlayer.getImage(),currPlayer.getX()+currPlayer.getImage().getWidth(null),currPlayer.getY(), -currPlayer.getImage().getWidth(null), currPlayer.getImage().getHeight(null), this);
      } else {
        g.drawImage(currPlayer.getImage(),currPlayer.getX(),currPlayer.getY(), this);
      }
    }
    
    /*
     * displayHitbox
     * method that displays the hitboxes and hurtboxes of the players to the screen
     * @param Player object currPlayer, the player whose hitboxes are to be displayed and Graphics g (required)
     * @return none
     */ 
    public void displayHitbox(Player currPlayer, Graphics g){
      //loop through the hitbox array of the player and draw rectangles on the screen equivalent to the hitboxes
      if (currPlayer.getHitBox() != null){
        for(int i = 0; i<currPlayer.getHitBox().length; i++){ 
          if (currPlayer.getHitBox()[i] != null){
            g.drawRect((int)currPlayer.getHitBox()[i].getX(), (int)currPlayer.getHitBox()[i].getY(), (int)currPlayer.getHitBox()[i].getWidth(), (int)currPlayer.getHitBox()[i].getHeight());
          }
        }
      }
      //loop through the projectiles of the player and draw their hitboxes to the screen
      for (int i = 0; i<currPlayer.getProjectiles().size(); i++){
        if (currPlayer.getProjectiles().get(i).getHitBox()!=null){
          for(int j = 0; j<currPlayer.getProjectiles().get(i).getHitBox()[0].length; j++){
            g.drawRect((int)currPlayer.getProjectiles().get(i).getCurrentHitBox(j).getX(), (int)currPlayer.getProjectiles().get(i).getCurrentHitBox(j).getY(), (int)currPlayer.getProjectiles().get(i).getCurrentHitBox(j).getWidth(), (int)currPlayer.getProjectiles().get(i).getCurrentHitBox(j).getHeight());
          }
        }
      }
      //loop through the hurtboxes of the player and draw them to the screen
      if (currPlayer.getHurtBox() != null){
        for(int i = 0; i<currPlayer.getHurtBox().length; i++){ 
          if (currPlayer.getHurtBox()[i] != null){
            g.drawRect((int)currPlayer.getHurtBox()[i].getX(), (int)currPlayer.getHurtBox()[i].getY(), (int)currPlayer.getHurtBox()[i].getWidth(), (int)currPlayer.getHurtBox()[i].getHeight());
          }
        }
      }
    }
  }
    
    // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
  private class MyKeyListener implements KeyListener {
    
    /*
     * keyTyped
     * does nothing, just here to overwrite the interface
     * @param KeyEvent e (required)
     * @return none
     */ 
    public void keyTyped(KeyEvent e) {
    }
    
    /*
     * keyPressed
     * method that detects key presses and updates the input arrays accordingly
     * @param KeyEvent e (required)
     * @return none
     */ 
    public void keyPressed(KeyEvent e) {

      //detects if any of the specific keys are pressed and update player 1's input array if they are (key bindings are shown in controls)
      if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {  
        input[2] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
        input[3] = true;
      }if (KeyEvent.getKeyText(e.getKeyCode()).equals("W")) {
        input[0] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("S")) {
        input [1] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("J")) {
        input[4] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("K")) {
        input[5] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("I")) {
        input[7] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("U")) {
        input[6] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("L")) {
        input[8] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("O")) {
        input[9] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("Backspace")) { //if backspace is pressed toggle the drawHitbox variable
        if (drawHitbox){
          drawHitbox = false;
        } else {
          drawHitbox = true;
        }
      } if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
        //if the game is already over stop the music, dispose of this frame and go back to the menu (StartingFrame)
        if (!inGame){
          music.stop();
          player1 = null;
          player2 = null;
          thisFrame.dispose();
          new StartingFrame();
        } else {
          //otherwise toggle the pause variable
          if (paused){
            paused = false;
          } else {
            paused = true;
          }
        }
      }
      
      //same key detection but for player 2 with their own input array and key bindings (see controls for bindings)
      if (KeyEvent.getKeyText(e.getKeyCode()).equals("Right")) {  //If 'D' is pressed
        input2[2] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("Left")) {
        input2[3] = true;
      }if (KeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {
        input2[0] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
        input2[1] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("NumPad-0")) {
        input2[4] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("NumPad .")) {
        input2[5] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("NumPad-3")) {
        input2[7] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("NumPad-2")) {
        input2[6] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("NumPad-1")) {
        input2[8] = true;
      } if (KeyEvent.getKeyText(e.getKeyCode()).equals("Enter")) {
        input2[9] = true;
      }
    }   
    
    /*
     * keyReleased
     * method that detects key releases and updates some variables accordingly. This allows for smooth movement and toggleing of block, crouch e.t.c.
     * @param KeyEvent e (required)
     * @return none
     */ 
    public void keyReleased(KeyEvent e) {
      if(!(starting) && (player1!= null) && (player2!= null)){
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) { //if either of the direction keys are released, remove it from the input array and set the player's movingRight variable to false (this variable keeps track if the right key is pressed)
          input [2] = false;
          player1.setMovingRight();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) { //do the same for left
          input [3] = false;
          player1.setMovingLeft();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("S")) { //do the same for crouch
          input [1] = false;
          player1.setCrouch();
          player1.setLastStatus("crouching"); //set the lastStatus varible, this allows for ending animations for various movements
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("L")) { //do the same for block
          input[8] = false;
          player1.setBlock();
        }
        
        //Repeat above process for the 2nd player
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("Right")) {
          input2[2] = false;
          player2.setMovingRight();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("Left")) {
          input2[3] = false;
          player2.setMovingLeft();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
          input2[1] = false;
          player2.setCrouch();
          player2.setLastStatus("crouching");
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("NumPad-1")) {
          input2[8] = false;
          player2.setBlock();
        }
      }
    }
  } //end of keyboard listener
}