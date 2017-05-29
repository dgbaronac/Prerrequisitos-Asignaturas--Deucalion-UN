class Circles extends Button{
  
  Integer[] index;
  HashMap<Integer,PVector> positions;
  Boolean mouseover;
  
  

  Circles(){
    
  
  }
  Circles(float tempx, float tempy, float tempsize, int[] templist){
  askx(tempx);
  asky(tempy);
  askSize(tempsize);
  declarateLists();
  Integer[] tempIndex = new Integer[templist.length];
  for(int i = 0; i<templist.length;i++){
    tempIndex[i] = templist[i];
  }
  askIndex(tempIndex);
  }
  
    void declarateLists() {
        positions = new HashMap<Integer,PVector>();
  }
void over(Integer s,float px, float py){
  
  if(positions.containsKey(s)){
    float distance = dist(px,py,positions.get(s).x,positions.get(s).y);
  if(distance < size/8) mouseover = true;
   
    
  }
  
 mouseover = false;


}



  
void display(){
  
      ellipseMode(CORNER);
      if (index.length == 0) return;
      for(int i = 0; i<index.length;i++){
      colorMode(HSB);
      fill(150,150,(i+1)*255/index.length);
      colorMode(RGB);
       ellipse(x + i*size/4,y+size*5/6,size/4,size/4);
       PVector pos = new PVector( x +i*size/4 +size/8 , y+size*5/6 + size/8);
       positions.put(index[i],pos);
       fill(0);
       text(index[i],x+size/8 + i*size/4,y +size*0.96);
    
    }
  


}

Integer[] index(){
 return index;
}

void askIndex(Integer[] tempIndex){
index = tempIndex;
}



}