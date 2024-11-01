User Data storage is handled by two different files, User.java and UserDatabase.java.

# User.java
This file handles all of the creation and modification of the user object. This includes handling friendships, blockages, usernames, and password validation. When creating an object, the User class obtains a username, password, firstname, lastname, and profile from the constructor and sets it to the appropriate field (all of which are strings). None of these strings allow commas. Every call to the constructor will have valid parameters passed to it, as all validation is done before creation of users. When users are stored in the database, they are stored in a csv line with the following format:
username,password