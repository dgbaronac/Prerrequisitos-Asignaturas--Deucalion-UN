


import java.util.Map;
Assignature test;
Career testing;

HashMap<String, Assignature> assignatures; // TODAS LAS MATERIAS(Generadas a partir de DATA)   key: code
HashMap<String, Career> careers;  // Todas las carreras    key : code

JSONArray assignaturesjson; //Datos de materias.
JSONArray careersjson; //Datos de Carreras.
StringList careerKeys; //Nombres de archivos.


PFont f;
void setup() {

  size(800, 800);

  test = new Assignature();



  f = createFont("Calibri", 16, true);

  assignatures = new HashMap<String, Assignature>();
  careers = new HashMap<String, Career>();  
  loadData();
  testing = careers.get("2879");

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
  println("PROBANDO " + testing.preLine(testing.getAssignature("1000005")));
  testing.getAssignature("2015703").display(mouseX, mouseY);
  println("CREDITOS DE LA AGRUPACIÓN " + testing.getAssignature("202563").getAgr()+ ": " + testing.credAgr(testing.getAssignature("2025963").getAgr()));
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
  //background(0);


  //test.display(mouseX,mouseY);
}