package sudokuSolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the gaming board/grid, it contains all the methods to load
 * a Sudoku from a txt file and to check if the given answer is correct. Having this board
 * gives the player the option to play the game without executing any of the algorithms, or to 
 * execute them halfway through a game.
 * 
 * Author: Venkatesh Mayakrishnan
 */
public class Grid {
    private File file;
    private int[][] m_board;

    public Grid() {
        file = null;
        m_board = new int[9][9];

        // Initialize board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                m_board[i][j] = 0;
            }
        }
    }

    /**
     * Copy constructor
     */
    public Grid(Grid original) {
        file = original.file;
        m_board = new int[9][9];

        // Copy board
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original.m_board[i], 0, m_board[i], 0, 9);
        }
    }

    /**
     * Returns cell value
     */
    public int getCell(int row, int col) {
        return m_board[row][col];
    }

    /**
     * Sets value to the given cell
     */
    public void setCell(int value, int row, int col) {
        m_board[row][col] = value;
    }

    /**
     * Sets file name
     */
    public void setFile(File fi) {
        file = fi;
    }

    /**
     * Returns file
     */
    public File getFile() {
        return file;
    }

    /**
     * Loads Sudoku from a file
     */
    public void loadBoard() {
        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            String line;
            for (int i = 0; i < 9 && (line = bf.readLine()) != null; i++) {
                String[] numbers = line.split(" ");
                for (int j = 0; j < numbers.length; j++) {
                    m_board[i][j] = Integer.parseInt(numbers[j]); // Inserts the numbers into the board
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + file.getName());
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file: " + file.getName());
        }
    }

    /**
     * Sets all the cells to 0
     */
    public void cleanBoard() {
        // Iterate through the board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                m_board[i][j] = 0;
            }
        }
    }

    /**
     * Check if the board is empty
     */
    public boolean emptyBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (m_board[i][j] != 0) {
                    return false; // Found a number, board is not empty
                }
            }
        }
        return true; // Board is empty
    }

    /**
     * Checks if there are any repeated numbers in a sub-matrix
     */
    public boolean checkMatrix(int startRow, int startCol) {
        boolean[] seen = new boolean[10]; // 1 to 9, ignore index 0
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                int num = m_board[i][j];
                if (num != 0) { // Ignore empty cells
                    if (seen[num]) {
                        return false; // Number already seen in this sub-matrix
                    }
                    seen[num] = true;
                }
            }
        }
        return true; // No duplicates found
    }

    /**
     * Checks if the given answer is correct
     */
    public boolean checkBoard() {
        // Check for empty cells
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (m_board[i][j] == 0) {
                    return false; // Found an empty cell
                }
            }
        }

        // Check rows and columns
        for (int i = 0; i < 9; i++) {
            boolean[] rowSeen = new boolean[10]; // 1 to 9
            boolean[] colSeen = new boolean[10]; // 1 to 9
            for (int j = 0; j < 9; j++) {
                if (m_board[i][j] != 0) {
                    if (rowSeen[m_board[i][j]]) {
                        return false; // Duplicate in row
                    }
                    rowSeen[m_board[i][j]] = true;

                    if (m_board[j][i] != 0) {
                        if (colSeen[m_board[j][i]]) {
                            return false; // Duplicate in column
                        }
                        colSeen[m_board[j][i]] = true;
                    }
                }
            }
        }

        // Check sub-matrices
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                if (!checkMatrix(i, j)) {
                    return false; // Duplicate in sub-matrix
                }
            }
        }

        return true; // Board is valid
    }

    /**
     * Forward Checking method
     * Returns true if a solution is found, false otherwise.
     */
    public boolean forwardChecking(int row, int col, int value) {
        // Temporarily assign the value to the cell
        setCell(value, row, col);

        // Check if this assignment leads to a valid state
        boolean valid = true;
        for (int i = 0; i < 9; i++) {
            if (i != col && getCell(row, i) == value) {
                valid = false; // Check row
            }
            if (i != row && getCell(i, col) == value) {
                valid = false; // Check column
            }
        }

        if (valid) {
            // Check the 3x3 sub-grid
            int startRow = row - row % 3;
            int startCol = col - col % 3;
            for (int i = startRow; i < startRow + 3; i++) {
                for (int j = startCol; j < startCol + 3; j++) {
                    if (i != row && j != col && getCell(i, j) == value) {
                        valid = false; // Check sub-grid
                    }
                }
            }
        }

        // Restore the cell to its previous state
        setCell(0, row, col);
        return valid; // Return the validity of the assignment
    }

    /**
     * AC-3 algorithm implementation
     * Returns true if the constraints are satisfied, false otherwise.
     */
    public boolean ac3() {
        List<Arc> queue = new ArrayList<>();

        // Initialize the queue with all arcs (pairs of cells)
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (m_board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            queue.add(new Arc(row, col, num));
                        }
                    }
                }
            }
        }

        // Process the queue
        while (!queue.isEmpty()) {
            Arc arc = queue.remove(0);
            if (removeInferences(arc)) {
                // If any cell is reduced to zero values, return false
                if (m_board[arc.row][arc.col] == 0) {
                    return false;
                }
                // Add neighbors to the queue
                for (int i = 0; i < 9; i++) {
                    if (i != arc.col) {
                        queue.add(new Arc(arc.row, i, m_board[arc.row][i]));
                    }
                    if (i != arc.row) {
                        queue.add(new Arc(i, arc.col, m_board[i][arc.col]));
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if a number can be placed in the given cell
     */
    private boolean isValid(int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (m_board[row][i] == num || m_board[i][col] == num) {
                return false; // Conflict found
            }
        }
        // Check 3x3 sub-grid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (m_board[i][j] == num) {
                    return false; // Conflict found
                }
            }
        }
        return true; // No conflict
    }

    /**
     * Removes inferences and checks for consistency
     */
    private boolean removeInferences(Arc arc) {
        int row = arc.row;
        int col = arc.col;
        int value = arc.value;

        // If value can be placed, set it and check for the rest
        if (isValid(row, col, value)) {
            m_board[row][col] = value; // Assign value
            return true; // Value assigned
        }
        return false; // No value could be assigned
    }

    /**
     * Draws the Sudoku grid.
     */
    public void draw(Graphics g) {
        int cellWidth = 35; // Set cell width
        int cellHeight = 35; // Set cell height

        // Draw grid lines
        for (int i = 0; i < 10; i++) {
            g.drawLine(i * cellWidth, 0, i * cellWidth, 9 * cellHeight); // Vertical lines
            g.drawLine(0, i * cellHeight, 9 * cellWidth, i * cellHeight); // Horizontal lines
        }

        // Draw numbers
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = m_board[row][col];
                if (value != 0) {
                    g.drawString(String.valueOf(value), col * cellWidth + 15, row * cellHeight + 25);
                }
            }
        }
    }

    // Helper class for representing an arc (constraint)
    private static class Arc {
        int row;
        int col;
        int value;

        public Arc(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }
}