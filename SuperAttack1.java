/* class SuperAttack1
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class is another type of attack with other additional attributes, it also is an IndicatorAttack (every super attack will have their own class)
 * this class extends Attack and IndicatorAttack
*/

//imports
import java.awt.Rectangle;

//class starts here
class SuperAttack1 extends Attack implements IndicatorAttack{
  
  //variable declaration
  private static int manaCost;
  private int [] xMove2;
  private int [] yMove2;
  private int [] enemyX;
  private int [] enemyY;
  private int [] atkAnim2 = {201, 202, 203, 212, 213, 214, 214, 215, 216, 217, 217, 218, 218, 219, 220, 221, 222, 223, 223, 224, 225, 226, 227, 228, 228, 229, 230, 231, 231, 232, 233, 233, 234, 235, 236, 237, 238, 239, 49, 49, 161, 162, 163, 158, 160, 49, 49, 161, 162, 163, 158, 160, 49, 49, 161, 162, 163, 158, 160, 49, 49, 161, 162, 163, 158, 160, 49, 49, 161, 162, 163, 158, 160, 49, 49, 161, 162, 163, 163, 163, 163, 164, 165, 166, 167, 167};
  private Rectangle [][] hurtbox2;
  private boolean [] damageFrame;
  private boolean success;
  private int damage2;
  private int baseXKnockBack;
  private int xKnockBack;
  private int yKnockBack;
  private int [] stun2;
  private int currentStun;
  private int directionChangeFrame;
  private Effect [][] effect;
  private int [][][] effectLocation;
  
  //constructor for this object, takes in certain values as well as setting other fixed values of this object
  SuperAttack1 (int [] atkAnim, Rectangle [][] Hitbox, Rectangle [][] Hurtbox, int [] xMove, Effect [][] effect){
    //call to super for the first part of this attack
    super(atkAnim, false, Hitbox, Hurtbox, 50, 14, 6, 4, 1, true, 6, 5, xMove, null, null, false, 8, false, null, null, null, null, null);
    
    //set effect created by this attack
    this.effect = effect;
    
    //set manacost
    manaCost = 100;
    
    //set the effect locations of this effect on certain frames
    effectLocation = new int [87][1][2];
    effectLocation [37][0][0] = -152;
    effectLocation [37][0][1] = 209;
    effectLocation [45][0][0] = 279;
    effectLocation [45][0][1] = 209;
    effectLocation [52][0][0] = 279;
    effectLocation [52][0][1] = 209;
    effectLocation [59][0][0] = 279;
    effectLocation [59][0][1] = 209;
    effectLocation [66][0][0] = 279;
    effectLocation [66][0][1] = 209;
    effectLocation [73][0][0] = 279;
    effectLocation [73][0][1] = 209;
    
    //set the movement of this attack on certain frames
    xMove2 = new int [87];
    xMove2[0] = 30;
    sameNum(xMove2, 0, 5);
    xMove2[9] = 30;
    sameNum(xMove2, 9, 11);
    xMove2[15] = 30;
    sameNum(xMove2, 15, 17);
    xMove2[21] = 30;
    sameNum(xMove2, 21, 23);
    xMove2[27] = 30;
    sameNum(xMove2, 27, 30);
    xMove2[37] = 60;
    xMove2[40] = 431;
    xMove2[41] = 10;
    xMove2[42] = 10;
    xMove2[44] = 60;
    xMove2[47] = 674;
    xMove2[48] = 10;
    xMove2[49] = 10;
    xMove2[51] = 60;
    xMove2[54] = 674;
    xMove2[55] = 10;
    xMove2[56] = 10;
    xMove2[58] = 60;
    xMove2[61] = 674;
    xMove2[62] = 10;
    xMove2[63] = 10;
    xMove2[65] = 60;
    xMove2[68] = 674;
    xMove2[69] = 10;
    xMove2[70] = 10;
    xMove2[72] = 60;
    xMove2[75] = 674;
    xMove2[76] = 10;
    xMove2[77] = 10;
    
    //set the hurtbox of this attack
    hurtbox2 = new Rectangle [87][1];
    hurtbox2[0][0] = new Rectangle (170, 245, 161, 151);
    sameBox(hurtbox2, 0, 37);
    
    //set the frames that this attack deals damage in
    damageFrame = new boolean [87];
    damageFrame [5] = true;
    damageFrame [11] = true;
    damageFrame [17] = true;
    damageFrame [23] = true;
    damageFrame [30] = true;
    damageFrame [41] = true;
    damageFrame [49] = true;
    damageFrame [56] = true;
    damageFrame [63] = true;
    damageFrame [70] = true;
    damageFrame [77] = true;
    
    //set the damage of the attack
    damage2 = 50;
    
    //set the stun inflicted by certain hits of this attack
    stun2 = new int [87];
    stun2 [5] = 6;
    stun2 [11] = 6;
    stun2 [17] = 6;
    stun2 [23] = 6;
    stun2 [30] = 8;
    stun2 [41] = 8;
    stun2 [49] = 8;
    stun2 [56] = 8;
    stun2 [63] = 8;
    stun2 [70] = 8;
    stun2 [77] = 12;
    currentStun = 6;
    
    //set the x and y knockbacks
    baseXKnockBack = 10;
    xKnockBack = baseXKnockBack;
    yKnockBack = 0;
  }
  
  /*
   * sameNum
   * method that sets a certain interval of an array to the same value
   * @param int array to be set, int startidx and endidx, the starting and ending index
   * @return none
   */ 
  private void sameNum(int [] arr, int startidx, int endidx){
    for(int i = startidx+1; i<=endidx; i++){
      arr[i] = arr[startidx];
    }
  }
  
  /*
   * sameBox
   * method that sets a certain interval of an array to the same value
   * @param 2D Rectangle array boxArray (the array that will be changed), int startidx and endidx, the starting and ending index
   * @return none
   */ 
  private void sameBox(Rectangle [][] boxArray, int startidx, int endidx){
    for(int i = startidx+1; i<=endidx; i++){
      for(int j = 0; j<boxArray[i].length;j++){
        boxArray[i][j] = boxArray[startidx][j];
      }
    }
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
   * getManaCost
   * getter for manaCost
   * @param none
   * @return int manaCost, the amount of MP this attack costs to cast
   */ 
  public static int getManaCost(){
    return manaCost;
  }
  
   //this attack overwrites a number of getters from the superclass to return values depending on if this attack is successful or not, similarly to grab
  
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
      return effect[i];
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
   * @return Rectangle array from the hitbox2 variable if this attack is successful and the hurtbox of the superclass if not
   */
  public Rectangle[] getHurtBox(int idx){
    if (success == true){
      return hurtbox2[idx];
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
      return damage2;
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
      return currentStun;
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
    
    //on certain hits of this attack the x and y knockback is changed
    //x and y knockback is set by default to the base values
    xKnockBack = baseXKnockBack;
    yKnockBack = 0;
    
    //if the attack takes place after frame 30, the knockback is scaled based on the distance the enemy player is from the center of the attack
    if ((player.getAnimationCounter()>30) && (damageFrame[player.getAnimationCounter()])) {
      
      //calculate the location difference by distance from the center of the attack
      int locationDiff;
      if (player.getDirection().equals("left")){
        locationDiff = otherplayer.getX()-player.getX();
      } else {
        locationDiff = player.getX() - otherplayer.getX();
      }
      locationDiff = locationDiff-386;
      
      //knockback caps at 100 and has to be greater than base knockback
      if (locationDiff>100){
        locationDiff = 100;
      } else if (locationDiff<baseXKnockBack){
        locationDiff = baseXKnockBack;
      }
      //set xKnockback to the location difference
      xKnockBack = locationDiff;
    }
    
    //at the last hit of the attack the x and y knockback are made bigger to launch the target
    if (player.getAnimationCounter() == 77){
      yKnockBack = 60;
      xKnockBack = 100;
    }
    
    //on certain frames of the attack the player direction is reversed
    if ((player.getAnimationCounter() == 43) || (player.getAnimationCounter() == 50) || (player.getAnimationCounter() == 57) || (player.getAnimationCounter() == 64) || (player.getAnimationCounter() == 71)){
      if (player.getDirection().equals("left")){
        player.setDirection("right");
      } else {
        player.setDirection("left");
      }
    }
    
    //set the stun of this attack based on the stun2 array of this object
    currentStun = stun2[player.getAnimationCounter()];
    
    //if the attack is on certain frames damage the target
    if (damageFrame[player.getAnimationCounter()]){
      otherplayer.hurt(this, player.getDirection());
    }
  }
}