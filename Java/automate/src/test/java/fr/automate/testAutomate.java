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

}