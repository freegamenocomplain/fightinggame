/* class Attack
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class stores all necessary information for a basic attack to be used by the Player object
*/

//imports
import java.awt.Rectangle;

//class starts here
class Attack {
  
  //variable declaration
  private int [] attackAnim;
  private int length;
  private boolean aerial;
  private Rectangle [][] hitbox;
  private Rectangle [][] leftHitBox;
  private Rectangle [][] hurtbox;
  private int damage;
  private int xKnockback;
  private int yKnockback;
  private int stunDuration;
  private int selfStun;
  private int blockStun;
  private int shieldDamage;
  private boolean launch;
  private int hitInvul = 2;
  private boolean [] still;
  private int [] xMove;
  private int [] yMove;
  private boolean attackCancel;
  private boolean jumpCancel;
  private boolean [] phase;
  private Effect [][] effect;
  private int [][][] effectLocation;
  private Projectile [][] projectile;
  private int [][][] projectileLocation;
  private int comboPriority = 1;
  
  //constructor for the Attack object, takes all necessary variables and assigns them to this object
  Attack (int [] atkAnim, boolean air, Rectangle [][] hit, Rectangle [][] hurt, int damage, int stun, int selfStun, int blockStun, int hitInvul, boolean launch, int xKnockback, int yKnockback, int [] xMove, int [] yMove, boolean [] still, boolean attackCancel, int comboPriority, boolean jumpCancel, boolean [] phase, Effect [][] effect, int [][][] effectLocation, Projectile [][] projectile, int [][][] projectileLocation){
    this.attackAnim = atkAnim;
    this.length = attackAnim.length-1;
    this.hitbox = hit;
    this.hurtbox = hurt;
    //loop that calculates the left hitbox by reflecting the normal hitbox
    this.leftHitBox = new Rectangle[hit.length][hit[0].length];
    for (int i = 0; i<hit.length; i++){
      for (int j = 0; j<hit[i].length; j++){
        if (hit[i][j] != null) {
          this.leftHitBox[i][j] = new Rectangle((int)(550-hit[i][j].getX()-hit[i][j].getWidth()), (int)hit[i][j].getY(), (int)hit[i][j].getWidth(), (int)hit[i][j].getHeight());
        }
      }
    }
    this.damage = damage;
    //shield damage is by default half of the attack's damage, but some attacks implemented later may do extra damage to shield so this variable is left here
    this.shieldDamage = damage/2;
    this.aerial = air;
    this.stunDuration = stun;
    this.selfStun = selfStun;
    this.blockStun = blockStun;
    this.launch = launch;
    this.xKnockback = xKnockback;
    this.yKnockback = yKnockback;
    this.xMove = xMove;
    this.yMove = yMove;
    this.still = still;
    this.attackCancel = attackCancel;
    this.jumpCancel = jumpCancel;
    this.comboPriority = comboPriority; 
    this.phase = phase;
    this.effect = effect;
    this.effectLocation = effectLocation;
    this.projectile = projectile;
    this.projectileLocation = projectileLocation;
    this.hitInvul = hitInvul;
  }
  
  //alternative constructor which only takes the bare minimum variables, used for more basic attacks
  Attack (int [] atkAnim, boolean air, Rectangle [][] hit, Rectangle [][] hurt, int damage, int stun, int selfStun, int blockStun, int hitInvul, boolean launch, int xKnockback, int yKnockback, boolean attackCancel, int comboPriority, boolean jumpCancel){
    this.attackAnim = atkAnim;
    this.length = attackAnim.length-1;
    this.hitbox = hit;
    this.hurtbox = hurt;
    this.leftHitBox = new Rectangle[hit.length][hit[0].length];
    for (int i = 0; i<hit.length; i++){
      for (int j = 0; j<hit[i].length; j++){
        if (hit[i][j] != null) {
          this.leftHitBox[i][j] = new Rectangle((int)(550-hit[i][j].getX()-hit[i][j].getWidth()), (int)hit[i][j].getY(), (int)hit[i][j].getWidth(), (int)hit[i][j].getHeight());
        }
      }
    }
    this.damage = damage;
    this.shieldDamage = damage/2;
    this.aerial = air;
    this.stunDuration = stun;
    this.selfStun = selfStun;
    this.blockStun = blockStun;
    this.launch = launch;
    this.xKnockback = xKnockback;
    this.yKnockback = yKnockback;
    this.attackCancel = attackCancel;
    this.jumpCancel = jumpCancel;
    this.comboPriority = comboPriority; 
    this.hitInvul = hitInvul;
  }
  
  /*
   * getAttackAnim
   * getter for attackAnim
   * @param int i, the index of the attackAnim array needed
   * @return the ith value of the attackAnim array (used to animate the attack and make calls to certain images)
   */ 
  public int getAttackAnim(int i){
    return attackAnim[i];
  }
  
  /*
   * getLength
   * getter for Length
   * @param none
   * @return int length, the amount of frames this attack takes
   */ 
  public int getLength(){
    return length;
  }
  
  /*
   * getStunDuration
   * getter for stunDuration
   * @param none
   * @return int stunDuration, the amount of frames this attack stuns for on hit
   */ 
  public int getStunDuration(){
    return stunDuration;
  }
  
  /*
   * getBlockStun
   * getter for blockStun
   * @param none
   * @return int blockStun, the amount of frames this attack stuns for on block
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
   * getDamage
   * getter for damage
   * @param none
   * @return int damage, the amount of damage this attack deals
   */ 
  public int getDamage(){
    return damage;
  }
  
  /*
   * getShieldDamage
   * getter for shieldDamage;
   * @param none
   * @return int shieldDamage, the amount of damage this attack deals to shield
   */ 
  public int getShieldDamage(){
    return shieldDamage;
  }
  
  /*
   * isAerial
   * getter for aerial
   * @param none
   * @return boolean aerial, variable that determines if this attack is an aerial attack
   */ 
  public boolean isAerial(){
    return aerial;
  }
  
  /*
   * launching
   * getter for launch 
   * @param none
   * @return boolean launch, variable that determins if this attack launches the opposing player on hit
   */ 
  public boolean launching(){
    return launch;
  }
  
  /*
   * getXKnockBack
   * getter for xKnockBack
   * @param none
   * @return int xKnockback, the distance this attack knocks back on hit
   */ 
  public int getXKnockBack(){
    return xKnockback;
  }
  
  /*
   * getYKnockBack
   * getter for yKnockBack
   * @param none
   * @return int yKnockback, the vertical distance this attack knocks back on hit
   */ 
  public int getYKnockBack(){
    return yKnockback;
  }
  
  /*
   * getHitBox
   * getter for hitbox 
   * @param none
   * @return 2D Rectangle array hitbox, the hitbox of this attack
   */ 
  public Rectangle[][] getHitBox () {
    return hitbox;
  }
  /*
   * getLHitBox
   * getter for leftHitBox 
   * @param none
   * @return 2DRectangle array leftHitBox, the hitbox for this attack if facing left (reflected hitbox)
   */ 
  public Rectangle[][] getLHitBox(){
    return leftHitBox;
  }
  
  /*
   * getHurtBox
   * getter for hurtbox 
   * @param int idx, the index of the hurtbox array needed
   * @return Rectangle array hurtbox, the hurtbox for this attack on the ith frame
   */ 
  public Rectangle[] getHurtBox(int idx){
    return hurtbox[idx];
  }
  
  /*
   * getXMove
   * getter for xMove 
   * @param int i, the index of the xMove array needed
   * @return int xMove, the distance this attack moves the player on the ith frame
   */ 
  public int getXMove(int i){
    if (xMove != null){
      return xMove[i];
    } 
    return 0;
  }
  
  /*
   * getYMove
   * getter for yMove
   * @param int i, the index of the yMove array needed
   * @return int yMove, the vertical distance this attack moves the player on the ith frame
   */ 
  public int getYMove(int i){
    if (yMove != null){
      return yMove[i];
    }
    return 0;
  }
  
  /*
   * isStill
   * getter for still 
   * @param int i, the index of the still array needed
   * @return boolean still, which determines if the aerial attack is to be moved on the current trajectory or be stopped in place
   */ 
  public boolean isStill(int i){
    if (still == null){
      return false;
    }
    return still[i];
  }
  
  /*
   * getSelfStun
   * getter for selfStun
   * @param none
   * @return int selfStun, the amount of frames this attack stuns the user
   */ 
  public int getSelfStun(){
    return selfStun;
  }
  
  /*
   * canAttackCancel
   * getter for attackCancel
   * @param none
   * @return boolean attackCance, which determines if this attack can be canceled by another attack
   */ 
  public boolean canAttackCancel(){
    return attackCancel;
  }
  
  /*
   * getComboPriority
   * getter for comboPriority
   * @param none
   * @return int comboPriority, which determines which attacks this attack can combo into
   */ 
  public int getComboPriority(){
    return comboPriority;
  }
  
  /*
   * canJumpCancel
   * getter for jumpCancel
   * @param none
   * @return boolean jumpCancel, which determines if this attack can be canceled by a jump
   */ 
  public boolean canJumpCancel(){
    return jumpCancel;
  }
  
  /*
   * getPhase
   * getter for phase 
   * @param int i, the index of the phase array needed
   * @return boolean phase, which determines if this attack ignores collision detection at the ith frame
   */ 
  public boolean getPhase(int i){
    if (phase != null){
      return phase[i];
    }
    return false;
  }
  
  /*
   * getEffect
   * getter for effect 
   * @param int i, the index of the effect array needed
   * @return Effect effect, the effect this attack creates at the ith frame
   */ 
  public Effect[] getEffect(int i){
    if (effect!= null){
      return effect[i];
    }
    return null;
  }
  
  /*
   * getEffectX
   * getter for effectLocation 
   * @param int i and j, the index of the effectLocatin needed
   * @return int effectLocation, the location that the effect this attack creates spawn at (X coordinate)
   */ 
  public int getEffectX(int i, int j){
    if (effectLocation != null){
      return effectLocation[i][j][0];
    }
    return 0;
  }
  
  /*
   * getEffectY
   * getter for effectLocation 
   * @param int i and j, the index of the effectLocatin needed
   * @return int effectLocation, the location that the effect this attack creates spawn at (Y coordinate)
   */ 
  public int getEffectY(int i, int j){
    if (effectLocation != null){
      return effectLocation[i][j][1];
    }
    return 0;
  }
  
  /*
   * getProjectile
   * getter for projectile
   * @param int i, the index of the projectile array needed
   * @return Projectile projectile, the projectile this attack spawns at the ith frame
   */ 
  public Projectile[] getProjectile(int i){
    if (projectile!=null){
      return projectile[i];
    }
    return null;
  }
  
  /*
   * getProjectileX
   * getter for projectileLocation 
   * @param int i and j, the index of the projectileLocatin needed
   * @return int projectileLocation, the location that the projectile this attack creates spawn at (X coordinate)
   */ 
  public int getProjectileX(int i, int j){
    if (projectileLocation != null){
      return projectileLocation[i][j][0];
    }
    return 0;
  }
  
  /*
   * getProjectileY
   * getter for projectileLocation 
   * @param int i and j, the index of the projectileLocatin needed
   * @return int projectileLocation, the location that the projectile this attack creates spawn at (Y coordinate)
   */
  public int getProjectileY(int i, int j){
    if (projectileLocation != null){
      return projectileLocation[i][j][1];
    }
    return 0;
  }
}