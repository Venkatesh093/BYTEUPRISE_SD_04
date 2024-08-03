Sudoku Solver (AC3, Backtracking, Forward Checking)
This project features a comprehensive Sudoku solver application that includes an interactive interface for loading, creating, and solving Sudoku puzzles. Users can load an existing Sudoku from a provided TXT file or create their own puzzle by selecting numbers for each cell. Once the Sudoku is set up, users can either solve it manually or utilize one of the three advanced algorithms to find the solution.

Features
Load and Create Sudoku:

Load existing Sudoku puzzles from provided TXT files.
Create custom Sudoku puzzles through an interactive interface.
Algorithms:

Backtracking: A brute-force algorithm that tries every possible number in each cell until a solution is found.
AC3 (Arc Consistency 3): Reduces the search space by enforcing arc consistency and simplifying the domains of the variables. To observe this algorithm in action, execute the program from the command line to view domain outputs through the console.
Forward Checking: Enhances backtracking by looking ahead and eliminating values that violate constraints, thus reducing the search space.
Performance Metrics:

Measures and displays the number of recursive calls and the elapsed time for each algorithm.
Compare the efficiency of backtracking and forward checking before and after running AC3 to see how domain reduction impacts performance.
Usage
Loading and Creating Puzzles
Load from File: Use the provided TXT files to load existing Sudoku puzzles into the application.
Create Puzzle: Manually select numbers for each cell to create a custom Sudoku puzzle.
Solving Puzzles
Manual Play: Solve the puzzle manually using the interactive interface.
Automated Solvers:
Select and execute one of the three algorithms to automatically solve the puzzle.
View the solution directly on the screen if the puzzle is solved correctly.
Command Line Execution
AC3 Algorithm: To see the AC3 algorithm's domain reduction process in action, run the program from the command line. The domain outputs will be displayed in the console.
Performance Comparison
Recursive Calls and Elapsed Time: The application tracks and displays the number of recursive calls and the time taken for each algorithm.
Impact of AC3: Run backtracking and forward checking algorithms before and after AC3 to observe the effect of domain reduction on performance.
Examples
Four example TXT files are provided to demonstrate the functionality of the application. These examples can be used to test the loading, solving, and performance measurement features.

Author
Developed by Venkatesh Mayakrishnan.

For any questions or further information, feel free to contact me.