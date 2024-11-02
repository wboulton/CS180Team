```java
Thread one = new Thread(() -> FUNCTION(PARAMETERS));
Thread two = new Thread(() -> FUNCTION(PARAMETERS));
```
This code creates a thread without having to create an object, it just creates a new thread object with the run method piped from the function that you pass into it. 
