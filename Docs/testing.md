Testing is contained within four classes using junit tests ran in Intellij. Those four classes are the following:
1. [MessageTest.java](../MessageTest.java)
2. [MessageDatabaseTest.java](../MessageDatabaseTest.java)
3. [UserTest.java](../UserTest.java)
4. [UserDatabaseTest.java](../UserDatabaseTest.java)
5. [MessageInterfaceTest.java](../MessageDatabaseInterfaceTest.java)
6. [UserInterfaceTest.java](../UserInterfaceTest.java)
7. [MessageDatabaseInterfaceTest.java](../MessageDatabaseInterfaceTest.java)
8. [UserDatabaseInterfaceTest.java](../UserDatabaseInterfaceTest.java)
9. [BadDataExceptionTest.java](../BadDataExceptionTest.java)

Each of these files contain individual test cases for each method contained within the file. Further, the MessageDatabaseTest.java and UserDatabaseTest.java files contain comprehensive test cases that manipulate a created database and look at the wholistic representation.

The first four files contain tests for all of the methods implemented to help run and alter the database of the social media app. These are the most important tests at the current stage of the project as they simulate the actuall running of the project. The following classes (5-8) represent all of the interface test cases and simply make sure that the classes used to handle the database implement the appropriate test cases. Individual methods do not need to be tested here as all methods in the interface must be implemented in order for the class to compile.

The final file (BadDataExceptionTest) checks to make sure that BadDataException implements the correct interface and that the excpetion works when it is thrown. 