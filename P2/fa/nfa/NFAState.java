package fa.nfa;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.LinkedHashSet;

import fa.State;
import fa.dfa.DFAState;

/**
 *   This class extends state to be used by NFA as state objects
 *   This class allows for NFA.java to use correct states
 *   @author Wesley Brown
 *   @author Ethan Frech
 */

public class NFAState extends State {
    private HashMap<Character, LinkedHashSet<NFAState>> delta;
    private boolean finalState;

    /**
     * Below are the two constructor types
     * The first takes in the name of the state while the other takes in the name
     * and if that state is a final state
     * @param name
     */
    public NFAState(String name) {
        newState(name);
        finalState = false;
    }

    /**
     * 
     * @param name
     * @param finalState
     */
    public NFAState(String name, boolean finalState) {
        newState(name);
        this.finalState = finalState;
    }

    /**
     * Adding of a new state
     * @param name
     */
    private void newState(String name) {
        this.name = name;
        delta = new HashMap<Character, LinkedHashSet<NFAState>>();
    }
  
    /**
     * Used as the getter for finalState
     * @return boolean
     */
    public boolean getFinalState() {
        return finalState;
    }
    
    /** 
     * Used as the setter for the finalState
     * @param finalState
     */
    public void setFinalState(Boolean finalState) {
        this.finalState = finalState;
    }

    /**
     * Sets up the transistions from this state
     * @param onSymb
     * @param toState
     */
    public void addTransition(char onSymb, NFAState toState) {
        LinkedHashSet<NFAState> transitions = new LinkedHashSet<NFAState>();
        if (delta.containsKey(onSymb)) {
            transitions.addAll(delta.get(onSymb));
        }
        transitions.add(toState);
        delta.put(onSymb, transitions);
    }

    /**
     * Gets all of the next states from this states transisions
     * @param symb
     * @return Set<NFAState>
     */
    public Set<NFAState> getTo(char symb) {
        LinkedHashSet<NFAState> deltaSet = new LinkedHashSet<NFAState>();
        deltaSet = delta.get(symb);
        return deltaSet;

    }

}
