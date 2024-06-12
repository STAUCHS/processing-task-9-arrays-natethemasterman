import processing.core.PApplet;

public class Sketch extends PApplet {

  // Holds snowflakes positions
  float[] snowX = new float[42];
  float[] snowY = new float[42];
  boolean[] ballHideStatus = new boolean[42];

  // Diameter of snowflakes
  int snowDiameter = 15;

  // Speed of snowflakes falling
  float snowSpeed = 2;

  // Global variables for player functions
  float playerX, playerY;
  float playerDiameter = 20; // player diameter
  int lives = 3; // lives player has
  boolean gameOver = false; 

  public void settings() {
    // Size Call
    size(400, 400);
  }

  public void setup() {
    // Set background color
    background(0);

    // make player spawn in middle of the x-axis
    playerX = width / 2;
    playerY = height - 30;

    // Generate random x- and y-values for snowflakes
    for (int i = 0; i < snowX.length; i++) {
      snowX[i] = random(width);
      snowY[i] = random(-height, 0); // y starts off of the screen
      ballHideStatus[i] = false; 
    }
  }

  public void draw() {
    if (gameOver) {
      // when game ends, the screen turns white
      background(255); 
      // Stop drawing
      return; 
    }
    background(0);

    // Drawing the initial parts of the game(lives in top right corner, player, snow falling, check for collision between snow and player)
    drawLives();
    drawPlayer();
    snow();
    checkCollisions();
  }

  public void drawPlayer() {
    // color the player blue
    fill(0, 0, 255); 
    // make a circle to represent player
    circle(playerX, playerY, playerDiameter);
  }

  public void drawLives() {
    // color amount of lives red
    fill(255, 0, 0); 
    // draw  squares that represent lives
    for (int i = 0; i < lives; i++) {
      rect(width - 20 * (i + 1), 10, 15, 15);
    }
  }

  public void snow() {
    for (int i = 0; i < snowX.length; i++) {
      // Only draw visible snow
      if (!ballHideStatus[i]) {
        // set color to whit
        fill(255);
        // draw snowflakes
        circle(snowX[i], snowY[i], snowDiameter);
        snowY[i] += snowSpeed;

        // Reset snowflakes when they hit with bottom
        if (snowY[i] > height) {
          resetSnowflake(i);
        }
      }
    }
  }

  public void checkCollisions() {
    for (int i = 0; i < snowX.length; i++) {
      if (!ballHideStatus[i]) {
        // if player is a certain distance between snow, it counts as collision and when they collide the lives decrease by one
        float d = dist(playerX, playerY, snowX[i], snowY[i]);
        if (d < (playerDiameter + snowDiameter) / 2) {
          lives--;
          resetSnowflake(i);
          // game ends when no lives are left
          if (lives <= 0) {
            gameOver = true;
          }
        }
      }
    }
  }

  public void resetSnowflake(int i) {
    // reset to random y position off the screen
    snowY[i] = random(-height, 0); 
    // randomize x position
    snowX[i] = random(width);      
    // make snowball visible
    ballHideStatus[i] = false;     
  }

  public void keyPressed() {
    // control speed of snow falling with up and down keys
    if (keyCode == DOWN) {
      snowSpeed += 1;
    } else if (keyCode == UP) {
      snowSpeed -= 1;
      if (snowSpeed < 1) {
        // speed cannot be slowed to 0(there would be no point of the game if the snow completely stopped falling)
        snowSpeed = 1; 
      }
    }

    // WASD keys - player movement 
    if (key == 'w' || key == 'W') {
      playerY -= 5;
    }
    if (key == 's' || key == 'S') {
      playerY += 5;
    }
    if (key == 'a' || key == 'A') {
      playerX -= 5;
    }
    if (key == 'd' || key == 'D') {
      playerX += 5;
    }

    // constrain player from leaving the screen(i dont know if i used it perfectly but this is easiest way i found to do this)
    playerX = constrain(playerX, 0, width);
    playerY = constrain(playerY, 0, height);
  }

  public void mousePressed() {
    // hide snowflakes when clicked
    for (int i = 0; i < snowX.length; i++) {
      if (!ballHideStatus[i]) {
        float d = dist(mouseX, mouseY, snowX[i], snowY[i]);
        if (d < snowDiameter / 2) {
          ballHideStatus[i] = true; // hide snowflake on click
        }
      }
    }
  }
}