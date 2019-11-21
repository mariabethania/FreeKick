class Cube {
  float x = width*0.5;
  float y = height*0.5;
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
  
  void display() {
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
