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
        raise "Automaton isn't valid" unless self.isValid()
        @transition.each do |from, alpha|
            if alpha.key?(Epsilon)
                return true
            end
        end
        return false
    end

    def isDeterministic
        raise "Automaton isn't valid" unless self.isValid()
        if @initial.length != 1
            return false
        end
        @transition.each do |from|
            from[1].each do |letter, to|
                if to.length > 1
                    return false
                end
            end
        end
        return true
    end

    def isComplete
        raise "Automaton isn't valid" unless self.isValid()
        @states.each do |state|
            if !@transition.key?(state)
                return false
            end
            size = 0
            @alphabet.each do |alpha|
                if @transition[state].key?(alpha) && @transition[state][alpha].length > 0
                    size += 1
                end
            end
            if size != @alphabet.length
                return false
            end
        end
        return true
    end

    def makeTransition(origin, alpha)
        raise "Automaton isn't valid" unless self.isValid()
        raise "Origin isn't valid" unless origin.class == Set
        raise "Alpha isn't valid" unless alpha.class == String

        res = Set.new()

        origin.each do |state|
            @states.each do |to|
                if self.hasTransition(state, alpha, to)
                    res.add(to)
                end
            end
        end

        return res
    end

    def readString(word)
        @res = Set.new()

        if @initial.empty?
            return Set.new()
        end

        if word.empty?
            return @initial
        end

        word.length.times do |i|
            if !@alphabet.include?(word[i])
                return Set.new()
            end
        end

        @initial.each do |init|
            initTransitions = makeTransition(Set.new([init]), word[0])
            if initTransitions.empty?
                next
            end
            if word.length == 1
                initTransitions.each do |value|
                    @res.add(value)
                end
                next
            end
            transitions = makeTransition(initTransitions, word[1])
            if word.length == 2
                transitions.each do |value|
                    @res.add(value)
                end
                next
            end
            (2..word.length-1).each do |i|
                transitions = makeTransition(transitions, word[i])
            end
            transitions.each do |value|
                res.add(value)
            end
        end

        return @res
    end

    def match(word)
        m = readString(word)
        m.each do |value|
            if self.isFinalState(value)
                return true
            end
        end
        return false
    end

    

    def removeNonAccessibleState
        return
    end

    def removeNonCoAccessibleStates
        return
    end

    def isLanguageEmpty
        raise "Automaton isn't valid" unless self.isValid()
        @visited = Set.new()

        if @initial.length == 0 || @final.length == 0
            return true
        end

        @states.each do |state|
            if self.isInitialState(state)
                DepthFirstSearch(state, @visited)
            end
        end
        
        @visited.each do |state|
            if self.isFinalState(state)
                return false
            end
        end

        return true
    end

    def hasEmptyIntersectionWith(other)
        return false
    end

    def isIncludedIn(other)
        return false
    end

    def self.createMirror(automaton)
        raise "Automaton isn't valid" unless automaton.isValid()
        @fa = Automaton.new()
        @fa.alphabet = automaton.alphabet
        @fa.state = automaton.state

        automaton.states.each do |state|
            if automaton.isInitialState(state)
                @fa.setFinalState(state)
            end
            if automaton.isFinalState(state)
                @fa.setInitialState(state)
            end
        end

        automaton.transition.each do |from, alpha|
            if automaton.key?(from) && automaton[from].key?(alpha)
                automaton[from][alpha].each do |to|
                    @fa.addTransition(to, alpha, from)
                end
            end
        end

        return @fa
    end

    def self.createComplete(automaton)
        raise "Automaton isn't valid" unless automaton.isValid()
        if automaton.isLanguageEmpty()
            @base = Automaton.new()
            @base.alphabet = automaton.alphabet
            @base.addState(0)
            @base.setInitialState(0)
            @alphabet.each do |alpha|
                @base.addTransition(0, alpha, 0)
            end
            return @base
        end

        if automaton.isComplete()
            return automaton
        end

        @fa = automaton
        trash = 0
        size = automaton.states.length

        size.times do |i|
            trash = i
            if @fa.hasState(i)
                break
            end
        end

        if trash == size-1

            if size-1 == 0 && !fa.hasState(0)
                trash = 0
            else
                trash = size
            end
        end

        @fa.addState(trash)

        @fa.alphabet.each do |alpha|
            @fa.addTransition(trash, alpha, trash)
        end

        @fa.states.each do |state|
            @fa.alphabet.each do |alpha|
                if !@fa.transition.key?(state) || !@fa.transition[state].key?(alpha) || @fa.transition[state][alpha].length == 0
                    @fa.addTransition(state, alpha, trash)
                end
            end
        end

        return @fa
    end

    def self.createComplement(automaton)
        raise "Automaton isn't valid" unless automaton.isValid()

        @fa = createComplete(createDeterministic(automaton))

        @fa.state.each do |state|
            if !@fa.isFinalState(state)
                @fa.setFinalState(state)
            elsif @fa.isFinalState(state)
                @fa.final.delete(state)
            end
        end

        return @fa
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

    private
    def DepthFirstSearch(state, visited)
        visited.add(state)
        stack = []
        stack.push(state)
        while !stack.empty?
            s = stack.pop()
            @alphabet.each do |alpha|
                transitions = makeTransition(Set.new([s]), alpha)
                transitions.each do |to|
                    if !visited.include?(to)
                        visited.add(to)
                        stack.push(to)
                    end
                end
            end
        end
    end

end

class String
    def isgraph
        return false if self.class != String 
        return true if !self.match(/\A[[:space:]]*\z/) && self.match(/[[:print:]]/)
        false
    end
end