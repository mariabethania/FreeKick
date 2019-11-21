void collision() {

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
