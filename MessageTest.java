import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    //junit test case with the format given below
    /*
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


     */
    @Test(timeout = 1000)
    public void testMessage() {
        // Set the input
        // Separate each input with a newline (\n).
        String input = "Line One\nLine Two\n";

        // Pair the input with the expected result
        String expected = "Insert the expected output here";

        // Runs the program with the input values
        // Replace TestProgram with the name of the class with the main method
        receiveInput(input);


        // Retrieves the output from the program
        String stuOut = getOutput();

        // Trims the output and verifies it is correct.
        stuOut = stuOut.replace("\r\n", "\n");
        assertEquals("Error message if output is incorrect, customize as needed",
                        expected.trim(), stuOut.trim());

    }

    private void receiveInput(String input) {
    }
}
