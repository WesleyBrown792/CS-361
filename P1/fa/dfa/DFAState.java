package fa.dfa;

import java.util.HashMap;
import fa.State;

public class DFAState extends State{
    boolean startState = false;
    boolean finalState = false;
    HashMap <Character, DFAState> fa = new HashMap<Character, DFAState>();

    void setTransition(Character C, DFAState nextState){
        this.fa.put(C, nextState);
    }

    DFAState getTransition(char current){
        return this.fa.get(current);
    }


    void setFinal(boolean current){
        finalState = current;
    }

    boolean getFinal(){
        return finalState;
    }

    void setStart(boolean current){
        startState = current;
    }

    boolean getStart(){
        return startState;
    }

    void setName(String name){
        this.name = name;
    }

}
