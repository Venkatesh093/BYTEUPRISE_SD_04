package sudokuSolver;

/**
 * Class representing a cell of the board/grid.
 * Author: Venkatesh Mayakrishnan
 */
public class Cell {
    private int row; // Row index of the cell
    private int col; // Column index of the cell

    // Default constructor initializes cell to (0, 0)
    public Cell() {
        this.row = 0;
        this.col = 0;
    }

    // Constructor that initializes the cell with specified row and column
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Getters for row and column
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Returns the next empty cell in the grid.
     *
     * @param board The Sudoku grid to search for the next empty cell.
     * @return A Cell representing the next empty cell or a cell indicating that none exists.
     */
    public Cell nextCell(Grid board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.getCell(r, c) == 0) {
                    return new Cell(r, c); // Found an empty cell
                }
            }
        }
        return new Cell(9, 9); // Indicate that no empty cell was found
    }

    @Override
    public int hashCode() {
        return row * 9 + col; // Unique hash code for the cell
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check reference equality
        if (!(o instanceof Cell)) return false; // Check type
        Cell c = (Cell) o; // Cast to Cell
        return this.row == c.row && this.col == c.col; // Compare row and column
    }

    // Override toString for better debugging output
    @Override
    public String toString() {
        return "Cell{" + "row=" + row + ", col=" + col + '}';
    }
}
