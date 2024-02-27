package fr.automate;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Automate {
    
    public static char Epsilon = '\0';

    private Set<Character> alphabet;
    private Set<Integer> states;
    private Set<Integer> initialStates;
    private Set<Integer> finalStates;
    private Map<Integer, Map<Character, Set<Integer>>> transitions;

    public Automate() {
        alphabet = new HashSet<>();
        states = new HashSet<>();
        initialStates = new HashSet<>();
        finalStates = new HashSet<>();
        transitions = new HashMap<>();
    }

    public boolean isValid(){
        return !alphabet.isEmpty() && !states.isEmpty();
    }

    public boolean addSymbol(char symbol){
        if(!(Character.isDefined(symbol) && symbol >= 0x21 && symbol <= 0x7E) || symbol == Epsilon || alphabet.contains(symbol)){
            return false;
        }
        return alphabet.add(symbol);
    }

    public boolean removeSymbol(char symbol){
        if(!hasSymbol(symbol)){
            return false;
        }
        for(int state : states){
            for(char alpha : alphabet){
                if(transitions.get(state).keySet().contains(alpha)){
                    transitions.remove(state);
                }
            }
        }
        return alphabet.remove(symbol);
    }

    public boolean hasSymbol(char symbol){
        return alphabet.contains(symbol);
    }   

    public long countSymbols(){
        return alphabet.size();
    }

    public boolean addState(int state){
        return states.add(state);
    }

    public boolean removeState(int state){
        if(!hasState(state)){
            return false;
        }
        if(initialStates.contains(state)){
            initialStates.remove(state);
        }
        if(finalStates.contains(state)){
            finalStates.remove(state);
        }
        for(int from : states){
            if(transitions.keySet().contains(from)){
                transitions.remove(state);
            }
            for(char alpha : alphabet){
                for(int to : states){
                    if(transitions.get(from).get(alpha).contains(to)){
                        removeTransition(from, alpha, to);
                    }
                }
            }
        }
        return states.remove(state);
    }

    public boolean hasState(int state){
        return states.contains(state);
    }

    public long countStates(){
        return states.size();
    }

    public void setStateInitial(int state){
    }

    public boolean isStateInitial(int state){
        return initialStates.contains(state);
    }

    public void setStateFinal(int state){
    }

    public boolean isStateFinal(int state){
        return finalStates.contains(state);
    }

    public boolean addTransition(int from, char symbol, int to){
        if(!hasTransition(from, symbol, to)){
            return false;
        }
        Map<Character, Set<Integer>> fromTransitions = transitions.get(from);
        if(fromTransitions == null){
            fromTransitions = new HashMap<>();
            Set<Integer> symbolTransitions = new HashSet<>();
            fromTransitions.put(symbol, symbolTransitions);
            transitions.put(from, fromTransitions);
        }
        return true;
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