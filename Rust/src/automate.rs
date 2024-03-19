use std::collections::HashSet as Set;

pub struct Automate {
    alphabet: Set<char>,
    states: Set<i64>,
    initial_state: Set<i64>,
    final_states: Set<i64>,
    transitions: Set<(i64, char, i64)>,
}

impl Automate {

    pub const EPSILON: char = '\0';

    pub fn new() -> Automate {
        Automate {
            alphabet: Set::new(),
            states: Set::new(),
            initial_state: Set::new(),
            final_states: Set::new(),
            transitions: Set::new(),
        }
    }

    pub fn is_valid(&self) -> bool {
        return self.alphabet.len() > 0 && self.states.len() > 0;
    }

    pub fn add_symbol(&mut self, symbol: char) -> bool {
        if self.has_symbol(symbol) || symbol == Automate::EPSILON || !symbol.is_ascii_graphic() {
            return false;
        }
        return self.alphabet.insert(symbol);
    }

    pub fn remove_symbol(&mut self, symbol: char) -> bool {
        if !self.has_symbol(symbol) {
            return false;
        }
        return self.alphabet.remove(&symbol);
    }

    pub fn has_symbol(&self, symbol: char) -> bool {
        if !symbol.is_ascii_graphic() {
            return false;
        }
        return self.alphabet.contains(&symbol);
    }

    pub fn count_symbols(&self) -> i64 {
        return self.alphabet.len() as i64;
    }

    pub fn add_state(&mut self, state: i64) -> bool {
        if self.has_state(state) || state < 0 {
            return false;
        }
        return self.states.insert(state);
    }

    pub fn remove_state(&mut self, state: i64) -> bool {
        if !self.has_state(state) {
            return false;
        }
        return self.states.remove(&state);
    }

    pub fn has_state(&self, state: i64) -> bool {
        return self.states.contains(&state);
    }

    pub fn count_states(&self) -> i64 {
        return self.states.len() as i64;
    }

    pub fn set_state_initial(&mut self, state: i64) {
        self.initial_state.insert(state);
    }

    pub fn is_state_initial(&self, state: i64) -> bool {
        self.initial_state.contains(&state);
        return true;
    }

    pub fn set_state_final(&mut self, state: i64) {
        self.final_states.insert(state);
    }

    pub fn is_state_final(&self, state: i64) -> bool {
        self.final_states.contains(&state);
        return true;
    }

    pub fn add_transition(&mut self, from: i64, symbol: char, to: i64) -> bool {
        self.transitions.insert((from, symbol, to));
        return true;
    }

    pub fn remove_transition(&mut self, from: i64, symbol: char, to: i64) -> bool {
        self.transitions.remove(&(from, symbol, to));
        return true;
    }

    pub fn has_transition(&self, from: i64, symbol: char, to: i64) -> bool {
        self.transitions.contains(&(from, symbol, to));
        return true;
    }

    pub fn count_transitions(&self) -> i64 {
        return self.transitions.len() as i64;
    }

    pub fn has_epsilon_transition(&self) -> bool {
        return false;
    }

    pub fn is_deteministic(&self) -> bool {
        return false;
    }

    pub fn is_complete(&self) -> bool {
        return false;
    }

    pub fn create_complete(other: &Automate) -> Automate {
        return Automate::new();
    }

    pub fn create_complement(other: &Automate) -> Automate {
        return Automate::new();
    }

    pub fn create_mirror(other: &Automate) -> Automate {
        return Automate::new();
    }

    pub fn make_transition(&self, origin: Set<i64>, symbol: char) -> Set<i64> {
        return Set::new();
    }

    pub fn read_string(&self, string: &str) -> Set<i64> {
        return Set::new();
    }

    // pub fn match(&self, string: &str) -> bool {
    //     return false;
    // }

    pub fn is_language_empty(&self) -> bool {
        return false;
    }

    pub fn remove_non_accessible_states(&mut self) {
    }

    pub fn remove_non_co_accessible_states(&mut self) {
    }

    pub fn create_intersection(lhs: &Automate, rhs: &Automate) -> Automate {
        return Automate::new();
    }

    pub fn has_empty_intersection_with(&self, other: &Automate) -> bool {
        return false;
    }

    pub fn create_deterministic(other: &Automate) -> Automate {
        return Automate::new();
    }

    pub fn is_included_in(&self, other: &Automate) -> bool {
        return false;
    }

    pub fn create_minimal_moore(other: &Automate) -> Automate {
        return Automate::new();
    }

    pub fn create_minimal_brzozowski(other: &Automate) -> Automate {
        return Automate::new();
    }

}