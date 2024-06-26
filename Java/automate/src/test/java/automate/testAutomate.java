package automate;

import java.util.HashSet;
import java.util.Set;

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
        Assert.assertTrue(a.addState(0));
        Assert.assertFalse(a.isValid());
        Assert.assertTrue(a.hasState(0));
    }

    @Test
    public void isValid_WithSymbolNoState(){
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertFalse(a.isValid());
        Assert.assertTrue(a.hasSymbol('a'));
    }

    @Test
    public void isValid_WithSymbolWithState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
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
        for(int i = 1; i < 256; ++i){
            if(Character.isDefined((char)i) && i >= 0x21 && i <= 0x7E){
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
            if(Character.isDefined(i) && !Character.isWhitespace(i)){
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
            if(Character.isDefined(i) && !Character.isWhitespace(i)){
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

    @Test
    public void createComplete_NotComplete(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'b', 3));
        a.setStateInitial(0);
        a.setStateFinal(3);
        Assert.assertFalse(a.isComplete());

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertTrue(a.hasState(3));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertTrue(a.hasSymbol('b'));
        Assert.assertEquals(a.countSymbols(), 2);
        Assert.assertTrue(a.hasTransition(0, 'b', 0));
        Assert.assertTrue(a.hasTransition(1, 'a', 1));
        Assert.assertTrue(a.hasTransition(2, 'a', 2));
        Assert.assertTrue(a.hasTransition(2, 'b', 3));
        Assert.assertEquals(a.countTransitions(), 4);

        Automate b = Automate.createComplete(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplete_NoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Assert.assertFalse(a.isComplete());

        Automate b = Automate.createComplete(a);

        Assert.assertTrue(b.isValid());
        Assert.assertTrue(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertEquals(b.countSymbols(), 1);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplete_WithMaxState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(Integer.MAX_VALUE));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(Integer.MAX_VALUE);
        Assert.assertTrue(a.addTransition(0, 'a', Integer.MAX_VALUE));

        Automate b = Automate.createComplete(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplete_MissingState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 0));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertFalse(a.isComplete());

        Automate b = Automate.createComplete(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertEquals(b.countSymbols(), 1);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplete_Flecy(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate b = Automate.createComplete(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplement_NotCompleteAndAlreadyDeterministic(){
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
        Assert.assertFalse(a.isComplete());
        Assert.assertTrue(a.isDeterministic());

        Automate b = Automate.createComplement(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplement_AlreadyCompleteAndAlreadyDeterministic(){
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
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'b', 2));
        Assert.assertTrue(a.isComplete());
        Assert.assertTrue(a.isDeterministic());

        Automate b = Automate.createComplement(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplement_NotCompleteAndNotDeterministic(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertFalse(a.isComplete());
        Assert.assertFalse(a.isDeterministic());

        Automate b = Automate.createComplement(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplement_NoInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateFinal(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertFalse(a.isComplete());
        Assert.assertFalse(a.isDeterministic());

        Automate b = Automate.createComplement(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createComplement_MultipleInitialState(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateInitial(1);
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

        Assert.assertFalse(a.isDeterministic());
        Assert.assertFalse(a.isComplete());

        Automate b = Automate.createComplement(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertTrue(b.isComplete());
        Assert.assertTrue(b.isDeterministic());
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, b));
    }

    @Test
    public void createMirror_Empty(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);

        Automate b = Automate.createMirror(a);

        Assert.assertTrue(b.isValid());
        Assert.assertTrue(b.isLanguageEmpty());
        Assert.assertEquals(b.countStates(), 1);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertEquals(b.countSymbols(), 1);
        Assert.assertEquals(b.countTransitions(), 0);
        Assert.assertTrue(equivalentMirror(a, b));
    }

    @Test
    public void createMirror_Mirror(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));

        Automate b = Automate.createMirror(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertEquals(b.countStates(), 2);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertEquals(b.countSymbols(), 1);
        Assert.assertEquals(b.countTransitions(), 1);
        Assert.assertTrue(equivalentMirror(a, b));
    }

    @Test
    public void createMirror_DeterministicAndNotCompleteWithMultipleFinalState(){
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
        Assert.assertFalse(a.isComplete());

        Automate b = Automate.createMirror(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertEquals(b.countStates(), 3);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertEquals(b.countTransitions(), 2);
        Assert.assertTrue(equivalentMirror(a, b));
    }

    @Test
    public void createMirror_DeterministicAndNotCompleteWithOneFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.isDeterministic());
        Assert.assertFalse(a.isComplete());

        Automate b = Automate.createMirror(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertEquals(b.countStates(), 3);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertEquals(b.countTransitions(), 2);
        Assert.assertTrue(equivalentMirror(a, b));
    }

    @Test
    public void createMirror_NotDeterministicAndComplete(){
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
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 2));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'b', 2));
        Assert.assertFalse(a.isDeterministic());
        Assert.assertTrue(a.isComplete());

        Automate b = Automate.createMirror(a);

        Assert.assertTrue(b.isValid());
        Assert.assertFalse(b.isLanguageEmpty());
        Assert.assertEquals(b.countStates(), 3);
        Assert.assertTrue(b.hasSymbol('a'));
        Assert.assertTrue(b.hasSymbol('b'));
        Assert.assertEquals(b.countSymbols(), 2);
        Assert.assertEquals(b.countTransitions(), 7);
        Assert.assertTrue(equivalentMirror(a, b));
    }

    @Test
    public void makeTransition_NoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Set<Integer> res = new HashSet<>(), attend = new HashSet<>();
        res = a.makeTransition(new HashSet<Integer>(), 'a');
        Assert.assertEquals(res, attend);
    }

    @Test
    public void makeTransition_NoState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Set<Integer> res = new HashSet<>(), attend = new HashSet<>(), origin = new HashSet<>();
        origin.add(2);
        res = a.makeTransition(origin, 'a');
        Assert.assertEquals(res, attend);
    }

    @Test
    public void makeTransition_NoSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Set<Integer> res = new HashSet<>(), attend = new HashSet<>(), origin = new HashSet<>();
        origin.add(0);
        res = a.makeTransition(origin, 'b');
        Assert.assertEquals(res, attend);
    }

    @Test
    public void makeTransition_OriginNullAndNoSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Set<Integer> res = new HashSet<>(), attend = new HashSet<>();
        res = a.makeTransition(new HashSet<Integer>(), 'b');
        Assert.assertEquals(res, attend);
    }

    @Test
    public void makeTransition_NoStateNoSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Set<Integer> res = new HashSet<>(), attend = new HashSet<>(), origin = new HashSet<>();
        origin.add(2);
        res = a.makeTransition(origin, 'b');
        Assert.assertEquals(res, attend);
    }

    @Test
    public void makeTransition_NoTransitionAndNoSymbol(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Set<Integer> res = new HashSet<>(), attend = new HashSet<>(), origin = new HashSet<>();
        origin.add(1);
        res = a.makeTransition(origin, 'b');
        Assert.assertEquals(res, attend);
    }

    @Test
    public void makeTransition_Success(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = new HashSet<>(), attend = new HashSet<>(), origin = new HashSet<>();
        origin.add(0); origin.add(1);
        attend.add(1); attend.add(2); attend.add(3);

        res = a.makeTransition(origin, 'a');
        Assert.assertFalse(res.isEmpty());
        Assert.assertEquals(res, attend);
    }

    @Test
    public void readString_NotInAlphabetWithFinalAndInitialState(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("abc");
        Assert.assertEquals(res, new HashSet<Integer>());
    }

    @Test
    public void readString_NotInAlphabetWithNoFinalAndInitialState(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("abc");
        Assert.assertEquals(res, new HashSet<Integer>());
    }

    @Test
    public void readString_NoStateInitial(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateFinal(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("a");
        Assert.assertEquals(res, new HashSet<Integer>());
    }

    @Test
    public void readString_NoStateFinal(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("a"), attend = new HashSet<>();
        attend.add(1); attend.add(2); attend.add(3);
        Assert.assertEquals(res, attend);
    }

    @Test
    public void readString_NoStateFinalAndNoStateInitial(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("a");
        Assert.assertEquals(res, new HashSet<Integer>());
    }

    @Test
    public void readString_ToShortWord(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("ab"), attend = new HashSet<>();
        attend.add(3); attend.add(4);
        Assert.assertEquals(res, attend);
    }

    @Test
    public void readString_ToLongWord(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Set<Integer> res = a.readString("abbaa");
        Assert.assertEquals(res, new HashSet<Integer>());
    }

    @Test
    public void readString_StringNullAccept(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));

        a.setStateInitial(0);
        a.setStateFinal(0);
        a.setStateFinal(1);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'b', 0));

        Set<Integer> res = a.readString(""), attend = new HashSet<>();
        attend.add(0);
        Assert.assertEquals(res, attend);
    }

    @Test
    public void match_NotInAlphabetWithFinalAndInitialState(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertFalse(a.match("abc"));
    }

    @Test
    public void match_NotInAlphabetWithNoFinalAndInitialState(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertFalse(a.match("abc"));
    }

    @Test
    public void match_NoStateInitial(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateFinal(1);
        a.setStateFinal(4);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertFalse(a.match("a"));
    }

    @Test
    public void match_NoStateFinal(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        a.setStateInitial(0);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertFalse(a.match("a"));
    }

    @Test
    public void match_NoStateFinalAndNoStateInitial(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertFalse(a.match("a"));
    }

    @Test
    public void match_ToShortWord(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));

        Assert.assertFalse(a.match("ab"));
    }

    @Test
    public void match_ToLongWord(){
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
        Assert.assertTrue(a.addTransition(2, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 4));

        Assert.assertFalse(a.match("abbaa"));
    }

    @Test
    public void match_StringNullAccept(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));

        a.setStateInitial(0);
        a.setStateFinal(0);
        a.setStateFinal(1);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'b', 0));

        Assert.assertTrue(a.match(""));
    }

    @Test
    public void match_StringNullNotAccept(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));

        a.setStateInitial(0);
        a.setStateFinal(1);

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'b', 0));

        Assert.assertFalse(a.match(""));
    }

    @Test
    public void isLanguageEmpty_NotEmpty(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(0);
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertFalse(a.isLanguageEmpty());
    }

    @Test
    public void isLanguageEmpty_NoFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.isLanguageEmpty());
    }

    @Test
    public void isLanguageEmpty_Empty(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Assert.assertTrue(a.isLanguageEmpty());
    }

    @Test
    public void isLanguageEmpty_MultipleInitialStateWithAcceptWord(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertFalse(a.isLanguageEmpty());
    }

    @Test
    public void isLanguageEmpty_NoInitial(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        a.setStateFinal(1);
        Assert.assertTrue(a.isLanguageEmpty());
    }

    @Test
    public void isLanguageEmpty_NotLinked(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(1, 'a', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        a.setStateInitial(0);
        a.setStateFinal(2);
        Assert.assertTrue(a.isLanguageEmpty());
    }

    @Test
    public void removeNonAccessibleStates_NoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 2));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 2);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonAccessibleStates_MultipleNoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(3, 'a', 3));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 2));
        Assert.assertFalse(a.hasTransition(3, 'a', 3));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonAccessibleStates_NoTransitionWithInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 2);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonAccessibleStates_MultipleNoTransitionWithInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        Assert.assertTrue(a.addTransition(3, 'a', 1));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 1));
        Assert.assertFalse(a.hasTransition(3, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonAccessibleStates_AllNoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        Assert.assertTrue(a.addTransition(3, 'a', 3));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertEquals(a.countTransitions(), 2);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 1));
        Assert.assertFalse(a.hasTransition(3, 'a', 3));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonAccessibleStates_NoInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 1);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removeNonAccessibleStates_AllAccessibleState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 3);
    }

    @Test
    public void removeNonAccessibleStates_InitialStateWithNoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        a.removeNonAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(1, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonCoAccessibleStates_NoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 2));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 2);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonCoAccessibleStates_MultipleNoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(3, 'a', 3));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(2, 'a', 2));
        Assert.assertFalse(a.hasTransition(3, 'a', 3));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonCoAccessibleStates_NoTransitionWithFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 2);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 2));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonCoAccessibleStates_MultipleNoTransitionWithFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 2));
        Assert.assertFalse(a.hasTransition(0, 'a', 3));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonCoAccessibleStates_AllNoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(3, 'a', 3));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 4);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertFalse(a.hasState(3));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertFalse(a.hasTransition(0, 'a', 2));
        Assert.assertFalse(a.hasTransition(3, 'a', 3));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void removeNonCoAccessibleStates_NoFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 2);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertFalse(a.hasState(2));
        Assert.assertEquals(a.countStates(), 2);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 0);
    }

    @Test
    public void removeNonCoAccessibleStates_AllCoAccessible(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateFinal(1);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));

        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertEquals(a.countTransitions(), 3);
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertEquals(a.countTransitions(), 2);
    }

    @Test
    public void removeNonCoAccessibleStates_FinalStateWithNoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(2);
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        a.removeNonCoAccessibleStates();

        Assert.assertTrue(a.isValid());
        Assert.assertFalse(a.isLanguageEmpty());
        Assert.assertTrue(a.hasState(0));
        Assert.assertTrue(a.hasState(1));
        Assert.assertTrue(a.hasState(2));
        Assert.assertEquals(a.countStates(), 3);
        Assert.assertTrue(a.hasSymbol('a'));
        Assert.assertEquals(a.countSymbols(), 1);
        Assert.assertTrue(a.hasTransition(0, 'a', 1));
        Assert.assertEquals(a.countTransitions(), 1);
    }

    @Test
    public void createIntersection_LhsDeterministicAndRhsDeterministic(){
        Automate lhs = new Automate(), rhs = new Automate();
        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        lhs.setStateInitial(0);
        lhs.setStateFinal(1);


        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertTrue(lhs.isDeterministic());
        Assert.assertTrue(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertTrue(equivalent(rhs, res));
        Assert.assertTrue(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNotDeterministicWithMultipleInitStateAndRhsDeterministic(){
        Automate lhs = new Automate(), rhs = new Automate();
        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addState(2));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 2));
        Assert.assertTrue(lhs.addTransition(2, 'b', 2));
        lhs.setStateInitial(0);
        lhs.setStateInitial(1);
        lhs.setStateFinal(2);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertTrue(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNotDeterministicWithMultipleInitStateAndRhsNotDeterministicWithMultipleInitState(){
        Automate lhs = new Automate(), rhs = new Automate();
        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addState(2));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 2));
        Assert.assertTrue(lhs.addTransition(1, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 2));
        Assert.assertTrue(lhs.addTransition(2, 'b', 2));
        lhs.setStateInitial(0);
        lhs.setStateInitial(1);
        lhs.setStateFinal(2);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addState(2));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 2));
        Assert.assertTrue(rhs.addTransition(1, 'a', 2));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        Assert.assertTrue(rhs.addTransition(2, 'a', 0));
        Assert.assertTrue(rhs.addTransition(2, 'b', 1));
        Assert.assertTrue(rhs.addTransition(2, 'b', 2));
        rhs.setStateInitial(0);
        rhs.setStateInitial(1);
        rhs.setStateFinal(2);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertFalse(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNotDeterministicWithMultipleInitFinalStateAndRhsDeterministic(){
        Automate lhs = new Automate(), rhs = new Automate();

        for(int i = 5; i < 5; ++i){
            Assert.assertTrue(lhs.addState(i));
        }
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 2));
        Assert.assertTrue(lhs.addTransition(1, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 3));
        Assert.assertTrue(lhs.addTransition(2, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'b', 4));
        lhs.setStateInitial(0);
        lhs.setStateInitial(1);
        lhs.setStateFinal(3);
        lhs.setStateFinal(4);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertTrue(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNotDeterministicWithMultipleInitFinalStateAndRhsNotDeterministicWithMultipleInitState(){
        Automate lhs = new Automate(), rhs = new Automate();

        for(int i = 5; i < 5; ++i){
            Assert.assertTrue(lhs.addState(i));
        }
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 2));
        Assert.assertTrue(lhs.addTransition(1, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 3));
        Assert.assertTrue(lhs.addTransition(2, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'b', 4));
        lhs.setStateInitial(0);
        lhs.setStateInitial(1);
        lhs.setStateFinal(3);
        lhs.setStateFinal(4);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addState(2));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 2));
        Assert.assertTrue(rhs.addTransition(1, 'a', 2));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        Assert.assertTrue(rhs.addTransition(2, 'a', 0));
        Assert.assertTrue(rhs.addTransition(2, 'b', 1));
        Assert.assertTrue(rhs.addTransition(2, 'b', 2));
        rhs.setStateInitial(0);
        rhs.setStateInitial(1);
        rhs.setStateFinal(2);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertFalse(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNotDeterministicWithMultipleInitFinalStateAndRhsNotDeterministicWithMultipleInitFinalState(){
        Automate lhs = new Automate(), rhs = new Automate();

        for(int i = 5; i < 5; ++i){
            Assert.assertTrue(lhs.addState(i));
        }
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 2));
        Assert.assertTrue(lhs.addTransition(1, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 2));
        Assert.assertTrue(lhs.addTransition(2, 'a', 3));
        Assert.assertTrue(lhs.addTransition(2, 'b', 2));
        Assert.assertTrue(lhs.addTransition(2, 'b', 4));
        lhs.setStateInitial(0);
        lhs.setStateInitial(1);
        lhs.setStateFinal(3);
        lhs.setStateFinal(4);

        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(rhs.addState(i));
        }
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 2));
        Assert.assertTrue(rhs.addTransition(1, 'a', 2));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        Assert.assertTrue(rhs.addTransition(2, 'a', 0));
        Assert.assertTrue(rhs.addTransition(2, 'a', 3));
        Assert.assertTrue(rhs.addTransition(2, 'b', 1));
        Assert.assertTrue(rhs.addTransition(2, 'b', 2));
        Assert.assertTrue(rhs.addTransition(2, 'b', 4));
        Assert.assertTrue(rhs.addTransition(3, 'a', 3));
        Assert.assertTrue(rhs.addTransition(3, 'b', 3));
        Assert.assertTrue(rhs.addTransition(4, 'a', 4));
        Assert.assertTrue(rhs.addTransition(4, 'b', 4));
        rhs.setStateInitial(0);
        rhs.setStateInitial(1);
        rhs.setStateFinal(3);
        rhs.setStateFinal(4);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertFalse(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_lhsNotDeterministicRhsDeterministic(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 0));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        lhs.setStateInitial(0);
        lhs.setStateFinal(1);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertTrue(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_lhsNotDeterministicRhsNotDeterministic(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 0));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        lhs.setStateInitial(0);
        lhs.setStateFinal(1);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'a', 1));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertFalse(lhs.isDeterministic());
        Assert.assertFalse(rhs.isDeterministic());

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsHaveMoreLetterThanRhs(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addState(2));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addSymbol('c'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        Assert.assertTrue(lhs.addTransition(1, 'c', 2));
        lhs.setStateInitial(0);
        lhs.setStateFinal(1);
        lhs.setStateFinal(2);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsAlphabetDifferentRhsAlphabetWithSameLetter(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addState(2));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addSymbol('c'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        Assert.assertTrue(lhs.addTransition(1, 'c', 2));
        lhs.setStateInitial(0);
        lhs.setStateFinal(1);
        lhs.setStateFinal(2);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addState(2));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addSymbol('d'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'd', 2));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);
        rhs.setStateFinal(2);

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_NotSameLetter(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        lhs.setStateInitial(0);
        lhs.setStateFinal(1);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('c'));
        Assert.assertTrue(rhs.addSymbol('d'));
        Assert.assertTrue(rhs.addTransition(0, 'c', 0));
        Assert.assertTrue(rhs.addTransition(0, 'd', 1));
        Assert.assertTrue(rhs.addTransition(1, 'c', 0));
        Assert.assertTrue(rhs.addTransition(1, 'd', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNoTransition(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        lhs.setStateInitial(0);
        lhs.setStateFinal(0);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 1));
        Assert.assertTrue(rhs.addTransition(1, 'b', 0));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void createIntersection_LhsNoTransitionAndRhsNoTransition(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        lhs.setStateInitial(0);
        lhs.setStateFinal(0);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Automate res = Automate.createIntersection(lhs, rhs);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertFalse(equivalent(rhs, res));
        Assert.assertFalse(equivalent(lhs, res));
    }

    @Test
    public void hasEmptyIntersectionWith_NoEmpty(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        lhs.setStateFinal(0);
        lhs.setStateFinal(1);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 0));
        Assert.assertTrue(rhs.addTransition(0, 'b', 1));
        Assert.assertTrue(rhs.addTransition(1, 'a', 0));
        Assert.assertTrue(rhs.addTransition(1, 'b', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertFalse(lhs.hasEmptyIntersectionWith(rhs));
    }

    @Test
    public void hasEmptyIntersectionWith_NotSameLetter(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addState(1));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        Assert.assertTrue(lhs.addTransition(0, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'a', 1));
        Assert.assertTrue(lhs.addTransition(1, 'b', 1));
        lhs.setStateFinal(0);
        lhs.setStateFinal(1);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('c'));
        Assert.assertTrue(rhs.addSymbol('d'));
        Assert.assertTrue(rhs.addTransition(0, 'c', 0));
        Assert.assertTrue(rhs.addTransition(0, 'd', 1));
        Assert.assertTrue(rhs.addTransition(1, 'c', 0));
        Assert.assertTrue(rhs.addTransition(1, 'd', 1));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertTrue(lhs.hasEmptyIntersectionWith(rhs));
    }

    @Test
    public void hasEmptyIntersectionWith_LhsNoTransitionAndRhsNoTransition(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        lhs.setStateInitial(0);
        lhs.setStateFinal(0);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertTrue(lhs.hasEmptyIntersectionWith(rhs));
    }

    @Test
    public void hasEmptyIntersectionWith_LhsNoTransition(){
        Automate lhs = new Automate(), rhs = new Automate();

        Assert.assertTrue(lhs.addState(0));
        Assert.assertTrue(lhs.addSymbol('a'));
        Assert.assertTrue(lhs.addSymbol('b'));
        lhs.setStateInitial(0);
        lhs.setStateFinal(0);

        Assert.assertTrue(rhs.addState(0));
        Assert.assertTrue(rhs.addState(1));
        Assert.assertTrue(rhs.addSymbol('a'));
        Assert.assertTrue(rhs.addSymbol('b'));
        Assert.assertTrue(rhs.addTransition(0, 'a', 1));
        Assert.assertTrue(rhs.addTransition(1, 'b', 0));
        rhs.setStateInitial(0);
        rhs.setStateFinal(1);

        Assert.assertTrue(lhs.hasEmptyIntersectionWith(rhs));
    }

    @Test
    public void createDeterministic_DS2022(){
        for(int i = 0; i < 7; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 5));
        Assert.assertTrue(a.addTransition(0, 'b', 4));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'b', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 0));
        Assert.assertTrue(a.addTransition(4, 'a', 3));
        Assert.assertTrue(a.addTransition(4, 'a', 4));
        Assert.assertTrue(a.addTransition(5, 'a', 5));
        Assert.assertTrue(a.addTransition(5, 'b', 4));
        a.setStateInitial(0);
        a.setStateFinal(3);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_exemplePDF(){
        for(int i = 0; i < 5; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(0, 'b', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.addTransition(1, 'b', 4));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'a', 3));
        Assert.assertTrue(a.addTransition(2, 'b', 1));
        Assert.assertTrue(a.addTransition(2, 'b', 2));
        Assert.assertTrue(a.addTransition(3, 'a', 4));
        Assert.assertTrue(a.addTransition(4, 'a', 4));
        Assert.assertTrue(a.addTransition(4, 'b', 4));
        a.setStateInitial(0);
        a.setStateFinal(4);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_AlreadyDeterministic(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        a.setStateInitial(0);
        a.setStateFinal(1);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_NoInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        a.setStateFinal(1);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_NoFinalState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        a.setStateInitial(0);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_MultipleInitialState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addState(4));
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
        a.setStateInitial(0);
        a.setStateInitial(1);
        a.setStateFinal(4);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_DS(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 2));
        Assert.assertTrue(a.addTransition(0, 'a', 3));
        Assert.assertTrue(a.addTransition(1, 'a', 4));
        Assert.assertTrue(a.addTransition(2, 'b', 5));
        Assert.assertTrue(a.addTransition(3, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'b', 1));
        Assert.assertTrue(a.addTransition(4, 'b', 2));
        Assert.assertTrue(a.addTransition(5, 'a', 2));
        Assert.assertTrue(a.addTransition(5, 'b', 4));
        a.setStateInitial(0);
        a.setStateInitial(5);
        a.setStateFinal(2);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_NoTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void createDeterministic_AlreadyMinimal(){
        for(int i = 0; i < 7; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.addTransition(0, 'c', 3));
        Assert.assertTrue(a.addTransition(1, 'a', 6));
        Assert.assertTrue(a.addTransition(1, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'c', 6));
        Assert.assertTrue(a.addTransition(2, 'a', 6));
        Assert.assertTrue(a.addTransition(2, 'b', 4));
        Assert.assertTrue(a.addTransition(2, 'c', 0));
        Assert.assertTrue(a.addTransition(3, 'a', 4));
        Assert.assertTrue(a.addTransition(3, 'b', 6));
        Assert.assertTrue(a.addTransition(3, 'c', 6));
        Assert.assertTrue(a.addTransition(4, 'a', 4));
        Assert.assertTrue(a.addTransition(4, 'b', 5));
        Assert.assertTrue(a.addTransition(4, 'c', 3));
        Assert.assertTrue(a.addTransition(5, 'a', 6));
        Assert.assertTrue(a.addTransition(5, 'b', 4));
        Assert.assertTrue(a.addTransition(5, 'c', 6));
        Assert.assertTrue(a.addTransition(6, 'a', 6));
        Assert.assertTrue(a.addTransition(6, 'b', 6));
        Assert.assertTrue(a.addTransition(6, 'c', 6));
        a.setStateInitial(0);
        a.setStateFinal(0);
        a.setStateFinal(4);

        Assert.assertTrue(a.isDeterministic());

        Automate res = Automate.createDeterministic(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(equivalent(a, res));
    }

    @Test
    public void isIncludedIn_Same(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        a.setStateInitial(0);
        a.setStateFinal(2);

        Assert.assertTrue(a.isIncludedIn(a));
    }

    @Test
    public void isIncludedIn_DifferentSymbols(){
        Automate a2 = new Automate();

        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        a.setStateInitial(0);
        a.setStateFinal(2);

        Assert.assertTrue(a2.addState(0));
        Assert.assertTrue(a2.addState(1));
        Assert.assertTrue(a2.addState(2));
        Assert.assertTrue(a2.addSymbol('b'));
        Assert.assertTrue(a2.addTransition(0, 'b', 1));
        Assert.assertTrue(a2.addTransition(1, 'b', 2));
        a2.setStateInitial(0);
        a2.setStateFinal(2);

        Assert.assertFalse(a.isIncludedIn(a2));
        Assert.assertFalse(a2.isIncludedIn(a));
    }

    @Test
    public void isIncludedIn_EmptyInEmpty(){
        Automate a2 = new Automate();

        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addSymbol('a'));

        Assert.assertTrue(a2.addState(0));
        Assert.assertTrue(a2.addSymbol('a'));

        Assert.assertTrue(a.isIncludedIn(a2));
        Assert.assertTrue(a2.isIncludedIn(a));
    }

    @Test
    public void isIncludedIn_Royer1(){
        Automate a2 = new Automate();

        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addSymbol('d'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        a.setStateInitial(0);
        a.setStateFinal(1);

        Assert.assertTrue(a2.addState(0));
        Assert.assertTrue(a2.addState(1));
        Assert.assertTrue(a2.addSymbol('a'));
        Assert.assertTrue(a2.addTransition(0, 'a', 1));
        a2.setStateInitial(0);
        a2.setStateFinal(1);

        Assert.assertTrue(a.isIncludedIn(a2));
    }

    @Test
    public void isIncludedIn_Royer2(){
        Automate a2 = new Automate();

        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addState(3));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addSymbol('d'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 2));
        Assert.assertTrue(a.addTransition(2, 'c', 3));
        Assert.assertTrue(a.addTransition(2, 'd', 3));
        a.setStateInitial(0);
        a.setStateFinal(3);

        Assert.assertTrue(a2.addState(0));
        Assert.assertTrue(a2.addState(1));
        Assert.assertTrue(a2.addSymbol('a'));
        Assert.assertTrue(a2.addTransition(0, 'a', 1));
        a2.setStateInitial(0);
        a2.setStateFinal(1);

        Assert.assertFalse(a.isIncludedIn(a2));
    }

    @Test
    public void createMinimalMoore_AlreadyCompleteAndAlreadyDeterministic(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

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
        a.setStateInitial(0);
        a.setStateFinal(3);
        a.setStateFinal(4);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 4);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_AlreadyMinimal_ex18_1TD(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 2));
        Assert.assertTrue(a.addTransition(0, 'b', 4));
        Assert.assertTrue(a.addTransition(0, 'c', 5));
        Assert.assertTrue(a.addTransition(1, 'b', 0));
        Assert.assertTrue(a.addTransition(2, 'c', 0));
        Assert.assertTrue(a.addTransition(3, 'b', 4));
        Assert.assertTrue(a.addTransition(3, 'c', 5));
        Assert.assertTrue(a.addTransition(4, 'b', 3));
        Assert.assertTrue(a.addTransition(5, 'a', 3));
        a.setStateInitial(0);
        a.setStateFinal(0);
        a.setStateFinal(3);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 7);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NoInitialState(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

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
        a.setStateFinal(3);
        a.setStateFinal(4);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NonAccessibleState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertEquals(res.countSymbols(), 1);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NonAccessibleStatesWithTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertEquals(res.countSymbols(), 1);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NonAccessibleStatesWithTransitionNonDeterminist(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_AlreadyMinimal1(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 19));
        Assert.assertTrue(a.addTransition(16, 'a', 19));
        Assert.assertTrue(a.addTransition(16, 'b', 22));
        Assert.assertTrue(a.addTransition(19, 'a', 16));
        Assert.assertTrue(a.addTransition(19, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        a.setStateInitial(7);
        a.setStateFinal(19);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 6);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_AlreadyMinimal2(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('0'));
        Assert.assertTrue(a.addSymbol('1'));
        Assert.assertTrue(a.addTransition(7, '0', 10));
        Assert.assertTrue(a.addTransition(7, '1', 13));
        Assert.assertTrue(a.addTransition(10, '0', 19));
        Assert.assertTrue(a.addTransition(10, '1', 22));
        Assert.assertTrue(a.addTransition(13, '0', 7));
        Assert.assertTrue(a.addTransition(13, '1', 7));
        Assert.assertTrue(a.addTransition(16, '0', 22));
        Assert.assertTrue(a.addTransition(16, '1', 19));
        Assert.assertTrue(a.addTransition(19, '0', 16));
        Assert.assertTrue(a.addTransition(19, '1', 22));
        Assert.assertTrue(a.addTransition(22, '0', 16));
        Assert.assertTrue(a.addTransition(22, '1', 19));
        a.setStateInitial(7);
        a.setStateFinal(22);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 6);
        Assert.assertTrue(res.hasSymbol('0'));
        Assert.assertTrue(res.hasSymbol('1'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NotMinimal1(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 19));
        Assert.assertTrue(a.addTransition(16, 'a', 19));
        Assert.assertTrue(a.addTransition(16, 'b', 22));
        Assert.assertTrue(a.addTransition(19, 'a', 16));
        Assert.assertTrue(a.addTransition(19, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        a.setStateInitial(7);
        a.setStateFinal(16);
        a.setStateFinal(19);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 4);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NotMinimal2(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 16));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 13));
        Assert.assertTrue(a.addTransition(13, 'b', 13));
        Assert.assertTrue(a.addTransition(16, 'a', 16));
        Assert.assertTrue(a.addTransition(16, 'b', 16));
        a.setStateInitial(7);
        a.setStateFinal(10);
        a.setStateFinal(16);


        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NotMinimal3(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 22));
        Assert.assertTrue(a.addTransition(7, 'b', 10));
        Assert.assertTrue(a.addTransition(10, 'a', 19));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 13));
        Assert.assertTrue(a.addTransition(13, 'b', 22));
        Assert.assertTrue(a.addTransition(16, 'a', 16));
        Assert.assertTrue(a.addTransition(16, 'b', 7));
        Assert.assertTrue(a.addTransition(19, 'a', 10));
        Assert.assertTrue(a.addTransition(19, 'b', 13));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 19));
        a.setStateInitial(7);
        a.setStateFinal(7);
        a.setStateFinal(22);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NotMinimal4Buronfosse(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addState(25));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 19));
        Assert.assertTrue(a.addTransition(16, 'a', 19));
        Assert.assertTrue(a.addTransition(16, 'b', 22));
        Assert.assertTrue(a.addTransition(19, 'a', 16));
        Assert.assertTrue(a.addTransition(19, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        Assert.assertTrue(a.addTransition(25, 'a', 16));
        Assert.assertTrue(a.addTransition(25, 'b', 22));
        a.setStateInitial(7);
        a.setStateFinal(16);
        a.setStateFinal(19);
        a.setStateFinal(25);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 4);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NotMinimal5Pirolley(){
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addState(25));
        Assert.assertTrue(a.addState(28));
        Assert.assertTrue(a.addState(31));
        Assert.assertTrue(a.addState(34));
        Assert.assertTrue(a.addState(37));
        Assert.assertTrue(a.addState(40));
        Assert.assertTrue(a.addState(43));
        Assert.assertTrue(a.addState(46));
        Assert.assertTrue(a.addState(49));
        Assert.assertTrue(a.addState(52));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(13, 'c', 22));
        Assert.assertTrue(a.addTransition(16, 'b', 19));
        Assert.assertTrue(a.addTransition(19, 'c', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'c', 22));
        Assert.assertTrue(a.addTransition(22, 'c', 25));
        Assert.assertTrue(a.addTransition(22, 'c', 31));
        Assert.assertTrue(a.addTransition(25, 'a', 28));
        Assert.assertTrue(a.addTransition(31, 'b', 34));
        Assert.assertTrue(a.addTransition(37, 'a', 43));
        Assert.assertTrue(a.addTransition(40, 'b', 43));
        Assert.assertTrue(a.addTransition(43, 'c', 46));
        Assert.assertTrue(a.addTransition(46, 'a', 49));
        Assert.assertTrue(a.addTransition(46, 'b', 52));
        a.setStateInitial(10);
        a.setStateInitial(16);
        a.setStateInitial(37);
        a.setStateInitial(40);
        a.setStateFinal(28);
        a.setStateFinal(34);
        a.setStateFinal(49);
        a.setStateFinal(52);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 6);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_EmptyRolletViprey(){
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_Leonard(){
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 16));
        Assert.assertTrue(a.addTransition(16, 'a', 10));
        a.setStateInitial(10);
        a.setStateFinal(10);
        a.setStateFinal(13);
        a.setStateFinal(16);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 2);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_Gasca(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 10));
        Assert.assertTrue(a.addTransition(7, 'c', 16));
        Assert.assertTrue(a.addTransition(10, 'a', 10));
        Assert.assertTrue(a.addTransition(10, 'b', 10));
        Assert.assertTrue(a.addTransition(10, 'c', 10));
        Assert.assertTrue(a.addTransition(13, 'a', 7));
        Assert.assertTrue(a.addTransition(13, 'b', 13));
        Assert.assertTrue(a.addTransition(13, 'c', 16));
        Assert.assertTrue(a.addTransition(16, 'a', 16));
        Assert.assertTrue(a.addTransition(16, 'b', 16));
        Assert.assertTrue(a.addTransition(16, 'c', 16));
        a.setStateInitial(7);
        a.setStateFinal(10);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NonAccessibleStateGasca(){
        for(int i = 0; i < 4; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 1));
        Assert.assertTrue(a.addTransition(0, 'c', 3));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        Assert.assertTrue(a.addTransition(1, 'c', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 0));
        Assert.assertTrue(a.addTransition(2, 'b', 2));
        Assert.assertTrue(a.addTransition(2, 'c', 3));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 3));
        Assert.assertTrue(a.addTransition(3, 'c', 3));
        a.setStateInitial(0);
        a.setStateFinal(1);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalMoore_NonAccessibleStatePereiraGehant(){
        for(int i = 0; i < 4; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addSymbol('d'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(0, 'c', 0));
        a.setStateInitial(1);
        a.setStateFinal(0);
        a.setStateFinal(1);

        Automate res = Automate.createMinimalMoore(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 2);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertTrue(res.hasSymbol('d'));
        Assert.assertEquals(res.countSymbols(), 4);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_ExempleCours(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

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
        a.setStateInitial(0);
        a.setStateFinal(3);
        a.setStateFinal(4);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 4);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NoInitialState(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

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
        a.setStateFinal(3);
        a.setStateFinal(4);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NoFinalState(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

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
        a.setStateInitial(0);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_DS2022ExoDeter(){
        for(int i = 0; i < 6; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'a', 5));
        Assert.assertTrue(a.addTransition(0, 'b', 4));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 3));
        Assert.assertTrue(a.addTransition(2, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'b', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 0));
        Assert.assertTrue(a.addTransition(4, 'a', 3));
        Assert.assertTrue(a.addTransition(4, 'a', 4));
        Assert.assertTrue(a.addTransition(5, 'a', 5));
        Assert.assertTrue(a.addTransition(5, 'b', 4));
        a.setStateInitial(0);
        a.setStateFinal(3);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 15);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NonAccessibleState(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertEquals(res.countSymbols(), 1);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NonAccessibleStatesWithTransition(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertEquals(res.countSymbols(), 1);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NonAccessibleStatesWithTransitionNonDeterminist(){
        Assert.assertTrue(a.addState(0));
        Assert.assertTrue(a.addState(1));
        Assert.assertTrue(a.addState(2));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(1, 'a', 2));
        Assert.assertTrue(a.addTransition(2, 'a', 1));
        a.setStateInitial(0);
        a.setStateFinal(0);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_AlreadyMinimal1(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 19));
        Assert.assertTrue(a.addTransition(16, 'a', 19));
        Assert.assertTrue(a.addTransition(16, 'b', 22));
        Assert.assertTrue(a.addTransition(19, 'a', 16));
        Assert.assertTrue(a.addTransition(19, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        a.setStateInitial(7);
        a.setStateFinal(19);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 6);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_AlreadyMinimal2(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('0'));
        Assert.assertTrue(a.addSymbol('1'));
        Assert.assertTrue(a.addTransition(7, '0', 10));
        Assert.assertTrue(a.addTransition(7, '1', 13));
        Assert.assertTrue(a.addTransition(10, '0', 19));
        Assert.assertTrue(a.addTransition(10, '1', 22));
        Assert.assertTrue(a.addTransition(13, '0', 7));
        Assert.assertTrue(a.addTransition(13, '1', 7));
        Assert.assertTrue(a.addTransition(16, '0', 22));
        Assert.assertTrue(a.addTransition(16, '1', 19));
        Assert.assertTrue(a.addTransition(19, '0', 16));
        Assert.assertTrue(a.addTransition(19, '1', 22));
        Assert.assertTrue(a.addTransition(22, '0', 16));
        Assert.assertTrue(a.addTransition(22, '1', 19));
        a.setStateInitial(7);
        a.setStateFinal(22);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 6);
        Assert.assertTrue(res.hasSymbol('0'));
        Assert.assertTrue(res.hasSymbol('1'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NotMinimal1(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 19));
        Assert.assertTrue(a.addTransition(16, 'a', 19));
        Assert.assertTrue(a.addTransition(16, 'b', 22));
        Assert.assertTrue(a.addTransition(19, 'a', 16));
        Assert.assertTrue(a.addTransition(19, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        a.setStateInitial(7);
        a.setStateFinal(16);
        a.setStateFinal(19);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 4);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NotMinimal2(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 16));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 13));
        Assert.assertTrue(a.addTransition(13, 'b', 13));
        Assert.assertTrue(a.addTransition(16, 'a', 16));
        Assert.assertTrue(a.addTransition(16, 'b', 16));
        a.setStateInitial(7);
        a.setStateFinal(10);
        a.setStateFinal(16);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NotMinimal3(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 22));
        Assert.assertTrue(a.addTransition(7, 'b', 10));
        Assert.assertTrue(a.addTransition(10, 'a', 19));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 13));
        Assert.assertTrue(a.addTransition(13, 'b', 22));
        Assert.assertTrue(a.addTransition(16, 'a', 16));
        Assert.assertTrue(a.addTransition(16, 'b', 7));
        Assert.assertTrue(a.addTransition(19, 'a', 10));
        Assert.assertTrue(a.addTransition(19, 'b', 13));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 19));
        a.setStateInitial(7);
        a.setStateFinal(7);
        a.setStateFinal(22);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NotMinimal4Buronfosse(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addState(25));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 13));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(10, 'b', 16));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 19));
        Assert.assertTrue(a.addTransition(16, 'a', 19));
        Assert.assertTrue(a.addTransition(16, 'b', 22));
        Assert.assertTrue(a.addTransition(19, 'a', 16));
        Assert.assertTrue(a.addTransition(19, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        Assert.assertTrue(a.addTransition(25, 'a', 16));
        Assert.assertTrue(a.addTransition(25, 'b', 22));
        a.setStateInitial(7);
        a.setStateFinal(16);
        a.setStateFinal(19);
        a.setStateFinal(25);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 4);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NotMinimal5Pirolley(){
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addState(19));
        Assert.assertTrue(a.addState(22));
        Assert.assertTrue(a.addState(25));
        Assert.assertTrue(a.addState(28));
        Assert.assertTrue(a.addState(31));
        Assert.assertTrue(a.addState(34));
        Assert.assertTrue(a.addState(37));
        Assert.assertTrue(a.addState(40));
        Assert.assertTrue(a.addState(43));
        Assert.assertTrue(a.addState(46));
        Assert.assertTrue(a.addState(49));
        Assert.assertTrue(a.addState(52));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(10, 'a', 13));
        Assert.assertTrue(a.addTransition(13, 'c', 22));
        Assert.assertTrue(a.addTransition(16, 'b', 19));
        Assert.assertTrue(a.addTransition(19, 'c', 22));
        Assert.assertTrue(a.addTransition(22, 'a', 22));
        Assert.assertTrue(a.addTransition(22, 'b', 22));
        Assert.assertTrue(a.addTransition(22, 'c', 22));
        Assert.assertTrue(a.addTransition(22, 'c', 25));
        Assert.assertTrue(a.addTransition(22, 'c', 31));
        Assert.assertTrue(a.addTransition(25, 'a', 28));
        Assert.assertTrue(a.addTransition(31, 'b', 34));
        Assert.assertTrue(a.addTransition(37, 'a', 43));
        Assert.assertTrue(a.addTransition(40, 'b', 43));
        Assert.assertTrue(a.addTransition(43, 'c', 46));
        Assert.assertTrue(a.addTransition(46, 'a', 49));
        Assert.assertTrue(a.addTransition(46, 'b', 52));
        a.setStateInitial(10);
        a.setStateInitial(16);
        a.setStateInitial(37);
        a.setStateInitial(40);
        a.setStateFinal(28);
        a.setStateFinal(34);
        a.setStateFinal(49);
        a.setStateFinal(52);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 6);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_EmptyRolletViprey(){
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertTrue(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 1);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_Leonard(){
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addTransition(13, 'a', 10));
        Assert.assertTrue(a.addTransition(13, 'b', 16));
        Assert.assertTrue(a.addTransition(16, 'a', 10));
        a.setStateInitial(10);
        a.setStateFinal(10);
        a.setStateFinal(13);
        a.setStateFinal(16);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 2);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertEquals(res.countSymbols(), 2);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_Gasca(){
        Assert.assertTrue(a.addState(7));
        Assert.assertTrue(a.addState(10));
        Assert.assertTrue(a.addState(13));
        Assert.assertTrue(a.addState(16));
        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(7, 'a', 10));
        Assert.assertTrue(a.addTransition(7, 'b', 10));
        Assert.assertTrue(a.addTransition(7, 'c', 16));
        Assert.assertTrue(a.addTransition(10, 'a', 10));
        Assert.assertTrue(a.addTransition(10, 'b', 10));
        Assert.assertTrue(a.addTransition(10, 'c', 10));
        Assert.assertTrue(a.addTransition(13, 'a', 7));
        Assert.assertTrue(a.addTransition(13, 'b', 13));
        Assert.assertTrue(a.addTransition(13, 'c', 16));
        Assert.assertTrue(a.addTransition(16, 'a', 16));
        Assert.assertTrue(a.addTransition(16, 'b', 16));
        Assert.assertTrue(a.addTransition(16, 'c', 16));
        a.setStateInitial(7);
        a.setStateFinal(10);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NonAccessibleStateGasca(){
        for(int i = 0; i < 4; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addTransition(0, 'a', 1));
        Assert.assertTrue(a.addTransition(0, 'b', 1));
        Assert.assertTrue(a.addTransition(0, 'c', 3));
        Assert.assertTrue(a.addTransition(1, 'a', 1));
        Assert.assertTrue(a.addTransition(1, 'b', 1));
        Assert.assertTrue(a.addTransition(1, 'c', 1));
        Assert.assertTrue(a.addTransition(2, 'a', 0));
        Assert.assertTrue(a.addTransition(2, 'b', 2));
        Assert.assertTrue(a.addTransition(2, 'c', 3));
        Assert.assertTrue(a.addTransition(3, 'a', 3));
        Assert.assertTrue(a.addTransition(3, 'b', 3));
        Assert.assertTrue(a.addTransition(3, 'c', 3));
        a.setStateInitial(0);
        a.setStateFinal(1);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 3);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertEquals(res.countSymbols(), 3);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }

    @Test
    public void createMinimalBrzozowski_NonAccessibleStatePereiraGehant(){
        for(int i = 0; i < 4; ++i){
            Assert.assertTrue(a.addState(i));
        }

        Assert.assertTrue(a.addSymbol('a'));
        Assert.assertTrue(a.addSymbol('b'));
        Assert.assertTrue(a.addSymbol('c'));
        Assert.assertTrue(a.addSymbol('d'));
        Assert.assertTrue(a.addTransition(0, 'a', 0));
        Assert.assertTrue(a.addTransition(0, 'b', 0));
        Assert.assertTrue(a.addTransition(0, 'c', 0));
        a.setStateInitial(1);
        a.setStateFinal(0);
        a.setStateFinal(1);

        Automate res = Automate.createMinimalBrzozowski(a);

        Assert.assertTrue(res.isValid());
        Assert.assertFalse(res.isLanguageEmpty());
        Assert.assertEquals(res.countStates(), 2);
        Assert.assertTrue(res.hasSymbol('a'));
        Assert.assertTrue(res.hasSymbol('b'));
        Assert.assertTrue(res.hasSymbol('c'));
        Assert.assertTrue(res.hasSymbol('d'));
        Assert.assertEquals(res.countSymbols(), 4);
        Assert.assertTrue(res.isDeterministic());
        Assert.assertTrue(res.isComplete());
        Assert.assertTrue(equivalent(res, a));
    }


}