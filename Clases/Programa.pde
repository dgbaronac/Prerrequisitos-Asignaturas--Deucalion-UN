class Career {

  String code;
  String name;
  int cred;
  int credFUND;
  int credDISC;
  int credLIBR;

  StringList agrList;
  StringList compList;
  IntList colors;

  JSONArray assignatureList;
  JSONArray hierarchy;



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
    createHierarchy(plan.getJSONArray("component"));
    
    
  }
  
  JSONObject getJSONObject(){
    
  JSONObject plan = new JSONObject();

  
  return plan;
  
  }
  
  
  void declarateLists(){
   agrList = new StringList();
   compList = new StringList();
   colors = new IntList();
   assignatureList = new JSONArray();

  
  
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
  String[] getAssignatures(){
    String[] lista = new String[assignatureList.size()];
    for(int i = 0; i< assignatureList.size(); i++){
    lista[i] = assignatureList.getString(i);
    }
    return lista;
  }
  //Lista de nombres de agrupaciones
  String[] getAgrupations(){
    String[] lista = agrList.array();
    return lista;
  }
  
    String[] getComp(){
    String[] lista = compList.array();
    return lista;
  }

  void createHierarchy(JSONArray plan) {
   
  }
}