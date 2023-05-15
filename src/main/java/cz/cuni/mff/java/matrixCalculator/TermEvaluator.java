/**
 * The TermEvaluator class evaluates a mathematical expression represented in an infix form using a stack-based algorithm.
 * It performs arithmetic operations between matrices and unary matrix operations, while supporting parentheses
 * to control the order of operations. It evaluates the expression by adding operators and operands to separate stacks.
 * Once the expression is fully added, it evaluates the expression by popping operators and performing the corresponding
 * operation on the operands in the operand stack, and then pushing the result back onto the operand stack.
 * The supported operators include addition, subtraction, multiplication, transpose, ref, rref, and inverse.
 */

package cz.cuni.mff.java.matrixCalculator;

import java.security.InvalidParameterException;
import java.util.Stack;

public class TermEvaluator {
    /** Stack for matrix operations */
    private Stack<Operators> operatorStack =  new Stack<>();
    /** Stack for matrices */
    private Stack<Matrix> operandStack =  new Stack<>();

    /**
     * Adds an operator to the operator stack.
     *
     * @param operator the operator to be added to the operator stack.
     */
    public void addOperator(Operators operator) {
        if (operatorStack.isEmpty() || operator == Operators.openBracket)
            operatorStack.push(operator);
        else if (operator == Operators.closedBracket) {
            evaluateBracket(operator);
            // pop openBracket
            operatorStack.pop();
        } else {                               // current operator has bigger priority than the op on top of the stack
            if (previousOperatorOpenBracket() || operator.ordinal() < operatorStack.peek().ordinal())
                operatorStack.push(operator);
            else {
                Operators topOnStack = operatorStack.pop();
                evaluate(topOnStack);
                operatorStack.push(operator);
            }
        }
    }

    /**
     * Evaluates the expression and returns the result matrix.
     *
     * @return the result matrix of the evaluated expression.
     * @throws InvalidParameterException if the expression is invalid.
     */
    public Matrix getResult() {
        while (!operatorStack.isEmpty()) {
            Operators topOnStack = operatorStack.pop();
            if (topOnStack == Operators.openBracket)
                throw new InvalidParameterException("Invalid expression error");
            else if (topOnStack == Operators.closedBracket)
                evaluateBracket(topOnStack);
            else
                evaluate(topOnStack);
        }
        if (operandStack.size() > 1)
            throw new InvalidParameterException("Invalid expression error");
        else {
            // return result of the whole expression
            return operandStack.pop();
        }
    }

    /**
     * Adds an operand to the operand stack.
     *
     * @param matrix the matrix to be added to the operand stack.
     */
    public void addOperand(Matrix matrix) {
        operandStack.push(matrix);
    }

    /**
     * Evaluates the given operator and performs the corresponding matrix operation on the operands.
     * The result matrix is pushed onto the operand stack.
     *
     * @param currentOperator the operator to be evaluated.
     */
    public void evaluate(Operators currentOperator) {
        Matrix matrixResult = matrixOperation(currentOperator);
        operandStack.push(matrixResult);
    }

    /**
     * Evaluates the given operator and performs the corresponding matrix operation on the operands until openBracket.
     * The result matrix is pushed onto the operand stack.
     *
     * @param currentOperator the operator to be evaluated.
     */

    public void evaluateBracket(Operators currentOperator) {
        while (!previousOperatorOpenBracket() && currentOperator.ordinal() >= operatorStack.peek().ordinal()) {
            Operators matrixOperation = operatorStack.pop();
            Matrix matrixResult = matrixOperation(matrixOperation);
            operandStack.push(matrixResult);
        }
    }

    /**
     * Returns true if the previous operator is an open bracket.
     *
     * @return true if the previous operator is an open bracket.
     */
    private boolean previousOperatorOpenBracket() {
        return operatorStack.peek() == Operators.openBracket;
    }

    /**
     * Performs the matrix operation corresponding to the given operator on the top two matrices in the operand stack.
     * The result matrix is returned.
     *
     * @param matrixOperation the operator to be evaluated.
     * @return the result matrix of the matrix operation.
     * @throws UnsupportedOperationException if the operator is invalid.
     */
    private Matrix matrixOperation(Operators matrixOperation) {
        switch (matrixOperation) {
            case addition:
                return Matrix.addition(operandStack.pop(), operandStack.pop());
            case subtraction:
                Matrix A = operandStack.pop();
                Matrix B = operandStack.pop();
                return Matrix.subtraction(B, A);
            case multiplication:
                Matrix X = operandStack.pop();
                Matrix Y = operandStack.pop();
                return Matrix.multiplication(Y, X);
            case transpose:
                return Matrix.transpose(operandStack.pop());
            case ref:
                return Matrix.ref(operandStack.pop());
            case rref:
                return Matrix.rref(operandStack.pop());
            case inverse:
                return Matrix.inverse(operandStack.pop());
            case openBracket:
                return new Matrix(operandStack.pop());
            default:
                throw new UnsupportedOperationException("Invalid operator:" + matrixOperation);
        }
    }
}