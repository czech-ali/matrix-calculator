## MatrixCalculator - User Documentation

### Program Description
MatrixCalculator is a console-based matrix calculator that accepts matrix expressions in infix notation as input. Matrices are represented using square brackets, with float values separated by spaces. Matrix rows are separated by semicolons. After evaluating the input, the program outputs the resulting matrix. If the input contains any errors, an error message is displayed. Users can enter new input in both cases.

### Running the Program
To run the program, execute the following command in the terminal:
`java -jar jarPath`

Replace `jarPath` with the path to the jar file located in the target directory. Optionally, you can add the "brackets" argument to the command (`java -jar jarPath brackets`) to display the output matrices in the same format as the input matrices. This argument is not mandatory. If omitted, the program displays the values of individual rows of the resulting matrix on separate lines.

### Supported Operations
The program supports the following matrix operations:

#### Binary Operations
- Addition
- Subtraction
- Multiplication

#### Unary Operations
- Transpose
- Row-Echelon Form (REF)
- Inverse
- Reduced Row-Echelon Form (RREF)

The program also recognizes the unary minus for values inside matrices. Ensure there are no spaces between the unary minus and the float value. Parentheses are supported to ensure input validity. If no parentheses follow a unary matrix operation, the operation is applied to the immediate matrix on the right.

### Examples of Input for Each Operation

- Addition: `[2 1; 3 1] + [-1 -1; -1 -1]`
- Subtraction: `[2 1; 3 1] - [2 1; 3 1]`
- Multiplication: `[2 1; 3 1] * [2 1; 3 1]`
- Transpose: `transpose [2 1; 3 1]`
- Row-Echelon Form: `ref [2 1; 3 1]`
- Inverse: `inverse [2 1; 3 1]`
- Reduced Row-Echelon Form: `rref [2 1; 3 1]`
- Brackets: `inverse([1 2 3; 3 1 2; 4 5 3] + [3 2 1; 2 1 3; 4 1 2] * [5 3 1; 2 3 4; 4 3 1])`

The operations can be chained, use of multiple matrices is supported.
For example:

`[2.0 2.1;2.0 2.0] * (([1.0 1.0; 2.0 1.0] * transpose([2.0 2.1;2.0 2.0])) * inverse [1.0 1.0; 2.0 1.0])`


### Exiting the Program
To exit the program, input "^Q" or "^q" in the user input.
