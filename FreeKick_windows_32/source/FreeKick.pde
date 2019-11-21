import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

import peasy.*;

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

void setup() {
  fullScreen(P3D, 1);
  //size(1000, 600, P3D);
  //frameRate(16);
  W = width;
  H = height;
  timer = new Timer(1);
  soccer = loadImage("soccerBall.jpg");
  c = new Cube(W, H, W);
  pxLimit = width*0.5;
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
  ball = new Ball(c.x, c.y+(c.h/2)-21, c.z+(c.d*0.5)-(((W/64)*4)+1), soccer);
  goal1 = new Goal(width*0.5, width*0.25, width*0.05);
  goal2 = new Goal(width*0.5, width*0.25, width*0.05);
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
  py = c.y+(c.h*0.5)-(p1.ph*0.5);
  pz = c.z+(c.d*0.5)+((p1.pd*0.5)-(ball.rad*3));//c.z+(c.d*0.5)+((p1.pd*0.5)+1);
  gX = c.x;
  gY = c.y+(c.h*0.5);
  gZ = c.z-(c.d*0.5);
  goalie.add(gX, gY, gZ);
  goalieX = width*0.5;
  goalieY = height*0.5;
  pyLimit = c.y+(c.h*0.5)-(p1.ph*0.99);//H-(p1.ph*0.25);
}

void draw() {
  background(0);
  translate(width/2, 0);
  //fill(255, 255, 0);
  //sphere(20);
  lights();
  //rotateY(radians(frameCount)*0.5);
  pointLight(250, 250, 250, 0, 0, height*0.5);
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
  rotateX(PI*1.5);
  noStroke();
  fill(0, 120, 0);
  rect(c.x-(c.w*0.5), c.y-(c.h*0.75), c.x+(c.w*0.5), c.y+(c.d));
  popMatrix();
  if (arrows[0]) {
    if (kickLimit < 120) {
      kickLimit += 2;
      pz += 2;
    }
  }
  if (arrows[1]) {
    if (pxLimit <= c.x+(c.w*0.5)-p1.pw) {
      pxLimit += 7;
      px -= 7;
    }
  } else if (arrows[2]) {
    if (pxLimit >= c.x-(c.w*0.5)+p1.pw) {
      pxLimit -= 7;
      px += 7;
    }
  }
  //print(pxLimit);
  if (arrows[3]) {
    if (pyLimit > H-(goal2.gh)+(p1.ph*0.5)) {//c.y+c.h*0.5-(p1.ph*0.2)
      pyLimit -= 5;
      py -= 5;
      //py = py*3;
    }
  } else if (arrows[4]) {
    if (pyLimit < c.y+(c.h*0.5)-(p1.ph*0.25)) {
      pyLimit += 5;
      py += 5;
      //py = py*3;
      //  text(pyLimit,width*0.5,height);
    }
  }
  if (arrows[9]) {
    if (pyLimit > H-(goal2.gh)+(p1.ph*0.5)) {//c.y+c.h*0.5-(p1.ph*0.2)
      pyLimit -= 1;
      py -= 1;
      //py = py*3;
    }
  } else if (arrows[10]) {
    if (pyLimit < c.y+(c.h*0.5)-(p1.ph*0.25)) {
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
    if (yRotate < 1.56) {
      yRotate += 0.01;
    }
  } else if (arrows[8]) {
    if (yRotate > 0.01) {
    yRotate -= 0.01;
    }
  }
pushMatrix();
rotateY(-yRotate*1.1);
translate(-yRotate*510,0,-yRotate*480);
  textAlign(CENTER);
  textSize(64);
  fill(255, 0, 0);
  text(scoreAI, width*0.25, height*0.35);
  fill(0, 0, 255);
  text(score, width*0.75, height*0.35);
  popMatrix();
  //text(round(px), width*0.5, height*0.25);
  p1.add(px, py, pz);
  p2.add(px, py, pz);
  line1.add(px-(p1.pw*0.5), c.y+c.h*0.5, c.z);
  line2.add(px+(p1.pw*0.5), c.y+c.h*0.5, c.z);
  line3.add(c.x, c.y+c.h*0.5, pz-p1.pd*0.5);
  line4.add(c.x, c.y+c.h*0.5, pz+p1.pd*0.5);
  line5.add(c.x+c.w*0.5, py+(p1.ph*0.5), c.z);
  line6.add(c.x+c.w*0.5, py-(p1.ph*0.5), c.z);
  line7.add(c.x-c.w*0.5, py+(p1.ph*0.5), c.z);
  line8.add(c.x-c.w*0.5, py-(p1.ph*0.5), c.z);
  pushMatrix();
  translate(0, 0, width*0.5);
  noFill();
  stroke(255.100);
  //rect(width*0.235,height*0.592,15,185);
  //rect(width*0.235,height*0.9,15,60);
  rect(width*0.75, height*0.8, 15, 120);
  noStroke();
  fill((pyLimit*3)+33, 0, -(pyLimit*3)+70);
  //println((pyLimit*2)+120);
  //rect(width*0.235,height*0.9,15,pyLimit*2.4);
  noStroke();
  fill(kickLimit*2+15, 0, 255-(kickLimit*2));
  //println(kickLimit*2);
  rect(width*0.75, height*0.8, 15, kickLimit);
  popMatrix();
  p1.showPlane();
  stroke(255,50);
  strokeWeight(0.5);
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
  goal1.addGoal(c.x, c.y+(c.h*0.5), c.z-(c.d*0.5));
  //stroke(255,50);
  goal2.addGoal(c.x, c.y+(c.h*0.5), c.z+(c.d*0.5)+goal2.gd);

  noStroke();
  goalie.move(moveGoalieX(), moveGoalieY());
  fill(255, goalieGreen, 0);
  goalieGreen -= 10;
  goalie.showPlane();
  ball.edges();
  ball.update();
  ball.display();
}

void keyPressed() {
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

void keyReleased() {
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

void mouseDragged() {
  pz = mouseY;//map(mouseX,0,displayWidth,-(displayWidth)-(p1.pd*2),displayWidth)+p1.pd;
  px = mouseX;//map(mouseY,0,displayHeight,displayWidth*2,0);
}

float moveGoalieX() {
  //println("GOALIE"+goalie.pos.x);
  //println("BALL"+ball.pos.x);

  if (goalie.pos.x+goalie.pd*0.5 < ball.pos.x-ball.rad) {
    if (abs(ball.vel.x) < 3) {
      goalieX = 2.5;
    } else {
      goalieX = 9;
    }
    //gX += 1;
  } else if (goalie.pos.x-goalie.pd*0.5 > ball.pos.x+ball.rad) { // (error*level)-constant  -  (error*level)+constant
    if (abs(ball.vel.z) < 3) {
      goalieX = -2.5;
    } else {
      goalieX = -9;
    }
    //gX -= 1;
  }
  return goalieX;
}

float moveGoalieY() {
  if (goalie.pos.y+goalie.ph*0.5 < ball.pos.y-ball.rad) {
    if (abs(ball.vel.z) < 3) {
      goalieY = 3;
    } else {
      goalieY = 9;
    }
    //gY += 1;
  } else if (goalie.pos.y-goalie.ph*0.5 > ball.pos.y+ball.rad) { // (error*level)-constant  -  (error*level)+constant
    if (abs(ball.vel.y) < 3) {
      goalieY = -3;
    } else {
      goalieY = -9;
    }
    //gY -= 1;
  }
  return goalieY;
}
