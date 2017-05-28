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
 
  

  cp5 = new ControlP5(this); 
  
  //inicializacion color botones y barra de menu
  colorInactive= color(150,150,150);
  colorActive= color(50,50,100);
  colorSelected= color(100,100,200);
  barMenu=color(160,160,160);

  f = createFont("Calibri", 16, true);
  menuFont = createFont("arial",14);
  
  assignatures = new HashMap<String, Assignature>();
  careers = new HashMap<String, Career>();  
  
  loadData();
  
  StringList tempList = new StringList();
  for(Map.Entry<String,Career> m: careers.entrySet()){
   tempList.append(m.getValue().getName());
  }
  printArray(tempList.array());
  
  
  testing = careers.get("2879");
  
  printArray(careerKeys.array());
  
  //Menu Lista desplegable del plan de estudios  
  cp5.addDropdownList("Plan de estudios") 
     .setOpen(false) 
     .setPosition(5,5)                                   
     .setWidth(320)//Largo lista desplegable
     .setHeight(200)//Tama\u00f1o lista desplegable                  
     .setBarHeight(40)//alto barra principal
     .setItemHeight(25)//alto barra items
     .addItems(tempList.array()) //AQUI VA LA LISTA CON EL NOMBRE DE LAS CARRERAS                 
     .setColorBackground(colorInactive)                  
     .setColorForeground(colorSelected)
     .setColorActive(colorActive)
     .setColorLabel(0) 
     .setColorValue(color(50,50,50))
     .setFont(createFont("arial",14))
     ;    
  
  cp5.addBang("Materias")//a\u00f1ade un boton para visualizar la lista de materias
     .setPosition(330,5)
     .setSize(80,40)
     .setColorForeground(colorInactive)
     .setColorActive(colorSelected)
     .setColorCaptionLabel(0) 
     .setFont(createFont("arial",14))         
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;  
  
  cp5.addBang("Malla")//a\u00f1ade un boton para visualizar la malla curricular
     .setPosition(415,5)
     .setSize(80,40)
     .setColorForeground(colorInactive)
     .setColorActive(colorSelected)
     .setColorCaptionLabel(0) 
     .setFont(createFont("arial",14))         
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;   
  
  println(careers.size());
  println(assignatures.size());

  for (Map.Entry<String, Assignature> m : assignatures.entrySet()) {
    println(m.getValue().getName() + " " + m.getKey());
  }
  println();
  for (Map.Entry<String, Career> m : careers.entrySet()) {
    println(m.getValue().getName() + " " + m.getKey());
  }
  println(careers.size());
  println("nombre: " + testing.getName());
  println("C\u00f3digo: " + testing.getCode());
  println(" n\u00b0 de materias " + testing.getAssignatures().length);
  println("Materias: ");
  printArray( testing.getAssignatures());
  println(" n\u00b0 de agrupaciones " + testing.getAgrupations().length);
  println("Agrupaciones: ");
  printArray( testing.getAgrupations());
  println(" n\u00b0 de componentes "+ testing.getComp().length);
  println("Componentes: ");
  printArray( testing.getComp());


 printArray(testing.pos(testing.getAssignature("1000004").getCode()));
  testing.createDict();

  
  testing.getAssignaturesLIST().sort();

//esta funcion a sido implementada en displayMalla()  
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
  fill(barMenu);
  rect(0,0,width,50);//Barra del menu                 
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

public void displayMalla(){//Funcion que dibuja una malla
  //Valores para recorrer el for que dibuja las materias
  
  testing.display();
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
   
class Assignature {
  String name;
  String code;
  int cred;
  int hour; 
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
    fill(0);
    rect(x, y, size, sizeb);

    //Cr\u00e9ditos

    rect(x, y, size/3, sizeb/7);
    fill(255);
    text(cred, x+size/6, y+sizeb/14);

    //Horas Presenciales
    fill(200);
    rect(x+size/3, y, size/3, sizeb/7);
    fill(0);
    text(hour, x+size/6 + size/3, y+sizeb/14);



    //Espacio para bot\u00f3n

    fill(225);
    rect(x+2*size/3, y, size/3, sizeb/7); 
    fill(0);



    // Nombre
    textFont(f, size*3/45);
    fill(255);
    rect(x, y+sizeb/7, size, 3*sizeb/7);
    fill(0);
    //Funci\u00f3n para salto de linea TODO
    printName(name, x, y, size, sizeb);



    // Agrupaci\u00f3n
    textFont(f, size*3/55);
    fill(agrColor);//TEMPORAL: OFRECIDO POR LA AGRUPACI\u00d3N

    rect(x, y+4*sizeb/7, size, 2*sizeb/7);
    fill(0);
    printAgr(agr, x, y, size, sizeb);




    //C\u00f3digo
    textFont(f, size*3/40);
    fill(20);
    rect(x, y+sizeb*6/7, size*3/4, sizeb/7);
    fill(255);
    text(code, x + size/3, y + sizeb*13/14);


    //Componente
    if (this.getComp() == "B") fill(255, 200, 0);
    else if (this.getComp() == "C") fill(0, 120, 0);
    else if (this.getComp() == "P") fill(100, 0, 130);
    else if (this.getComp() == "L") fill(180, 240, 240);
    else fill(255);    
    rect(x + size*3/4, y+sizeb*6/7, size/4, sizeb/7);
    fill(0);
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

      if (suma + i>25 || i == words.size() - 1) {
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

  int cred;
  int credFUND;
  int credDISC;
  int credLIBR;
  int semTotal;
  IntDict semestres;
  HashMap<Integer, String> sem;



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
    semestres = new IntDict();
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


  //Nombre
  public void askName(String tempName) {
    name = tempName;
  }
  public String getName() {
    return name;
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
    print(tempsemestres);
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

  }
  
  public void buttons(float x,float y,float size){
  
  
  
  
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
    fill(120,150,20+(semTotal -i)*15);
      
      rect(i*width/semTotal, 0, (i+1)*width/semTotal, height);
    stroke(0);
    }
    float tempSize = width/semTotal;


    float y = 0;

    for (String s : semestres.keys()) {

      localAssignatures.get(s).display( tempSize*0.1f + tempSize*(-1+semestres.get(localAssignatures.get(s).getCode())), height/14 + y*tempSize, tempSize*0.8f);
      y++;
      if (y>= mporsem.get(semestres.get(localAssignatures.get(s).getCode()))  ) y = 0;
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "StartPage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
