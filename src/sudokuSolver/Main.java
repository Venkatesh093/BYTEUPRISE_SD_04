package sudokuSolver;

/**
 * Main class to initiate the Sudoku game.
 * 
 * Author: Venkatesh Mayakrishnan
 */
public class Main {

    /**
     * Entry point of the Sudoku application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Create a new Sudoku grid
        Grid sudokuGrid = new Grid();

        // Create and start the game interface
        SudokuInterface gameInterface = new SudokuInterface(sudokuGrid);
        gameInterface.setVisible(true);
    }
}
