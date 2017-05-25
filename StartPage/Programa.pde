class Career {

  String code;
  String name;

  int cred;
  int credFUND;
  int credDISC;
  int credLIBR;


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
  }
  //Código SIA de 4 cifras
  void askCode(String tempCode) {
    code = tempCode;
  }  
  String getCode() {
    return code;
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



  int preline(String s) {
    int x = 0;
    println();
    println("start");
    println(s);
    Assignature m = localAssignatures.get(s);
    
    String[] materias = m.requirementsArray().array();
    if (materias.length != 0) x++;
    println("Prerrequisitos");
    printArray(materias);
    if ( materias.length == 0 ) return 0; //return 0;

    StringList temp = new StringList();

    for (int i =  0; i< materias.length; i++) {
      if (localAssignatures.get(materias[i]).hasRequirements()) temp.append(localAssignatures.get(materias[i]).requirementsArray());
    }
    temp = new StringList(temp.getUnique());    
    println("prerrequisitos de esos prerrequisitos");
    printArray(temp.array());


   


    println("skip");
    return x + preline(temp.array());
  }



  int preline(String[] s) {
    println("new");
    print(s);
    println();
    int x= 0;

    Assignature[] all = new Assignature[s.length];
    if (all.length == 0)return 0; else x++;
    
    StringList pre = new StringList();

    for (int i = 0; i< all.length; i++) {
      all[i] = localAssignatures.get(s[i]);
      println(all[i].getName());
      pre.append(all[i].requirementsArray().array());
      println(all[i].requirementsArray());
    }
    println("materias");


    //m.requirementsArray().array();



    if ( pre.size() != 0 ) x++; //return 0;

    StringList temp = new StringList();

    for (int i =  0; i< pre.size(); i++) {
      if (localAssignatures.get(pre.get(i)).hasRequirements()) temp.append(localAssignatures.get(pre.get(i)).requirementsArray());
    }

    temp = new StringList(temp.getUnique());
    println();
    printArray(temp.array());





    return x + preline(temp.array());
  }





  void posLine(String s) {
    
    Assignature m = localAssignatures.get(s);
    StringList pos = new StringList();
    for(Map.Entry<String,Assignature> map : localAssignatures.entrySet()){
    if (m.needsMe(map.getValue())) pos.append( map.getValue().getCode());
    
    }
    print(pos);
  }




  HashMap<String, Assignature> subLocal(String[] s) {
    HashMap<String, Assignature> m = new HashMap<String, Assignature>();
    for (int i = 0; i<s.length; i++) {
      m.put(localAssignatures.get(s[i]).getCode(), localAssignatures.get(s[i]));
    }
    return m;
  }

  void display() {
  }
}