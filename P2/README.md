# Project 2: 

* Authors: Wesley Brown, Ethan Frech
* Class: CS361 Section 002
* Semester: Fall 2021

## Overview

This is a java program that
allows for the input of the 
alphabet, states, and transitions
of an NFA to be turned into a DFA

## Compiling and Using

To compile and use this ensure that you are
in the topmost directory.  Then using the testcases
as examples you can input an NFA.

To compile use:
$ javac fa/nfa/NFADriver.java

To run a testcase use:
$ java fa.nfa.NFADriver ./tests/p2tc#.txt
Note the hashtag is the number of the testcase

This will output information of the transformation 
from NFA to DFA, using data formatted like the
testcases you can make your own NFAs

## Discussion

We were able to get a strong start on this project at the beginning because of how thoroughly we looked through the code.  With this we created a NFAState class which even before testing we know we had done right as we used both the provided state class as well as our previous DFAState class as a good baseline for.

It was only once we got to the eClosure and getDFA that we had any serious issues.  We got stuck on how to approach translating an NFA to DFA but were able to make it work using what we learned in class as well as some guess and check with testing.

The other issue that we ran into while testing is the fact that when we ran the testcases it would be technically correct but for some reason the states would be in the wrong order.  For example, when using the first case it should have [a, b] as the empty string state but when we run it our state is [b, a]. 

We chalked this up to the fact that when using a linked hash set it is technically not ordered and therefore comes down to the implementation of it.  We believe that these two states really just mean the same thing with regards to what it represents and chose not to focus on this aspect and ensure that our code worked correctly.

This project was easy to start but had some issues popup during production with certain methods.  We also thought that the use of Javadoc comments helped us better understand the methods that the other team member wrote.

## Sources used

- We started by looking at the NFADriver, State, and DFA classes to help us better understand the code we were working with.
- We also used sites like geeks4geeks and stack overflow to help with ensuring our usage of various methods, datatypes, and inheritance.
