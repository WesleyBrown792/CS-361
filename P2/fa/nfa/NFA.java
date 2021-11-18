package fa.nfa;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.dfa.DFAState;
import fa.dfa.DFA;

/**
 *   This class implements the NFAInterface
 *   It also is responsble for allowing the driver to traverse
 *   a NFA using NFAStates
 *   @author Wesley Brown
 *   @author Ethan Frech
 */

public class NFA implements NFAInterface {
    private NFAState startState;
    private Set<NFAState> stateTracker;
    private Set<Character> alphabetTracker;

    public NFA() {

        stateTracker = new LinkedHashSet<NFAState>();
        alphabetTracker = new LinkedHashSet<Character>();
    }

    /**
     * Ensures that there is only one state of each name
     * @param name
     * @return boolean
     */
    private boolean doesContainState(String name) {
        boolean doesContain = false;
        for (NFAState state : stateTracker) {
            if (state.getName().equals(name)) {
                doesContain = true;
                break;
            }
        }
        return doesContain;
    }

    /**
     * Adds the start state of the NFA
     * Also adds the state to the stateTracker
     * @param name
     */
    @Override
    public void addStartState(String name) {
        if (!doesContainState(name)) {
            NFAState state = new NFAState(name);
            stateTracker.add(state);
            startState = state;
        }
    }

    /**
     * Adds a new NFAState to the NFA
     * Also adds the state to the stateTracker
     * @param name
     */
    @Override
    public void addState(String name) {
        if (!doesContainState(name)) {
            NFAState state = new NFAState(name);
            stateTracker.add(state);
        }

    }

    /**
     * Adds a final state to the NFA
     * Also adds the state to the stateTracker
     * This will aslo set a final state if the name
     * is the same as an already created state
     * @param name
     */
    @Override
    public void addFinalState(String name) {
        if (!doesContainState(name)) {
            NFAState state = new NFAState(name, true);
            stateTracker.add(state);
        } else {
            for (NFAState state : stateTracker) {
                if (state.getName().equals(name) && state == getStartState()) {
                    state.setFinalState(true);
                    break;
                }
            }
        }
    }

    /**
     * Adds transistions to and from the states listed
     * to the NFA
     * @param fromState
     * @param onSymb
     * @param toState
     * @throws NullPointerException
     */
    @Override
    public void addTransition(String fromState, char onSymb, String toState) throws NullPointerException {
        NFAState to = null;
        NFAState from = null;
        for (NFAState state : stateTracker) {
            if (state.getName().equals(fromState)){
                from = state;
                break;
            }
        }
        for (NFAState state : stateTracker) {
            if (state.getName().equals(toState)){
                to = state;
                break;
            }
                
        }

        if(to == null||from == null){
            System.out.println("Missing an NFA state with that name");
            System.exit(2);
        }
        

        from.addTransition(onSymb, to);

        if (!alphabetTracker.contains(onSymb) && onSymb != 'e') {
            alphabetTracker.add(onSymb);
        }
    }

    /**
     * Gets the states currently in the stateTracker
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> getStates() {
        return stateTracker;
    }

    /**
     * Gets the final states currently in the stateTracker
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> getFinalStates() {
        Set<NFAState> finalStates = new LinkedHashSet<NFAState>();
		for (NFAState state : finalStates) {
			if (state.getFinalState()) {
				finalStates.add(state);
			}
		}
		return finalStates;
    }

    /**
     * Gets the start state and returns it
     * @return State
     */
    @Override
    public NFAState getStartState() {
        return startState;
    }

    /**
     * Gets the alphabet being used and returns it
     * @return Set<Character>
     */
    @Override
    public Set<Character> getABC() {
        return alphabetTracker;
    }

    /**
     * Creates a DFA which is equivalent to the NFA being used
     * @return DFA
     */
    @Override
    public DFA getDFA() {
        //Create a DFA and a HashMap to store states
        DFA dfa = new DFA();
		LinkedHashMap<Set<NFAState>, String> DFAStates = new LinkedHashMap<>();  
		Set<NFAState> newStates = eClosure(startState);
        LinkedList<Set<NFAState>> stateQueue = new LinkedList<>();
		// Checks if there is a final state in the newStates
		boolean finalState = false;
		for (NFAState currState : newStates) {
			if (currState.getFinalState())
				finalState = true;
		}
        //adds the final and start states to the NFA
		DFAStates.put(newStates, newStates.toString());
		if (finalState == true)
			dfa.addFinalState(DFAStates.get(newStates));

		dfa.addStartState(DFAStates.get(newStates));
		stateQueue.add(newStates);

        //loops through all of the states from the NFA
		for(int i=0; i<1;)  {
            if(stateQueue.isEmpty()){
                i++;
            }else{
                newStates = stateQueue.remove();
                
                //checks each state with each letter of the language alphabet
                for (char currChar : alphabetTracker) {
                    LinkedHashSet<NFAState> machine = new LinkedHashSet<>();
                    for (NFAState currState : newStates) {
                        if (currState.getTo(currChar) != null) {
                            for (NFAState Estate : currState.getTo(currChar)) {
                                machine.addAll(eClosure(Estate));
                            }
                        }
                    }
                    while (!DFAStates.containsKey(machine)) {
                        System.out.println(machine.toString());
                        DFAStates.put(machine, machine.toString());
                        stateQueue.add(machine);
                        finalState = false;
                        for (NFAState check : machine) {
                            if (check.getFinalState())
                                finalState = true;
                        }
                        if (finalState) {
                            System.out.println(DFAStates);
                        }
                        if (!finalState) {
                            dfa.addState(DFAStates.get(machine));
                        }
                    }
                    dfa.addTransition(newStates.toString(), currChar, DFAStates.get(machine));
                }
            }
		}
		return dfa;
    }

    /**
     * returns the next states which can be reched with onSymb
     * @param from
     * @param onSymb
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    /**
     * Returns the states which use empty transistions
     * Also uses a method to recursivly check all possible states via
     * empty transistions
     * @param s
     * @return Set<NFAState>
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        //create a set to know what states have been gone too
        Set<NFAState> seenStates = new LinkedHashSet<NFAState>();

        eClosureWalk(s,seenStates);
        return seenStates;
    }

    public void eClosureWalk(NFAState s, Set<NFAState> seenStates){
        seenStates.add(s);
        Set<NFAState> N = s.getTo('e');
        if (N != null){
            for (NFAState currState : N) {
                if(!seenStates.contains(currState))
                    eClosureWalk(currState, seenStates);
            }
        }
    }

}
