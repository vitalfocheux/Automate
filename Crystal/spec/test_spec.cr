require "spec"
require "./Automaton.cr"

describe Automaton do

  describe "isValid" do

    it "noSymbolNoState " do
      a = Automaton.new
      a.isValid.should be_false
    end
  end

end