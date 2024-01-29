require 'rspec'
require 'set'
require_relative '../lib/Automaton.rb'
require_relative './Automaton_spec_helper.rb'

INT_MAX = (2**(0.size * 8 -2) -1)
INT_MIN = -INT_MAX - 1

RSpec.describe Automaton do
    before(:each) do
        @a = Automaton.new
    end

    describe "isValid" do
        it "noSymbol and noState" do
            expect(@a.isValid()).to eq(false)
        end
    
        it "withSymbol and noState" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.isValid()).to eq(false)
            expect(@a.hasSymbol("a")).to eq(true)
        end

        it "noSymbol and withState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.isValid()).to eq(false)
            expect(@a.hasState(0)).to eq(true)
        end

        it "withSymbol and withState" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addState(0)).to eq(true)
            expect(@a.isValid()).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasState(0)).to eq(true)
        end
    end

    describe "addSymbol" do
        it "Epsilon" do
            expect(@a.addSymbol(Automaton::Epsilon)).to eq(false)
        end
    
        it "Space" do
            expect(@a.addSymbol(" ")).to eq(false)
        end
    
        it "isNotGraph" do
            expect(@a.addSymbol("\n")).to eq(false)
        end
    
        it "isGraph" do
            count = 0
            256.times do |i|
                c = i.chr(Encoding::UTF_8)
                if c.isgraph()
                    count += 1
                    expect(@a.addSymbol(c)).to eq(true)
                    expect(@a.hasSymbol(c)).to eq(true)
                    expect(@a.countSymbols()).to eq(count)
                else
                    expect(@a.addSymbol(c)).to eq(false)
                    expect(@a.hasSymbol(c)).to eq(false)
                    expect(@a.countSymbols()).to eq(count)
                end
            end
            expect(@a.countSymbols()).to eq(189)
        end
   
        it "oneSymbol" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
        end
    
        it "twoSymbol" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.addSymbol("b")).to eq(true)
            expect(@a.hasSymbol("b")).to eq(true)
            expect(@a.countSymbols()).to eq(2)
        end
    
        it "twoSameSymbol" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addSymbol("a")).to eq(false)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
        end
    end

    describe "removeSymbol" do
        it "oneSymbol" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.removeSymbol("a")).to eq(true)
            expect(@a.hasSymbol("a")).to eq(false)
            expect(@a.countSymbols()).to eq(0)
        end
    
        it "Empty" do
            expect(@a.removeSymbol("a")).to eq(false)
        end
    
        it "notInAlphabet" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.removeSymbol("b")).to eq(false)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasSymbol("b")).to eq(false)
            expect(@a.countSymbols()).to eq(1)
        end
    
        it "allCharacter" do
            count = 0
            256.times do |i|
                c = i.chr(Encoding::UTF_8)
                if c.isgraph()
                    count += 1
                    expect(@a.addSymbol(c)).to eq(true)
                    expect(@a.hasSymbol(c)).to eq(true)
                    expect(@a.countSymbols()).to eq(count)
                else
                    expect(@a.addSymbol(c)).to eq(false)
                    expect(@a.hasSymbol(c)).to eq(false)
                    expect(@a.countSymbols()).to eq(count)
                end
            end

            expect(@a.countSymbols()).to eq(189)

            256.times do |i|
                c = i.chr(Encoding::UTF_8)
                if c.isgraph()
                    count -= 1
                    expect(@a.removeSymbol(c)).to eq(true)
                    expect(@a.hasSymbol(c)).to eq(false)
                    expect(@a.countSymbols()).to eq(count)
                else
                    expect(@a.removeSymbol(c)).to eq(false)
                    expect(@a.hasSymbol(c)).to eq(false)
                    expect(@a.countSymbols()).to eq(count)
                end
            end

            expect(@a.countSymbols()).to eq(0)
        end
    
        it "symbolInTransition" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)
            expect(@a.removeSymbol("a")).to eq(true)
            expect(@a.hasSymbol("a")).to eq(false)
            expect(@a.countSymbols()).to eq(0)
        end
    end

    describe "hasSymbol" do
        it "Empty" do
            expect(@a.hasSymbol("a")).to eq(false)
        end
    
        it "Succes" do
            7.times do |i|
                a = i + 'a'.ord
                expect(@a.addSymbol(a.to_s)).to eq(true)
                expect(@a.hasSymbol(a.to_s)).to eq(true)
            end
        end
    
        it "notIsGraph" do
            7.times do |i|
                a = i + 'a'.ord
                expect(@a.addSymbol(a.to_s)).to eq(true)
                expect(@a.hasSymbol(a.to_s)).to eq(true)
            end
            expect(@a.hasSymbol("\n")).to eq(false)
        end
    end

    describe "countSymbols" do
        it "Empty" do
            expect(@a.countSymbols()).to eq(0)
        end

        it "Full" do
            7.times do |i|
                a = i + 'a'.ord
                expect(@a.addSymbol(a.to_s)).to eq(true)
                expect(@a.hasSymbol(a.to_s)).to eq(true)
            end
            expect(@a.countSymbols()).to eq(7)
        end
    end

    describe "addState" do
        it "oneState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end

        it "twoSameState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(0)).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end

        it "twoState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
        end

        it "Negative" do
            expect(@a.addState(-1)).to eq(false)
            expect(@a.hasState(-1)).to eq(false)
            expect(@a.countStates()).to eq(0)
        end

        it "MAX" do
            expect(@a.addState(INT_MAX)).to eq(true)
            expect(@a.hasState(INT_MAX)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end

        it "MIN" do
            expect(@a.addState(INT_MIN)).to eq(false)
            expect(@a.hasState(INT_MIN)).to eq(false)
            expect(@a.countStates()).to eq(0)
        end
    end

    describe "removeState" do
        it "Empty" do
            expect(@a.removeState(0)).to eq(false)
            expect(@a.hasState(0)).to eq(false)
            expect(@a.countStates()).to eq(0)
        end

        it "UnknownState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.removeState(1)).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(false)
            expect(@a.countStates()).to eq(1)
        end

        it "OneState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.removeState(0)).to eq(true)
            expect(@a.hasState(0)).to eq(false)
            expect(@a.countStates()).to eq(0)
        end

        it "AllStates" do
            count = 0
            10.times do |i|
                expect(@a.addState(i)).to eq(true)
                expect(@a.hasState(i)).to eq(true)
                count += 1
                expect(@a.countStates()).to eq(count)
            end

            10.times do |i|
                expect(@a.removeState(i)).to eq(true)
                expect(@a.hasState(i)).to eq(false)
                count -= 1
                expect(@a.countStates()).to eq(count)
            end
        end

        it "OriginInTransition" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)
            expect(@a.removeState(0)).to eq(true)
            expect(@a.hasState(0)).to eq(false)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(1)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(false)
            expect(@a.countTransitions()).to eq(0)
        end

        it "DestinationInTransition" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)
            expect(@a.removeState(1)).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(false)
            expect(@a.countStates()).to eq(1)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(false)
            expect(@a.countTransitions()).to eq(0)
        end

        it "OriginAndDestinationInTransition" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)
            expect(@a.removeState(0)).to eq(true)
            expect(@a.removeState(1)).to eq(true)
            expect(@a.hasState(0)).to eq(false)
            expect(@a.hasState(1)).to eq(false)
            expect(@a.countStates()).to eq(0)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(false)
            expect(@a.countTransitions()).to eq(0)
        end
    end

    describe "hasState" do
        it "Empty" do
            expect(@a.hasState(0)).to eq(false)
        end

        it "AlreadyIn" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.hasState(0)).to eq(true)
        end

        it "NotIn" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(false)
        end
    end

    describe "countStates" do
        it "Empty" do
            expect(@a.countStates()).to eq(0)
        end

        it "NotEmpty" do
            4.times do |i|
                expect(@a.addState(i)).to eq(true)
                expect(@a.hasState(i)).to eq(true)
            end
            expect(@a.countStates()).to eq(4)
        end
    end

    describe "setInitialState" do
        it "oneInitialState" do
            expect(@a.addState(0)).to eq(true)
            @a.setInitialState(0)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.isInitialState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end

        it "ToFinalAndInitial" do
            expect(@a.addState(0)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(0)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.isInitialState(0)).to eq(true)
            expect(@a.isFinalState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end

        it "twoInitialStates" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            @a.setInitialState(0)
            @a.setInitialState(1)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.isInitialState(0)).to eq(true)
            expect(@a.isInitialState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
        end

        it "UnknownState" do
            expect(@a.addState(0)).to eq(true)
            @a.setInitialState(1)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(false)
            expect(@a.isInitialState(0)).to eq(false)
            expect(@a.isInitialState(1)).to eq(false)
            expect(@a.countStates()).to eq(1)
        end
    end

    describe "setFinalState" do
        it "oneFinalState" do
            expect(@a.addState(0)).to eq(true)
            @a.setFinalState(0)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.isFinalState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end

        it "twoFinalStates" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            @a.setFinalState(0)
            @a.setFinalState(1)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.isFinalState(0)).to eq(true)
            expect(@a.isFinalState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
        end

        it "UnknownState" do
            expect(@a.addState(0)).to eq(true)
            @a.setFinalState(1)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(false)
            expect(@a.isFinalState(0)).to eq(false)
            expect(@a.isFinalState(1)).to eq(false)
            expect(@a.countStates()).to eq(1)
        end

        it "ToFinalAndInitial" do
            expect(@a.addState(0)).to eq(true)
            @a.setFinalState(0)
            @a.setInitialState(0)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.isFinalState(0)).to eq(true)
            expect(@a.isInitialState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
        end
    end

    describe "addTransition" do
        it "unknownSymbol" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(false)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol("a")).to eq(false)
            expect(@a.hasTransition(0, "a", 1)).to eq(false)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(0)
            expect(@a.countTransitions()).to eq(0)
        end

        it "unknownOrigin" do
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(false)

            expect(@a.hasState(0)).to eq(false)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasTransition(0, "a", 1)).to eq(false)
            expect(@a.countStates()).to eq(1)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(0)
        end

        it "unknownTarger" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(false)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(false)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasTransition(0, "a", 1)).to eq(false)
            expect(@a.countStates()).to eq(1)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(0)
        end

        it "oneTransition" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasTransition(0, "a", 1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(1)
        end

        it "twoSameTransition" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(false)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasTransition(0, "a", 1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(1)
        end

        it "sameOriginAndLetter" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addTransition(0, "a", 1)).to eq(true)
            expect(@a.addTransition(0, "a", 2)).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasTransition(0, "a", 1)).to eq(true)
            expect(@a.hasTransition(0, "a", 2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(2)
        end

        it "SameOriginAndTarget" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 1)).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasSymbol('b')).to eq(true)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(0, 'b', 1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(2)
            expect(@a.countTransitions()).to eq(2)
        end

        it "SameLetterAndTarget" do
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(1, 'a', 2)).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasTransition(0, 'a', 2)).to eq(true)
            expect(@a.hasTransition(1, 'a', 2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(2)
        end

        it "Epsilon" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addTransition(0, Automaton::Epsilon, 1)).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol(Automaton::Epsilon)).to eq(false)
            expect(@a.hasTransition(0, Automaton::Epsilon, 1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(0)
            expect(@a.countTransitions()).to eq(1)
        end
    end

    describe "prettyPrint" do
        it "test" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            expect(@a.addState(3)).to eq(true)
            expect(@a.addState(4)).to eq(true)
            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'b', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)
            @a.prettyPrint("prettyPrint.txt")
        end
    end

    describe "hasEpsilonTransition" do
        it "WithoutEpsilon" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.hasEpsilonTransition()).to eq(false)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(0)
        end

        it "WithEpsilon" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, Automaton::Epsilon, 2)).to eq(true)
            expect(@a.hasEpsilonTransition()).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(1, Automaton::Epsilon, 2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(2)
        end

        it "Twice" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, Automaton::Epsilon, 1)).to eq(true)
            expect(@a.addTransition(1, Automaton::Epsilon, 2)).to eq(true)
            expect(@a.hasEpsilonTransition()).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasTransition(0, Automaton::Epsilon, 1)).to eq(true)
            expect(@a.hasTransition(1, Automaton::Epsilon, 2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(2)
        end

        it "AddAndRemove" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, Automaton::Epsilon, 1)).to eq(true)
            expect(@a.hasEpsilonTransition()).to eq(true)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasTransition(0, Automaton::Epsilon, 1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(1)

            expect(@a.removeTransition(0, Automaton::Epsilon, 1)).to eq(true)
            expect(@a.hasEpsilonTransition()).to eq(false)

            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasTransition(0, Automaton::Epsilon, 1)).to eq(false)
            expect(@a.countStates()).to eq(2)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(0)
        end
    end

    describe "isDeterministic" do
        it "noTransition" do
            expect(@a.addState(0)).to eq(true)
            @a.setInitialState(0)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.isDeterministic()).to eq(true)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countStates()).to eq(1)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(0)
        end

        it "twoTransitions" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.isDeterministic()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(false)
            expect(@a.match("a")).to eq(true)
            expect(@a.match("")).to eq(false)
            expect(@a.match("aa")).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(0, 'a', 2)).to eq(true)
            expect(@a.countTransitions()).to eq(2)
        end

        it "ZeroInitialState" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            @a.setFinalState(1)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.isDeterministic()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.countTransitions()).to eq(1)
        end

        it "NoFinalState" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            @a.setInitialState(0)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.isDeterministic()).to eq(true)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.countTransitions()).to eq(1)
        end

        it "twoInitialStates" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'a', 2)).to eq(true)
            expect(@a.isDeterministic()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(false)
            expect(@a.match("")).to eq(true)
            expect(@a.match("a")).to eq(true)
            expect(@a.match("aa")).to eq(true)
            expect(@a.match("aaa")).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(1, 'a', 2)).to eq(true)
            expect(@a.countTransitions()).to eq(2)
        end

        it "alreadyDeterministic" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addState(2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 2)).to eq(true)
            expect(@a.isDeterministic()).to eq(true)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(false)
            expect(@a.match("")).to eq(false)
            expect(@a.match("a")).to eq(true)
            expect(@a.match("b")).to eq(true)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.hasState(2)).to eq(true)
            expect(@a.countStates()).to eq(3)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasSymbol('b')).to eq(true)
            expect(@a.countSymbols()).to eq(2)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(0, 'b', 2)).to eq(true)
            expect(@a.countTransitions()).to eq(2)
        end

        # it "alreadyDeterministicAndCreateDeterministic" do

        #     expect(@a.addState(0)).to eq(true)
        #     expect(@a.addState(1)).to eq(true)
        #     expect(@a.addState(2)).to eq(true)
        #     @a.setInitialState(0)
        #     @a.setFinalState(1)
        #     @a.setFinalState(2)
        #     expect(@a.addSymbol('a')).to eq(true)
        #     expect(@a.addSymbol('b')).to eq(true)
        #     expect(@a.addTransition(0, 'a', 1)).to eq(true)
        #     expect(@a.addTransition(0, 'b', 2)).to eq(true)
        #     expect(@a.isDeterministic()).to eq(true)

        #     expect(@a.isValid()).to eq(true)
        #     expect(@a.isLanguageEmpty()).to eq(false)
        #     expect(@a.match("")).to eq(false)
        #     expect(@a.match("a")).to eq(true)
        #     expect(@a.match("b")).to eq(true)
        #     expect(@a.hasState(0)).to eq(true)
        #     expect(@a.hasState(1)).to eq(true)
        #     expect(@a.hasState(2)).to eq(true)
        #     expect(@a.countStates()).to eq(3)
        #     expect(@a.hasSymbol('a')).to eq(true)
        #     expect(@a.hasSymbol('b')).to eq(true)
        #     expect(@a.countSymbols()).to eq(2)
        #     expect(@a.hasTransition(0, 'a', 1)).to eq(true)
        #     expect(@a.hasTransition(0, 'b', 2)).to eq(true)
        #     expect(@a.countTransitions()).to eq(2)

        #     @det = @a.createDeterministic()

        #     expect(@det.isValid()).to eq(true)
        #     expect(@det.isLanguageEmpty()).to eq(false)
        #     expect(@det.isDeterministic()).to eq(true)
        #     expect(@det.match("")).to eq(false)
        #     expect(@det.match("a")).to eq(true)
        #     expect(@det.match("b")).to eq(true)
        #     expect(@a.hasSymbol('a')).to eq(true)
        #     expect(@a.hasSymbol('b')).to eq(true)
        #     expect(@det.countSymbols()).to eq(2)
        # end

        # it "alreadyDeterministicAndCreateMinimalMoore" do

        #     (0..5).each do |i|
        #         expect(@a.addState(i)).to eq(true)
        #     end
        #     expect(@a.addSymbol('a')).to eq(true)
        #     expect(@a.addSymbol('b')).to eq(true)
        #     @a.setInitialState(0)
        #     @a.setFinalState(3)
        #     @a.setFinalState(4)
        #     expect(@a.addTransition(0, 'a', 1)).to eq(true)
        #     expect(@a.addTransition(0, 'b', 2)).to eq(true)
        #     expect(@a.addTransition(1, 'a', 2)).to eq(true)
        #     expect(@a.addTransition(1, 'b', 3)).to eq(true)
        #     expect(@a.addTransition(2, 'a', 1)).to eq(true)
        #     expect(@a.addTransition(2, 'b', 4)).to eq(true)
        #     expect(@a.addTransition(3, 'a', 4)).to eq(true)
        #     expect(@a.addTransition(3, 'b', 5)).to eq(true)
        #     expect(@a.addTransition(4, 'a', 3)).to eq(true)
        #     expect(@a.addTransition(4, 'b', 5)).to eq(true)
        #     expect(@a.addTransition(5, 'a', 5)).to eq(true)
        #     expect(@a.addTransition(5, 'b', 5)).to eq(true)

        #     expect(@a.isValid()).to eq(true)
        #     expect(@a.isLanguageEmpty()).to eq(false)
        #     (0..5).each do |i|
        #         expect(@a.hasState(i)).to eq(true)
        #     end
        #     expect(@a.countStates()).to eq(6)
        #     expect(@a.hasSymbol('a')).to eq(true)
        #     expect(@a.hasSymbol('b')).to eq(true)
        #     expect(@a.countSymbols()).to eq(2)
        #     expect(@a.hasTransition(0, 'a', 1)).to eq(true)
        #     expect(@a.hasTransition(0, 'b', 2)).to eq(true)
        #     expect(@a.hasTransition(1, 'a', 2)).to eq(true)
        #     expect(@a.hasTransition(1, 'b', 3)).to eq(true)
        #     expect(@a.hasTransition(2, 'a', 1)).to eq(true)
        #     expect(@a.hasTransition(2, 'b', 4)).to eq(true)
        #     expect(@a.hasTransition(3, 'a', 4)).to eq(true)
        #     expect(@a.hasTransition(3, 'b', 5)).to eq(true)
        #     expect(@a.hasTransition(4, 'a', 3)).to eq(true)
        #     expect(@a.hasTransition(4, 'b', 5)).to eq(true)
        #     expect(@a.hasTransition(5, 'a', 5)).to eq(true)
        #     expect(@a.hasTransition(5, 'b', 5)).to eq(true)
        #     expect(@a.countTransitions()).to eq(12)

        #     @moore = Automaton.createMinimalMoore(@a)

        #     expect(@moore.isValid()).to eq(true)
        #     expect(@moore.isLanguageEmpty()).to eq(false)
        #     expect(@moore.isDeterministic()).to eq(true)
        #     expect(@moore.isComplete()).to eq(true)
        #     expect(@moore.countStates()).to eq(4)
        #     expect(@moore.hasSymbol('a')).to eq(true)
        #     expect(@moore.hasSymbol('b')).to eq(true)
        #     expect(@moore.countSymbols()).to eq(2)
        # end

        # it "alreadyDeterministicAndCreateMinimalBrzozowski" do

        #     (0..5).each do |i|
        #         expect(@a.addState(i)).to eq(true)
        #     end
        #     expect(@a.addSymbol('a')).to eq(true)
        #     expect(@a.addSymbol('b')).to eq(true)
        #     @a.setInitialState(0)
        #     @a.setFinalState(3)
        #     @a.setFinalState(4)
        #     expect(@a.addTransition(0, 'a', 1)).to eq(true)
        #     expect(@a.addTransition(0, 'b', 2)).to eq(true)
        #     expect(@a.addTransition(1, 'a', 2)).to eq(true)
        #     expect(@a.addTransition(1, 'b', 3)).to eq(true)
        #     expect(@a.addTransition(2, 'a', 1)).to eq(true)
        #     expect(@a.addTransition(2, 'b', 4)).to eq(true)
        #     expect(@a.addTransition(3, 'a', 4)).to eq(true)
        #     expect(@a.addTransition(3, 'b', 5)).to eq(true)
        #     expect(@a.addTransition(4, 'a', 3)).to eq(true)
        #     expect(@a.addTransition(4, 'b', 5)).to eq(true)
        #     expect(@a.addTransition(5, 'a', 5)).to eq(true)
        #     expect(@a.addTransition(5, 'b', 5)).to eq(true)

        #     expect(@a.isValid()).to eq(true)
        #     expect(@a.isLanguageEmpty()).to eq(false)
        #     (0..5).each do |i|
        #         expect(@a.hasState(i)).to eq(true)
        #     end
        #     expect(@a.countStates()).to eq(6)
        #     expect(@a.hasSymbol('a')).to eq(true)
        #     expect(@a.hasSymbol('b')).to eq(true)
        #     expect(@a.countSymbols()).to eq(2)
        #     expect(@a.hasTransition(0, 'a', 1)).to eq(true)
        #     expect(@a.hasTransition(0, 'b', 2)).to eq(true)
        #     expect(@a.hasTransition(1, 'a', 2)).to eq(true)
        #     expect(@a.hasTransition(1, 'b', 3)).to eq(true)
        #     expect(@a.hasTransition(2, 'a', 1)).to eq(true)
        #     expect(@a.hasTransition(2, 'b', 4)).to eq(true)
        #     expect(@a.hasTransition(3, 'a', 4)).to eq(true)
        #     expect(@a.hasTransition(3, 'b', 5)).to eq(true)
        #     expect(@a.hasTransition(4, 'a', 3)).to eq(true)
        #     expect(@a.hasTransition(4, 'b', 5)).to eq(true)
        #     expect(@a.hasTransition(5, 'a', 5)).to eq(true)
        #     expect(@a.hasTransition(5, 'b', 5)).to eq(true)
        #     expect(@a.countTransitions()).to eq(12)

        #     @brzozowski = Automaton.createMinimalBrzozowski(@a)

        #     expect(@brzozowski.isValid()).to eq(true)
        #     expect(@brzozowski.isLanguageEmpty()).to eq(false)
        #     expect(@brzozowski.isDeterministic()).to eq(true)
        #     expect(@brzozowski.isComplete()).to eq(true)
        #     expect(@brzozowski.countStates()).to eq(4)
        #     expect(@brzozowski.hasSymbol('a')).to eq(true)
        #     expect(@brzozowski.hasSymbol('b')).to eq(true)
        #     expect(@brzozowski.countSymbols()).to eq(2)
        # end
    end

    describe "isComplete" do

        it "ZeroTransition" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.isComplete()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.match("")).to eq(false)
            expect(@a.match("a")).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.countTransitions()).to eq(0)
        end

        it "Good" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 0)).to eq(true)
            expect(@a.isComplete()).to eq(true)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.match("")).to eq(false)
            expect(@a.match("a")).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.countStates()).to eq(1)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 0)).to eq(true)
            expect(@a.countTransitions()).to eq(1)
        end

        it "twoTransitionWithAlphabetTwoLetter" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 0)).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'a', 0)).to eq(true)
            expect(@a.addTransition(1, 'a', 1)).to eq(true)
            expect(@a.isComplete()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.match("")).to eq(false)
            expect(@a.match("a")).to eq(false)
            expect(@a.match("b")).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasSymbol('b')).to eq(true)
            expect(@a.countSymbols()).to eq(2)
            expect(@a.hasTransition(0, 'a', 0)).to eq(true)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(1, 'a', 0)).to eq(true)
            expect(@a.hasTransition(1, 'a', 1)).to eq(true)
            expect(@a.countTransitions()).to eq(4)
        end

        it "AddedRemovedTransition" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'a', 0)).to eq(true)
            expect(@a.isComplete()).to eq(true)
            expect(@a.removeTransition(1, 'a', 0)).to eq(true)
            expect(@a.isComplete()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(true)
            expect(@a.match("")).to eq(false)
            expect(@a.match("a")).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasTransition(0, 'a', 1)).to eq(true)
            expect(@a.hasTransition(1, 'a', 0)).to eq(false)
            expect(@a.countTransitions()).to eq(1)
        end
    end

    describe "createComplete" do

        it "alreadyComplete" do 

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 0)).to eq(true)
            expect(@a.addTransition(0, 'b', 1)).to eq(true)
            expect(@a.addTransition(1, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'b', 0)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)

            expect(@a.isComplete()).to eq(true)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(false)
            expect(@a.hasState(0)).to eq(true)
            expect(@a.hasState(1)).to eq(true)
            expect(@a.countStates()).to eq(2)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasSymbol('b')).to eq(true)
            expect(@a.countSymbols()).to eq(2)
            expect(@a.hasTransition(0, 'a', 0)).to eq(true)
            expect(@a.hasTransition(0, 'b', 1)).to eq(true)
            expect(@a.hasTransition(1, 'a', 1)).to eq(true)
            expect(@a.hasTransition(1, 'b', 0)).to eq(true)
            expect(@a.countTransitions()).to eq(4)

            @complete = Automaton.createComplete(@a)

            expect(@complete.isComplete()).to eq(true)
            expect(@complete.isValid()).to eq(true)
            expect(@complete.isLanguageEmpty()).to eq(false)
            expect(@a.countSymbols()).to eq(2)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasSymbol('b')).to eq(true)

        end

        it "notComplete" do 

            (0..3).each do |i|
                expect(@a.addState(i)).to eq(true)
            end
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(3)
            expect(@a.addTransition(0, 'b', 1)).to eq(true)
            expect(@a.addTransition(1, 'a', 2)).to eq(true)
            expect(@a.addTransition(2, 'a', 2)).to eq(true)
            expect(@a.addTransition(2, 'b', 3)).to eq(true)
            expect(@a.isComplete()).to eq(false)

            expect(@a.isValid()).to eq(true)
            expect(@a.isLanguageEmpty()).to eq(false)
            (0..3).each do |i|
                expect(@a.hasState(i)).to eq(true)
            end
            expect(@a.countStates()).to eq(4)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.hasSymbol('b')).to eq(true)
            expect(@a.countSymbols()).to eq(2)
            expect(@a.hasTransition(0, 'b', 1)).to eq(true)
            expect(@a.hasTransition(1, 'a', 2)).to eq(true)
            expect(@a.hasTransition(2, 'a', 2)).to eq(true)
            expect(@a.hasTransition(2, 'b', 3)).to eq(true)
            expect(@a.countTransitions()).to eq(4)

            @complete = Automaton.createComplete(@a)

            expect(@complete.isValid()).to eq(true)
            expect(@complete.isLanguageEmpty()).to eq(false)
            expect(@complete.hasSymbol('a')).to eq(true)
            expect(@complete.hasSymbol('b')).to eq(true)
            expect(@complete.countSymbols()).to eq(2)
            expect(@complete.isComplete()).to eq(true)

        end

        it "withMaxState" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(INT_MAX)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(INT_MAX)
            expect(@a.addTransition(0, 'a', INT_MAX)).to eq(true)
            expect(@a.isComplete()).to eq(false)

            @complete = Automaton.createComplete(@a)

            expect(@complete.isValid()).to eq(true)
            expect(@complete.isLanguageEmpty()).to eq(false)
            expect(@complete.hasSymbol('a')).to eq(true)
            expect(@complete.hasSymbol('b')).to eq(true)
            expect(@complete.countSymbols()).to eq(2)
            expect(@complete.isComplete()).to eq(true)

        end

        it "MissingState" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'a', 0)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            expect(@a.isComplete()).to eq(false)

            @complete = Automaton.createComplete(@a)

            expect(@complete.isValid()).to eq(true)
            expect(@complete.isLanguageEmpty()).to eq(false)
            expect(@complete.hasSymbol('a')).to eq(true)
            expect(@complete.countSymbols()).to eq(1)
            expect(@complete.isComplete()).to eq(true)

        end

        it "Flecy" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(0)

            @complete = Automaton.createComplete(@a)

            expect(@complete.isValid()).to eq(true)
            expect(@complete.isLanguageEmpty()).to eq(false)
            expect(@complete.hasSymbol('a')).to eq(true)
            expect(@complete.hasSymbol('b')).to eq(true)
            expect(@complete.countSymbols()).to eq(2)
            expect(@complete.isComplete()).to eq(true)

        end

    end

    describe "createComplement" do

        it "notCompleteAndAlreadyDeterministic" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.isDeterministic()).to eq(true)
            expect(@a.isComplete()).to eq(false)

            @complement = Automaton.createComplement(@a)

            expect(@complement.isValid()).to eq(true)
            expect(@complement.isLanguageEmpty()).to eq(false)
            expect(@complement.isDeterministic()).to eq(true)
            expect(@complement.isComplete()).to eq(true)
            expect(@complement.countSymbols()).to eq(2)
            expect(@complement.hasSymbol('a')).to eq(true)
            expect(@complement.hasSymbol('b')).to eq(true)
        end

        it "AlreadyCompleteAndAlreadyDeterministic" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 2)).to eq(true)
            expect(@a.addTransition(1, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'b', 1)).to eq(true)
            expect(@a.addTransition(2, 'a', 2)).to eq(true)
            expect(@a.addTransition(2, 'b', 2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.isDeterministic()).to eq(true)
            expect(@a.isComplete()).to eq(true)

            @complement = Automaton.createComplement(@a)

            expect(@complement.isValid()).to eq(true)
            expect(@complement.isLanguageEmpty()).to eq(false)
            expect(@complement.isDeterministic()).to eq(true)
            expect(@complement.isComplete()).to eq(true)
            expect(@complement.countSymbols()).to eq(2)
            expect(@complement.hasSymbol('a')).to eq(true)
            expect(@complement.hasSymbol('b')).to eq(true)
        end

        it "NotCompleteAndNotDeterministic" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.isDeterministic()).to eq(false)
            expect(@a.isComplete()).to eq(false)

            @complement = Automaton.createComplement(@a)

            expect(@complement.isValid()).to eq(true)
            expect(@complement.isLanguageEmpty()).to eq(false)
            expect(@complement.isDeterministic()).to eq(true)
            expect(@complement.isComplete()).to eq(true)
            expect(@complement.countSymbols()).to eq(2)
            expect(@complement.hasSymbol('a')).to eq(true)
            expect(@complement.hasSymbol('b')).to eq(true)
        end

        it "noInitialState" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.isDeterministic()).to eq(false)
            expect(@a.isComplete()).to eq(false)

            @complement = Automaton.createComplement(@a)

            expect(@complement.isValid()).to eq(true)
            expect(@complement.isLanguageEmpty()).to eq(false)
            expect(@complement.isDeterministic()).to eq(true)
            expect(@complement.isComplete()).to eq(true)
            expect(@complement.countSymbols()).to eq(2)
            expect(@complement.hasSymbol('a')).to eq(true)
            expect(@complement.hasSymbol('b')).to eq(true)

        end

        it "multipleInitialState" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'b', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)
            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(4)
            expect(@a.isDeterministic()).to eq(false)
            expect(@a.isComplete()).to eq(false)

            @complement = Automaton.createComplement(@a)

            expect(@complement.isValid()).to eq(true)
            expect(@complement.isLanguageEmpty()).to eq(false)
            expect(@complement.isDeterministic()).to eq(true)
            expect(@complement.isComplete()).to eq(true)
            expect(@complement.countSymbols()).to eq(2)
            expect(@complement.hasSymbol('a')).to eq(true)
            expect(@complement.hasSymbol('b')).to eq(true)
        end
    end

    describe "createMirror" do

        it "Empty" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)

            @mirror = Automaton.createMirror(@a)

            expect(@mirror.isValid()).to eq(true)
            expect(@mirror.isLanguageEmpty()).to eq(true)
            expect(@a.countStates()).to eq(1)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.hasSymbol('a')).to eq(true)
            expect(@a.countTransitions()).to eq(0)

        end

        it "Mirror" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)

            @mirror = Automaton.createMirror(@a)

            expect(@mirror.isValid()).to eq(true)
            expect(@mirror.isLanguageEmpty()).to eq(false)
            expect(@mirror.countStates()).to eq(2)
            expect(@mirror.countSymbols()).to eq(1)
            expect(@mirror.hasSymbol('a')).to eq(true)
            expect(@mirror.countTransitions()).to eq(1)

        end

        it "deterministicAndNotCompleteWithMultipleFinalState" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @a.setFinalState(2)
            expect(@a.isDeterministic()).to eq(true)
            expect(@a.isComplete()).to eq(false)

            @mirror = Automaton.createMirror(@a)

            expect(@mirror.isValid()).to eq(true)
            expect(@mirror.isLanguageEmpty()).to eq(false)
            expect(@mirror.countStates()).to eq(3)
            expect(@mirror.countSymbols()).to eq(2)
            expect(@mirror.hasSymbol('a')).to eq(true)
            expect(@mirror.hasSymbol('b')).to eq(true)
            expect(@mirror.countTransitions()).to eq(2)
        end

        it "deterministicAndNotCompleteWithOneFinalState" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 2)).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            expect(@a.isDeterministic()).to eq(true)
            expect(@a.isComplete()).to eq(false)

            @mirror = Automaton.createMirror(@a)

            expect(@mirror.isValid()).to eq(true)
            expect(@mirror.isLanguageEmpty()).to eq(false)
            expect(@mirror.countStates()).to eq(3)
            expect(@mirror.countSymbols()).to eq(2)
            expect(@mirror.hasSymbol('a')).to eq(true)
            expect(@mirror.hasSymbol('b')).to eq(true)
            expect(@mirror.countTransitions()).to eq(2)

        end

        it "notDeterministicAndComplete" do

            (0..2).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(1, 'a', 1)).to eq(true)
            expect(@a.addTransition(1, 'b', 2)).to eq(true)
            expect(@a.addTransition(2, 'a', 2)).to eq(true)
            expect(@a.addTransition(2, 'b', 2)).to eq(true)
            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(2)
            expect(@a.isDeterministic()).to eq(false)   
            expect(@a.isComplete()).to eq(true)

            @mirror = Automaton.createMirror(@a)

            expect(@mirror.isValid()).to eq(true)
            expect(@mirror.isLanguageEmpty()).to eq(false)
            expect(@mirror.countStates()).to eq(3)
            expect(@mirror.countSymbols()).to eq(2)
            expect(@mirror.hasSymbol('a')).to eq(true)
            expect(@mirror.hasSymbol('b')).to eq(true)
            expect(@mirror.countTransitions()).to eq(7)
        end
    end

    describe "makeTransition" do

        it "noTransition" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @res = @a.makeTransition(Set.new([0]), 'a')
            expect(@res).to eq(Set.new())
        end

        it "originNull" do 

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @res = @a.makeTransition(Set.new(), 'a')
            expect(@res).to eq(Set.new())
        end

        it "noState" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @res = @a.makeTransition(Set.new([2]), 'a')
            expect(@res).to eq(Set.new())
        end

        it "noSymbol" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @res = @a.makeTransition(Set.new([0]), 'b')
            expect(@res).to eq(Set.new())
        end

        it "originNullAndNoSymbol" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @res = @a.makeTransition(Set.new(), 'b')
            expect(@res).to eq(Set.new())
        end

        it "noStateAndNoSymbol" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @res = @a.makeTransition(Set.new([2]), 'b')
            expect(@res).to eq(Set.new())
        end

        it "noTransitionAndNoSymbol" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)
            expect(@a.addSymbol('a')).to eq(true)
            @a.setInitialState(0)
            @a.setFinalState(1)
            @res = @a.makeTransition(Set.new([1]), 'b')
            expect(@res).to eq(Set.new())
        end

        it "succes" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)
            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)
            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            @res = @a.makeTransition(Set.new([0, 1]), 'a')
            expect(@res).to eq(Set.new([1, 2, 3]))
            expect(@res.empty?).to eq(false)
        end

    end
            
    describe "readString" do

        it "notInAlphabetWithFinalAndInitialState" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            @res = @a.readString("abc");
            expect(@res).to eq(Set.new())

        end

        it "notInAlphabetWithNoFinalAndInitialState" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            @res = @a.readString("abc");
            expect(@res).to eq(Set.new())
        end

        it "noStateInitial" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            @res = @a.readString("a");
            expect(@res).to eq(Set.new())
        end

        it "noStateFinal" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            @res = @a.readString("a");
            expect(@res).to eq(Set.new([1, 2, 3]))
        end

        it "noStateFinalAndNoStateInitial" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            @res = @a.readString("a");
            expect(@res).to eq(Set.new())
        end

        it "toShortWord" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            @res = @a.readString("ab");
            expect(@res).to eq(Set.new([3, 4]))
        end

        it "toLongWord" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)

            @res = @a.readString("abbaa");
            expect(@res).to eq(Set.new())
        end

        it "stringNullNotAccept" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setFinalState(1)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 0)).to eq(true)
            expect(@a.addTransition(1, 'b', 0)).to eq(true)

            @res = @a.readString("");
            expect(@res).to eq(Set.new([0]))
        end
    end

    describe "match" do

        it "notInAlphabetWithFinalAndInitialState" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            expect(@a.match("abc")).to eq(false)

        end

        it "notInAlphabetWithNoFinalAndInitialState" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            expect(@a.match("abc")).to eq(false)
        end

        it "noStateInitial" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            expect(@a.match("a")).to eq(false)
        end

        it "noStateFinal" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            expect(@a.match("a")).to eq(false)
        end

        it "noStateFinalAndNoStateInitial" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            expect(@a.match("a")).to eq(false)
        end

        it "toShortWord" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)
            expect(@a.addTransition(4, 'a', 4)).to eq(true)

            expect(@a.match("ab")).to eq(true)
        end

        it "toLongWord" do

            (0..4).each do |i|
                expect(@a.addState(i)).to eq(true)
            end

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setInitialState(1)
            @a.setFinalState(1)
            @a.setFinalState(4)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'a', 2)).to eq(true)
            expect(@a.addTransition(0, 'a', 3)).to eq(true)
            expect(@a.addTransition(1, 'b', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 3)).to eq(true)
            expect(@a.addTransition(2, 'a', 4)).to eq(true)
            expect(@a.addTransition(3, 'a', 3)).to eq(true)
            expect(@a.addTransition(3, 'b', 4)).to eq(true)

            expect(@a.match("abbaa")).to eq(false)
        end

        it "stringNullNotAccept" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setFinalState(1)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 0)).to eq(true)
            expect(@a.addTransition(1, 'b', 0)).to eq(true)

            expect(@a.match("")).to eq(false)
        end

        it "stringNullAccept" do

            expect(@a.addState(0)).to eq(true)
            expect(@a.addState(1)).to eq(true)

            expect(@a.addSymbol('a')).to eq(true)
            expect(@a.addSymbol('b')).to eq(true)

            @a.setInitialState(0)
            @a.setFinalState(0)
            @a.setFinalState(1)

            expect(@a.addTransition(0, 'a', 1)).to eq(true)
            expect(@a.addTransition(0, 'b', 0)).to eq(true)
            expect(@a.addTransition(1, 'b', 0)).to eq(true)

            expect(@a.match("")).to eq(true)
        end
    end

    # describe "createIntersection" do

    #     it "hugeAutomaton" do

    #         nbStates = 150

    #         begin150A = Automaton.new()
    #         expect(begin150A.addState(0)).to eq(true)
    #         begin150A.setInitialState(0)
    #         expect(begin150A.addSymbol('a')).to eq(true)
    #         expect(begin150A.addSymbol('b')).to eq(true)
    #         (1..nbStates-1).each do |i|
    #             expect(begin150A.addState(i)).to eq(true)
    #             expect(begin150A.addTransition(i-1, 'a', i)).to eq(true)
    #         end
    #         expect(begin150A.addState(nbStates)).to eq(true)
    #         begin150A.setFinalState(nbStates)
    #         expect(begin150A.addTransition(nbStates-1, 'a', nbStates)).to eq(true)
    #         expect(begin150A.addTransition(nbStates, 'a', nbStates)).to eq(true)
    #         expect(begin150A.addTransition(nbStates, 'b', nbStates)).to eq(true)

    #         end150A = Automaton.new()
    #         expect(end150A.addState(0)).to eq(true)
    #         end150A.setInitialState(0)
    #         expect(end150A.addSymbol('a')).to eq(true)
    #         expect(end150A.addSymbol('b')).to eq(true)
    #         (1..nbStates-1).each do |i|
    #             expect(end150A.addState(i)).to eq(true)
    #             expect(end150A.addTransition(i - 1, 'a', i))
    #         end
    #         expect(end150A.addState(nbStates)).to eq(true)
    #         end150A.setFinalState(nbStates)
    #         expect(end150A.addTransition(nbStates - 1, 'a', nbStates)).to eq(true)
    #         expect(end150A.addTransition(0, 'a', 0)).to eq(true)
    #         expect(end150A.addTransition(0, 'b', 0)).to eq(true)

    #         inter = Automaton.createIntersection(begin150A, end150A)
    #         expect(inter.isIncludedIn(begin150A)).to eq(true)
    #         # puts "Debut 2nd included"
    #         # expect(inter.isIncludedIn(end150A)).to eq(true)
    #     end
    # end
end