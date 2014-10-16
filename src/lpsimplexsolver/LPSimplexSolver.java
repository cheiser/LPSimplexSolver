/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lpsimplexsolver;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattis
 */
public class LPSimplexSolver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LPSimplexSolver lp = new LPSimplexSolver();
        lp.solveSimplex();
    }
    
    /*
     * STEG:
     * 1. läs in vad som skall maximeras t.ex. 3x+5y
     * 2. läs in vilka begränsningar som skall gälla, t.ex. x >= 0 x+4y <= 15 osv...
     * 3. gör om input till annan input, t.ex. introducera slack variablar om det behövs.
     *      det kan se ut ungefär såhär: 3x+5y+0u+0v        x+4y+u = 15 osv....
     * 4. läs in det i en tabell
     * 5. hitta det minsta värdet i objektiv raden
     * 6. 
     */
    
    public Table solveSimplex(){
        Table table = initiateTable(); // initiate table
        table.printTable();
        while(!table.checkObjectiveRow()){ // check if all is positive(done)
            table.findAndSetEntryVariable();
            table.findAndSetPivotRow();
            table.calculateAndUpdateWNewRow();
            System.out.println("\n\n");
            table.printTable();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LPSimplexSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("\n\n");
        table.printTable();
        System.out.println("\n\n");
        table.printSolution();
        return table;
    }
    
    public Table solveSimplex(Table table){
        table.printTable();
        while(!table.checkObjectiveRow()){ // check if all is positive(done)
            table.findAndSetEntryVariable();
            table.findAndSetPivotRow();
            table.calculateAndUpdateWNewRow();
            System.out.println("\n\n");
            table.printTable();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LPSimplexSolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("\n\n");
        table.printTable();
        System.out.println("\n\n");
        table.printSolution();
        return table;
    }
    
    public Table initiateTable(){
        Table table = new Table(5, 3);
        table.setValueAt(0, 0, 1);
        table.setValueAt(1, 0, 1);
        table.setValueAt(2, 0, 1);
        table.setValueAt(3, 0, 0);
        table.setValueAt(4, 0, 4);
        table.setValueAt(0, 1, 1);
        table.setValueAt(1, 1, 3);
        table.setValueAt(2, 1, 0);
        table.setValueAt(3, 1, 1);
        table.setValueAt(4, 1, 6);
        table.setValueAt(0, 2, -3);
        table.setValueAt(1, 2, -5);
        table.setValueAt(2, 2, 0);
        table.setValueAt(3, 2, 0);
        table.setValueAt(4, 2, 0);
        return table;
    }
    
}
