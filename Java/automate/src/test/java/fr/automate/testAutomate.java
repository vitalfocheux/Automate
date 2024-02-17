package fr.automate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testAutomate {
    
    public String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public boolean equivalent(Automate a1, Automate a2) {
        return ((a1.isIncludedIn(a2)) && (a2.isIncludedIn(a1)));
    }

    

    public boolean equivalentMirror(Automate a, Automate mirror){
        return ((a.isIncludedIn(Automate.createMirror(mirror))) && (Automate.createMirror(mirror).isIncludedIn(a)));
    }

    Automate a;

    @Before
    public void setup(){
        a = new Automate();
    }

    @Test
    public void isValid_NoSymbolNoState(){
        Assert.assertFalse(a.isValid());
    }

    @Test
    public void isValid_NoSymbolWithState(){
        a.addState(0);
        Assert.assertFalse(a.isValid());
        Assert.assertTrue(a.hasState(0));
    }

    @Test
    public void isValid_WithSymbolNoState(){
        a.addSymbol('a');
        Assert.assertFalse(a.isValid());
        Assert.assertTrue(a.hasSymbol('a'));
    }

    @Test
    public void isValid_WithSymbolWithState(){
        a.addState(0);
        a.addSymbol('a');
        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasSymbol('a'));
    }

    @Test
    public void addedSymbol_Epsilon(){
        Assert.assertFalse(a.addSymbol(Automate.Epsilon));
    }

    @Test
    public void addedSymbol_Space(){
        Assert.assertFalse(a.addSymbol(' '));
    }

    @Test
    public void addedSymbol_IsNotGraph(){
        Assert.assertFalse(a.addSymbol('\n'));
    }

    @Test
    public void addedSymbol_IsGraph(){
        int c = 0;
        for(int i = 0; i < 256; ++i){
            if(Character.isDefined(i) && !Character.isSpaceChar(i)){
                Assert.assertTrue(a.addSymbol((char)i));
                Assert.assertTrue(a.hasSymbol((char)i));
                c++;
                Assert.assertEquals(a.countSymbols(), c);
            }else{
                Assert.assertFalse(a.addSymbol((char)i));
                Assert.assertFalse(a.hasSymbol((char)i));
                Assert.assertEquals(a.countSymbols(), c);
            }
        }
        Assert.assertEquals(a.countSymbols(), 94);
    }

    @Test
    public void addedSymbol_OneSymbol(){
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
    }

    @Test
    public void addedSymbol_TwoSymbols(){
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
    }

    @Test
    public void addedSymbol_TwoIdenticalSymbols(){
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.addSymbol('a'));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
    }

    @Test
    public void removedSymbol_OneSymbol(){
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.removeSymbol('a'));
        Assert.assertFalse(a.hasSymbol('a'));
    }

    @Test
    public void removedSymbol_Empty(){
        Assert.assertFalse(a.removeSymbol('a'));
    }

    @Test
    public void removedSymbol_NotInAlphabet(){
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.removeSymbol('b'));
        Assert.assertTrue(a.hasSymbol('a'));
    }

    @Test
    public void removedSymbol_AllCharacter(){
        int c = 0;
        for(int i = 0; i < 256; ++i){
            if(Character.isDefined(i) && !Character.isSpaceChar(i)){
                Assert.assertTrue(a.addSymbol((char)i));
                Assert.assertTrue(a.hasSymbol((char)i));
                c++;
                Assert.assertEquals(a.countSymbols(), c);
            }else{
                Assert.assertFalse(a.addSymbol((char)i));
                Assert.assertFalse(a.hasSymbol((char)i));
                Assert.assertEquals(a.countSymbols(), c);
            }
        }

        Assert.assertEquals(a.countSymbols(), 94);

        for(int i = 0; i < 256; ++i){
            if(Character.isDefined(i) && !Character.isSpaceChar(i)){
                Assert.assertTrue(a.removeSymbol((char)i));
                Assert.assertFalse(a.hasSymbol((char)i));
                c--;
                Assert.assertEquals(a.countSymbols(), c);
            }else{
                Assert.assertFalse(a.removeSymbol((char)i));
                Assert.assertFalse(a.hasSymbol((char)i));
                Assert.assertEquals(a.countSymbols(), c);
            }
        }

        Assert.assertEquals(a.countSymbols(), 0);
    }

    @Test
    public void removedSymbol_SymbolInTransition(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'b', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertTrue(a.removeSymbol('a'));
        Assert.assertFalse(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));

        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.hasState(i));
        }

        Assert.assertTrue(a.hasTransition(1, 'b', 3));
        Assert.assertTrue(a.hasTransition(2, 'b', 4)); 
        Assert.assertTrue(a.hasTransition(3, 'b', 4));         
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertFalse(a.hasTransition(0, 'a', 2));
        Assert.assertFalse(a.hasTransition(0, 'a', 3));
        Assert.assertFalse(a.hasTransition(2, 'a', 3));
        Assert.assertFalse(a.hasTransition(3, 'a', 3));
        Assert.assertFalse(a.hasTransition(4, 'a', 4));
        Assert.assertEquals(a.countTransitions(), 3);
    }

    @Test
    public void hasSymbol_Successful(){
        for(int i = 0; i < 7; ++i){
            Assert.assertTrue(a.addSymbol((char)(i + 'a')));
        }

        Assert.assertTrue(a.hasSymbol('f'));
    }

    @Test
    public void hasSymbol_Empty(){
        Assert.assertFalse(a.hasSymbol('a'));
    }

    @Test
    public void hasSymbol_NotIsGraph(){
        for(int i = 0; i < 7; ++i){
            Assert.assertTrue(a.addSymbol((char)(i + 'a')));
        }

        Assert.assertFalse(a.hasSymbol('\n'));
    }

    @Test
    public void countSymbol_Full(){
        for(int i = 0; i < 7; ++i){
            Assert.assertTrue(a.addSymbol((char)(i + 'a')));
        }

        Assert.assertEquals(a.countSymbols(), 7);
    }

    @Test
    public void countSymbol_Empty(){
        Assert.assertEquals(a.countSymbols(), 0);
    }

    @Test
    public void addedState_OneState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void addedState_TwoIdenticalStates(){
        Assert.assertTrue(a.addState(0));
        Assert.assertFalse(a.addState(0));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void addedState_TwoStates(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
    }

    @Test
    public void addedState_Negative(){
        Assert.assertFalse(a.addState(-1));
        Assert.assertFalse(a.hasState(-1));
        Assert.assertEquals(a.countStates(), 0);
    }

    @Test
    public void addedState_MAX(){
        Assert.assertTrue(a.addState(Integer.MAX_VALUE));
        Assert.assertTrue(a.hasState(Integer.MAX_VALUE));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void addedState_MIN(){
        Assert.assertFalse(a.addState(Integer.MIN_VALUE));
        Assert.assertFalse(a.hasState(Integer.MIN_VALUE));
        Assert.assertEquals(a.countStates(), 0);
    }

    @Test
    public void removedState_Empty(){
        Assert.assertFalse(a.removeState(0));

        Assert.assertFalse(a.hasState(0));
        Assert.assertEquals(a.countStates(), 0);
    }

    @Test
    public void removedState_UnknownState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertFalse(a.removeState(1));
        Assert.assertTrue(a.hasState(0));

        Assert.assertTrue(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void removedState_OneState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.removeState(0));
        Assert.assertFalse(a.hasState(0));
        Assert.assertEquals(a.countStates(), 0);
    }

    @Test
    public void removedState_AllStates(){
        int c = 0;
        for(int i = 0; i < 10; ++i){
            Assert.assertTrue(a.addState(i));
            Assert.assertTrue(a.hasState(i));
            ++c;
            Assert.assertEquals(a.countStates(), c);
        }

        for(int i = 0; i < 10; ++i){
            Assert.assertTrue(a.removeState(i));
            Assert.assertFalse(a.hasState(i));
            --c;
            Assert.assertEquals(a.countStates(), c);
        }
    }

    @Test
    public void removedState_OriginInTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.removeState(0));

        Assert.assertFalse(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedState_DestinationInTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.removeState(1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedState_OriginAndDestinationInTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.removeState(0));
        Assert.assertTrue(a.removeState(1));

        Assert.assertFalse(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 0);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedState_StateInTransition(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'b', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertTrue(a.removeState(3));

        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertTrue(a.hasState(4));
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'a', 2));
        Assert.assertFalse(a.hasTransition(0, 'a', 3));
        Assert.assertFalse(a.hasTransition(1, 'b', 3));
        Assert.assertFalse(a.hasTransition(2, 'a', 3));
        Assert.assertTrue(a.hasTransition(2, 'b', 4));
        Assert.assertFalse(a.hasTransition(3, 'a', 3));
        Assert.assertFalse(a.hasTransition(3, 'b', 4));
        Assert.assertTrue(a.hasTransition(4, 'a', 4));
        Assert.assertEquals(a.countTransitions(), 4);
    }

    @Test
    public void hasState_Empty(){
        Assert.assertFalse(a.hasState(0));
    }

    @Test
    public void hasState_AlreadyIn(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.hasState(0));
    }

    @Test
    public void hasState_NotIn(){
        Assert.assertTrue(a.addState(0));
        Assert.assertFalse(a.hasState(1));
    }

    @Test
    public void countStates_Empty(){
        Assert.assertEquals(a.countStates(), 0);
    }

    @Test
    public void countStates_NotEmpty(){
        for(int i = 0; i < 10; ++i){
            Assert.assertTrue(a.addState(i));
            Assert.assertEquals(a.countStates(), i + 1);
        }
    }

    @Test
    public void setStateInitial_OneInitialState(){
        Assert.assertTrue(a.addState(0));
        a.setStateInitial(0);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);;
    }

    @Test
    public void setStateInitial_ToFinalAndInitial(){
        Assert.assertTrue(a.addState(0));
        a.setStateFinal(0);
        a.setStateInitial(0);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void setStateInitial_TwoInitialStates(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        a.setStateInitial(0);
        a.setStateInitial(1);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateInitial(1));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
    }

    @Test
    public void setStateInitial_UnknownState(){
        Assert.assertTrue(a.addState(0));
        a.setStateInitial(1);
        Assert.assertFalse(a.isStateInitial(1));
        Assert.assertTrue(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void setStateFinal_OneFinalState(){
        Assert.assertTrue(a.addState(0));
        a.setStateFinal(0);
        Assert.assertTrue(a.isStateFinal(0));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void setStateFinal_TwoFinalStates(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        a.setStateFinal(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.isStateFinal(0));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
    }

    @Test
    public void setStateFinal_ToFinalAndInitial(){
        Assert.assertTrue(a.addState(0));
        a.setStateInitial(0);
        a.setStateFinal(0);
        Assert.assertTrue(a.isStateFinal(0));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void setStateFinal_UnknownState(){
        Assert.assertTrue(a.addState(0));
        a.setStateFinal(1);
        Assert.assertFalse(a.isStateFinal(1));
        Assert.assertTrue(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
    }

    @Test
    public void addedTransition_UnknownSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertFalse(a.addTransition(0, 'a', 1));

        
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertFalse(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 0);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void addedTransition_UnknownOrigin(){
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.addTransition(0, 'a', 1));

        Assert.assertFalse(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(1, 'a', 0));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void addedTransition_UnknownTarget(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.addTransition(0, 'a', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void addedTransition_OneTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void addedTransition_TwoIdenticalTransitions(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertFalse(a.addTransition(0, 'a', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void addedTransition_SameOriginAndLetter(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void addedTransition_SameOriginAndDestination(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'b', 1));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void addedTransition_SameLetterAndDestination(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(1, 'a', 2));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 2));
        Assert.assertTrue(a.hasTransition(1, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void addedTransition_Epsilon(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addTransition(0, Automate.Epsilon, 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertFalse(a.hasSymbol(Automate.Epsilon));
        Assert.assertEquals(a.countSymbols(), 0);
        Assert.assertTrue(a.hasTransition(0, Automate.Epsilon, 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removedTransition_UnknownSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertFalse(a.removeTransition(0, 'b', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertFalse(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 0);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedTransition_UnknownOrigin(){
        Assert.assertTrue(a.addState(1));;
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.removeTransition(0, 'a', 1));

        Assert.assertFalse(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedTransition_UnknownTarget(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.removeTransition(0, 'a', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertFalse(a.hasState(1));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedTransition_Empty(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.removeTransition(0, 'a', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removedTransition_OneTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.removeTransition(0, 'a', 1));

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertFalse(a.hasTransition(0, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void hasTransition_Empty(){
        Assert.assertFalse(a.hasTransition(0, 'a', 1));
    }

    @Test
    public void hasTransition_DontHaveThisTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertFalse(a.hasTransition(1, 'a', 0));
    }

    @Test
    public void hasTransition_DontHaveSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertFalse(a.hasTransition(1, 'b', 0));
    }

    @Test
    public void hasTransition_DontHaveStateFrom(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertFalse(a.hasTransition(2, 'a', 1));
    }

    @Test
    public void hasTransition_DontHaveStateTo(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertFalse(a.hasTransition(1, 'a', 2));
    }

    @Test
    public void hasTransition_Success(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
    }

    @Test
    public void countTransitions_Empty(){
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void countTransitions_NotEmpty(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void prettyPrint_Test(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'b', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));
        Assert.assertEquals(a.countTransitions(), 9);

        a.prettyPrint();
    }

    @Test
    public void hasEpsilonTransition_WithoutEpsilon(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        a.setStateInitial(0);
        a.setStateInitial(1);
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.hasEpsilonTransition());

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateInitial(1));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void hasEpsilonTransition_WithEpsilon(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        a.setStateInitial(0);
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, Automate.Epsilon, 2));
        Assert.assertTrue(a.hasEpsilonTransition());

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertTrue(a.isStateFinal(2));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(1, Automate.Epsilon,2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void hasEpsilonTransition_Twice(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, Automate.Epsilon, 1));
        Assert.assertTrue(a.addTransition(1, Automate.Epsilon, 2));
        Assert.assertTrue(a.hasEpsilonTransition());

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, Automate.Epsilon,1));
        Assert.assertTrue(a.hasTransition(1, Automate.Epsilon,2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void hasEpsilonTransition_AddAndRemove(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, Automate.Epsilon, 1));
        Assert.assertTrue(a.hasEpsilonTransition());

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, Automate.Epsilon, 1));
        Assert.assertEquals(a.countTransitions(), 1);

        Assert.assertTrue(a.removeTransition(0, Automate.Epsilon, 1));
        Assert.assertFalse(a.hasEpsilonTransition());

        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, Automate.Epsilon, 1));
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void isDeterministic_NoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Assert.assertTrue(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void isDeterministic_TwoTransitions(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        a.setStateInitial(0);
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertFalse(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.match("a"));
        Assert.assertFalse(a.match(""));
        Assert.assertFalse(a.match("aa"));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void isDeterministic_ZeroInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertFalse(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void isDeterministic_NoFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void isDeterministic_TwoInitialStates(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertFalse(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.match(""));
        Assert.assertTrue(a.match("a"));
        Assert.assertTrue(a.match("aa"));
        Assert.assertFalse(a.match("aaa"));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateInitial(1));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertTrue(a.isStateFinal(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 2));
        Assert.assertTrue(a.hasTransition(1, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void isDeterministic_AlreadyDeterministic(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.match("a"));
        Assert.assertTrue(a.match("b"));
        Assert.assertFalse(a.match(""));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertTrue(a.isStateFinal(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'b', 2));
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void isDeterministic_AlreadyDeterministicAndCreateDeterministic(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.match("a"));
        Assert.assertTrue(a.match("b"));
        Assert.assertFalse(a.match(""));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertTrue(a.isStateFinal(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'b', 2));
        Assert.assertEquals(a.countTransitions(), 2);

        Automate b = Automate.createDeterministic(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.match("a"));
        Assert.assertTrue(b.match("b"));
        Assert.assertFalse(b.match(""));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
    }

    @Test
    public void isDeterministic_AlreadyDeterministicAndCreateMinimalMoore(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateFinal(3);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'b', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'b', 5));
        Assert.assertTrue(a.addTransition(4, 'a', 3));
        Assert.assertTrue(a.addTransition(4, 'b', 5));
        Assert.assertTrue(a.addTransition(5, 'a', 5));
        Assert.assertTrue(a.addTransition(5, 'b', 5));
        Assert.assertTrue(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.hasState(i));
        }
        Assert.assertEquals(a.countStates(), 6);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(3));
        Assert.assertTrue(a.isStateFinal(4));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'b', 2));
        Assert.assertTrue(a.hasTransition(1, 'a', 2));
        Assert.assertTrue(a.hasTransition(1, 'b', 3));
        Assert.assertTrue(a.hasTransition(2, 'a', 1));
        Assert.assertTrue(a.hasTransition(2, 'b', 4));
        Assert.assertTrue(a.hasTransition(3, 'a', 4));
        Assert.assertTrue(a.hasTransition(3, 'b', 5));
        Assert.assertTrue(a.hasTransition(4, 'a', 3));
        Assert.assertTrue(a.hasTransition(4, 'b', 5));
        Assert.assertTrue(a.hasTransition(5, 'a', 5));
        Assert.assertTrue(a.hasTransition(5, 'b', 5));
        Assert.assertEquals(a.countTransitions(), 12);

        Automate b = Automate.createMinimalMoore(a);

        Assert.assertTrue(equivalent(a, b));

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.isComplete());
        Assert.assertEquals(b.countStates(), 4);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
    }

    @Test
    public void isDeterministic_AlreadyDeterministicAndCreateMinimalBrzozowski(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateFinal(3);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'b', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'b', 5));
        Assert.assertTrue(a.addTransition(4, 'a', 3));
        Assert.assertTrue(a.addTransition(4, 'b', 5));
        Assert.assertTrue(a.addTransition(5, 'a', 5));
        Assert.assertTrue(a.addTransition(5, 'b', 5));
        
        Assert.assertTrue(a.isDeterministic());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.hasState(i));
        }
        Assert.assertEquals(a.countStates(), 6);
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(3));
        Assert.assertTrue(a.isStateFinal(4));
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(0, 'b', 2));
        Assert.assertTrue(a.hasTransition(1, 'a', 2));
        Assert.assertTrue(a.hasTransition(1, 'b', 3));
        Assert.assertTrue(a.hasTransition(2, 'a', 1));
        Assert.assertTrue(a.hasTransition(2, 'b', 4));
        Assert.assertTrue(a.hasTransition(3, 'a', 4));
        Assert.assertTrue(a.hasTransition(3, 'b', 5));
        Assert.assertTrue(a.hasTransition(4, 'a', 3));
        Assert.assertTrue(a.hasTransition(4, 'b', 5));
        Assert.assertTrue(a.hasTransition(5, 'a', 5));
        Assert.assertTrue(a.hasTransition(5, 'b', 5));
        Assert.assertEquals(a.countTransitions(), 12);

        Automate b = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(equivalent(a, b));

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.isComplete());
        Assert.assertEquals(b.countStates(), 4);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
    }

    @Test
    public void isComplete_ZeroTransittion(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.isComplete());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 0);
        Assert.assertFalse(a.match(""));
        Assert.assertFalse(a.match("a"));
    }

    @Test
    public void isComplete_Good(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.isComplete());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertFalse(a.match("a"));
        Assert.assertFalse(a.match(""));
        Assert.assertTrue(a.hasState(0));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 0));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void isComplete_TwoTransitionWithAlphabetTwoLetter(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertFalse(a.isComplete());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertFalse(a.match("a"));
        Assert.assertFalse(a.match("b"));
        Assert.assertFalse(a.match(""));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 0));
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertTrue(a.hasTransition(1, 'a', 0));
        Assert.assertTrue(a.hasTransition(1, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 4);
    }

    @Test
    public void isComplete_AddedRemovedTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 0));
        Assert.assertTrue(a.isComplete());
        Assert.assertTrue(a.removeTransition(1, 'a', 0));
        Assert.assertFalse(a.isComplete());

        Assert.assertTrue(a.isValid());
        Assert.assertTrue(a.isLanguageEmpty());
        Assert.assertFalse(a.match("a"));
        Assert.assertFalse(a.match(""));
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void createComplete_AlreadyComplete(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(0, 'b', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 0));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.isComplete());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.isStateInitial(0));
        Assert.assertTrue(a.isStateFinal(1));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'a', 0));
        Assert.assertTrue(a.hasTransition(0, 'b', 1));
        Assert.assertTrue(a.hasTransition(1, 'a', 1));
        Assert.assertTrue(a.hasTransition(1, 'b', 0));
        Assert.assertEquals(a.countTransitions(), 4);

        Automate b = Automate.createComplete(a);

        Assert.assertTrue(equivalent(a, b));
        Assert.assertTrue(b.isValid());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
    }

}