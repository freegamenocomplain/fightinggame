/* interfaceIndicatorAttack
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this a special type of an attack which has 2 parts: the first part of the attack which runs like a normal attack
 * and the 2nd part of the attack, which is what actually deals the damage, only activated if the first part hits.
*/

//interface starts here
interface IndicatorAttack {
  //the successful variable is used to track if the attack hit
  
  /*
   * successful
   * getter for success 
   * @param none
   * @return success, if the attack has successfully hit the target
   */ 
  public boolean successful();
  
  /*
   * setSuccess
   * setter for success (always sets to false)
   * @param none
   * @return none
   */ 
  public void setSuccess();
  
  /*
   * hit
   * method that is ran when the attack hits
   * @param Player object, the player using this attack
   * @return none
   */ 
  public void hit(Player player);
  
  /*
   * process
   * method that is ran after the attack is successfully hit which activates certain attributes of the attack
   * @param 2 player objects, the player using this attack and the player recieving the attack
   * @return none
   */ 
  public void process(Player player, Player otherplayer);
}