/**
 * This package provides a Matrix class for operations with matrices.
 */

package cz.cuni.mff.java.matrixCalculator;

import java.security.InvalidParameterException;
import java.util.Arrays;

public class Matrix {
    float[][] matrix;

    /**
     * Constructs a Matrix object with a given 2D array of floats
     *
     * @param inputMatrix A 2D float array representing the matrix
     * @throws InvalidParameterException if the input matrix has inconsistent dimensions
     */
    public Matrix(float[][] inputMatrix) {
        int row = inputMatrix.length;
        int col = inputMatrix[0].length;
        for (int i = 0; i < row; i++) {
            if (inputMatrix[i].length != col) {
                throw new InvalidParameterException("The input matrix does not have appropriate dimensions");
            }
        }
        matrix = inputMatrix;
    }

    /**
     * Constructs a Matrix object that is a copy of another Matrix object
     *
     * @param matrix A Matrix object to copy
     */
    public Matrix(Matrix matrix) {
        int row = matrix.getSize()[0];
        int col = matrix.getSize()[1];
        this.matrix = new float[row][col];

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                this.matrix[i][j] = matrix.getValue(i,j);
    }

    /**
     * Returns the dimensions of this Matrix object
     *
     * @return An integer array containing the number of rows and columns, respectively
     */
    public int[] getSize() {
        return new int[]{matrix.length, matrix[0].length};
    }

    /**
     * Returns the value at a given row and column of this Matrix object
     *
     * @param row The row index of the desired value
     * @param col The column index of the desired value
     * @return The float value at the specified row and column
     */
    public float getValue(int row, int col) {
        return matrix[row][col];
    }

    /**
     * Checks if two matrices are equal.
     *
     * @param A the first matrix to compare
     * @param B the second matrix to compare
     * @return {@code true} if the matrices are equal, {@code false} otherwise
     */
    public static boolean equals(Matrix A, Matrix B) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];
        int rowB = B.getSize()[0];
        int colB = B.getSize()[1];

        if (rowA != rowB && colA != colB)
            return false;

        for (int i = 0; i < rowA; i++)
            for (int j = 0; j < colA; j++)
                if (A.getValue(i,j) != B.getValue(i,j))
                    return false;
        return true;
    }

    /**
     * {@inheritDoc}
     * Overrides the equals method of the Object class to check if this object is equal to another object.
     * If the object being compared is a Matrix, it delegates the comparison to the Matrix.equals() method.
     * Otherwise, it throws an InvalidParameterException.
     *
     * @param obj the object to be compared for equality with this object
     * @return true if the objects are equal, false otherwise
     * @throws InvalidParameterException if the obj parameter is not of type Matrix
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix)
            return Matrix.equals(this, (Matrix)obj);
        else
            return false;
    }

    /**
     * Creates a new Matrix object of a given size with all values initialized to zero
     *
     * @param row The number of rows in the new matrix
     * @param col The number of columns in the new matrix
     * @return A new Matrix object with dimensions (row, col) and all values initialized to zero
     */
    public static Matrix allZeroes(int row, int col) {
        float[][]resultMatrix = new float[row][col];

        for (int i = 0; i < row; i++)
            Arrays.fill(resultMatrix[i], 0);
        return new Matrix(resultMatrix);
    }

    /**
     * Creates a new square Matrix object of a given size that is the identity matrix
     *
     * @param size The number of rows and columns in the new matrix
     * @return A new Matrix object with dimensions size x size that is the identity matrix
     */
    public static Matrix identityMatrix(int size) {
        float[][]resultMatrix = new float[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (i != j)
                    resultMatrix[i][j] = 0;
                else
                    resultMatrix[i][j] = 1;
        return new Matrix(resultMatrix);
    }

    /**
     * Adds two matrices element-wise.
     *
     * @param A the first matrix
     * @param B the second matrix
     * @return the sum of the two matrices
     * @throws InvalidParameterException if the matrices do not have the same dimensions
     */
    public static Matrix addition(Matrix A, Matrix B) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];
        int rowB = B.getSize()[0];
        int colB = B.getSize()[1];

        if (rowA == rowB && colA == colB) {
            var resultMatrix = new float[rowA][colA];
            for (int i = 0; i < rowA; i++) {
                for (int j = 0; j < colB; j++) {
                    resultMatrix[i][j] = A.getValue(i, j) + B.getValue(i, j);
                }
            }
            return new Matrix(resultMatrix);
        } else throw new InvalidParameterException("Matrices do not have appropriate dimensions");
    }

    /**
     * Subtracts two matrices element-wise.
     *
     * @param A the first matrix
     * @param B the second matrix
     * @return the difference of the two matrices
     * @throws InvalidParameterException if the matrices do not have the same dimensions
     */
    public static Matrix subtraction(Matrix A, Matrix B) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];
        int rowB = B.getSize()[0];
        int colB = B.getSize()[1];

        if (rowA == rowB && colA == colB) {
            var resultMatrix = new float[rowA][colA];
            for (int i = 0; i < rowA; i++) {
                for (int j = 0; j < colB; j++) {
                    resultMatrix[i][j] = A.getValue(i, j) - B.getValue(i, j);
                }
            }
            return new Matrix(resultMatrix);
        } else throw new InvalidParameterException("Matrices do not have appropriate dimensions");
    }

    /**
     * Multiplies two matrices.
     *
     * @param A the first matrix
     * @param B the second matrix
     * @return the product of the two matrices
     * @throws InvalidParameterException if the number of columns in the first matrix
     *          does not match the number of rows in the second matrix
     */
    public static Matrix multiplication(Matrix A, Matrix B) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];
        int rowB = B.getSize()[0];
        int colB = B.getSize()[1];

        if (colA == rowB) {
            var resultMatrix = new float[rowA][colA];
            for (int i = 0; i < rowA; i++) {
                for (int j = 0; j < colB; j++) {
                    for (int k = 0; k < rowB; k++)
                        resultMatrix[i][j] += A.getValue(i, k) * B.getValue(k, j);
                }
            }
            return new Matrix(resultMatrix);
        } else throw new InvalidParameterException("Matrices do not have appropriate dimensions");
    }

    /**
     * Transposes a matrix.
     *
     * @param A the matrix to transpose
     * @return the transpose of the matrix
     */
    public static Matrix transpose(Matrix A) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];
        float[][] resultMatrix = new float[rowA][colA];

        for (int i = 0; i < rowA; i++)
            for (int j = 0; j < colA; j++)
                resultMatrix[j][i] = A.getValue(i, j);
        return new Matrix(resultMatrix);
    }

    /**
     * Calculates the absolute value of a given float.
     *
     * @param val the input value to calculate the absolute value of
     * @return the absolute value of the input value
     */
    public static float abs(float val) {
        if (val < 0)
            return -val;
        else
            return val;
    }

    /**
     * Swaps two rows in the matrix.
     *
     * @param row1 the index of the first row to swap
     * @param row2 the index of the second row to swap
     */
    public void rowSwap(int row1, int row2) {
        float[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    /**
     * Sets the value of a specific element in the matrix.
     *
     * @param row the row index of the element to set
     * @param col the column index of the element to set
     * @param value the value to set the element to
     */
    public void setValue(int row, int col, float value) {
        matrix[row][col] = value;
    }

    /**
     * Multiplies a row in the matrix by a scalar coefficient.
     *
     * @param row the index of the row to multiply by the scalar coefficient
     * @param coefficient the scalar coefficient to multiply the row by
     */
    public void rowMultiply(int row, float coefficient) {
        for (int i = 0; i < matrix.length; i++)
            matrix[row][i]*=coefficient;
    }

    /**
     * Calculates the row echelon form of a given matrix. A new copy of the input matrix is created and modified;
     * the original matrix is left untouched.
     *
     * @param A the input matrix to calculate the row echelon form of
     * @return a new matrix object containing the row echelon form of the input matrix
     */
    public static Matrix ref(Matrix A) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];

        //create a copy of the given matrix
        Matrix refMatrix = new Matrix(A);

        int row = 0; // i
        int col = 0; // j
        boolean allZeroes = true;

        int pivotRow = 0;

        while (row < rowA && col < colA) {
            // 2. step
            for (int k = row; k < rowA; k++) {
                for (int l = col; l < colA; l++) {
                    if (A.getValue(k, l) != 0) {
                        allZeroes = false;
                        // 3. step
                        col = l;
                        pivotRow = k;
                        break;
                    }
                }
                if (!allZeroes){
                    break;
                }
            }
            if (allZeroes) {
                break;
            }

            // 4. step
            refMatrix.rowSwap(row, pivotRow);

            // 6. step
            for (int k = 0; k < rowA; k++) {
                if (k <= row)
                    continue;
                float refPivotRowValue = refMatrix.getValue(k, col)/refMatrix.getValue(row, col);
                for (int i = 0; i < colA; i++){
                    float refResult = refMatrix.getValue(k, i) - refPivotRowValue * refMatrix.getValue(pivotRow, i);
                    refMatrix.setValue(k, i, refResult);
                }
            }
            // 7. step
            row++;
            col++;
        }
        return refMatrix;
    }

    /**
     * Calculates the reduced row echelon form and the inverse of a given matrix.
     * A new copy of the input matrix is created and modified; the original matrix is left untouched.
     *
     * @param A the input matrix to calculate the reduced row echelon form and inverse of
     * @return an array containing two new matrix objects:
     * the reduced row echelon form and the inverse of the input matrix
     */
    private static Matrix[] rrefAndInverse(Matrix A) {
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];

        //create a copy of the given matrix
        Matrix rrefMatrix = new Matrix(A);
        Matrix inverseMatrix = new Matrix(identityMatrix(rowA));

        int row = 0; // i
        int col = 0; // j
        boolean allZeroes = true;

        int pivotRow = 0;

        while (row < rowA && col < colA) {
            // 2. step
            for (int k = row; k < rowA; k++) {
                for (int l = col; l < colA; l++) {
                    if (A.getValue(k, l) != 0) {
                        allZeroes = false;
                        // 3. step
                        col = l;
                        pivotRow = k;
                        break;
                    }
                }
                if (!allZeroes){
                    break;
                }
            }
            if (allZeroes) {
                break;
            }

            // 4. step
            rrefMatrix.rowSwap(row, pivotRow);
            inverseMatrix.rowSwap(row, pivotRow);
            // 5. step
            float pivotValue = rrefMatrix.getValue(row, col);
            for (int i = 0; i < rowA; i++){
                var rrefResult = rrefMatrix.getValue(row, i) / pivotValue;
                rrefMatrix.setValue(row, i, rrefResult);

                var inverseResult = inverseMatrix.getValue(row, i) / pivotValue;
                inverseMatrix.setValue(row, i, inverseResult);
            }
            // 6. step
            for (int k = 0; k < rowA; k++) {
                if (k == row)
                    continue;
                float rrefPivotRowValue = rrefMatrix.getValue(k, col);
                for (int i = 0; i < colA; i++){
                    float rrefResult = rrefMatrix.getValue(k, i) - rrefPivotRowValue * rrefMatrix.getValue(pivotRow, i);
                    rrefMatrix.setValue(k, i, rrefResult);
                    float inverseResult = inverseMatrix.getValue(k, i) - rrefPivotRowValue * inverseMatrix.getValue(pivotRow, i);
                    inverseMatrix.setValue(k, i, inverseResult);
                }
            }
            // 7. step
            row++;
            col++;
        }
        return new Matrix[] {rrefMatrix, inverseMatrix};
    }

    /**
     * Calculates the inverse of a square matrix A.
     *
     * @param A the input square matrix to calculate the inverse of
     * @return the inverse matrix of the input square matrix
     * @throws InvalidParameterException if the input matrix is not square
     */
    public static Matrix inverse(Matrix A){
        int rowA = A.getSize()[0];
        int colA = A.getSize()[1];
        if (rowA != colA)
            throw new InvalidParameterException("Matrix is not a square matrix");
        var resultMatrices = rrefAndInverse(A);
        return resultMatrices[1];
    }

    /**
     * Calculates the reduced row echelon form (RREF).
     *
     * @param A the input matrix
     * @return the RREF of the input matrix
     */
    public static Matrix rref(Matrix A){
        var resultMatrices = rrefAndInverse(A);
        return resultMatrices[0];
    }

    public String stringRepresentationWithBrackets() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append('[');
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                stringRepresentation.append(" ");
                stringRepresentation.append(matrix[i][j]);
            }
            if (matrix.length > 1 && i != matrix.length-1)
                stringRepresentation.append(" ;");
        }
        stringRepresentation.append(" ]");
        return stringRepresentation.toString();
    }

    /**
     * Returns a string representation of the matrix, where each element is separated by a space
     * and each row is separated by a newline character.
     *
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        int row = matrix.length;
        int col = matrix[0].length;
        StringBuilder matrixData = new StringBuilder();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrixData.append(matrix[i][j]);
                matrixData.append(" ");
            }
            matrixData.append('\n');
        }
        return matrixData.toString();
    }
}