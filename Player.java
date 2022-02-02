/*class Player
 * version beta-1.00
 * Eric Wang
 * 01-20-19
 * This is a 2D fighting game where 2 player controlled characters use various attacks to bring the other player's HP to 0. 
 * The aim of this program is to implement all major features in a traditional fighting game, including normal attacks,
 * special attacks, super attacks, blocking, combos and grabs. The character has a large moveset with different attacks 
 * in air, dashing, standing and crouching.
 * this class stores an object representing a playable character, as well as all methods required to animate that character
*/

//imports
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Image; 
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Arrays;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;

//class starts here
class Player{
  
  //file variables that store the hit and block sound
  private File hitSound;
  private File blockSound;
  
  //variables that store the player's statistics
  private int runSpeed = 50;
  private int x;
  private int y;
  private int xVelocity = 0;
  private int yVelocity = 0;
  private int maxHP = 2500;
  private int hp;
  private double maxMP = 300;
  private double mp;
  private double maxBP = 900;
  private double bp;
  
  //variable that is used to call images to animate the character
  private int animationCounter;
  
  //attack variables
  /* ATTACK NAMING SCHEME
   * 789
   * 456 
   * 123
   * the above represents the direction of the attack. for example 6 represents forwards and 2 represents down
   * the attack is named with first it's button (A for light, C for heavy, D for special) and then it's direction
   * for example A6 is a forward light attack
   */
  private Attack A5;
  private Attack AA5;
  private Attack AAA5;
  private Attack AAAA5;
  private Attack jA5;
  private Attack A2;
  private Attack A6;
  private Attack C5;
  private Attack C2;
  private Attack C6;
  private Attack jC5;
  private Attack jD5;
  private Attack D5;
  private Grab grab1;
  private SuperAttack1 super1;
  
  //variables to store hurtboxes for different states
  private Rectangle [] idleHurtBox;
  private Rectangle [] movingHurtBox;
  private Rectangle [] aerialHurtBox;
  private Rectangle [] crouchingHurtBox;
  
  //variable to store current hurtbox and hitbox, as well as the projectiles of this player
  private Rectangle [] hitbox;
  private Rectangle [] hurtbox;
  private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
  
  //variables to do with the current status of the player
  private String status;
  private String direction;
  private Attack currentAttack;
  private int stunDuration;
  private int softStunDuration;
  private int invulDuration;
  private int barrierCD;
  private boolean inAir;
  private String aerialDrift;
  private int jumpsLeft;
  private boolean blockPressed;
  private boolean blocking;
  private boolean crouching;
  private String lastStatus;
  private boolean attackSuccessful;
  private boolean phase;
  private boolean movingLeft = false;
  private boolean movingRight = false;
  
  //variables to store the sprites of this character
  private BufferedImage [] sprites = new BufferedImage[241];
  private BufferedImage [] startSprites = new BufferedImage[35];
  private BufferedImage [] loseSprites = new BufferedImage[8];
  
  //certain animation arrays for different states, animation arrays store indexes of the image arrays to be called with animationCounter
  private int [] startingAnim = {0, 1, 2, 3, 4, 5, 6, 6, 7, 8, 9, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34};
  private int [] loseAnim = {0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7};
  private int [] idleAnim = {0};
  private int [] movingAnim  = {1, 2, 2, 3, 3, 4, 5, 6, 7, 8, 9};
  private int [] jumpAnim = {19,19,19,19,20,21,22,22,22,22,23,23,24,24};
  private int [] hurtAnim = {35, 36, 36, 37, 38};
  private int [] blockAnim = {39, 62};
  private int [] crouchAnim = {60, 61};
  
  //Constructor of the player class, takes in the spawn location, direction and color of the player and creates the player accordingly
  Player(int spawnX, int spawnY, String direction, int colour){
    //certain image arrays to be passed into attacks later
    Image [] effect1Sprites = new Image [9];
    Image [] effect2Sprites = new Image [5];
    Image [] projectile1Sprites = new Image[5];
    Image [] specialProjectile1Sprites = new Image [16];
    Image [] specialProjectile2Sprites = new Image [17];
    
    //load up the image arrays
    for (int i = 1; i<241; i++){
      if (i<10){
        try {
          sprites [i-1] = ImageIO.read(new File("knifeSprite"+colour+"000"+i+".png"));
        } catch (Exception e){}
      } else if (i<100){
        try {
          sprites [i-1] = ImageIO.read(new File("knifeSprite"+colour+"00"+i+".png"));
        } catch (Exception e){}
      } else {
        try {
          sprites [i-1] = ImageIO.read(new File("knifeSprite"+colour+"0"+i+".png"));
        } catch (Exception e){}
      }
    }
    for (int i = 1; i<9; i++){
      try {
        effect1Sprites [i-1] = ImageIO.read(new File("effect1000"+i+".png"));
      } catch (Exception e){}
    }
    for (int i = 1; i<6; i++){
      try {
        effect2Sprites [i-1] = ImageIO.read(new File("effect2000"+i+".png"));
      } catch (Exception e){}
    }
    for (int i = 1; i<6; i++){
      try {
        projectile1Sprites [i-1] = ImageIO.read(new File("projectile1000"+i+".png"));
      } catch (Exception e){}
    }
    for (int i = 1; i<16; i++){
      if (i<10){
        try {
          specialProjectile1Sprites [i-1] = ImageIO.read(new File("specialProjectile1000"+i+".png"));
        } catch (Exception e){}
      } else {
        try {
          specialProjectile1Sprites [i-1] = ImageIO.read(new File("specialProjectile100"+i+".png"));
        } catch (Exception e){}
      }
    }
    for (int i = 1; i<17; i++){
      if (i<10){
        try {
          specialProjectile2Sprites [i-1] = ImageIO.read(new File("specialProjectile2000"+i+".png"));
        } catch (Exception e){}
      } else {
        try {
          specialProjectile2Sprites [i-1] = ImageIO.read(new File("specialProjectile200"+i+".png"));
        } catch (Exception e){}
      }
    }
    for (int i = 1; i<36; i++){
      if (i<10){
        try {
          startSprites [i-1] = ImageIO.read(new File("knifeStart"+colour+"000"+i+".png"));
        } catch (Exception e){}
      } else {
        try {
          startSprites [i-1] = ImageIO.read(new File("knifeStart"+colour+"00"+i+".png"));
        } catch (Exception e){}
      }
    }
    for (int i = 1; i<9; i++){
      try {
        loseSprites [i-1] = ImageIO.read(new File("knifeLose"+colour+"000"+i+".png"));
      } catch (Exception e){}
    } 
  
    //load up the hit and block sounds
    hitSound = new File("sfx1.wav");
    blockSound = new File("sfx5.wav");
    try{
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(hitSound);
      DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
      Clip clip = (Clip) AudioSystem.getLine(info);
      AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(blockSound);
      DataLine.Info info2 = new DataLine.Info(Clip.class, audioStream.getFormat());
      Clip clip2 = (Clip) AudioSystem.getLine(info);
    }catch (Exception e){}
    
    //set player coordinates according to spawn coordinates
    x = spawnX;
    y = spawnY;
    //set other starting values for variables
    hp = maxHP;
    mp = 0;
    bp = maxBP;
    barrierCD = 0;
    animationCounter = 0;
    status = "idle";
    this.direction = direction;
    invulDuration = 0;
    jumpsLeft = 2;
    
    //set hurtboxes
    idleHurtBox = new Rectangle[1];
    idleHurtBox[0] = new Rectangle (228, 192, 57, 206);
    movingHurtBox = new Rectangle[1];
    movingHurtBox[0] = new Rectangle (67, 271, 223, 126);
    aerialHurtBox = new Rectangle[1];
    aerialHurtBox[0] = new Rectangle (224, 191, 85, 138);
    crouchingHurtBox = new Rectangle[1];
    crouchingHurtBox[0] = new Rectangle (205, 274, 79, 123);
    
    //attack declaration
    //attack hitboxes and hurtboxes are declared here and passed into the attacks
    //how attack declaration work:
    //the various arrays are declared here and passed into the attack through the constuctor
    //the statistics of the attack are also declared in the constructor
    int [] A5Anim = {10,11,12,13,13,14,15,16,17,18};
    Rectangle [][] A5HitBox = new Rectangle [10][2];
    A5HitBox [3][0] = new Rectangle (321, 160, 79, 79);
    A5HitBox [3][1] = new Rectangle (275, 238, 86, 58);
    sameBox(A5HitBox, 3, 4);
    Rectangle [][] A5HurtBox = new Rectangle [10][2];
    A5HurtBox [0][0] = new Rectangle (223, 193, 59, 206);
    sameBox(A5HurtBox, 0, 9);
    A5HurtBox [3][1] = new Rectangle (283, 177, 67, 48);
    sameBox(A5HurtBox, 3, 6);
    A5HurtBox [7][1] = new Rectangle (283, 204, 59, 55);
    sameBox(A5HurtBox, 6, 7);
    A5 = new Attack(A5Anim, false, A5HitBox, A5HurtBox, 80, 5, 5, 4, 2, false, 4, 10, true, 1, false);
    
    int [] AA5Anim = {76, 77, 78, 78, 79, 80, 81, 82, 83, 84};
    Rectangle [][] AA5HitBox = new Rectangle [10][1];
    AA5HitBox [2][0] = new Rectangle (183, 184, 204, 100);
    sameBox(AA5HitBox, 2, 3);
    Rectangle [][] AA5HurtBox = new Rectangle [10][2];
    AA5HurtBox [0][0] = new Rectangle (223, 193, 59, 206);
    sameBox(AA5HurtBox, 0, 9);
    AA5HurtBox [0][1] = new Rectangle (283, 181, 63, 40);
    sameBox(AA5HurtBox, 0, 1);
    AA5HurtBox [2][1] = new Rectangle (193, 238, 31, 13);
    sameBox(AA5HurtBox, 2, 5);
    AA5 = new Attack (AA5Anim, false, AA5HitBox, AA5HurtBox, 80, 10, 4, 4, 2, false, 20, 10, true, 1, false);
    
    int [] AAA5Anim = {85, 86, 87, 87, 88, 89, 89, 90, 91, 91, 92, 93, 93, 94, 95, 96, 97, 98, 99, 100, 101};
    Rectangle [][] AAA5HitBox = new Rectangle [21][1];
    AAA5HitBox [8][0] = new Rectangle(215, 259, 251, 114);
    sameBox (AAA5HitBox, 8, 9);
    Rectangle [][] AAA5HurtBox = new Rectangle [21][1];
    AAA5HurtBox [0][0] = new Rectangle (223, 193, 59, 206);
    sameBox (AAA5HurtBox, 0, 3);
    AAA5HurtBox [4][0] = new Rectangle (236, 193, 76, 205);
    AAA5HurtBox [5][0] = new Rectangle (253, 215, 106, 183);
    sameBox (AAA5HurtBox, 5, 6);
    AAA5HurtBox [7][0] = new Rectangle (254, 247, 161, 151);
    AAA5HurtBox [8][0] = new Rectangle (135, 279, 189, 119);
    sameBox (AAA5HurtBox, 8, 14);
    AAA5HurtBox [15][0] = new Rectangle (228, 207, 86, 191);
    sameBox (AAA5HurtBox, 15, 16);
    AAA5HurtBox [17][0] = new Rectangle (228, 192, 57, 206);
    sameBox (AAA5HurtBox, 17, 20);
    int [] AAA5XMove = new int[21];
    AAA5XMove [8] = 120;
    AAA5 = new Attack (AAA5Anim, false, AAA5HitBox, AAA5HurtBox, 100, 12, 10, 8, 2, false, 8, 10, AAA5XMove, null, null, true, 2, false, null, null, null, null, null);
    
    int [] AAAA5Anim = {102, 103, 104, 105, 106, 107, 108, 108, 109, 110, 111, 111, 112, 113, 114, 115, 115, 116};
    Rectangle [][] AAAA5HitBox = new Rectangle [18][1];
    AAAA5HitBox [6][0] = new Rectangle (245, 128, 109, 255);
    sameBox (AAAA5HitBox, 6, 7);
    Rectangle [][] AAAA5HurtBox = new Rectangle [18][1];
    AAAA5HurtBox [0][0] = new Rectangle (164, 258, 160, 140);
    sameBox (AAAA5HurtBox, 0, 2);
    AAAA5HurtBox [3][0] = new Rectangle (231, 202, 76, 198);
    sameBox (AAAA5HurtBox, 3, 5);
    AAAA5HurtBox [6][0] = new Rectangle (205, 140, 67, 256);
    sameBox (AAAA5HurtBox, 6, 11);
    AAAA5HurtBox [12][0] = new Rectangle (221, 192, 64, 206);
    sameBox (AAAA5HurtBox, 12, 17);
    int [] AAAA5XMove = new int[18];
    AAAA5XMove [6] = 51;
    AAAA5 = new Attack (AAAA5Anim, false, AAAA5HitBox, AAAA5HurtBox, 120, 12, 8, 9, 2, true, 30, 70, AAAA5XMove, null, null, true, 4, true, null, null, null, null, null);
    
    int [] JA5Anim = {25,26,26,27,28,28,29,30,31,32,33,34};
    Rectangle [][] jA5HitBox = new Rectangle [12][2];
    jA5HitBox [4][0] = new Rectangle (258, 140, 150, 126);
    jA5HitBox [4][1] = new Rectangle (188, 266, 253, 109);
    sameBox(jA5HitBox, 4, 5);
    Rectangle [][] jA5HurtBox = new Rectangle [12][2];
    jA5HurtBox [0][0] = new Rectangle (235, 193, 87, 138);
    sameBox(jA5HurtBox, 0, 11);
    jA5HurtBox [4][0] = new Rectangle (200, 165, 121, 165);
    jA5HurtBox [4][1] = new Rectangle (152, 330, 60, 37);
    sameBox(jA5HurtBox, 4, 7);
    jA5 = new Attack(JA5Anim, true, jA5HitBox, jA5HurtBox, 150, 12, 5, 5, 2, false, 40, 60, true, 1, true);
    
    int [] A2Anim = {63, 65, 65, 65, 66, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75};
    Rectangle [][] A2HitBox = new Rectangle [15][1];
    A2HitBox[4][0] = new Rectangle (301, 293, 248, 100);
    sameBox(A2HitBox, 4, 5);
    Rectangle [][] A2HurtBox = new Rectangle [15][1];
    A2HurtBox[0][0] = new Rectangle (211, 275, 141, 121);
    sameBox(A2HurtBox, 0, 3);
    A2HurtBox[4][0] = new Rectangle (213, 303, 212, 95);
    sameBox(A2HurtBox, 4, 9);
    A2HurtBox[10][0] = new Rectangle (213, 283, 170, 115);
    sameBox(A2HurtBox, 10, 12);
    A2HurtBox[13][0] = new Rectangle (205, 258, 107, 139);
    sameBox(A2HurtBox, 13, 14);
    A2 = new Attack(A2Anim, false, A2HitBox, A2HurtBox, 150, 9, 6, 7, 2, false, 10, 15, true, 1, false);
    
    int [] A6Anim = {117, 118, 119, 120, 120, 120, 120, 121, 121, 121, 122, 123, 124, 125, 126, 127, 127, 128, 128, 129, 129, 130, 130};
    Rectangle [][] A6HitBox = new Rectangle [23][1];
    A6HitBox [7][0] = new Rectangle (236, 274, 231, 112);
    sameBox(A6HitBox, 7, 9);
    Rectangle [][] A6HurtBox = new Rectangle [23][1];
    A6HurtBox [0][0] = new Rectangle (150, 289, 207, 111);
    sameBox(A6HurtBox, 0, 13);
    A6HurtBox [14][0] = new Rectangle (214, 214, 110, 186);
    sameBox(A6HurtBox, 14, 17);
    A6HurtBox [18][0] = new Rectangle (228, 192, 57, 206);
    sameBox(A6HurtBox, 18, 22);
    int [] A6XMove = new int [23];
    A6XMove [0] = (int)(runSpeed/1.5);
    sameNum(A6XMove, 0, 6);
    A6XMove [7] = runSpeed/2;
    A6XMove [8] = runSpeed/4;
    A6XMove [13] = runSpeed/5;
    sameNum(A6XMove, 13, 22);
    A6 = new Attack (A6Anim, false, A6HitBox, A6HurtBox, 150, 10, 10, 8, 3, false, 10, 15, A6XMove, null, null, true, 2, false, null, null, null, null, null);
    
    int [] C5Anim = {131, 132, 132, 132, 133, 134, 134, 134, 135, 136, 137, 138, 139, 140, 140, 141};
    Rectangle [][] C5HitBox = new Rectangle [16][2];
    C5HitBox [5][0] = new Rectangle (228, 156, 159, 240);
    C5HitBox [5][1] = new Rectangle (387, 220, 64, 154);
    sameBox(C5HitBox, 5, 7);
    Rectangle [][] C5HurtBox = new Rectangle [16][1];
    C5HurtBox [0][0] = new Rectangle (255, 185, 102, 214);
    sameBox(C5HurtBox, 0, 3);
    C5HurtBox [4][0] = new Rectangle (158, 212, 164, 177);
    sameBox(C5HurtBox, 4, 10);
    C5HurtBox [11][0] = new Rectangle (225, 195, 68, 204);
    sameBox(C5HurtBox, 11, 15);
    int [] C5XMove = new int [16];
    C5XMove [4] = 97;
    C5 = new Attack (C5Anim, false, C5HitBox, C5HurtBox, 200, 12, 9, 10, 3, false, 10, 15, C5XMove, null, null, true, 2, false, null, null, null, null, null);
      
    int [] C2Anim = {142, 143, 143, 143, 144, 145, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156};
    Rectangle [][] C2HitBox = new Rectangle [18][1];
    C2HitBox [5][0] = new Rectangle (178, 86, 196, 241);
    sameBox(C2HitBox, 5, 6);
    Rectangle [][] C2HurtBox = new Rectangle [18][1];
    C2HurtBox [0][0] = new Rectangle (217, 253, 74, 145);
    sameBox(C2HurtBox, 0, 17);
    C2 = new Attack (C2Anim, false, C2HitBox, C2HurtBox, 150, 14, 8, 10, 2, true, 30, 60, true, 3, true);
    
    int [] C6Anim = {157, 157, 158, 158, 159, 160, 49, 49, 49, 161, 162, 163, 163, 163, 163, 164, 165, 166, 167, 167};
    Rectangle [][] C6HitBox = new Rectangle [20][1];
    C6HitBox [6][0] = new Rectangle (178, 237, 751, 143);
    sameBox (C6HitBox, 6, 8);
    C6HitBox [9][0] = new Rectangle (140, 237, 182, 144);
    sameBox (C6HitBox, 9, 10);
    Rectangle [][] C6HurtBox = new Rectangle [22][1];
    C6HurtBox [0][0] = new Rectangle (208, 202, 100, 196);
    sameBox (C6HurtBox, 0, 3);
    C6HurtBox [4][0] = new Rectangle (225, 262, 200, 136);
    sameBox (C6HurtBox, 4, 5);
    C6HurtBox [9][0] = new Rectangle (158, 226, 147, 172);
    sameBox (C6HurtBox, 9, 17);
    C6HurtBox [18][0] = new Rectangle (201, 193, 75, 206);
    sameBox (C6HurtBox, 18, 19);
    int [] C6XMove = new int [20];
    C6XMove [5] = 60;
    C6XMove [9] = 674;
    C6XMove [10] = 10;
    C6XMove [11] = 10;
    //declaration of effects also happen here before being passed into the attacks
    int [] effect2Anim = {0, 1, 2, 3, 4};
    Effect [][] effect2 = new Effect[20][1];
    effect2 [6][0] = new Effect(effect2Sprites, effect2Anim);
    int [][][] effect2Location = new int [20][1][2];
    effect2Location [6][0][0] = 278;
    effect2Location [6][0][1] = 209;
    boolean [] C6PhaseFrames = new boolean [20];
    C6PhaseFrames [4] = true; C6PhaseFrames [5] = true; C6PhaseFrames [6] = true; C6PhaseFrames [7] = true; C6PhaseFrames [8] = true; C6PhaseFrames [9] = true; C6PhaseFrames [10] = true; C6PhaseFrames [11] = true;
    C6 = new Attack (C6Anim, false, C6HitBox, C6HurtBox, 200, 15, 11, 4, 4, true, 100, 60, C6XMove, null, null, true, 4, false, C6PhaseFrames, effect2, effect2Location, null, null);
    
    int [] jC5Anim = {169, 170, 171, 172, 173, 174, 175, 176, 176, 176, 177, 177, 178, 178, 179, 179, 180};
    Rectangle [][] jC5HurtBox = new Rectangle [17][1];
    jC5HurtBox[0][0] = new Rectangle (210, 170, 78, 221);
    sameBox (jC5HurtBox, 0, 4);
    jC5HurtBox[5][0] = new Rectangle (198, 150, 116, 239);
    sameBox (jC5HurtBox, 5, 13);
    jC5HurtBox[14][0] = new Rectangle (214, 196, 97, 162);
    sameBox (jC5HurtBox, 14, 16);
    int [] jC5XMove = new int [17];
    jC5XMove[5] = -20;
    int [] jC5YMove = new int [17];
    jC5YMove[5] = -50;
    //declaration of projectiles similarly happens here before being passed into the attacks
    int [] projectile1Anim = {0, 1};
    int [] projectile1EndAnim = {0, 2, 3, 4};
    Rectangle [][] projectile1HitBox = new Rectangle [2][1];
    projectile1HitBox [0][0] = new Rectangle(0, 0, 100, 100);
    projectile1HitBox [1][0] = projectile1HitBox[0][0];
    int [] projectile1XVelocity = {100, 100};
    int [] projectile1YVelocity = {80, 80};
    Projectile projectile1 = new Projectile (projectile1Sprites, projectile1Anim, projectile1EndAnim, projectile1XVelocity, projectile1YVelocity, -1, 10, projectile1HitBox, 50, 8, 3, false, 4, 10, "basic");
    Projectile [][] jC5Projectile = new Projectile [17][3];
    jC5Projectile [5][0] = projectile1;
    jC5Projectile [5][1] = projectile1;
    jC5Projectile [5][2] = projectile1;
    int [][][] jC5ProjectileLocation = new int [18][3][2];
    jC5ProjectileLocation [5][0][0] = 202;
    jC5ProjectileLocation [5][0][1] = 150;
    jC5ProjectileLocation [5][1][0] = 192;
    jC5ProjectileLocation [5][1][1] = 200;
    jC5ProjectileLocation [5][2][0] = 182;
    jC5ProjectileLocation [5][2][1] = 250;
    jC5  = new Attack (jC5Anim, true, new Rectangle[17][1], jC5HurtBox, 0, 0, 10, 0, 0, false, 0, 0, jC5XMove, jC5YMove, null, true, 4, false, null, null, null, jC5Projectile, jC5ProjectileLocation);
    
    int [] D5Anim = {181, 182, 183, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 185, 186, 186, 187, 187, 188, 188, 189, 189, 190};
    Rectangle [][] D5HurtBox = new Rectangle [28][2];
    D5HurtBox [0][0] = new Rectangle(252, 193, 45, 205);
    sameBox(D5HurtBox, 0, 27);
    D5HurtBox [3][1] = new Rectangle(297, 215, 67, 17);
    sameBox(D5HurtBox, 3, 20);
    int []  specialProjectile1Anim = {1, 2, 3, 4, 5, 5, 5, 5, 5, 7, 7, 7, 8, 9, 10, 11, 12, 13, 14};
    Rectangle [][]  specialProjectile1HitBox = new Rectangle [19][1];
    specialProjectile1HitBox[4][0] = new Rectangle (100, 0, 380, 178);
    sameBox(specialProjectile1HitBox, 4, 7);
    int [] specialProjectile1XVelocity = new int [19];
    specialProjectile1XVelocity [0] = 40;
    sameNum (specialProjectile1XVelocity, 0, 5);
    int [] specialProjectile1YVelocity = new int [19];
    Projectile specialProjectile1 = new SpecialProjectileKnife (specialProjectile1Sprites, specialProjectile1Anim, new int [19], specialProjectile1XVelocity, specialProjectile1YVelocity, 19, 0, specialProjectile1HitBox, 150, 17, 8, true, 30, 40, "special5D");
    Projectile [][] D5Projectile = new Projectile [28][1];
    D5Projectile [2][0] = specialProjectile1;
    int [][][] D5ProjectileLocation = new int [28][1][2];
    D5ProjectileLocation [2][0][0] = 0;
    D5ProjectileLocation [2][0][1] = 230;
    D5 = new SpecialAttack (D5Anim, false, new Rectangle [28][2], D5HurtBox, 0, 0, 13, 0, 0, false, 0, 0, null, null, null, true, 5, false, null, null, null, D5Projectile, D5ProjectileLocation, "special5D", 6);
    
    int [] jD5Anim = {191, 192, 193, 194, 194, 194, 194, 194, 194, 194, 194, 194, 194, 194, 195, 195, 196, 196, 197, 197, 198, 198};
    Rectangle [][] jD5HurtBox = new Rectangle [22][1];
    jD5HurtBox [0][0] = new Rectangle (183, 206, 157, 160);
    sameBox(jD5HurtBox, 0, 21);
    boolean [] jD5still = new boolean[22];
    Arrays.fill(jD5still, true);
    int [] specialProjectile2Anim = {1, 2, 3, 3, 4, 5, 6, 6, 6, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    Rectangle [][] specialProjectile2HitBox = new Rectangle [20][1];
    specialProjectile2HitBox [6][0] = new Rectangle (94, 57, 317, 123);
    sameBox(specialProjectile2HitBox, 6, 10);
    int [] specialProjectile2XVelocity = new int [20];
    specialProjectile2XVelocity[0] = 40;
    sameNum (specialProjectile2XVelocity, 0, 5);
    int [] specialProjectile2YVelocity = new int [20];
    Projectile specialProjectile2 = new SpecialProjectileKnife (specialProjectile2Sprites, specialProjectile2Anim, new int [20], specialProjectile2XVelocity, specialProjectile2YVelocity, 20, 0, specialProjectile2HitBox, 150, 17, 8, false, 30, 40, "specialj5D");
    Projectile [][] jD5Projectile = new Projectile [22][1];
    jD5Projectile [2][0] = specialProjectile2;
    int [][][] jD5ProjectileLocation = new int [22][1][2];
    jD5ProjectileLocation [2][0][0] = 0;
    jD5ProjectileLocation [2][0][1] = 170;
    jD5 = new SpecialAttack (jD5Anim, true, new Rectangle [22][2], jD5HurtBox, 0, 0, 13, 0, 0, false, 0, 0, new int [22], new int [22], jD5still, true, 5, false, null, null, null, jD5Projectile, jD5ProjectileLocation, "specialj5D", 8);
    
    int [] grab1Anim = {40, 40, 41, 41, 42, 42, 42, 42, 42, 42, 43, 44, 44, 45, 45, 46, 47};
    int [] grab1Anim2 = {42, 42, 42, 42, 42, 42, 48, 49, 49, 49, 49, 49, 50, 50, 51, 51, 52, 53, 54, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 58, 59};
    Rectangle [][] grab1HitBox = new Rectangle [17][1];
    grab1HitBox [4][0] = new Rectangle (298, 216, 32, 60);
    grab1HitBox [5][0] = grab1HitBox[3][0];
    Rectangle [][] grab1HurtBox = new Rectangle [17][2];
    grab1HurtBox[0][0] = new Rectangle (224, 192, 74, 206);
    sameBox(grab1HurtBox, 0, 16);
    grab1HurtBox [4][1] = new Rectangle (298, 216, 32, 33);
    sameBox(grab1HurtBox, 4, 10);
    int [] grab1XMove2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 407, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int [] enemyX = new int[41];
    enemyX[0] = 70;
    sameNum(enemyX, 0, 10);
    enemyX[12] = 337;
    sameNum(enemyX, 11, 34);
    int [] enemyY = new int [41];
    enemyY[0] = -22;
    sameNum(enemyY, 0, 34);
    int [] effect1Anim = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 3, 4, 4, 5, 6, 7};
    Effect [][] effect1 = new Effect[42][1];
    effect1 [12][0] = new Effect(effect1Sprites, effect1Anim);
    int [][][] effectLocation = new int [42][1][2];
    effectLocation [12][0][1] = -20;
    grab1 = new Grab(grab1Anim, grab1Anim2, false, grab1HitBox, grab1HurtBox, 300, 52, 17, 300, 80, null, null, grab1XMove2, null, enemyX, enemyY, 35, 12, effect1, effectLocation);
    
    
    //the super attack is special as most of the statistics exist inside the attack and only the arrays (hitboxes, hurtboxes, animation) are passed in
    //this is because every super (if there's more than one) has it's own unique class and its statistics will be declared inside it
    int [] super1Anim = {132, 199, 199, 199, 200, 201, 201, 201, 201, 202, 203, 203, 203, 203, 203, 204, 205, 206, 207, 208, 209, 210, 211};
    Rectangle [][] super1Hitbox = new Rectangle [23][1];
    super1Hitbox[5][0] = new Rectangle(252, 266, 265, 110);
    sameBox(super1Hitbox, 5, 6);
    Rectangle [][] super1Hurtbox = new Rectangle [23][1];
    super1Hurtbox[0][0] = new Rectangle(200, 266, 195, 131);
    sameBox(super1Hurtbox, 0, 14);
    super1Hurtbox[15][0] = new Rectangle(222, 211, 93, 187);
    sameBox(super1Hurtbox, 15, 22);
    int [] super1XMove = new int [23];
    super1XMove[1] = 80;
    super1XMove[2] = 60;
    sameNum(super1XMove, 2, 5);
    super1XMove[15] = 52;
    super1XMove[16] = 5;
    sameNum(super1XMove, 16, 22);
    Effect [][] superEffect = new Effect [87][1];
    superEffect [37][0] = new Effect(effect2Sprites, effect2Anim);
    superEffect [45][0] = new Effect(effect2Sprites, effect2Anim);
    superEffect [52][0] = new Effect(effect2Sprites, effect2Anim);
    superEffect [59][0] = new Effect(effect2Sprites, effect2Anim);
    superEffect [66][0] = new Effect(effect2Sprites, effect2Anim);
    superEffect [73][0] = new Effect(effect2Sprites, effect2Anim);
    super1 = new SuperAttack1(super1Anim, super1Hitbox, super1Hurtbox, super1XMove, superEffect);
  }
  
  /*
   * sameNum
   * method that sets a certain interval of an array to the same value (used in some array setting)
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
   * method that sets a certain interval of an array to the same value (used in some hitbox array setting)
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
   * playSound
   * method that plays a sound based on the parameter file
   * @param File audioFile, the file whose sound will be played
   * @return none
   */ 
  private void playSound(File audioFile){
    try{
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
      DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());
      Clip clip = (Clip) AudioSystem.getLine(info);
      clip.open(audioStream);
      clip.start();
    }catch (Exception e){}
  }
  
  /*
   * processStart
   * method that is ran to play the starting animation
   * @param none
   * @return boolean, true if the start animation has completed
   */ 
  public boolean processStart(){
    //set status to starting
    status = "starting";
    
    //calculate gravity as usual for the falling effect at the start
    y+=yVelocity;
    inAir = false;
    if (y<600){
      inAir = true;
      yVelocity = 250;
    }
    if (y>600){
      y = 600;
      yVelocity = 0;
    }
    
    //if in the air animationCounter is set to 0
    if (inAir){
      animationCounter = 0;
    } else { //if on the ground the animation counter increases to play the starting animation
      animationCounter++;
      if (animationCounter>51){ //if the animation is completed set this player to idle and return true
        animationCounter = 51;
        status = "idle";
        return true;
      }
    }
    
    //normally return false
    return false;
  }
  
  /*
   * processEnd
   * method that is ran to play the ending animation
   * @param int width (width of the screen), Player otherplayer (the other player), Effect ArrayList effects (the effect ArrayList in gameframe), these are used to call other methods in this object
   * @return none
   */ 
  public void processEnd(int width, Player otherplayer, ArrayList<Effect> effects){
    
    //call process aerial and projectiles as they keep going after the game has ended
    processAerial();
    processProjectiles(otherplayer);
    
    //if this player has lost
    if (status.equals("lose")){
      
      //calculate velocity and gravity as if player is hurt
      x+=xVelocity;
      xVelocity = xVelocity/2;
      if (xVelocity == 1){
        xVelocity = 0;
      }
      if (x<-250) {
        x = -250;
      }
      if (x>width-300){
        x = width-300;
      }
      y+=yVelocity;
      inAir = false;
      if (y<600){
        inAir = true;
        yVelocity +=10;
      }
      if (y>600){
        y = 600;
        yVelocity = 0;
      }
      
      //increase animation counter to play the losing animation
      if (animationCounter<14){
        if ((animationCounter<12) || !(inAir)){ //only increase past 12 (the part where the player lies down) if on the ground
          animationCounter++;
        }
      }
    } else {
      //the winner completes whatever they're currently doing and ends idle
      processAnimationCounter();
      processResources();
      processMovement(width);
      processProjectiles(otherplayer);
      processHitBox();
      processHits(otherplayer, effects);
    }
  }
  
  /*
   * processProjectiles
   * method that is ran process the projectiles that this player has
   * @param Player otherPlayer, the other player in this game
   * @return none
   */ 
  public void processProjectiles(Player otherPlayer){
    
    //if there is an attack going on and the player is at the frame where a projectile is to be spawned, spawn that projectile
    if (currentAttack != null){
      if (currentAttack.getProjectile(animationCounter)!=null){
        for(int i = 0; i<currentAttack.getProjectile(animationCounter).length; i++){
          if (currentAttack.getProjectile(animationCounter)[i]!= null){
            //spawn projectile based on direction
            if (direction.equals("left")){
              //check if projectile is a special projectile
              if (currentAttack.getProjectile(animationCounter)[i] instanceof SpecialProjectileKnife){
                projectiles.add(new SpecialProjectileKnife(currentAttack.getProjectile(animationCounter)[i], (x+550-currentAttack.getProjectileX(animationCounter, i)-currentAttack.getProjectile(animationCounter)[i].getImage().getWidth(null)), (y+currentAttack.getProjectileY(animationCounter, i)), direction));
              } else {
                projectiles.add(new Projectile(currentAttack.getProjectile(animationCounter)[i], (x+550-currentAttack.getProjectileX(animationCounter, i)-currentAttack.getProjectile(animationCounter)[i].getImage().getWidth(null)), (y+currentAttack.getProjectileY(animationCounter, i)), direction));
              }
            } else {
              if (currentAttack.getProjectile(animationCounter)[i] instanceof SpecialProjectileKnife){
                projectiles.add(new SpecialProjectileKnife(currentAttack.getProjectile(animationCounter)[i], x+currentAttack.getProjectileX(animationCounter, i), y+currentAttack.getProjectileY(animationCounter, i), direction));
              } else {
                projectiles.add(new Projectile(currentAttack.getProjectile(animationCounter)[i], x+currentAttack.getProjectileX(animationCounter, i), y+currentAttack.getProjectileY(animationCounter, i), direction));
              }
            }
          }
        }
      }
    }
    
    //loops through the projectiles array and call process on all of them
    for (int i = 0; i<projectiles.size(); i++){
      if (projectiles.get(i) instanceof SpecialProjectileKnife){ //if the projectile is a special projectile call the special projectile's process
        if (!((SpecialProjectileKnife)projectiles.get(i)).process(otherPlayer)){ //remove the projectile if the process returns false
          projectiles.remove(i);
          i--;
        }
      } else { //do the same for normal projectiles
        if (!projectiles.get(i).process()){
          projectiles.remove(i);
          i--;
        }
      }
    }
  }
  
  /*
   * processHitBox
   * method that adds the appropriate hitboxes and hurtboxes to the player's hitbox and hurtbox arrays
   * @param none
   * @return none
   */ 
  public void processHitBox(){
    //clear the hitbox and hurtbox
    hitbox = null;
    hurtbox = null;
    
    phase = false;
    Rectangle [] currHurtBox;
    //if there is an attack append the current attack's hitboxes to the player's hitbox array
    if (currentAttack != null) {
      if ((currentAttack.getHitBox()!= null) && (currentAttack.getHitBox()[animationCounter][0] != null)){
        hitbox = new Rectangle [currentAttack.getHitBox()[animationCounter].length];
        for (int i = 0; i<currentAttack.getHitBox()[animationCounter].length; i++){
          if (currentAttack.getHitBox()[animationCounter][i] != null){
            if (direction.equals("left")){ //if the player is facing left take the left hitbox
              hitbox[i] = new Rectangle((int)(x+currentAttack.getLHitBox()[animationCounter][i].getX()), (int)(y+currentAttack.getLHitBox()[animationCounter][i].getY()), (int)currentAttack.getLHitBox()[animationCounter][i].getWidth(), (int)currentAttack.getLHitBox()[animationCounter][i].getHeight());
            } else {
              hitbox[i] = new Rectangle((int)(x+currentAttack.getHitBox()[animationCounter][i].getX()), (int)(y+currentAttack.getHitBox()[animationCounter][i].getY()), (int)currentAttack.getHitBox()[animationCounter][i].getWidth(), (int)currentAttack.getHitBox()[animationCounter][i].getHeight());
            }
          }
        }
      }
      //also take the current attack's hurtbox
      currHurtBox = currentAttack.getHurtBox(animationCounter);
      //also record the phase here
      phase = currentAttack.getPhase(animationCounter);
    } else { //if there isn't currently an attack append the hurtbox based on the player's current status
      if (inAir == true){
        currHurtBox = aerialHurtBox;
      } else if (crouching){
        currHurtBox = crouchingHurtBox;
      } else if (blocking){
        currHurtBox = idleHurtBox;
      } else if (status.equals("moving")){
        currHurtBox = movingHurtBox;
      } else {
        currHurtBox = idleHurtBox;
      }
    }
    
    hurtbox = new Rectangle[currHurtBox.length];
    //set the current hurtbox to be the one calculated by the previous loop
    //if the player is facing left reflect the hurtbox
    for(int i = 0; i<currHurtBox.length; i++){
      if (currHurtBox[i] != null){
        if (direction.equals("left")) {
          hurtbox[i] = new Rectangle((int)(x+550-currHurtBox[i].getX()-currHurtBox[i].getWidth()), (int)(y+currHurtBox[i].getY()), (int)currHurtBox[i].getWidth(), (int)currHurtBox[i].getHeight());
        } else {
          hurtbox[i] = new Rectangle ((int)(x+currHurtBox[i].getX()), (int)(y+currHurtBox[i].getY()), (int)currHurtBox[i].getWidth(), (int)currHurtBox[i].getHeight());
        }
      }
    }
  }
  
  /*
   * processHits
   * this method acts as the hit detection for the players, checking if the player's attack hit the other player
   * @param Player otherplayer, the other player to have these attacks be checked with, Effect ArrayList effects which is the effects array from GameFrame passed into here
   * @return none
   */ 
  public void processHits(Player otherplayer, ArrayList<Effect> effects){
    
    //loop through all projectiles of this player and check if any of them hit
    for (int i = 0; i<projectiles.size(); i++){
      if (projectiles.get(i).getHitBox()!=null){
        for(int j = 0; j<projectiles.get(i).getHitBox()[0].length; j++){
          for(int k = 0; k<otherplayer.getHurtBox().length; k++){
            if(otherplayer.getHurtBox()[k]!= null){
              if(projectiles.get(i).getCurrentHitBox(j).intersects(otherplayer.getHurtBox()[k])) { //check if the projectile's hitbox intersects the other player's hurtbpx
                if (otherplayer.isBlocking()){
                  otherplayer.blocked(projectiles.get(i)); //if the other player is blocking call the blocked method (method that registers a blocked attack)
                } else {
                  otherplayer.hurt(projectiles.get(i)); // if the other player is not blocking call the hurt method (method that registers a hit attack)
                  
                  //gain some MP for hitting your attacks
                  mp+=5;
                  if (mp>maxMP){
                    mp = maxMP;
                  }
                }
              }
            }
          }
        }
      }
    }
    
    //loop through the hitboxes of this player and check if any of them hit
    if (hitbox != null) {
      for(int i = 0; i<hitbox.length; i++){
        if (hitbox[i]!=null){
          for(int j = 0; j<otherplayer.getHurtBox().length; j++){
            if(otherplayer.getHurtBox()[j]!= null){
              if(hitbox[i].intersects(otherplayer.getHurtBox()[j])) { // check if the hitbox intersects with the other player's hurtbox 
                if ((currentAttack instanceof Grab) && !((Grab)currentAttack).successful()){ //grabs go through block and invulnerability so they are calculated first
                  otherplayer.hurt(currentAttack, direction);
                  ((Grab)currentAttack).hit(this);
                }
                if (!otherplayer.isInvul()){ // check if the other player is current invulnerable
                  if (otherplayer.isBlocking()){ //if the other player is blocking
                    if(x<otherplayer.getX()){ //knock this player back a little
                      x-=20;
                    } else {
                      x+=20;
                    }
                    otherplayer.blocked(currentAttack, direction); //register the attack as blocked
                    attackSuccessful = true; //set the attacksuccessful variable to true (used in combos)
                  } else { //if the other player is not blocking
                    otherplayer.hurt(currentAttack, direction); //register the attack as hit
                    
                    //gain some MP
                    mp+=5;
                    if (mp>maxMP){
                      mp = maxMP;
                    }
                    
                    if ((currentAttack instanceof IndicatorAttack) && !((IndicatorAttack)currentAttack).successful()){ //if the attack uses IndicatoAttack run the hit method
                      ((IndicatorAttack)currentAttack).hit(this);
                    }
                    attackSuccessful = true; //set attackSuccessful to true
                  }
                }
              }
            }
          }
        }
      }
    }
    
    //if there currently is an attack 
    if (currentAttack != null){
      if ((currentAttack instanceof IndicatorAttack) && ((IndicatorAttack)currentAttack).successful()){ //if there is currently an IndicatorAttack
        ((IndicatorAttack)currentAttack).process(this, otherplayer); // call the IndicatorAttack's process
      }
      if (currentAttack.getEffect(animationCounter)!= null){ //if the current attack spawns an effect
        for (int i = 0; i< currentAttack.getEffect(animationCounter).length; i++){
          if (currentAttack.getEffect(animationCounter)[i]!= null){
            //add the effect to the effects array based the player's location and direction
            if (direction.equals("left")){
              effects.add(new Effect(currentAttack.getEffect(animationCounter)[i], x+550-currentAttack.getEffectX(animationCounter, i)-currentAttack.getEffect(animationCounter)[i].getImage().getWidth(null), y+currentAttack.getEffectY(animationCounter, i), direction));
            } else {
              effects.add(new Effect(currentAttack.getEffect(animationCounter)[i], x+currentAttack.getEffectX(animationCounter, i), y+currentAttack.getEffectY(animationCounter, i), direction));
            }
          }
        }
      }
    }
  }
  
  /*
   * processResources
   * this method processes the mana and barrier of the player
   * @param none
   * @return none
   */ 
  public void processResources(){
    
    //if mana is less than the maximum mana passively generate some mana
    if (mp<maxMP){
      mp+=0.11;
    }else if (mp>maxMP){
      mp = maxMP;
    }
    
    //if barrier is less than the maximum barrier and the barrier bar is currently not on cooldown passively generate some barrier
    if ((bp<maxBP) && (barrierCD == 0)){
      bp+=2;
    } else if (barrierCD>0){ //if the barrier is on cooldown subtract from the cooldown
      barrierCD--;
    }
    if (bp<=0){ //the player cannot block when barrier is gone
      blocking = false;
      bp = 0;
    }
  }
  
  /*
   * processAerial
   * this method checks if the player is in the air and makes sure a ground attack cannot be in the air
   * @param none
   * @return none
   */ 
  public void processAerial(){
    
    //set the inAir variable based on the player's location
    inAir = false;
    if (y<600) {
      inAir = true;
    }
    
    //if on the ground 
    if (inAir == false) {
      if ((currentAttack != null) && (currentAttack.isAerial() == true)){ //if there is an aerial attack while the player is on the ground cancel that attack
        currentAttack = null;
        stunDuration = 0;
        softStunDuration = 0;
        xVelocity = 0;
        yVelocity = 0;
        status = "idle";
      }
      
      //recover the player's jumps remainint
      jumpsLeft = 2;
    }
    
    if ((inAir == true) && (currentAttack != null) && (currentAttack.isAerial() == false)){ //if there is a ground attack in the air cancel that attack
      currentAttack = null;
      stunDuration = 0;
      softStunDuration = 0;
      status = "idle";
    }
  }
  
  /*
   * processMovement
   * this method moves the player
   * @param int width, the width of the allowed field of movement
   * @return none
   */ 
  public void processMovement (int width){
    
    //if the player is on the ground and not blocking (blocking stops movement)
    if (!((blocking) && !(inAir))){
      if (currentAttack == null) { //if there is currently no attack
        if (status.equals("hurt")){ //if the player is hurt move the player based on it's current x velocity
          //knock the player back based on it's x velocity
          x+=xVelocity; 
          //x velocity halves every frame
          xVelocity = xVelocity/2;
          //if xvelocity reaches 1 it becomes 0
          if (xVelocity == 1){
            xVelocity = 0;
          }
        } else if (!crouching){ //i the player is not hurt and not crouchint (crouching stops movement)
          //if only one direction button is currently being pressed move in that direction based on the runSpeed of the player
          //otherwise stop
          if ((movingRight == true) && (movingLeft == true)){
            status = "idle";
          } else if (movingRight == true){
            direction = "right";
            status = "moving";
            x+=runSpeed;
          } else if (movingLeft == true) {
            direction = "left";
            status = "moving";
            x-=runSpeed;
          } else {
            status = "idle";
          }
        }
      } else { //if there is an attack going on
        if (currentAttack.isAerial() == true){ //if the current attack is an aerial attack
          if ((currentAttack.getXMove(animationCounter)!=0) || (currentAttack.isStill(animationCounter))){ //if the attack is still or the xMove variable calls for movement
            //move the player based on direction and the attack's xMove variable
            if (direction.equals("left")){
              xVelocity = -currentAttack.getXMove(animationCounter);
            } else {
              xVelocity = currentAttack.getXMove(animationCounter);
            }
            x+=xVelocity;
          } else { //if the attack is not still but xMove calls for no movement
            //move player based on current trajectory
            x+=xVelocity;
          }
          if ((currentAttack.getYMove(animationCounter)!=0) || (currentAttack.isStill(animationCounter))){ //if the attack is still or the yMove variable calls for movement
            //set yVelocity to the yMove variable
            yVelocity=currentAttack.getYMove(animationCounter);
          }
        } else { //if attack is on the ground
          //move player based on xMove, yMove and direction
          if (direction.equals("left")){
            x-=currentAttack.getXMove(animationCounter);
          } else {
            x+=currentAttack.getXMove(animationCounter);
          }
          y+=currentAttack.getYMove(animationCounter);
        }
      }
    }
    
    //check for boundaries so player cannot leave the screen
    if (x<-250) {
      x = -250;
    }
    if (x>width-300){
      x = width-300;
    }

    //apply y velocity and if in air increase it by 10 because of gravity, also detect if player has hit the ground
    y+=yVelocity;
    inAir = false;
    if (y<600){
      inAir = true;
      yVelocity +=10;
    }
    if (y>600){
      y = 600;
      yVelocity = 0;
    }
  }
  
  /*
   * processAnimationCounter
   * this method calculates the current animationCounter, a variable used in many places but mostly as a index marker for the current image
   * @param none
   * @return none
   */ 
  public void processAnimationCounter (){
    
    //if there is an attack
    if (currentAttack != null) {
      if (animationCounter>=currentAttack.getLength()) { //if the attack has run its course and ended
        if (inAir){
          animationCounter = 6; //set animation counter to 6 (in air idle) if in air
        } else {
          animationCounter = 0; //set animation counter to 0 if on ground
        }
        if ((currentAttack == A2) || (currentAttack == C2)){ //if the attack is a crouching attack set animationcounter to 1
          animationCounter = 1;
        }
        
        //reset the success of IndicatorAttacks
        if (currentAttack instanceof IndicatorAttack){
          ((IndicatorAttack)currentAttack).setSuccess();
        }
        
        //set the current attack back to none
        currentAttack = null;
        attackSuccessful = false;
        stunDuration = 0;
        softStunDuration = 0;
      } else {
        animationCounter++; //if the attack hasn't finished increment animation counter by 1
      }
    } else { //if there isn't an attack
       //general rule of thumb: increase until reached the end of the animation and then either loop back or stay there
      if (inAir == true){ 
        if (animationCounter<jumpAnim.length-1){ 
          animationCounter++;
        }
      } else if (crouching){
        if (animationCounter<1){
          animationCounter++;
        } else {
          animationCounter = 1;
        }
      } else if ((status.equals("idle")) || (blocking)){
        animationCounter = 0;
      } else if (status.equals("moving")){
        if (animationCounter>=10){
          animationCounter = 0;
        } else {
          animationCounter++;
        }
      }
    } 
    
    //remove 1 from all the countdown duration
    if (stunDuration>0){
      stunDuration--;
    }
    if (softStunDuration>0){
      softStunDuration--;
    }
    if ((stunDuration == 0) && (softStunDuration == 0)){ //if the stun has ran out the player is no longer hurt
      status = "idle";
      xVelocity = 0;
    } 
    if (invulDuration>0){
      invulDuration--;
    }
  }
  
  /*
   * processInput
   * this method takes the input the user has given this object
   * @param boolean array in, the keyboard input the user has given
   * @return none
   */
  public void processInput (boolean [] in){
    
    //These variables are updated first as they are more trackers for what keys have been pressed than actual actions
    if (in[1] == true){
      crouching = true;
    } if (in[2] == true){
      movingRight = true;
    } if (in[3] == true){
      movingLeft = true;
    } if (in[8] == true){
      blockPressed = true;
    }
    
    //if the current attack is a special attack check for the command for their activated effect
    if ((currentAttack!=null) && (currentAttack instanceof SpecialAttack)) {
      if (in[7] == true) {
        ((SpecialAttack)currentAttack).activate(this);
        in[7] = false;
      }
    }
    
    //the player has 2 types of stun conditions, stun and softstun. Stun means you cannot take any other movement while softstun can be canceled out of depending on the attack with a specific other move
    //check if player is stunned or blocking, both prevents any other action from being taken
    if ((stunDuration == 0) && !(blocking)){
      
      //check conditions for jump (jump command has been presses, the player hasn't done more than 1 inair jump and either the player is not softstunned or the current attack can be jumpcanceled)
      if (((in[0] == true) && (jumpsLeft>0)) && ((softStunDuration == 0) || (currentAttack != null) && (currentAttack.canJumpCancel()))){
        //if conditions are met jump
        yVelocity=-60;
        animationCounter = 0;
        jumpsLeft--;
        inAir = true;
      }
      if ((in[6] == true) && (inAir == false) && (softStunDuration == 0)){ //check for grab conditions (on the ground and not softstunned)
        //initiate grab
        currentAttack = grab1;
        attackSetup();
      } else if ((in[4] == true) || (in[5] == true) || (in[7] == true) || (in[9] == true)){//check if any of the attack commands are pressed
        //attack condition checking goes as follows: 
        //check for command (in this case on the ground and the super button has been pressed
        //check if the current attack is not itself as attacks do not combo into themselves
        //check either that the player isn't softstunned
        //or there is a current attack that can be combo'd from (the current attack must be attackcancelable and has equal or lower combo priority compared to this attack.
        //supers can be canceled from any attack
        //the above applies to almost every attack below
        //List of attacks:
        //7 light attacks: standing X4 (auto combo), crouching, dashing, jumping
        //4 heavy attscks: standing, crouching, dashing, jumping
        //2 special attacks: standing, jumping
        //1 grab: standing
        //1 super: standing, 100MP
        if (in[9] == true){
          if(!(inAir) && (currentAttack != super1) && (mp>super1.getManaCost()) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.getComboPriority()<= super1.getComboPriority())))){
            currentAttack = super1;
            mp-=super1.getManaCost();
            attackSetup();
          }
        }
        if (in[7] == true){
          //check conditions for special attacks
          if ((inAir) && (currentAttack != jD5) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= jD5.getComboPriority())))){
            currentAttack = jD5;
            attackSetup();
          } else if ((currentAttack != D5) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= D5.getComboPriority())))){
            currentAttack = D5;
            attackSetup();
          }
        }
        if (in[5] == true){
          //check condition for heavy attacks
          if ((inAir) && (currentAttack != jC5) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= jC5.getComboPriority())))){
            currentAttack = jC5;
            attackSetup();
          } else if ((crouching) && (currentAttack != C2) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= C2.getComboPriority())))){
            currentAttack = C2;
            attackSetup();
          } else if ((((movingLeft) && !(movingRight))||(!(movingLeft) && (movingRight))) && (currentAttack != C6) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= C6.getComboPriority())))){
            currentAttack = C6;
            attackSetup();
          } else if ((currentAttack != C5) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= C5.getComboPriority())))){
            currentAttack = C5;
            attackSetup();
          }
          
        } else if (in[4] == true) {
          //check condition for light attacks
          if ((inAir) && (currentAttack != jA5) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= jA5.getComboPriority())))){
            currentAttack = jA5;
            attackSetup();
          } else if ((crouching) && (currentAttack != A2) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= A2.getComboPriority())))) {
            currentAttack = A2;
            attackSetup();
          } else if ((((movingLeft) && !(movingRight))||(!(movingLeft) && (movingRight))) && (currentAttack != A6) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= A6.getComboPriority())))){
            currentAttack = A6;
            attackSetup();
          } else if ((currentAttack != null) && (currentAttack == AAA5) && (attackSuccessful) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= AAAA5.getComboPriority())))){
            //this attack and the 3 attacks below it is a part of an auto combo, which means it shares it's command with the each other and is accessed in a row by repeatingly issueing the same command after hitting the previous attack
            currentAttack = AAAA5;
            attackSetup();
          } else if ((currentAttack != null) && (currentAttack == AA5) && (attackSuccessful) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= AAA5.getComboPriority())))){
            currentAttack = AAA5;
            attackSetup();
          } else if ((currentAttack != null) && (currentAttack == A5) && (attackSuccessful) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= AA5.getComboPriority())))){
            currentAttack = AA5;
            attackSetup();
          } else if ((currentAttack != A5) && (currentAttack != AA5) && ((softStunDuration == 0) || ((currentAttack!= null) && (currentAttack.canAttackCancel()) && (currentAttack.getComboPriority()<= A5.getComboPriority())))){
            currentAttack = A5;
            attackSetup();
          }
        }
      } else { //if no attack comand has been issued
        if ((blockPressed) && (softStunDuration == 0)){ //check if block is pressed and if so activate block
          blocking = true;
          animationCounter = 0;
        } if ((in[1] == true) && (softStunDuration == 0)){ //check if crouching is and previously false if so play the crouch animation by setting animation counter to 0 and switch crouching to true
          if (!crouching && !inAir){
            animationCounter = 0;
          }
          crouching = true;
        } if ((in[2] == true) && (softStunDuration == 0)){ //check if either direction keys is pressed and update the direction of the player accordingly
          if (movingLeft == false){ //the update doesnt happen if both direction keys are held simutainously
            direction = "right";
          }
        } if ((in[3] == true) && (softStunDuration == 0)){
          if (movingRight == false){
            direction = "left";
          }
        }
      }
    }
  }
  
  /*
   * attackSetup
   * this method makes certain changes to the player after an attack has been issued
   * @param none
   * @return none
   */
  private void attackSetup(){
    
    //set the softstun to be the length of the attack
    softStunDuration = currentAttack.getLength()+1;
    //stun the player based on the selfstun of the attack
    stunDuration = currentAttack.getSelfStun();
    //reset animation counter so that the attack animation can play from the beginning
    animationCounter = 0;
    //set this attack to not successful
    attackSuccessful = false;
    
    //update direction of the player
    if ((movingRight != true) && (movingLeft == true)){
      direction = "left";
    } else if ((movingRight == true) && (movingLeft != true)){
      direction = "right";
    }
    //if the player is in the air update the velocity based on direction
    if (currentAttack.isAerial()){
      if ((movingRight == true) && (movingLeft != true)){
        xVelocity = runSpeed;
      } else if ((movingRight != true) && (movingLeft == true)){
        xVelocity = -runSpeed;
      } else {
        xVelocity = 0;
      }
    }
  }
  
  /*
   * hurt
   * this method registers an attack that has hit the player
   * @param Attack object attackRecieved, the attack that has hit this player and String attackDirection, the direction that attack was facing
   * @return none
   */
  public void hurt (Attack attackRecieved, String attackDirection){
    if (!status.equals("lose")){ //check if the player has not already lost
      //reset animation counter
      animationCounter = 0;
      //stun the player based on the attack's stun
      stunDuration = attackRecieved.getStunDuration();
      //change player status to be hurt
      status = "hurt";
      //deduct hp base on the attack's damage
      hp-=attackRecieved.getDamage();
      //cancel any blocking (in the case of grabs or guard piercing attacks)
      blocking = false;
      //give the player invulnerability based on the attack's hitInvul
      invulDuration = attackRecieved.getHitInvul();
      //gain some MP
      mp+=5;
      if (mp>maxMP){
        mp = maxMP;
      }
      //update the direction based on the recieved attack's direction and apply x and y knockback
      if (attackDirection.equals("left")){
        direction = "right";
        xVelocity = -attackRecieved.getXKnockBack();
      } else {
        direction = "left";
        xVelocity = attackRecieved.getXKnockBack();
      }
      if ((attackRecieved.launching()) || (inAir)){ //y knockback only occurs if the player is in air or the attack has the launching property
        yVelocity = -attackRecieved.getYKnockBack();
      }
      if (attackRecieved.getDamage()>0){ // play the hit sound if the attack delt damage
        playSound(hitSound);
      }
    }
  }
  
  /*
   * hurt
   * this method registers a projectile that has hit the player and overloads the previous hurt method
   * @param Projectile object projectileRecieved, the projectile that has hit this player
   * @return none
   */
  public void hurt (Projectile projectileRecieved){
    if (!status.equals("lose")){
      //repeat the same process with recieving the attack but with a projectile's information
      animationCounter = 0;
      stunDuration = projectileRecieved.getStunDuration();
      status = "hurt";
      hp-=projectileRecieved.getDamage();
      blocking = false;
      invulDuration = projectileRecieved.getHitInvul();
      projectileRecieved.setHitBoxActive();
      mp+=5;
      if (mp>maxMP){
        mp = maxMP;
      }
      if (projectileRecieved.getDirection().equals("left")){
        direction = "right";
        xVelocity = -projectileRecieved.getXKnockBack();
      } else {
        direction = "left";
        xVelocity = projectileRecieved.getXKnockBack();
      }
      if ((projectileRecieved.launching()) || (inAir)){
        yVelocity = -projectileRecieved.getYKnockBack();
      }
      if (projectileRecieved.getDamage()>0){
        playSound(hitSound);
      }
    }
  }
  
  /*
   * blocked
   * this method registers an attack that has been blocked by the player
   * @param Attack object attackBlocked, the attack that has been blocked by this player and String attackDirection, the direction that attack was facing
   * @return none
   */
  public void blocked (Attack attackBlocked, String attackDirection){
    //reset animation counter
    animationCounter = 0;
    //stun the player based on attack's blockstun (usually shorter than hitstun)
    stunDuration = attackBlocked.getBlockStun();
    //damage the player's barrier
    bp-=attackBlocked.getShieldDamage();
    //put the barrier on a cooldown so it does not recover while a player is being attacked
    if (bp<=0){
      barrierCD = 120;
    } else {
      barrierCD = 60;
    }
    //grant invulnerability
    invulDuration = attackBlocked.getHitInvul();
    //update the direction and apply knockback
    if (attackDirection.equals("left")){
      direction = "right";
      x -= attackBlocked.getXKnockBack();
    } else {
      direction = "left";
      x += attackBlocked.getXKnockBack();
    }
    playSound(blockSound); //play the block sound
  }
  
  /*
   * blocked
   * this method registers a projectile that has been blocked by the player and overloads the previous blocked method
   * @param Projectile object projectileBlocked, the projectile that has been blocked by this player
   * @return none
   */
  public void blocked (Projectile projectileBlocked){
    //repeat the same process with blocking an attack
    animationCounter = 0;
    stunDuration = projectileBlocked.getBlockStun();
    bp-=projectileBlocked.getShieldDamage();
    if (bp<=0){
      barrierCD = 90;
    } else {
      barrierCD = 30;
    }
    invulDuration = projectileBlocked.getHitInvul();
    projectileBlocked.setHitBoxActive();
    if (projectileBlocked.getDirection().equals("left")){
      direction = "right";
      x -= projectileBlocked.getXKnockBack();
    } else {
      direction = "left";
      x += projectileBlocked.getXKnockBack();
    }
    playSound(blockSound);
  }
  
  /*
   * setMovingRight
   * setter for movingRight, a variable that keeps track of if the right command is pressed (used for smooth movement)
   * always sets to false
   * @param none
   * @return none
   */ 
  public void setMovingRight(){
    movingRight = false;
  }
  
  /*
   * setMovingLeft
   * setter for movingLeft, a variable that keeps track of if the left command is pressed (used for smooth movement)
   * always sets to false
   * @param none
   * @return none
   */ 
  public void setMovingLeft(){
    movingLeft = false;
  }
  
  /*
   * setBlock
   * setter for blocking and blockPressed
   * blockPressed keeps track of if the block command is pressed
   * blocking keeps track of if the player is actually blocking
   * always sets to false
   * @param none
   * @return none
   */ 
  public void setBlock(){
    blocking = false;
    blockPressed = false;
  }
  
  /*
   * setMovingRight
   * setter for currentAttack, a variable that keeps track of the player's current attack
   * always sets to null (removes the attack)
   * @param none
   * @return none
   */ 
  public void setAttack(){
    if (currentAttack instanceof IndicatorAttack){
      ((IndicatorAttack)currentAttack).setSuccess(); //call the setSuccess of the IndicatorAttack interface if the attack uses said interface (so the values of the last attack won't carry over to the next)
    }
    //set currentAttack to null and attackSuccessful to false
    currentAttack = null;
    attackSuccessful = false;
  }
  
  /*
   * addX
   * adds a value to the x location of this player
   * @param int add, the amount that is to be added to x
   * @return none
   */ 
  public void addX(int add){
    x +=add;
  }
  
  /*
   * setStun
   * setter for Stun
   * @param int i, the new value of Stun
   * @return none
   */ 
  public void setStun(int i){
    stunDuration = i;
  }
  
  /*
   * setSoftStun
   * setter for softStun
   * @param int i, the new value of softStun
   * @return none
   */ 
  public void setSoftStun(int i){
    softStunDuration = i;
  }
  
  /*
   * setAnimationCounter
   * setter for animationCounter
   * @param int i, the new value of animationCounter
   * @return none
   */ 
  public void setAnimationCounter(int i){
    animationCounter = i;
  }
  
  /*
   * setX
   * setter for the x location coordinate of this player
   * @param int i, the new value of x
   * @return none
   */ 
  public void setX(int i){
    x = i;
  }
  
  /*
   * setY
   * setter for the y location coordinate of this player
   * @param int i, the new value of y
   * @return none
   */ 
  public void setY(int i){
    y = i;
  }
  
  /*
   * setDirection
   * setter for the direction of the player
   * @param String direction, the new value of direction
   * @return none
   */ 
  public void setDirection(String direction){
    this.direction = direction;
  }
  
  /*
   * setCrouch
   * setter for crouching, that variable that determines if the player is crouching
   * always sets to false
   * @param none
   * @return none
   */ 
  public void setCrouch(){
    crouching = false;
  }
  
  /*
   * setLastStatus
   * setter for lastStatus (used in recovery animation of certain statuses)
   * @param String s, the new value of lastStatus
   * @return none
   */ 
  public void setLastStatus(String s){
    lastStatus = s;
  }
  
  /*
   * setWin
   * setter for status, but only to win and lose
   * @param boolean b, if the player has won the game
   * @return none
   */ 
  public void setWin(boolean b){
    if (b){
      status = "win"; //set status to win
    } else {
      status = "lose"; //set status to lose
      animationCounter = 0; //reset animation counter
    }
  }
  
  /*
   * addMP
   * adds a value to mp
   * @param int i, the value to be added to mp
   * @return none
   */ 
  public void addMP(int i){
    mp+=i;
  }
  
  /*
   * getX
   * getter for x
   * @param none
   * @return int x, the horizontal location of this object
   */ 
  public int getX (){
    return x;
  }
  
  /*
   * getY
   * getter for y
   * @param none
   * @return int y, the vertical location of this object
   */ 
  public int getY (){
    return y;
  }
  
  /*
   * isInvul
   * getter for invulDuration
   * @param none
   * @return boolean value, true if the player is invulnerable and false if not
   */ 
  public boolean isInvul(){
    if(invulDuration == 0){
      return false;
    }
    return true;
  }
  
  /*
   * getDirection
   * getter for direction
   * @param none
   * @return String direction, the direction of this player
   */ 
  public String getDirection(){
    return direction;
  }
  
  /*
   * getHitBox
   * getter for hitbox
   * @param none
   * @return Rectangle array, the current hitbox(s) of this player
   */ 
  public Rectangle[] getHitBox(){
    return hitbox;
  }
  
  /*
   * getAnimationCounter
   * getter for animationCounter
   * @param none
   * @return int animationCounter, the current animation counter of this object
   */ 
  public int getAnimationCounter(){
    return animationCounter;
  }
  
  /*
   * getHurtBox
   * getter for hurtbox
   * @param none
   * @return Rectangle array, the current hurtbox(s) of this player
   */ 
  public Rectangle[] getHurtBox(){
    return hurtbox;
  }
  
  /*
   * isBlocking
   * getter for blocking
   * @param none
   * @return boolean blocking, the variable that determines if the player is currently blocking
   */ 
  public boolean isBlocking(){
    return blocking;
  }
  
  /*
   * getStatus
   * getter for status
   * @param none
   * @return String status, the current status of this player
   */ 
  public String getStatus(){
    return status;
  }
  
  /*
   * getMaxHP
   * getter for maxHP
   * @param none
   * @return int maxHP, the maximum health of this player
   */ 
  public int getMaxHP(){
    return maxHP;
  }
  
  /*
   * getHealth
   * getter for hp
   * @param none
   * @return int hp, the current health of this player
   */ 
  public int getHealth(){
    return hp;
  }
  
  /*
   * getMaxMP
   * getter for maxMP
   * @param none
   * @return int maxMP, the maximum mana of this player
   */ 
  public int getMaxMP(){
    return (int)maxMP;
  }
  
  /*
   * getMP
   * getter for mp
   * @param none
   * @return int mp, the current mana of this player
   */ 
  public int getMP(){
    return (int)mp;
  }
  
  /*
   * getMaxBP
   * getter for maxBP
   * @param none
   * @return int maxBP, the maximum barrier of this player
   */ 
  public int getMaxBP(){
    return (int)maxBP;
  }
  
  /*
   * getBP
   * getter for bp
   * @param none
   * @return int bp, the current barrier of this player
   */ 
  public int getBP(){
    return (int)bp;
  }
  
  /*
   * getPhase
   * getter for phase
   * @param none
   * @return boolean plase, if the player ignores collision detection this turn
   */ 
  public boolean getPhase(){
    return phase;
  }
  
  /*
   * getProjectiles
   * getter for projectiles
   * @param none
   * @return Projectile ArrayList representing the current projectiles of this player
   */ 
  public ArrayList<Projectile> getProjectiles(){
    return projectiles;
  }
  
  /*
   * getImage
   * getter for the sprites of the player
   * @param none
   * @return Image object displayImage which is the current image of this player
   */
  public Image getImage (){
    
    //make sure animationCounter is valid
    if (animationCounter<0){
      animationCounter = 0;
    }
    
    //the sprite array is accessed in the following way:
    //the animationCounter (which increments by 1 each frame) points to an index in the animation array which contains an int
    //the int in the animation array then points to an index in the sprites array which contains the image itself
    //this allows for conservation and reusing of sprites, which drastically reduces load times and storage space
    
    //the image is the idle image by default
    Image displayImage = sprites[idleAnim[0]]; 
    //different animation arrays are used for different statuses of the player
    if (status.equals("starting")){
      displayImage = startSprites[startingAnim[animationCounter]]; //access the starting animation
    } else if (status.equals("lose")) {
      displayImage = loseSprites[loseAnim[animationCounter]]; //access the ending animation
    } else if (currentAttack != null) {
      displayImage = sprites[currentAttack.getAttackAnim(animationCounter)]; //access the attack animation (stored in the attack class)
    } else if (status.equals("hurt")){
      //the hurt animation is calculated in a different way, as instead of using an animationCounter it uses the amount of stuntime left in the player 
      //this is to show the player recovering out of the stun near the end of the stun
      if (stunDuration>=4){
        displayImage = sprites[hurtAnim[0]];
      } else {
        displayImage = sprites[hurtAnim[4-stunDuration]];
      }
    } else if (inAir == true){
      displayImage = sprites[jumpAnim[animationCounter]]; //access the aerial animation
    } else if (blocking){
      if (crouching){
        displayImage = sprites[blockAnim[1]]; //access the crouching block animation
      } else { 
        animationCounter = 0;
        displayImage = sprites[blockAnim[animationCounter]]; //access the standing block animation
      }
    } else if (crouching){
      //make sure the animationCounter is valid
      if (animationCounter>1){
        animationCounter = 1;
      }
      displayImage = sprites[crouchAnim[animationCounter]]; //access the crouching animation
    } else if (status.equals("moving")){
      //make sure the animationCounter is valid
      if (animationCounter>=10){
        animationCounter = 0;
      }
      displayImage = sprites[movingAnim[animationCounter]]; //access the moving animation
    } else if ((lastStatus != null) && (lastStatus.equals("crouching"))){
      //if the last status is crouching it means the player is currently idle but recovering from the crouching status
      displayImage = sprites[crouchAnim[0]]; //access the recovery animation for crouching
    }
    //consume lastStatus when done
    lastStatus = "";
    
    //return the Image
    return displayImage;
  }
}
