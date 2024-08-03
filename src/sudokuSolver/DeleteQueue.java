package sudokuSolver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages a list of variables (cells) that may need to update their domains
 * during forward checking in the Sudoku solver. If any variable has an empty domain 
 * after updates, the domains must be restored before trying another value.
 * 
 * Author: Venkatesh Mayakrishnan
 */
public class DeleteQueue {
    private ArrayList<Cell> checkDelete = new ArrayList<>(); // List of candidates (variables) to check for deletion
    private ArrayList<Cell> deletedList = new ArrayList<>(); // List of deleted variables

    /**
     * Adds the row, column, and matrix of the given cell to the checkDelete list.
     */
    public void fcAddToDelete(Cell c) {
        fcAddRow(c);
        fcAddColumn(c);
        fcAddMatrix(c);
    }

    private void fcAddRow(Cell c) {
        // Add all cells in the same row (excluding the current cell)
        for (int i = 0; i < 9; i++) {
            if (i != c.getCol()) {
                checkDelete.add(new Cell(c.getRow(), i)); // Add row cells
            }
        }
    }

    private void fcAddColumn(Cell c) {
        // Add all cells in the same column (excluding the current cell)
        for (int i = 0; i < 9; i++) {
            if (i != c.getRow()) {
                checkDelete.add(new Cell(i, c.getCol())); // Add column cells
            }
        }
    }

    private void fcAddMatrix(Cell c) {
        // Add all cells in the same 3x3 matrix (excluding the current cell)
        int startRow = (c.getRow() / 3) * 3;
        int startCol = (c.getCol() / 3) * 3;

        for (int x = startRow; x < startRow + 3; x++) {
            for (int y = startCol; y < startCol + 3; y++) {
                if (x != c.getRow() || y != c.getCol()) {
                    checkDelete.add(new Cell(x, y)); // Add matrix cells
                }
            }
        }
    }

    /**
     * Executes deletion of the specified value from the domains of affected cells.
     */
    public void executeDeletion(int value, HashMap<Cell, ArrayList<Integer>> map) {
        for (Cell c : checkDelete) {
            updateDomain(c, value, map);
        }
    }

    private boolean fcDeleteValue(ArrayList<Integer> values, int number) {
        // Checks if the value exists in the domain of a variable
        return values.contains(number);
    }

    private void updateDomain(Cell c, int value, HashMap<Cell, ArrayList<Integer>> map) {
        ArrayList<Integer> domain = map.get(c);
        if (domain != null && fcDeleteValue(domain, value)) {
            deletedList.add(c); // Keep track of deleted cells
            domain.remove(Integer.valueOf(value)); // Remove the value from domain
            map.put(c, domain); // Update the domain in the map
        }
    }

    /**
     * Checks if any variable has an empty domain.
     */
    public boolean checkForEmptyDomains(HashMap<Cell, ArrayList<Integer>> map) {
        for (Cell c : deletedList) {
            if (map.get(c).isEmpty()) {
                return true; // Found an empty domain
            }
        }
        return false; // No empty domains found
    }

    /**
     * Restores the domains of the deleted variables.
     */
    public void restoreDomains(int value, HashMap<Cell, ArrayList<Integer>> map) {
        for (Cell c : deletedList) {
            ArrayList<Integer> domain = map.get(c);
            if (domain != null) {
                domain.add(value); // Restore the value to the domain
                map.put(c, domain); // Update the domain in the map
            }
        }
        deletedList.clear(); // Clear the deleted list after restoration
    }

    /**
     * Clears the checkDelete list and the deletedList.
     */
    public void clear() {
        checkDelete.clear(); // Clear the checkDelete list
        deletedList.clear(); // Clear the deleted list
    }
}