# Project 3: 

* Authors: Wesley Brown, Ethan Frech
* Class: CS361 Section 002
* Semester: Fall 2021

## Overview

This projevt focused on constructing an NFA from a given regular expression.  We used recursive parsing to break up the regex and and split it into different NFAs then join the back into a single one.

## Compiling and Using

To compile re.REDriver then you must be in the top directory of the files and ensure that jar and tests are installed. Then run the following command.

$ javac -cp ".:./CS361FA.jar" re/REDriver.java

This will compile the driver then you can run it with a given test using the following command structure.

$ java -cp ".:./CS361FA.jar" re.REDriver ./tests/p3tc1.txt

This should print to console the test which you chose to run.

## Discussion

We were able to test our project thouroly using the test cases given in the zip folder.  With this we were easily able to work through any of the issues we found.

During development we had two specific issues with our code.  The first issue that we had was with our state transistions.  Our code was adding an extra e transision to the end of each sequence which we were able to learn about when we started testing.  To solve this we used a series of print statments to locate the line of code which was causing this issue.  This was hard to do because the way in which the methods interact with eachother made it hard to find the differences.

The second issue that we found in our code was based on the interaction with peek and more.  We found that there was an index out of bounds error with peek as it would look out side of the string for the next character but would err out.  We found where the issues were coming from using print statments again and using more to check the location within the regex string inorder to not err.

After fixing these issues we were able to easily check our code against the tests and found that it worked correctly.

## Sources used

- http://matt.might.net/articles/parsing-regex-with-recursive-descent/ 
- We used this to help us understand how to use recursion to create the base line for our recusion
- We looked through the Driver and Interface classes to help with our initial understanding of how the code would work
- We also used the tests to walk through our code at points when our testing looked wrong
