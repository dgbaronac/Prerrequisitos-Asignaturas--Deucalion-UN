class Assignature {
  String name;
  String code;
  int cred;
  int hour; 
  StringList prerequisites;
  String agr;
  color agrColor;
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
  void askName(String tempName) {
    name = tempName;
  }
  String getName() {
    return name;
  }




  //Código
  void askCode(String tempCode) {
    code = tempCode;
  }
  String getCode() {
    return code;
  } 




  //Agrupación.
  void askAgr(String tempAgr) {
    agr = tempAgr;
  }
  String getAgr() {
    return agr;
  }



  //Componente.

  void askComp(String tempComp) {
    comp = tempComp;
  }
  String getComp() {
    return comp;
  }



  //Créditos
  void askCredit(Integer tempCred) {
    cred = tempCred;
  }
  Integer getCredits() {
    return cred;
  }




  //Horas
  
  void askHour(Integer tempHour) {
    hour = tempHour;
  }
  Integer getHour() {
    return hour;
  }


  //Color de agrupación
  void askColor(Integer tempColor) {
    agrColor = tempColor;
  }
  Integer getColor() {
    return agrColor;
  }






  JSONObject getJSONObject() {
    JSONObject materia = new JSONObject();
    materia.setString("code", code);
    materia.setString("name", name);
    materia.setInt("cred", cred);
    materia.setJSONArray("need", new JSONArray(prerequisites));      
    return materia;
  }



  Boolean needsMe(Assignature m){
    
    StringList pre = m.requirementsArray();
    
    if(pre.hasValue(code)) return true;  
    else return false; 
 
  }
  
  
  
  Boolean needsIt(Assignature m){
    if(prerequisites.hasValue(m.getCode())) return true;  
    else return false;
  }
  
  




  //Pre-Requisitos

  Boolean hasRequirements() {
    if (prerequisites.size() == 0) return false;
    else return true;
  }

  void loadRequirements(StringList req) {
    prerequisites = req;
  }
  void initRequire() {
    prerequisites = new StringList();
  }

  StringList requirementsArray() {
    StringList s = new StringList();
    s.append(prerequisites);
    return s;
  }

  void deleteRequirement(String code) {
    prerequisites.removeValue(code);
  }

  void addRequirement(String code) {
    prerequisites.append(code);
  }  
  void addRequirement(String[] code) {
    prerequisites.append(code);
  }  
  void addRequirement(Assignature assignature) {
    if (prerequisites.hasValue(assignature.getCode())== false)
      prerequisites.append(assignature.getCode());
  }

  


  void display(int x, int y) {


    float size = 170;
    float sizeb = size*5/6;
    // Depende del tamaño
    textAlign(CENTER, CENTER);
    textFont(f, size*3/40);
    noStroke();
    fill(0);
    rect(x, y, size, sizeb);

    //Créditos

    rect(x, y, size/3, sizeb/7);
    fill(255);
    text(cred, x+size/6, y+sizeb/14);

    //Horas Presenciales
    fill(200);
    rect(x+size/3, y, size/3, sizeb/7);
    fill(0);
    text(hour, x+size/6 + size/3, y+sizeb/14);



    //Espacio para botón

    fill(225);
    rect(x+2*size/3, y, size/3, sizeb/7); 
    fill(0);



    // Nombre
    textFont(f, size*3/45);
    fill(255);
    rect(x, y+sizeb/7, size, 3*sizeb/7);
    fill(0);
    //Función para salto de linea TODO
    printName(name, x, y, size, sizeb);



    // Agrupación
    textFont(f, size*3/55);
    fill(agrColor);//TEMPORAL: OFRECIDO POR LA AGRUPACIÓN

    rect(x, y+4*sizeb/7, size, 2*sizeb/7);
    fill(0);
    printAgr(agr, x, y, size, sizeb);




    //Código
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

  void printName(String s, float x, float y, float size, float sizeb) {

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

  void printAgr(String s, float x, float y, float size, float sizeb) {

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