package re;

import java.util.Set;
import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

public class RE implements REInterface {

    private int stateIndex;
    private char currentTransition;
    private String regex;

    public RE(String input) {
        stateIndex = 0;
        regex = input;
        currentTransition = 'e';
    }

    /**
     * @return NFA
     */
    public NFA getNFA() {
        return regex();
    }

   

    /**
     * @return char
     */
    private char peek() {
        return regex.charAt(0);
    }

    /**
     * @param c
     */
    private void eat(char c) {
        if (peek() == c) {
            regex = regex.substring(1);
        } else {
            throw new RuntimeException("Expected: " + c + "; got: " + peek());
        }
    }

    /**
     * @return char
     */
    private char next() {
        char c = peek();
        eat(c);
        return c;
    }

    /**
     * @return boolean
     */
    private boolean more() {
        return regex.length() > 0;
    }

     /**
     * @param nfa1
     * @param nfa2
     * @return NFA
     */
    private NFA join(NFA nfa1, NFA nfa2) {
        for (State state1 : nfa2.getStates()) {
            nfa1.addState(state1.getName());
        }
        nfa1.addAbc(nfa2.getABC());
        for (State state1 : nfa2.getStates()) {
            Set<Character> alphabetTracker = nfa2.getABC();
            alphabetTracker.add('e');
            for (char character : alphabetTracker) {
                Set<NFAState> toState = nfa2.getToState((NFAState) state1, character);
                for (State state2 : toState) {
                    nfa1.addTransition(state1.getName(), character, state2.getName());
                }
            }
        }
        return nfa1;
    }

  

    /**
     * @return NFA
     */
    private NFA base() {
        switch (peek()) {
            case '(':
                eat('(');
                NFA regex = regex();
                eat(')');
                return regex;
            default:
                stateIndex++;
                NFA done = new NFA();
                currentTransition = next();
                if (more() && peek() == '*') {
                    done.addFinalState(String.valueOf(stateIndex));
                    done.addTransition(String.valueOf(stateIndex), currentTransition, String.valueOf(stateIndex));
                }
                done.addStartState(String.valueOf(stateIndex));
                return done;
        }
    }

    /**
     * @return NFA
     */
    private NFA factor() {
        NFA base = base();
        if (more() && peek() == '*') {
            eat('*');
            for (State state : base.getFinalStates()) {
                base.addTransition(state.getName(), 'e', base.getStartState().getName());
            }
            base.addFinalState(base.getStartState().getName());
        } else {
            String fromState = String.valueOf(stateIndex);
            stateIndex++;
            base.addState(String.valueOf(stateIndex));
            base.addTransition(fromState, currentTransition, String.valueOf(stateIndex));
            base.addFinalState(String.valueOf(stateIndex));
        }
        return base;
    }

    /**
     * @return NFA
     */
    private NFA term() {
        NFA factor = factor();
        while (more() && peek() != ')' && peek() != '|') {
            NFA nextFactor = factor();
            NFA temp = new NFA();
            temp = join(temp, factor);
            temp = join(temp, nextFactor);
            temp.addStartState(factor.getStartState().getName());
            for (State state : factor.getFinalStates()) {
                temp.addTransition(state.getName(), 'e', nextFactor.getStartState().getName());
            }
            for (State state : nextFactor.getFinalStates()) {
                temp.addFinalState(state.getName());
            }
            factor = temp;
        }
        return factor;
    }

      /**
     * @return NFA
     */
    private NFA regex() {
        NFA term = term();
        if (more() && peek() == '|') {
            eat('|');
            NFA regex = regex();
            NFA temp = new NFA();
            stateIndex++;
            temp.addStartState(String.valueOf(stateIndex));
            temp = join(temp, term);
            temp = join(temp, regex);
            temp.addTransition(temp.getStartState().getName(), 'e', term.getStartState().getName());
            temp.addTransition(temp.getStartState().getName(), 'e', regex.getStartState().getName());
            for (State state : term.getFinalStates()) {
                temp.addFinalState(state.getName());
            }
            for (State state : regex.getFinalStates()) {
                temp.addFinalState(state.getName());
            }
            return temp;
        } else {
            return term;
        }
    }

}
