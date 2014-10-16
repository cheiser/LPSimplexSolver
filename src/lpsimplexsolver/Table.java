/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lpsimplexsolver;

/**
 *
 * @author Mattis
 */
public class Table {

    double[][] simplexTable;
    int pivotColumn;
    int pivotRow;
    int entryVariable;
    double[] newRow;

    /*
     * columns and rows is the size one wants on the table remember to use one
     * extra row and one extra column for the objective row and result column.
     */
    public Table(int columns, int rows) {
        simplexTable = new double[columns][rows];
    }

    public Table(double[][] simplexTable, int pivotColumn, int pivotRow) {
        this.simplexTable = simplexTable;
        this.pivotColumn = pivotColumn;
        this.pivotRow = pivotRow;
    }

    public Table(double[][] simplexTable, int pivotColumn, int pivotRow, int entryVariable) {
        this.simplexTable = simplexTable;
        this.pivotColumn = pivotColumn;
        this.pivotRow = pivotRow;
        this.entryVariable = entryVariable;
    }

    public Table(Table table) {
        this.simplexTable = table.getSimplexTable();
        this.pivotColumn = table.pivotColumn;
        this.pivotRow = table.pivotRow;
        this.entryVariable = table.entryVariable;
        this.newRow = table.getNewRow();
    }

    public void setValueAt(int column, int row, int value) {
        try {
            simplexTable[column][row] = value;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("out of bounds");
        }
    }

    public void setPivotColumn(int pivotColumn) {
        this.pivotColumn = pivotColumn;
    }

    public void setPivotRow(int pivotRow) {
        this.pivotRow = pivotRow;
    }

    public void setEntryVariable(int entryVariable) {
        this.entryVariable = entryVariable;
    }

    public int getEntryVariable() {
        return entryVariable;
    }

    public double[] getNewRow() {
        return newRow;
    }

    public int getPivotColumn() {
        return pivotColumn;
    }

    public int getPivotRow() {
        return pivotRow;
    }

    public double getValueAt(int column, int row) {
        try {
            return simplexTable[column][row];
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("not valid column or row");
        }
        return -Double.MAX_VALUE; // no valid column or row
    }

    /*
     * used to check if all the values in the objective row is positive if it is
     * then one should stop. if all is positive true is returned
     */
    public boolean checkObjectiveRow() {
        for (int i = 0; i < simplexTable.length; i++) {
            if (simplexTable[i][simplexTable[0].length - 1] < 0) {
                return false;
            }
        }
        return true;
    }

    public void printSolution() {
        for (int i = 0; i < simplexTable[simplexTable.length - 1].length - 1; i++) {
            System.out.printf("variable %d = %.2f\n", i, simplexTable[simplexTable.length - 1][i]);
            // System.out.println("variable " + i + " = " + simplexTable[simplexTable.length - 1][i]);
        }
        System.out.printf("solution value = %.2f\n", simplexTable[simplexTable.length - 1][simplexTable[0].length - 1]);
    }
    
    public double[] getSolution(){
        double[] solution = new double[simplexTable[simplexTable.length - 1].length];
        for (int i = 0; i < simplexTable[simplexTable.length - 1].length - 1; i++) {
            solution[i] = simplexTable[simplexTable.length - 1][i];
        }
        solution[solution.length-1] = simplexTable[simplexTable.length - 1][simplexTable[0].length - 1];
        return solution;
    }

    public int findEntryColumn() {
        double smallestABSValue = 0;
        int smallestColumn = 0;
        for (int i = 0; i < simplexTable.length; i++) {
            if (simplexTable[i][simplexTable[0].length - 1] <= smallestABSValue) {
                smallestColumn = i;
                smallestABSValue = simplexTable[i][simplexTable[0].length - 1];
            }
        }

        return smallestColumn;
    }

    public void findAndSetEntryVariable() {
        this.entryVariable = findEntryColumn();
        this.pivotColumn = entryVariable;
    }

    /*
     * checks if any of the entries in the pivot column is positive, if none is
     * then the problem cant be solved returns true if the problem still can be
     * solved
     */
    public boolean checkPivotColumn() {
        for (int i = 0; i < simplexTable[0].length; i++) {
            if (simplexTable[pivotColumn][i] > 0) {
                return true;
            }
        }
        return false;
    }

    public int findDepartingVariable() {
        double smallestGRatio = Double.MAX_VALUE;
        int smallestVariable = -1;
        for (int i = 0; i < simplexTable[0].length-1; i++) {
            try {
                if ((simplexTable[simplexTable.length - 1][i] / (simplexTable[pivotColumn][i])) <= smallestGRatio) {
                    smallestGRatio = (simplexTable[simplexTable.length - 1][i] / (simplexTable[pivotColumn][i]));
                    smallestVariable = i;
                }
            } catch (ArithmeticException ex) {
                System.out.println("cant divide by zero");
            }
        }
        return smallestVariable;
    }

    public void findAndSetPivotRow() {
        if (checkPivotColumn()) {
            pivotRow = findDepartingVariable();
        } else {
            System.out.println("cant be solved");
            System.exit(1);
        }
    }

    public double[] calculateNewRow() {
        double[] newRowTemp = new double[simplexTable.length];
        for (int i = 0; i < simplexTable.length; i++) {
            newRowTemp[i] = simplexTable[i][pivotRow] / simplexTable[pivotColumn][pivotRow];
        }
        return newRowTemp;
    }

    public void calculateAndUpdateWNewRow() {
        this.newRow = calculateNewRow();
        updateWNewRow();
    }

    /*
     * updates the table using newRow
     */
    public void updateWNewRow() {
        double rowEntry;
        for (int i = 0; i < simplexTable[0].length; i++) {
            if (i != pivotRow) {
                rowEntry = simplexTable[pivotColumn][i];
                for (int j = 0; j < simplexTable.length; j++) {
                    simplexTable[j][i] -= rowEntry * newRow[j];
                }
            }else{
                for (int j = 0; j < simplexTable.length; j++) {
                    simplexTable[j][i] = newRow[j];
                }
            }
        }
    }

    public double[][] getSimplexTable() {
        return simplexTable;
    }

    public void printTable() {
        for (int i = 0; i < simplexTable[0].length; i++) {
            for (int j = 0; j < simplexTable.length; j++) {
                System.out.printf("%.2f ", simplexTable[j][i]);
            }
            System.out.println("");
        }
    }
}
