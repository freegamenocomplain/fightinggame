/* class Grab
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * a special type of attack that can be used by the player
 * this class extends Attack class and implements IndicatorAttack interface (visit the IndicatorAttack interface for an explination to this type of attack
*/

//imports 
import java.awt.Rectangle;
import java.util.ArrayList;
import java.lang.Math;

//class starts here
class Grab extends Attack implements IndicatorAttack{
  
  //variable declaration
  private int [] xMove2;
  private int [] yMove2;
  private int [] enemyX;
  private int [] enemyY;
  private int [] atkAnim2;
  private int damageFrame;
  private boolean success;
  private int grabDamage;
  private int xKnockBack;
  private int yKnockBack;
  private int stun2;
  private int directionChangeFrame;
  private Effect [][] effect;
  private int [][][] effectLocation;
  
  //constructor for the Grab, which takes in all necessary variables and assigns them to this object
  Grab (int [] atkAnim, int [] atkAnim2, boolean air, Rectangle [][] hit, Rectangle [][] hurt, int damage, int stun, int selfStun, int xKnockback, int yKnockback, int []xMove, int[]yMove, int []xMove2, int []yMove2, int []enemyX, int []enemyY, int damageFrame, int directionChangeFrame, Effect [][] effect, int [][][] effectLocation){
    //call to super constructor to make an attack (the indicator attack always does 0 damage and has 0 knockback)
    super(atkAnim, air, hit, hurt, 0, stun, selfStun, 0, 2, true, 0, 0, xMove, yMove, null, false, 0, false, null, effect, effectLocation, null, null);
    this.atkAnim2 = atkAnim2;
    this.xMove2 = xMove2;
    this.yMove2 = yMove2;
    this.enemyX = enemyX;
    this.enemyY = enemyY;
    this.damageFrame = damageFrame;
    this.grabDamage = damage;
    this.xKnockBack = xKnockback;
    this.yKnockBack = yKnockback;
    this.stun2 = stun-damageFrame;
    this.directionChangeFrame = directionChangeFrame;
    this.effect = new Effect [effect.length][effect[0].length];
    this.effectLocation = new int [effectLocation.length][effectLocation[0].length][2];
    success = false;
  }
  
  /*
   * successful
   * getter for success 
   * @param none
   * @return success, if the attack has successfully hit the target
   */ 
  public boolean successful(){
    return success;
  }
  
  /*
   * hit
   * method that is ran when the attack hits
   * @param Player object, the player using this attack
   * @return none
   */ 
  public void hit(Player player){
    //reset player's animationCounter
    player.setAnimationCounter(0);
    //set this attack to be successful
    success = true;
    //stuns the player for the 2nd part of the attack
    player.setStun(atkAnim2.length-1);
  }
  
  //this attack overwrites a number of getters from the superclass to return values depending on if this attack is successful or not
  
  /*
   * getAttackAnim
   * overwirte getter for atkAnim
   * @param int i, the index of the atkAnim array required
   * @return atkAnim int value which comes from the atkAnim2 variable if the attack is successful and the attackAnim of the superclass if not
   */ 
  public int getAttackAnim(int i){
    if (success == true){
      return atkAnim2[i];
    }
    return super.getAttackAnim(i);
  }
  
   /*
   * getXMove
   * overwirte getter for xMove 
   * @param int i, the index of the xMove array needed
   * @return xMove int value which comes from the xMove2 variable if this attack is successful and the xMove of the superclass if not
   */ 
  public int getXMove(int i){
    if (success == true){
      if (xMove2 != null){
        return xMove2[i];
      } 
      return 0;
    }
    return super.getXMove(i);
  }
  
  /*
   * getYMove
   * overwirte getter for yMove 
   * @param int i, the index of the yMove array needed
   * @return yMove int value which comes from the yMove2 variable if this attack is successful and the yMove of the superclass if not
   */
  public int getYMove(int i){
    if (success == true){
      if (yMove2 != null){
        return yMove2[i];
      } 
      return 0;
    }
    return super.getYMove(i);
  }
  
  /*
   * getLength
   * overwirte getter for Lenfth
   * @param none
   * @return length int value which comes from the atkAnim2 variable if this attack is successful and the length of the superclass if not
   */
  public int getLength(){
    if (success == true){
      return atkAnim2.length-1;
    }
    return super.getLength();
  }
  
  /*
   * getHitbox
   * overwirte getter for hitbox 
   * @param none
   * @return null if this attack is successful and the hitbox of the superclass if not
   */
  public Rectangle[][] getHitBox () {
    if (success == true){
      return null;
    }
    return super.getHitBox();
  }
  
  /*
   * getLHitBox
   * overwirte getter for leftHitBox
   * @param none
   * @return null if this attack is successful and the leftHitBox of the superclass if not
   */
  public Rectangle[][] getLHitBox(){
    if (success == true){
      return null;
    }
    return super.getLHitBox();
  }
  
  /*
   * Effect
   * overwirte getter for effect
   * @param int i, the index of the effect array needed
   * @return effect of the superclass if this attack is successful and null if not
   */
  public Effect[] getEffect(int i){
    if (success == true){
      return super.getEffect(i);
    }
    return null;
  }
  
  /*
   * getEffectX
   * overwirte getter for effectLocation
   * @param int i, j the index of the effectLocation array needed
   * @return int value of the effectLocation variable if this attack is successful and 0 if not
   */
  public int getEffectX(int i, int j){
    if (success == true){
      return effectLocation[i][j][0];
    }
    return 0;
  }
  
  /*
   * getEffectY
   * overwirte getter for effectLocation
   * @param int i, j the index of the effectLocation array needed
   * @return int value of the effectLocation variable if this attack is successful and 0 if not
   */
  public int getEffectY(int i, int j){
    if (success == true){
      return effectLocation[i][j][1];
    }
    return 0;
  }
  
  /*
   * getHurtBox
   * overwirte getter for hurtbox 
   * @param int idx, the index of the hurtbox array needed
   * @return a hitbox of 1, 1, 1, 1 if this attack is successful and the hurtbox of the superclass if not
   */
  public Rectangle[] getHurtBox(int idx){
    if (success == true){
      Rectangle [] temp = new Rectangle[1];
      temp[0] = new Rectangle(1,1,1,1);
      return temp;
    }
    return super.getHurtBox(idx);
  }
  
  /*
   * getXKnockBack
   * overwirte getter for xKnockBack
   * @param none
   * @return xKnockBack int value which comes from the xKnockBack variable if this attack is successful and the xKnockBack of the superclass if not
   */
  public int getXKnockBack(){
    if (success == true){
      return xKnockBack;
    }
    return super.getXKnockBack();
  }
  
  /*
   * getYKnockBack
   * overwirte getter for yKnockBack
   * @param none
   * @return yKnockBack int value which comes from the yKnockBack variable if this attack is successful and the yKnockBack of the superclass if not
   */
  public int getYKnockBack(){
    if (success == true){
      return yKnockBack;
    }
    return super.getYKnockBack();
  }
  
  /*
   * getDamage
   * overwirte getter for damage
   * @param none
   * @return grabDamage int value which comes from the grabDamage variable if this attack is successful and the damage of the superclass if not
   */
  public int getDamage(){
    if (success == true){
      return grabDamage;
    }
    return super.getDamage();
  }
  
  /*
   * getStunDuration
   * overwirte getter for stunDuration
   * @param none
   * @return stunDuration int value which comes from the stun2 variable if this attack is successful and the stunDuration of the superclass if not
   */
  public int getStunDuration(){
    if (success == true){
      return stun2;
    }
    return super.getStunDuration();
  }
  
  /*
   * setSuccess
   * setter for success (always sets to false)
   * @param none
   * @return none
   */ 
  public void setSuccess(){
    success = false;
  }
  
  /*
   * process
   * method that is ran after the attack is successfully hit which activates certain attributes of the attack
   * @param 2 player objects, the player using this attack and the player recieving the attack
   * @return none
   */ 
  public void process(Player player, Player otherplayer){
    
    //if player is at the frame which the direction is to be changed, change the player's direction
    if (player.getAnimationCounter()==directionChangeFrame){
      if (player.getDirection().equals("left")){
        player.setDirection("right");
      } else {
        player.setDirection("left");
      }
    }
    
    //set the enemy location based on the enemyX and enemyY variables
    if (player.getDirection().equals("left")){
      if (enemyX[player.getAnimationCounter()]!=0){
        otherplayer.setX(player.getX()-enemyX[player.getAnimationCounter()]);
      }
      
    } else {
      if (enemyX[player.getAnimationCounter()]!=0){
        otherplayer.setX(player.getX()+enemyX[player.getAnimationCounter()]);
      }
    }
    if (enemyY[player.getAnimationCounter()]!=0){
      otherplayer.setY(player.getY()+enemyY[player.getAnimationCounter()]);
    }
    
    //change the effectLocation based on the location of the hurt player
    //this is exclusive to this attack as usually location is calcualted through the attacker
    if (super.getEffect(player.getAnimationCounter()) != null){
      for(int i = 0; i<super.getEffect(player.getAnimationCounter()).length; i++){
        effectLocation[player.getAnimationCounter()][i][0] = Math.abs(otherplayer.getX()-player.getX())+super.getEffectX(player.getAnimationCounter(), i);
      }
    }
    
    //damage the other player at a set frame
    if (player.getAnimationCounter() == damageFrame){
      otherplayer.hurt(this, player.getDirection());
    }
  }
}
