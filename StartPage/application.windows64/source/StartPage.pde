import java.util.Map;
import controlP5.*;

ControlP5 cp5;

Assignature test;
Career testing;

HashMap<String, Assignature> assignatures; // TODAS LAS MATERIAS(Generadas a partir de DATA)   key: code
HashMap<String, Career> careers;  // Todas las carreras    key : code

JSONArray assignaturesjson; //Datos de materias.
JSONArray careersjson; //Datos de Carreras.
StringList careerKeys; //Nombres de archivos.

//Color para los botones cuando estan activos, inactivos o seleccionados y la barra de menu
color colorInactive;
color colorActive;
color colorSelected;
color barMenu;

PFont menuFont;//Fuente para el menu
PFont f;
void setup() {
  background(0);

  fullScreen();
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
   .setHeight(200)//Tamaño lista desplegable                  
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
   
   cp5.addBang("Materias")//añade un boton para visualizar la lista de materias
   .setPosition(330, 5)
   .setVisible(false)
   .setSize(80, 40)
   .setColorForeground(colorInactive)
   .setColorActive(colorSelected)
   .setColorCaptionLabel(0) 
   .setFont(createFont("arial", 14))         
   .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
   ;  
   
   cp5.addBang("Malla")//añade un boton para visualizar la malla curricular
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

  println("Código: " + testing.getCode());
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
      .setPosition(testing.positions.get(s).x -int(width/100), testing.positions.get(s).y)
      .setSize(int(width/100), (int)(width/105))
      .setId(testing.index.get(s))
      .setColorActive(color(200)) 
      .setColorForeground(color(50)) 
      .setLabelVisible(false)
      ;
  }
  /////////////////////////////////////////
}


void loadData() {

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
    color[] colors = new color[colorsize];

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


void draw() {

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

void displayMalla() {//Funcion que dibuja una malla
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


void colored() {

  testing.askColor((testing.getColor()+1)%255);
}