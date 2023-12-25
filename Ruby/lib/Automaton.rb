require 'set'

class Automaton
    Epsilon = "\0"

    attr_reader :states, :alphabet, :transition, :initial, :final

    def initialize
        @states = Set.new()
        @initial = Set.new()
        @final = Set.new()
        @alphabet = Set.new()
        @transition = {}
    end

    def isValid()
        return @states.length > 0 && @alphabet.length > 0
    end

    def addSymbol(symbol)
        if symbol.class != String || hasSymbol(symbol) || symbol == Epsilon || symbol == " " || !symbol.isgraph()
            return false
        end
        @alphabet.add(symbol)
        return true
    end

    def removeSymbol(symbol)
        if symbol.class != String || !hasSymbol(symbol)
            return false
        end
        @alphabet.delete(symbol)
        transition.each do |from|
            if transition.key?(from) && transition[from].key?(symbol)
                transition[from].delete(symbol)
            end
        end
        return true
    end

    def hasSymbol(symbol)
        if symbol.class != String
            return false
        end
        return @alphabet.include?(symbol)
    end

    def countSymbols
        return @alphabet.length
    end

    def addState(state)
        if state.class != Integer || hasState(state) || state < 0
            return false
        end
        @states.add(state)
        return true
    end

    def removeState(state)
        if state.class != Integer || !hasState(state)
            return false
        end
        @states.delete(state)
        transition.each do |from, key|
            if transition.key?(from)
                transition.delete(from)
            end
            if transition.key?(state) && transition[state].key?(key) && transition[state][key].include?(from)
                transition[state][key].delete(from)
            end
        end
        return true
    end

    def hasState(state)
        if state.class != Integer
            return false
        end
        return @states.include?(state)
    end

    def countStates
        return @states.length
    end

    def setInitialState(state)
        if state.class != Integer || !hasState(state)
            return
        end
        @initial.add(state)
    end

    def isInitialState(state)
        if state.class != Integer || !hasState(state)
            return false
        end
        return @initial.include?(state)
    end

    def setFinalState(state)
        if state.class != Integer || !hasState(state)
            return
        end
        @final.add(state)
    end

    def isFinalState(state)
        if state.class != Integer || !hasState(state)
            return false
        end
        return @final.include?(state)
    end

    def addTransition(from, alpha, to)
        if !from.is_a?(Integer) || !alpha.is_a?(String) || !to.is_a?(Integer) || !hasState(from) || !hasState(to) || (!hasSymbol(alpha) && alpha != Epsilon) || hasTransition(from, alpha, to)
            return false
        end
        if !transition.key?(from)
            transition[from] = {}
        end
        if !transition[from].key?(alpha)
            transition[from][alpha] = Set.new()
        end
        transition[from][alpha].add(to)
        return true
    end

    def removeTransition(from, alpha, to)
        if from.class != Integer || alpha.class != String || to.class != Integer || !hasState(from) || !hasState(to) || (!hasSymbol(alpha) && alpha != Epsilon) || !hasTransition(from, alpha, to)
            return false
        end
        if alpha == Epsilon
            transition[from].delete(alpha)
            return true
        end
        if transition[from][alpha].include?(to)
            transition[from][alpha].delete(to)
            return true
        end
        return false
    end

    def hasTransition(from, alpha, to)
        if from.class != Integer || alpha.class != String || to.class != Integer  || !hasState(from) || !hasState(to) || (!hasSymbol(alpha) && alpha != Epsilon) || transition.length == 0
            return false
        end
        if !transition.key?(from) || !transition[from].key?(alpha)
            return false
        end
        if alpha == Epsilon
            return transition[from].key?(alpha)
        end
        return transition[from][alpha].include?(to)
    end

    def countTransitions()
        count = 0
        @transition.each do |from, alpha|
            alpha.each do |alpha, to|
                count += to.length
            end
        end
        return count
    end

    def prettyPrint(file)
        File.open(file, "w") do |f|
            f.write("Initial states\n\t")
            @initial.each do |state|
                f.write("#{state} ")
            end
            f.write("\nFinal states\n\t")
            @final.each do |state|
                f.write("#{state} ")
            end
            f.write("\nTransitions:\n")
            @states.each do |state|
                f.write("\tFor state #{state}:\n")
                @alphabet.each do |alpha|
                    f.write("\t\tFor letter #{alpha}: ")
                    if @transition.key?(state) && @transition[state].key?(alpha)
                        @transition[state][alpha].each do |to|
                            f.write("#{to} ")
                        end
                    end
                    f.write("\n")
                end
            end
        end
    end

    def hasEpsilonTransition
        @transition.each do |from, alpha|
            if alpha.key?(Epsilon)
                return true
            end
        end
        return false
    end

    def isDeterministic
        return false
    end

    def isComplete
        return false
    end

    def makeTransition(origin, alpha)
        return Set.new()
    end

    def readString(word)
        return Set.new()
    end

    def match(word)
        return false
    end

    def removeNonAccessibleState
        return
    end

    def removeNonCoAccessibleStates
        return
    end

    def isLanguageEmpty
        return false
    end

    def hasEmptyIntersectionWith(other)
        return false
    end

    def isIncludedIn(other)
        return false
    end

    def self.createMirror(automaton)
        return Automaton.new()
    end

    def self.createComplete(automaton)
        return Automaton.new()
    end

    def self.createComplement(automaton)
        return Automaton.new()
    end

    def self.createIntersection(lhs, rhs)
        return Automaton.new()
    end

    def self.createDeterministic(other)
        return Automaton.new()
    end

    def self.createMinimalMoore(other)
        return Automaton.new()
    end

    def self.createMinimalBrzozowski(other)
        return Automaton.new()
    end

end

class String
    def isgraph
        return false if self.class != String 
        return true if !self.match(/\A[[:space:]]*\z/) && self.match(/[[:print:]]/)
        false
    end
end

# h = {}
# h[0] = {}
# h[0][Automaton::Epsilon] = Set.new()
# h[0][Automaton::Epsilon].add(1)
# puts h
# puts h[0].key?(Automaton::Epsilon)