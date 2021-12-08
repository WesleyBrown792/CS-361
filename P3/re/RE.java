package re;

import java.util.Set;
import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

/**
 *   This class implements the REInterface to allow
 *   for the user to change a regular expression and
 *   build an NFA  
 * 
 *   @author Wesley Brown
 *   @author Ethan Frech
 */
public class RE implements REInterface {

    private int stateIndex;
    private char currentTransition;
    private String regex;

    /**
     * Using the input fillout the pirvate vars
     * @param input
     */
    public RE(String input) {
        stateIndex = 0;
        regex = input;
        currentTransition = 'e';
    }

    /**
     * Getter for the NFA
     * @return NFA
     */
    public NFA getNFA() {
        return regex();
    }

   

    /**
     * Checks the charAt() 0
     * @return char
     */
    private char peek() {
        return regex.charAt(0);
    }

    /**
     * removes the first char of the regex string
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
     * returns the value removed by eat
     * @return char
     */
    private char next() {
        char c = peek();
        eat(c);
        return c;
    }

    /**
     * checks to see if there is still more chars
     * in the regex string
     * @return boolean
     */
    private int more() {
        if(regex.length() > 0){
            return 1;
        }else{
            return 0;
        }
    }

     /**
      * Taking two NFAs and combines them into a single returned NFA
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
     * Creates and returns the base NFA
     * Also parses out () from the regex
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
                if (more() == 1 && peek() == '*') {
                    done.addFinalState(String.valueOf(stateIndex));
                    done.addTransition(String.valueOf(stateIndex), currentTransition, String.valueOf(stateIndex));
                }
                done.addStartState(String.valueOf(stateIndex));
                return done;
        }
    }

    /**
     * Builds the NFA when it finds *
     * within the regex
     * @return NFA
     */
    private NFA factor() {
        NFA base = base();
        String baseStart = base.getStartState().getName();
        if (more() == 1 && peek() == '*') {
            eat('*');
            for (State state : base.getFinalStates()) {
                base.addTransition(state.getName(), 'e', baseStart);
            }
            base.addFinalState(baseStart);
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
     * Builds the NFA so long as it does not reach
     * a (), |, or the end of the regex
     * @return NFA
     */
    private NFA term() {
        NFA firstFactor = factor();
        while (more() == 1 && peek() != '|' && peek() != ')') {
            NFA secondFactor = factor();
            NFA temp = new NFA();
            temp = join(temp, firstFactor);
            temp = join(temp, secondFactor);
            temp.addStartState(firstFactor.getStartState().getName());
            for (State state : firstFactor.getFinalStates()) {
                temp.addTransition(state.getName(), 'e', secondFactor.getStartState().getName());
            }
            for (State state : secondFactor.getFinalStates()) {
                temp.addFinalState(state.getName());
            }
            firstFactor = temp;
        }
        return firstFactor;
    }

    /**
     * Builds the NFA for the values following
     * a | found in the regex
     * @return NFA
     */
    private NFA regex() {
        NFA term = term();
        if (more() == 1 && peek() == '|') {
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
