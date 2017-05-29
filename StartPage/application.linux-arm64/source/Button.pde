abstract class Button {
  float x;
  float y;
  float size;
  String code;
  Button() {
    askx(0);
    asky(0);
    askCode("0");
    askSize(10);   
  }
  
  abstract void display();


  float getx() {
    return x;
  }
  Button askx(float tempx) {
    x = tempx;
    return this;
  }

  float gety() {
    return y;
  }
  Button asky(float tempy) {
    y = tempy;
    return this;
  }

  float getSize() {
    return size;
  }
  Button askSize(float tempSize) {
    size = tempSize;
    
    return this;
  }

  String code() {

    return code;
  }
  Button askCode(String tempCode) {
    code = tempCode;
    return this;
  }
}