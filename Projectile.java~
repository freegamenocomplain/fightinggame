/*class Projectile
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class contains a projectile that can be created by certain attaacks
*/

//imports
import java.awt.Image;
import java.awt.Rectangle;

//class starts here
class Projectile {
  
  //variable declaration
  private int duration;
  private Image [] projectileImage;
  private int [] projectileAnim;
  private int [] projectileEndAnim;
  private int animationCounter;
  private int x;
  private int y;
  private int [] xVelocity;
  private int [] yVelocity;
  private Rectangle [][] hitbox;
  private String direction;
  private int lingerFrames;
  private boolean active;
  private int damage;
  private int shieldDamage;
  private int stunDuration;
  private int blockStun;
  private int hitInvul;
  private boolean launch;
  private int xKnockback;
  private int yKnockback;
  private boolean hitBoxActive;
  private String identifier;
  
  //constructor of the Projectile class, this takes in all necessary variables and assigns them to this object
  Projectile (Image [] projectileImage, int [] projectileAnim, int [] projectileEndAnim, int [] xVelocity, int [] yVelocity, int duration, int lingerFrames, Rectangle [][] hitbox, int damage, int stun, int blockStun, boolean launch, int xKnockback, int yKnockback, String identifier){
    this.projectileImage = projectileImage;
    this.projectileAnim = projectileAnim;
    this.projectileEndAnim = projectileEndAnim;
    this.xVelocity = xVelocity;
    this.yVelocity = yVelocity;
    this.hitbox = hitbox;
    this.animationCounter = 0;
    this.duration = duration;
    this.lingerFrames = lingerFrames;
    this.damage = damage;
    this.shieldDamage = damage;
    this.stunDuration = stun;
    this.blockStun = blockStun;
    this.launch = launch;
    this.xKnockback = xKnockback;
    this.yKnockback = yKnockback;
    this.hitInvul = 0;
    this.identifier = identifier;
    this.active = true;
  }
  //alternative copy constructor for the Projectile class, used to actually create a projectile to be used. 
  //Takes in another Projectile (projectileCopy) and makes this a copy of that Projectile, also assigning int x, int y (the location of this projectile) and the direction of this projectile
  Projectile (Projectile projectileCopy, int x, int y, String direction){
    this.projectileImage = projectileCopy.getProjectileImage();
    this.projectileAnim = projectileCopy.getProjectileAnim();
    this.projectileEndAnim = projectileCopy.getProjectileEndAnim();
    this.xVelocity = projectileCopy.getXVelocity();
    this.yVelocity = projectileCopy.getYVelocity();
    this.hitbox = projectileCopy.getHitBox();
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.animationCounter = -1;
    this.duration = projectileCopy.getDuration();
    this.lingerFrames = projectileCopy.getLingerFrames();
    this.damage = projectileCopy.getDamage();
    this.shieldDamage = projectileCopy.getShieldDamage();
    this.stunDuration = projectileCopy.getStunDuration();
    this.blockStun = projectileCopy.getBlockStun();
    this.launch = projectileCopy.launching();
    this.xKnockback = projectileCopy.getXKnockBack();
    this.yKnockback = projectileCopy.getYKnockBack();
    this.identifier = projectileCopy.getIdentifier();
    this.active = true;
    this.hitBoxActive = true;
  }
  
  /*
   * getHitBox
   * getter for hitbox
   * @param none
   * @return Rectangle 2D array hitbox, the raw hitbox of this object
   */
  public Rectangle[][] getHitBox () {
    return hitbox;
  }
  
  /*
   * getCurrentHitBox
   * alternate getter for hitbox
   * @param int j, the index of the hitbox array to be accessed
   * @return the current hitbox of this object, scaled to direction and the projectile's current location
   */
  public Rectangle getCurrentHitBox(int j){
    //make sure the animationCounter value is valid
    if (animationCounter == -1){
      animationCounter = 0;
    }
    
    //return the current hitbox of this object scaled to the current location of this object and it's direction
    if (((hitBoxActive) || (duration == lingerFrames)) && (hitbox[animationCounter][j] != null)){
      if (direction.equals("left")){
        return new Rectangle((int)(x+this.getImage().getWidth(null)-hitbox[animationCounter][j].getX()-hitbox[animationCounter][j].getWidth()), (int)(y+hitbox[animationCounter][j].getY()), (int)hitbox[animationCounter][j].getWidth(), (int)hitbox[animationCounter][j].getHeight());
      } else {
        return new Rectangle ((int)(x+hitbox[animationCounter][j].getX()), (int)(y+hitbox[animationCounter][j].getY()), (int)hitbox[animationCounter][j].getWidth(), (int)hitbox[animationCounter][j].getHeight());
      }
    }
    return new Rectangle (0, 0, 0, 0);
  }
  
  /*
   * process
   * make certain changes to this projectile 
   * @param none
   * @return boolean, false if this projectile has ended and needs to be removed
   */
  public boolean process(){
    
    //if projectile is active
    if (active){
      //increase it's animation counter if it's less than it's length
      if (animationCounter<projectileAnim.length-1){
        animationCounter++;
      }
      
      //move projectile based on it's velocity and direction
      if (direction.equals("left")){
        x-=xVelocity[animationCounter];
      } else {
        x+=xVelocity[animationCounter];
      }
      y+=yVelocity[animationCounter];
      
      //if projectile hits the ground, the projectile stops being active and it lingers on the screen based on it's lingerFrames
      if (y>950){
        y = 950;
        duration = lingerFrames+1;
        animationCounter = 0;
        active = false;
        hitBoxActive = false;
      }
    } else {
      //if projectile is inactive just increase animation counter and do not move it
      if (animationCounter<projectileEndAnim.length-1){
        animationCounter++;
      }
    }
    
    //if projectile is out of duration return false to remove it
    if (duration == 0){
      return false;
    } else if (duration != -1){ //if duration is -1 it means this projectile has infinite duration
      //decrease duration
      duration --;
    }
    
    //return true if this projectile is not to be removed
    return true;
  }
  
  /*
   * getProjectileImage
   * getter for projectileImage
   * @param none
   * @return Image array projecitleImage, the raw images array used in this projectile
   */
  public Image [] getProjectileImage(){
    return projectileImage;
  }
  
  /*
   * getProjectileAnim
   * getter for projectileAnim
   * @param none
   * @return int array projectileAnim, the animation array of this projectile
   */ 
  public int [] getProjectileAnim(){
    return projectileAnim;
  }
  
  /*
   * getXVelocity
   * getter for xVelocity
   * @param none
   * @return int array xVelocity, the horizontal velocity of this object (at all frames of the projectile)
   */ 
  public int [] getXVelocity(){
    return xVelocity;
  }
  
  /*
   * getYVelocity
   * getter for yVelocity
   * @param none
   * @return int yVelocity, the vertical velocity of this object
   */ 
  public int [] getYVelocity(){
    return yVelocity;
  }
  
  /*
   * addX
   * adds a value to the x location of this projectile
   * @param int add, the amount that is to be added to x
   * @return none
   */ 
  public void addX(int i){
    x+=i;
  }
  
  /*
   * getX
   * getter for x
   * @param none
   * @return int x, the horizontal location of this object
   */
  public int getX(){
    return x;
  }
  
  /*
   * getY
   * getter for y
   * @param none
   * @return int y, the vertical location of this object
   */ 
  public int getY(){
    return y;
  }
  
  /*
   * getDirection
   * getter for direction
   * @param none
   * @return String direction, the direction of this projectile
   */ 
  public String getDirection(){
    return direction;
  }
  
  /*
   * getDuration
   * getter for duration
   * @param none
   * @return int duration, the amount of frames this projectile lasts
   */ 
  public int getDuration(){
    return duration;
  }
  
  /*
   * getLingerFrames
   * getter for lingerFrames
   * @param none
   * @return int lingerFrames, the amount of frames this projectile stays on the screen for after becoming inactive
   */ 
  public int getLingerFrames(){
    return lingerFrames;
  }
  
  /*
   * getProjectileEndAnim
   * getter for projectileEndAnim
   * @param none
   * @return int array projectileEndAnim, the animation array for the end animation of the projectile (after it becomes inactive)
   */ 
  public int [] getProjectileEndAnim(){
    return projectileEndAnim;
  }
  
  /*
   * getDamage
   * getter for damage
   * @param none
   * @return int damage, the amount of damage this projectile deals
   */ 
  public int getDamage(){
    return damage;
  }
  
  /*
   * getShieldDamage
   * getter for shieldDamage
   * @param none
   * @return int shieldDamage, the amount of damage this projectile deals to block
   */ 
  public int getShieldDamage(){
    return shieldDamage;
  }
  
  /*
   * getStunDuration
   * getter for stunDuration
   * @param none
   * @return int stunDuration, the amount of frames this projectile stuns for on hit
   */ 
  public int getStunDuration(){
    return stunDuration;
  }
  
  /*
   * getBlockStun
   * getter for blockStun
   * @param none
   * @return int blockStun, the amount of frames this projectile stuns for on block
   */ 
  public int getBlockStun(){
    return blockStun;
  }
  
  /*
   * getHitInvul
   * getter for hitInvul
   * @param none
   * @return int hitInvul, the amount of frames of invulnerability the hit player is granted
   */ 
  public int getHitInvul(){
    return hitInvul;
  }
  
  /*
   * launching
   * getter for launch 
   * @param none
   * @return boolean launch, variable that determins if this projectile launches the opposing player on hit
   */ 
  public boolean launching(){
    return launch;
  }
  
  /*
   * getXKnockBack
   * getter for xKnockBack
   * @param none
   * @return int xKnockback, the distance this projectile knocks back on hit
   */ 
  public int getXKnockBack(){
    return xKnockback;
  }
  
  /*
   * getYKnockBack
   * getter for yKnockBack
   * @param none
   * @return int yKnockback, the vertical distance this projectile knocks back on hit
   */ 
  public int getYKnockBack(){
    return yKnockback;
  }
  
  /*
   * getIdentifier
   * getter for identifier
   * @param none
   * @return String identifier, the "name" of this projectile used for searching it with other methods
   */ 
  public String getIdentifier(){
    return identifier;
  }
  
  /*
   * getImage
   * getter for projectileImage
   * @param none
   * @return Image object which is the current image of this projectile
   */
  public Image getImage(){
    //make sure animationCounter is valid
    if (animationCounter == -1){
      animationCounter = 0;
    }
    
    //if projectile is inactive return image based on projectileEndAnim
    if (!active) {
      if (duration>=projectileEndAnim.length){
        return projectileImage[projectileEndAnim[0]];
      } else {
        return projectileImage[projectileEndAnim[projectileEndAnim.length-duration-1]];
      }
    } else {
      //otherwise return image based off of projectileAnim and animationCounter
      return projectileImage[projectileAnim[animationCounter]];
    }
  }
  
  /*
   * setHitBoxActive
   * setter for hitBoxActive
   * @param none
   * @return boolean hitBoxActive, boolean that determines if this projectile's hitbox is still in use
   */ 
  public void setHitBoxActive(){
    hitBoxActive = false;
  }
}