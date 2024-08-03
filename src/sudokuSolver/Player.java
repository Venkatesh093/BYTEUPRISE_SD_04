package sudokuSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a virtual player that solves the Sudoku puzzle using
 * different algorithms. It maintains a map of cells and their domains to implement
 * algorithms like AC3 and forward checking.
 */
public class Player {

    private HashMap<Cell, ArrayList<Integer>> map = new HashMap<>(); // Maps cells to their domains
    private ArrayList<CellPair> queue = new ArrayList<>(); // Queue containing arcs for AC3
    private boolean AC3flag = false; // Indicates if AC3 has been executed
    private int calls = 0; // Counts the number of recursive calls

    // Fill the auxiliary map with cell domains based on the board state
    private void fillMap(Grid board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = new Cell(row, col);
                if (board.getCell(row, col) == 0) {
                    map.put(cell, fillValues()); // Empty cell gets all values 1-9
                } else {
                    ArrayList<Integer> domain = new ArrayList<>();
                    domain.add(board.getCell(row, col)); // Non-empty cell gets its value
                    map.put(cell, domain);
                }
            }
        }
    }

    // Initialize the domain with values 1-9
    private ArrayList<Integer> fillValues() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int value = 1; value <= 9; value++) {
            values.add(value);
        }
        return values;
    }

    // Print the domains of all cells for debugging purposes
    private void printDomains() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = new Cell(row, col);
                ArrayList<Integer> domain = map.get(cell);
                System.out.print("Domain Cell (" + row + "," + col + "): ");
                System.out.println(domain);
            }
        }
    }

    // Fill the queue with arcs for AC3 algorithm
    private void fillQueue() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = new Cell(row, col);
                fillArcs(cell, true);
            }
        }
    }

    // Check if an arc already exists in the queue
    private boolean checkQueue(CellPair arc) {
        return queue.contains(arc);
    }

    // Add arcs for row, column, and 3x3 box
    private void fillArcs(Cell cell, boolean firstFill) {
        fillRowArcs(cell, firstFill);
        fillColArcs(cell, firstFill);
        fillBoxArcs(cell, firstFill);
    }

    // Add row arcs to the queue
    private void fillRowArcs(Cell cell, boolean firstFill) {
        for (int col = 0; col < 9; col++) {
            if (col != cell.getCol()) {
                CellPair arc = firstFill ? new CellPair(cell, new Cell(cell.getRow(), col)) : new CellPair(new Cell(cell.getRow(), col), cell);
                if (!checkQueue(arc)) {
                    queue.add(arc);
                }
            }
        }
    }

    // Add column arcs to the queue
    private void fillColArcs(Cell cell, boolean firstFill) {
        for (int row = 0; row < 9; row++) {
            if (row != cell.getRow()) {
                CellPair arc = firstFill ? new CellPair(cell, new Cell(row, cell.getCol())) : new CellPair(new Cell(row, cell.getCol()), cell);
                if (!checkQueue(arc)) {
                    queue.add(arc);
                }
            }
        }
    }

    // Add 3x3 box arcs to the queue
    private void fillBoxArcs(Cell cell, boolean firstFill) {
        int boxRow = (cell.getRow() / 3) * 3;
        int boxCol = (cell.getCol() / 3) * 3;

        for (int row = boxRow; row < boxRow + 3; row++) {
            for (int col = boxCol; col < boxCol + 3; col++) {
                if (row != cell.getRow() || col != cell.getCol()) {
                    CellPair arc = firstFill ? new CellPair(cell, new Cell(row, col)) : new CellPair(new Cell(row, col), cell);
                    if (!checkQueue(arc)) {
                        queue.add(arc);
                    }
                }
            }
        }
    }

    // Execute AC3 algorithm
    public boolean AC3(Grid board) {
        AC3flag = true;
        boolean change;
        boolean result = true;

        fillMap(board); // Populate the map based on the current board
        fillQueue(); // Initialize the queue with arcs

        while (!queue.isEmpty() && result) {
            CellPair arc = queue.remove(0);
            change = false;
            ArrayList<Integer> domain1 = map.get(arc.getFirstCell());
            ArrayList<Integer> domain2 = map.get(arc.getSecondCell());

            for (int i = domain1.size() - 1; i >= 0; i--) {
                if (delete(domain2, domain1.get(i))) {
                    domain1.remove(i);
                    change = true;
                }
            }

            if (domain1.isEmpty()) {
                result = false; // No solution possible
            }

            if (change) {
                map.put(arc.getFirstCell(), domain1);
                fillArcs(arc.getFirstCell(), false); // Add new constraints to the queue
            }
        }

        printDomains(); // Debug: Print domains of the cells

        if (!result) {
            System.out.println("There is no solution for this problem");
            return false;
        }

        return true;
    }

    // Check if the domain contains the value
    private boolean delete(ArrayList<Integer> domain, int value) {
        return domain.contains(value);
    }

    // Backtracking algorithm
    private boolean backtrack(Grid board, Cell cell) {
        calls++;
        if (isComplete(board)) {
            return true;
        }

        ArrayList<Integer> domain = map.get(cell);
        for (int value : domain) {
            board.setCell(value, cell.getRow(), cell.getCol());
            if (checkValue(board, cell, value)) {
                if (backtrack(board, cell.nextCell(board))) {
                    return true;
                }
            }
        }
        board.setCell(0, cell.getRow(), cell.getCol()); // Restore the cell value
        return false;
    }

    // Check if the board is completely filled
    private boolean isComplete(Grid board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getCell(row, col) == 0) {
                    return false; // Found an empty cell
                }
            }
        }
        return true;
    }

    // Validate the value against Sudoku rules
    private boolean checkValue(Grid board, Cell cell, int value) {
        for (int col = 0; col < 9; col++) {
            if (col != cell.getCol() && board.getCell(cell.getRow(), col) == value) {
                return false; // Repeated value in row
            }
        }

        for (int row = 0; row < 9; row++) {
            if (row != cell.getRow() && board.getCell(row, cell.getCol()) == value) {
                return false; // Repeated value in column
            }
        }

        return boxCheck(board, cell, value); // Check 3x3 box
    }

    // Check for repeated values in a 3x3 box
    private boolean boxCheck(Grid board, Cell cell, int value) {
        int boxRow = (cell.getRow() / 3) * 3;
        int boxCol = (cell.getCol() / 3) * 3;

        for (int row = boxRow; row < boxRow + 3; row++) {
            for (int col = boxCol; col < boxCol + 3; col++) {
                if (row != cell.getRow() || col != cell.getCol()) {
                    if (board.getCell(row, col) == value) {
                        return false; // Found a duplicate in box
                    }
                }
            }
        }
        return true; // Value is valid in box
    }

    // Forward checking algorithm
    public boolean forwardChecking(Grid board, Cell cell) {
        calls++;
        if (isComplete(board)) {
            return true;
        }

        DeleteQueue deleteQueue = new DeleteQueue();
        deleteQueue.fcAddToDelete(cell);
        ArrayList<Integer> originalDomain = new ArrayList<>(map.get(cell));

        for (int value : originalDomain) {
            deleteQueue.executeDeletion(value, map); // Remove value from other domains
            if (deleteQueue.checkForEmptyDomains(map)) {
                deleteQueue.restoreDomains(value, map); // Restore if any domain is empty
            } else {
                ArrayList<Integer> newDomain = new ArrayList<>();
                newDomain.add(value);
                map.put(cell, newDomain);
                board.setCell(value, cell.getRow(), cell.getCol());
                if (forwardChecking(board, cell.nextCell(board))) {
                    return true;
                } else {
                    deleteQueue.restoreDomains(value, map); // Restore domains
                    map.put(cell, originalDomain); // Restore original domain
                }
            }
        }
        board.setCell(0, cell.getRow(), cell.getCol()); // Restore original value on the board
        return false;
    }

    // Run the backtracking algorithm
    public boolean runBacktracking(Grid board) {
        if (!AC3flag) {
            fillMap(board);
        }
        Cell cell = new Cell(); // Initialize the first cell

        long startTime = System.nanoTime();
        boolean success = backtrack(board, cell.nextCell(board)); // Start backtracking
        long endTime = System.nanoTime();
        System.out.println("Time elapsed (Backtracking): " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        System.out.println("Recursive calls: " + calls);

        return success; // Return success status
    }

    // Run the AC3 algorithm
    public boolean runAC3(Grid board) {
        long startTime = System.nanoTime();
        boolean solutionExists = AC3(board); // Execute AC3
        long endTime = System.nanoTime();
        System.out.println("Time elapsed (AC3): " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");

        return solutionExists; // Return whether a solution exists
    }

    // Run the forward checking algorithm
    public boolean runForwardChecking(Grid board) {
        if (!AC3flag) {
            fillMap(board);
        }
        Cell cell = new Cell(); // Initialize the first cell

        long startTime = System.nanoTime();
        boolean success = forwardChecking(board, cell.nextCell(board)); // Start forward checking
        long endTime = System.nanoTime();
        System.out.println("Time elapsed (Forward Checking): " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + "ms");
        System.out.println("Recursive calls: " + calls);

        return success; // Return success status
    }
}
