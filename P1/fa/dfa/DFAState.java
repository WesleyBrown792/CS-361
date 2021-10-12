package fa.dfa;

import java.util.HashMap;
import fa.State;

public class DFAState extends State{
    boolean startState = false;
    boolean finalState = false;
    HashMap <Character, DFAState> fa = new HashMap<Character, DFAState>();




    void ChangeFinal(boolean current){
        finalState = current;
    }

    boolean getFinal(){
        return finalState;
    }

    void ChangeStart(boolean current){
        startState = current;
    }

    boolean getStart(){
        return startState;
    }

    void setName(String name){
        this.name = name;
    }

}
