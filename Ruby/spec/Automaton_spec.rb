require 'rspec'
require_relative '../lib/Automaton.rb'

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
    end

    describe "isValid" do
        it "withSymbol and noState" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.isValid()).to eq(false)
            expect(@a.hasSymbol("a")).to eq(true)
        end
    end

    describe "isValid" do
        it "noSymbol and withState" do
            expect(@a.addState(0)).to eq(true)
            expect(@a.isValid()).to eq(false)
            expect(@a.hasState(0)).to eq(true)
        end
    end

    describe "isValid" do
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
    end

    describe "addSymbol" do
        it "Space" do
            expect(@a.addSymbol(" ")).to eq(false)
        end
    end

    describe "addSymbol" do
        it "isNotGraph" do
            expect(@a.addSymbol("\n")).to eq(false)
        end
    end

    describe "addSymbol" do
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
    end

    describe "addSymbol" do
        it "oneSymbol" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
        end
    end

    describe "addSymbol" do
        it "twoSymbol" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.countSymbols()).to eq(1)
            expect(@a.addSymbol("b")).to eq(true)
            expect(@a.hasSymbol("b")).to eq(true)
            expect(@a.countSymbols()).to eq(2)
        end
    end

    describe "addSymbol" do
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
    end

    describe "removeSymbol" do
        it "Empty" do
            expect(@a.removeSymbol("a")).to eq(false)
        end
    end

    describe "removeSymbol" do
        it "notInAlphabet" do
            expect(@a.addSymbol("a")).to eq(true)
            expect(@a.removeSymbol("b")).to eq(false)
            expect(@a.hasSymbol("a")).to eq(true)
            expect(@a.hasSymbol("b")).to eq(false)
            expect(@a.countSymbols()).to eq(1)
        end
    end

    describe "removeSymbol" do
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
    end

    describe "removeSymbol" do
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
    end

    describe "hasSymbol" do
        it "Succes" do
            7.times do |i|
                a = i + 'a'.ord
                expect(@a.addSymbol(a.to_s)).to eq(true)
                expect(@a.hasSymbol(a.to_s)).to eq(true)
            end
        end
    end

    describe "hasSymbol" do
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

end