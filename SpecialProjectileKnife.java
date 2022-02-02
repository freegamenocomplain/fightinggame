/* class SpecialProjectileKnife
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this is a special type of projectile launched by SpecialAttacks (it's current difference is that it has collision detection)
 * this class extends Projectile
*/

//import statements
import java.awt.Image;
import java.awt.Rectangle;

//class starts here
class SpecialProjectileKnife extends Projectile{
  
  //variable declaration
  Rectangle collisionBox;
  
  //constructor of the SpecialProjectileKnife class, this takes in all necessary variables and assigns them to this object
  SpecialProjectileKnife (Image [] projectileImage, int [] projectileAnim, int [] projectileEndAnim, int [] xVelocity, int [] yVelocity, int duration, int lingerFrames, Rectangle [][] hitbox, int damage, int stun, int blockStun, boolean launch, int xKnockback, int yKnockback, String identifier){
    super(projectileImage, projectileAnim, projectileEndAnim, xVelocity, yVelocity, duration, lingerFrames, hitbox, damage, stun, blockStun, launch, xKnockback, yKnockback, identifier);
    collisionBox = new Rectangle(195, 39, 150, 112);
  }
  //alternative copy constructor for the SpecialProjectileKnife class, used to actually create a projectile to be used. 
  //Takes in another Projectile (projectileCopy) and makes this a copy of that Projectile, also assigning int x, int y (the location of this projectile) and the direction of this projectile
  SpecialProjectileKnife (Projectile projectileCopy, int x, int y, String direction){
    super(projectileCopy, x, y, direction);
    collisionBox = new Rectangle(195, 39, 150, 112);
  }
  
  /*
   * process
   * make certain changes to this projectile 
   * @param none
   * @return boolean, false if this projectile has ended and needs to be removed
   */
  public boolean process(Player otherplayer){
    
    //create variable to track of the return value
    boolean projectileActive;
    
    //track the beginning location of thie projectile (similar to one found in GameFrame)
    String location;
    int startingX = super.getX();
    if (super.getX()<otherplayer.getX()){
      location = "left";
    } else {
      location = "right";
    }
    
    //call the superclass process
    projectileActive = super.process();
    
    //update the collisionBox used later in collision detection to match current location and direction
    Rectangle currentCollisionBox;
    if (super.getDirection().equals("left")){
      currentCollisionBox = new Rectangle((int)(super.getX()+super.getImage().getWidth(null)-collisionBox.getX()-collisionBox.getWidth()), (int)(super.getY()+collisionBox.getY()), (int)collisionBox.getWidth(), (int)collisionBox.getHeight());
    } else {
      currentCollisionBox = new Rectangle((int)(super.getX()+collisionBox.getX()), (int)(super.getY()+collisionBox.getY()), (int)collisionBox.getWidth(), (int)collisionBox.getHeight());
    }
    
    //run collision detection code similar to one found in GameFrame but with the currentCollisionBox as this projectile's hurtbox
    for (int j = 0; j<1; j++){
      if (otherplayer.getHurtBox()[j]!=null){
        if (currentCollisionBox.intersects(otherplayer.getHurtBox()[j])){
          if (location.equals("left")){
            int overlap = (int)((currentCollisionBox.getX()+currentCollisionBox.getWidth()) - otherplayer.getHurtBox()[j].getX());
            if (overlap>=1){
              super.addX(-(overlap/2));
            }
          }
          if (location.equals("right")){
            int overlap = (int)((otherplayer.getHurtBox()[j].getX()+otherplayer.getHurtBox()[j].getWidth()) - currentCollisionBox.getX());
            if (overlap>=1){
              super.addX(overlap/2);
            }
          }
        }
      }
    }
    
    //the projectile cannot move backwards and will revert to original location if it ends up being pushed back
    if (super.getDirection().equals("left")){
      if (super.getX()>startingX){
        super.addX(startingX-super.getX());
      }
    } else {
      if (super.getX()<startingX){
        super.addX(startingX-super.getX());
      }
    }
    
    //return boolean that the superclass process returned
    return projectileActive;
  }
}