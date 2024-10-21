# CS180Team
Team project for CS180

Read This First
This is a team project. You must contact your team members on your chosen communication platform to verify your participation status by Friday, October 25th. Failure to do so will result in removal from the team and a 0 on the Team Project. There is no option to complete the project individually. We highly recommend that you start early and communicate with your team members far before the deadline. Peer reviews will have a significant impact on your score. 

Additionally, you and your entire team should be attending lab for the rest of the semester. Your lab’s Lead TA will help you as a Project Manager, checking in and making sure things stay on track. 

Start early! 

Contact your lab’s Lead TA and CC the Head TA of your section if you have any concerns. For team-related issues, reach out as soon as possible. There might not be much that can be done if you reach out at the last moment. 

Description
This is an open-ended project, meaning that outside the minimum requirements your team is free to design the software as they see fit. The assignment for the team is to build a fully functioning Social Media Platform. You can choose to model your software off an existing social media platform, like Facebook or LinkedIn, or you could choose to design something entirely new that meets the requirements. Every team needs to implement some form of user profile, a way to search for users, a way to view users, and a way to add, remove, and block friends. Each team can then choose to implement either a news feed or a direct messaging system. Any additional features are at the discretion of the team. Teams will consist of 4-5 students from the same lab.  Grading requirements will be adjusted according to how many members are on a team. A team of 5 will be expected to submit software of a higher quality than a team consisting of 4 members due to the extra resources at their disposal. Functionality and appearance will both be a part of the grading criteria for the project.

The minimum technical requirements for the project are as follows:

Every program class must have a dedicated interface.
Every class must have its own comprehensive JUnit test case.
The software must be implemented over Network IO.
All computation must be carried out on the server side of the software.
The server must support multiple client logins.
The server/database must be thread safe.
All data must be stored in a server-side database.
The database must persist after reboots (reads and writes data to hard disk from memory).
The client must have a fully functional GUI. Note: A complex, fully functional GUI is required. You may only use simple GUIs where they are appropriate.
The software should not crash - major crashes could result in substantial loss of points. 
There are likely phrases above that you do not recognize, that is fine, they will be in upcoming assignments and lectures. The CS 180 Team Project is divided into 3 phases. Each phase builds upon the last culminating in a fully functional program.

Functionality requirements:

User profiles.
New user account creation.
Password protected login.
User search.
User viewer.
Add, block, and remove friend features.
Extra credit opportunity – Add support to upload and display profile pictures.
At least one of:

Direct messaging
Send and delete messages.
Block users from sending messages (see block user above).
Restrict messages to either all users or friends only (see add friend above).
Extra credit opportunity – Add photo messaging.
News feed
Allow users to make posts.
Display all friends' posts in a feed.
Upvote, downvote, and hide posts from feed.
Enable comment on posts.
Anyone that can view a comment can upvote or downvote it.
The post owner and comment owner can delete the comment.
Extra credit opportunity – Add photo posts to news feed.
The first phase of the project consists of building the entire database side of the project including interfaces and test cases. Forward thinking is strongly encouraged. Questions such as: What are we storing? How are we accessing it? What are we calculating? All should be addressed during this stage. Project 3 was an example of a database. HW10 covers techniques on how to make the database thread safe. Remember the database will only be interacting with a single server; however, that server will be interacting with multiple clients simultaneously. A README.txt file is required with each phase and should be submitted in the Vocareum workspace alongside any java files. See documentation bellow.

The second phase will be to design and implement the server by linking it to the database and to start work on the client side of the program. Again, forward thinking is strongly encouraged, remember the team will be required to add a GUI to their client. The client should only communicate with the server, all computation should be handled on the server side of the software. A README.txt file is required with each phase and should be submitted in the Vocareum workspace alongside any java files. See documentation bellow.

The third and final phase will be to design and implement a GUI for the client. This will be the user facing side of your team’s social media platform. This will also be the last chance for the team to make any changes to previously implemented parts of the program. A README.txt file is required with each phase and should be submitted in the Vocareum workspace alongside any java files. See documentation bellow.

Each stage will be graded individually; however, you can change the contents between stages. For example: The team discovers their database needs a new feature during phase 3, that feature could be added then. Remember to modify the required interfaces and test cases accordingly.

The last two parts of the project are a written report and a team presentation. The grading breakdown for the entire project is as follows:

Phase 1: 2.5%
Phase 2: 2.5%
Phase 3: 5%
Presentation: 2.5%
Report: 2.5%
This project is worth a total 15% of your final grade. We recommend that you take it, along with the other projects in the class, very seriously. 

Team Work Expectations
Teams will consist of 4-5 students in the same lab. We expect each member of every team to contribute to the project equally. You are permitted to divide the work in any way you see fit as long as responsibilities are evenly distributed, and every team member contributes to the project source code. You will be required to document your individual contributions to the project in the final report. 

Each of you must send a message to your teammates by Friday, October 25th, 11:59 pm.  If a team member misses this deadline and is reported for doing so, a team meeting will be scheduled with the project manager. The individual who was reported for missing the deadline will be required to provide evidence of sending a message before the deadline. If they are unable to do so, they will be removed from the team and receive a 0 on the team project. There are no exceptions to this policy. 

You may use any chat or communication platform to coordinate your development efforts. Regardless of your choice of communication tool, you must contact your teammates by Friday, October 25th, 11:59 pm. 

Be aware that team collaboration is limited to the members of your team. You should not be sharing code with individuals outside of your team. All work must be your own. Copying code from the internet is academic dishonesty, as is requesting help from someone (other than a CS 18000 TA) who is not on your team. Any team member who is caught copying code will receive a 0 on the project and the other team members will be given an opportunity to continue without them.  Remember to follow the course academic honesty policy. If you have concerns about whether or not something is okay, just ask us. 

To simplify collaboration, you must make use of a Code Repository on Gitlab or Github. It will make sharing code, tracking changes, and debugging significantly easier. However, keep in mind that any repository you use must be private, with access limited only to members of your team. Code made available publicly is academic dishonesty. You will be required to submit a copy of your repository on Vocareum by cloning it into your work folder. 

Cloning the repository on Vocareum follows the same process as cloning it locally. Use the Linux terminal in your workspace and enter the appropriate commands! 
Alternatively, you may upload a zip folder with the repository. 
Note: Every team member must commit to the repository. A lack of commits may be used as evidence that a team member did not participate. 

Any team member who fails to contribute will receive a 0 on the project.

We reserve the right to modify your grade based on participation. You may receive a 0 for not contributing at all, or you may receive a 75% deduction on your team score for only contributing superficial content. If you wait until the last minute to work on the assignment, you will receive a 0 or a significant deduction as well. These cases will be judged on a case-by-case basis using evidence provided by teams. 

Your team will share a workspace on Vocareum. Only one team member needs to submit. Every team member is required to visit the workspace via the Brightspace link at least once to get a grade. Each phase will have a dedicated workspace on vocareum. It is your responsibility to ensure you are in the correct workspace and that you log into the workspace at least once.

In the event of a team disagreement, dispute, or lack of participation from any individual, you should contact your project manager as soon as possible. We can only help if we are aware of the situation. Please reach out, even if it is only to notify us that you are worried that an issue may develop. 

Details about Peer Evaluations will be announced after you begin working. 

Role Distribution

Every team member needs to work on this project. Before you begin coding, we highly recommend that you meet and identify roles for everyone. These roles can change from phase to phase. Here is an example for phase 1: 

Role 1: Project lead, interface designer, interface test cases.
Role 2: Develop database objects and test cases.
Role 3: Develop methods and application control flow.
Role 4: Develop log in details and user permissions. 
Role 5: Develop data persistence solutions and test cases. 
You can define the roles in any way that your team prefers. Two team members may work on the same topic. Alternatively, you may find it simpler to have everyone complete a specific task separately. Communication will be key! A project leader is recommended to facilitate each phase. The project leader doesn’t have to change from phase to phase, but it is recommended. It is also suggested to have that phase’s project leader oversee the maintenance of that phase’s content through the remaining phases.

Remember, this is a team project. Your project score will reflect your contribution to the team. 
Testing
There are no public test cases for this project. Each implementation will look different, and we do not want to restrict your creativity in any way. 

However, you are expected to write your own custom test cases, specific to your team's implementation. We've given you examples of what this looks like in all your previous assignments.  Here's an example: 

@Test(timeout = 1000)
public void testExpectedOne() {
    // Set the input        
    // Separate each input with a newline (\n). 
    String input = "Line One\nLine Two\n"; 

    // Pair the input with the expected result
    String expected = "Insert the expected output here" 

    // Runs the program with the input values
    // Replace TestProgram with the name of the class with the main method
    receiveInput(input);
    TestProgram.main(new String[0]);

    // Retrieves the output from the program
    String stuOut = getOutput();

    // Trims the output and verifies it is correct. 
    stuOut = stuOut.replace("\r\n", "\n");
    assertEquals("Error message if output is incorrect, customize as needed",
                    expected.trim(), stuOut.trim());

}
The test cases should do the following: 

Verify all fields, constructors, and methods function properly.
Methods and constructors should have error tests to verify they do not crash when receiving invalid input, where applicable.
Data that persists should be validated with appropriate test cases. 
When designing your implementation, be sure to use methods appropriately. It will be challenging to design test cases for overly complex methods. 

Presentation
Along with Phase 3 your team will record and submit a video presentation as part of this project. The requirements are as follows: 

Each team member must actively contribute to the presentation for at least two minutes. 
The overall presentation will be a minimum of 10 minutes and a maximum of 15 minutes.
The presentation must include the following: 
An overall pitch for the project (as in, try to sell the product to a prospective client). 
A demo of the project's functionality.
An explanation of the testing done to ensure the project works reliably.
 An audio-visual component (Powerpoint, Google Slides, Prezi, movie, etc.) 
2 minutes of time allotted to an FAQ section of questions about the project and your implementation. Answer 2-3 questions a recruiter or business manager would be interested in. 
Nearly every CS job interview (and many actual jobs) involve presentations along these lines. Use this opportunity to practice the skills you will need in interviews for internships and post-graduation positions. 

Report
In addition to your presentation, you must submit a project report. There will be two parts. 

Part One
Part One is a minimum of 1000 words and requires the following: 

A minimum of 500 words describing your project and the functionality you implemented.
A minimum of 500 words documenting your design choices while implementing the project. Justify the project structure.
Part Two
Each team member must write a minimum of 350 words on the following:

A minimum of 200 words describing your contributions to the project. 
A minimum of 150 words on what you would do differently, if anything, if you were given the opportunity to start over again. If you would not do anything differently, explain why. 
Each team member's section should be labelled with that individual's name. 
Note: Be sure your document does not have any major grammar or structural errors. You are not required to adhere to any writing style guide (for example, APA), but your document should be organized following the guidelines listed above.  You can use the following structure for a general outline: 

A cover page with the report title and all team member names listed
A section labelled "Part 1" on a new page with the information described above (including as many pages as necessary). 
A section labelled "Part 2" on a new page with the information described above (including as many pages as necessary). 
We recommend using a 12 point font such as Times New Roman and double spacing. All section and subsection labels should be bolded.  
Documentation
Your project must follow Coding Style (it will be 5 points of your solution grade). You should also document your code with comments explaining the functionality you implement. Not only will your teammates appreciate it if they have to debug, but you will also make it simpler to explain why you chose to implement it that way if asked by your Project Manager. 

Additionally, your project will need to have a README document. This document will include the following in order:

Instructions on how to compile and run your project. 
A list of who submitted which parts of the assignment on Brightspace and Vocareum. 
For example: Student 1 - Submitted Report on Brightspace. Student 2 - Submitted Vocareum workspace.
A detailed description of each class. This should include the functionality included in the class, the testing done to verify it works properly, and its relationship to other classes in the project. 
You can format the README however you like, provided it is written as markdown file (.md). 

Submit
Each phase needs to be submitted in the proper Vocareum workspace by the due date.
Phase 1 – Starts Monday Oct 21 at 6PM – Ends Sunday Nov 3 at 11:59PM - Late until Monday Nov 4 at 11:59PM.
Phase 2 – Starts Monday Nov 4 at 6PM – Ends Sunday Nov 17 at 11:59PM – Late until Monday Nov 18 at 11:59PM.
Phase 3 – Starts Monday Nov 18 at 6PM – Ends Sunday Dec 8 at 11:59PM – Late until Monday Dec 9 at 11:59PM.
Presentation and Report are due  Sunday Dec 8 at 11:59PM – Late until Monday Dec 9 at 11:59PM. 

Here's a breakdown of grading for Phase 1: 

Database code (40 points)
Interfaces for each class (15 points)
Related test cases (30 points)
Documentation (5 points)
Everything must be submitted to Vocareum by cloning the repository. All required files should be included in the repository.
Up to 5 points of extra credit can be earned by designing the database to store user photos and/or photo posts/messages. Extra credit can not make up for lost points on the peer evaluation or coding style portions nor can it take the total score over 100.
The peer evaluation form. (5 points).
Coding Style (5 points)
Remember ALL team members must visit the Vocareum workspace at least once to receive a grade.
