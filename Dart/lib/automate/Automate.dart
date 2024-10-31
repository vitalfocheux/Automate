class Automate {

  Set<int> initialStates = {};
  Set<int> finalStates = {};
  Set<String> alphabet = {};

  static const String Epsilon = '\0';

  Automate();

  bool isValid() {
    return false;
  }

  bool addSymbol(String symbol){
    return false;
  }

  bool removeSymbol(String symbol){
    return false;
  }

  bool hasSymbol(String symbol){
    return false;
  }

  int countSymbols(){
    return 0;
  }

  bool addState(int state){
    return false;
  }

  bool removeState(int state){
    return false;
  }

  bool hasState(int state){
    return false;
  }

  int countStates(){
    return 0;
  }

  void setStateInitial(int state){

  }

  bool isStateInitial(int state){
    return false;
  }

  void setStateFinal(int state){

  }

  bool isStateFinal(int state){
    return false;
  }

  bool addTransition(int from, String alpha, int to){
    return false;
  }

  bool removeTransition(int from, String alpha, int to){
    return false;
  }

  bool hasTransition(int from, String alpha, int to){
    return false;
  }

  int countTransitions(){
    return 0;
  }

  void prettyPrint(StringBuffer buffer){

  }

  void dotPrint(StringBuffer buffer){

  }

  bool hasEpsilonTransition(){
    return false;
  }

  bool isDeterministic(){
    return false;
  }

  bool isComplete(){
    return false;
  }

  Set<int> makeTransition(Set<int> origin, String alpha){
    return {};
  }

  Set<int> readString(String word){
    return {};
  }

  bool match(String word){
    return false;
  }

  void removeNonAccessibleStates(){

  }

  void removeNonCoAccessibleStates(){

  }

  bool isLanguageEmpty(){
    return false;
  }

  bool hasEmptyIntersectionWith(Automate other){
    return false;
  }

  bool isIncludedIn(Automate other){
    return false;
  }

  static Automate createMirror(Automate automate){
    return Automate();
  }

  static Automate createComplete(Automate automate){
    return Automate();
  }

  static Automate createComplement(Automate automate){
    return Automate();
  }

  static Automate createIntersection(Automate lhs, Automate rhs){
    return Automate();
  }

  static Automate createDeterministic(Automate other){
    return Automate();
  }

  static Automate createMinimalMoore(Automate other){
    return Automate();
  }

  static Automate createMinimalBrzozowski(Automate other){
    return Automate();
  }

}