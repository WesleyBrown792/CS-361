package fa.dfa;

import java.util.*;
import fa.State;

public class DFA implements DFAInterface{

    public Set<DFAState> States = new LinkedHashSet<DFAState>();
    public Set<DFAState> FStates = new LinkedHashSet<DFAState>();
    public Set<Character> Alphabet = new LinkedHashSet<Character>();
    DFAState StartS = new DFAState();

    @Override
    public void addStartState(String name) {
        this.StartS.setName(name);
        this.StartS.setStart(true);
        if(!States.contains(getState(name))){
            States.add(StartS);
        }else{
            if(getState(name).finalState)
                this.StartS.setFinal(true);
            getState(name).setStart(true);            
        }
    }

    @Override
    public void addState(String name) {
        if(!States.contains(getState(name))){
            DFAState nState = new DFAState();
            nState.setName(name);
            States.add(nState);
        }
    }

    @Override
    public void addFinalState(String name) {
        if(!States.contains(getState(name))){
            DFAState nState = new DFAState();
            nState.setName(name);
            nState.setFinal(true);
            States.add(nState);
            FStates.add(nState);
        }
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        Alphabet.add(onSymb);
        getState(fromState).setTransition(onSymb, getState(toState));
        if(fromState.equals(StartS.getName()))
            StartS = getState(fromState);
    }

    @Override
    public Set<? extends State> getStates() {
        return States;
    }

    @Override
    public Set<? extends State> getFinalStates() {
        return FStates;
    }

    @Override
    public State getStartState() {
        return StartS;
    }

    @Override
    public Set<Character> getABC() {
        return Alphabet;
    }

    @Override
    public boolean accepts(String s) {
        DFAState current = StartS;
        char[] input = s.toCharArray();
        int i=0;

        while(i<s.length()){
            char currChar = s.charAt(i);
            if(currChar != 'e')
                current = (DFAState) getToState(current, currChar);
            if(current == null)
                return false;
        }
        return current.finalState;
    }

    @Override
    public State getToState(DFAState from, char onSymb) {
        return from.getTransition(onSymb);
    }

    public DFAState getState(String name){
        return null; // needs to return the actual shit
    }

}