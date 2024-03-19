require "set"

class Automaton
    Epsilon = '\0'

    private getter states : Set(Int32)
    private getter initials : Set(Int32)
    private getter finals : Set(Int32)
    private getter alphabet : Set(Char)
    private getter transitions : Hash(Int32, Hash(Char, Set(Int32)))

    def initialize()
        @states = Set(Int32).new
        @transitions = Hash(Int32, Hash(Char, Set(Int32))).new { |h, k| h[k] = Hash(Char, Set(Int32)).new }
        @initials = Set(Int32).new
        @finals = Set(Int32).new
        @alphabet = Set(Char).new
    end

    def isValid() : Bool
        return false
    end

    def addSymbol(symbol : Char) : Bool
        return false
    end

    def removeSymbol(symbol : Char) : Bool
        return false
    end

    def hasSymbol(symbol : Char) : Bool
        return false
    end

    def countSymbols() : UInt64
        return 0
    end

    def addState(state : Int) : Bool
        return false
    end

    def removeState(state : Int) : Bool
        return false
    end

    def hasState(state : Int) : Bool
        return false
    end

    def countStates() : UInt64
        return 0
    end

    def setStateInitial(state : Int)
    end

    def isStateInitial(state : Int) : Bool
        return false
    end

    def setStateFinal(state : Int)
    end

    def isStateFinal(state : Int) : Bool
        return false
    end

    def addTransition(from : Int, alpha : Char, to : Int) : Bool
        return false
    end

    def removeTransition(from : Int, alpha : Char, to : Int) : Bool
        return false
    end

    def hasTransition(from : Int, alpha : Char, to : Int) : Bool
        return false
    end

    def countTransitions() : UInt64
        return 0
    end

    def prettyPrint()
    end

    def hasEpislonTransitions() : Bool
        return false
    end

    def isDeterministic() : Bool
        return false
    end

    def isComplete() : Bool
        return false
    end

    def makeTransition(origin : Set(Int), alpha : Char) : Set(Int)
        return Set.new
    end

    def readString(word : String) : Set(Int)
        return Set.new
    end

    def match(word : String) : Bool
        return false
    end

    def removeNonAccessibleStates()
    end

    def removeNonCoAccessibleStates()
    end

    def isLanguageEmpty() : Bool
        return false
    end

    def hasEmptyIntersectionWith(other : Automaton) : Bool
        return false
    end

    def isIncludedIn(other : Automaton) : Bool
        return false
    end

    def self.createMirror(automaton : Automaton) : Automaton
        return Automaton.new
    end

    def self.createComplete(automaton : Automaton) : Automaton
        return Automaton.new
    end

    def self.createComplement(automaton : Automaton) : Automaton
        return Automaton.new
    end

    def self.createIntersection(lhs : Automaton, rhs : Automaton) : Automaton
        return Automaton.new
    end

    def self.createDeterministic(automaton : Automaton) : Automaton
        return Automaton.new
    end

    def self.createMinimalMoore(automaton : Automaton) : Automaton
        return Automaton.new
    end

    def self.createMinimalBrzozowski(automaton : Automaton) : Automaton
        return Automaton.new
    end

end