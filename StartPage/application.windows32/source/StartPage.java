import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Map; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class StartPage extends PApplet {




ControlP5 cp5;

Assignature test;
Career testing;

HashMap<String, Assignature> assignatures; // TODAS LAS MATERIAS(Generadas a partir de DATA)   key: code
HashMap<String, Career> careers;  // Todas las carreras    key : code

JSONArray assignaturesjson; //Datos de materias.
JSONArray careersjson; //Datos de Carreras.
StringList careerKeys; //Nombres de archivos.

//Color para los botones cuando estan activos, inactivos o seleccionados y la barra de menu
int colorInactive;
int colorActive;
int colorSelected;
int barMenu;

PFont menuFont;//Fuente para el menu
PFont f;
public void setup() {
  background(0);

  
  //size(1280,900);
  cp5 = new ControlP5(this); 

  //inicializacion color botones y barra de menu
  colorInactive= color(150, 150, 150);
  colorActive= color(50, 50, 100);
  colorSelected= color(100, 100, 200);
  barMenu=color(160, 160, 160);

  f = createFont("L", 16, true);
  menuFont = createFont("Calibri", 12);

  assignatures = new HashMap<String, Assignature>();
  careers = new HashMap<String, Career>();  

  loadData();

  StringList tempList = new StringList();
  for (Map.Entry<String, Career> m : careers.entrySet()) {
    tempList.append(m.getValue().getName());
  }



  testing = careers.get("2879");



  //Menu Lista desplegable del plan de estudios  
  /*cp5.addDropdownList("Plan de estudios") 
   .setOpen(false)
   .setVisible(false)
   .setPosition(5, 5)                                   
   .setWidth(320)//Largo lista desplegable
   .setHeight(200)//Tama\u00f1o lista desplegable                  
   .setBarHeight(40)//alto barra principal
   .setItemHeight(25)//alto barra items
   .addItems(tempList.array()) //AQUI VA LA LISTA CON EL NOMBRE DE LAS CARRERAS                 
   .setColorBackground(colorInactive)                  
   .setColorForeground(colorSelected)
   .setColorActive(colorActive)
   .setColorLabel(0) 
   .setColorValue(color(50, 50, 50))
   .setFont(createFont("arial", 14))
   ;    
   
   cp5.addBang("Materias")//a\u00f1ade un boton para visualizar la lista de materias
   .setPosition(330, 5)
   .setVisible(false)
   .setSize(80, 40)
   .setColorForeground(colorInactive)
   .setColorActive(colorSelected)
   .setColorCaptionLabel(0) 
   .setFont(createFont("arial", 14))         
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
   ;  
   
   cp5.addBang("Malla")//a\u00f1ade un boton para visualizar la malla curricular
   .setPosition(415, 5)
   .setVisible(false)
   .setSize(80, 40)
   .setColorForeground(colorInactive)
   .setColorActive(colorSelected)
   .setColorCaptionLabel(0) 
   .setFont(createFont("arial", 14))         
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
   ;  */



  println();

 
  println("Nombre del programa: " + testing.getName());

  println("C\u00f3digo: " + testing.getCode());
    println();
    println("Componentes: (" + testing.getComp().length+")");
    println();
  printArray( testing.getComp());
println();
  println("Agrupaciones: ("+testing.getAgrupations().length+ ")");
  println();
  printArray( testing.getAgrupations());
println();

  println("Materias: (" +   testing.getAssignatures().length + ")");
  println();
    for (Map.Entry<String, Assignature> m : assignatures.entrySet()) {
    println(m.getValue().getName() + " " + m.getKey());
  }
  println();
  


  testing.createDict();
  for (int i = 0; i<testing.related("2025960").size(); i++) {
    testing.getAssignature(testing.related("2025960").get(i)).askOpac(100);
  }
  testing.getAssignaturesLIST().sort();

  //esta funcion ha sido implementada en displayMalla()  
  ////////////////////////////////////////////  
  /* 
   for(String s: testing.getAssignatures()){ 
   testing.getAssignature(s).display(190*x++,180*y);
   if( 190*x> width -190){
   x = 0;
   y++;
   }
   }
   */

  testing.display();
  for (String s : testing.getAssignaturesLIST()) {
    cp5.addBang(s)
      .setPosition(testing.positions.get(s).x -PApplet.parseInt(width/100), testing.positions.get(s).y)
      .setSize(PApplet.parseInt(width/100), (int)(width/105))
      .setId(testing.index.get(s))
      .setColorActive(color(200)) 
      .setColorForeground(color(50)) 
      .setLabelVisible(false)
      ;
  }
  /////////////////////////////////////////
}


public void loadData() {

  assignaturesjson = loadJSONArray("data/assignatures.json");
  careerKeys = new StringList();
  careerKeys.append(loadJSONArray("data/careers/index.json").getStringArray());
  careersjson = new JSONArray();

  for (int i = 0; i< careerKeys.size(); i++) {    
    JSONObject programa; 
    String newRoute = "data/careers/"+ careerKeys.get(i) + ".json" ;
    programa = loadJSONObject(newRoute);
    careersjson.append(programa);
  }

  for (int i = 0; i<assignaturesjson.size(); i++) { //HashMap de materias
    JSONObject temp = assignaturesjson.getJSONObject(i);    
    assignatures.put(temp.getString("code"), new Assignature(temp));
  }  


  for (int i = 0; i<careersjson.size(); i++) { //HashMap de carreras
    JSONObject temp = careersjson.getJSONObject(i);    
    careers.put(temp.getString("code"), new Career(temp));
  }




  for (Map.Entry<String, Career> m : careers.entrySet()) {
    String[] a = m.getValue().getAssignatures();
    StringList as = new StringList(m.getValue().getAgrupations());
    int colorsize = m.getValue().getAgrupations().length;
    int[] colors = new int[colorsize];

    for (int i = 0; i< colors.length; i++) {
      colorMode(HSB);
      colors[i] = color(255*i/colors.length, 200, 200);
      colorMode(RGB);
    }
    for (int i = 0; i< a.length; i++) {
      if ( assignatures.containsKey(a[i])) {
        Assignature temp = assignatures.get(a[i]);
        m.getValue().getAssignature(a[i]).askName(temp.getName());
        m.getValue().getAssignature(a[i]).askCredit(temp.getCredits());
        m.getValue().getAssignature(a[i]).askHour(temp.getHour());
        m.getValue().getAssignature(a[i]).loadRequirements(temp.requirementsArray());             
        m.getValue().getAssignature(a[i]).askColor(colors[as.index(m.getValue().getAssignature(a[i]).getAgr())]);
      }
    }
    careers.put(m.getValue().getCode(), m.getValue());
  }
}


public void draw() {

  displayMalla();//Corregir para que las materias se empiezen a dibujar en y=50
  //Se puede observar como se superponen con la barra de menu
  //fill(barMenu);

}

/*void controlEvent(ControlEvent theEvent){//Para controlar lo que hacen los botones del menu
 if(theEvent.isController()) {
 
 print("control event from : "+theEvent.controller().name());
 println(", value : "+theEvent.controller().value());
 
 if(theEvent.controller().name()=="Malla") {//para el boton Malla
 displayMalla();
 }
 }
 }*/

public void displayMalla() {//Funcion que dibuja una malla
  //Valores para recorrer el for que dibuja las materias

  testing.display();
  colored();

  //int x = 0;
  //int y = 0;
  /*for(String s: testing.getAssignatures()){ 
   testing.getAssignature(s).display(190*x++,180*y);
   if( 190*x> width -190){
   x = 0;
   y++;
   }
   }*/
}


public void controlEvent(ControlEvent theEvent) {
  for (String s : testing.getAssignaturesLIST()) {
    if (theEvent.getController().getName().equals(s)) {
      if (testing.getCurrent().equals(s) == false) testing.askCurrent(s); 
      else testing.askCurrent("0000000");
    }
  }
}


public void colored() {

  testing.askColor((testing.getColor()+1)%255);
}
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
  
  public abstract void display();


  public float getx() {
    return x;
  }
  public Button askx(float tempx) {
    x = tempx;
    return this;
  }

  public float gety() {
    return y;
  }
  public Button asky(float tempy) {
    y = tempy;
    return this;
  }

  public float getSize() {
    return size;
  }
  public Button askSize(float tempSize) {
    size = tempSize;
    
    return this;
  }

  public String code() {

    return code;
  }
  public Button askCode(String tempCode) {
    code = tempCode;
    return this;
  }
}
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
  
    public void declarateLists() {
        positions = new HashMap<Integer,PVector>();
  }
public void over(Integer s,float px, float py){
  
  if(positions.containsKey(s)){
    float distance = dist(px,py,positions.get(s).x,positions.get(s).y);
  if(distance < size/8) mouseover = true;
   
    
  }
  
 mouseover = false;


}



  
public void display(){
  
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
       text(index[i],x+size/8 + i*size/4,y +size*0.96f);
    
    }
  


}

public Integer[] index(){
 return index;
}

public void askIndex(Integer[] tempIndex){
index = tempIndex;
}



}
class Assignature {
  String name;
  String code;
  int cred;
  int hour; 
  int opac;
  StringList prerequisites;
  String agr;
  int agrColor;
  String comp;
  Boolean aproved;  
  Integer timesStudied;
  
  
  //Dado por el programa

  Assignature() {
    askName("N/A");
    askCode("0000000");
    askCredit(0);
    askHour(0);
    askAgr("N/A");
    askComp("");
    askOpac(255);
    askColor(200);
    initRequire();
  }

  Assignature(JSONObject temp) {
    //temp.getString("name"),temp.getString("code"),temp.getInt("cred"),temp.getInt("hourA"),temp.getInt("hourB"),temp.getJSONArray("need").getStringArray())
    askName(temp.getString("name"));
    askCode(temp.getString("code"));
    askCredit(temp.getInt("cred"));
    askHour(temp.getInt("hour"));   
    askAgr("");
    askComp("");
    askColor(0);
    askOpac(255);
    initRequire();    
    addRequirement(temp.getJSONArray("need").getStringArray());
  }


  //Nombre
  public void askName(String tempName) {
    name = tempName;
  }
  public String getName() {
    return name;
  }




  //C\u00f3digo
  public void askCode(String tempCode) {
    code = tempCode;
  }
  public String getCode() {
    return code;
  } 




  //Agrupaci\u00f3n.
  public void askAgr(String tempAgr) {
    agr = tempAgr;
  }
  public String getAgr() {
    return agr;
  }



  //Componente.

  public void askComp(String tempComp) {
    comp = tempComp;
  }
  public String getComp() {
    return comp;
  }



  //Cr\u00e9ditos
  public void askCredit(Integer tempCred) {
    cred = tempCred;
  }
  public Integer getCredits() {
    return cred;
  }

  public void askOpac(Integer tempOpac) {
    opac = tempOpac;
  }
  public Integer getOpac() {
    return opac;
  }


  //Horas
  
  public void askHour(Integer tempHour) {
    hour = tempHour;
  }
  public Integer getHour() {
    return hour;
  }


  //Color de agrupaci\u00f3n
  public void askColor(Integer tempColor) {
    agrColor = tempColor;
  }
  public Integer getColor() {
    return agrColor;
  }






  public JSONObject getJSONObject() {
    JSONObject materia = new JSONObject();
    materia.setString("code", code);
    materia.setString("name", name);
    materia.setInt("cred", cred);
    materia.setJSONArray("need", new JSONArray(prerequisites));      
    return materia;
  }



  public Boolean needsMe(Assignature m){
    
    StringList pre = m.requirementsArray();
    
    if(pre.hasValue(code)) return true;  
    else return false; 
 
  }
  
  
  
  public Boolean needsIt(Assignature m){
    if(prerequisites.hasValue(m.getCode())) return true;  
    else return false;
  }
  
  




  //Pre-Requisitos

  public Boolean hasRequirements() {
    if (prerequisites.size() == 0) return false;
    else return true;
  }

  public void loadRequirements(StringList req) {
    prerequisites = req;
  }
  public void initRequire() {
    prerequisites = new StringList();
  }

  public StringList requirementsArray() {
    StringList s = new StringList();
    s.append(prerequisites);
    return s;
  }

  public void deleteRequirement(String code) {
    prerequisites.removeValue(code);
  }

  public void addRequirement(String code) {
    prerequisites.append(code);
  }  
  public void addRequirement(String[] code) {
    prerequisites.append(code);
  }  
  public void addRequirement(Assignature assignature) {
    if (prerequisites.hasValue(assignature.getCode())== false)
      prerequisites.append(assignature.getCode());
  }

  


  public void display(float x, float y,float size) {


   
    float sizeb = size*5/6;
    // Depende del tama\u00f1o
    textAlign(CENTER, CENTER);
    textFont(f, size*3/40);
    noStroke();
    fill(0,opac);
    rect(x, y, size, sizeb);

    //Cr\u00e9ditos

    rect(x, y, size/3, sizeb/7);
    fill(255,opac);
    text(cred, x+size/6, y+sizeb/14);

    //Horas Presenciales
    fill(200,opac);
    rect(x+size/3, y, size/3, sizeb/7);
    fill(0,opac);
    text(hour, x+size/6 + size/3, y+sizeb/14);



    //Espacio para bot\u00f3n

    fill(225,opac);
    rect(x+2*size/3, y, size/3, sizeb/7); 
    fill(0,opac);



    // Nombre
    textFont(f, size*3/45);
    fill(255,opac);
    rect(x, y+sizeb/7, size, 3*sizeb/7);
    fill(0,opac);
    //Funci\u00f3n para salto de linea TODO
    printName(name, x, y, size, sizeb);



    // Agrupaci\u00f3n
    textFont(f, size*3/50);
    fill(agrColor,opac);//TEMPORAL: OFRECIDO POR LA AGRUPACI\u00d3N

    rect(x, y+4*sizeb/7, size, 2*sizeb/7);
    fill(0,opac);
    printAgr(agr, x, y, size, sizeb);




    //C\u00f3digo
    textFont(f, size*3/40);
    fill(20,opac);
    rect(x, y+sizeb*6/7, size*3/4, sizeb/7);
    fill(255,opac);
    text(code, x + size/3, y + sizeb*13/14);


    //Componente
     colorMode(RGB);
    if (this.getComp() .equals( "B")) fill(255, 200, 0,opac);
    else if (this.getComp().equals( "C")) fill(0, 120, 0,opac);
          else if (this.getComp() .equals( "P")) fill(100, 0, 130,opac);
                else if (this.getComp().equals( "L")) fill(180, 240, 240,opac);
                      else fill(255,opac);    
    rect(x + size*3/4, y+sizeb*6/7, size/4, sizeb/7);
    fill(0,opac);
    text(comp, x + size*3/4 +size/8, y+sizeb*6/7 + size/20);
  }

  public void printName(String s, float x, float y, float size, float sizeb) {

    if (s.length()>87) s = s.substring(0, 84) + "...";  




    StringList words = new StringList();
    StringList wordsInLine = new StringList();


    words.append(split(s, " "));
    int suma = 0;
    String tempLine ="";
    for (int i = 0; i< words.size(); i++) {
      suma += words.get(i).length();
      tempLine += words.get(i) + " ";

      if (suma + i>17 || i == words.size() - 1) {
        wordsInLine.append(tempLine.substring(0, tempLine.length()-1));
        suma = 0;
        tempLine = "";
      }
    }
    int lines = wordsInLine.size();
    if (lines == 1) {
      text(name, x+ size/2, y+ 5*sizeb/14);
    } else if (lines == 2) {   
      text(wordsInLine.get(0), x+ size/2, y+ 2*sizeb/7);
      text(wordsInLine.get(1), x+ size/2, y+ 3*sizeb/7);
    } else if ( lines == 3) {
      text(wordsInLine.get(0), x+ size/2, y+ 3*sizeb/14);
      text(wordsInLine.get(1), x+ size/2, y+ 5*sizeb/14); 
      text(wordsInLine.get(2), x+ size/2, y+ 7*sizeb/14);
    }
  }

  public void printAgr(String s, float x, float y, float size, float sizeb) {

    if (s.length()>87) s = s.substring(0, 84) + "...";  




    StringList words = new StringList();
    StringList wordsInLine = new StringList();


    words.append(split(s, " "));
    int suma = 0;
    String tempLine ="";
    for (int i = 0; i< words.size(); i++) {
      suma += words.get(i).length();
      tempLine += words.get(i) + " ";

      if (suma + i>25 || i == words.size() - 1) {
        wordsInLine.append(tempLine.substring(0, tempLine.length()-1));
        suma = 0;
        tempLine = "";
      }
    }
    int lines = wordsInLine.size();

    if (lines == 1) {
      text(s, x+ size/2, y+ 5*sizeb/7);
    } else if (lines == 2) {   
      text(wordsInLine.get(0), x+ size/2, y+ 9*sizeb/14);
      text(wordsInLine.get(1), x+ size/2, y+ 10*sizeb/14);
    } else if ( lines == 3) {
      text(wordsInLine.get(0), x+ size/2, y+ 13*sizeb/21);
      text(wordsInLine.get(1), x+ size/2, y+ 5*sizeb/7); 
      text(wordsInLine.get(2), x+ size/2, y+ 17*sizeb/21);
    }
  }
}
class Career {

  String code;
  String name;
  String current;
  int cred;
  int credFUND;
  int credDISC;
  int credLIBR;
  int semTotal;
  int displayColor;
  IntDict semestres;
  HashMap<Integer, String> sem;
  HashMap<String, Integer> index;
  HashMap<String, PVector> positions;



  StringList agrList;
  StringList compList;
  StringList assignatureList;
  StringList colors;
  JSONArray hierarchy;

  HashMap<String, Assignature> localAssignatures;


  Career() {
    askName("");
    askCode("");
    askCredits(0);
    askCreditsFUND(0);
    askCreditsDISC(0);
    askCreditsLIBR(0);
    askColor(100);
    declarateLists();
    askSem(10);
  }


  Career(JSONObject plan) {
    declarateLists();
    askName(plan.getString("name"));
    askCode(plan.getString("code"));
    askCredits(plan.getJSONObject("creditos").getInt("total"));
    askCreditsFUND(plan.getJSONObject("creditos").getInt("credFUND"));
    askCreditsDISC(plan.getJSONObject("creditos").getInt("credDISC"));
    askCreditsLIBR(plan.getJSONObject("creditos").getInt("credLIBR"));
    createHierarchy(plan.getJSONArray("components"));
    askCurrent("0000000");
    askColor(100);
    askSem(10);
  }

  public HashMap<String, Assignature> localAssignatures() {

    return localAssignatures;
  }

  public JSONObject getJSONObject() {

    JSONObject plan = new JSONObject();
    plan.setString("code", code);
    plan.setString("name", name);
    JSONObject creditos = new JSONObject();
    creditos.setInt("total", cred);
    creditos.setInt("credFUND", credFUND) ;
    creditos.setInt("credDISC", credDISC);
    creditos.setInt("credLIBR", credLIBR); 
    plan.setJSONObject("creditos", creditos);
    plan.setJSONArray("components", hierarchy);



    return plan;
  }

  public void askColors(String[] s) {
    colors.append(s);
  }


  public void declarateLists() {
    agrList = new StringList();
    compList = new StringList();    
    assignatureList = new StringList();
    hierarchy = new JSONArray();
    colors = new StringList();
    localAssignatures = new HashMap<String, Assignature>();
    index = new HashMap<String, Integer>();
    semestres = new IntDict();
    positions = new HashMap<String, PVector>();
  }


  //C\u00f3digo SIA de 4 cifras
  public void askCode(String tempCode) {
    code = tempCode;
  }  
  public String getCode() {
    return code;
  } 

  //Semestres

  public void askSem(int tempSem) {
    semTotal = tempSem;
  }
  public int getSem() {
    return semTotal;
  }

  public void growSem() {
    semTotal++;
  }

  public void decreaseSem() {
    semTotal++;
  }
 //Color del fondo
   public void askColor(int tempColor) {
    displayColor = tempColor;
  }
  public int getColor() {
    return displayColor;
  }

  //Nombre
  public void askName(String tempName) {
    name = tempName;
  }
  public String getName() {
    return name;
  }
  //
  public void askCurrent(String tempCurrent) {
    current = tempCurrent;
  }  
  public String getCurrent() {
    return current;
  } 
  //Creditos
  public void askCredits(int tempCode) {
    cred = tempCode;
  }
  public int getCredits() {
    return cred;
  }
  // Cr\u00e9ditos de fundamentaci\u00f3n.
  public void askCreditsFUND(int tempCode) {
    credFUND = tempCode;
  }
  public int getCreditsFUND() {
    return credFUND;
  }
  //Cr\u00e9ditos de disciplinar.
  public void askCreditsDISC(int tempCode) {
    credDISC = tempCode;
  }
  public int getCreditsDISC() {
    return credDISC;
  }
  //Cr\u00e9ditos de libre elecci\u00f3n.
  public void askCreditsLIBR(int tempCode) {
    credLIBR = tempCode;
  }
  public int getCreditsLIBR() {
    return credLIBR;
  }

  //Lista de c\u00f3digos de materias.
  public String[] getAssignatures() {
    String[] lista = assignatureList.array();
    return lista;
  }
  public StringList getAssignaturesLIST() {
    StringList lista = assignatureList;
    return lista;
  }

  //Lista de nombres de agrupaciones
  public String[] getAgrupations() {
    String[] lista = agrList.array();
    return lista;
  }

  public String[] getComp() {
    String[] lista = compList.array();
    return lista;
  }

  public Assignature getAssignature(String cod) {


    if (localAssignatures.containsKey(cod))return localAssignatures.get(cod);
    else return new Assignature();
  }

  public HashMap<String, PVector> positions() {

    return positions;
  }
   public HashMap<String, Integer> index() {

    return index;
  }

  public void createHierarchy(JSONArray components) {
    hierarchy = components;
    //if(components.size() > 0){
    for (int i = 0; i<components.size(); i++) {
      String compCode = components.getJSONObject(i).getString("code");
      compList.append(compCode);
      JSONArray agrupations = components.getJSONObject(i).getJSONArray("agrupation");
      for (int o = 0; o<agrupations.size(); o++) {
        String agrName = agrupations.getJSONObject(o).getString("name");
        agrList.append(agrName);
        String[] assignatures = agrupations.getJSONObject(o).getJSONArray("materias").getStringArray();

        for ( int u = 0; u<assignatures.length; u++) {
          Assignature temp = new Assignature();
          temp.askComp(compCode);
          temp.askAgr(agrName);
          temp.askCode(assignatures[u]);
          assignatureList.append(temp.getCode());         
          localAssignatures.put(assignatures[u], temp);
        }
      }
    }
  }


  public int credAgr(String s) {
    int a = 0;
    for (Map.Entry<String, Assignature> m : localAssignatures.entrySet()) {
      if (m.getValue().getAgr().equals(s)) a += m.getValue().getCredits();
    }
    return a;
  }



  public void createDict() {
    IntDict tempsemestres = new IntDict();

    for (Map.Entry<String, Assignature> map : localAssignatures.entrySet()) {
      tempsemestres.set(map.getValue().getCode(), 1+preline(map.getValue().getCode()));
    }
    tempsemestres.sortValues();

    IntDict temp = new IntDict();
    for ( Integer i = 1; i<=semTotal; i++) {
      temp.set(i.toString(), 0);
    }

    for (String s : tempsemestres.keys()) {
      Integer rec = 1+ premax(s, semestres);

      while (true) {
        if (temp.get(rec.toString()) >= 14) {
          rec++;
        } else {
          for (int i = 0; i< localAssignatures.get(s).getCredits(); i++) {
            temp.increment(rec.toString());
          }
          break;
        }
      }
      semestres.set(s, rec);
    }





    semestres.sortValues();
    Integer x = 1;
    for (String s : semestres.keys()) {
      index.put(s, x++);
    }
  }



  public void buttons(float x, float y, float size, String s) {


    colorMode(HSB);
    fill(170, 200, 50);
    colorMode(RGB);    
    rect(x + 2*size/3, y, size/3, 5*size/42);
    fill(255);    
    text(index.get(s), x + 5*size/6, y+size/21);


    String[] list = localAssignatures.get(s).requirementsArray().array();
    IntDict ord = new IntDict();



    for ( int i = 0; i< list.length; i++) {
      ord.set(list[i], index.get(list[i]));
    }


    ord.sortValues();
    if (list.length == 0) return;

    Circles boton = new Circles(x, y, size, ord.valueArray())   ;
    boton.display();
  }



  public int suma(int[] prev) {
    int suma = 0;
    for (int i = 0; i<prev.length; i++) {
      suma+=prev[i];
    }

    return suma;
  }
  public int premax(String s, IntDict dict) {
    if (localAssignatures.get(s).requirementsArray().array().length == 0) return 0;
    int x = 0;
    int tempx = 0;

    for (String st : localAssignatures.get(s).requirementsArray()) {
      tempx = dict.get(st);
      if (tempx> x) x = tempx;
    }
    return x;
  }


  public int preline(String s) {
    int x = 0;

    Assignature m = localAssignatures.get(s);

    String[] materias = m.requirementsArray().array();
    if (materias.length != 0) x++;

    if ( materias.length == 0 ) return 0; //return 0;

    StringList temp = new StringList();

    for (int i =  0; i< materias.length; i++) {
      if (localAssignatures.get(materias[i]).hasRequirements()) temp.append(localAssignatures.get(materias[i]).requirementsArray());
    }
    temp = new StringList(temp.getUnique());    






    return x + preline(temp.array());
  }



  public int preline(String[] s) {

    int x= 0;

    Assignature[] all = new Assignature[s.length];
    if (all.length == 0)return 0; 
    else x++;

    StringList pre = new StringList();

    for (int i = 0; i< all.length; i++) {
      all[i] = localAssignatures.get(s[i]);
      pre.append(all[i].requirementsArray().array());
    }







    if ( pre.size() != 0 ) x++; //return 0;

    StringList temp = new StringList();

    for (int i =  0; i< pre.size(); i++) {
      if (localAssignatures.get(pre.get(i)).hasRequirements()) temp.append(localAssignatures.get(pre.get(i)).requirementsArray());
    }

    temp = new StringList(temp.array());





    return x + preline(temp.array());
  }

  public StringList related(String m) {
    StringList temp = new StringList(pos(m));
    temp.append(m);
    temp.append(localAssignatures.get(m).requirementsArray());


    temp.append(related(localAssignatures.get(m).requirementsArray()));




    return new StringList(temp.getUnique());
  }

  public String[] related(StringList s) {

    StringList temp = new StringList();
    if (s.size() == 0) return temp.array();
    for (int i = 0; i<s.size(); i++) {
      temp.append(localAssignatures.get(s.get(i)).requirementsArray());
    }

    if (temp.size()== 0) return temp.array();

    temp.append(related(temp));


    return temp.getUnique();
  }

  public String[] pos(String s) {

    StringList templist = new StringList();
    for (Map.Entry<String, Assignature> map : localAssignatures.entrySet()) {
      if (localAssignatures.get(s).needsMe(map.getValue())) templist.append(map.getValue().getCode());
    }

    return templist.array();
  }





  public HashMap<String, Assignature> subLocal(String[] s) {
    HashMap<String, Assignature> m = new HashMap<String, Assignature>();
    for (int i = 0; i<s.length; i++) {
      m.put(localAssignatures.get(s[i]).getCode(), localAssignatures.get(s[i]));
    }
    return m;
  }
  
  
  
  
  
  
  
  

  public void display() {

    for (Map.Entry<String, Assignature> map : localAssignatures.entrySet()) {
      map.getValue().askOpac(255);
      if (localAssignatures.containsKey(current)) {
        if (!related(current).hasValue(map.getKey())) {

          map.getValue().askOpac(90);
        }
      }
    }
    Assignature example = new Assignature();
    example.askCode("C\u00f3digo");
    example.askName("NOMBRE DE LA ASIGNATURA");
    example.askAgr("NOMBRE DE LA AGRUPACI\u00d3N");

    HashMap<Integer, Integer> mporsem = new HashMap<Integer, Integer>();

    for (int i = 1; i<= semTotal; i++) {
      int x= 0;
      for (String s : semestres.keys()) {
        if (i == semestres.get(s)) x++;
      }
      mporsem.put(i, x);
    }
    colorMode(HSB);

    for (int i = 0; i<semTotal; i++) {
      noStroke();
      fill(getColor(), 100, 20+(semTotal -i)*15);

      rect(i*width/semTotal, 0, (i+1)*width/semTotal, height);
      stroke(0);
    }
    float tempSize = width/semTotal;


    float y = 0;

    for (String s : semestres.keys()) {
      PVector pos = new PVector(tempSize*0.1f + tempSize*(-1+semestres.get(localAssignatures.get(s).getCode())), height/14 + y*tempSize);
      positions.put(s,pos);
      localAssignatures.get(s).display( tempSize*0.1f + tempSize*(-1+semestres.get(localAssignatures.get(s).getCode())), height/14 + y*tempSize, tempSize*0.8f);
      buttons(tempSize*0.1f + tempSize*(-1+semestres.get(localAssignatures.get(s).getCode())), height/14 + y*tempSize, tempSize*0.8f, s);
      y++;
      if (y>= mporsem.get(semestres.get(localAssignatures.get(s).getCode()))  ) y = 0;
    }
    example.display(10, height-width/semTotal, 0.9f*width/semTotal);
    fill(0);
    rect(10, height-width/semTotal, 0.3f*width/semTotal, 20);
    fill(255);
    text("Cr\u00e9ditos", 10+ 0.15f*width/semTotal, 8+height-width/semTotal);
    fill(130);
    rect(10 + 0.3f*width/semTotal, height-width/semTotal, 0.3f*width/semTotal, 20);
    fill(0);
    text("Horas", 0.3f*width/semTotal+10+ 0.15f*width/semTotal, 8+height-width/semTotal);
    text("ID", 0.6f*width/semTotal+10+ 0.15f*width/semTotal, 8+height-width/semTotal);
      rect(0, 0, width, 50);//Barra del menu
      colorMode(HSB);
  fill(getColor(),100,100);
  colorMode(RGB);
  textSize(25);
  textFont(menuFont,30);
  text(getCode() + "    " +getName().toUpperCase() + "     " + "Malla est\u00e1ndar", width/2, 20);
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#FF0505", "StartPage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
