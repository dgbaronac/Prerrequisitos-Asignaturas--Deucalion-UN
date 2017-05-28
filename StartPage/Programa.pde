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
  HashMap<String,Integer> index;



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

  HashMap<String, Assignature> localAssignatures() {

    return localAssignatures;
  }

  JSONObject getJSONObject() {

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

  void askColors(String[] s) {
    colors.append(s);
  }


  void declarateLists() {
    agrList = new StringList();
    compList = new StringList();    
    assignatureList = new StringList();
    hierarchy = new JSONArray();
    colors = new StringList();
    localAssignatures = new HashMap<String, Assignature>();
    index = new HashMap<String,Integer>();
    semestres = new IntDict();
  }


  //Código SIA de 4 cifras
  void askCode(String tempCode) {
    code = tempCode;
  }  
  String getCode() {
    return code;
  } 

  //Semestres

  void askSem(int tempSem) {
    semTotal = tempSem;
  }
  int getSem() {
    return semTotal;
  }

  void growSem() {
    semTotal++;
  }

  void decreaseSem() {
    semTotal++;
  }


  //Nombre
  void askName(String tempName) {
    name = tempName;
  }
  String getName() {
    return name;
  }

  //Creditos
  void askCredits(int tempCode) {
    cred = tempCode;
  }
  int getCredits() {
    return cred;
  }
  // Créditos de fundamentación.
  void askCreditsFUND(int tempCode) {
    credFUND = tempCode;
  }
  int getCreditsFUND() {
    return credFUND;
  }
  //Créditos de disciplinar.
  void askCreditsDISC(int tempCode) {
    credDISC = tempCode;
  }
  int getCreditsDISC() {
    return credDISC;
  }
  //Créditos de libre elección.
  void askCreditsLIBR(int tempCode) {
    credLIBR = tempCode;
  }
  int getCreditsLIBR() {
    return credLIBR;
  }

  //Lista de códigos de materias.
  String[] getAssignatures() {
    String[] lista = assignatureList.array();
    return lista;
  }
  StringList getAssignaturesLIST() {
    StringList lista = assignatureList;
    return lista;
  }

  //Lista de nombres de agrupaciones
  String[] getAgrupations() {
    String[] lista = agrList.array();
    return lista;
  }

  String[] getComp() {
    String[] lista = compList.array();
    return lista;
  }
  
  Assignature getAssignature(String cod) {


    if (localAssignatures.containsKey(cod))return localAssignatures.get(cod);
    else return new Assignature();
  }
  
  
  void createHierarchy(JSONArray components) {
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


  int credAgr(String s) {
    int a = 0;
    for (Map.Entry<String, Assignature> m : localAssignatures.entrySet()) {
      if (m.getValue().getAgr().equals(s)) a += m.getValue().getCredits();
    }
    return a;
  }



  void createDict() {
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
    Integer x = 1;
    for(String s : semestres.keys()){
      index.put(s,x++);
    }

  }
  
  void buttons(float x,float y,float size, String s){
  
   
    colorMode(HSB);
    fill(170,200,50);
    colorMode(RGB);    
    rect(x + 2*size/3,y,size/3,5*size/42);
    fill(255);    
    text(index.get(s),x + 5*size/6,y+size/21);
    
    
    String[] list = localAssignatures.get(s).requirementsArray().array();
    IntDict ord = new IntDict();
    
    
    for( int i = 0; i< list.length; i++){
      ord.set(list[i],index.get(list[i]));    
    }
    
    
    ord.sortValues();
    if(list.length == 0) return;
    
  Circles boton = new Circles(x,y,size, ord.valueArray())   ;
  boton.display();
  
  
  }



  int suma(int[] prev) {
    int suma = 0;
    for (int i = 0; i<prev.length; i++) {
      suma+=prev[i];
    }

    return suma;
  }
  int premax(String s, IntDict dict) {
    if (localAssignatures.get(s).requirementsArray().array().length == 0) return 0;
    int x = 0;
    int tempx = 0;

    for (String st : localAssignatures.get(s).requirementsArray()) {
      tempx = dict.get(st);
      if (tempx> x) x = tempx;
    }
    return x;
  }
  

  int preline(String s) {
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



  int preline(String[] s) {

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



  String[] pos(String s) {

    StringList templist = new StringList();
    for (Map.Entry<String, Assignature> map : localAssignatures.entrySet()) {
      if (localAssignatures.get(s).needsMe(map.getValue())) templist.append(map.getValue().getCode());
    }

    return templist.array();
  }





  HashMap<String, Assignature> subLocal(String[] s) {
    HashMap<String, Assignature> m = new HashMap<String, Assignature>();
    for (int i = 0; i<s.length; i++) {
      m.put(localAssignatures.get(s[i]).getCode(), localAssignatures.get(s[i]));
    }
    return m;
  }

  void display() {
    Assignature example = new Assignature();
    example.askCode("Código");
    example.askName("NOMBRE DE LA ASIGNATURA");
    example.askAgr("NOMBRE DE LA AGRUPACIÓN");
    
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
    fill(150,200,20+(semTotal -i)*15);
      
      rect(i*width/semTotal, 0, (i+1)*width/semTotal, height);
    stroke(0);
    }
    float tempSize = width/semTotal;


    float y = 0;

    for (String s : semestres.keys()) {

      localAssignatures.get(s).display( tempSize*0.1 + tempSize*(-1+semestres.get(localAssignatures.get(s).getCode())), height/14 + y*tempSize, tempSize*0.8);
      buttons(tempSize*0.1 + tempSize*(-1+semestres.get(localAssignatures.get(s).getCode())), height/14 + y*tempSize, tempSize*0.8,s);
      y++;
      if (y>= mporsem.get(semestres.get(localAssignatures.get(s).getCode()))  ) y = 0;
    }
    example.display(10,height-width/semTotal,0.9*width/semTotal);
    fill(0);
    rect(10,height-width/semTotal,0.3*width/semTotal,   20);
    fill(255);
    text("Créditos",10+ 0.15*width/semTotal, 8+height-width/semTotal);
     fill(130);
    rect(10 + 0.3*width/semTotal ,height-width/semTotal,0.3*width/semTotal,   20);
    fill(0);
    text("Horas",0.3*width/semTotal+10+ 0.15*width/semTotal, 8+height-width/semTotal);
     text("ID",0.6*width/semTotal+10+ 0.15*width/semTotal, 8+height-width/semTotal);
  }
}