/**
 * The CommandLineInput class is responsible for parsing user input of a mathematical expression
 * containing matrices and evaluating it using a TermEvaluater object. It also contains methods for
 * identifying unary and binary operators and for converting a string representation of a matrix
 * into a Matrix object. The class keeps track of the state of parsing, including the current
 * unary operation, whether a matrix has been opened or closed, the current matrix values, the
 * number of rows and columns in a matrix, and the length of each row.
 */

package cz.cuni.mff.java.matrixCalculator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Objects;

public class CommandLineInput {
    /** A StringBuilder object for holding the current unary operation in the input */
    public StringBuilder unary = new StringBuilder();
    /** A StringBuilder object for holding the current matrix value in the input */
    public StringBuilder matrixValue = new StringBuilder();
    /** A boolean flag indicating whether a whitespace character has been encountered in the input */
    public boolean whiteSpace = false;
    /** A boolean flag indicating whether a matrix has been opened in the input */
    public boolean matrixOpened = false;
    /** A boolean flag indicating whether a matrix has been closed in the input */
    public boolean matrixClosed = true;
    // contains the values of matrix, from which the matrix is transformed into type Matrix
    /** An ArrayList object for holding the current matrix values in the input */
    public ArrayList<Float> matrixValues = new ArrayList<>();
    /** An integer representing the number of rows in the current matrix */
    public int rows = 0;
    /** An integer representing the number of columns in the current matrix row */
    public int cols = 0;
    /** An integer representing the length of each row in the current matrix */
    public int rowLength = 0;
    /** A TermEvaluater object for evaluating the mathematical expression */
    public TermEvaluator evaluator = new TermEvaluator();

    /**
     * Returns the result of evaluating the mathematical expression as a Matrix object.
     *
     * @return the result of evaluating the mathematical expression as a Matrix object
     */
    public Matrix getResult() {
        return evaluator.getResult();
    }

    /**
     * Parses the given input string and evaluates the mathematical expression as a Matrix object.
     *
     * @param input the input string to parse and evaluate
     * @return the result of evaluating the mathematical expression as a Matrix object
     */
    public Matrix evaluateTerm(String input) {
        for (int i = 0; i < input.length(); i++)
            parseExpression(input.charAt(i));
        return getResult();
    }

    /**
     * Parses a single character of the expression and identifies the operator or operand it represents.
     * If the character is a letter, it appends it to the unary operator string.
     * If the character is a whitespace, it sets the whitespace flag to true.
     * If the character is an open parenthesis, it checks if the matrix brackets are valid and adds an open bracket operator.
     * If the character is a close parenthesis, it checks if the matrix brackets are valid and adds a close bracket operator.
     * If the character is a binary operator (+, -, or *), it checks if the matrix brackets are valid and identifies the operator.
     * If the character is none of the above, it looks for a matrix and adds it as an operand if found.
     * @param value the character to be parsed
     */
    private void parseExpression(char value) {
        if (isLetter(value)) {
            unary.append(value);
        }
        else {
            if (!unary.isEmpty()) {
                Operators unaryOperation = identifyUnaryOperation(unary.toString());
                evaluator.addOperator(unaryOperation);
                // clear content of unary
                unary.setLength(0);
            }
            if (value == ' ')
                whiteSpace = true;
            else if (value == '(') {
                hasValidMatrixBrackets();
                evaluator.addOperator(Operators.openBracket);
            } else if (value == ')') {
                hasValidMatrixBrackets();
                evaluator.addOperator(Operators.closedBracket);
            } else if (value == '-' && matrixOpened) {
                // if matrix is opened, '-' must be a unary minus
                    findMatrix(value);
            } else if (isBinaryOperator(value)) {
                hasValidMatrixBrackets();
                Operators operator = identifyBinaryOperator(value);
                evaluator.addOperator(operator);
            } else {
                var foundMatrix = findMatrix(value);
                if (foundMatrix != null) {
                    evaluator.addOperand(foundMatrix);
                    resetMatrixValues();
                }
            }
        }
    }

    /**
     * Identifies the unary operator based on the given string.
     *
     * @param unary the string containing the unary operator
     * @return the corresponding operator
     * @throws UnsupportedOperationException if the given string is not a defined unary operation
     */
    private Operators identifyUnaryOperation(String unary) {
        switch (unary) {
            case "transpose":
                return Operators.transpose;
            case "ref":
                return Operators.ref;
            case "rref":
                return Operators.rref;
            case "inverse":
                return Operators.inverse;
            default:
                throw new UnsupportedOperationException(unary + "is not a defined unary operation");
        }
    }

    /**
     * Identifies the binary operator based on the given character.
     *
     * @param value the character representing the binary operator
     * @return the corresponding operator
     * @throws UnsupportedOperationException if the given character is not a valid operator (+, -, or *)
     */
    private Operators identifyBinaryOperator(char value) {
        switch (value) {
            case '+':
                return Operators.addition;
            case '-':
                return Operators.subtraction;
            case '*':
                return Operators.multiplication;
            default:
                throw new UnsupportedOperationException("Invalid operator:" + value);
        }
    }

    /**
     * Checks if the given character is a letter.
     *
     * @param value the character to be checked
     * @return true if the character is a letter, false otherwise
     */
    private boolean isLetter(char value) {
        return value >= 'a' && value <= 'z';
    }

    /**
     * Checks if the matrix brackets are valid and consistent.
     *
     * @throws IllegalArgumentException if the matrix brackets are inconsistent.
     */
    private void hasValidMatrixBrackets() {
        if (matrixOpened || !matrixClosed)
            throw new IllegalArgumentException("Inconsistent matrix brackets");
    }

    /**
     * Checks if the given character represents a binary operator (+, -, *).
     *
     * @param operator the operator to be checked
     * @return true if the operator is a binary operator, false otherwise
     */
    private boolean isBinaryOperator(char operator) {
        return operator == '+' || operator == '-' || operator == '*';
    }

    /**
     * Finds a matrix in the input string starting from the given character.
     *
     * @param value the character to start searching for a matrix from
     * @return the matrix found, or null if no matrix was found yet
     * @throws InvalidParameterException if the parameter is not a valid character in the input string
     */
    private Matrix findMatrix(char value) {
        if (whiteSpace) {
            whiteSpace = false;
            addFloatToMatrix();
        }
        if (value == '-' || Character.isDigit(value) || value == '.') {
            matrixValue.append(value);
        } else {
            if (value == '[') {
                matrixOpened = true;
                matrixClosed = false;
                rows++;
            } else if (value == ']') {
                matrixClosed = true;
                addFloatToMatrix();
                checkRowLength();
                return stringToMatrix();
            } else if (value == ';') {
                addFloatToMatrix();
                checkRowLength();
                cols = 0;
                rows++;;
            } else {
                throw new InvalidParameterException("Invalid parameter:" + value);
            }
        }
        return null;
    }

    /**
     * Adds the current float value in matrixValue to the list of matrix values.
     * Clears the content of matrixValue afterwards.
     * Also increments the column count.
     */
    private void addFloatToMatrix() {
        if (!matrixValue.isEmpty()) {
            float matrixNumber = Float.parseFloat(matrixValue.toString());
            // clear content of matrixValue
            matrixValue.setLength(0);
            matrixValues.add(matrixNumber);
            cols++;
        }
    }

    /**
     * Resets the matrix-related variables.
     */
    private void resetMatrixValues() {
        matrixValues.clear();
        matrixOpened = false;
        matrixClosed = true;
        rows = 0;
        cols = 0;
        rowLength = 0;
    }

    /**
     * Checks if the current row has the same length as the first row.
     * If rowLength has not been set yet, sets it to the current column count.
     *
     * @throws InvalidParameterException if the row lengths are inconsistent.
     */
    private void checkRowLength() {
        if (rowLength == 0) {
            rowLength = cols;
        }
        else {
            if (!Objects.equals(rowLength, cols))
                throw new InvalidParameterException("Matrix has inconsistent size");
        }
    }

    /**
     * Converts the list of matrix values to a 2D float array and creates a Matrix object from it.
     * @return a Matrix object created from the list of matrix values
     */
    public Matrix stringToMatrix() {
        float[][] matrix = new float[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                matrix[i][j] = matrixValues.get(i*cols + j);
        return new Matrix(matrix);
    }
}