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

  fullScreen();
  
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
     .setHeight(200)//Tamaño lista desplegable                  
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
  
  cp5.addBang("Materias")//añade un boton para visualizar la lista de materias
     .setPosition(330,5)
     .setSize(80,40)
     .setColorForeground(colorInactive)
     .setColorActive(colorSelected)
     .setColorCaptionLabel(0) 
     .setFont(createFont("arial",14))         
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;  
  
  cp5.addBang("Malla")//añade un boton para visualizar la malla curricular
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
  println("Código: " + testing.getCode());
  println(" n° de materias " + testing.getAssignatures().length);
  println("Materias: ");
  printArray( testing.getAssignatures());
  println(" n° de agrupaciones " + testing.getAgrupations().length);
  println("Agrupaciones: ");
  printArray( testing.getAgrupations());
  println(" n° de componentes "+ testing.getComp().length);
  println("Componentes: ");
  printArray( testing.getComp());
  background(50);
  
 // testing.getAssignature("2025970").display(mouseX, mouseY);
  
  String[] sa = {"1000013", "1000004"};
  testing.preline("2025966");
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
  background(100);
  fill(barMenu);
  rect(0,0,width,50);//Barra del menu  
  
  displayMalla();//Corregir para que las materias se empiezen a dibujar en y=50
                 //Se puede observar como se superponen con la barra de menu
}

/*void controlEvent(ControlEvent theEvent){//Para controlar lo que hacen los botones del menu
   if(theEvent.isController()) {
     
      print("control event from : "+theEvent.controller().name());
      println(", value : "+theEvent.controller().value());
    
      if(theEvent.controller().name()=="Malla") {
           displayMalla();
      }
   }
}*/

void displayMalla(){//Funcion que dibuja una malla
  //Valores para recorrer el for que dibuja las materias
  int x = 0;
  int y = 0;
  for(String s: testing.getAssignatures()){ 
    testing.getAssignature(s).display(190*x++,180*y);
    if( 190*x> width -190){
      x = 0;
      y++;
    }
  }
}
   