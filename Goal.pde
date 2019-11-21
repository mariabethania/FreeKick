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
  
  void addGoal(float x, float y, float z) {
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
