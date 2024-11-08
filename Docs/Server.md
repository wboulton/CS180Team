# MediaServer.java
This file handles all of the server functions for this social media application. It runs a main method which waits for new users/clients to try to connect to the server. When a new client connects to the server, it creates a new thread that interacts with that client exclusively. 

This class has two fields: 
```java
    private static UserDatabase database;
    public static final Object lock = new Object();
```
The first field is just the user database which is used to find the client and verify their userinformation. The second field is just the object which is used for thread synchronization. 
