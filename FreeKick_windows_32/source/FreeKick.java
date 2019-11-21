import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 
import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class FreeKick extends PApplet {










float x;
float y;
float W;
float H;
float xRotate;
float yRotate = 0; 
float zRotate;
PImage soccer;
float px, gX;
float py, gY;
float pz, gZ;
float pzBackup, pyBackup;
float kickLimit = 0;
float pyLimit = 0;
float pxLimit;
float goalieX;
float goalieY;
float goalieGreen = 0;
int scoreAI, score = 0;

Timer timer;
Ball ball;
Plane line1, line2, line3, line4, line5, line6, line7, line8, p1,p2, goalie;
Cube c;
Goal goal1, goal2;
PeasyCam cam;
AudioSample bounce1, bounce2, bounce3, bounce4, bounce5, bounce6, kickSound, kickBallFar;
AudioPlayer crowd,goalSound, Oooh;
Minim minim;
boolean[] arrows;

public void setup() {
  
  //size(1000, 600, P3D);
  //frameRate(16);
  W = width;
  H = height;
  timer = new Timer(1);
  soccer = loadImage("soccerBall.jpg");
  c = new Cube(W, H, W);
  pxLimit = width*0.5f;
  p1 = new Plane(30, 30, 60);
  p2 = new Plane(60, 60, 60);
  line1 = new Plane(1, 1, c.d);
  line2 = new Plane(1, 1, c.d);
  line3 = new Plane(c.w, 1, 1);
  line4 = new Plane(c.w, 1, 1);
  line5 = new Plane(1, 1, c.d);
  line6 = new Plane(1, 1, c.d);
  line7 = new Plane(1, 1, c.d);
  line8 = new Plane(1, 1, c.d);
  goalie = new Plane(100, 100, 30);//c.z+(c.d/2)-35
  ball = new Ball(c.x, c.y+(c.h/2)-21, c.z+(c.d*0.5f)-(((W/64)*4)+1), soccer);
  goal1 = new Goal(width*0.5f, width*0.25f, width*0.05f);
  goal2 = new Goal(width*0.5f, width*0.25f, width*0.05f);
  arrows = new boolean[11];
  //cam = new PeasyCam(this,width*0.5,height*0.5,0,600);
  minim = new Minim(this);
  kickSound = minim.loadSample("BallKick.mp3", 1024);
  kickBallFar = minim.loadSample("BallKickFar.mp3", 1024);
  bounce1 = minim.loadSample("Bounce1.mp3", 1024);
  bounce2 = minim.loadSample("Bounce2.mp3", 1024);
  bounce3 = minim.loadSample("Bounce3.mp3", 1024);
  bounce4 = minim.loadSample("Bounce4.mp3", 1024);
  bounce5 = minim.loadSample("Bounce5.mp3", 1024);
  bounce6 = minim.loadSample("Bounce6.mp3", 1024);
  goalSound = minim.loadFile("GritoDeGol.mp3", 1024);
  Oooh = minim.loadFile("OooHStereo.mp3", 1024);
  crowd = minim.loadFile("Crowd.mp3");
  crowd.setGain(-3);
  crowd.loop();
  px = c.x;
  py = c.y+(c.h*0.5f)-(p1.ph*0.5f);
  pz = c.z+(c.d*0.5f)+((p1.pd*0.5f)-(ball.rad*3));//c.z+(c.d*0.5)+((p1.pd*0.5)+1);
  gX = c.x;
  gY = c.y+(c.h*0.5f);
  gZ = c.z-(c.d*0.5f);
  goalie.add(gX, gY, gZ);
  goalieX = width*0.5f;
  goalieY = height*0.5f;
  pyLimit = c.y+(c.h*0.5f)-(p1.ph*0.99f);//H-(p1.ph*0.25);
}

public void draw() {
  background(0);
  translate(width/2, 0);
  //fill(255, 255, 0);
  //sphere(20);
  lights();
  //rotateY(radians(frameCount)*0.5);
  pointLight(250, 250, 250, 0, 0, height*0.5f);
  //translate(-width/2, -height/2, -width);
  rotateY(yRotate);

  translate(-width/2, -40, -760);
  //if (yRotate < 100) {
  translate(yRotate*445, yRotate*20, yRotate*485);
  //} else 
  //if (yRotate > 100) {
  //translate(-yRotate*500,0,-yRotate*500);
  //}

  pushMatrix();
  translate(0, c.h, width/2);
  rotateX(PI*1.5f);
  noStroke();
  fill(0, 120, 0);
  rect(c.x-(c.w*0.5f), c.y-(c.h*0.75f), c.x+(c.w*0.5f), c.y+(c.d));
  popMatrix();
  if (arrows[0]) {
    if (kickLimit < 120) {
      kickLimit += 2;
      pz += 2;
    }
  }
  if (arrows[1]) {
    if (pxLimit <= c.x+(c.w*0.5f)-p1.pw) {
      pxLimit += 7;
      px -= 7;
    }
  } else if (arrows[2]) {
    if (pxLimit >= c.x-(c.w*0.5f)+p1.pw) {
      pxLimit -= 7;
      px += 7;
    }
  }
  //print(pxLimit);
  if (arrows[3]) {
    if (pyLimit > H-(goal2.gh)+(p1.ph*0.5f)) {//c.y+c.h*0.5-(p1.ph*0.2)
      pyLimit -= 5;
      py -= 5;
      //py = py*3;
    }
  } else if (arrows[4]) {
    if (pyLimit < c.y+(c.h*0.5f)-(p1.ph*0.25f)) {
      pyLimit += 5;
      py += 5;
      //py = py*3;
      //  text(pyLimit,width*0.5,height);
    }
  }
  if (arrows[9]) {
    if (pyLimit > H-(goal2.gh)+(p1.ph*0.5f)) {//c.y+c.h*0.5-(p1.ph*0.2)
      pyLimit -= 1;
      py -= 1;
      //py = py*3;
    }
  } else if (arrows[10]) {
    if (pyLimit < c.y+(c.h*0.5f)-(p1.ph*0.25f)) {
      pyLimit += 1;
      py += 1;
      //py = py*3;
      //  text(pyLimit,width*0.5,height);
    }
  }

  if (arrows[5]) {
    //if (pyLimit > H-(goal2.gh)+p1.ph) {
    //  pyLimit -= 7;
    pz += 7;
    //py = py*3;
    //}
  } else if (arrows[6]) {
    //if (pyLimit < H) {
    //  pyLimit += 7;
    pz -= 7;
    //py = py*3;
    //  text(pyLimit,width*0.5,height);
    //}
  }

  if (arrows[7]) {
    if (yRotate < 1.56f) {
      yRotate += 0.01f;
    }
  } else if (arrows[8]) {
    if (yRotate > 0.01f) {
    yRotate -= 0.01f;
    }
  }
pushMatrix();
rotateY(-yRotate*1.1f);
translate(-yRotate*510,0,-yRotate*480);
  textAlign(CENTER);
  textSize(64);
  fill(255, 0, 0);
  text(scoreAI, width*0.25f, height*0.35f);
  fill(0, 0, 255);
  text(score, width*0.75f, height*0.35f);
  popMatrix();
  //text(round(px), width*0.5, height*0.25);
  p1.add(px, py, pz);
  p2.add(px, py, pz);
  line1.add(px-(p1.pw*0.5f), c.y+c.h*0.5f, c.z);
  line2.add(px+(p1.pw*0.5f), c.y+c.h*0.5f, c.z);
  line3.add(c.x, c.y+c.h*0.5f, pz-p1.pd*0.5f);
  line4.add(c.x, c.y+c.h*0.5f, pz+p1.pd*0.5f);
  line5.add(c.x+c.w*0.5f, py+(p1.ph*0.5f), c.z);
  line6.add(c.x+c.w*0.5f, py-(p1.ph*0.5f), c.z);
  line7.add(c.x-c.w*0.5f, py+(p1.ph*0.5f), c.z);
  line8.add(c.x-c.w*0.5f, py-(p1.ph*0.5f), c.z);
  pushMatrix();
  translate(0, 0, width*0.5f);
  noFill();
  stroke(255.100f);
  //rect(width*0.235,height*0.592,15,185);
  //rect(width*0.235,height*0.9,15,60);
  rect(width*0.75f, height*0.8f, 15, 120);
  noStroke();
  fill((pyLimit*3)+33, 0, -(pyLimit*3)+70);
  //println((pyLimit*2)+120);
  //rect(width*0.235,height*0.9,15,pyLimit*2.4);
  noStroke();
  fill(kickLimit*2+15, 0, 255-(kickLimit*2));
  //println(kickLimit*2);
  rect(width*0.75f, height*0.8f, 15, kickLimit);
  popMatrix();
  p1.showPlane();
  stroke(255,50);
  strokeWeight(0.5f);
  noFill();
  p2.showPlane();
  noStroke();
  fill(150);
  line1.showPlane();
  line2.showPlane();
  line3.showPlane();
  line4.showPlane();
  line5.showPlane();
  line6.showPlane();
  line7.showPlane();
  line8.showPlane();
  c.display();
  goal1.addGoal(c.x, c.y+(c.h*0.5f), c.z-(c.d*0.5f));
  //stroke(255,50);
  goal2.addGoal(c.x, c.y+(c.h*0.5f), c.z+(c.d*0.5f)+goal2.gd);

  noStroke();
  goalie.move(moveGoalieX(), moveGoalieY());
  fill(255, goalieGreen, 0);
  goalieGreen -= 10;
  goalie.showPlane();
  ball.edges();
  ball.update();
  ball.display();
}

public void keyPressed() {
  if (keyCode == 32 || keyCode == ENTER) {
    pzBackup = pz;
    arrows[0] = true;
  }
  if (keyCode == LEFT) {
    arrows[1] = true;
  }
  if (keyCode == DOWN) {
    arrows[5] = true;
  }
  if (keyCode == UP) {
    arrows[6] = true;
  }
  if (keyCode == RIGHT) {
    arrows[2] = true;
  }
  if (key == 'w') {
    pyBackup = py;
    arrows[3] = true;
  }
  if (key == 's') {
    pyBackup = py;
    arrows[4] = true;
  }
  if (key =='W') {
    arrows[9] = true;
  }
  if (key =='S') {
    arrows[10] = true;
  }
  if (key == 'r' || key == 'R') {
    ball.reset();
  }
  if (key == 'd' || key == 'D') {
    arrows[7] = true;
    //yRotate -= 0.01;
  }
  if (key == 'a' || key == 'A') {
    arrows[8] = true;
    //yRotate += 0.01;
  }
}

public void keyReleased() {
  if (keyCode == 32 || keyCode == ENTER) {
    arrows[0] = false;
    p1.pos.z = pz-pzBackup-10;//c.z+(c.d*0.5)-10;
    ball.kickKey(pzBackup-pz);
    pz = pzBackup;
    kickLimit = 0;

    //println(pz);
  }
  if (keyCode == LEFT) {
    arrows[1] = false;
  }
  if (keyCode == RIGHT) {
    arrows[2] = false;
  }
  if (key == 'w') {
    arrows[3] = false;
  }
  if (key == 's') {
    arrows[4] = false;
  }
  if (key =='W') {
    arrows[9] = false;
  }
  if (key =='S') {
    arrows[10] = false;
  }
  if (keyCode == DOWN) {
    arrows[5] = false;
  }
  if (keyCode == UP) {
    arrows[6] = false;
  }
  if (key == 'd' || key == 'D') {
    arrows[7] = false;
    //yRotate -= 0.01;
  }
  if (key == 'a' || key == 'A') {
    arrows[8] = false;
    //yRotate += 0.01;
  }
}

public void mouseDragged() {
  pz = mouseY;//map(mouseX,0,displayWidth,-(displayWidth)-(p1.pd*2),displayWidth)+p1.pd;
  px = mouseX;//map(mouseY,0,displayHeight,displayWidth*2,0);
}

public float moveGoalieX() {
  //println("GOALIE"+goalie.pos.x);
  //println("BALL"+ball.pos.x);

  if (goalie.pos.x+goalie.pd*0.5f < ball.pos.x-ball.rad) {
    if (abs(ball.vel.x) < 3) {
      goalieX = 2.5f;
    } else {
      goalieX = 9;
    }
    //gX += 1;
  } else if (goalie.pos.x-goalie.pd*0.5f > ball.pos.x+ball.rad) { // (error*level)-constant  -  (error*level)+constant
    if (abs(ball.vel.z) < 3) {
      goalieX = -2.5f;
    } else {
      goalieX = -9;
    }
    //gX -= 1;
  }
  return goalieX;
}

public float moveGoalieY() {
  if (goalie.pos.y+goalie.ph*0.5f < ball.pos.y-ball.rad) {
    if (abs(ball.vel.z) < 3) {
      goalieY = 3;
    } else {
      goalieY = 9;
    }
    //gY += 1;
  } else if (goalie.pos.y-goalie.ph*0.5f > ball.pos.y+ball.rad) { // (error*level)-constant  -  (error*level)+constant
    if (abs(ball.vel.y) < 3) {
      goalieY = -3;
    } else {
      goalieY = -9;
    }
    //gY -= 1;
  }
  return goalieY;
}
class Ball {
  //float x;
  //float y;
  float rad;
  float zRotate;
  float xRotate;
  PVector pos;
  PVector acc;
  PVector vel;
  PVector grv;
  PShape bola;
  //float ballDamp = 0.5;
  boolean turnZ = true;
  boolean turnX = true;
  boolean trueGoal = false;
  float vol, volGoal;
  float yVel;
  int num = 0;

  Ball(float x, float y, float z, PImage img) {
    pos = new PVector(x, y, z);
    vel= new PVector(0, 0, 0); 
    acc = new PVector(0, 0, 0);
    grv = new PVector(0, 0, 0);
    rad = 20;
    noStroke();
    noFill();
    //fill(255,255,0);
    bola = createShape(SPHERE, rad);
    bola.setTexture(img);
  }

  public void kickKey(float velZ) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/2)
      && ball.pos.x+ball.rad >= p1.pos.x-p1.pw/2 
      && ball.pos.x-ball.rad <= p1.pos.x+p1.pw/2 
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/2 
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/2) 
    {
      vel.z = -1*abs(velZ*0.2f);//-1*abs((pmouseY - mouseY)/8);
//println(vel.z);
      acc.z = 0.02f;
      //acc.y = -1*abs(velZ/12);//-1*abs((pmouseY - mouseY)/8);
      acc.y = ((pos.y - p1.pos.y)+(velZ*0.2f))*0.5f;
      grv.y = 0.4f;
      //kickSound.trigger();
      vel.x = (pos.x - p1.pos.x)*0.5f;
      kickSound.setGain(map(abs(velZ), 0, 120, -30, 0));
      kickSound.trigger();
      acc.x = 0.02f;
      vol = 0;
      yVel = 0.2f;
      //acc.y = 0.035;
      //acc.y = ((pos.y - p1.pos.y));//+(velZ*0.5))*0.33;
    }
  }
  
  public void kickMouse(float velZ) {
    //println(velZ);
    num++;
    kickSound.setGain(map(abs(velZ), 0, 60, -20, 10));
    kickSound.trigger();
    //vel.y = 0.2;
    vel.z = velZ/4;
    vel.x = (pos.x - p1.pos.x)/2;
    acc.y = ((pos.y - p1.pos.y)/2)+(velZ/10);
    acc.x = 0.02f;
    vol = 0;
    yVel = 0.2f;
    //passing de parameter velZ with the speed of Z axis
    //vel.z = -1*abs((pmouseY - mouseY)/4);
    acc.z = 0.02f;
    //acc.y = -1*abs((pmouseY - mouseY)/4);
    grv.y = 0.4f;
  }
  

  public void edges() {
    //    if (pos.x+(rad/2) < p1.gx-(p1.gl/2)) {// || pos.y > p1.gy+p1.ph/2) {
    //p1.pos.x += 1;
    vel.y = yVel;
    //vel.z = 0.2;
    acc.add(grv);
    //vel.add(acc);
    //    }
    // Y edges
    if (pos.y+(rad) >= c.y+(c.h/2)) {
      pos.y = c.y+(c.h/2)-rad;
      acc.y *= -0.75f;//= -1 * (abs(acc.y*0.65));
      int num = round(random(1, 4));
      //if (vel.y < 0.201 && vel.y > -0.201) {
        //yVel = 0.0;
        switch (num) {
        case 1:
          bounce1.setGain(vol);
          bounce1.trigger();
          break;
        case 2:
          bounce2.setGain(vol);
          bounce2.trigger();
          break;
        case 3:
          bounce4.setGain(vol);
          bounce4.trigger();
          break;
        case 4:
          bounce5.setGain(vol);
          bounce5.trigger();
          break;
        }
      //}
    } else if (pos.y+(rad+3) <= c.y-(c.h/2)) {
      bounce1.setGain(vol);
      bounce1.trigger();
      acc.y *= -1;//(abs(acc.y*0.7));
    }

    // X edges
    if (pos.x-rad <= c.x-(c.w/2)) {
      pos.x = c.x-(c.w/2)+rad;
      vel.x *= -0.99f;//(abs(vel.x));
      acc.x *= -0.9f;//*(abs(acc.x));
      bounce3.setGain(vol);
      bounce3.trigger();
      if (turnZ) {turnZ = false;} else {turnZ = true;}
    } else if (pos.x+rad >= c.x+(c.w/2)) {
      if (turnZ) {turnZ = false;} else {turnZ = true;}
      pos.x = c.x+(c.w/2)-rad;
      vel.x *= -0.99f;//*(abs(vel.x));
      acc.x *= -0.9f;//(abs(acc.x));
      bounce6.setGain(vol);
      bounce6.trigger();
    }
    // Z edges


    if (pos.z-(rad) <= c.z-(c.d/2)) {
      if (
        pos.x-rad > goal1.pos.x-(goal1.gw/2) 
        && pos.x+rad < goal1.pos.x+(goal1.gw/2) 
        && pos.y-rad > goal1.pos.y-(goal1.gh)
        ) {
        goalSound.setGain(volGoal);
        goalSound.play();
        trueGoal = true;
        vel.x = 0;
        vel.z *= 0.99f;
        acc.z *= 0.9f;
      } else if (trueGoal == false)
      {
      if (turnX) {turnX = false;} else {turnX = true;}
        pos.z = c.z-(c.d/2)+rad;
        bounce6.setGain(vol);
        bounce6.trigger();
        vel.z *= -0.99f;//abs(vel.z)*0.7;
        acc.z *= -0.9f;//-0.7;
      }
    } else 
    if (pos.z - rad <= goalie.pos.z + (goalie.pd/2) 
      && pos.x+rad > goalie.pos.x-(goalie.pw/2) 
      && pos.x-rad < goalie.pos.x+(goalie.pw/2) 
      && pos.y+rad > goalie.pos.y-(goalie.ph/2)
      && pos.y-rad < goalie.pos.y+(goalie.ph/2)
      ) {
      if (turnX) {turnX = false;} else {turnX = true;}
      pos.z = goalie.pos.z+(goalie.pd/2)+rad;
      vel.z *= -0.99f;//abs(vel.z);
      acc.z *= -0.9f;
      goalieGreen = 255;
      kickBallFar.setGain(-7);
      kickBallFar.trigger();
    }

    if (pos.z >= c.z+(c.d/2)) {
      if (
        pos.x-rad > goal1.pos.x-(goal1.gw/2) 
        && pos.x+rad < goal1.pos.x+(goal1.gw/2) 
        && pos.y-rad > goal1.pos.y-(goal1.gh)
        ) {
        Oooh.setGain(-5);
        Oooh.play();
        trueGoal = true;
        vel.x = 0;
        vel.z *= 0.99f;
        acc.z *= 0.9f;
      } else if (trueGoal == false)
      {
        bounce3.setGain(vol);
        bounce3.trigger();
        vel.z *= -0.99f;//-1*abs(vel.z)*0.7;
        acc.z *= -0.9f;
      }
    } 

    collision();
  }

  public void update() {
//if (turnZ) {
//    zRotate -= abs(vel.x*0.04);
//} else {zRotate += abs(vel.x*0.04);}

//    if (turnX) {
//    xRotate += abs(vel.z*0.04);
//} else {xRotate -= abs(vel.z*0.04);}
    zRotate += (vel.x*0.03f);//-(vel.z*0.01);;
    xRotate -= (vel.z*0.03f);//-(vel.x*0.01);
    vel.add(acc);
    pos.add(vel);

    //if (vel.z < 0.0099 && vel.z > -0.0099 && pos.y+rad >= c.y+(c.h/2)){// && abs(vel.x) > 0.0099) {
    //  acc.z = 0;
      //vel.z = 0;
      //grv.z = 0;
      //grv.y = 0;
      //acc.y = 0;
      //grv.y *= 0.3;
      //acc.y *= 0.3;
      //vel.y *= 0.3;
      //if (acc.y == 0) {
      //  if (pos.y < c.y+(c.h*0.5)-(rad+1)) {
      //    pos.y += 1;
      //  }
      //}
    //} 
    //if (vel.x < 0.0099 && vel.x > -0.0099 && pos.y+rad >= c.y+(c.h/2)){// && abs(vel.z) > 0.009) {
    //  acc.x = 0;
      //vel.x = 0;
      //grv.z = 0;
      //grv.y = 0;
      //acc.y = 0;
      //grv.y *= 0.3;
      //acc.y *= 0.3;
      //vel.y *= 0.3;
      //if (acc.y == 0) {
      //  if (pos.y < c.y+(c.h*0.5)-(rad+1)) {
      //    pos.y += 1;
      //  }
      //}
    //}

    //if (pos.z-(rad/2) < c.z-(c.d/2)) { //*****************************************************************
    //  volGoal -= 0.1;
    //}

    //if (vel.z == 0) {
    //  reset();
    //}
    //acc.y *= 0.00009;
    //println(vel.z);
    //println(acc.z);
    //println(grv.z);
    //println();

    acc.z *= 0.99f;
    vel.z *= 0.995f;
    acc.x *= 0.995f;
    vel.x *= 0.995f;
    acc.y *= 0.995f;
    vel.y *= 0.995f;
    //grv.y *= 0.999;
}

  public void display() {
    vol -= 0.07f;
    //vol = (abs(vel.z)-35);
    pushMatrix();
    translate(pos.x, pos.y+5, pos.z);
    rotateZ(zRotate);
    rotateX(xRotate);
    shape(bola);
    //sphere(rad);
    popMatrix();
  }

  public void reset() {
    if (pos.z-rad <= c.z-(c.d/2) 
      && pos.x-rad > goal1.pos.x-(goal1.gw/2) 
      && pos.x+rad < goal1.pos.x+(goal1.gw/2) 
      && pos.y-rad > goal1.pos.y-(goal1.gh)) {
      score++;
    } else
    if (pos.z+rad >= c.z+(c.d/2) 
      && pos.x-rad > goal1.pos.x-(goal1.gw/2) 
      && pos.x+rad < goal1.pos.x+(goal1.gw/2) 
      && pos.y-rad > goal1.pos.y-(goal1.gh)) {
      scoreAI++;
    }
    goalSound.pause();
    Oooh.pause();
    ball.pos.x = c.x;
    ball.pos.y = (c.y+c.h/2)-(rad+1);
    ball.pos.z = c.z+(c.d*0.5f)-(rad*4)-1;  
    ball.vel.mult(0);//c.z+(c.d/2)+(p1.pd
    ball.acc.mult(0);
    //ball.grv.mult(0);
    goalSound.rewind();
    Oooh.rewind();
    //ball.volGoal = 0;
    ball.trueGoal = false;
    //pz = c.z+(c.d*0.5)+((p1.pd*0.5)+1);
    //pz = c.z+(c.d*0.5)+((p1.pd*0.5)-(rad*3));
    //px = width/2;
  }
}



//else                                                  //******************************************
//  if (pos.z + rad >= p1.pos.z - (p1.pd/2) 
//    && pos.x+rad >= p1.pos.x-(p1.pw/2) 
//    && pos.x-rad <= p1.pos.x+(p1.pw/2) 
//    && pos.y+rad >= p1.pos.y-(p1.ph/2)
//    && pos.y-rad <= p1.pos.y+(p1.ph/2)
//    ) {
//    pos.z = p1.pos.z - (p1.pd/2)-rad;
//    vel.z = -1*abs(vel.z);
//    acc.z *= -1.1;
//    //goalieGreen = 255;
//    kickSound.setGain(-7);
//    kickSound.trigger();
//    if (turnX) {
//      turnX = false;
//    } else {
//      turnX = true;
//    }
//  }

//  if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/2)
//    && ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/2)
//    && ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/2) 
//    && ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/2) 
//    && ball.pos.y+ball.rad >= p1.pos.y-(p1.ph/2) 
//    && ball.pos.y-ball.rad <= p1.pos.y+(p1.ph/2) 
//    )
//  {
//    if ((pmouseY - mouseY) > 0) {
//      pos.z = (p1.pos.z-p1.pd)-(ball.rad);
//      //println("NEG"+(p1.pos.z-p1.pd-(ball.rad*8)));
//      //println(-1*(abs(pmouseY - mouseY)*0.25));
//      kick(-1*(abs(pmouseY - mouseY)*0.25));
//    } 
//    else 
//    if ((pmouseY - mouseY) < 0) {
//      pos.z = (p1.pos.z+p1.pd)+(ball.rad);
//      //println("POS"+p1.pos.z+p1.pd+(ball.rad*8));
//      kick(abs(pmouseY - mouseY));
//    }
//  }
public void collision() {

  // -Z collision

  if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/2)) {
    if (ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/4) &&
      ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/4) 
      && ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/4) 
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/2
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/2
      ) 
    {
      ball.pos.z = p1.pos.z-(p1.pd/2)-ball.rad;
      ball.kickMouse(-1*abs(pmouseY - mouseY));
      if ((pmouseY - mouseY) == 0) {
      ball.vel.z = -1*abs(ball.vel.z);
      ball.vel.z += -3;
      }
    }
  } 

  // +Z collision

  if (ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/2)) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/4) && 
      ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/4) 
      && ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/4) 
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/6
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/6
      ) 
    {
      ball.pos.z = p1.pos.z+(p1.pd/2)+ball.rad;
      ball.kickMouse(abs(pmouseY - mouseY));
      ball.vel.z = -1*abs(ball.vel.z);
      if ((pmouseY - mouseY) == 0) {
      ball.vel.z = -1*abs(ball.vel.z);
      ball.vel.z += 3;
      }
    }
  }

  // -X collision
  if (ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/2)) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/4)
      && ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/6) 
      && ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/4)
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/6
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/6
      ) 
    {
      ball.pos.x = p1.pos.x-(p1.pw/2)-ball.rad;
      ball.vel.x = -(abs(pmouseX - mouseX)/2);
    ball.vel.z = (ball.pos.z - p1.pos.z)/10;
      ball.vel.x = -1*abs(ball.vel.x);
    }
  }

  // +X collision
  if (ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/2)) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/4)
      && ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/4)
      && ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/6) 
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/6
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/6
      ) 
    {
      ball.pos.x = p1.pos.x+(p1.pw/2)+ball.rad;
      ball.vel.x = abs(pmouseX - mouseX)/2;
      ball.vel.z = (ball.pos.z - p1.pos.z)/10;
      ball.vel.x = -1*abs(ball.vel.x);
    }
  }

  // -Y collision
  if (ball.pos.y+ball.rad >= p1.pos.y-p1.ph/2) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/4)
      && ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/4)
      && ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/6) 
      && ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/6)
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/6
      ) 
    {
      ball.pos.y = p1.pos.x-(p1.pw/2)-ball.rad;
      ball.vel.y = -1*abs(ball.vel.y);
    }
  }

  // +Y collision
  if (ball.pos.y-ball.rad <= p1.pos.y+p1.ph/2 ) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/4)
      && ball.pos.z-ball.rad <= p1.pos.z+(p1.pd/4)
      && ball.pos.x+ball.rad >= p1.pos.x-(p1.pw/6) 
      && ball.pos.x-ball.rad <= p1.pos.x+(p1.pw/6)
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/6
      ) 
    {
      ball.pos.y = p1.pos.x+(p1.pw/2)+ball.rad;
      ball.vel.y = -1*abs(ball.vel.y);
      
    }
  }
}
class Cube {
  float x = width*0.5f;
  float y = height*0.5f;
  float z = 0;
  float w;
  float h;
  float d;
  float yRot = 0;
  
  Cube(float cx, float cy, float cz) {
    w = cx;
    h = cy;
    d = cz;
  }
  
  public void display() {
    pushMatrix();
    translate(x,y,z);
    rotateY(yRot);
    strokeWeight(1);
    stroke(100,150,100);
    noFill();
    //fill(255,10);
    box(w,h,d);
    popMatrix();
    
  }
}
class Goal {
  PVector pos;
  float gw;
  float gh;
  float gd;
  //float gx,gy,gz;
  
  
  Goal(float w, float h, float d) {
    gw = w;
    gh = h;
    gd = d;
  }
  
  public void addGoal(float x, float y, float z) {
    pos = new PVector(x,y,z);
    pushMatrix();
    translate(pos.x,pos.y-gh/2,pos.z-gd/2);
    stroke(255);
    strokeWeight(2);
    noFill();
    //fill(255,0);
    box(gw,gh,gd);
    popMatrix();
  }
  
  
}
class Plane {
  PVector pos;
  float pd;
  float ph;
  float pw;
  //float gx,gy,gz;
  
  
  Plane(float w, float h, float d) {
    pw = w;
    ph = h;
    pd = d;
  }
  
  public void add(float x, float y, float z) {
    pos = new PVector(x,y,z);
   }
  
  public void move(float stepsX, float stepsY){
      pos.x += stepsX;
      pos.x = constrain(pos.x,(goal1.pos.x-(goal1.gw/2))+(goalie.pw/2), (goal1.pos.x+(goal1.gw/2))-(goalie.pw/2));

      pos.y += stepsY;
      pos.y = constrain(pos.y,(goal1.pos.y-(goal1.gh))+(goalie.ph/2), (goal1.pos.y)-(goalie.ph/2));
}
  
  
  public void showPlane() {
    pushMatrix();
    translate(pos.x,pos.y,pos.z);
    //noStroke();
    
    box(pw,ph,pd);
    popMatrix();
  }
  
}
class Timer {

float Time;

Timer(float set) {
Time = set;
}

public float getTime() {
 return(Time); 
}

public void setTime(float set) {
 Time = set; 
}

public void countUp() {
 Time += 1/frameRate; 
}

public void countDown() {
 Time -= 1/frameRate; 
}

}
  public void settings() {  fullScreen(P3D, 1); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "FreeKick" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
