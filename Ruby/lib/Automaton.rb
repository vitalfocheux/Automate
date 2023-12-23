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
        if state.class != Integer || hasState(state)
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

    def setStateInitial(state)
        if state.class != Integer || !hasState(state)
            return
        end
        @initial.add(state)
    end

    def isStateInitial(state)
        if state.class != Integer || !hasState(state)
            return false
        end
        return @initial.include?(state)
    end

    def setStateFinal(state)
        if state.class != Integer || !hasState(state)
            return
        end
        @final.add(state)
    end

    def isStateFinal(state)
        if state.class != Integer || !hasState(state)
            return false
        end
        return @final.include?(state)
    end


end

class String
    def isgraph
        return false if self.class != String 
        return true if !self.match(/\A[[:space:]]*\z/) && self.match(/[[:print:]]/)
        false
    end
end