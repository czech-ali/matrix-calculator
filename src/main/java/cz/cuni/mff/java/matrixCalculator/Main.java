/**
 * The Main class contains the main method to run the Matrix Library command-line interface.
 * This class reads user input from the console, passes it to the CommandLineInput class for evaluation,
 * and prints the resulting matrix to the console. If an error occurs during input evaluation, an error message
 * is printed to the console.
 */

package cz.cuni.mff.java.matrixCalculator;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    /**
     * The main method of the Matrix Library command-line interface.
     * This method reads user input from the console, passes it to the CommandLineInput class for evaluation,
     * and prints the resulting matrix to the console. If an error occurs during input evaluation, an error message is printed
     * to the console. "^Q" and "^q" terminate the program.
     *
     * @param args accepts one optional argument "brackets".
     *             If the argument is present, all matrix results are printed in the same format as the input
     */
    public static void main(String[] args) {
        // output uses matrix brackets
        boolean brackets = false;

        // determine, what kind of output the user wants
        if (args.length == 1 && Objects.equals(args[0], "brackets"))
            brackets = true;
        else if (args.length > 1)
            throw new IllegalArgumentException("Program takes at most only one argument");
        else if (args.length > 0 && !Objects.equals(args[0], "brackets"))
            throw new IllegalArgumentException("Illegal argument");

        while (true) {
            try {
                Scanner input = new Scanner(System.in);
                String data = input.nextLine();
                if (Objects.equals(data, "^Q") || Objects.equals(data, "^q"))
                    return;
                Matrix result = new CommandLineInput().evaluateTerm(data);
                if (brackets)
                    System.out.println(result.stringRepresentationWithBrackets());
                else
                    System.out.println(result);
            } catch (InvalidParameterException invalidParameter) {
                System.out.println(invalidParameter.getMessage());
            } catch (IllegalArgumentException illegalArgument) {
                System.out.println(illegalArgument.getMessage());
            } catch (UnsupportedOperationException unsupportedOperation) {
                System.out.println(unsupportedOperation.getMessage());
            } catch (Exception exception) {
                System.out.println("Inconsistent input");
            }
        }
    }
}