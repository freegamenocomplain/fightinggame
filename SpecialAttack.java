/*class SpecialAttack
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class is an attack but with some additional attributes
 * this class extends attack
*/

//import statements
import java.awt.Rectangle;

//class starts here
class SpecialAttack extends Attack {
  
  //variable declaration
  private String projectileIdentifier;
  private int activationTime;
  
  //constructor for SpecialAttack, which takes in all necessary variables and assigns them to this object
  SpecialAttack (int [] atkAnim, boolean air, Rectangle [][] hit, Rectangle [][] hurt, int damage, int stun, int selfStun, int blockStun, int hitInvul, boolean launch, int xKnockback, int yKnockback, int [] xMove, int [] yMove, boolean [] still, boolean attackCancel, int comboPriority, boolean jumpCancel, boolean [] phase, Effect [][] effect, int [][][] effectLocation, Projectile [][] projectile, int [][][] projectileLocation, String projectileIdentifier, int activationTime){
    //call to super
    super(atkAnim, air, hit, hurt, damage, stun, selfStun, blockStun, hitInvul, launch, xKnockback, yKnockback, xMove, yMove, still, attackCancel, comboPriority, jumpCancel, phase, effect, effectLocation, projectile, projectileLocation);
    //assigning this object's variables
    this.projectileIdentifier = projectileIdentifier;
    this.activationTime = activationTime;
  }
  
  /*
   * activate
   * a method that is ran upon re-pressing the button for this special attack
   * @param Player player, the attacker
   * @return none
   */
  public void activate(Player player){
    
    //check if the player has enough MP to activate this effect
    if ((player.getMP()>=50) && (player.getAnimationCounter()>=activationTime)){
      player.addMP(-50);
      
      //search the player's projectiles array for the matching projectile created by this attack
      for(int i = 0; i<player.getProjectiles().size(); i++){
        if (player.getProjectiles().get(i).getIdentifier().equals(projectileIdentifier)){
          
          //teleport the player to that projectile's location
          if (player.getProjectiles().get(i).getDirection().equals("left")){
            player.setX(player.getProjectiles().get(i).getX()-90);
          } else {
            player.setX(player.getProjectiles().get(i).getX()+90);
          }
          player.setY(player.getProjectiles().get(i).getY()-200);
          
          //set the player's attack to none, stun to none, and animationCounter back to the default
          player.setAttack();
          player.setStun(0);
          player.setSoftStun(0);
          if (super.isAerial()){
            player.setAnimationCounter(6);
          } else {
            player.setAnimationCounter(0);
          }
          
          //remove the projectile
          player.getProjectiles().remove(i);
          
          //end the loop
          i = player.getProjectiles().size();
        }
      }
    }
  }
}