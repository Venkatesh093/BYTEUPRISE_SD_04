package sudokuSolver;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * GUI for Sudoku Solver.
 * Allows interaction with the Sudoku board, solving it, and loading/saving puzzles.
 * 
 * Author: Venkatesh Mayakrishnan
 */
public class SudokuInterface extends JFrame {

    private boolean sudoku;
    private boolean selection;
    private int row;
    private int col;
    private Grid gameGrid;
    private JLabel jLabel1;

    public SudokuInterface(Grid grid) {
        sudoku = false;
        gameGrid = grid;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanelTab = new JPanel();
        jPanelNum = new JPanel();
        jButCheck = new JButton();
        jButtonAC3 = new JButton();
        jButtonFC = new JButton();
        jLabel1 = new JLabel();
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenuItemLoad = new JMenuItem();
        jMenuItemDelete = new JMenuItem();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Sudoku Solver");
        setResizable(false);

        jPanelTab.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2));
        jPanelTab.setPreferredSize(new java.awt.Dimension(315, 315));
        jPanelTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelTabMouseClicked(evt);
            }
        });

        jPanelNum.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2));
        jPanelNum.setPreferredSize(new java.awt.Dimension(105, 105));
        jPanelNum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelNumMouseClicked(evt);
            }
        });

        jButCheck.setText("Check Solution");
        jButCheck.addActionListener(evt -> jButCheckActionPerformed(evt));

        jButtonAC3.setText("Solve with AC3");
        jButtonAC3.addActionListener(evt -> jButtonAC3ActionPerformed(evt));

        jButtonFC.setText("Solve with Forward Checking");
        jButtonFC.addActionListener(evt -> jButtonFCActionPerformed(evt));

        jMenu1.setText("File");
        jMenuItemLoad.setText("Load Sudoku");
        jMenuItemLoad.addActionListener(evt -> jMenuItemLoadActionPerformed(evt));
        jMenu1.add(jMenuItemLoad);

        jMenuItemDelete.setText("Delete Sudoku");
        jMenuItemDelete.addActionListener(evt -> jMenuItemDeleteActionPerformed(evt));
        jMenu1.add(jMenuItemDelete);

        jMenuBar1.add(jMenu1);
        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanelTab, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jButCheck, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAC3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonFC, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanelTab, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButCheck)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonAC3)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonFC)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void jPanelTabMouseClicked(java.awt.event.MouseEvent evt) {
        int cellWidth = jPanelTab.getSize().width / 9;
        int cellHeight = jPanelTab.getSize().height / 9;

        col = evt.getX() / cellWidth;
        row = evt.getY() / cellHeight;

        if (gameGrid.getCell(row, col) != 0) {
            selection = false; // If cell is not empty
        } else {
            selection = true;
            jLabel1.setText("Selected Cell: " + row + ", " + col);
        }
    }

    private void jPanelNumMouseClicked(java.awt.event.MouseEvent evt) {
        if (selection) {
            // Example: setting the number as '1' for demonstration
            int numberToPlace = 1; // Set this based on your GUI design
            gameGrid.setCell(numberToPlace, row, col);
            jLabel1.setText("Placed number: " + gameGrid.getCell(row, col));
            jPanelTab.repaint();
        }
    }

    private void jButCheckActionPerformed(java.awt.event.ActionEvent evt) {
        if (gameGrid.checkBoard()) {
            jLabel1.setText("Solution is correct.");
        } else {
            jLabel1.setText("Solution is incorrect.");
        }
    }

    private void jButtonAC3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (gameGrid.ac3()) {
            jLabel1.setText("Solved using AC3!");
            jPanelTab.repaint();
        } else {
            jLabel1.setText("No solution found!");
        }
    }

    private void jButtonFCActionPerformed(java.awt.event.ActionEvent evt) {
        if (gameGrid.forwardChecking(row, col, gameGrid.getCell(row, col))) {
            jLabel1.setText("Solved using Forward Checking!");
            jPanelTab.repaint();
        } else {
            jLabel1.setText("No solution found!");
        }
    }

    private void jMenuItemLoadActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            gameGrid.setFile(fileChooser.getSelectedFile());
            gameGrid.loadBoard();
            jLabel1.setText("Loaded Sudoku from file.");
            jPanelTab.repaint();
        }
    }

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        gameGrid.cleanBoard();
        jLabel1.setText("Sudoku board cleared.");
        jPanelTab.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        gameGrid.draw(g);
    }

    // Variables declaration
    private JButton jButCheck;
    private JButton jButtonAC3;
    private JButton jButtonFC;
    private JMenuBar jMenuBar1;
    private JMenu jMenu1;
    private JMenuItem jMenuItemLoad;
    private JMenuItem jMenuItemDelete;
    private JPanel jPanelNum;
    private JPanel jPanelTab;
    // End of variables declaration
}