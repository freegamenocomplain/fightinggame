/*class Effect
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class is a visual effect that can be displayed to the screen, created by certain effects.
*/

//imports
import java.awt.Image;

//class starts here
class Effect {
  
  //variable declaration
  private Image[] effectImages;
  private int animationCounter;
  private int [] effectAnim;
  private int x;
  private int y;
  private String direction;
  
  //constructor for the Effect class, which takes in the images necessary and the animation array of said images and assigns them to this object
  Effect (Image [] images, int [] effAnim){
    effectImages = images;
    effectAnim = effAnim;
    animationCounter = 0;
  }
  
  //alternative copy constructor for the Effect class, used to actually create an effect to be displayed. 
  //Takes in another effect (copyImage) and makes this a copy of that effect, also assigning int x, int y (the location of this effect) and the direction of this effect
  Effect (Effect copyImage, int x, int y, String direction) {
    this(copyImage.getEffectImages(), copyImage.getEffectAnim());
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.animationCounter = -1;
  }
  
  /*
   * getX
   * getter for x
   * @param none
   * @return int x, the horizontal location of this effect
   */ 
  public int getX(){
    return x;
  }
  
  /*
   * getY
   * getter for y 
   * @param none
   * @return int y, the vertical location of this effect
   */ 
  public int getY(){
    return y;
  }
  
  /*
   * getEffectImages
   * getter for effectImages
   * @param none
   * @return Image array effectImages, the images used in this effect
   */ 
  public Image [] getEffectImages(){
    return effectImages;
  }
  
  /*
   * getEffectAnim
   * getter for effectAnim
   * @param none
   * @return int array effectAnim, used to animate and make calls to the effectImages array
   */ 
  public int [] getEffectAnim(){
    return effectAnim;
  }
  
  /*
   * getDirection
   * getter for direction
   * @param none
   * @return String direction, the direction this object currently faces
   */ 
  public String getDirection(){
    return direction;
  }
  
  /*
   * getImage
   * getter for effectImages
   * @param none
   * @return an Image, which is the current image this effect is at
   */ 
  public Image getImage(){
    if (animationCounter == -1){
      animationCounter = 0;
    }
    //accessed through the animationCounter and effectAnim array
    return effectImages[effectAnim[animationCounter]];
  }
  
  /*
   * processEffect
   * Animates this effect
   * @param none
   * @return boolean value that determins if this effect has been finished yet
   */ 
  public boolean processEffect(){
    //if animationCounter is less than this image's length increase it by 1
    if (animationCounter>=effectAnim.length-1){
      return false;
    } else {
      animationCounter++;
      return true;
    }
  }
}