package sudokuSolver;

/**
 * Class representing a pair of cells in the Sudoku grid.
 * 
 * Author: Venkatesh Mayakrishnan
 */
public class CellPair {

    private Cell firstCell;
    private Cell secondCell;

    // Default constructor initializes the cells to null
    public CellPair() {
        this.firstCell = null;
        this.secondCell = null;
    }

    // Constructor that initializes the pair with the specified cells
    public CellPair(Cell firstCell, Cell secondCell) {
        this.firstCell = firstCell;
        this.secondCell = secondCell;
    }

    // Returns the first cell in the pair
    public Cell getFirstCell() {
        return firstCell;
    }

    // Returns the second cell in the pair
    public Cell getSecondCell() {
        return secondCell;
    }

    // Overrides equals method to compare two CellPair objects
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof CellPair)) {
            return false;
        }
        CellPair otherPair = (CellPair) other;

        // Compare the two cells in the pairs for equality
        return otherPair.getFirstCell().equals(firstCell) && 
               otherPair.getSecondCell().equals(secondCell);
    }

    // Overrides hashCode method for proper functioning in collections
    @Override
    public int hashCode() {
        return firstCell.hashCode() * 31 + secondCell.hashCode();
    }
}