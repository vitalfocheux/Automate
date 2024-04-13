mod automate;

fn main() {
}

#[cfg(test)]
mod tests {
    use crate::automate::Automate;

    fn equivalent(a: &Automate, b: &Automate) -> bool {
        return a.is_included_in(b) && b.is_included_in(a);
    }

    #[test]
    fn test_is_valid_no_symbol_no_state() {
        let a = Automate::new();
        assert!(!a.is_valid());
    }

    #[test]
    fn test_is_valid_with_symbol_no_state() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(!a.is_valid());
        assert!(a.has_symbol('a'));
    }

    #[test]
    fn test_is_valid_no_symbol_with_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(!a.is_valid());
        assert!(a.has_state(0));
    }

    #[test]
    fn test_is_valid_with_symbol_with_state() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.is_valid());
        assert!(a.has_symbol('a'));
        assert!(a.has_state(0));
    }

    #[test]
    fn test_add_symbol_epsilon() {
        let mut a = Automate::new();
        assert!(!a.add_symbol(Automate::EPSILON));
        assert!(!a.has_symbol(Automate::EPSILON));
    }

    #[test]
    fn test_add_symbol_space() {
        let mut a = Automate::new();
        assert!(!a.add_symbol(' '));
        assert!(!a.has_symbol(' '));
    }

    #[test]
    fn test_add_symbol_is_not_graph() {
        let mut a = Automate::new();
        assert!(!a.add_symbol('\n'));
        assert!(!a.has_symbol('\n'));
    }

    #[test]
    fn test_add_symbol_is_graph() {
        let mut a = Automate::new();
        let mut c: i64 = 0;
        for value in 0..255 {
            let i = value as u8 as char;
            if i.is_ascii_graphic() {
                assert!(a.add_symbol(i));
                assert!(a.has_symbol(i));
                c += 1;
                assert_eq!(c, a.count_symbols());
            } else {
                assert!(!a.add_symbol(i));
                assert!(!a.has_symbol(i));
                assert_eq!(c, a.count_symbols());
            }
        }
        assert_eq!(94, a.count_symbols());
    }

    #[test]
    fn test_add_symbol_one_symbol() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
    }

    #[test]
    fn test_add_symbol_two_symbol() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.has_symbol('a'));
        assert!(a.add_symbol('b'));
        assert!(a.has_symbol('b'));
        assert_eq!(2, a.count_symbols());
    }

    #[test]
    fn test_add_symbol_two_identical_symbols() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(!a.add_symbol('a'));
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
    }

    #[test]
    fn test_remove_symbol_one_symbol() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.remove_symbol('a'));
        assert!(!a.has_symbol('a'));
    }

    #[test]
    fn test_remove_symbol_empty() {
        let mut a = Automate::new();
        assert!(!a.remove_symbol('a'));
        assert!(!a.has_symbol('a'));
    }

    #[test]
    fn test_remove_symbol_all_character() {
        let mut a = Automate::new();
        let mut c: i64 = 0;
        for value in 0..255 {
            let i = value as u8 as char;
            if i.is_ascii_graphic() {
                assert!(a.add_symbol(i));
                assert!(a.has_symbol(i));
                c += 1;
                assert_eq!(c, a.count_symbols());
            }else{
                assert!(!a.add_symbol(i));
                assert!(!a.has_symbol(i));
                assert_eq!(c, a.count_symbols());
            }
        }

        assert_eq!(94, a.count_symbols());

        for value in 0..255 {
            let i = value as u8 as char;
            if i.is_ascii_graphic() {
                assert!(a.remove_symbol(i));
                assert!(!a.has_symbol(i));
                c -= 1;
                assert_eq!(c, a.count_symbols());
            }else{
                assert!(!a.remove_symbol(i));
                assert!(!a.has_symbol(i));
                assert_eq!(c, a.count_symbols());
            }
        }
        assert_eq!(0, a.count_symbols());
    }

    #[test]
    fn test_remove_symbol_symbol_in_transition() {
        let mut a = Automate::new();
        for i in 0..=4 {
            assert!(a.add_state(i));
        }
        a.set_state_initial(0);
        a.set_state_initial(1);
        a.set_state_final(1);
        a.set_state_final(4);
        assert!(a.add_symbol('a'));
        assert!(a.add_symbol('b'));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.add_transition(0, 'a', 2));
        assert!(a.add_transition(0, 'a', 3));
        assert!(a.add_transition(1, 'b', 3));
        assert!(a.add_transition(2, 'a', 3));
        assert!(a.add_transition(2, 'b', 4));
        assert!(a.add_transition(3, 'a', 3));
        assert!(a.add_transition(3, 'b', 4));
        assert!(a.add_transition(4, 'a', 4));

        assert!(a.remove_symbol('a'));
        assert!(!a.has_symbol('a'));
        assert!(a.has_symbol('b'));
        for i in 0..=4 {
            assert!(a.has_state(i));
        }
        assert!(a.has_transition(1, 'b', 3));
        assert!(a.has_transition(2, 'b', 4));
        assert!(a.add_transition(3, 'b', 4));
        assert!(!a.has_transition(0, 'a', 1));
        assert!(!a.has_transition(0, 'a', 2));
        assert!(!a.has_transition(0, 'a', 3));
        assert!(!a.has_transition(2, 'a', 3));
        assert!(!a.has_transition(3, 'a', 3));
        assert!(!a.has_transition(4, 'a', 4));
        assert_eq!(3, a.count_transitions());
    }

    #[test]
    fn test_has_symbol_success() {
        let mut a = Automate::new();
        for i in 0i64..=6i64 {
            let c = (('a' as u8) + (i as u8)) as char;
            assert!(a.add_symbol(c));
            assert!(a.has_symbol(c));
        }
    }

    #[test]
    fn test_has_symbol_empty() {
        let a = Automate::new();
        assert!(!a.has_symbol('a'));
    }

    #[test]
    fn test_has_symbol_not_is_graph() {
        let mut a = Automate::new();
        for i in 0i64..=6i64 {
            let c = ('a' as u8 + i as u8) as char;
            assert!(a.add_symbol(c));
        }
        assert!(!a.has_symbol('\n'));
    }

    #[test]
    fn test_count_symbol_full() {
        let mut a = Automate::new();
        for i in 0i64..=6i64 {
            let c = ('a' as u8 + i as u8) as char;
            assert!(a.add_symbol(c));
            assert_eq!(i + 1, a.count_symbols());
        }
    }

    #[test]
    fn test_count_symbol_empty() {
        let a = Automate::new();
        assert_eq!(0, a.count_symbols());
    }

    #[test]
    fn test_add_state_one_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_add_state_two_identical_states() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(!a.add_state(0));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_add_state_two_states() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
    }

    #[test]
    fn test_add_state_negative() {
        let mut a = Automate::new();
        assert!(!a.add_state(-1));
        assert!(!a.has_state(-1));
        assert_eq!(0, a.count_states());
    }

    #[test]
    fn test_add_state_MAX() {
        let mut a = Automate::new();
        assert!(a.add_state(i64::MAX));
        assert!(a.has_state(i64::MAX));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_add_state_MIN() {
        let mut a = Automate::new();
        assert!(!a.add_state(i64::MIN));
        assert!(!a.has_state(i64::MIN));
        assert_eq!(0, a.count_states());
    }

    #[test]
    fn test_remove_state_empty() {
        let mut a = Automate::new();
        assert!(!a.remove_state(0));
        assert!(!a.has_state(0));
    }

    #[test]
    fn test_remove_state_unknown_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(!a.remove_state(1));

        assert!(a.has_state(0));
        assert!(!a.has_state(1));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_remove_state_one_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.remove_state(0));

        assert!(!a.has_state(0));
        assert_eq!(0, a.count_states());
    }

    #[test]
    fn test_remove_state_all_states() {
        let mut a = Automate::new();
        let mut c = 0;
        for i in 0..=9 {
            assert!(a.add_state(i));
            assert!(a.has_state(i));
            c += 1;
            assert_eq!(c, a.count_states());
        }

        for i in 0..=9 {
            assert!(a.remove_state(i));
            assert!(!a.has_state(i));
            c -= 1;
            assert_eq!(c, a.count_states());
        }
    }

    #[test]
    fn test_remove_state_origin_in_transition() {
        let mut a = Automate::new();

        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_symbol('a'));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.remove_state(0));

        assert!(!a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(1, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_state_destination_in_transition() {
        let mut a = Automate::new();

        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_symbol('a'));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.remove_state(1));

        assert!(a.has_state(0));
        assert!(!a.has_state(1));
        assert_eq!(1, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_state_origin_and_destination_in_transition() {
        let mut a = Automate::new();

        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_symbol('a'));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.remove_state(0));
        assert!(a.remove_state(1));

        assert!(!a.has_state(0));
        assert!(!a.has_state(1));
        assert_eq!(0, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_state_state_in_transition() {
        let mut a = Automate::new();

        for i in 0..=4 {
            assert!(a.add_state(i));
        }
        a.set_state_initial(0);
        a.set_state_initial(1);
        a.set_state_final(1);
        a.set_state_final(4);

        assert!(a.add_symbol('a'));
        assert!(a.add_symbol('b'));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.add_transition(0, 'a', 2));
        assert!(a.add_transition(0, 'a', 3));
        assert!(a.add_transition(1, 'b', 3));
        assert!(a.add_transition(2, 'a', 3));
        assert!(a.add_transition(2, 'b', 4));
        assert!(a.add_transition(3, 'a', 3));
        assert!(a.add_transition(3, 'b', 4));
        assert!(a.add_transition(4, 'a', 4));
        assert!(a.remove_state(3));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert!(a.has_state(2));
        assert!(!a.has_state(3));
        assert!(a.has_state(4));
        assert_eq!(4, a.count_states());
        assert!(a.has_symbol('a'));
        assert!(a.has_symbol('b'));
        assert_eq!(2, a.count_symbols());
        assert!(a.has_transition(0, 'a', 1));
        assert!(a.has_transition(0, 'a', 2));
        assert!(!a.has_transition(0, 'a', 3));
        assert!(!a.has_transition(1, 'b', 3));
        assert!(!a.has_transition(2, 'a', 3));
        assert!(a.has_transition(2, 'b', 4));
        assert!(!a.has_transition(3, 'a', 3));
        assert!(!a.has_transition(3, 'b', 4));
        assert!(a.has_transition(4, 'a', 4));
        assert_eq!(4, a.count_transitions());
    }

    #[test]
    fn test_has_state_empty() {
        let a = Automate::new();
        assert!(!a.has_state(0));
    }

    #[test]
    fn test_has_state_already_in() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.has_state(0));
    }

    #[test]
    fn test_has_state_not_in() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(!a.has_state(2));
    }

    #[test]
    fn test_count_states_empty() {
        let a = Automate::new();
        assert_eq!(0, a.count_states());
    }

    #[test]
    fn test_count_states_not_empty() {
        let mut a = Automate::new();
        for i in 0..=3 {
            assert!(a.add_state(i));
            assert_eq!(i + 1, a.count_states());
        }
    }

    #[test]
    fn test_set_initial_state_one_initial_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        a.set_state_initial(0);
        assert!(a.is_state_initial(0));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_set_initial_state_to_final_and_initial() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        a.set_state_final(0);
        a.set_state_initial(0);
        assert!(a.is_state_initial(0));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_set_initial_state_two_initial_states() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        a.set_state_initial(0);
        a.set_state_initial(1);
        assert!(a.is_state_initial(0));
        assert!(a.is_state_initial(1));
        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
    }

    #[test]
    fn test_set_initial_state_unknwon_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        a.set_state_initial(1);
        assert!(!a.is_state_initial(1));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_set_final_state_one_final_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        a.set_state_final(0);
        assert!(a.is_state_final(0));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_set_final_state_two_final_states() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        a.set_state_final(0);
        a.set_state_final(1);
        assert!(a.is_state_final(0));
        assert!(a.is_state_final(1));
        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
    }

    #[test]
    fn test_set_final_state_to_final_and_initial() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        a.set_state_final(0);
        a.set_state_initial(0);
        assert!(a.is_state_final(0));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_set_final_state_unknwon_state() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        a.set_state_final(1);
        assert!(!a.is_state_final(1));
        assert!(a.has_state(0));
        assert_eq!(1, a.count_states());
    }

    #[test]
    fn test_add_transition_unknown_symbol() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(!a.add_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(!a.has_symbol('a'));
        assert_eq!(0, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_add_transition_unknown_origin() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(1));
        assert!(!a.add_transition(0, 'a', 1));

        assert!(!a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(1, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_add_transition_unknown_target() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(!a.add_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(!a.has_state(1));
        assert_eq!(1, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_add_transition_one_transition() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(a.has_transition(0, 'a', 1));
        assert_eq!(1, a.count_transitions());
    }

    #[test]
    fn test_add_transition_two_identical_transitions() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(!a.add_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(a.has_transition(0, 'a', 1));
        assert_eq!(1, a.count_transitions());
    }

    #[test]
    fn test_add_transition_same_origin_and_letter() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_state(2));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.add_transition(0, 'a', 2));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert!(a.has_state(2));
        assert_eq!(3, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(a.has_transition(0, 'a', 1));
        assert!(a.has_transition(0, 'a', 2));
        assert_eq!(2, a.count_transitions());
    }

    #[test]
    fn test_add_transition_same_origin_and_destination() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_symbol('b'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.add_transition(0, 'b', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(a.has_symbol('a'));
        assert!(a.has_symbol('b'));
        assert_eq!(2, a.count_symbols());
        assert!(a.has_transition(0, 'a', 1));
        assert!(a.has_transition(0, 'b', 1));
        assert_eq!(2, a.count_transitions());
    }

    #[test]
    fn test_add_transition_same_letter_and_destination() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_state(2));
        assert!(a.add_transition(0, 'a', 2));
        assert!(a.add_transition(1, 'a', 2));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert!(a.has_state(2));
        assert_eq!(3, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(a.has_transition(0, 'a', 2));
        assert!(a.has_transition(1, 'a', 2));
        assert_eq!(2, a.count_transitions());
    }

    #[test]
    fn test_add_transition_epsilon() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, Automate::EPSILON, 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(!a.has_symbol(Automate::EPSILON));
        assert_eq!(0, a.count_symbols());
        assert!(a.has_transition(0, Automate::EPSILON, 1));
        assert_eq!(1, a.count_transitions());
    }

    #[test]
    fn test_remove_transition_unknown_symbol() {
        let mut a = Automate::new();
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(!a.remove_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(!a.has_symbol('a'));
        assert_eq!(0, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_transition_unknown_origin() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(1));
        assert!(!a.add_transition(0, 'a', 1));

        assert!(!a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(1, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_transition_unknown_target() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(!a.add_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(!a.has_state(1));
        assert_eq!(1, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_transition_empty() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(!a.remove_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert_eq!(2, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_remove_transition_one_transition() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_state(2));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.add_transition(0, 'a', 2));
        assert!(a.remove_transition(0, 'a', 1));

        assert!(a.has_state(0));
        assert!(a.has_state(1));
        assert!(a.has_state(2));
        assert_eq!(3, a.count_states());
        assert!(a.has_symbol('a'));
        assert_eq!(1, a.count_symbols());
        assert!(a.has_transition(0, 'a', 2));
        assert!(!a.has_transition(0, 'a', 1));
        assert_eq!(1, a.count_transitions());
    }

    #[test]
    fn test_remove_transition_success() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.remove_transition(0, 'a', 1));
        assert!(!a.has_transition(0, 'a', 1));
    }

    #[test]
    fn test_has_transition_empty() {
        let mut a = Automate::new();
        assert!(!a.has_transition(0, 'a', 1));
    }

    #[test]
    fn test_has_transition_dont_have_this_transition() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(!a.has_transition(1, 'a', 0));
    }

    #[test]
    fn test_has_transition_dont_have_symbol() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(!a.has_transition(1, 'b', 0));
    }

    #[test]
    fn test_has_transition_dont_have_state_from() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(!a.has_transition(2, 'a', 1));
    }

    #[test]
    fn test_has_transition_dont_have_state_to() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(!a.has_transition(1, 'a', 2));
    }

    #[test]
    fn test_has_transition_success() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert!(a.has_transition(0, 'a', 1));
    }

    #[test]
    fn test_count_transition_empty() {
        let mut a = Automate::new();
        assert_eq!(0, a.count_transitions());
    }

    #[test]
    fn test_count_transition_not_empty() {
        let mut a = Automate::new();
        assert!(a.add_symbol('a'));
        assert!(a.add_state(0));
        assert!(a.add_state(1));
        assert!(a.add_transition(0, 'a', 1));
        assert_eq!(1, a.count_transitions());
    }
}
