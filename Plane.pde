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
  
  void add(float x, float y, float z) {
    pos = new PVector(x,y,z);
   }
  
  void move(float stepsX, float stepsY){
      pos.x += stepsX;
      pos.x = constrain(pos.x,(goal1.pos.x-(goal1.gw/2))+(goalie.pw/2), (goal1.pos.x+(goal1.gw/2))-(goalie.pw/2));

      pos.y += stepsY;
      pos.y = constrain(pos.y,(goal1.pos.y-(goal1.gh))+(goalie.ph/2), (goal1.pos.y)-(goalie.ph/2));
}
  
  
  void showPlane() {
    pushMatrix();
    translate(pos.x,pos.y,pos.z);
    //noStroke();
    box(pw,ph,pd);
    popMatrix();
  }
  
}
