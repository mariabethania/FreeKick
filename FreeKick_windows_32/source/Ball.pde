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

  void kickKey(float velZ) {
    if (ball.pos.z+(ball.rad) >= p1.pos.z-(p1.pd/2)
      && ball.pos.x+ball.rad >= p1.pos.x-p1.pw/2 
      && ball.pos.x-ball.rad <= p1.pos.x+p1.pw/2 
      && ball.pos.y+ball.rad >= p1.pos.y-p1.ph/2 
      && ball.pos.y-ball.rad <= p1.pos.y+p1.ph/2) 
    {
      vel.z = -1*abs(velZ*0.2);//-1*abs((pmouseY - mouseY)/8);
//println(vel.z);
      acc.z = 0.02;
      //acc.y = -1*abs(velZ/12);//-1*abs((pmouseY - mouseY)/8);
      acc.y = ((pos.y - p1.pos.y)+(velZ*0.2))*0.5;
      grv.y = 0.4;
      //kickSound.trigger();
      vel.x = (pos.x - p1.pos.x)*0.5;
      kickSound.setGain(map(abs(velZ), 0, 120, -30, 0));
      kickSound.trigger();
      acc.x = 0.02;
      vol = 0;
      yVel = 0.2;
      //acc.y = 0.035;
      //acc.y = ((pos.y - p1.pos.y));//+(velZ*0.5))*0.33;
    }
  }
  
  void kickMouse(float velZ) {
    //println(velZ);
    num++;
    kickSound.setGain(map(abs(velZ), 0, 60, -20, 10));
    kickSound.trigger();
    //vel.y = 0.2;
    vel.z = velZ/4;
    vel.x = (pos.x - p1.pos.x)/2;
    acc.y = ((pos.y - p1.pos.y)/2)+(velZ/10);
    acc.x = 0.02;
    vol = 0;
    yVel = 0.2;
    //passing de parameter velZ with the speed of Z axis
    //vel.z = -1*abs((pmouseY - mouseY)/4);
    acc.z = 0.02;
    //acc.y = -1*abs((pmouseY - mouseY)/4);
    grv.y = 0.4;
  }
  

  void edges() {
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
      acc.y *= -0.75;//= -1 * (abs(acc.y*0.65));
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
      vel.x *= -0.99;//(abs(vel.x));
      acc.x *= -0.9;//*(abs(acc.x));
      bounce3.setGain(vol);
      bounce3.trigger();
      if (turnZ) {turnZ = false;} else {turnZ = true;}
    } else if (pos.x+rad >= c.x+(c.w/2)) {
      if (turnZ) {turnZ = false;} else {turnZ = true;}
      pos.x = c.x+(c.w/2)-rad;
      vel.x *= -0.99;//*(abs(vel.x));
      acc.x *= -0.9;//(abs(acc.x));
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
        vel.z *= 0.99;
        acc.z *= 0.9;
      } else if (trueGoal == false)
      {
      if (turnX) {turnX = false;} else {turnX = true;}
        pos.z = c.z-(c.d/2)+rad;
        bounce6.setGain(vol);
        bounce6.trigger();
        vel.z *= -0.99;//abs(vel.z)*0.7;
        acc.z *= -0.9;//-0.7;
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
      vel.z *= -0.99;//abs(vel.z);
      acc.z *= -0.9;
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
        vel.z *= 0.99;
        acc.z *= 0.9;
      } else if (trueGoal == false)
      {
        bounce3.setGain(vol);
        bounce3.trigger();
        vel.z *= -0.99;//-1*abs(vel.z)*0.7;
        acc.z *= -0.9;
      }
    } 

    collision();
  }

  void update() {
//if (turnZ) {
//    zRotate -= abs(vel.x*0.04);
//} else {zRotate += abs(vel.x*0.04);}

//    if (turnX) {
//    xRotate += abs(vel.z*0.04);
//} else {xRotate -= abs(vel.z*0.04);}
    zRotate += (vel.x*0.03);//-(vel.z*0.01);;
    xRotate -= (vel.z*0.03);//-(vel.x*0.01);
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

    acc.z *= 0.99;
    vel.z *= 0.995;
    acc.x *= 0.995;
    vel.x *= 0.995;
    acc.y *= 0.995;
    vel.y *= 0.995;
    //grv.y *= 0.999;
}

  void display() {
    vol -= 0.07;
    //vol = (abs(vel.z)-35);
    pushMatrix();
    translate(pos.x, pos.y+5, pos.z);
    rotateZ(zRotate);
    rotateX(xRotate);
    shape(bola);
    //sphere(rad);
    popMatrix();
  }

  void reset() {
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
    ball.pos.z = c.z+(c.d*0.5)-(rad*4)-1;  
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
