package fr.automate;

import java.util.Set;

public class Automate {
    
    public static char Epsilon = '\0';

    public Automate() {
        // TODO Auto-generated constructor stub
    }

    public boolean isValid(){
        return false;
    }

    public boolean addSymbol(char symbol){
        return false;
    }

    public boolean removeSymbol(char symbol){
        return false;
    }

    public boolean hasSymbol(char symbol){
        return false;
    }   

    public long countSymbols(){
        return 0;
    }

    public boolean addState(int state){
        return false;
    }

    public boolean removeState(int state){
        return false;
    }

    public boolean hasState(int state){
        return false;
    }

    public long countStates(){
        return 0;
    }

    public void setStateInitial(int state){
    }

    public boolean isStateInitial(int state){
        return false;
    }

    public void setStateFinal(int state){
    }

    public boolean isStateFinal(int state){
        return false;
    }

    public boolean addTransition(int from, char symbol, int to){
        return false;
    }

    public boolean removeTransition(int from, char symbol, int to){
        return false;
    }

    public boolean hasTransition(int from, char symbol, int to){
        return false;
    }

    public long countTransitions(){
        return 0;
    }

    public void prettyPrint(){
    }

    public boolean hasEpsilonTransition(){
        return false;
    }

    public boolean isDeterministic(){
        return false;
    }

    public boolean isComplete(){
        return false;
    }

    public Set<Integer> makeTransition(Set<Integer> origin, char alpha){
        return null;
    }

    public Set<Integer> readString(String word){
        return null;
    }

    public boolean match(String word){
        return false;
    }

    public void removeNonAccessibleStates(){
    }

    public void removeNonCoAccessibleStates(){
    }

    public boolean isLanguageEmpty(){
        return false;
    }

    public boolean hasEmptyIntersectionWith(Automate other){
        return false;
    }

    public boolean isIncludedIn(Automate other){
        return false;
    }

    public static Automate createMirror(Automate a){
        return null;
    }

    public static Automate createComplete(Automate a){
        return null;
    }

    public static Automate createComplement(Automate a){
        return null;
    }

    public static Automate createIntersection(Automate lhs, Automate rhs){
        return null;
    }

    public static Automate createDeterministic(Automate other){
        return null;
    }

    public static Automate createMinimalMoore(Automate other){
        return null;
    }

    public static Automate createMinimalBrzozowski(Automate other){
        return null;
    }



}