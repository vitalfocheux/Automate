require 'rspec'
require_relative '../lib/Automaton.rb'

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

    # describe "isValid" do
    #     it "noSymbol and withState" do
    #         expect(@a.addState(0)).to eq(true)
    #         expect(@a.isValid()).to eq(false)
    #         expect(@a.hasState(0)).to eq(true)
    #     end
    # end

    # describe "isValid" do
    #     it "withSymbol and withState" do
    #         expect(@a.addSymbol("a")).to eq(true)
    #         expect(@a.addState(0)).to eq(true)
    #         expect(@a.isValid()).to eq(true)
    #         expect(@a.hasSymbol("a")).to eq(true)
    #         expect(@a.hasState(0)).to eq(true)
    #     end
    # end

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

    # describe "removeSymbol" do
    #     it "symbolInTransition" do
    #         expect(@a.addSymbol("a")).to eq(true)
    #         expect(@a.addState(0)).to eq(true)
    #         expect(@a.addState(1)).to eq(true)
    #         expect(@a.addTransition(0, "a", 1)).to eq(true)
    #         expect(@a.removeSymbol("a")).to eq(false)
    #         expect(@a.hasSymbol("a")).to eq(true)
    #         expect(@a.countSymbols()).to eq(1)
    #     end
    # end

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
end